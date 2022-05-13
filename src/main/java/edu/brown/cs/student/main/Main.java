package edu.brown.cs.student.main;

import com.google.gson.Gson;
import edu.brown.cs.student.tablevisualization.Table;
import edu.brown.cs.student.tablevisualization.TableCommand;
import edu.brown.cs.student.tablevisualization.TableLoader;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = getHerokuAssignedPort();
  private static REPL repl = new REPL();
  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  static void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

    // Put Routes Here
    Spark.get("/metadata", new MetaDataHandler());
    Spark.post("/table", new TableHandler());
    Spark.post("/insert", new InsertHandler());
    Spark.post("/delete", new DeleteHandler());
    Spark.post("/update", new UpdateHandler());
    Spark.init();
  }

  private void run() {
//    // set up parsing of command line flags
//    OptionParser parser = new OptionParser();
//
//    // "./run --gui" will start a web server
//    parser.accepts("gui");
//
//    // use "--port <n>" to specify what port on which the server runs
//    parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_PORT);
//
//    OptionSet options = parser.parse(args);
//    if (options.has("gui")) {
//      runSparkServer((int) options.valueOf("port"));
//    }
    runSparkServer(getHerokuAssignedPort());
    // run REPL
//    this.repl.run();
  }


  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return DEFAULT_PORT;
  }

  /**
   * This class contains a handler for the metadata of the tables in the database.
   *
   * @author Alex Wey
   */
  private static class MetaDataHandler implements Route {

    /**
     * Handles the requests sent to /metadata endpoint and responds with database metadata.
     *
     * @param req - the request
     * @param res - the response
     * @return a JSON response containing the database metadata
     */
    @Override
    public String handle(Request req, Response res) {
      // get the table loader
      TableCommand tc = (TableCommand) repl.getCommands().get("load");
      TableLoader tableLoader = tc.getTableLoader();

      // return a JSON of the selected table
      Map<String, Object> metaData;
      try {
        metaData = tableLoader.getMetaData();
      } catch (NullPointerException e) {
        System.out.println("ERROR: please ensure database is loaded into backend.");
        return null;
      }

      return new Gson().toJson(metaData);
    }
  }

  /**
   * This class contains a handler for a selecting a table from the database.
   *
   * @author Alex Wey
   */
  private static class TableHandler implements Route {

    /**
     * Handles the requests sent to /table endpoint and responds with selected table data.
     *
     * @param req - the request
     * @param res - the response
     * @return a JSON response containing the table data
     */
    @Override
    public String handle(Request req, Response res) {
      // get table name from request
      JSONObject json;
      String tableName;

      try {
        json = new JSONObject(req.body());
        tableName = json.getString("tableName");
      } catch (JSONException e) {
        System.out.println("ERROR: " + e.getMessage());
        return null;
      }

      // get the table loader
      TableCommand tc = (TableCommand) repl.getCommands().get("load");
      TableLoader tableLoader = tc.getTableLoader();

      // return a JSON of the selected table
      Table table;
      try {
        table = tableLoader.selectTable(tableName);
      } catch (SQLException e) {
        System.out.println("ERROR: " + e.getMessage());
        return null;
      }
      return new Gson().toJson(table);
    }
  }

  /**
   * This class contains a handler for inserting data into a table from the database.
   *
   * @author Nick Fah-Sang
   */
  private static class InsertHandler implements Route {

    /**
     * Handles the request sent to the /insert endpoint and responds with updated table data.
     *
     * @param req - the request
     * @param res - the response
     * @return a JSON response containing table data with inserted data
     */
    @Override
    public String handle(Request req, Response res) {
      // get table name and row values from request
      JSONObject json;
      String tableName;
      TreeMap<String, String> formData = new TreeMap<>();

      try {
        json = new JSONObject(req.body());
        tableName = json.getString("tableName");
        List<String> allData = REPL.parsePostParams(json.getString("data"));

        // populate TreeMap such that column name => cell data
        for (int i = 0; i < allData.size(); i++) {
          if (i % 2 == 1) {
            formData.put(allData.get(i - 1), allData.get(i));
          }
        }

      } catch (JSONException e) {
        System.out.println("ERROR:" + e.getMessage());
        return null;
      }

      // get the table loader
      TableCommand tc = (TableCommand) repl.getCommands().get("load");
      TableLoader tableLoader = tc.getTableLoader();

      // return a JSON of the table after insertion
      Table table;
      try {
        table = tableLoader.insertTable(tableName, formData);
      } catch (SQLException e) {
        System.out.println("ERROR: " + e.getMessage());
        return null;
      }
      return new Gson().toJson(table);
    }
  }

  /**
   * This class contains a handler for deleting data from a table from the database.
   *
   * @author Nick Fah-Sang
   */
  private static class DeleteHandler implements Route {

    /**
     * Handles the request sent to the /delete endpoint and responds with updated table data.
     *
     * @param req - the request
     * @param res - the response
     * @return a JSON response containing table data with deleted data
     */
    @Override
    public Object handle(Request req, Response res) {
      // get table name, key name, and key value from request
      JSONObject json;
      String tableName;
      String keyName;
      String keyValue;

      try {
        json = new JSONObject(req.body());
        tableName = json.getString("tableName");
        List<String> keyData = REPL.parsePostParams(json.getString("data"));
        keyName = keyData.get(0);
        keyValue = keyData.get(1);
      } catch (JSONException e) {
        System.out.println("ERROR: " + e.getMessage());
        return null;
      }

      // get the table loader
      TableCommand tc = (TableCommand) repl.getCommands().get("load");
      TableLoader tableLoader = tc.getTableLoader();

      // return a JSON of the table after deletion
      Table table;
      try {
        table = tableLoader.deleteTable(tableName, keyName, keyValue);
      } catch (SQLException e) {
        System.out.println("ERROR:" + e.getMessage());
        return null;
      }
      return new Gson().toJson(table);
    }
  }

  /**
   * This class contains a handler for updating data in a table from the database.
   *
   * @author Nick Fah-Sang
   */
  private static class UpdateHandler implements Route {

    /**
     * Handles the request sent to the /delete endpoint and responds with updated table data.
     *
     * @param req - the request
     * @param res - the response
     * @return a JSON response containing table data with updated data
     */
    @Override
    public String handle(Request req, Response res) {
      // get table name, key name, old key value, new key value, and form data from request
      JSONObject json;
      String tableName;
      String keyName;
      String oldKeyValue;
      TreeMap<String, String> formData = new TreeMap<>();

      try {
        json = new JSONObject(req.body());
        tableName = json.getString("tableName");
        List<String> allData = REPL.parsePostParams(json.getString("data"));

        // populate TreeMap such that column name => cell data
        for (int i = 0; i < allData.size(); i++) {
          if (i % 2 == 1) {
            formData.put(allData.get(i - 1), allData.get(i));
          }
        }

        // get old key and remove from map
        Map.Entry<String, String> keyData = formData.firstEntry();
        keyName = keyData.getKey();
        oldKeyValue = keyData.getValue();
        formData.remove(keyName);
      } catch (JSONException e) {
        System.out.println("ERROR:" + e.getMessage());
        return null;
      }

      // get the table loader
      TableCommand tc = (TableCommand) repl.getCommands().get("load");
      TableLoader tableLoader = tc.getTableLoader();

      // return a JSON of the selected table
      Table table;
      System.out.println(formData);
      try {
        table = tableLoader.updateTable(tableName, keyName.trim(), oldKeyValue, formData);
      } catch (SQLException e) {
        System.out.println("ERROR:" + e.getMessage());
        return null;
      }
      return new Gson().toJson(table);
    }
  }
}


