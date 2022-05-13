package edu.brown.cs.student.main;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class tests that the REPL helper methods have correct functionality.
 *
 * @author Nick Fah-Sang
 */
public class REPLTest {

  /**
   * Tests the parseUserInput() method.
   */
  @Test
  public void parseUserInputTest() {
    String testString1 = "'Testing' data inside 'This is supposed to be one string' here";
    String testString2 = "[{Hello} {Hi} {Hola}]";
    String emptyString = "";

    List<String> answerString1 = new ArrayList<>();
    answerString1.add("Testing");
    answerString1.add("data");
    answerString1.add("inside");
    answerString1.add("This is supposed to be one string");
    answerString1.add("here");

    List<String> answerString2 = new ArrayList<>();
    answerString2.add("[{Hello}");
    answerString2.add("{Hi}");
    answerString2.add("{Hola}]");

    List<String> emptyList = new ArrayList<>();

    assertEquals(answerString1, REPL.parseUserInput(testString1));
    assertEquals(answerString2, REPL.parseUserInput(testString2));
    assertEquals(emptyList, REPL.parseUserInput(emptyString));
  }

  /**
   * Tests the parsePostParams() method.
   */
  @Test
  public void parsePostParamsTest() {
    String testString1 = "[\"1\", \"2\", \"3\", \"4\"]";
    String testString2 = "[{\"Hello\"} {\"Hi\"} {\"Hola\"}]";
    String emptyString = "";

    List<String> answerString1 = new ArrayList<>();
    answerString1.add("1");
    answerString1.add("2");
    answerString1.add("3");
    answerString1.add("4");

    List<String> answerString2 = new ArrayList<>();
    answerString2.add("Hello");
    answerString2.add("Hi");
    answerString2.add("Hola");

    List<String> emptyList = new ArrayList<>();

    assertEquals(answerString1, REPL.parsePostParams(testString1));
    assertEquals(answerString2, REPL.parsePostParams(testString2));
    assertEquals(emptyList, REPL.parsePostParams(emptyString));
  }
}
