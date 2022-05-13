package edu.brown.cs.student.types;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Farm {

  private String farmName;
  private String farmDescription;
  private List<User> userList;
  private List<Task> taskList;
  private List<Decoration> decorationList;
  private Frequency frequency;
  private Integer score;

  public Farm(String farmName, String farmDescription, String frequency) {
    this.farmName = farmName;
    this.farmDescription = farmDescription;
    this.userList = new ArrayList<User>();
    this.taskList = new ArrayList<Task>();
    this.decorationList = new ArrayList<Decoration>();
    this.frequency = Freq.getFrequency(frequency);
    this.score = 0;
  }

  public String getFarmName() {
    return farmName;
  }

  public void setFarmName(String farmName) {
    this.farmName = farmName;
  }

  public String getFarmDescription() {
    return farmDescription;
  }

  public void setFarmDescription(String farmDescription) {
    this.farmDescription = farmDescription;
  }

  public List<User> getUserList() {
    return userList;
  }

  public void setUserList(List<User> userList) {
    this.userList = userList;
  }

  public List<Task> getTaskList() {
    return taskList;
  }

  public void setTaskList(List<Task> taskList) {
    this.taskList = taskList;
  }

  public List<Decoration> getDecorationList() {
    return decorationList;
  }

  public void setDecorationList(List<Decoration> decorationList) {
    this.decorationList = decorationList;
  }

  public Frequency getFrequency() {
    return frequency;
  }

  public void setFrequency(Frequency frequency) {
    this.frequency = frequency;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public void addUser(User user) {
    this.userList.add(user);
    user.getSharedFarms().add(this);
  }

  public void addTask(Task task) {
    this.taskList.add(task);
  }

  public double completion() {
    int completion  = 0;
    int total = 0;
    for (Task task: this.taskList) {
      if (task.isComplete() == true) {
        completion += 1;
      }
      total += 1;
    }
    return (completion / total);
  }

  //TODO: figure it out
  public void refreshFarm() {

  }



}
