package cn.stylefeng.guns.modular.workflow.service;

import cn.hutool.core.date.DateUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.workflow.model.HistoryTaskModel;
import cn.stylefeng.guns.modular.workflow.model.TaskModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 工作流常用接口服务类
 *
 * @author yaoliguo
 * @date 2019-05-25 09:44
 * ©2018 版权所有
 */
@Service
public class FlowService {

    @Autowired
    private ProcessEngine processEngine;

    public Page<TaskModel> findTask(String assignee) {

        Page<TaskModel> page = LayuiPageFactory.defaultPage();

        TaskQuery query = processEngine.getTaskService()//
                .createTaskQuery()//
                .taskAssignee(assignee)//
                .includeProcessVariables()//
                .includeTaskLocalVariables()//
                .orderByTaskCreateTime()//
                .desc();
        long totalCount = query.count();
        Long current = page.getCurrent();
        Long size = page.getSize();
        List<Task> tasks = query.listPage(getFristResult(current.intValue(), size.intValue()), size.intValue());
        List<TaskModel> models = new ArrayList<TaskModel>();

        for (Task task : tasks) {
            TaskModel model = new TaskModel();
            model.setTaskId(task.getId());
            model.setAssignee(task.getAssignee());
            model.setCreateTime(DateUtil.format(task.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            model.setTaskName(task.getName());
            model.setTaskDefKey(task.getTaskDefinitionKey());
            model.setProcessInstanceId(task.getProcessInstanceId());
            // 查询任务所属的流程名称
            ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).singleResult();
            model.setProcessDefinttionName(processInstance.getProcessDefinitionName());
            model.setProcessDefinitionId(task.getProcessDefinitionId());
            model.setProcessVariables(task.getProcessVariables());
            model.setTaskLocalVariables(task.getTaskLocalVariables());
            model.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
            // 设置表单url地址
            model.setFormKey(task.getFormKey());
            model.setStartTime(DateUtil.format(processInstance.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            models.add(model);
        }
        page.setRecords(models);
        // 设置分页信息
        page.setTotal(totalCount);
        page.setCurrent(current);
        page.setSize(size);

        return page;
    }

    public Page<HistoryTaskModel> getHisTaskPage(String assignee) {

        Page<HistoryTaskModel> page = LayuiPageFactory.defaultPage();
        HistoricTaskInstanceQuery query = processEngine.getHistoryService()//
                .createHistoricTaskInstanceQuery()//
                .taskAssignee(assignee)//
                .includeProcessVariables()
                .includeTaskLocalVariables()//
                .finished()//
                .orderByTaskCreateTime()//
                .desc();
        long totalCount = query.count();
        Long current = page.getCurrent();
        Long size = page.getSize();
        long count = query.count();
        List<HistoricTaskInstance> tasks = query.listPage(getFristResult(current.intValue(), size.intValue()), size.intValue());

        List<HistoryTaskModel> hisTasks = new ArrayList<HistoryTaskModel>();
        for (HistoricTaskInstance task : tasks) {
            HistoryTaskModel model = new HistoryTaskModel();
            model.setTaskId(task.getId());
            model.setAssignee(task.getAssignee());
            model.setCreateTime(DateUtil.format(task.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            model.setEndTime(DateUtil.format(task.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            model.setTaskName(task.getName());
            model.setProcessInstanceId(task.getProcessInstanceId());
            // 查询当前流程是否结束
            HistoricProcessInstance processInst = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).includeProcessVariables().singleResult();
            if (processInst == null) {
                continue;
            }

            // 设置流程名称
            HistoricProcessInstance processInstance = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).singleResult();
            model.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
            model.setProcessDefinttionName(processInstance.getProcessDefinitionName());
            model.setProcessDefinitionId(task.getProcessDefinitionId());
            model.setProcessVariables(task.getProcessVariables());
            model.setTaskLocalVariables(task.getTaskLocalVariables());
            model.setDescription(task.getDescription());
            hisTasks.add(model);
        }

        page.setRecords(hisTasks);
        // 设置分页信息
        page.setTotal(totalCount);
        page.setCurrent(current);
        page.setSize(size);

        return page;
    }

    /*
     * 计算记录开始行
     */
    private int getFristResult(int pageNo, int pageSize) {
        int fristResult = (pageNo - 1) * pageSize;
        return fristResult;
    }

}
