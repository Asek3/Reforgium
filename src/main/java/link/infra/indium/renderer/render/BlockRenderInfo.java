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

import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraftforge.client.model.data.ModelData;

/**
 * Holds, manages and provides access to the block/world related state
 * needed by fallback and mesh consumers.
 *
 * <p>Exception: per-block position offsets are tracked in {@link ChunkRenderInfo}
 * so they can be applied together with chunk offsets.
 */
public class BlockRenderInfo {
	private final BlockColors blockColorMap = MinecraftClient.getInstance().getBlockColors();
	public final Random random = Random.create();
	public BlockRenderView blockView;
	public BlockPos blockPos;
	public BlockState blockState;
	public long seed;
	boolean defaultAo;
	RenderLayer defaultLayer;
	
	// Forge
	public ModelData modelData;
	public RenderLayer layer;

	public final Supplier<Random> randomSupplier = () -> {
		final Random result = random;
		long seed = this.seed;

		if (seed == -1L) {
			seed = blockState.getRenderingSeed(blockPos);
			this.seed = seed;
		}

		result.setSeed(seed);
		return result;
	};

	public void setBlockView(BlockRenderView blockView) {
		this.blockView = blockView;
	}

	public void prepareForBlock(BlockView level, BlockState blockState, BlockPos blockPos, boolean modelAO, ModelData modelData, RenderLayer layer) {
		this.blockPos = blockPos;
		this.blockState = blockState;
		this.modelData = modelData;
		this.layer = layer;
		// in the unlikely case seed actually matches this, we'll simply retrieve it more than one
		seed = -1L;
		defaultAo = modelAO && MinecraftClient.isAmbientOcclusionEnabled() && blockState.getLightEmission(level, blockPos) == 0;

		// TODO Check if corrrect logic
		defaultLayer = layer;
		//defaultLayer = RenderLayers.getBlockLayer(blockState);
	}

	public void release() {
		blockPos = null;
		blockState = null;
	}

	int blockColor(int colorIndex) {
		return 0xFF000000 | blockColorMap.getColor(blockState, blockView, blockPos, colorIndex);
	}

	boolean shouldDrawFace(Direction face) {
		return true;
	}

	RenderLayer effectiveRenderLayer(BlendMode blendMode) {
		return blendMode == BlendMode.DEFAULT ? this.defaultLayer : blendMode.blockRenderLayer;
	}
}
