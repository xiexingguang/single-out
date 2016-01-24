package com.ec.singleOut.entity;

import java.util.Date;

/**
 * Created by jasshine_xxg on 2016/1/2.
 */
public class TaskEntity {

    private int id;
    private String taskcode;
    private String taskName;
    private String exectime;
    private Date lastStartDate;
    private Date lastEndDate;
    private String starting; //是否正在执行
    private String taskPath;
    private int frequence;
    private String taskType;
    private String flag;
    private String createUser;
    private String createDate;
    private String updateUser;
    private String updateDate;

    public int getId() {
        return id;
    }

    public TaskEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public TaskEntity setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public TaskEntity setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public TaskEntity setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getCreateDate() {
        return createDate;
    }

    public TaskEntity setCreateDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getFlag() {
        return flag;
    }

    public TaskEntity setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public String getTaskType() {
        return taskType;
    }

    public TaskEntity setTaskType(String taskType) {
        this.taskType = taskType;
        return this;
    }

    public int getFrequence() {
        return frequence;
    }

    public TaskEntity setFrequence(int frequence) {
        this.frequence = frequence;
        return this;
    }

    public String getTaskPath() {
        return taskPath;
    }

    public TaskEntity setTaskPath(String taskPath) {
        this.taskPath = taskPath;
        return this;
    }

    public String getStarting() {
        return starting;
    }

    public TaskEntity setStarting(String starting) {
        this.starting = starting;
        return this;
    }

    public String getExectime() {
        return exectime;
    }

    public TaskEntity setExectime(String exectime) {
        this.exectime = exectime;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public TaskEntity setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String getTaskcode() {
        return taskcode;
    }

    public TaskEntity setTaskcode(String taskcode) {
        this.taskcode = taskcode;
        return this;
    }

    public Date getLastEndDate() {
        return lastEndDate;
    }

    public TaskEntity setLastEndDate(Date lastEndDate) {
        this.lastEndDate = lastEndDate;
        return this;
    }

    public Date getLastStartDate() {
        return lastStartDate;
    }

    public TaskEntity setLastStartDate(Date lastStartDate) {
        this.lastStartDate = lastStartDate;
        return this;
    }
}
