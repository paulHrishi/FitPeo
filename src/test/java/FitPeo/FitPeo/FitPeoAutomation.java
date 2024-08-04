package FitPeo.FitPeo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Keys;

public class FitPeoAutomation {
	public static void main(String[] args) {
		// Set the path to the ChromeDriver executable
		//System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
		WebDriverManager.chromedriver().setup();
		// Initialize the Chrome WebDriver
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		try {

			// Navigate to the FitPeo Homepage
			driver.get("https://www.fitpeo.com");
			// Wait for the homepage to load
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
			// Navigate to the Revenue Calculator Page
			WebElement revenueCalculatorLink = driver.findElement(By.linkText("Revenue Calculator"));
			revenueCalculatorLink.click();

			// Wait for the Revenue Calculator page to load
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='range']")));
			// Scroll down to the Slider section
			WebElement sliderSection = driver.findElement(By.xpath("//input[@type='range']"));
			((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", sliderSection);

			// Adjust the Slider to set its value to 820
			WebElement slider = driver.findElement(By.cssSelector("input[type='range']"));
			WebElement sliderValueField = driver.findElement(By.xpath("//input[contains(@class,'MuiInputBase-input')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(slider, (93), 0).click().build().perform();
			Thread.sleep(2000);
			sliderValueField.sendKeys(Keys.BACK_SPACE);
			sliderValueField.sendKeys(Keys.BACK_SPACE);
			sliderValueField.sendKeys(Keys.BACK_SPACE);
			sliderValueField.click();
			sliderValueField.sendKeys("820");
			//actions.clickAndHold(slider).moveByOffset(93, 0).release().perform();
			//actions.dragAndDropBy(slider, 93, 0).perform();
			Thread.sleep(1000); // Wait for the value to update
			// Ensure the value is set to 820
			if (!sliderValueField.getAttribute("value").equals("820")) {
				throw new AssertionError("Slider value is not 820");
			}

			// Update the Text Field to 560
			sliderValueField.click();
			Thread.sleep(2000);
			sliderValueField.sendKeys(Keys.BACK_SPACE);
			sliderValueField.sendKeys(Keys.BACK_SPACE);
			sliderValueField.sendKeys(Keys.BACK_SPACE);
			//sliderValueField.clear();
			sliderValueField.sendKeys("560");
			sliderValueField.sendKeys(Keys.RETURN);
			// Validate Slider Value
			if (!slider.getAttribute("value").equals("560")) {
				throw new AssertionError("Slider value did not update to 560");
			}
			
			// Scroll down further to select CPT Codes
			WebElement cptCodeSection = driver.findElement(By.xpath("//div[contains(@class,'MuiBox-root css-1p19z09')]"));
			((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", cptCodeSection);
			
			// Select CPT Codes
			String[] cptCodes = {"CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474"};
			for (String code : cptCodes) {
				WebElement checkbox = driver.findElement(By.xpath("//p[contains(text(),'" + code + "')]//following-sibling::label/descendant::input"));
				if (!checkbox.isSelected()) {
					checkbox.click();
				}
			}

			// Validate Total Recurring Reimbursement
			WebElement totalReimbursementText = driver.findElement(By.xpath("//p[text()='Total Recurring Reimbursement for all Patients Per Month:']"));
			System.out.println("text got from xpath is"+totalReimbursementText.getText());
			if (!totalReimbursementText.getText().equals("Total Recurring Reimbursement for all Patients Per Month:\n"
					+ "$75600")) {
				throw new AssertionError("Total reimbursement value is incorrect");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the browser
			driver.quit();
		}

	}

}

