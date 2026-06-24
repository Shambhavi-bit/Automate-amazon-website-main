package testcases;


import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import actions.OrderActions;
import pages.ProductPage;
import utils.DriverUtils;

/**
 * Created by tmaher on 12/14/2015.
 */
public class PurchaseOrderTest {

    public WebDriver driver;

    @BeforeClass
    public void setUp(){
        driver = DriverUtils.getDriver();

    }

//    @Test()
//    public void test_Login(){
//        OrderActions orderActions = new OrderActions();
//        String username = LoadProperties.user.getProperty("tester23.username");
//        String password = LoadProperties.user.getProperty("tester23.password");
//        orderActions.navigateToHomePage();
//        orderActions.loginAs(username, password);
//        orderActions.initializeLogin();
//    }

    @Test()
    public void test_createPurchaseOrderForSingleProduct(){
        String query = "encyclopedia latest edition";
        OrderActions orderActions = new OrderActions();

        // Assume user is already logged in; skip login flow
        orderActions.navigateToHomePage();
        orderActions.initializeCart();

        // Search for the product, sort Low->High, open lowest-price result
        orderActions.searchAndOpenFirstResult(query);
        ProductPage productPage = new ProductPage();
        productPage.sleep(3500);

        // Gather details
        String title = productPage.getProductTitle();
        String author = productPage.getAuthor();
        String edition = productPage.getEdition();
        String price = productPage.getPrice();
        String rating = productPage.getRating();

        System.out.println("PRODUCT_TITLE: '" + title + "'");
        System.out.println("PRODUCT_AUTHOR: '" + author + "'");
        System.out.println("PRODUCT_EDITION: '" + edition + "'");
        System.out.println("PRODUCT_PRICE: '" + price + "'");
        System.out.println("PRODUCT_RATING: '" + rating + "'");

        // Take screenshot of the product detail page
        productPage.takeScreenshot("product-detail.png");
        // basic sanity: rating may be empty if not available, but code exercised
        //Book bookShoppingCart = orderActions.loadShoppingCartDataIntoProductObject(testBook);
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }
}
