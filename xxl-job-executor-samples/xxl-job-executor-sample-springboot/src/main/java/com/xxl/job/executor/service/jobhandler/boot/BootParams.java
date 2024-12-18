package com.xxl.job.executor.service.jobhandler.boot;

import java.util.List;

public class BootParams {
    private int jobId;
    private int groupId;
    private int vcore;
    private int memory;
    private List<BootCommand> containers;

    public BootParams(int groupId) {
        this.groupId = groupId;
    }

    public int getVcore() {
        return vcore;
    }

    public void setVcore(int vcore) {
        this.vcore = vcore;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public List<BootCommand> getContainers() {
        return containers;
    }

    public void setContainers(List<BootCommand> containers) {
        this.containers = containers;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
