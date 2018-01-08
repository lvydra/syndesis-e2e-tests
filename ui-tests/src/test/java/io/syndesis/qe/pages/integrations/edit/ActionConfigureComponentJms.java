package io.syndesis.qe.pages.integrations.edit;

import static com.codeborne.selenide.Condition.visible;

import java.lang.reflect.Field;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ActionConfigureComponentJms extends ActionConfigureComponent {

	abstract Class getInputClass();

	abstract Class getSelectClass();

	public SelenideElement checkAndGetFieldType(String elementId) throws IllegalArgumentException, IllegalAccessException {
		log.info("field: {} is being checked", elementId);
		By elem = By.id(elementId);
		
		Class inputFields = getInputClass();
		Class selectFields = getSelectClass();

		SelenideElement element = this.getRootElement().find(elem).shouldBe(visible);
		if (element.getTagName().equals("input") && isContainedInLocators(elem, inputFields) || element.getTagName().equals("select") && isContainedInLocators(elem, selectFields)) {
			return element;
		} else {
			throw new RuntimeException("This field id does not belong to this page!");
		}
	}

	public boolean isContainedInLocators(By by, Class c) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = c.getDeclaredFields();

		for (Field field : fields) {
			if (by.equals(field.get(null))) {
				log.info("CONTAINED " + field.getName() + " - " + field.getType());
				return true;
			}
		}
		return false;
	}
}
