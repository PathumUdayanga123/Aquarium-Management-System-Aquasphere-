package com.example.AquaSphere.Backend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

	private static WebDriver driver;
	private static final String FRONTEND_URL = "http://localhost:5173";
	private static final String TEST_EMAIL = "bandarapathum123@gmail.com";
	private static final String TEST_PASSWORD = "Pathum222";

	@BeforeAll
	public static void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@AfterAll
	public static void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testSuccessfulLogin() {
		try {
			// Navigate to login page
			driver.get(FRONTEND_URL + "/login");

			// Wait for page elements
			WebElement emailField = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
			WebElement passwordField = driver.findElement(By.id("password"));
			WebElement loginButton = driver.findElement(By.className("login-button"));

			// Enter valid credentials
			emailField.sendKeys(TEST_EMAIL);
			passwordField.sendKeys(TEST_PASSWORD);

			// Click login button
			loginButton.click();

			// Wait for successful redirect
			new WebDriverWait(driver, Duration.ofSeconds(15))
					.until(ExpectedConditions.or(
							ExpectedConditions.urlContains("/homepage"),
							ExpectedConditions.urlContains("/admin")
					));

			// Verify final destination
			String currentUrl = driver.getCurrentUrl();
			assertTrue(currentUrl.contains("/homepage") || currentUrl.contains("/admin"),
					"Not redirected to proper page after login");

			// Verify localStorage items
			JavascriptExecutor js = (JavascriptExecutor) driver;
			assertNotNull(js.executeScript("return localStorage.getItem('userId')"),
					"User ID not stored in localStorage");
			assertNotNull(js.executeScript("return localStorage.getItem('userEmail')"),
					"Email not stored in localStorage");

			// Verify page content
			WebElement welcomeElement = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.visibilityOfElementLocated(
							By.cssSelector("[data-testid='welcome-message']")
					));
			assertNotNull(welcomeElement, "Welcome message not displayed");

		} catch (Exception e) {
			// Capture screenshot on failure
			((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			System.err.println("Test failed at URL: " + driver.getCurrentUrl());
			System.err.println("Page source:\n" + driver.getPageSource());
			throw e;
		}
	}

	@Test
	public void testCorrectEmailWrongPassword() {
		driver.get(FRONTEND_URL + "/login");

		WebElement emailField = driver.findElement(By.id("email"));
		emailField.sendKeys(TEST_EMAIL);  // Correct email
		driver.findElement(By.id("password")).sendKeys("WrongPassword123");  // Wrong password
		driver.findElement(By.className("login-button")).click();

		// Verify error message
		WebElement errorMessage = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("error-message")));

		assertTrue(errorMessage.getText().contains("Login failed"),
				"Error message not shown for wrong password");

		// Verify still on login page
		assertTrue(driver.getCurrentUrl().contains("/login"),
				"User should remain on login page after failed attempt");
	}

	@Test
	public void testInvalidCredentials() {
		driver.get(FRONTEND_URL + "/login");

		// Test wrong email + wrong password
		driver.findElement(By.id("email")).sendKeys("invalid@example.com");
		driver.findElement(By.id("password")).sendKeys("wrongpass");
		driver.findElement(By.className("login-button")).click();

		WebElement errorMessage = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("error-message")));

		assertTrue(errorMessage.getText().contains("Login failed"),
				"Error message not shown for invalid credentials");

		// Verify URL remains on login page
		assertTrue(driver.getCurrentUrl().contains("/login"),
				"User should stay on login page after invalid attempt");
	}
}