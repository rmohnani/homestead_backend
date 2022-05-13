package edu.brown.cs.student.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;


/**
 * This class tests that Selenium is installed and working properly.
 *
 * @author Nicholas Fah-Sang
 */
public class SeleniumSetupTest {

  WebDriver driver;

  @Before
  public void setup() {
    System.out.println("Setting Up");
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
  }

  @After
  public void tearDown() {
    System.out.println("Tearing Down");
    driver.quit();
  }

  @Test
  public void testOne() {
    driver.get("https://google.com");
    String title = driver.getTitle();

    assertEquals("Google", title);
  }

  @Test
  public void testTwo() {
    driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
    String title = driver.getTitle();
    assertEquals("Hands-On Selenium WebDriver with Java", title);
  }

}
