package io.syndesis.qe.pages.integrations.edit.steps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

import io.syndesis.qe.pages.SyndesisPageObject;

public abstract class StepComponent extends SyndesisPageObject {

    private static final class Element {
        public static final By ROOT = By.cssSelector("syndesis-integrations-step-configure");
    }

    @Override
    public SelenideElement getRootElement() {
        SelenideElement elementRoot = $(Element.ROOT).shouldBe(visible);
        return elementRoot;
    }

    public abstract void fillConfiguration();

    public abstract boolean validate();

    public abstract void initialize();

    public abstract void setParameter(String parameter);

    public abstract String getParameter();
}
