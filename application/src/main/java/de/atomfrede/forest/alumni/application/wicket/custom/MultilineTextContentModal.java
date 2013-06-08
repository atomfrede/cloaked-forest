package de.atomfrede.forest.alumni.application.wicket.custom;

import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.IModel;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;

@SuppressWarnings("serial")
/**
 * A special text content for modal dialogs tha uses a multi line label and does not escape markup strings. So 
 * line breaks can be used inside a modal dialog generated dynamically.
 * @author fred
 *
 */
public class MultilineTextContentModal extends Modal {

	public MultilineTextContentModal(String markupId, IModel<String> model) {
		super(markupId);
		setEscapeModelStrings(false);
		MultiLineLabel label = new MultiLineLabel("content", model);
		label.setEscapeModelStrings(false);
		add(label);
	}

}
