package anticope.rejects.mixin;

import anticope.rejects.modules.Rendering;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5EarsLayer.class)
public class Deadmau5FeatureRendererMixin {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    private void onRender(com.mojang.blaze3d.vertex.PoseStack matrixStack, MultiBufferSource bufferSource, int light,
            PlayerRenderState renderState, float limbAngle, float limbDistance, CallbackInfo ci) {
        // Check if Modules system is initialized
        if (Modules.get() != null) {
            Rendering renderingModule = Modules.get().get(Rendering.class);
            if (renderingModule != null && renderingModule.deadmau5EarsEnabled()) {
                // Allow rendering by not canceling
                return;
            }
        }

        // Default vanilla behavior: only render for "deadmau5"
        if (renderState.name != null) {
            String playerName = renderState.name;
            if (!playerName.equals("deadmau5")) {
                ci.cancel();
            }
        } else {
            ci.cancel();
        }
    }
}
