package dev.kofeychi.earthquake.impl;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.function.ToDoubleFunction;

public class ScreenShakeHandler {
    public static ArrayList<ScreenShakeInstance> Instances = new ArrayList<>();
    public static Vector3f IRot=new Vector3f(),IPos=new Vector3f();
    public static Vector3f Rot=new Vector3f(),Pos=new Vector3f();

    public static void cameraTick(Camera camera, Entity entity){
        calculateIntensities(camera);
        calculateRandom(camera,entity);
        Instances.forEach(i -> i.CameraTick());
    }
    public static void Tick(){
        Instances.forEach(i -> i.Tick());
        Instances.removeIf(i -> i.lifetime >= i.duration);
    }
    public static void addInstance(ScreenShakeInstance instance){
        Instances.add(instance);
    }
    public static void calculateRandom(Camera camera,Entity entity){
        Random random = entity.getRandom();
        ToDoubleFunction<ScreenShakeInstance> rx = (i) -> i.Cluster.RotationXPipeline.updateRng(IRot.x,random,0);
        ToDoubleFunction<ScreenShakeInstance> ry = (i) -> i.Cluster.RotationYPipeline.updateRng(IRot.y,random,10);
        ToDoubleFunction<ScreenShakeInstance> rz = (i) -> i.Cluster.RotationZPipeline.updateRng(IRot.z,random,20);
        ToDoubleFunction<ScreenShakeInstance> px = (i) -> i.Cluster.PositionXPipeline.updateRng(IPos.x,random,30);
        ToDoubleFunction<ScreenShakeInstance> py = (i) -> i.Cluster.PositionYPipeline.updateRng(IPos.y,random,40);
        ToDoubleFunction<ScreenShakeInstance> pz = (i) -> i.Cluster.PositionZPipeline.updateRng(IPos.z,random,50);
        double RX = Instances.stream().mapToDouble(rx).sum();
        double RY = Instances.stream().mapToDouble(ry).sum();
        double RZ = Instances.stream().mapToDouble(rz).sum();
        double PX = Instances.stream().mapToDouble(px).sum();
        double PY = Instances.stream().mapToDouble(py).sum();
        double PZ = Instances.stream().mapToDouble(pz).sum();
        Rot = new Vector3f((float) RX, (float) RY, (float) RZ);
        Pos = new Vector3f((float) PX, (float) PY, (float) PZ);
    }
    public static void calculateIntensities(Camera camera){
        ToDoubleFunction<ScreenShakeInstance> rx = (i) -> i.Cluster.RotationXPipeline.updateIntensity(camera);
        double RX = Instances.stream().mapToDouble(rx).sum();//Math.pow(Instances.stream().mapToDouble(rx).sum(),curved(Instances.stream().mapToDouble(rx).sum()));

        ToDoubleFunction<ScreenShakeInstance> ry = (i) -> i.Cluster.RotationYPipeline.updateIntensity(camera);
        double RY = Instances.stream().mapToDouble(ry).sum();//Math.pow(Instances.stream().mapToDouble(ry).sum(),curved(Instances.stream().mapToDouble(ry).sum()));

        ToDoubleFunction<ScreenShakeInstance> rz = (i) -> i.Cluster.RotationZPipeline.updateIntensity(camera);
        double RZ = Instances.stream().mapToDouble(rz).sum();//Math.pow(Instances.stream().mapToDouble(rz).sum(),curved(Instances.stream().mapToDouble(rz).sum()));

        ToDoubleFunction<ScreenShakeInstance> px = (i) -> i.Cluster.PositionXPipeline.updateIntensity(camera);
        double PX = Instances.stream().mapToDouble(px).sum();//Math.pow(Instances.stream().mapToDouble(px).sum(),curved(Instances.stream().mapToDouble(px).sum()));

        ToDoubleFunction<ScreenShakeInstance> py = (i) -> i.Cluster.PositionYPipeline.updateIntensity(camera);
        double PY = Instances.stream().mapToDouble(py).sum();//Math.pow(Instances.stream().mapToDouble(py).sum(),curved(Instances.stream().mapToDouble(py).sum()));

        ToDoubleFunction<ScreenShakeInstance> pz = (i) -> i.Cluster.PositionZPipeline.updateIntensity(camera);
        double PZ = Instances.stream().mapToDouble(pz).sum();//Math.pow(Instances.stream().mapToDouble(pz).sum(),curved(Instances.stream().mapToDouble(pz).sum()));
        IRot = new Vector3f((float) Math.min(RX,SharedValues.Intensity), (float) Math.min(RY,SharedValues.Intensity), (float) Math.min(RZ,SharedValues.Intensity));
        IPos = new Vector3f((float) Math.min(PX,SharedValues.Intensity), (float) Math.min(PY,SharedValues.Intensity), (float) Math.min(PZ,SharedValues.Intensity));

    }
}
