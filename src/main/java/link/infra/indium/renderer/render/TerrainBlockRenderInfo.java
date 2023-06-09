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

package link.infra.indium.renderer.render;

import me.jellysquid.mods.sodium.client.render.occlusion.BlockOcclusionCache;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraftforge.client.model.data.ModelData;

public class TerrainBlockRenderInfo extends BlockRenderInfo {
	protected final BlockOcclusionCache blockOcclusionCache;

	private int cullCompletionFlags;
	private int cullResultFlags;

	public TerrainBlockRenderInfo(BlockOcclusionCache blockOcclusionCache) {
		this.blockOcclusionCache = blockOcclusionCache;
	}

	@Override
	public void prepareForBlock(BlockView level, BlockState blockState, BlockPos blockPos, boolean modelAO, ModelData modelData, RenderLayer layer) {
		super.prepareForBlock(level, blockState, blockPos, modelAO, modelData, layer);
		cullCompletionFlags = 0;
		cullResultFlags = 0;
	}

	@Override
	boolean shouldDrawFace(Direction face) {
		if (face == null) {
			return true;
		}

		final int mask = 1 << face.getId();

		if ((cullCompletionFlags & mask) == 0) {
			cullCompletionFlags |= mask;

			if (blockOcclusionCache.shouldDrawSide(blockState, blockView, blockPos, face)) {
				cullResultFlags |= mask;
				return true;
			} else {
				return false;
			}
		} else {
			return (cullResultFlags & mask) != 0;
		}
	}
}
