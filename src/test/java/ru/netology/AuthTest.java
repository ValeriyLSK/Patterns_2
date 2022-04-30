package ru.netology;

import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.generateLogin;
import static ru.netology.DataGenerator.generatePassword;

public class AuthTest {


    @BeforeEach
    public void shouldOpenForm() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessRegistration(){
        val registeredUser = getRegisteredUser("active");
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldCheckNotRegistered() {
        val notRegisteredUser = getUser("active");
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(notRegisteredUser.getLogin());
        form.$("[data-test-id=\"password\"] input").setValue(notRegisteredUser.getPassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldCheckBlocked() {
        val blockedUser = getRegisteredUser("blocked");
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(blockedUser.getLogin());
        form.$("[data-test-id=\"password\"] input").setValue(blockedUser.getPassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldCheckWrongLogin() {
        val registeredUser = getRegisteredUser("active");
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(generateLogin());
        form.$("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldCheckWrongPassword() {
        val registeredUser = getRegisteredUser("active");
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=\"password\"] input").setValue(generatePassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}
