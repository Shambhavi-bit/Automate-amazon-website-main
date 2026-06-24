package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Created by tmaher on 12/22/2015.
 */
public class DriverUtils {

    public static  WebDriver _driver;

    @BeforeSuite(alwaysRun = true)
    public void setUp() {
        try {
            WebDriverManager.edgedriver().setup();
        } catch (Exception e) {
            System.out.println("WebDriverManager failed to setup edgedriver, falling back to system PATH: " + e.getMessage());
        }
        // If user provided explicit driver path via system property or env var, use it
        String driverPath = System.getProperty("msedgedriver.path");
        if (driverPath == null || driverPath.trim().isEmpty()) {
            driverPath = System.getenv("MSEDGEDRIVER_PATH");
        }
        EdgeOptions options = new EdgeOptions();
        if (driverPath != null && !driverPath.trim().isEmpty()){
            try {
                java.io.File exe = new java.io.File(driverPath);
                org.openqa.selenium.edge.EdgeDriverService service = new org.openqa.selenium.edge.EdgeDriverService.Builder()
                        .usingDriverExecutable(exe)
                        .usingAnyFreePort()
                        .build();
                _driver = new EdgeDriver(service, options);
            } catch (Exception e){
                System.out.println("Failed to start EdgeDriver with provided path, falling back to default: " + e.getMessage());
                _driver = new EdgeDriver(options);
            }
        } else {
            _driver = new EdgeDriver(options);
        }
    }

    public static WebDriver getDriver() {
        if (_driver == null) {
            try {
                WebDriverManager.edgedriver().setup();
            } catch (Exception e) {
                System.out.println("WebDriverManager failed to setup edgedriver, falling back to system PATH: " + e.getMessage());
            }
            String driverPath = System.getProperty("msedgedriver.path");
            if (driverPath == null || driverPath.trim().isEmpty()) {
                driverPath = System.getenv("MSEDGEDRIVER_PATH");
            }
            EdgeOptions options = new EdgeOptions();
            if (driverPath != null && !driverPath.trim().isEmpty()){
                try {
                    java.io.File exe = new java.io.File(driverPath);
                    org.openqa.selenium.edge.EdgeDriverService service = new org.openqa.selenium.edge.EdgeDriverService.Builder()
                            .usingDriverExecutable(exe)
                            .usingAnyFreePort()
                            .build();
                    _driver = new EdgeDriver(service, options);
                } catch (Exception e){
                    System.out.println("Failed to start EdgeDriver with provided path, falling back to default: " + e.getMessage());
                    _driver = new EdgeDriver(options);
                }
            } else {
                _driver = new EdgeDriver(options);
            }
        }
        return _driver;
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws Exception {
        _driver.close();
        _driver.quit();
    }
}
