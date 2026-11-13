package nz.jordy.donutsmp.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Blocks the F-key swap when the main-hand item isn't a totem. */
@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientCommonNetworkHandlerMixin {
    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void blockOffhandSwap(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof PlayerActionC2SPacket p
                && p.getAction() == PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND) {
            var player = MinecraftClient.getInstance().player;
            if (player == null) return;
            ItemStack main = player.getMainHandStack();
            if (!main.isEmpty() && !main.isOf(Items.TOTEM_OF_UNDYING)) ci.cancel();
        }
    }
}
