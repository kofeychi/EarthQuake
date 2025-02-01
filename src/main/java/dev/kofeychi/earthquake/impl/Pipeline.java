package dev.kofeychi.earthquake.impl;

import dev.kofeychi.earthquake.impl.Util.EScreenShakeData;
import dev.kofeychi.earthquake.impl.Util.Easing;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.SimplexNoise;
import org.joml.Vector3f;

public class Pipeline {
    // DATA -------------------------------------------------------------------------------------
    public int duration,lifetime = 0;
    public float InIntensity,OutIntensity;
    public String InCurve = Easing.LINEAR.name;
    public String OutCurve = Easing.LINEAR.name;
    public String LinearCurve = Easing.LINEAR.name;
    public float PerlinIntensity=.1f,PerlinSpeed=.1f;
    public float PerlinX=0,PerlinY=0;
    public EScreenShakeData.EaseType EaseMode = EScreenShakeData.EaseType.LINEAR;
    public EScreenShakeData.RngType RngMode = EScreenShakeData.RngType.RANDOM;
    public boolean isAxisEnabled = true;
    public boolean isPositionedInstance = false;
    public Vec3d position = new Vec3d(0,0,0);
    public float falloffDistance = 0;
    public float maxDistance = 0;
    // DATA -------------------------------------------------------------------------------------

    public Pipeline setRngMode(EScreenShakeData.RngType rngMode) {RngMode = rngMode;return this;}
    public Pipeline setPerlinSpeed(float perlinSpeed) {PerlinSpeed = perlinSpeed;return this;}
    public Pipeline setPerlinIntensity(float perlinIntensity) {PerlinIntensity = perlinIntensity;return this;}
    public Pipeline setOutIntensity(float outIntensity) {OutIntensity = outIntensity;return this;}
    public Pipeline setOutCurve(String outCurve) {OutCurve = outCurve;return this;}
    public Pipeline setLinearCurve(String linearCurve) {LinearCurve = linearCurve;return this;}
    public Pipeline setInIntensity(float inIntensity) {InIntensity = inIntensity;return this;}
    public Pipeline setInCurve(String inCurve) {InCurve = inCurve;return this;}

    public Pipeline(Pipeline pipeline,Vec3d position,float falloffDistance,float maxDistance) {
        duration = pipeline.duration;
        InIntensity = pipeline.InIntensity;
        OutIntensity = pipeline.OutIntensity;
        InCurve = pipeline.InCurve;
        OutCurve = pipeline.OutCurve;
        LinearCurve = pipeline.LinearCurve;
        PerlinIntensity = pipeline.PerlinIntensity;
        PerlinSpeed = pipeline.PerlinSpeed;
        EaseMode = pipeline.EaseMode;
        RngMode = pipeline.RngMode;
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        isPositionedInstance = true;
    }

    public Pipeline(Pipeline pipeline, boolean axis){
        if (pipeline.isPositionedInstance) {
            isPositionedInstance = true;
            this.position = pipeline.position;
            this.falloffDistance = pipeline.falloffDistance;
            this.maxDistance = pipeline.maxDistance;
        }
        duration = pipeline.duration;
        InIntensity = pipeline.InIntensity;
        OutIntensity = pipeline.OutIntensity;
        InCurve = pipeline.InCurve;
        OutCurve = pipeline.OutCurve;
        LinearCurve = pipeline.LinearCurve;
        PerlinIntensity = pipeline.PerlinIntensity;
        PerlinSpeed = pipeline.PerlinSpeed;
        EaseMode = pipeline.EaseMode;
        RngMode = pipeline.RngMode;
        isAxisEnabled = axis;
    }
    public Pipeline(int duration, EScreenShakeData.EaseType EaseType, EScreenShakeData.RngType RngType,float inIntensity,float outIntensity){
        this.duration = duration;
        EaseMode = EaseType;
        RngMode = RngType;
        InIntensity = inIntensity;
        OutIntensity = outIntensity;
    }
    public Pipeline(int duration, EScreenShakeData.EaseType EaseType, EScreenShakeData.RngType RngType,float inIntensity,float outIntensity,Easing Easing4all){
        this.duration = duration;
        EaseMode = EaseType;
        RngMode = RngType;
        InCurve = Easing4all.name;
        InIntensity = inIntensity;
        OutIntensity = outIntensity;
    }
    public void CameraTick(){
        PerlinX += PerlinSpeed/100;
        if (PerlinX >= 500) {PerlinX = -500;PerlinY += PerlinSpeed/100;}
        if (PerlinY >= 500) {PerlinY = -500;}
    }
    public void Tick(){
        lifetime++;
    }
    public int getDuration() {
        return duration;
    }
    public float updateIntensity(Camera cam){
        if (isPositionedInstance){
            float intensity = upd(cam);
            float distance = (float) position.distanceTo(cam.getPos());
            if (distance > maxDistance) {
                return 0;
            }
            float distanceMultiplier = 1;
            if (distance > falloffDistance) {
                float remaining = maxDistance - falloffDistance;
                float current = distance - falloffDistance;
                distanceMultiplier = 1 - current / remaining;
            }
            Vector3f lookDirection = cam.getHorizontalPlane();
            Vec3d directionToScreenshake = position.subtract(cam.getPos()).normalize();
            float angle = Math.max(0, lookDirection.dot(new Vector3f((float) directionToScreenshake.x, (float) directionToScreenshake.y, (float) directionToScreenshake.z)));
            return ((intensity * distanceMultiplier) + (intensity * angle)) * 0.5f;
        } else {
            return upd(cam);
        }
    }

    public float upd(Camera camera) {
        if (!isAxisEnabled|lifetime >= duration) {
            return 0;
        }
        float percentage = (lifetime / (float) duration);
        if (EaseMode == EScreenShakeData.EaseType.INOUT) {
            if (percentage >= 0.5f) {
                return MathHelper.lerp(Easing.valueOf(OutCurve).ease(percentage - 0.5f, 0, 1, 0.5f), OutIntensity, InIntensity);
            } else {
                return MathHelper.lerp(Easing.valueOf(InCurve).ease(percentage, 0, 1, 0.5f), InIntensity, OutIntensity);
            }
        } else {
            return MathHelper.lerp(Easing.valueOf(LinearCurve).ease(percentage, 0, 1, 1), InIntensity, OutIntensity);
        }
    }
    public float updateRng(float intensity, Random random,float Offset){
        return isAxisEnabled ? (RngMode == EScreenShakeData.RngType.PERLIN ? ((SimplexNoise.noise(PerlinX+Offset,PerlinY+Offset)*PerlinIntensity)*intensity) : MathHelper.nextFloat(random,-intensity,intensity)) : 0;
    }


    @Override
    public String toString() {
        return "Pipeline{" +
                "duration=" + duration +
                ", lifetime=" + lifetime +
                ", InIntensity=" + InIntensity +
                ", OutIntensity=" + OutIntensity +
                ", InCurve='" + InCurve +
                ", OutCurve='" + OutCurve +
                ", LinearCurve='" + LinearCurve +
                ", PerlinIntensity=" + PerlinIntensity +
                ", PerlinSpeed=" + PerlinSpeed +
                ", PerlinX=" + PerlinX +
                ", PerlinY=" + PerlinY +
                ", EaseMode=" + EaseMode +
                ", RngMode=" + RngMode +
                ", isAxisEnabled=" + isAxisEnabled +
                ", isPositionedInstance=" + isPositionedInstance +
                ", position=" + position +
                ", falloffDistance=" + falloffDistance +
                ", maxDistance=" + maxDistance +
                '}';
    }
}
