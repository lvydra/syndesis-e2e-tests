package io.syndesis.qe.pages.integrations.edit;

import java.lang.reflect.Field;

import org.openqa.selenium.By;

import io.syndesis.qe.pages.MultipleInputs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ActionConfigureComponentJms extends ActionConfigureComponent implements MultipleInputs {

	@Override
	public String checkAndGetFieldType(String element) throws IllegalArgumentException, IllegalAccessException {
		log.info("field: {} is being checked", element);
		By elem = By.id(element);
		
		Field[] inputFields = getInputFields();
		Field[] selectFields = getSelectFields();

		String tagName = this.getRootElement().$(elem).getTagName();
		if ("input".equals(tagName) && isContainedInLocators(elem, inputFields) || "select".equals(tagName) && isContainedInLocators(elem, selectFields)) {
			return tagName;
		} else {
			throw new RuntimeException("This field id does not belong to this page!");
		}
	}
	
	abstract Field[] getInputFields();
	
	abstract Field[] getSelectFields();
	
	public boolean isContainedInLocators(By by, Field[] fields) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : fields) {
			if (by.equals(field.get(null))) {
				log.info("CONTAINED " + field.getName() + " - " + field.getType());
				return true;
			}
		}
		return false;
	}
}
