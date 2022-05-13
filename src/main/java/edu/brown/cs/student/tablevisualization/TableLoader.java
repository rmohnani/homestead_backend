package edu.brown.cs.student.tablevisualization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles the loading of all table data from a database. It contains methods that
 * specifically get table names, columns, and column data.
 *
 * @author Alex Wey
 */
public class TableLoader {

  private Connection conn;
  private Map<String, Object> metaData = new HashMap<>();
  private Map<String, List<String>> tableToColumns = new HashMap<>();

  /**
   * Constructor for TableHandler object. Instantiates the database connection and populates the
   * table data.
   *
   * @param filename - file name of SQLite3 database to open
   * @throws SQLException           if there is an error in any SQL statement
   * @throws ClassNotFoundException if there is an error connecting to database
   */
  public TableLoader(String filename) throws SQLException, ClassNotFoundException {
    // initialize the database connection and turn foreign keys on
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    this.conn = DriverManager.getConnection(urlToDB);
    Statement stat = this.conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");

    // populate all table fields
    this.getTableNames();
    this.getColumnNames();
  }

  /**
   * Gets all table names from the database.
   *
   * @throws SQLException if there is an error in executing the SQL statement
   */
  private void getTableNames() throws SQLException {
    // execute SQL statement to get all table names
    String sql = "SELECT name FROM sqlite_master WHERE type = 'table';";
    PreparedStatement statement = conn.prepareStatement(sql);
    ResultSet rs = statement.executeQuery();

    // add table to table data
    List<String> tableNames = new ArrayList<>();
    while (rs.next()) {
      String tableName = rs.getString(1);
      tableNames.add(tableName);
    }
    this.metaData.put("tableNames", tableNames);

    // close prepared statement
    statement.close();
  }

  /**
   * Gets all column names from every table.
   *
   * @throws SQLException if there is an error in executing the SQL statement
   */
  private void getColumnNames() throws SQLException {
    // loop through table names
    for (String tableName : (List<String>) this.metaData.get("tableNames")) {

      // execute SQL statement to get all column names
      String sql = "PRAGMA table_info(" + tableName + ");";
      PreparedStatement statement = conn.prepareStatement(sql);
      ResultSet rs = statement.executeQuery();

      // put column names for every table in hashmap
      while (rs.next()) {
        String columnName = rs.getString(2);
        if (tableToColumns.containsKey(tableName)) {
          tableToColumns.get(tableName).add(columnName);
        } else {
          List<String> columnNames = new ArrayList<>();
          columnNames.add(columnName);
          tableToColumns.put(tableName, columnNames);
        }
      }

      // close prepared statement
      statement.close();
    }
  }

  /**
   * Selects a table specified by the end user.
   *
   * @param tableName - name of the table
   * @return the selected table
   * @throws SQLException if table does not exist in database
   */
  public Table selectTable(String tableName) throws SQLException {
    // instantiate a table
    Table table = new Table(tableName);

    // execute SQL statement to get all column names
    String sql = "PRAGMA table_info(" + tableName + ");";
    PreparedStatement statement = conn.prepareStatement(sql);
    ResultSet rs = statement.executeQuery();

    // put column names for every table in hashmap
    while (rs.next()) {
      String columnName = rs.getString(2);
      table.addColumnName(columnName);
    }

    // close prepared statement
    statement.close();

    // execute SQL statement to get all column data
    sql = "SELECT * FROM " + tableName + ";";
    statement = conn.prepareStatement(sql);
    rs = statement.executeQuery();

    // add rows of data to table
    Map<String, List<String>> columnData = new HashMap<>();
    while (rs.next()) {
      List<String> row = new ArrayList<>();
      for (int i = 0; i < table.getColumnNames().size(); i++) {
        String columnDataCell = rs.getString(i + 1);
        row.add(columnDataCell);
      }
      table.addRowData(row);
    }

    // close prepared statement
    statement.close();

    return table;
  }

  /**
   * Inserts row data into a table specified by the end user.
   *
   * @param tableName - name of the table
   * @param formData  - form data to be inserted into table
   * @return the selected table with inserted data
   * @throws SQLException if there is an error inserting data
   */
  public Table insertTable(String tableName, Map<String, String> formData) throws SQLException {
    //build string such that (column1, column2, etc).
    StringBuilder columnBuilder = new StringBuilder("(");
    for (String columnName : formData.keySet()) {
      columnBuilder.append(columnName)
          .append(", ");
    }
    String columns = columnBuilder.substring(0, columnBuilder.length() - 2) + ")";

    // build string such that (value1, value2, etc).
    StringBuilder valueBuilder = new StringBuilder("(");
    for (String columnName : formData.keySet()) {
      valueBuilder.append("'")
          .append(formData.get(columnName))
          .append("'")
          .append(",");
    }
    String values = valueBuilder.substring(0, valueBuilder.length() - 1) + ");";

    // execute SQL statement to insert form data into table
    String sql = "INSERT INTO " + tableName + " " + columns + " VALUES " + values;
    PreparedStatement statement = conn.prepareStatement(sql);
    statement.executeUpdate();

    // close prepared statement
    statement.close();

    // return the selected table
    return selectTable(tableName);
  }

  /**
   * Deletes row data from a table specified by the end user.
   *
   * @param tableName - name of the table
   * @param keyName   - name of the key (i.e. id)
   * @param keyValue  - value of the key (i.e. 0)
   * @return the selected table with deleted data
   * @throws SQLException if there is an error deleting data
   */
  public Table deleteTable(String tableName, String keyName, String keyValue) throws SQLException {
    // execute SQL statement to insert form data into table
    String sql = "DELETE FROM " + tableName + " WHERE " + keyName + "='" + keyValue + "';";
    PreparedStatement statement = conn.prepareStatement(sql);
    statement.executeUpdate();

    // close prepared statement
    statement.close();

    // return the selected table
    return selectTable(tableName);
  }

  /**
   * Updates row data in a table specified by the end user.
   *
   * @param tableName - name of the table
   * @param keyName   - name of the key (i.e. id)
   * @param keyValue  - value of the key (i.e. 0)
   * @param formData  - data to be updated
   * @return the selected table with updated data
   * @throws SQLException if there is an error updating data
   */
  public Table updateTable(String tableName, String keyName, String keyValue,
                           Map<String, String> formData) throws SQLException {
    // build string such that column1=value1, column2=value2, etc.
    StringBuilder setBuilder = new StringBuilder();
    for (String columnName : formData.keySet()) {
      setBuilder.append(columnName)
          .append("=")
          .append("'")
          .append(formData.get(columnName))
          .append("'")
          .append(", ");
    }
    String set = setBuilder.substring(0, setBuilder.length() - 2);

    // execute SQL statement to insert form data into table
    String sql = "UPDATE " + tableName + " SET " + set
        + " WHERE " + keyName + "='" + keyValue + "';";
    PreparedStatement statement = conn.prepareStatement(sql);
    statement.executeUpdate();

    // close prepared statement
    statement.close();

    // return the selected table
    return selectTable(tableName);
  }

  /**
   * Closes the connection to database.
   *
   * @throws SQLException if there is an error closing the database connection
   */
  public void closeDatabase() throws SQLException {
    this.conn.close();
  }

  /**
   * Gets the metadata.
   *
   * @return the metadata
   */
  public Map<String, Object> getMetaData() {
    return this.metaData;
  }

  /**
   * Gets the table-to-columns hashmap.
   *
   * @return the table-to-columns hashmap
   */
  public Map<String, List<String>> getTableToColumns() {
    return this.tableToColumns;
  }
}
