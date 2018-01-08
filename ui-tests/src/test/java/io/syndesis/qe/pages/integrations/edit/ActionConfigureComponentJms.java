package io.syndesis.qe.pages.integrations.edit;

import static com.codeborne.selenide.Condition.visible;

import java.lang.reflect.Field;

import org.openqa.selenium.By;

import io.syndesis.qe.pages.MultipleInputs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ActionConfigureComponentJms extends ActionConfigureComponent implements MultipleInputs {

	abstract Class getInputClass();

	abstract Class getSelectClass();

	@Override
	public String checkAndGetFieldType(String element) throws IllegalArgumentException, IllegalAccessException {
		log.info("field: {} is being checked", element);
		By elem = By.id(element);
		
		Class inputFields = getInputClass();
		Class selectFields = getSelectClass();

		String tagName = this.getRootElement().find(elem).shouldBe(visible).getTagName();
		if ("input".equals(tagName) && isContainedInLocators(elem, inputFields) || "select".equals(tagName) && isContainedInLocators(elem, selectFields)) {
			return tagName;
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
