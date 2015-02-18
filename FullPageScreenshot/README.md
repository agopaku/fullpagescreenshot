This tool can be used to capture the full page screenshot of a Web Application. With the release of the version 2 of ChromeDriver, the screenshot captured will contain only the part of the web page present in the viewport. This tool will scroll through the webpage, capture and stitch the screenshots together and returns a BufferedImage object of the stitched screenshot To use this, please call the getStitchedScreenshot() defined in com.stitchscreenshot.StitchedScreenshot class

This method accepts the WebDriver object that you use in your project as parameter and returns the BufferedImage object

You can download the JAR at https://github.com/agopaku/fullpagescreenshot/blob/master/FullPageScreenshot/target/FullPageScreenshot-0.0.1-SNAPSHOT.jar 
The Java file is located at https://github.com/agopaku/fullpagescreenshot/blob/master/FullPageScreenshot/src/main/fullpagescreenshot/FullPageScreenshot.java
A sample Test Program is available at https://github.com/agopaku/fullpagescreenshot/blob/master/FullPageScreenshot/src/test/fullpagescreenshot/FullPageScreenshotTest.java

You can log issues at https://github.com/agopaku/fullpagescreenshot/issues
