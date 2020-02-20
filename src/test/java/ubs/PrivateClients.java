package ubs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class PrivateClients {
    private WebDriver driver;
    private final static String TAG = "TAG";
    private int conversionValue;

    @Before
    public void login() {
        Properties base = new Properties();
        try {
            base.load(new FileReader("src/test/resources/ubs/settings.ini"));
            System.setProperty("webdriver.gecko.driver", base.getProperty("webdriver.gecko.driver"));
        } catch (IOException e) {
            Logger.getLogger(TAG).info("Exception, tests intentionally will fail");
        }

        driver = new FirefoxDriver();

        //FIXME: check if it works for Chrome driver
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
    }

    @After
    public void logout() {
        driver.close();
    }

    @Given("opened page is {string}")
    public void opened_page_is(String path) {
        driver.get("https://www.ubs.com");

        if (path.isEmpty()) return;

        String[] tempArray = path.split("\\.");
        for (String pathPart : tempArray) {
            driver.findElement(By.linkText(pathPart)).click();
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, 60);

        wait.until(driver -> js
                .executeScript("return document.readyState;")
                .equals("complete"));
    }

    @When("I click link with text {string}")
    public void i_click_link_with_text(String text) {
        driver.findElement(By.linkText(text)).click();
    }

    @When("I click link with URL {string}")
    public void i_click_link_with_URL(String url) {
        driver.get(url);
    }

    @Then("I should see {string} link")
    public void i_should_see_link(String text) {
        assertNotNull(driver.findElement(By.linkText(text)));
    }

    @Then("I should open page {string} with {string}")
    public void i_should_open_page_with(String url, String title) {
        assertEquals(url, driver.getCurrentUrl());
        assertEquals(title, driver.getTitle());
    }

    @Then("I should see page with {string} title")
    public void i_should_see_page_with_title(String title) {
        assertEquals(title, driver.getTitle());
    }

    @Then("I should see amount field")
    public void i_should_see_amount_field() {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(presenceOfElementLocated(By.id("ccyCalcAmount")));
        assertNotNull(element);
    }

    private String getConversionValue() {
        return driver.findElement(By.xpath("//p[@class='grt_resultPrice']"))
                .getAttribute("textContent");
    }

    @When("I enter {string} in the amount field and it is calculated")
    public void i_enter_in_the_amount_field_and_it_is_calculated(String newValue) {
        String oldValue = getConversionValue();

        driver.findElement(By.id("ccyCalcAmount")).clear();
        driver.findElement(By.id("ccyCalcAmount")).sendKeys(newValue);

        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(driver -> !getConversionValue().equals(oldValue));
    }

    @Then("I can get conversion value")
    public void i_can_get_conversion_value() {
        String value = getConversionValue();
        Logger.getLogger(TAG).info("Conversion value string is " + value);

        conversionValue = Integer.parseInt(
                value.substring(4, value.length() - 3)
                        .replace(".", "")
                        .replace("'", ""));
    }

    @Then("I can get conversion value and it is 10x bigger")
    public void i_can_get_conversion_value_and_it_is_10x_bigger() {
        int oldValue = conversionValue;
        i_can_get_conversion_value();
        assertEquals(oldValue * 10, conversionValue);
    }

    @When("I click element with JS and text {string}")
    public void i_click_element_with_JS_and_text(String text) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath("//span[text()='" + text + "']"));
        js.executeScript("arguments[0].click();", element);
    }
}
