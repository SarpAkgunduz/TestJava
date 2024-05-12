package TestiniumBeymen;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

public class ProjectObjects {
    WebDriver driver;
    WebDriverWait wait;

    public ProjectObjects(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    public void acceptCookies() {
    	// deny cookies
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#onetrust-accept-btn-handler"))).click();
    }

    public void selectGender() {
    	// select gender as male
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("genderManButton"))).click();
    }
    
    public void focusSearchBar() {
    	// search bar is a dynamic element so it changes after clicking and I wrote this script to click to search bar so that it turns to static and writable mode
    	WebElement focusbar = driver.findElement(By.cssSelector("body > header > div > div > div.col-4.col-sm-4.col-md-4.col-lg-4.col-xl-6 > div > div > input"));
    	focusbar.click();
    }
    public void search(String text) {
        // Wait for the search bar to be visible
    	WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#o-searchSuggestion__input")));
        searchBar.click();
    	searchBar.sendKeys(text);
    }

    public void cleanSearchBar() {
    	// cleans all the text at the search bar and I tried to use implicit wait but it did not work so I used thread sleep 
    	try {
    	    Thread.sleep(1000);
    	} catch (InterruptedException e) {
    	    e.printStackTrace();
    	}

    	WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#o-searchSuggestion__input")));
    	searchBar.clear();
    }
    
    public void enterSearch() {
    	// sends enter key to search bar
    	driver.findElement(By.cssSelector("#o-searchSuggestion__input")).sendKeys(Keys.ENTER);
    }
    public void returnHomePage() {
        driver.findElement(By.cssSelector("a.o-header__logo")).click();
    }
    public void denyPopup() {
    	WebElement denyButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#dengage-push-perm-slide > div > div.dn-slide-body > div.dn-slide-buttons.horizontal > button.dn-slide-deny-btn")));
    	denyButton.click();
    }
    
    
    public void selectItemToBuy() {
    	WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#productList > div:nth-child(2) > div > div > div.m-productCard__photo > a")));
    	item.click();
    }
    
    
    public void selectSize() {
    	// we have to select a size to proceed to basket so here we are selecting m size
    	WebElement sizem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#sizes > div > span:nth-child(2)")));
    	sizem.click();
    }
    public void addToBasket() {
    	// sepete ekle button clicked here but we are calling selectSize method first
    	selectSize();
    	WebElement addbasket = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#addBasket")));
    	addbasket.click();
    }
    
    
    public void writeDataToTxt(String[] data) {
    	// this script writes the data taken from item page so it accepts a string list as argument
        String price = data[0];
        String iteminfo = data[1];
        try {
            FileWriter writer = new FileWriter("C:\\Users\\Sarp Akgündüz\\eclipse-workspace\\SeleniumTest\\TextFolder\\item_info.txt");
            System.out.println("Attempting to write to file...");
            writer.write(iteminfo + "\n");
            writer.write(price + "\n");
            writer.close();
            System.out.println("Data written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String[] getDataItemScreen() {
    	// after we reach to a specific item's screen we are getting data because we will check the price on the basket after some steps
        String iteminfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("o-productDetail__description"))).getText();
        String price = driver.findElement(By.cssSelector("#priceNew")).getText();
        // this is edited because some prices does not have ,00 on the end
        if (!price.contains(",")) {
            price = price.replace(" TL", ",00 TL");
        }
        
        return new String[]{price, iteminfo};
    }
        
    public void goToBasket() {
    	WebElement basketicon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > header > div > div > div.col.col-xl-3.d-flex.justify-content-end > div > a.o-header__userInfo--item.bwi-cart-o.-cart")));
    	basketicon.click();
    }
    
    public String getPriceOnBasket() {
    	return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#app > div.container > div > div.col-12.col-md-12.col-lg-8 > div.m-basket__body > div.m-basket__item > div.m-basket__content > div > div > div.m-basket__productTools > div > div.m-basket__productPrice > div > div > span"))).getText();
    }
    public void emptyBasket() {
    	WebElement empty = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#removeCartItemBtn0-key-0")));
    	empty.click();
    }
    public void selectDropdownOption(int quantity) {
    	// accepts an integer argument as quantity
    	// dropdown menu selector
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#quantitySelect0-key-0")));
        // opens dropdown menu 
        Select quant = new Select(dropdown);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        quant.selectByValue(String.valueOf(quantity));
    }

    public String selectExcelString(String dress) {
    	// this is a very bad script since it can use only two cells of the excel file, since my task is only uses 2 cells I did not tried to use this excel file more modular
        String excelFilePath = "C:\\Users\\Sarp Akgündüz\\eclipse-workspace\\SeleniumTest\\ExcelFolder\\words.xlsx";
        int columnIndex;
        int rowIndex = 0;

        if (dress.equals("ş")) {
            columnIndex = 0;
        } else if(dress.equals("g")){
            columnIndex = 1;
        }
        else {
        	columnIndex = 0;
        }
        String searchText = null;

        try (InputStream inp = new FileInputStream(excelFilePath)) {
            Workbook workbook = new XSSFWorkbook(inp);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowIndex);
            Cell cell = row.getCell(columnIndex);

            // take the data on the excel cell
            searchText = cell.getStringCellValue();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchText;
    }
}
