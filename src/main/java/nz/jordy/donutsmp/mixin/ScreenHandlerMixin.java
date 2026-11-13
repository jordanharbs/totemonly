package nz.jordy.donutsmp.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Blocks inventory actions that would put a non-totem into the offhand slot. */
@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    private static final int OFFHAND_SLOT = 45; // PlayerScreenHandler offhand slot index

    @Shadow public abstract ItemStack getCursorStack();
    @Shadow public abstract net.minecraft.screen.slot.Slot getSlot(int index);

    private static boolean bad(ItemStack s) {
        return !s.isEmpty() && !s.isOf(Items.TOTEM_OF_UNDYING);
    }

    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
    private void totemOnlyOffhand(int slotIndex, int button, SlotActionType type, PlayerEntity player, CallbackInfo ci) {
        if (!((Object) this instanceof PlayerScreenHandler)) return;
        if (slotIndex < 0) return;

        switch (type) {
            case PICKUP, QUICK_CRAFT -> {
                // placing the cursor stack into the offhand slot
                if (slotIndex == OFFHAND_SLOT && bad(getCursorStack())) ci.cancel();
            }
            case SWAP -> {
                // number key onto the offhand slot
                if (slotIndex == OFFHAND_SLOT && button >= 0 && button < 9
                        && bad(player.getInventory().getStack(button))) ci.cancel();
                // offhand (F) key while hovering a slot
                if (button == 40 && bad(getSlot(slotIndex).getStack())) ci.cancel();
            }
            case QUICK_MOVE -> {
                // shift-click auto-equipping a shield into the offhand
                if (getSlot(slotIndex).getStack().isOf(Items.SHIELD)) ci.cancel();
            }
            default -> {}
        }
    }
}
