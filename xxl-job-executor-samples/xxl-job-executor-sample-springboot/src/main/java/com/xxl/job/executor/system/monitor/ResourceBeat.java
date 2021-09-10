package com.xxl.job.executor.system.monitor;

/**
 * @author zhangsk01
 */
public class ResourceBeat {

    private int threadCount;
    private int cpuCount;
    private double runningCpu;
    private double remainsCpu;
    private double totalMemory;
    private double remainsMemory;
    private double usedMemory;
    private long createdTime;

    private String address;

    ResourceBeat(int threadCount, int cpuCount, double runningCpu, double remainsCpu, double totalMemory, double remainsMemory, double usedMemory, String address) {
        this.threadCount = threadCount;
        this.cpuCount = cpuCount;
        this.runningCpu = runningCpu;
        this.remainsCpu = remainsCpu;
        this.totalMemory = totalMemory;
        this.remainsMemory = remainsMemory;
        this.usedMemory = usedMemory;
        this.createdTime = System.currentTimeMillis();
        this.address = address;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(int cpuCount) {
        this.cpuCount = cpuCount;
    }

    public double getRunningCpu() {
        return runningCpu;
    }

    public void setRunningCpu(double runningCpu) {
        this.runningCpu = runningCpu;
    }

    public double getRemainsCpu() {
        return remainsCpu;
    }

    public void setRemainsCpu(double remainsCpu) {
        this.remainsCpu = remainsCpu;
    }

    public double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public double getRemainsMemory() {
        return remainsMemory;
    }

    public void setRemainsMemory(double remainsMemory) {
        this.remainsMemory = remainsMemory;
    }

    public double getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(double usedMemory) {
        this.usedMemory = usedMemory;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
