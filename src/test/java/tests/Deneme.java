package tests;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;
import utilities.Driver;

public class Deneme extends TestBase{

    @Test
    public void testCase01() {
        extentLogger=report.createTest("login test");
        extentLogger.info("username input");
        driver.findElement(By.id("prependedInput")).sendKeys(ConfigurationReader.get("driver_username"));
        extentLogger.info("password input");
        driver.findElement(By.id("prependedInput2")).sendKeys(ConfigurationReader.get("driver_password"));
        driver.findElement(By.id("_submit")).click();

        extentLogger.pass("PASS: successfully logged in");

    }
}
