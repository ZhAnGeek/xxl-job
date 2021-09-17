package com.xxl.job.executor.service.jobhandler.boot;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.executor.core.config.XxlJobConfig;
import com.xxl.job.executor.model.XxlJobResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangsk01
 */
@Component
public class BootRouter {

    @Autowired
    private XxlJobConfig config;

    public void route(List<XxlJobResource> resources, BootParams params) {
        double maxRemainsMemory = resources.stream().mapToDouble(XxlJobResource::getTotalMemory).max().orElse(16 * 1024);
        List<XxlJobResource> reserves = resources.stream()
                .filter(jr -> jr.getTotalMemory() == maxRemainsMemory)
                .collect(Collectors.toList());
        List<XxlJobResource> nonReserves = resources.stream()
                .filter(jr -> jr.getTotalMemory() != maxRemainsMemory)
                .collect(Collectors.toList());
        List<String> cmds = params.getContainers().stream().map(BootCommand::getCmd).collect(Collectors.toList());

        int cmdIndex = 0;
        // 遍历普通机器提交处理
        while (!CollectionUtils.isEmpty(nonReserves) && cmdIndex < params.getContainers().size()) {
            XxlJobResource resource = nonReserves.stream().findFirst().orElse(null);
            if (resource == null) {
                break;
            }
            if (resource.getRemainsMemory()  < params.getMemory()) {
                nonReserves.remove(resource);
                continue;
            }

            remoteExecute(resource.getAddress(), cmds.get(cmdIndex++), params.getJobId());
            resource.setRemainsMemory(resource.getRemainsMemory() - params.getMemory());
        }

        // 普通机器无法处理，提交给大机器
        int reserveIndex = 0;
        while (!CollectionUtils.isEmpty(reserves) && cmdIndex < params.getContainers().size()) {
            XxlJobResource resource = reserves.get(reserveIndex++ % reserves.size());
            remoteExecute(resource.getAddress(), cmds.get(cmdIndex++), params.getJobId());
        }
    }

    private void remoteExecute(String address, String cmd, int jobId) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("id", jobId);
        map.add("executorParam", cmd);
        map.add("addressList", address);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
        template.postForEntity(config.getAdminAddress() + "/jobinfo/trigger", entity, String.class);

        XxlJobHelper.log("route job {} to node {}", cmd, address);
    }
}
