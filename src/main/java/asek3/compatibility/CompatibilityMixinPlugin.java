package asek3.compatibility;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.minecraftforge.fml.loading.FMLLoader;

public class CompatibilityMixinPlugin implements IMixinConfigPlugin {

	@Override
	public void onLoad(String mixinPackage) { }

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		int secondDotIndex = mixinClassName.lastIndexOf('.');
		int firstDotIndex = mixinClassName.lastIndexOf('.', secondDotIndex - 1);

		String modid = mixinClassName.substring(firstDotIndex + 1, secondDotIndex);
		
		return FMLLoader.getLoadingModList().getModFileById(modid) != null;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
}