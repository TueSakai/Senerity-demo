package Core;

import io.cucumber.java.BeforeAll;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.mockito.junit.VerificationCollector;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    private WebDriver driver;
    private String projectPath = System.getProperty("user.dir");

    protected final Log log;

    protected BaseTest() {
        log = LogFactory.getLog(getClass());
    }

    private enum BROWSER {
        CHROME, FIREFOX, EDGE;
    }

    protected WebDriver getBrowserDriver(String browserName, String appUrl){
        if(browserName.equals("chrome")){
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browserName.equals("firefox")){
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browserName.equals("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else {
            throw new RuntimeException("Browser name valid");
        }

        driver.manage().timeouts().implicitlyWait(GlobalConstains.LONG_TIME, TimeUnit.SECONDS);
        driver.get(appUrl);
        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver getWebDriver() {
        return this.driver;
    }

    public boolean veryfiTrue(boolean condition) {
        boolean pass = true;
        try {
            if (condition == true) {
                System.out.println(" -------------------------- PASSED -------------------------- ");
            } else {
                System.out.println(" -------------------------- FAILED -------------------------- ");
            }
            Assert.assertTrue(condition);
        } catch (Throwable e) {
            pass = false;

            // Add lỗi vào ReportNG
//            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
//            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    public boolean verifyFailed(boolean condition) {
        boolean pass = true;
        try {
            if (condition == false) {
                System.out.println(" -------------------------- PASSED -------------------------- ");
            } else {
                System.out.println(" -------------------------- FAILED -------------------------- ");
            }
            Assert.assertFalse(condition);
        } catch (Throwable e) {
            pass = false;
//            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
//            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    public boolean verifyEquals(Object actual, Object expected) {
        boolean pass = true;
        try {
            Assert.assertEquals(actual, expected);
            System.out.println(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            pass = false;
            System.out.println(" -------------------------- FAILED -------------------------- ");
//            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
//            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    @BeforeAll
    public void deleteAllFilesInReportNGScreenshot() {
        log.info("---------- START delete file in folder ----------");
        try {
            String workingDir = System.getProperty("user.dir");
            String pathFolderDownload = workingDir + "/ReportNGScreenshots";
            File file = new File(pathFolderDownload);
            File[] listOfFiles = file.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    new File(listOfFiles[i].toString()).delete();
                }
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        log.info("---------- END delete file in folder ----------");
    }

    protected void clearDriverInstance(){
        // Browser
        try {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

//    @AfterSuite(alwaysRun = true)
//    protected void closeBrowserAndDriver() {
//        String cmd = "";
//        try {
//            String osName = System.getProperty("os.name").toLowerCase();
//            log.info("OS name = " + osName);
//
//            String driverInstanceName = driver.toString().toLowerCase();
//            log.info("Driver instance name = " + osName);
//
//            if (driverInstanceName.contains("chrome")) {
//                if (osName.contains("window")) {
//                    cmd = "taskkill /F /FI \"IMAGENAME eq chromedriver*\"";
//                } else {
//                    cmd = "pkill chromedriver";
//                }
//            } else if (driverInstanceName.contains("internetexplorer")) {
//                if (osName.contains("window")) {
//                    cmd = "taskkill /F /FI \"IMAGENAME eq IEDriverServer*\"";
//                }
//            } else if (driverInstanceName.contains("firefox")) {
//                if (osName.contains("windows")) {
//                    cmd = "taskkill /F /FI \"IMAGENAME eq geckodriver*\"";
//                } else {
//                    cmd = "pkill geckodriver";
//                }
//            } else if (driverInstanceName.contains("edge")) {
//                if (osName.contains("window")) {
//                    cmd = "taskkill /F /FI \"IMAGENAME eq msedgedriver*\"";
//                } else {
//                    cmd = "pkill msedgedriver";
//                }
//            } else if (driverInstanceName.contains("opera")) {
//                if (osName.contains("window")) {
//                    cmd = "taskkill /F /FI \"IMAGENAME eq operadriver*\"";
//                } else {
//                    cmd = "pkill operadriver";
//                }
//            } else if (driverInstanceName.contains("safari")) {
//                if (osName.contains("mac")) {
//                    cmd = "pkill safaridriver";
//                }
//            }
//                Process process = Runtime.getRuntime().exec(cmd);
//                process.waitFor();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    public int randomData(){
        Random random = new Random();
        return random.nextInt(999999);
    }
}
