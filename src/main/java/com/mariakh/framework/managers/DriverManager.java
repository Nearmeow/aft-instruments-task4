package com.mariakh.framework.managers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

public class DriverManager {

    private static DriverManager instance;

    private WebDriver driver;

    private TestPropManager propManager = TestPropManager.getInstance();

    private DriverManager() {
    }

    public static DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }

    public WebDriver getDriver() {
        if (driver == null) {
            //initDriver();
            initRemoteDriver();
        }
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private void initDriver() {
        String browser = System.getProperty("browser", "chrome");
        switch (browser) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", propManager.getProperty("path.chrome.driver.windows"));
                driver = new ChromeDriver();
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", propManager.getProperty("path.firefox.driver.windows"));
                driver = new FirefoxDriver();
                break;
            case "edge":
                System.setProperty("webdriver.edge.driver", propManager.getProperty("path.edge.driver.windows"));
                driver = new EdgeDriver();
                break;
        }
    }

    private void initRemoteDriver() {
        String browser = System.getProperty("browser", "chrome");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", false
        ));
        capabilities.setBrowserName(browser);
        switch (browser) {
            case "chrome":
            case "firefox":
                capabilities.setVersion("109.0");
                break;
            case "opera":
                capabilities.setVersion("94.0");
        }
        try {
            driver = new RemoteWebDriver(URI.create("http://149.154.71.152:8080/wd/hub").toURL(), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
