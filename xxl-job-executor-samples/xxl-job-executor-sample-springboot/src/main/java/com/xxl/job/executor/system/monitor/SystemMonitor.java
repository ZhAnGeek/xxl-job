package com.xxl.job.executor.system.monitor;

import com.sun.management.OperatingSystemMXBean;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.executor.core.config.XxlJobConfig;
import com.xxl.job.executor.storage.ResourceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;

/**
 * @author zhangsk01
 */
@Component
public class SystemMonitor {

    @Autowired
    private ResourceStorage storage;

    @Autowired
    private XxlJobSpringExecutor executor;

    @Scheduled(fixedRate = 1000)
    public ResourceBeat getResourceBeat() {

        SystemInfo systemInfo = new SystemInfo();

        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // 总的物理内存
        double totalMemorySize = scaleRemains(osmxb.getTotalPhysicalMemorySize() / 1024.0 / 1024);
        // 剩余的物理内存
        double freePhysicalMemorySize = scaleRemains(osmxb.getFreePhysicalMemorySize() / 1024.0 / 1024);
        // 已使用的物理内存
        double usedMemory = scaleRemains((osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / 1024.0 / 1024);

        double runningCpu = 0;
        while (runningCpu == 0) {
            try {
                runningCpu = scaleRemains(CPULinuxProcessHelper.getTotalCpuRate());
            } catch (Exception ignored) {
            }
        }

        int threadCount = getThreadCount();
        int cpuCount = osmxb.getAvailableProcessors();
        double remainsCpu = scaleRemains(cpuCount - runningCpu / 100.0);

        ResourceBeat resourceBeat = new ResourceBeat(threadCount, cpuCount, runningCpu, remainsCpu, totalMemorySize, freePhysicalMemorySize, usedMemory, executor.getAddress());
        System.out.println(String.format(
                " thread count: %s \n" +
                " cpu count: %s \n" +
                " cpu used rate: %s percent \n" +
                " remains cpu: %s \n" +
                " memory total: %s MB \n" +
                " memory remains: %s MB \n" +
                " memory used: %s MB \n", resourceBeat.getThreadCount(), resourceBeat.getCpuCount(), resourceBeat.getRunningCpu(), resourceBeat.getRemainsCpu(), resourceBeat.getTotalMemory(), resourceBeat.getRemainsMemory(), resourceBeat.getUsedMemory()));

        storage.updateResource(resourceBeat);
        return resourceBeat;
    }

    int getThreadCount() {
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent()) {

        }
        return parentThread.activeCount();
    }

    double scaleRemains (double number) {
        return new BigDecimal(number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
