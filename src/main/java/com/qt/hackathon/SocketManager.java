package com.qt.hackathon;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SocketManager {
    private String extensionUrl = "chrome-extension://oilioclnckkoijghdniegedkbocfpnip/pages/advance-tester.html?";
    private WebDriver driver;

    SocketManager(WebDriver driver) {
        this.driver = driver;
    }

    public String getAccessToken(String url, String msg) throws InterruptedException {
        String token = "";
        String originalHandle = driver.getWindowHandle();

        System.out.println(driver.getWindowHandle());
        ((JavascriptExecutor) driver).executeScript(
                "window.open('" + extensionUrl + "','_blank');");

        for(String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
            }
        }
        waitForPageToLoadCompletely();
        WebElement input_url = driver.findElement(By.cssSelector("input[placeholder='Websocket URL']"));
        WebElement btn_primary = driver.findElement(By.cssSelector("button[class*='primary']"));
        input_url.clear();
        input_url.sendKeys(url);
        btn_primary.click();
        waitForPageToLoadCompletely();
        Thread.sleep(3000);
        WebElement input_msg = driver.findElement(By.cssSelector("input[placeholder='Message']"));
        input_msg.clear();
        input_msg.sendKeys(msg);
        btn_primary = driver.findElement(By.cssSelector("button[class*='primary']"));
        btn_primary.click();
        Thread.sleep(3000);
        WebElement txt_log = driver.findElement(By.id("logholder"));
        int index = txt_log.getText().lastIndexOf("RECEIVED:");
        token = txt_log.getText().substring(index).replace("RECEIVED: ", "").trim();

        for(String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
                driver.close();
            }
        }
        driver.switchTo().window(originalHandle);
        return token;
    }


    public void waitForPageToLoadCompletely() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver wdriver) {
                return String.valueOf(((JavascriptExecutor) wdriver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("*")));
    }
}