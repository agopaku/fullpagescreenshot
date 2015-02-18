package main.fullpagescreenshot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

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
	 * @throws InterruptedException 
	 */
	/**
	 * @param driver
	 * @return
	 * @throws WebDriverException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unchecked")
	public static BufferedImage getStitchedScreenshot(WebDriver driver) throws WebDriverException, IOException, InterruptedException {

		long pageHeight 				= (long)((JavascriptExecutor)driver).executeScript("return document.body.clientHeight");
		long pageWidth					= (long)((JavascriptExecutor)driver).executeScript("return document.body.clientWidth");
		long viewportHeight 			= (long)((JavascriptExecutor)driver).executeScript("return window.innerHeight");
		BufferedImage stitchedImage 	= new BufferedImage((int)pageWidth, (int)pageHeight, BufferedImage.TYPE_INT_RGB);
		long windowYOffset = 0;
		int screens;
		
		/***************************************************************************************************************************************************************	
		 * The following code is used to fetch all the css style files present in the current HTML page
		 * This is to find all the elements with the style 'position: absolute'
		 * We replace all those styles with 'position: fixed', so that in the final screenshot, the overlays in the site will not be present repeatedly in all the pages
		 ***************************************************************************************************************************************************************/
 
		String jsForFetchingAbsoluteElements = 
				"function getStylesWithPositionFixed(style, value) {"+
						"var listOfFixedPositions = [];"+
						"var index = 0;"+
						"for (var i = 0; i < document.styleSheets.length; i++) {"+
						"var sheet = document.styleSheets[i];"+
						"if( !sheet.cssRules ) { continue; }"+
						"for (var j = 0; j < sheet.cssRules.length; j++) {"+
						"var rule = sheet.cssRules[j];"+
						"if (rule.selectorText) {"+
						"if(rule.style[style] == value)"+ 
						"listOfFixedPositions[index++] = rule.selectorText;"+	
						"}}}return listOfFixedPositions;"+
						"};";

		for(Object absoluteElement : (List<Object>) ((JavascriptExecutor)driver).executeScript(jsForFetchingAbsoluteElements + "return getStylesWithPositionFixed('position','fixed');")) {
			for( WebElement absoluteWebElement : driver.findElements(By.cssSelector(absoluteElement.toString()))) {
				String existingStyle=absoluteWebElement.getAttribute("style");
				existingStyle = existingStyle.contains("position: fixed;") ? existingStyle.replace("position: fixed;","position: absolute;") : existingStyle.concat(" position: absolute;");
				((JavascriptExecutor)driver).executeScript("return arguments[0].setAttribute('style', arguments[1]);",absoluteWebElement,existingStyle);	
			}
		}

		Graphics graphicsObject = stitchedImage.getGraphics();
		
		/***************************************************************************************************************************************************************	
		 * The following code is used to scroll to the bottom portion of the screen, so that, if the page consists of any lazy loads, this will be added to the 
		 * stitched screenshot
		 ***************************************************************************************************************************************************************/
		
		for(int countOfWaitsForLazyLoad = 0; countOfWaitsForLazyLoad < 10;) {
			windowYOffset = (long)((JavascriptExecutor)driver).executeScript("return window.pageYOffset");
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,"+pageHeight+");");
			for(int waitForScrollToHappen = 10; windowYOffset == (long)((JavascriptExecutor)driver).executeScript("return window.pageYOffset") 
					&& waitForScrollToHappen > 0; waitForScrollToHappen--) Thread.sleep(1000);
			if((pageHeight = pageHeight < (long)((JavascriptExecutor)driver).executeScript("return document.body.clientHeight") ? 
					(long)((JavascriptExecutor)driver).executeScript("return document.body.clientHeight") : pageHeight)
					== pageHeight) 
				countOfWaitsForLazyLoad ++;				
			else
				countOfWaitsForLazyLoad = 0;				
		}
		
		/***************************************************************************************************************************************************************	
		 * The following code is used to capture the screenshot of each screen. It captures the screenshot, uses js to scroll to the next screen
		 * and validates whether the scroll to the next screen has worked using the window.pageYOffset command. If the scroll did not work, it will wait for a second 
		 * and check again. It waits for a max of 10 seconds.
		 * For the last screen, if the complete view port size is not present, there will be a portion of the screen, which was there in the previous screen. 
		 * Initially, this will also be added to the stitched screenshot
		 ***************************************************************************************************************************************************************/

		for(screens = 0; screens <= pageHeight / viewportHeight ; screens ++) {
			graphicsObject.drawImage(ImageIO.read(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)), 0, (int) (screens * viewportHeight), null);
			windowYOffset = (long)((JavascriptExecutor)driver).executeScript("return window.pageYOffset");
			((JavascriptExecutor)driver).executeScript("window.scrollBy(0,"+viewportHeight+");");
			for(int waitForScrollToHappen = 10; windowYOffset == (long)((JavascriptExecutor)driver).executeScript("return window.pageYOffset") 
					&& waitForScrollToHappen > 0; waitForScrollToHappen--) Thread.sleep(1000); 
		}
		
		/***************************************************************************************************************************************************************
		 * The following code is used to get the bottom part of the last screen, which was left out while stitching. Adding this to the already stitched image will 
		 * render the final image
		 **************************************************************************************************************************************************************/
		if(screens != 1)
			graphicsObject.drawImage(ImageIO.read(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)), 
					0, (int) (pageHeight - pageHeight % viewportHeight), (int)pageWidth, (int)pageHeight, 
					0, (int) (viewportHeight - pageHeight % viewportHeight), (int)pageWidth, (int)viewportHeight, 
					null);
		graphicsObject.dispose();		
		return stitchedImage;
	}
}
