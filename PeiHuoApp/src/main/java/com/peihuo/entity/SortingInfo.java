package com.peihuo.entity;

import java.io.Serializable;

/**
 * Created by 123 on 2017/8/31.
 * 分拣单详情
 */

public class SortingInfo implements Serializable{
    private String acceptanceCode;//验收单号
    private String belongProductCode;//所属货品编码
    private String is_suit;//是否套装
    private String handlingOrderCode;//加工单号
    private String productCode;//货品编码
    private String proName;//货品名称
    private int orderCount;//订单数量
    private String proStandard;//规格
    private String proUnite;//单位
    private int useCount;//使用数量
    private int wasteCount;//损耗数量
    private String is_complete;//是否完成
    private String responsibleHuman;//责任人
    private String batchNo;//批次
    private String aid;//验收详情表主键id
    private String streamNo;//流水号
    private String pitPosition;//坑位
    private String assembleLineNo;//流水线号
    private String bookOrderNo;//订单号

    public String getAcceptanceCode() {
        return acceptanceCode;
    }

    public void setAcceptanceCode(String acceptanceCode) {
        this.acceptanceCode = acceptanceCode;
    }

    public String getBelongProductCode() {
        return belongProductCode;
    }

    public void setBelongProductCode(String belongProductCode) {
        this.belongProductCode = belongProductCode;
    }

    public String getIs_suit() {
        return is_suit;
    }

    public void setIs_suit(String is_suit) {
        this.is_suit = is_suit;
    }

    public String getHandlingOrderCode() {
        return handlingOrderCode;
    }

    public void setHandlingOrderCode(String handlingOrderCode) {
        this.handlingOrderCode = handlingOrderCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public String getProStandard() {
        return proStandard;
    }

    public void setProStandard(String proStandard) {
        this.proStandard = proStandard;
    }

    public String getProUnite() {
        return proUnite;
    }

    public void setProUnite(String proUnite) {
        this.proUnite = proUnite;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public int getWasteCount() {
        return wasteCount;
    }

    public void setWasteCount(int wasteCount) {
        this.wasteCount = wasteCount;
    }

    public String getIs_complete() {
        return is_complete;
    }

    public void setIs_complete(String is_complete) {
        this.is_complete = is_complete;
    }

    public String getResponsibleHuman() {
        return responsibleHuman;
    }

    public void setResponsibleHuman(String responsibleHuman) {
        this.responsibleHuman = responsibleHuman;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getStreamNo() {
        return streamNo;
    }

    public void setStreamNo(String streamNo) {
        this.streamNo = streamNo;
    }

    public String getPitPosition() {
        return pitPosition;
    }

    public void setPitPosition(String pitPosition) {
        this.pitPosition = pitPosition;
    }

    public String getAssembleLineNo() {
        return assembleLineNo;
    }

    public void setAssembleLineNo(String assembleLineNo) {
        this.assembleLineNo = assembleLineNo;
    }

    public String getBookOrderNo() {
        return bookOrderNo;
    }

    public void setBookOrderNo(String bookOrderNo) {
        this.bookOrderNo = bookOrderNo;
    }
}
