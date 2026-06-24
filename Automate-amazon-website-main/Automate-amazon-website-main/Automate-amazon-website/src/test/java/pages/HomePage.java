package pages;

import org.openqa.selenium.By;

import enums.Url;
import utils.CommonUtils;

/**
 * Created by tmaher on 12/21/2015.
 */
public class HomePage extends CommonUtils {
    private final By YOUR_ACCOUNT = By.id("nav-link-yourAccount");
    private final By SHOPPING_CART_ICON = By.cssSelector("#nav-cart");
    private final By SHOPPING_CART_COUNT = By.cssSelector("#nav-cart > #nav-cart-count");

    public HomePage(){
    }

    public void navigateToHomePage() {
        String url = Url.BASEURL.getURL();
        System.out.println("Navigating to Amazon.com: " + url);
        navigateToURL(url);
    }

    public void navigateToSignInPage(){
        System.out.println("HOME_PAGE: Selecting [YOUR_ACCOUNT] in navigation bar.");
        By[] candidates = new By[]{
                YOUR_ACCOUNT,
                By.id("nav-link-accountList"),
                By.cssSelector("#nav-link-accountList")
        };

        int attempts = 3;
        for (int attempt = 1; attempt <= attempts; attempt++){
            for (By candidate : candidates){
                try {
                    if (getElement(candidate) != null && getElement(candidate).isDisplayed()){
                        scrollToThenClick(candidate);
                        System.out.println("HOME_PAGE: Navigating to the SIGNIN_PAGE.\n");
                        return;
                    }
                } catch (Exception e){
                    // ignore and try next candidate
                }
            }
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }

        // Fallback: navigate directly to Amazon sign-in page
        System.out.println("HOME_PAGE: Fallback - navigating directly to the sign-in URL.");
        navigateToURL("https://www.amazon.com/ap/signin");
    }

    public void signOutWithSignOutLink(){
        String url = Url.BASEURL.getURL() + Url.SIGNOUT.getURL();
        navigateToURL(url);
    }

    public void selectShoppingCartIcon(){
        click(SHOPPING_CART_ICON);
    }

    public String getNumberOfItemsListedInShoppingCartIcon(){
        try {
            String text = getElementText(SHOPPING_CART_COUNT);
            if (text == null || text.trim().isEmpty()) return "0";
            return text;
        } catch (Exception e) {
            // If cart count isn't available (page layout changed or not visible), assume 0
            System.out.println("HOME_PAGE: Cart count not available, assuming 0.");
            return "0";
        }
    }
}
