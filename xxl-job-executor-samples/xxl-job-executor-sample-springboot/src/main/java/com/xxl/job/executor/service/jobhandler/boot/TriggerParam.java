package com.xxl.job.executor.service.jobhandler.boot;

/**
 * @author zhangsk01
 */
public class TriggerParam {
    private Integer id;
    private String executorParam;
    private String addressList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }
}
