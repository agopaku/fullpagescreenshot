package main.fullpagescreenshot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * @author Anil_Gopakumar
 * This is the class definition of FullPageScreenshot. You can use the static method getStitchedScreenshot(WebDriver) to capture the full page screenshot of the Web page
 */
public class FullPageScreenshot {

	/**
	 * @param driver
	 * @return the full page screenshot of the webpage that is tested using the driver object 
	 * @throws WebDriverException
	 * @throws IOException
	 */
	public static BufferedImage getStitchedScreenshot(WebDriver driver) throws WebDriverException, IOException {

		long pageHeight 				= (long)((JavascriptExecutor)driver).executeScript("return document.body.clientHeight");;
		long pageWidth					= (long)((JavascriptExecutor)driver).executeScript("return document.body.clientWidth");
		long viewportHeight 			= (long)((JavascriptExecutor)driver).executeScript("return window.innerHeight");
		BufferedImage stitchedImage 	= new BufferedImage((int)pageWidth, (int)pageHeight, BufferedImage.TYPE_INT_RGB);

		Graphics graphicsObject = stitchedImage.getGraphics();

		for(int screens = 0; screens <= pageHeight / viewportHeight ; ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,"+viewportHeight+");"), screens ++) 
			graphicsObject.drawImage(ImageIO.read(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)), 0, (int) (screens * viewportHeight), null);
		graphicsObject.dispose();		
		return stitchedImage;
	}
}
