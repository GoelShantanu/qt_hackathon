package com.qt.hackathon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

public class Runner extends Setup {

	public Runner() throws IOException {
		super();
		driver = Setup.initialization();
	}

	private static WebDriver driver;

	public static void main(String[] args) throws InterruptedException, IOException {

		Runner run = new Runner();
		driver.get(prop.getProperty("url"));

		// For Welcome Page

		WebElement img_door = driver.findElement(By.cssSelector("img[src*='door']"));
		img_door.click();

		// For Start Page

		WebElement btn_start = driver.findElement(By.cssSelector("button#start"));
		btn_start.click();

		// For Random Access Page

		((JavascriptExecutor) driver).executeScript(
				"document.querySelectorAll('.card-action button').forEach(e=>{if(e.onclick!=null){e.click()}})");

		// For Video Player Page

		WebElement video_frame = driver.findElement(By.id("aVideoPlayer"));
		driver.switchTo().frame(video_frame);
		WebElement play_btn = driver.findElement(By.cssSelector("button.ytp-large-play-button"));
		play_btn.click();
		Thread.sleep(11000);
		WebElement mute_btn = driver.findElement(By.cssSelector("button.ytp-mute-button"));
		mute_btn.click();
		driver.switchTo().defaultContent();
		WebElement video_proceed = driver.findElement(By.id("aVideoSubmit"));
		video_proceed.click();

		// For Crystal Maze Page

		SolveMazeUi s = new SolveMazeUi(driver);
		s.extractMaze();
		s.solveMazeUi();

		// For Map Page

		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.TAB).perform();
		actions.sendKeys("i").perform();

		int cx, cy;
		do {
			WebElement circle = driver.findElement(By.cssSelector("circle[id^='OpenLayers_Geometry_Point']"));
			cx = (int) Float.parseFloat(circle.getAttribute("cx").trim());
			actions.sendKeys(Keys.RIGHT).perform();
		} while (cx != 367);

		do {
			WebElement circle = driver.findElement(By.cssSelector("circle[id^='OpenLayers_Geometry_Point']"));
			cy = (int) Float.parseFloat(circle.getAttribute("cy").trim());
			actions.sendKeys(Keys.UP).perform();
		} while (cy != 98);

		WebElement btn_map_proceed = driver.findElement(By.id("mapsChallengeSubmit"));
		btn_map_proceed.click();

		// For "Not a Bot" Page

		String captcha_txt = extractCaptcha().replace("\"", "");
		WebElement input_captcha = driver.findElement(By.id("notABotCaptchaResponse"));
		input_captcha.sendKeys(captcha_txt);
		WebElement btn_captcha = driver.findElement(By.id("notABotCaptchaSubmit"));
		btn_captcha.click();

		// For "Socket Gate" Page

		WebElement txt_msg = driver.findElement(By.cssSelector("div.yellow"));

		SocketManager sm = new SocketManager(driver);
		String token = sm.getAccessToken(prop.getProperty("socketUrl"), txt_msg.getText().trim());
		WebElement input_token = driver.findElement(By.id("socketGateMessage"));
		input_token.sendKeys(token);
		WebElement btn = driver.findElement(By.cssSelector("#socketGate button"));
		btn.click();

		// For Congratulations Page

		WebElement final_title = driver.findElement(By.className("card-title"));
		System.out.println(final_title.getText().trim());

		driver.quit();
	}

	private static String extractCaptcha() {

		List<String> messages = new ArrayList<>();
		LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
		for (LogEntry entry : logEntries) {
			String temp = entry.getMessage().split(" ")[2];
			System.out.println(temp);
			messages.add(temp);
		}

		return messages.get(messages.size() - 1);

	}

}
