package com.peihuo.entity;

import java.io.Serializable;

/**
 * Created by hb on 2017/9/7.
 */

public class WorkLine implements Serializable {
    private String pipeline;//生产线
    private String id;

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
}
