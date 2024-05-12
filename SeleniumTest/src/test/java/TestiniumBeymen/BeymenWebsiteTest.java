package TestiniumBeymen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BeymenWebsiteTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "path_to_chrome_driver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        ProjectObjects objects = new ProjectObjects(driver);

        driver.get("https://www.beymen.com/tr");

        String title = driver.getTitle();
        // Title is checked so we know page is open
        assertEquals("Beymen.com – Türkiye’nin Tek Dijital Lüks Platformu", title);
        // also to be sure homepage logo is checked too
        assertEquals(true, driver.findElement(By.cssSelector("a.o-header__logo")).isDisplayed());

        // two pop-up appears after page is open since we want to reach to search we are closing them
        objects.acceptCookies();
        objects.selectGender();
        objects.denyPopup();
        // I created the method search which accepts one string argument to search on the searchbar on our task that string should be şort
        objects.focusSearchBar();
        
        objects.search(objects.selectExcelString("ş"));
        
        objects.cleanSearchBar();

        objects.search(objects.selectExcelString("g"));
        
        objects.enterSearch();
        
        objects.selectItemToBuy();
        
        String priceVal = objects.getDataItemScreen()[0];
        
        objects.writeDataToTxt(objects.getDataItemScreen());
        
        objects.addToBasket();
        
        objects.goToBasket();
        
        assertEquals(priceVal, objects.getPriceOnBasket());
        
        objects.selectDropdownOption(2);  
        
        objects.emptyBasket();
        
        assertEquals(true,wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#emtyCart"))).isDisplayed());
        
        driver.quit();
    }
}
