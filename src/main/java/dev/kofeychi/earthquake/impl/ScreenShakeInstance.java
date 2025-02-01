package dev.kofeychi.earthquake.impl;

public class ScreenShakeInstance {
    public int lifetime=0;
    public int duration=0;
    public PipelineCluster Cluster;
    public ScreenShakeInstance(PipelineCluster cluster){
        Cluster = cluster;
        duration = cluster.Pipelines.stream().mapToInt(Pipeline::getDuration).max().orElse(cluster.RotationXPipeline.duration);
    }
    public void Tick(){
        lifetime++;
        Cluster.Pipelines.forEach(Pipeline::Tick);
    }
    public void CameraTick(){
        Cluster.Pipelines.forEach(Pipeline::CameraTick);
    }

    @Override
    public String toString() {
        return "ScreenShakeInstance{" +
                "lifetime=" + lifetime +
                ", duration=" + duration +
                ", Cluster=" + Cluster +
                '}';
    }
}
