//package edu.brown.cs.student.tablevisualization;
//
//import edu.brown.cs.student.main.REPL;
//import org.junit.Test;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * This class tests that the Table methods have correct functionality.
// */
//public class TableMethodsTest {
//
//  TableCommand tc = new TableCommand();
//
//  /**
//   * Tests the toString() method.
//   */
//  @Test
//  public void tableToStringTest() throws Exception {
//    String commandString = "load ../data/horoscopes.sqlite3";
//    List<String> commandLine = REPL.parseUserInput(commandString);
//    tc.run(commandLine);
//
//    TableLoader tableLoader = tc.getTableLoader();
//    Table table = tableLoader.selectTable("horoscopes");
//
//    assertEquals("horoscopes: horoscope_id, horoscope. Row data size: 12.",
//        table.toString());
//  }
//}
