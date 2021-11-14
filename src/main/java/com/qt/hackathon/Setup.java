package com.qt.hackathon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

public class Setup {

	static String resourcePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
			+ File.separator + "resources" + File.separator;
	static String chromedriver_path = resourcePath + "chromedriver.exe";
	static String extension = resourcePath + "pie_socket.crx";
	static String propPath = resourcePath + "config.properties";
	public static WebDriver driver;
	public static Properties prop;

	public Setup() throws IOException {
		prop = new Properties();
		FileInputStream ip = new FileInputStream(propPath);
		prop.load(ip);
	}

	public static WebDriver initialization() {
		long implicitTimeout = Integer.parseInt(prop.getProperty("implicitTimeout"));
		long pageLoadTimeOut = Integer.parseInt(prop.getProperty("pageLoadTimeOut"));
		System.setProperty("webdriver.chrome.driver", chromedriver_path );
		ChromeOptions options = new ChromeOptions();
        options.addExtensions(new File(extension));
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logPrefs);
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(implicitTimeout, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(pageLoadTimeOut, TimeUnit.SECONDS);
		return driver;
	}
}
