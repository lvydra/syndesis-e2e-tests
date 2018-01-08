package io.syndesis.qe.pages.integrations.edit;

import static com.codeborne.selenide.Condition.visible;

import java.lang.reflect.Field;

import org.openqa.selenium.By;

/**
 * Created by sveres on 12/19/17.
 */
public class ActionConfigureComponentJmsSubscribe extends ActionConfigureComponentJms {


	private static final class Element {
		public static final By TITLE = By.cssSelector("div[innertext='Subscribe for Messages']");
	}

	private static final class Input {
		public static final By DESTINATION_NAME = By.id("destinationName");
		public static final By DURABLE = By.id("durable");
		public static final By SUBCRIPTION_ID = By.id("destinationSubscriptionId");
		public static final By MESSAGE_SELECTOR = By.id("messageSelector");
	}
	
	private static final class Select {
		public static final By DESTINATION_TYPE = By.id("destinationType");
	}

	@Override
	public boolean validate() {
		return this.getRootElement().find(Element.TITLE).is(visible);
	}

	@Override
	public Field[] getInputFields() {
		Class<Input> c = ActionConfigureComponentJmsSubscribe.Input.class;
		return c.getDeclaredFields();
	}

	@Override
	public Field[] getSelectFields() {
		Class<Select> c = ActionConfigureComponentJmsSubscribe.Select.class;
		return c.getDeclaredFields();
	}
}
