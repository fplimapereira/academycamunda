package com.camunda.training;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.assertj.core.api.*;
import org.camunda.bpm.extension.process_test_coverage.junit5.ProcessEngineCoverageExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



@ExtendWith(ProcessEngineCoverageExtension.class)
public class ProcessJUnitTest {

  @Test
  @Deployment(resources = "class_01.bpmn")
  public void testHappyPath() {


    // Create a HashMap to put in variables for the process instance
    Map<String, Object> variables = new HashMap<String, Object>();
    //variables.put("approved", null);
    variables.put("content", "Exercise 4 test - FELIPE 08.02X");
    // Start process with Java API and variables
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("twitter-qa", variables);

    assertThat(processInstance).isWaitingAt("Activity_0bq8ehk");

    List<Task> taskList = taskService()
            .createTaskQuery()
            .taskCandidateGroup("management")
            .processInstanceId(processInstance.getId())
            .list();

    Assertions.assertThat(taskList).isNotNull();
    Assertions.assertThat(taskList).hasSize(1);

    Task task = taskList.getFirst();

    Map<String, Object> approvedMap = new HashMap<String, Object>();
    approvedMap.put("approved", true);
    taskService().complete(task.getId(), approvedMap);

    // Make assertions on the process instance
    assertThat(processInstance).isEnded();
    
  }

}
