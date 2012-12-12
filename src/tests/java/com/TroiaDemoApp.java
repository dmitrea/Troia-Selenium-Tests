package tests.java.com;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class TroiaDemoApp extends Thread {

	private WebDriver driver;
	private Lock lock = new ReentrantLock();
	private static int repeat = 10;
	private static int initDriverAttempts = 5;
	private static int initDriverTimeout = 5000;
	private int defaultPageTimeout = 15000;

	public int ChooseRandomDataset() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		return randomInt % 2;
	}

	public TroiaDemoApp(String clientName) {
		super(clientName);
	}
	
	public void waitUntilWebElementIsPresent(final String webElementId){
		WebElement webElement = null;
		Wait<WebDriver> wait = new WebDriverWait(driver, defaultPageTimeout);
		wait.until(new ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver webDriver) {
		                System.out.println("Searching ...");
			            return webDriver.findElement(By.id(webElementId)) != null;
			            }
			        });
	}

	public Boolean InitiateDriver() {
		int noTries = 0;
		Boolean isDriverInstantiated = false;
		while ((noTries < initDriverAttempts) && (!isDriverInstantiated)) {
			System.out.println(super.toString()
					+ "tries to initiate driver - attempt no: " + noTries);
			try {
				lock.tryLock();
				driver = new FirefoxDriver();
				lock.unlock();
				isDriverInstantiated = true;
			}

			catch (WebDriverException ex) {
				noTries++;
				try {
					Thread.currentThread().sleep(initDriverTimeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return isDriverInstantiated;
	}
	
	public int GetExpectedNoOfPredictedLabels(int dataSetType){
		 switch (dataSetType) {
         case 1: 
        	 return 5;
         case 2:
        	 return 1000;
         default:
        	 return 0;
		 }
	}
	
	public int GetExpectedNoOfWorkers(int dataSetType){
		switch (dataSetType) {
        case 1: 
       	 	return 5;
        case 2:
       	 	return 82;
        default:
       	 	return 0;
		}
	}

	public void run() {

		Boolean instantiatedDriver = this.InitiateDriver();
		if (instantiatedDriver) {
			try {

				// Go to the main troia demo app;
				driver.get("http://www.project-troia.com/tutorials/1_demo_application.html");

				for (int i = 0; i < repeat; i++) {

					// click on Input button
					driver.findElement(By.xpath("//ul[@id='menuTab']/li[1]/a")).click();

					// select a random data set
					int dataSetType = ChooseRandomDataset() + 1;
					String dataChooseLocator = "//select[@id='id_data_choose']/option[@value='" + dataSetType + "']";
					driver.findElement(By.xpath(dataChooseLocator)).click();
					Thread.sleep(1000);
				
					WebElement processButton = driver.findElement(By.id("send_data"));
					processButton.click();

					//take a nap
					Thread.sleep(defaultPageTimeout);
					
					// make some basic checkings
					String resultsUrl = driver.findElement(By.id("url")).getText();

					System.out.println(resultsUrl);
					Assert.assertTrue(resultsUrl.startsWith("You can see later your results at:"));

					// check the no of predicted labels lines
					WebElement predictedLabelsTable = driver.findElement(By.id("classes"));
					int noPredictedLabels = predictedLabelsTable.findElements(By.xpath("div/table/tbody/tr")).size() - 1;
					System.out.println(super.toString() + ": noPredictedLabels = " + noPredictedLabels);
					Assert.assertEquals(noPredictedLabels, GetExpectedNoOfPredictedLabels(dataSetType));

					// check the no of rows in workerStatistics table
					WebElement workerStatisticsTable = driver.findElement(By.id("workers"));
					int noWorkerStatistics = workerStatisticsTable.findElements(By.xpath("div/table/tbody/tr")).size() - 1;
					System.out.println(super.toString() + ": noWorkerStatistics = " + noWorkerStatistics);
					Assert.assertEquals(noWorkerStatistics, GetExpectedNoOfWorkers(dataSetType));
				}
				driver.close();

			} catch (Exception ex) {
				System.out.println(ex.getStackTrace());
			}
		}
		else {
			Assert.fail("Could not instantiate webdriver");
		}
	}
}
