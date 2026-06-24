package pages;

import org.openqa.selenium.By;

import utils.CommonUtils;

/**
 * Created by tmaher on 12/21/2015.
 */
public class SignInPage extends CommonUtils {

    private final By USERNAME = By.cssSelector("#ap_email");
    private final By PASSWORD = By.cssSelector("#ap_password");
    private final By SIGNIN_BUTTON = By.cssSelector("#signInSubmit");

    public void enterUsername(String userName){
        System.out.println("SIGNIN_PAGE: Entering username: " + userName);
        By[] candidates = new By[]{
                By.cssSelector("#ap_email"),
                By.name("email"),
                By.cssSelector("input[type='email']")
        };

        int attempts = 3;
        for (int attempt = 1; attempt <= attempts; attempt++){
            for (By candidate : candidates){
                try {
                    waitForElementToBeVisible(candidate);
                    sendKeys(candidate, userName);
                    // After entering username, click Continue if present to proceed to password step
                    By[] continueCandidates = new By[]{By.id("continue"), By.cssSelector("#continue")};
                    for (By cont : continueCandidates){
                        try { click(cont); break; } catch (Exception ignored) {}
                    }
                    return;
                } catch (Exception e){
                    // try next candidate
                }
            }
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }

        throw new RuntimeException("SIGNIN_PAGE: Could not find username input on sign-in page.");
    }

    public void enterPassword(String password){
        System.out.println("SIGNIN_PAGE: Entering password.");
        By[] candidates = new By[]{
                By.cssSelector("#ap_password"),
                By.name("password"),
                By.cssSelector("input[type='password']")
        };

        int attempts = 3;
        for (int attempt = 1; attempt <= attempts; attempt++){
            for (By candidate : candidates){
                try {
                    waitForElementToBeVisible(candidate);
                    sendKeys(candidate, password);
                    return;
                } catch (Exception e){
                    // try next candidate
                }
            }
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }

        throw new RuntimeException("SIGNIN_PAGE: Could not find password input on sign-in page.");
    }

    public void clickSignInButton(){
        System.out.println("SIGNIN_PAGE: Clicking the [SIGN_IN] button.\n");
        By[] candidates = new By[]{
                By.cssSelector("#signInSubmit"),
                By.cssSelector("input[type='submit']"),
                By.xpath("//input[@id='signInSubmit']")
        };
        for (By candidate : candidates){
            try {
                click(candidate);
                return;
            } catch (Exception e){
                // try next
            }
        }
        throw new RuntimeException("SIGNIN_PAGE: Could not find sign-in button on sign-in page.");
    }
}
