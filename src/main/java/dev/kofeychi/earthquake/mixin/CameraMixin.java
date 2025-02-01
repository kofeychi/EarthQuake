package dev.kofeychi.earthquake.mixin;

import dev.kofeychi.earthquake.impl.ScreenShakeHandler;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(at = @At(value = "HEAD"),method = "setRotation",cancellable = true)
    public void cart$setRotation(float yaw, float pitch, CallbackInfo info){
        Camera cam = ((Camera)(Object)this);
        cam.pitch = pitch;
        cam.yaw = yaw;
        cam.rotation.rotationYXZ((3.1415927F - yaw * 0.017453292F)+ ScreenShakeHandler.Rot.x, (-pitch * 0.017453292F)+ScreenShakeHandler.Rot.y, (0f)+ScreenShakeHandler.Rot.z);
        cam.HORIZONTAL.rotate(cam.rotation, cam.horizontalPlane);
        cam.VERTICAL.rotate(cam.rotation, cam.verticalPlane);
        cam.DIAGONAL.rotate(cam.rotation, cam.diagonalPlane);
        info.cancel();
    }
    @Inject(at = @At(value = "HEAD"),method = "update",cancellable = true)
    public void cart$update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info){
        Camera cam = ((Camera)(Object)this);
        ScreenShakeHandler.cameraTick(cam,focusedEntity);
    }
    @Inject(at = @At(value = "TAIL"),method = "update",cancellable = true)
    public void cart$update$later(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta,CallbackInfo info){
        Camera cam = ((Camera)(Object)this);
        Vec3d v = cam.getPos();
        cam.setPos(new Vec3d(v.x+ScreenShakeHandler.Pos.x,v.y+ScreenShakeHandler.Pos.y,v.z+ScreenShakeHandler.Pos.z));
    }
}
