package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import utilities.ConfigurationReader;
import utilities.Driver;
import utilities.Sleeper;
import com.github.javafaker.Faker;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import utilities.BrowserUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestBase {

    public WebDriver driver;
    public Actions actions;
    public WebDriverWait wait;
    public Faker faker;
    //this class is used for starting and building reports;
    public ExtentReports report;
    //this class is used to create HTML report file
    public ExtentHtmlReporter htmlReporter;
    public ExtentTest extentLogger;

    @BeforeTest
    public void setUpTest(){

        //create a report path
        report = new ExtentReports();

        String projectPath=System.getProperty("user.dir");
        String path=projectPath + "/test-output/report.html";

        //initialize the html reporter with the report path
        htmlReporter=new ExtentHtmlReporter(path);


        //attach the html report to report object
        report.attachReporter(htmlReporter);

        //title in report
        htmlReporter.config().setReportName("Report Name");

        //set environment information
        report.setSystemInfo("Environment", "QA");
        report.setSystemInfo("Browser", ConfigurationReader.get("browser"));
        report.setSystemInfo("OS", System.getProperty("os.name"));
    }



    @BeforeMethod
    public void setUp() {
        driver= Driver.get();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        actions=new Actions(driver);
        wait=new WebDriverWait(driver, 10);
        faker=new Faker();
        driver.get(ConfigurationReader.get("url"));





    }

    @AfterMethod
    public void tearDown(ITestResult result) throws InterruptedException, IOException {
        //if test fails
        if(result.getStatus()==ITestResult.FAILURE){
            //record the name of failed test case
            extentLogger.fail(result.getName());

            //take the screenshot and return location of screenshot
            String screenShotPath= BrowserUtils.getScreenshot(result.getName());

            //add your screenshot to your report
            extentLogger.addScreenCaptureFromPath(screenShotPath);

            //capture the exception and put inside the report
            extentLogger.fail(result.getThrowable());

        }

        Thread.sleep(500);
        Driver.closeDriver();
    }
    @AfterTest
    public void tearDownTest(){
        //this is when the report is actually created
        report.flush();

    }
}
