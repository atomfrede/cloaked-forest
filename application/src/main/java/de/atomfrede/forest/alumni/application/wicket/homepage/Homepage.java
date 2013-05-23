package de.atomfrede.forest.alumni.application.wicket.homepage;

import org.apache.wicket.protocol.https.RequireHttps;
import org.wicketstuff.annotation.mount.MountPath;

import de.atomfrede.forest.alumni.application.wicket.base.BasePage;
import de.atomfrede.forest.alumni.application.wicket.member.MemberListPanel;

@SuppressWarnings("serial")
@MountPath(value = "/", alt = "/home")
public class Homepage extends BasePage<Void> {

	public Homepage() {
		super();
		add(new MemberListPanel("members"));
	}
}
