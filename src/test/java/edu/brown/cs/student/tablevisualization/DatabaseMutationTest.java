//package edu.brown.cs.student.tablevisualization;
//
//import edu.brown.cs.student.main.REPL;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//
///**
// * This class tests that TableLoader properly inserts, deletes, and updates a table in a database.
// *
// * @author Alex Wey
// */
//public class DatabaseMutationTest {
//
//  TableCommand tc = new TableCommand();
//
//  /**
//   * Tests that data can be inserted, updated, and deleted from table in database.
//   */
//  @Test
//  public void InsertUpdateDeleteTest() throws Exception {
//    // test insertion
//    String commandString = "load ../data/test/horoscopes.sqlite3";
//    List<String> commandLine = REPL.parseUserInput(commandString);
//    tc.run(commandLine);
//
//    TreeMap<String, String> rowValues = new TreeMap<>();
//    rowValues.put("id", "999");
//    rowValues.put("name", "Alex");
//    rowValues.put("role", "Student");
//
//    TableLoader tableLoader = tc.getTableLoader();
//    Table table = tableLoader.insertTable("tas", rowValues);
//    List<List<String>> rowData = table.getRowData();
//
//    assertEquals("999", rowData.get(7).get(0));
//    assertEquals("Alex", rowData.get(7).get(1));
//    assertEquals("Student", rowData.get(7).get(2));
//
//    // test update
//    Map<String, String> formData = new TreeMap<>();
//    formData.put("id", "1000");
//    formData.put("name", "Nick");
//    formData.put("role", "Teacher");
//
//    tableLoader = tc.getTableLoader();
//    table = tableLoader.updateTable("tas", "id", "999", formData);
//    rowData = table.getRowData();
//
//    assertEquals("1000", rowData.get(7).get(0));
//    assertEquals("Nick", rowData.get(7).get(1));
//    assertEquals("Teacher", rowData.get(7).get(2));
//
//    // test deletion
//    tableLoader = tc.getTableLoader();
//    table = tableLoader.deleteTable("tas", "id", "1000");
//    rowData = table.getRowData();
//
//    List<String> row1 = new ArrayList<>();
//    row1.add("999");
//    row1.add("Alex");
//    row1.add("Student");
//
//    List<String> row2 = new ArrayList<>();
//    row2.add("1000");
//    row2.add("Nick");
//    row2.add("Teacher");
//
//    assertFalse(rowData.contains(row1));
//    assertFalse(rowData.contains(row2));
//
//    tableLoader.closeDatabase();
//  }
//}
