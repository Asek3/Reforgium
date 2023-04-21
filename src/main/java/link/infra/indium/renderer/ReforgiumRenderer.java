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

package link.infra.indium.renderer;

import java.util.HashMap;

import link.infra.indium.renderer.mesh.MeshBuilderImpl;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.minecraft.util.Identifier;

// Renamed class cuz Debug HUD takes class name
public class ReforgiumRenderer implements Renderer {
	public static final ReforgiumRenderer INSTANCE = new ReforgiumRenderer();

	public static final RenderMaterialImpl.Value MATERIAL_STANDARD = (RenderMaterialImpl.Value) INSTANCE.materialFinder().find();

	static {
		INSTANCE.registerMaterial(RenderMaterial.MATERIAL_STANDARD, MATERIAL_STANDARD);
	}

	private final HashMap<Identifier, RenderMaterial> materialMap = new HashMap<>();

	private ReforgiumRenderer() { }

	@Override
	public MeshBuilder meshBuilder() {
		return new MeshBuilderImpl();
	}

	@Override
	public MaterialFinder materialFinder() {
		return new RenderMaterialImpl.Finder();
	}

	@Override
	public RenderMaterial materialById(Identifier id) {
		return materialMap.get(id);
	}

	@Override
	public boolean registerMaterial(Identifier id, RenderMaterial material) {
		if (materialMap.containsKey(id)) return false;

		// cast to prevent acceptance of impostor implementations
		materialMap.put(id, material);
		return true;
	}
}
