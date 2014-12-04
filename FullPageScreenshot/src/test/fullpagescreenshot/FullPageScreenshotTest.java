package test.fullpagescreenshot;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.fullpagescreenshot.FullPageScreenshot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;

public class FullPageScreenshotTest {

	public static void main(String[] args) {

		System.setProperty("webdriver.chrome.driver", "C:\\Chrome\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://ssod-qa.intra.searshc.com/ssod/login.jsp");
		driver.manage().window().maximize();

		try {
			ImageIO.write(FullPageScreenshot.getStitchedScreenshot(driver), "jpg", new File("C://screenshot//stitched_final.jpg"));
		} catch (WebDriverException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
		driver.quit();
	}


}
