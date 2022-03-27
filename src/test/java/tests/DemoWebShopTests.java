package tests;

import config.demowebshop.App;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DemoWebShopTests extends TestBase {

    public void cleanResults() {
        open("/wishlist");
        refresh();
        $("[name='removefromcart']").click();
        $("[name='updatecart']").click();

        $(".wishlist-qty").shouldHave(text("(0)"));
    }

    public Map<String, String> getAuthorizationCookie() {
        return step("Get cookie by api and set it to browser", () -> {
            Map<String, String> authorizationCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", App.config.userLogin())
                            .formParam("Password", App.config.userPassword())
                            .log().uri()
                            .log().body()
                            .when()
                            .post("/login")
                            .then()
                            .log().status()
                            .log().body()
                            .statusCode(302)
                            .extract()
                            .cookies();

            return authorizationCookie;
        });
    }

    @Test
    @DisplayName("Successful authorization and addToWishlistTestAndCleanResults (API + UI)")
    public void addToWishlistTestAndCleanResults() {

        Map<String, String> authorizationCookie = getAuthorizationCookie();

        String nopCustomer = "Nop.customer=" + authorizationCookie.get("Nop.customer") + ";";
        String nopCommerce = "NOPCOMMERCE.AUTH=" + authorizationCookie.get("NOPCOMMERCE.AUTH") + ";";
        String arrAffinity = "ARRAffinity=" + authorizationCookie.get("ARRAffinity") + ";";

        step("Open minimal content, because cookie can be set when site is opened", () -> {
            open("/Themes/DefaultClean/Content/images/logo.png");
        });

        step("Set cookie to to browser", () -> {
            getWebDriver().manage().addCookie(
                    new Cookie("NOPCOMMERCE.AUTH", authorizationCookie.get("NOPCOMMERCE.AUTH")));
        });

        step("Open main page", () -> {
            open("");
        });

        step("Verify successful authorization", () -> {
            $(".account").shouldHave(text(App.config.userLogin()));
        });

        int giftQuantity = 3;

        step("Add some gifts on wishlist", () -> {
            for (int i = 0; i < giftQuantity; i++) {
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie(nopCustomer)
                        .cookie(nopCommerce)
                        .cookie(arrAffinity)
                        .formParam("addtocart_43.EnteredQuantity", 1)
                        .log().uri()
                        .log().body()
                        .when()
                        .post("/addproducttocart/details/43/2")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                        .body("updatetopwishlistsectionhtml", is("(" + (i + 1) + ")"));

                open("");
            }
        });

        step("Check added gifts on wishlist", () -> {
            refresh();
            open("/wishlist");
            $(".wishlist-qty").shouldHave(text("(" + giftQuantity + ")"));
        });

        step("clear result test after test", () -> {
            cleanResults();
        });
    }

    @Test
    @Disabled
    public void cleanResultsTest() {

        Map<String, String> authorizationCookie = getAuthorizationCookie();

        String nopCustomer = "Nop.customer=" + authorizationCookie.get("Nop.customer") + ";";
        String nopCommerce = "NOPCOMMERCE.AUTH=" + authorizationCookie.get("NOPCOMMERCE.AUTH") + ";";
        String arrAffinity = "ARRAffinity=" + authorizationCookie.get("ARRAffinity") + ";";

        step("Open minimal content, because cookie can be set when site is opened", () -> {
            open("/Themes/DefaultClean/Content/images/logo.png");
        });

        step("Set cookie to to browser", () -> {
            getWebDriver().manage().addCookie(
                    new Cookie("NOPCOMMERCE.AUTH", authorizationCookie.get("NOPCOMMERCE.AUTH")));
        });

        step("Open main page", () -> {
            open("");
        });

        step("Verify successful authorization", () -> {
            $(".account").shouldHave(text(App.config.userLogin()));
        });

        step("clear result test after test", () -> {
            cleanResults();
        });
    }

}