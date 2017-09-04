package com.peihuo.entity;

import java.io.Serializable;

/**
 * Created by 123 on 2017/8/31.
 */

public class SortingForm implements Serializable{
    private String code;//验收单号
    private String acceptanceState;//状态 0：等待分拣  1：等待验收  2：验收通过  3：验收未通过
    private String batchCount;//批次
    private String assemblelineno;//流水
    private String pitposition;//坑位
    private String customerId;//客户Id
    private String belongorderid;//订单id

    public String getBelongorderid() {
        return belongorderid;
    }

    public void setBelongorderid(String belongorderid) {
        this.belongorderid = belongorderid;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAcceptanceState() {
        return acceptanceState;
    }

    public void setAcceptanceState(String acceptanceState) {
        this.acceptanceState = acceptanceState;
    }

    public String getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(String batchCount) {
        this.batchCount = batchCount;
    }

    public String getAssemblelineno() {
        return assemblelineno;
    }

    public void setAssemblelineno(String assemblelineno) {
        this.assemblelineno = assemblelineno;
    }

    public String getPitposition() {
        return pitposition;
    }

    public void setPitposition(String pitposition) {
        this.pitposition = pitposition;
    }

}
