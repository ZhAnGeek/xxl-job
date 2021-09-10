package com.xxl.job.executor.service.jobhandler.boot;

import com.google.gson.Gson;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.executor.model.XxlJobGroup;
import com.xxl.job.executor.model.XxlJobResource;
import com.xxl.job.executor.storage.JobStorage;
import com.xxl.job.executor.storage.ResourceStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BootJob {
    /**
     * 1、简单任务示例（Bean模式）
     */
    @Autowired
    private JobStorage jobStorage;

    @Autowired
    private ResourceStorage resourceStorage;

    @Autowired
    private BootRouter bootRouter;

    @XxlJob("bootJobHandler")
    public void execute() throws Exception {
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("booting start, params {}", params);

        BootParams bootParams;
        try {
            bootParams = new Gson().fromJson(params, BootParams.class);
        } catch (Exception e) {
            XxlJobHelper.log(new Throwable(String.format("can not parse params %s", e)));
            throw new Exception(e);
        }

        XxlJobGroup jobGroup = jobStorage.getJobGroup(bootParams.getGroupId());
        if (CollectionUtils.isEmpty(jobGroup.getRegistryList())) {
            throw new Exception("empty registry");
        }

        List<XxlJobResource> jobResources = resourceStorage.getResourceList();
        double totalMemory = jobResources.stream().mapToDouble(XxlJobResource::getRemainsMemory).sum();
        double totalCpu = jobResources.stream().mapToDouble(XxlJobResource::getRemainsCpu).sum();
        double totalMemoryNeeds = bootParams.getMemory() * bootParams.getCmd().size();
        double totalCpuNeeds = bootParams.getVcore() * bootParams.getCmd().size();

        if (totalMemory < totalMemoryNeeds) {
            throw new Exception(String.format("total limit resource, total memory is %s, needs %s", totalMemory, totalMemoryNeeds));
        }

        if (totalCpu < totalCpuNeeds) {
            throw new Exception(String.format("total limit resource, total cpu is %s, needs %s", totalCpu, totalCpuNeeds));
        }

        List<XxlJobResource> currentJobResources = jobResources.stream().filter(jr -> jobGroup.getRegistryList().contains(jr.getAddress())).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(currentJobResources)) {
            throw new Exception("empty resource");
        }

        List<XxlJobResource> availableJobResources = jobResources.stream().filter(jr -> jr.getRemainsCpu() > bootParams.getVcore() && jr.getRemainsMemory() > bootParams.getMemory()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(availableJobResources)) {
            bootRouter.route(jobResources, bootParams);
        } else {
            throw new Exception(String.format("limit resource, resource list %s %s", new Gson().toJson(jobResources), new Gson().toJson(bootParams)));
        }

        XxlJobHelper.log("booting end, params {}", params);
    }
}
