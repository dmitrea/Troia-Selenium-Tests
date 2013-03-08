package tests.java.com;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.io.Zip;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestDemoApplication {

	private WebDriver driver;
	private String webServerAddress = "http://devel.project-troia.com";
	private String fileDownloadPath = "c:\\downloads";
	private int defaultSleepTimeout = 1000;

	private void waitUntilWebElementIsPresent(String id) {
		WebDriverWait wait = new WebDriverWait(driver, defaultSleepTimeout);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
	}

	private void cleanupDownloadFolder() {
		File file = new File(fileDownloadPath);
		String[] myFiles;
		if (file.isDirectory()) {
			myFiles = file.list();
			for (int i = 0; i < myFiles.length; i++) {
				File myFile = new File(file, myFiles[i]);
				myFile.delete();
			}
		}
	}

	private void unzipArchive(String zipFileName) {
		Boolean isZipEmpty = true;
		ZipFile zipFile = null;
		while (isZipEmpty) {
			try {
				zipFile = new ZipFile(zipFileName);
				isZipEmpty = false;
			} catch (Exception ex) {
				try {
					Thread.sleep(defaultSleepTimeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
		}
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File entryDestination = new File(fileDownloadPath, entry.getName());
			entryDestination.getParentFile().mkdirs();
			InputStream in;
			try {
				in = zipFile.getInputStream(entry);
				OutputStream out = new FileOutputStream(entryDestination);
				IOUtils.copy(in, out);
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			} catch (Exception ex) {
			}
		}
		System.out.println("Finished unpacking");
	}

	private String downloadZipFile()  {
		File downloadDir = new File(fileDownloadPath);
		while ((downloadDir.list().length == 0) || (new File(downloadDir.list()[0]).getName().endsWith(".part"))) {
			System.out.println("waiting ... ");
			try {
				Thread.sleep(defaultSleepTimeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String[] filesList;
		filesList = downloadDir.list();
		File zipFile = new File(downloadDir, filesList[0]);
		System.out.println("Downloaded " + zipFile.getPath());
		return zipFile.getPath();
	}
	
	private Map<String, Map<String, String>> GetGALPredictedLabels() {
		Map<String, Map<String, String>> predictedLabels = new HashMap<String, Map<String, String>>();
		List<WebElement> predictedLabelsRows = driver.findElements(By.xpath("//div[@id='labels']/div/div/table/tbody/tr"));
		ArrayList<String> algoList = new ArrayList<String>();
		for (WebElement listRow : predictedLabelsRows) {
			try {
				// we're on the table header
				for (int i = 1; i <= 5; i++) {
					algoList.add(listRow.findElement(By.xpath("th[" + i + "]")).getText());
				}
				continue;
			} catch (Exception ex) {
				String objectName = listRow.findElement(By.xpath("td[1]")).getText();
				Map<String, String> algoValues = new HashMap<String, String>();
				algoValues.put(algoList.get(1),	listRow.findElement(By.xpath("td[2]")).getText());
				algoValues.put(algoList.get(2), listRow.findElement(By.xpath("td[3]")).getText());
				algoValues.put(algoList.get(3), listRow.findElement(By.xpath("td[4]")).getText());
				algoValues.put(algoList.get(4),	listRow.findElement(By.xpath("td[5]")).getText());
				predictedLabels.put(objectName, algoValues);
			}
		}
		System.out.println(predictedLabels);
		return predictedLabels;
	}
	
	private Map<String, Map<String, String>> GetGALCPredictedLabels() {
		Map<String, Map<String, String>> predictedLabels = new HashMap<String, Map<String, String>>();
		List<WebElement> predictedLabelsRows = driver.findElements(By.xpath("//div[@id='labels']/div/table/tbody/tr"));
		for (WebElement listRow : predictedLabelsRows) {
			try {
				// we're on the table header
				listRow.findElement(By.xpath("th[1]")).getText();
				continue;
			} catch (Exception ex) {
				String objectName = listRow.findElement(By.xpath("td[1]")).getText();
				Map<String, String> algoValues = new HashMap<String, String>();
				algoValues.put("est_value",	listRow.findElement(By.xpath("td[2]")).getText());
				algoValues.put("est_zeta", listRow.findElement(By.xpath("td[3]")).getText());
				predictedLabels.put(objectName, algoValues);
			}
		}
		System.out.println(predictedLabels);
		return predictedLabels;
	}
	
	private Map<String, Map<String, String>> GetGALWorkersQuality() {
		Map<String, Map<String, String>> workersQuality = new HashMap<String, Map<String, String>>();
		List<WebElement> workersQualityRows = driver.findElements(By.xpath("//div[@id='workers']/div/table/tbody/tr"));
		for (WebElement listRow : workersQualityRows) {
			try {
				// we're on the table header
				listRow.findElement(By.xpath("th[1]")).getText();
				continue;
			} catch (Exception ex) {
				String workerName = listRow.findElement(By.xpath("td[1]")).getText();
				Map<String, String> algoValues = new HashMap<String, String>();
				algoValues.put("ExpectedCost",String.format("%.2f", Double.parseDouble(listRow.findElement(By.xpath("td[2]")).getText())));
				algoValues.put("MaxLikelihood",	String.format("%.2f",Double.parseDouble(listRow.findElement(By.xpath("td[3]")).getText())));
				algoValues.put("MinCost", String.format("%.2f",	Double.parseDouble(listRow.findElement(By.xpath("td[4]")).getText())));
				workersQuality.put(workerName, algoValues);
			}
		}
		System.out.println(workersQuality);
		return workersQuality;
	}
	
	private Map<String, Map<String, String>> GetGALCWorkersQuality() {
		Map<String, Map<String, String>> workersQuality = new HashMap<String, Map<String, String>>();
		List<WebElement> workersQualityRows = driver.findElements(By.xpath("//div[@id='workers']/table/tbody/tr"));
		for (WebElement listRow : workersQualityRows) {
			try {
				// we're on the table header
				listRow.findElement(By.xpath("th[1]")).getText();
				continue;
			} catch (Exception ex) {
				String workerName = listRow.findElement(By.xpath("td[1]")).getText();
				Map<String, String> algoValues = new HashMap<String, String>();
				algoValues.put("est_mu", listRow.findElement(By.xpath("td[2]")).getText());
				algoValues.put("est_sigma",	listRow.findElement(By.xpath("td[3]")).getText());
				algoValues.put("est_rho", listRow.findElement(By.xpath("td[4]")).getText());
				workersQuality.put(workerName, algoValues);
			}
		}
		System.out.println(workersQuality);
		return workersQuality;

	}

	@Before
	public void setUp() {
		cleanupDownloadFolder();

		FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setPreference("browser.download.folderList", 2);
		firefoxProfile.setPreference("browser.download.dir", "c:\\downloads");
		firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/zip");

		driver = new FirefoxDriver(firefoxProfile);
		driver.get(webServerAddress);
		driver.findElement(By.id("drop2")).click();
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	private void testGALScenario(int dataSet) {		
		driver.findElement(By.xpath("//ul[@id='menu2']/li[1]")).click();
		waitUntilWebElementIsPresent("send_data");

		String dataChooseLocator = "//select[@id='id_data_choose']/option[@value='"	+ dataSet + "']";
		driver.findElement(By.xpath(dataChooseLocator)).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		driver.findElement(By.id("send_data")).click();
		waitUntilWebElementIsPresent("download_zip_btn");

		driver.findElement(By.id("download_zip_btn")).click();
		String zipFileName = downloadZipFile();
		unzipArchive(zipFileName);
		Map<String, Map<String, String>> webPredictedLabels = GetGALPredictedLabels();
		driver.findElement(By.xpath("//ul[@id='resultsTab']/li[2]")).click();
		Map<String, Map<String, String>> webWorkersQuality = GetGALWorkersQuality();

		FileParser fileParser = new FileParser();
		Map<String, Map<String, String>> filePredictedLabels = fileParser.ParseGALPredictionObjectsFile(fileDownloadPath + "\\prediction.tsv");
		Map<String, Map<String, String>> fileWorkersQuality = fileParser.ParseGALWorkersResultsFile(fileDownloadPath + "\\workers_quality.tsv");
		assertEquals(webPredictedLabels, filePredictedLabels);
		assertEquals(webWorkersQuality, fileWorkersQuality);
	}

	@Test
	public void TestGAL_DownloadZipResults_AdultContentDataSet() {
		testGALScenario(1);
	}

	@Test
	public void TestGAL_DownloadZipResults_BarzanMozafari() {
		testGALScenario(2);
	}
	
	@Test
	public void TestGAL_DownloadZipResults_HistoricalJobs() {
		driver.findElement(By.xpath("//ul[@id='menu2']/li[1]")).click();
		waitUntilWebElementIsPresent("send_data");

		String dataChooseLocator = "//select[@id='id_data_choose']/option[@value='1']";
		driver.findElement(By.xpath(dataChooseLocator)).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		driver.findElement(By.id("send_data")).click();
		waitUntilWebElementIsPresent("download_zip_btn");

		String resultsUrl = driver.findElement(By.xpath("//div[@id='url']/pre")).getText();
		
		driver.get(resultsUrl);
		waitUntilWebElementIsPresent("download_zip_btn");

		driver.findElement(By.id("download_zip_btn")).click();
		String zipFileName = downloadZipFile();
		assertNotEquals("", zipFileName);
	}
	
	@Test
	public void  TestGALC_DownloadZipResults() {		
		driver.findElement(By.xpath("//ul[@id='menu2']/li[2]")).click();
		waitUntilWebElementIsPresent("send_data");

		driver.findElement(By.id("send_data")).click();
		waitUntilWebElementIsPresent("download_zip_btn");

		driver.findElement(By.id("download_zip_btn")).click();
		String zipFileName = downloadZipFile();
		unzipArchive(zipFileName);
		Map<String, Map<String, String>> webPredictedLabels = GetGALCPredictedLabels();
		driver.findElement(By.xpath("//ul[@id='resultsTab']/li[2]")).click();
		Map<String, Map<String, String>> webWorkersQuality = GetGALCWorkersQuality();

		FileParser fileParser = new FileParser();
		Map<String, Map<String, String>> filePredictedLabels = fileParser.ParseGALCPredictionObjectsFile(fileDownloadPath + "\\prediction.tsv");
		Map<String, Map<String, String>> fileWorkersQuality = fileParser.ParseGALCWorkersResultsFile(fileDownloadPath + "\\workers_quality.tsv");
		assertEquals(webPredictedLabels, filePredictedLabels);
		assertEquals(webWorkersQuality, fileWorkersQuality);
	}
	
	@Test
	public void TestGALC_DownloadZipResults_HistoricalJobs() {
		driver.findElement(By.xpath("//ul[@id='menu2']/li[2]")).click();
		waitUntilWebElementIsPresent("send_data");

		driver.findElement(By.id("send_data")).click();
		waitUntilWebElementIsPresent("download_zip_btn");
		
		String resultsUrl = driver.findElement(By.xpath("//div[@id='url']/pre")).getText();
		
		driver.get(resultsUrl);
		waitUntilWebElementIsPresent("download_zip_btn");

		driver.findElement(By.id("download_zip_btn")).click();
		String zipFileName = downloadZipFile();
		assertNotEquals("", zipFileName);
	}
}
