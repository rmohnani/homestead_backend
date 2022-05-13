package edu.brown.cs.student.types;

enum Frequency {
  DAILY, WEEKLY, MONTHLY, ONCE;
}

public class Freq {
  public static Frequency getFrequency(String frequency) {
    Frequency freq;
    switch (frequency) {
      case "Daily":
        freq = Frequency.DAILY;
        break;
      case "Weekly":
        freq = Frequency.WEEKLY;
        break;
      case "Monthly":
        freq = Frequency.MONTHLY;
        break;
      default:
        freq = Frequency.ONCE;
    }
    return freq;
  }
}
