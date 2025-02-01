package dev.kofeychi.earthquake.impl;

import dev.kofeychi.earthquake.impl.Util.EnabledAffections;

import java.util.ArrayList;

public class PipelineCluster {
    public ArrayList<Pipeline> Pipelines = new ArrayList<>();
    public Pipeline RotationXPipeline;
    public Pipeline RotationYPipeline;
    public Pipeline RotationZPipeline;
    public Pipeline PositionXPipeline;
    public Pipeline PositionYPipeline;
    public Pipeline PositionZPipeline;
    public EnabledAffections affections;

    public static PipelineCluster NewPipelineCluster(Pipeline Line, EnabledAffections afect){
        return new PipelineCluster(Line,Line,Line,Line,Line,Line,afect);
    }
    public static PipelineCluster NewPipelineCluster(Pipeline RotationLine,Pipeline PositionLine, EnabledAffections afect){
        return new PipelineCluster(RotationLine,RotationLine,RotationLine,PositionLine,PositionLine,PositionLine,afect);
    }
    public static PipelineCluster NewPipelineCluster(
            Pipeline RotationX,
            Pipeline RotationY,
            Pipeline RotationZ,
            Pipeline PositionX,
            Pipeline PositionY,
            Pipeline PositionZ,
            EnabledAffections afect
    ){
        return new PipelineCluster(RotationX,RotationY,RotationZ,PositionX,PositionY,PositionZ,afect);
    }

    public PipelineCluster(
            Pipeline RX,
            Pipeline RY,
            Pipeline RZ,
            Pipeline PX,
            Pipeline PY,
            Pipeline PZ,
            EnabledAffections af
    ){
        affections = af;
        Pipeline IRX = new Pipeline(RX, af.RotX);
        Pipeline IRY = new Pipeline(RY, af.RotY);
        Pipeline IRZ = new Pipeline(RZ, af.RotZ);
        Pipeline IPX = new Pipeline(PX, af.PosX);
        Pipeline IPY = new Pipeline(PY, af.PosY);
        Pipeline IPZ = new Pipeline(PZ, af.PosZ);
        RotationXPipeline = IRX;Pipelines.add(IRX);
        RotationYPipeline = IRY;Pipelines.add(IRY);
        RotationZPipeline = IRZ;Pipelines.add(IRZ);
        PositionXPipeline = IPX;Pipelines.add(IPX);
        PositionYPipeline = IPY;Pipelines.add(IPY);
        PositionZPipeline = IPZ;Pipelines.add(IPZ);
    }

    @Override
    public String toString() {
        return "PipelineCluster{" +
                "Pipelines=" + Pipelines.toString() + '}';
    }
}
