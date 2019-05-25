package cn.stylefeng.guns.modular.workflow.model;

import java.util.Map;

public class TaskModel extends DataModel {

    private String startTime;

    private String taskId;

    private String taskName;

    private String createTime;

    private String assignee;

    private String procStatus;

    private String taskDefKey;

    private String formKey;

    private Map<String, Object> taskLocalVariables;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(String procStatus) {
        this.procStatus = procStatus;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public Map<String, Object> getTaskLocalVariables() {
        return taskLocalVariables;
    }

    public void setTaskLocalVariables(Map<String, Object> taskLocalVariables) {
        this.taskLocalVariables = taskLocalVariables;
    }
}
