package com.peihuo.entity;

import java.io.Serializable;

/**
 * Created by 123 on 2017/8/31.
 * 分拣单详情
 */

public class AcceptanceInfo implements Serializable{
    /**
     * 验收单号
     */
    private String is_suit;//是否套装
    private String handlingOrderCode;//加工单号
    private String productCode;//货品编码
    private String proName;//货品名称
    private String groupName;//货品名称
    private String proUnite;//单位
    private int useCount;//使用数量
    private String prostandard;//规格

    public String getProstandard() {
        return prostandard;
    }

    public void setProstandard(String prostandard) {
        this.prostandard = prostandard;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getHandlingOrderCode() {
        return handlingOrderCode;
    }

    public String getIs_suit() {
        return is_suit;
    }

    public void setIs_suit(String is_suit) {
        this.is_suit = is_suit;
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

}
