package spicejetUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SpicejetUtils {
	public static WebDriver driver;
	public String sheetName;
	
	public static String readProperty(String key) throws Exception {
		String projectPath = System.getProperty("user.dir");
		File file = new File(projectPath + "/configSpiceJet.properties");
		FileInputStream fileInput = new FileInputStream(file);
		Properties prop = new Properties();
		prop.load(fileInput);
		return prop.get(key).toString();
	}

	public static void launchBrowser(String browser) {
		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options=new FirefoxOptions();
			options.addArguments("--headless");
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else {
			System.out.println("Opening Chrome browser as Default browser");
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		waitImplicit();
	}

	public static void waitExplicit(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOf(element));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public static void waitUntillVisiblity(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void waitExplicitUntillTitle(String titleToWait) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.titleIs(titleToWait));
	}

	public static void waitImplicit() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public static void getApplication(String url) {
		driver.get(url);
	}

	public static void type(WebElement element, String text) {
		element.sendKeys(text);
	}

	public static void clickOn(WebElement element) {
		waitExplicit(element);
		element.click();
	}

	public static String getPageTitle() {
		waitExplicitUntillTitle(driver.getTitle());
		return driver.getTitle();
	}

	public static String extractText(WebElement element) {
		waitExplicit(element);
		return element.getText();
	}

	public static int getResponseCode(String url) throws Exception, Exception {
		URL link = new URL(url);
		HttpURLConnection connect = (HttpURLConnection) link.openConnection();
		// connect.setRequestMethod("Head");
		// connect.connect();
		int responseCode = connect.getResponseCode();
		return responseCode;
	}

	public static Object[][] dataReader(String sheetName) throws Exception {
		String excelPath = System.getProperty("user.dir");
		XSSFWorkbook workBook = new XSSFWorkbook(excelPath + "/Excel/SpiceJet.xlsx");
		XSSFSheet sheet = workBook.getSheet(sheetName);
		int row = sheet.getPhysicalNumberOfRows();
		int column = sheet.getRow(0).getPhysicalNumberOfCells();
		Object[][] data = new Object[row][column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				//data[i][j] = sheet.getRow(i).getCell(j) + "".toString();
				data[i][j] = sheet.getRow(i).getCell(j).getStringCellValue();
			}
		}
		workBook.close();
		return data;
	}

	public static void selectFromDropDown(WebElement element, String visibleText) {
		waitExplicit(element);
		Select select = new Select(element);
		select.selectByVisibleText(visibleText);
	}

	public static void titleAssertion(String expTitle) {
		Assert.assertEquals(getPageTitle(), expTitle);
	}
	
	public static void textAssertion(WebElement element, String expectedText) {
		Assert.assertEquals(extractText(element), expectedText);
	}

	public static void jsScrollUntillElement(WebElement element) {
		waitExplicit(element);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public static void jsClickOn(WebElement element) {
		waitExplicit(element);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public static void softAssert(String actResult, String expResult) {
		SoftAssert sa = new SoftAssert();
		sa.assertEquals(actResult, expResult);
	}
	
	public static String getScreenshot(String testCaseName) throws Exception, IOException {
		String time = getTime();
		String path=System.getProperty("user.dir")+"/screenshot/"+testCaseName+time+".png";
		FileUtils.copyFile(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE), new File(path));
		return path;
	}
	
	public static String getParentWindowId() {
		return driver.getWindowHandle();
	}
	
	public static void switchToChildWindow(String parentWindowId) {
		Set<String> allWindows = driver.getWindowHandles();
		for(String windows : allWindows) {
			if(windows!=parentWindowId) {
				driver.switchTo().window(windows);
			}
		}
	}
	
	public static String getTime() {
		DateFormat dateFormat = null;
		Date date = null;
		try {
			dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			date = new Date();
		} catch (Exception e) {
			System.out.println("Error in Getdateandtime : " + e.getMessage());
		}

		return dateFormat.format(date);
	}
	
	public static void switchToParentWindow(String parentWindowId) {
		driver.switchTo().window(parentWindowId);
	}
	
	public static void handleAlert() {
		driver.switchTo().alert().accept();
	}
	
	public void switchToFrame(WebElement elemnt) {
		driver.switchTo().frame(elemnt);
	}
	
	public void toDefaultContent() {
		driver.switchTo().defaultContent();
	}
	
	public boolean isElementDisplayed(WebElement element) {
		return element.isDisplayed();
	}
}
