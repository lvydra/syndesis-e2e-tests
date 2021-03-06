package io.syndesis.qe.pages.integrations.edit;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

import io.syndesis.qe.pages.SyndesisPageObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListActionsComponent extends SyndesisPageObject {

    private static final class Element {

        public static final By ROOT = By.cssSelector("syndesis-list-actions");
        public static final By NAME = By.className("name");
    }

    @Override
    public SelenideElement getRootElement() {
        SelenideElement elementRoot = $(Element.ROOT).shouldBe(visible);
        return elementRoot;
    }

    @Override
    public boolean validate() {
        return getRootElement().is(visible);
    }

    public void selectAction(String name) {
        log.info("Searching for integration action {}", name);
        getElementContainingText(Element.NAME, name).shouldBe(visible).click();
    }
}
