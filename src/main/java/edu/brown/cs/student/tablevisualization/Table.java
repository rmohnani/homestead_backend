package edu.brown.cs.student.tablevisualization;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all relevant table information.
 *
 * @author Alex Wey
 */
public class Table {

  private String tableName;
  private List<String> columnNames = new ArrayList<>();
  private List<List<String>> rowData = new ArrayList<>();

  /**
   * Constructor for Table object. Instantiates the table name.
   *
   * @param tableName - Name of the table
   */
  public Table(String tableName) {
    this.tableName = tableName;
  }

  /**
   * Gets the table name.
   *
   * @return the table name
   */
  public String getTableName() {
    return this.tableName;
  }

  /**
   * Gets the column names.
   *
   * @return the column names
   */
  public List<String> getColumnNames() {
    return this.columnNames;
  }

  /**
   * Gets the rows of data.
   *
   * @return the rows of data
   */
  public List<List<String>> getRowData() {
    return this.rowData;
  }

  /**
   * Adds a column name to the column names.
   *
   * @param columnName - the column name
   */
  public void addColumnName(String columnName) {
    this.columnNames.add(columnName);
  }

  /**
   * Adds a row to the rows of data.
   *
   * @param row - a row of data as a String
   */
  public void addRowData(List<String> row) {
    this.rowData.add(row);
  }

  /**
   * Converts a Table object into a String.
   *
   * @return a String representation of a table
   */
  @Override
  public String toString() {
    StringBuilder tableBuilder = new StringBuilder(this.tableName + ": ");
    for (String columnName : this.columnNames) {
      tableBuilder.append(columnName).append(", ");
    }

    // trim excess ", " and append size of row data
    String tableString = tableBuilder.substring(0, tableBuilder.length() - 2);
    tableString += ". Row data size: " + this.rowData.size() + ".";
    return tableString;
  }
}
