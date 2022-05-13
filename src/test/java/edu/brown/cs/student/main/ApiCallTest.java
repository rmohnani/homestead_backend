package edu.brown.cs.student.main;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class tests the API calls in the main package. Note that this class is excluded from the mvn
 * test suite and must be run independently.
 *
 * All of these tests MUST be run with:
 *   - the server running on port = 4567
 *   - the database loaded = movies.sqlite3
 *
 * Steps:
 * 1) cd to "backend" directory and run: ./run --gui --port=4567
 * 2) in same terminal, run: load ../data/movies.sqlite3
 * 3) run ApiCallTest.java (i.e. click green arrow left of "public class ApiCallTest")
 *
 * @author Nick Fah-Sang
 */
public class ApiCallTest {

  /**
   * Tests retrieval of data & table names after loading in a database.
   */
  @Test
  public void metadataHandlerTest() {
    HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(120)).build();
    String reqUri = "http://localhost:4567/metadata";
    HttpRequest request = HttpRequest.newBuilder(URI.create(reqUri))
        .header("Access-Control-Allow-Origin", "*")
        .GET()
        .timeout(Duration.ofMinutes(2))
        .build();
    try {
      HttpResponse<String> apiResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
      assertNotNull(apiResponse);
      assertEquals(200, apiResponse.statusCode());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  /**
   * Tests selecting a table through API.
   */
  @Test
  public void tableHandlerTest() {
    HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(120)).build();
    String reqUri = "http://localhost:4567/table";

    HashMap<String, String> values = new HashMap<>();
    values.put("tableName", "film");

    String requestBody = values.toString();

    HttpRequest request = HttpRequest.newBuilder(URI.create(reqUri))
        .header("Access-Control-Allow-Origin", "*")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .timeout(Duration.ofMinutes(2))
        .build();
    try {
      HttpResponse<String> apiResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
      assertNotNull(apiResponse);
      assertEquals(200, apiResponse.statusCode());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  /**
   * Tests inserting an element into the table through API.
   * Element: id = 1, name = Nick
   */
  @Test
  public void insertHandlerTest() {
    HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(120)).build();
    String reqUri = "http://localhost:4567/insert";

    String requestBody = "{\"data\":{\"name\":\"Nick\",\"id\":\"1\"},\"tableName\":\"film\"}";

    HttpRequest request = HttpRequest.newBuilder(URI.create(reqUri))
        .header("Access-Control-Allow-Origin", "*")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .timeout(Duration.ofMinutes(2))
        .build();
    try {
      HttpResponse<String> apiResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
      assertNotNull(apiResponse);
      assertEquals(200, apiResponse.statusCode());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  /**
   * Tests inserting an updating a table element through API.
   * Element: id = 1, name = Nick
   * Updated Element: id = 12, name = Alex
   */
  @Test
  public void updateHandlerTest() {
    HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(120)).build();
    String reqUri = "http://localhost:4567/update";

    String requestBody =
        "{\"data\":{\" id \":\"1\",\"name\":\"Alex\",\"id\":\"12\"},\"tableName\":\"film\"}";

    HttpRequest request = HttpRequest.newBuilder(URI.create(reqUri))
        .header("Access-Control-Allow-Origin", "*")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .timeout(Duration.ofMinutes(2))
        .build();
    try {
      HttpResponse<String> apiResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
      assertNotNull(apiResponse);
      assertEquals(200, apiResponse.statusCode());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  /**
   * Tests deleting an element from the table through API.
   * Element: id = 12, name = Alex
   */
  @Test
  public void deleteHandlerTest() {
    HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(120)).build();
    String reqUri = "http://localhost:4567/delete";

    String requestBody = "{\"data\":{\" id \":\"12\"},\"tableName\":\"film\"}";

    HttpRequest request = HttpRequest.newBuilder(URI.create(reqUri))
        .header("Access-Control-Allow-Origin", "*")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .timeout(Duration.ofMinutes(2))
        .build();
    try {
      HttpResponse<String> apiResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
      assertNotNull(apiResponse);
      assertEquals(200, apiResponse.statusCode());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
}
