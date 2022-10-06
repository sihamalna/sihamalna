package automation;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AppTest {

	
	String firstName = "Rohit";
	String lastName = "Sharma";
	String email = "sharma_rohit@gmail.com";
	String phone = "9398833674";
	
	static ExtentHtmlReporter reporter;
	static ExtentReports report;
	static ExtentTest test;
	ChromeDriver driver;
	String url = "http://localhost:8080/myproject.com/";
//	String url = System.getProperty("user.dir") + "/src/test/java/webServer/index.html";
	static File file = new File(System.getProperty("user.dir") + "/Report/Automation_.html");
		
	@BeforeClass
	public static void initilizeReport() {
		reporter = new ExtentHtmlReporter(file);
		reporter.config().setEncoding("utf-8");
		reporter.config().setDocumentTitle("Final Year Report");
		reporter.config().setReportName("Automation Report for Web & API");
		reporter.config().setTheme(Theme.STANDARD);
		
		report =  new ExtentReports();
		report.attachReporter(reporter);
		report.setSystemInfo("Enviroment", "Automation Test");
		report.setSystemInfo("Platform", System.getProperty("os.name"));
	}
	
	@Test
	public void apiTesting() {
		test = report.createTest("<b>[API]</b> Validate response code");
		try {
			
			RestAssured.baseURI = "http://localhost:8080";
			RestAssured.basePath = "/myproject.com/";
			
			
			Response response =
								given()
									.headers("Accept", "*/*")
									.headers("Accept-Encoding", "gzip, deflate, br")
									.headers("Connection", "keep-alive")
									.headers("sec-ch-ua", "'Google Chrome';v='105', 'Not)A;Brand';v='8', 'Chromium';v='105'")
									.headers("sec-ch-ua-mobile", "?0")
									.headers("sec-ch-ua-platform", "'Windows'")
								.when()
									.get()
								.then()
									.extract().response();
			long statusCode = response.getStatusCode();
			System.out.print("API status code: "+statusCode);
			if(statusCode == 200) {
				test.pass("Response status code: </b>"+statusCode+"</b>");
			} else {
				test.fail("Response status code: </b>"+statusCode+"</b>");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			test.fail("Exception occuring: "+ e);
		}
	}
	
	@Test
	public void webTesting() throws InterruptedException {
		test = report.createTest("<b>[User Interface]</b> Validate fields are inserting as expected");
		try {
			WebDriverManager.chromedriver().setup();
			driver= new ChromeDriver();
			if(driver != null) {
				test.pass("Chrome browser launched sucessfully !!!");
				driver.manage().window().maximize();
				
			}else {
				test.error("<b>[FAIL]</b> Chrome browser is not launched");
			}
			
			driver.navigate().to(url);
			Thread.sleep(5000);
			System.out.println(driver.getCurrentUrl());
			if(url.equals(driver.getCurrentUrl())) {
				test.pass("URL <b>"+url+"</b> opens sucessfully");
				
			} else {
				test.error("<b>[FAIL]</b> URL is not open");
			}
			
			driver.findElement(By.name("first_name")).sendKeys(firstName);
			test.pass("First name is inserted as <b>"+firstName+"</b> successfully");
			Thread.sleep(2000);
			driver.findElement(By.name("last_name")).sendKeys(lastName);
			test.pass("Last name is inserted as <b>"+lastName+"</b> successfully");
			Thread.sleep(2000);
			driver.findElement(By.name("email")).sendKeys(email);
			test.pass("Email Address is inserted as <b>"+email+"</b> successfully");
			Thread.sleep(2000);
			driver.findElement(By.name("phone")).sendKeys(phone);
			test.pass("Phone Number is inserted as <b>"+phone+"</b> successfully");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[@type='reset']")).click();
			test.pass("Reset button clicked successfully");
			Thread.sleep(2000);
			System.out.println(test.getStatus());
			
		} catch (Exception e) {
			e.printStackTrace();
			test.fail("Exception occuring: "+ e);
		}
		test.pass("Chrome browser closed successfully");
		
		driver.close();
		String status = String.valueOf(test.getStatus());
		if(status.equals("pass")) {
			test.pass(MarkupHelper.createLabel("Test Case is : "+status, ExtentColor.GREEN));
		} else {
			test.fail(MarkupHelper.createLabel("Test Case is : "+status, ExtentColor.RED));
		}
	}

	@AfterClass
	public static void shutdown() {
		report.flush();
	}
	
}
