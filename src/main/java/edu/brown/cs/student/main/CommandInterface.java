package edu.brown.cs.student.main;

import java.util.List;

/**
 * This interface is implemented by the Bloom Filter and KD Tree classes. It contains a run() method
 * to run a command associated with a specific class.
 *
 * @author Agnes Tran
 */
public interface CommandInterface {

  /**
   * Runs a command for provided by the user.
   * @param commandLine - the commandline represented as a list of strings
   * @throws Exception caused when running commands
   */
  void run(List<String> commandLine) throws Exception;
}
