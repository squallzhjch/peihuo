package com.peihuo.entity;

import java.io.Serializable;

/**
 * Created by hb on 2017/9/7.
 */

public class WorkLine implements Serializable {
    private String pipeline;//生产线
    private String id;
    private int holenum;//坑位总数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }

    public int getHolenum() {
        return holenum;
    }

    public void setHolenum(int holenum) {
        this.holenum = holenum;
    }
}
