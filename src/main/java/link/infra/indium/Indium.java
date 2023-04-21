/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package link.infra.indium;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import link.infra.indium.other.SpriteFinderCache;
import link.infra.indium.renderer.ReforgiumRenderer;
import link.infra.indium.renderer.aocalc.AoConfig;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.impl.client.indigo.Indigo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("reforgium")
public class Indium {
	public static final boolean ALWAYS_TESSELLATE_INDIUM;
	public static final AoConfig AMBIENT_OCCLUSION_MODE;
	public static final boolean FIX_EXTERIOR_VERTEX_LIGHTING;

	public static final Logger LOGGER = LogUtils.getLogger();

	private static boolean asBoolean(String property, boolean defValue) {
		Boolean bool = asBool(property);
		return bool == null ? defValue : bool.booleanValue();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T extends Enum> T asEnum(String property, T defValue) {
		if (property == null || property.isEmpty()) {
			return defValue;
		} else {
			for (Enum obj : defValue.getClass().getEnumConstants()) {
				if (property.equalsIgnoreCase(obj.name())) {
					//noinspection unchecked
					return (T) obj;
				}
			}

			return defValue;
		}
	}

	private static Boolean asBool(String property) {
		if (property == null || property.isEmpty()) {
			return null;
		} else {
			switch (property.toLowerCase(Locale.ROOT)) {
			case "true":
				return true;
			case "false":
				return false;
			case "auto":
			default:
				return null;
			}
		}
	}

	static {
		Path configFile = FMLPaths.CONFIGDIR.get().resolve("reforgium-renderer.properties");
		Properties properties = new Properties();

		if (Files.exists(configFile)) {
			try (InputStream stream = Files.newInputStream(configFile)) {
				properties.load(stream);
			} catch (IOException e) {
				LOGGER.warn("[Reforgium] Could not read property file '" + configFile.toAbsolutePath() + "'", e);
			}
		}

		ALWAYS_TESSELLATE_INDIUM = asBoolean((String) properties.computeIfAbsent("always-tesselate-blocks", (a) -> "auto"), false);
		AMBIENT_OCCLUSION_MODE = asEnum((String) properties.computeIfAbsent("ambient-occlusion-mode", (a) -> "auto"), AoConfig.ENHANCED);
		FIX_EXTERIOR_VERTEX_LIGHTING = asBoolean((String) properties.computeIfAbsent("fix-exterior-vertex-lighting", (a) -> "auto"), true);

		try (OutputStream stream = Files.newOutputStream(configFile)) {
			properties.store(stream, "Reforgium properties file");
		} catch (IOException e) {
			LOGGER.warn("[Reforgium] Could not store property file '" + configFile.toAbsolutePath() + "'", e);
		}
	}
	
	public static boolean rubidiumLoaded = FMLLoader.getLoadingModList().getModFileById("rubidium") != null;

	public Indium() {
		try {
			if(rubidiumLoaded)
				RendererAccess.INSTANCE.registerRenderer(ReforgiumRenderer.INSTANCE);
			else
				Indigo.init();
		} catch (UnsupportedOperationException e) {
			throw e;
		}
		
		if(rubidiumLoaded)
			MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void addListener(AddReloadListenerEvent event) {
		event.addListener(SpriteFinderCache.ReloadListener.INSTANCE);
	}
}
