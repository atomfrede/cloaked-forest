package de.atomfrede.forest.alumni.application.wicket.member;

import de.atomfrede.forest.alumni.application.wicket.member.detail.MemberDetailPage.Type;

public interface MemberDetailPageListener {

	void editTypeChanged(Type newEditType);
}
