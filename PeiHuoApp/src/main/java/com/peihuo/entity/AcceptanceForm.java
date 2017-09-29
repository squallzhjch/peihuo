package com.peihuo.entity;

import java.io.Serializable;

/**
 * Created by 123 on 2017/8/31.
 */

public class AcceptanceForm implements Serializable{
    private String code;//验收单号
    private String acceptanceState;//验收状态
    private int suitUniteProductCount;//合计验收数量
    private String batchCount;//批次
    private String transferPath;//配货路径
    private String customerId;//用户id
    private String startTime;//开始时间
    private String belongorderid;//订单ID
    private String pitposition;//坑位

    public String getPitposition() {
        return pitposition;
    }

    public void setPitposition(String pitposition) {
        this.pitposition = pitposition;
    }

    public String getBelongorderid() {
        return belongorderid;
    }

    public void setBelongorderid(String belongorderid) {
        this.belongorderid = belongorderid;
    }

    public String getTransferPath() {
        return transferPath;
    }

    public void setTransferPath(String transferPath) {
        this.transferPath = transferPath;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public String getAcceptanceState() {
        return acceptanceState;
    }

    public void setAcceptanceState(String acceptanceState) {
        this.acceptanceState = acceptanceState;
    }

    public int getSuitUniteProductCount() {
        return suitUniteProductCount;
    }

    public void setSuitUniteProductCount(int suitUniteProductCount) {
        this.suitUniteProductCount = suitUniteProductCount;
    }

    public String getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(String batchCount) {
        this.batchCount = batchCount;
    }
}
