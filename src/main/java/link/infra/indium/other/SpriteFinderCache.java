package link.infra.indium.other;

import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

/**
 * Cache SpriteFinders for maximum efficiency.
 *
 * <p><b>This class should not be used during a resource reload</b>, as returned SpriteFinders may be null or outdated.
 */
public class SpriteFinderCache {
	private static SpriteFinder blockAtlasSpriteFinder;

	public static SpriteFinder forBlockAtlas() {
		return blockAtlasSpriteFinder;
	}

	public static class ReloadListener implements SynchronousResourceReloader {
		public static final Identifier ID = new Identifier("indium", "sprite_finder_cache");
		public static final ReloadListener INSTANCE = new ReloadListener();

		private ReloadListener() {
		}

		// BakedModelManager#getAtlas only returns correct results after the BakedModelManager is done reloading
		@Override
		public void reload(ResourceManager manager) {
			BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();
			blockAtlasSpriteFinder = SpriteFinder.get(modelManager.getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE));
		}
	}
}
