package cn.stylefeng.guns.modular.workflow.model;

import java.util.Map;

public class DataModel {

    private String processInstanceId;

    private String processDefinitionId;

    private String processDefinitionKey;

    private String processDefinttionName;

    private int processDefinitionVersion;

    private String startUserId;

    private String startUserName;

    private Map<String, Object> processVariables;

    private String depolymentId;

    private String description;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinttionName() {
        return processDefinttionName;
    }

    public void setProcessDefinttionName(String processDefinttionName) {
        this.processDefinttionName = processDefinttionName;
    }

    public int getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(int processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public Map<String, Object> getProcessVariables() {
        return processVariables;
    }

    public void setProcessVariables(Map<String, Object> processVariables) {
        this.processVariables = processVariables;
    }

    public String getDepolymentId() {
        return depolymentId;
    }

    public void setDepolymentId(String depolymentId) {
        this.depolymentId = depolymentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
