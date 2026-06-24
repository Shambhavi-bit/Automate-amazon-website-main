package actions;

import org.openqa.selenium.By;

import enums.Products;
import pages.HomePage;
import pages.ProductPage;
import pages.ShoppingCartPage;
import pages.ShoppingCartReviewPage;
import pages.SignInPage;
import pojo.Book;

/**
 * Created by tmaher on 12/21/2015.
 */
public class OrderActions {

    public void navigateToHomePage(){
        HomePage homePage = new HomePage();
        homePage.navigateToHomePage();
    }

    public void loginAs(String username, String password){
        HomePage homePage = new HomePage();
        SignInPage signIn = new SignInPage();
        homePage.navigateToSignInPage();
        signIn.enterUsername(username);
        signIn.enterPassword(password);
        signIn.clickSignInButton();
    }

    public Book loadProductPageDataIntoProductObject(Products product){
        System.out.println("Starting process to load info for " + product + ":");
        Book book = new Book();
        ProductPage productPage = new ProductPage();
        productPage.navigateToProductPage(product);
        productPage.verifyProductTitle(product.getProductTitle());
        book.loadInfoFromProductPage();
        System.out.println(book + "\n");
        return book;
    }

    public void searchAndOpenFirstResult(String query){
        HomePage homePage = new HomePage();
        System.out.println("SEARCH: Searching for '" + query + "' on home page.");
        // Search box and submit button commonly used on Amazon - try multiple candidates
        By[] searchBoxCandidates = new By[]{
                By.id("twotabsearchtextbox"),
                By.name("field-keywords"),
                By.cssSelector("input#twotabsearchtextbox"),
                By.cssSelector("input[name='field-keywords']")
        };
        By[] searchButtonCandidates = new By[]{
                By.id("nav-search-submit-button"),
                By.cssSelector("input.nav-input[type='submit']"),
                By.cssSelector("input[type='submit'][value='Go']")
        };

        // Navigate directly to search results page to avoid varying search widget behavior
        try {
            String encoded = java.net.URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8.toString());
            // Append sort parameter to order by Price: Low to High
            String searchUrl = "https://www.amazon.com/s?k=" + encoded + "&s=price-asc-rank";
            homePage.navigateToURL(searchUrl);
            System.out.println("SEARCH: Navigated to search results: " + searchUrl);
        } catch (Exception e){
            throw new RuntimeException("SEARCH: Could not navigate to search results for: " + query);
        }

        // Wait for search results to appear
        try {
            homePage.waitUntilElementIsDisplayedOnScreen(By.cssSelector("div.s-main-slot"));
        } catch (Exception ignored) {}

        // Click first available search result link using multiple candidate selectors
        By[] resultCandidates = new By[]{
                By.cssSelector("div[data-component-type='s-search-result'] h2 a"),
                By.cssSelector("div.s-main-slot .s-result-item h2 a"),
                By.cssSelector("div[data-asin] h2 a"),
                By.cssSelector("h2 a.a-link-normal")
        };

        // Try to extract first product ASIN from results and navigate directly to product page
        try {
            java.util.List<org.openqa.selenium.WebElement> asins = homePage.getElements(By.cssSelector("div[data-asin]"));
            for (org.openqa.selenium.WebElement e : asins){
                try {
                    String asin = e.getAttribute("data-asin");
                    if (asin != null && !asin.trim().isEmpty()){
                        String productUrl = "https://www.amazon.com/dp/" + asin;
                        homePage.navigateToURL(productUrl);
                        System.out.println("SEARCH: Navigated directly to product: " + productUrl);
                        return;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}

        throw new RuntimeException("SEARCH: Could not open first search result for: " + query);
    }

    public void addProductToShoppingCartReview(Products product){
        ShoppingCartReviewPage shoppingCartReviewPage = new ShoppingCartReviewPage();
        System.out.println("Adding " + product + " to cart:");
        ProductPage productPage = new ProductPage();
        productPage.navigateToProductPage(product);
        productPage.verifyProductTitle(product.getProductTitle());
        productPage.clickAddToCart();
        shoppingCartReviewPage.verifyOnShoppingCartReviewPage();
    }

    public Book loadShoppingCartDataIntoProductObject(Products product){
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage();
        Book book = new Book();
        shoppingCartPage.verifyOnShoppingCartPage();

        return book;
    }

    public void initializeLogin(){
        System.out.println("INITIALIZING: Signing out, if needed.\n");
        signOut();
    }

    public void initializeCart(){
        System.out.println("INITIALIZING: Deleting all Items in Cart.\n");
        deleteAllItemsIfAnyFromCart();
    }

    public void signOut(){
        HomePage homePage = new HomePage();
        homePage.signOutWithSignOutLink();
    }

    public void deleteAllItemsIfAnyFromCart(){
        HomePage homePage = new HomePage();
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage();
        String itemsInCart = homePage.getNumberOfItemsListedInShoppingCartIcon();
        if (!itemsInCart.equals("0")){
            homePage.selectShoppingCartIcon();
            shoppingCartPage.deleteAllItemsInCart();
        } else {
            System.out.println("\t* There are already '0' items in the Shopping Cart.");
        }
    }

    /**
     * Compares actual and expected values, and sets up the results in one line item.
     * @param fieldName: What you are comparing, such as "unitPrice".
     * @param actualValue: The value you wish to compare.
     * @param expectedValue: The value you hope the actual value will be.
     * @return
     */
    private String outputPassOrFailOnFieldComparison(String fieldName, String actualValue, String expectedValue){
        if (actualValue.equals(expectedValue)){
            return "\t* " + fieldName + ": '" + actualValue + "' (PASS)";
        } else {
            return "\t* " + fieldName + ": '" + actualValue + "' : Should be: '" + expectedValue + "' (FAIL)";
        }
    }

    public boolean checkMatchingValues(String testHeading, Object actualValue, Object expectedValue) {
        String successMessage = "\t* The Expected and Actual Values match. (PASS)\n";
        String failureMessage = "\t* The Expected and Actual Values do not match! (FAIL)\n";

        boolean doesPriceMatch = false;

        System.out.println(testHeading);
        System.out.println("\t* Expected Value: " + expectedValue);
        System.out.println("\t* Actual Value: " + actualValue);

        if (actualValue.equals(expectedValue)) {
            System.out.println(successMessage);
            doesPriceMatch = true;
        } else {
            System.out.println(failureMessage);
            doesPriceMatch = false;
        }
        return doesPriceMatch;
    }
}
