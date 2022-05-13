package edu.brown.cs.student.types;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

  private String userName;
  private Session userSession;
  private Map<String, List<Farm>> farms;

  public User(String userName) {
    this.userName = userName;
    this.initializeFarms();
  }

  public void initializeFarms() {
    this.farms = new HashMap<>();
    this.farms.put("Individual", new ArrayList<Farm>());
    this.farms.put("Shared", new ArrayList<Farm>());
  }

  public List<Farm> getIndividualFarms() {
    return this.farms.get("Individual");
  }

  public List<Farm> getSharedFarms() {
    return this.farms.get("Shared");
  }

  public List<Farm> getAllFarms() {
    List<Farm> allFarms = new ArrayList<Farm>();
    allFarms.addAll(this.getIndividualFarms());
    allFarms.addAll(this.getSharedFarms());
    return allFarms;
  }

}
