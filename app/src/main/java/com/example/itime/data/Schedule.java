package com.example.itime.data;

import java.io.Serializable;

/**
 * Created by 谭小二 on 2019/11/20.
 */

public class Schedule implements Serializable {

    private String title;//标题
    private int coverResourceId;//图片
    private String remark;//备注
    private String deadline;//日期
    private String ddl_time;//时间

    public String getDdl_time() {
        return ddl_time;
    }

    public void setDdl_time(String ddl_time) {
        this.ddl_time = ddl_time;
    }


    public Schedule(String title, int coverResourceId, String remark, String deadline, String ddl_time) {
        this.title = title;
        this.coverResourceId = coverResourceId;
        this.remark = remark;
        this.deadline = deadline;
        this.ddl_time = ddl_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        this.coverResourceId = coverResourceId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

}