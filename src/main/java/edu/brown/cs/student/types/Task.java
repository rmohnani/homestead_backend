package edu.brown.cs.student.types;

public class Task {

  private String taskName;
  private String taskDescription;
  private boolean isComplete;
  private Decoration decorationReward;

  public Task(String taskName, String taskDescription, Decoration decoration) {
    this.taskName = taskName;
    this.taskDescription = taskDescription;
    this.decorationReward = decoration;
    this.isComplete = false;
  }

  public void completeTask() {
    this.isComplete = true;
  }

  public boolean isComplete() {
    return this.isComplete;
  }
}
