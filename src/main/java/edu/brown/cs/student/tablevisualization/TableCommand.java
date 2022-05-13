package edu.brown.cs.student.tablevisualization;

import edu.brown.cs.student.main.CommandInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * This class handles the execution of user commands associated with a Table. It contains methods
 * for loading files into a database.
 *
 * @author Alex Wey
 */
public class TableCommand implements CommandInterface {

  private TableLoader tableLoader;

  /**
   * Runs a Database Proxy command provided by the user.
   *
   * @param commandLine - the commandline represented as a list of strings
   * @throws Exception if invalid command is entered
   */
  @Override
  public void run(List<String> commandLine) throws Exception {
    // get the command
    String command = commandLine.get(0);

    switch (command) {
      // execute load database proxy command
      case "load":
        if (commandLine.size() == 2) {
          this.load(commandLine.get(1));
        } else {
          throw new IOException("please ensure correct number of args: load <path_to_file>");
        }
        break;

      case "select":
        if (commandLine.size() == 2) {
          Table table = this.tableLoader.selectTable(commandLine.get(1));
          System.out.println(table.toString());
        } else {
          throw new IOException("please ensure correct number of args: select <table_name>");
        }
        break;

      default:
        throw new Exception("please enter a valid TableLoader command.");
    }
  }

  /**
   * Loads an SQLite file into a database.
   *
   * @param filename  - the filename
   * @throws FileNotFoundException if SQLite file is not found
   * @throws SQLException if there is an error in executing the SQL statement
   * @throws ClassNotFoundException if there is an error connecting to database
   */
  public void load(String filename)
      throws FileNotFoundException, SQLException, ClassNotFoundException {
    // ensure filename is valid
    if (!new File(filename).exists()) {
      throw new FileNotFoundException("please ensure filename is valid.");
    }

    // load file into database
    try {
      this.tableLoader = new TableLoader(filename);

      // print out table-to-columns hashmap to terminal
      StringBuilder tableBuilder = new StringBuilder("{");
      Map<String, List<String>> tableToColumns = tableLoader.getTableToColumns();
      for (String tableName : tableToColumns.keySet()) {
        List<String> columnNames = tableToColumns.get(tableName);
        tableBuilder.append(tableName).append("=").append(columnNames).append(", ");
      }

      // trim excess ", " and print table
      String tableString = tableBuilder.substring(0, tableBuilder.length() - 2) + "}";
      System.out.println(tableString);
      System.out.println("Loaded database from " + filename + ".");
    } catch (SQLException e) {
      throw new SQLException("please ensure SQL statement is valid.");
    } catch (ClassNotFoundException e) {
      throw new ClassNotFoundException("unable to connect to database.");
    }
  }

  /**
   * Gets the database proxy object.
   *
   * @return the database proxy
   */
  public TableLoader getTableLoader() {
    return this.tableLoader;
  }
}
