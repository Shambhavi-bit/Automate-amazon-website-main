package pages;

import org.openqa.selenium.By;
import org.testng.TestException;

import enums.Products;
import enums.Url;
import utils.CommonUtils;

/**
 * Created by tmaher on 12/21/2015.
 */
public class ProductPage extends CommonUtils {

    private final By PRODUCT_TITLE = By.cssSelector("#productTitle");
    private final By AUTHOR = By.cssSelector(".a-link-normal.contributorNameID");
    private final By EDITION = By.cssSelector(".a-size-medium.a-color-secondary.a-text-normal");
    private final By PRICE = By.cssSelector(".a-size-medium.a-color-price.offer-price.a-text-normal");
    private final By ADD_TO_CART = By.cssSelector("#add-to-cart-button");
    private final By RATING = By.cssSelector("span.a-icon-alt");

    public void navigateToProductPage(Products product){
        String url = Url.BASEURL.getURL() + Url.PRODUCT_SECTION.getURL() + "/" + product.getProductId();
        navigateToURL(url);
        System.out.println("PRODUCT_PAGE: Navigated to " + url);
    }

    public void verifyProductTitle(String expectedTitle){
        String actualTitle = getProductTitle();
        System.out.println("PRODUCT_PAGE: Verifying that the product title is '" + actualTitle + "'");
        if (!expectedTitle.equals(actualTitle)){
            throw new TestException("ERROR: PRODUCT_PAGE: Product Title is ['" + actualTitle + "']. Expected ['" + expectedTitle + "'].");
        }
    }

    public String getProductTitle(){
        return getElementText(PRODUCT_TITLE);
    }

    public String getAuthor(){
        try {
            String a = getElementText(AUTHOR);
            if (a != null && !a.trim().isEmpty()) return a;
        } catch (Exception ignored) {}

        // Try common alternative selectors
        By[] alternates = new By[]{
                By.cssSelector("#bylineInfo"),
                By.cssSelector(".author a"),
                By.cssSelector(".contributorName")
        };
        for (By alt : alternates){
            try {
                String a = getElementText(alt);
                if (a != null && !a.trim().isEmpty()) return a;
            } catch (Exception ignored) {}
        }

        return "";
    }

    public String getEdition(){
        try {
            String e = getElementText(EDITION);
            if (e != null && !e.trim().isEmpty()) return e;
        } catch (Exception ignored) {}

        By[] alternates = new By[]{
                By.cssSelector("#productDetailsTable .a-size-medium"),
                By.cssSelector(".edition"),
                By.cssSelector(".a-size-base.a-color-secondary")
        };

        for (By alt : alternates){
            try {
                String e = getElementText(alt);
                if (e != null && !e.trim().isEmpty()) return e;
            } catch (Exception ignored) {}
        }

        return "";
    }

    public String getPrice(){
        try {
            String p = getElementText(PRICE);
            if (p != null && !p.trim().isEmpty()) return p;
        } catch (Exception ignored) {}

        By[] alternates = new By[]{
                By.id("priceblock_ourprice"),
                By.id("priceblock_dealprice"),
                By.cssSelector(".a-color-price"),
                By.cssSelector("#price_inside_buybox")
        };
        for (By alt : alternates){
            try {
                String p = getElementText(alt);
                if (p != null && !p.trim().isEmpty()) return p;
            } catch (Exception ignored) {}
        }

        return "";
    }

    public void clickAddToCart(){
        System.out.println("PRODUCT_PAGE: Clicking on [ADD_TO_CART] button. \n");
        click(ADD_TO_CART);
    }

    public String getRating(){
        try {
            String r = getElementText(RATING);
            if (r != null && !r.trim().isEmpty()) return r;
        } catch (Exception ignored) {}

        By[] alternates = new By[]{
                By.id("acrPopover"),
                By.cssSelector("i.a-icon-star span"),
                By.cssSelector(".averageStarRating")
        };

        for (By alt : alternates){
            try {
                String r = getElementText(alt);
                if (r != null && !r.trim().isEmpty()) return r;
            } catch (Exception ignored) {}
        }

        return "";
    }
}
