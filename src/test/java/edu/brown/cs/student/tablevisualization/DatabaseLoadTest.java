//package edu.brown.cs.student.tablevisualization;
//
//import edu.brown.cs.student.main.REPL;
//import org.junit.Test;
//
//import java.io.FileNotFoundException;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
///**
// * This class tests that TableLoader properly loads a database into the backend and selects a table
// * from the database.
// *
// * @author Alex Wey
// */
//public class DatabaseLoadTest {
//
//  TableCommand tc = new TableCommand();
//
//  /**
//   * Tests that exception is thrown when database file is invalid.
//   */
//  @Test(expected = FileNotFoundException.class)
//  public void loadInvalidFileTest() throws Exception {
//    String commandString = "load ../data/test/foo.sqlite3";
//    List<String> commandLine = REPL.parseUserInput(commandString);
//    tc.run(commandLine);
//  }
//
//  /**
//   * Tests that valid file successfully loads tables, columns, and data.
//   */
//  @Test
//  public void loadValidFileTest() throws Exception {
//    String commandString = "load ../data/test/horoscopes.sqlite3";
//    List<String> commandLine = REPL.parseUserInput(commandString);
//    tc.run(commandLine);
//
//    TableLoader tableLoader = tc.getTableLoader();
//    Map<String, List<String>> tableToColumns = tableLoader.getTableToColumns();
//
//    assertEquals(4, tableToColumns.keySet().size());
//    assertTrue(tableToColumns.containsKey("horoscopes"));
//    assertTrue(tableToColumns.containsKey("sqlite_sequence"));
//    assertTrue(tableToColumns.containsKey("ta_horoscope"));
//    assertTrue(tableToColumns.containsKey("tas"));
//
//    assertEquals(2, tableToColumns.get("horoscopes").size());
//    assertTrue(tableToColumns.get("horoscopes").contains("horoscope_id"));
//    assertTrue(tableToColumns.get("horoscopes").contains("horoscope"));
//
//    assertEquals(2, tableToColumns.get("sqlite_sequence").size());
//    assertTrue(tableToColumns.get("sqlite_sequence").contains("name"));
//    assertTrue(tableToColumns.get("sqlite_sequence").contains("seq"));
//
//    assertEquals(2, tableToColumns.get("ta_horoscope").size());
//    assertTrue(tableToColumns.get("ta_horoscope").contains("ta_id"));
//    assertTrue(tableToColumns.get("ta_horoscope").contains("horoscope_id"));
//
//    assertEquals(3, tableToColumns.get("tas").size());
//    assertTrue(tableToColumns.get("tas").contains("id"));
//    assertTrue(tableToColumns.get("tas").contains("name"));
//    assertTrue(tableToColumns.get("tas").contains("role"));
//
//    tableLoader.closeDatabase();
//  }
//
//  /**
//   * Tests that exception is thrown when an invalid table is selected from database.
//   */
//  @Test(expected = SQLException.class)
//  public void selectInvalidTableTest() throws Exception {
//    String commandString = "load ../data/test/horoscopes.sqlite3";
//    List<String> commandLine = REPL.parseUserInput(commandString);
//    tc.run(commandLine);
//
//    TableLoader tableLoader = tc.getTableLoader();
//    tableLoader.selectTable("foo");
//  }
//
//  /**
//   * Tests that valid table can be successfully selected and table contains correct information.
//   */
//  @Test
//  public void selectValidTableTest() throws Exception {
//    String commandString = "load ../data/test/horoscopes.sqlite3";
//    List<String> commandLine = REPL.parseUserInput(commandString);
//    tc.run(commandLine);
//
//    TableLoader tableLoader = tc.getTableLoader();
//    Table table = tableLoader.selectTable("horoscopes");
//
//    assertEquals("horoscopes", table.getTableName());
//    assertEquals("horoscope_id", table.getColumnNames().get(0));
//    assertEquals("horoscope", table.getColumnNames().get(1));
//    assertEquals(12, table.getRowData().size());
//
//    tableLoader.closeDatabase();
//  }
//}
