package io.syndesis.qe.pages;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.is;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SyndesisPageObject {

    public abstract SelenideElement getRootElement();

    public abstract boolean validate();

    public SelenideElement getButton(String buttonTitle) {
        log.info("searching for button {}", buttonTitle);
        return getRootElement().findAll(By.tagName("button")).filter(Condition.matchText("(\\s*)" + buttonTitle + "(\\s*)")).first();
    }

    public SelenideElement getFirstVisibleButton(String buttonTitle) {
        log.info("searching for first visible button {}", buttonTitle);
        ElementsCollection buttonElements = this.getRootElement().findAll(By.linkText(buttonTitle)).filter(visible);
        return buttonElements.get(0);
    }

    /**
     * Fill form with given data. It will look for ui element for every map entry.
     *
     * @param data key,value data. Key is used for element lookup
     * @param parrentElement search inputs in child elements of this one
     */
    public void fillForm(Map<By, String> data, SelenideElement parrentElement) {
        String value;
        for (By locator : data.keySet()) {
            value = data.get(locator);
            SelenideElement inputElement = parrentElement.find(locator).shouldBe(visible);
            inputElement.sendKeys(value);
        }
    }

    public void selectFromDropDown(String selectId, String option) {
        SelenideElement select = this.getElementById(selectId).shouldBe(visible);
        assertThat(select.getTagName(), is("select"));
        select.selectOption(option);
    }

    public String getCurrentUrl() {
        return getWebDriver().getCurrentUrl();
    }

    public void goToUrl(String url) {
        getWebDriver().get(url);
    }

    public SelenideElement getLink(String linkTitle) {
        return this.getRootElement().find(By.linkText(linkTitle));
    }

    public ElementsCollection getLinks(String linkTitle) {
        return this.getRootElement().findAll(By.linkText(linkTitle));
    }

    public SelenideElement getLinkRandom(String linkTitle) {
        ElementsCollection links = this.getLinks(linkTitle);
        int index = (int) Math.floor(Math.random() * links.size());
        return links.get(index);
    }

    public SelenideElement getElementByCssSelector(String cssSelector) {
        return this.getRootElement().find(By.cssSelector(cssSelector));
    }

    public SelenideElement getElementByXpath(String xpathSelector) {
        return this.getRootElement().find(By.xpath(xpathSelector));
    }

    public SelenideElement getElementByClassName(String elementClassName) {
        return this.getRootElement().find(By.className(elementClassName));
    }

    public SelenideElement getElementByLocator(By elementLocator) {
        log.info("searching for element {}", elementLocator.toString());
        return this.getRootElement().$(elementLocator);
    }

    public SelenideElement getTitleByText(String text){
        log.info("searching for title {}", text);
        return this.getRootElement().find(By.cssSelector(String.format("h2:contains('%s')", text)));
    }

    public ElementsCollection getElementsByClassName(String elementClassName) {
        return this.getRootElement().findAll(By.className(elementClassName));
    }

    public SelenideElement getElementRandom(String elementClassName) {
        ElementsCollection elements = this.getElementsByClassName(elementClassName);
        int index = (int) Math.floor(Math.random() * elements.size());
        return elements.get(index);
    }

    public SelenideElement getElementRandom(By locator) {
        ElementsCollection elements = this.getRootElement().findAll(locator);
        int index = (int) Math.floor(Math.random() * elements.size());
        return elements.get(index);
    }

    public void selectOption(SelenideElement selectElement, String option) {
        SelenideElement optionElement = getElementContainingText(By.tagName("option"), option);
        optionElement.shouldBe(visible).click();
    }

    private SelenideElement getElementById(String inputId) {
        return this.getRootElement().find(By.id(inputId));
    }

    public SelenideElement getInputBySelector(String selector) {
        return this.getRootElement().find(By.cssSelector(selector));
    }

    public void fillInput(String inputId, String value) {
        SelenideElement input = this.getElementById(inputId);
        assertThat(input.getTagName(), is("input"));
        if (input.getAttribute("type").equals("checkbox")) {
            input.setSelected(Boolean.valueOf(value));
        } else {
            input.shouldBe(visible).clear();
            input.shouldBe(visible).sendKeys(value);
        }
    }

    public void fillInput(SelenideElement input, String value) {
        if (input.getAttribute("type").equals("checkbox")) {
            input.setSelected(Boolean.valueOf(value));
        } else {
            input.shouldBe(visible).clear();
            input.shouldBe(visible).sendKeys(value);
        }
    }

    public void setElementValue(String value, SelenideElement element) {
        switch (element.getTagName()) {
            case "input":
                fillInput(element, value);
                break;
            case "select":
                element.selectOption(value);
                break;
        }
    }

    public SelenideElement getElementContainingText(By by, String text) {
        ElementsCollection elements = getRootElement().findAll(by).shouldBe(sizeGreaterThan(0));
        log.info("I found " + elements.size() + " elements.");

        elements = elements.filter(exactText(text));
        log.info("Elements after filter: " + elements.size());
        SelenideElement element = elements.shouldBe(sizeGreaterThan(0)).first();
        return element;
    }

    public String getElementText(By locator) {
        SelenideElement element = this.getRootElement().find(locator);
        return element.shouldBe(visible).getText();
    }

    public void checkButtonStatus(String buttonTitle, String status) {
        log.info("checking button {} status", buttonTitle);
        this.getButton(buttonTitle).shouldBe(this.conditionValueOf(status));
    }

    public Condition conditionValueOf(String status){
        Condition condition;
        switch(status){
            case "Active":
            case "Visible":
                condition = visible;
                break;
            case "Disabled":
            case "Inactive":
                condition = disabled;
                break;
            default:
                condition = visible;
                break;
        }
        return condition;
    }
}
