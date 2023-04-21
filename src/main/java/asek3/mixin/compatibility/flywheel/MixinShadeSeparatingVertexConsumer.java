package asek3.mixin.compatibility.flywheel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.render.VertexConsumer;

@Pseudo
@Mixin(targets = "com.jozufozu.flywheel.core.model.ShadeSeparatingVertexConsumer")
public class MixinShadeSeparatingVertexConsumer implements VertexConsumer {

	// Asek3 - ik that it's incorrect logic and breaks models based on FRAPI, but fully fix will take much more effort
	@Shadow
	protected VertexConsumer shadedConsumer;
	
	@Override
	public VertexConsumer color(int red, int green, int blue, int alpha) {
		shadedConsumer.color(red, green, blue, alpha);
		return this;
	}

	@Override
	public void fixedColor(int red, int green, int blue, int alpha) {
		shadedConsumer.fixedColor(red, green, blue, alpha);
	}

	@Override
	public VertexConsumer light(int u, int v) {
		shadedConsumer.light(u, v);
		return this;
	}

	@Override
	public void next() {
		shadedConsumer.next();
	}

	@Override
	public VertexConsumer normal(float x, float y, float z) {
		shadedConsumer.normal(x, y, z);
		return this;
	}

	@Override
	public VertexConsumer overlay(int u, int v) {
		shadedConsumer.overlay(u, v);
		return this;
	}

	@Override
	public VertexConsumer texture(float u, float v) {
		shadedConsumer.texture(u, v);
		return this;
	}

	@Override
	public void unfixColor() {
		shadedConsumer.unfixColor();
	}

	@Override
	public VertexConsumer vertex(double x, double y, double z) {
		shadedConsumer.vertex(x, y, z);
		return this;
	}

}
