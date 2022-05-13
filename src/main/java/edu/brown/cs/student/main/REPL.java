package edu.brown.cs.student.main;

import edu.brown.cs.student.tablevisualization.TableCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class adds commands to a hashmap (key: command string, value: CommandInterface class),
 * reads the user's input, and checks the hashmap to see if the command exists. If the key exists,
 * the value is retrieved from the hashmap, and run() method is called for a specific command.
 *
 * @author Agnes Tran
 */
public class REPL {

  private final Map<String, CommandInterface> commands;

  /**
   * Constructor for REPL, adds commands to the hashmap.
   */
  public REPL() {
    // instantiate commands map
    this.commands = new HashMap<>();

    // add database proxy commands
    CommandInterface tc = new TableCommand();
    this.addCommand("load", tc);
    this.addCommand("select", tc);
  }

  /**
   * Allows user to add any of their own commands.
   *
   * @param commandString - the user's command represented as a String
   * @param commandClass  - a class that implements the CommandInterface
   */
  public void addCommand(String commandString, CommandInterface commandClass) {
    this.commands.put(commandString, commandClass);
  }

  /**
   * Reads the commandline, checks the command hashmap if that command exists, and executes it if
   * it does. Otherwise, returns an error message.
   */
  public void run() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      // read line from input stream
      String input;
      while ((input = br.readLine()) != null) {
        try {
          // parse the user input
          List<String> commandLine = parseUserInput(input);
          String command = commandLine.get(0);

          // close stream if user wants to exit
          if (command.equals("exit")) {
            br.close();
            break;

            // check for invalid command
          } else if (this.commands.get(command) == null) {
            System.out.println("ERROR: please ensure command is valid.");
            continue;
          }

          // execute command with command line
          this.commands.get(command).run(commandLine);
        } catch (IndexOutOfBoundsException e) {
          System.out.println("ERROR: please ensure user input is not empty.");
        } catch (Exception e) {
          System.out.println("ERROR: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.out.println("ERROR: unable to close buffered reader.");
    }
  }

  /**
   * Adapted from: https://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-
   * space-when-not-surrounded-by-single-or-double/366532#366532.
   *
   * @param input - the user input that is to be parsed
   * @return command line as a list of String elements
   */
  public static List<String> parseUserInput(String input) {
    List<String> commandLine = new ArrayList<>();
    Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    Matcher regexMatcher = regex.matcher(input);

    while (regexMatcher.find()) {
      // add double-quoted string without the quotes
      if (regexMatcher.group(1) != null) {
        commandLine.add(regexMatcher.group(1));

      // add single-quoted string without the quotes
      } else if (regexMatcher.group(2) != null) {
        commandLine.add(regexMatcher.group(2));

      // add unquoted word
      } else {
        commandLine.add(regexMatcher.group());
      }
    }
    return commandLine;
  }

  /**
   * Parses parameters from POST request.
   *
   * @param input - POST parameters as a String
   * @return list of parsed POST parameters
   */
  public static List<String> parsePostParams(String input) {
    List<String> parsedArray = new ArrayList<>();
    Pattern p = Pattern.compile("\"([^\"]*)\"");
    Matcher m = p.matcher(input);
    while (m.find()) {
      parsedArray.add(m.group(1).replaceAll("\\\\", ""));
    }
    return parsedArray;
  }

  /**
   * Gets the REPL commands.
   *
   * @return the REPL commands.
   */
  public Map<String, CommandInterface> getCommands() {
    return this.commands;
  }
}
