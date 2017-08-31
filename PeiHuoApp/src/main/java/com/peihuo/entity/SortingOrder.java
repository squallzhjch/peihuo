package com.peihuo.entity;

/**
 * Created by 123 on 2017/8/31.
 */

public class SortingOrder {
    private String code;//验收单号
    private String id;
    private String startTime;//开单日期时间
    private String endTime;//结束日期时间
    private int totalProducts;//货品总数
    private int unitProductCount;//单品数量
    private int suitCount;//套装菜数量
    private String acceptanceState;//验收状态
    private int suitUniteProductCount;//合计验收数量
    private String acceptanceHuMan;//验收人
    private String batchCount;//批次

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getUnitProductCount() {
        return unitProductCount;
    }

    public void setUnitProductCount(int unitProductCount) {
        this.unitProductCount = unitProductCount;
    }

    public int getSuitCount() {
        return suitCount;
    }

    public void setSuitCount(int suitCount) {
        this.suitCount = suitCount;
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

    public String getAcceptanceHuMan() {
        return acceptanceHuMan;
    }

    public void setAcceptanceHuMan(String acceptanceHuMan) {
        this.acceptanceHuMan = acceptanceHuMan;
    }

    public String getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(String batchCount) {
        this.batchCount = batchCount;
    }
}
