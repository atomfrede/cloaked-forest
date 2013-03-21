package de.atomfrede.forest.alumni.application.wicket;

import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

public final class MessageUtils {

	private MessageUtils() {
		super();
		// No public default Constructor
	}

	/**
	 * Shortcut to create message translation model
	 * 
	 * @param message
	 * @param args
	 * @return
	 */
	public static StringResourceModel _(String message, Object... args) {
		return new StringResourceModel(message, new Model<Object[]>(args),
				message);
	}
}
