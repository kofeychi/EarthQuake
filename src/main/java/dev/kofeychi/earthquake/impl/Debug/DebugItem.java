package dev.kofeychi.earthquake.impl.Debug;

import dev.kofeychi.earthquake.impl.Pipeline;
import dev.kofeychi.earthquake.impl.PipelineCluster;
import dev.kofeychi.earthquake.impl.ScreenShakeHandler;
import dev.kofeychi.earthquake.impl.ScreenShakeInstance;
import dev.kofeychi.earthquake.impl.Util.EScreenShakeData;
import dev.kofeychi.earthquake.impl.Util.Easing;
import dev.kofeychi.earthquake.impl.Util.EnabledAffections;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DebugItem extends Item {
    public DebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Pipeline pl = new Pipeline(
                        100,
                        EScreenShakeData.EaseType.INOUT,
                        EScreenShakeData.RngType.PERLIN,
                        1f,
                        0,
                        Easing.QUAD_IN
        );
        pl.PerlinIntensity=.7f;
        pl.PerlinSpeed=4f;
        if (world.isClient()) {
            if (!user.isSneaking()) {
                ScreenShakeHandler.addInstance(
                        new ScreenShakeInstance(
                                PipelineCluster.NewPipelineCluster(pl,
                                        new EnabledAffections(
                                                true,
                                                true,
                                                true,
                                                true,
                                                true,
                                                true
                                        )
                                )
                        )
                );
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
