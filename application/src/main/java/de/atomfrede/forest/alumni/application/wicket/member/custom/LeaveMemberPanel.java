package de.atomfrede.forest.alumni.application.wicket.member.custom;

import static de.atomfrede.forest.alumni.application.wicket.MessageUtils._;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.atomfrede.forest.alumni.service.member.MemberService;

public class LeaveMemberPanel extends Panel {

	@SpringBean
	private MemberService memberService;

	private DateTextField leaveDate;
	private Date _leaveDate;
	private Label infoLabel;

	@SuppressWarnings("serial")
	public LeaveMemberPanel(String id, Long memberId) {
		super(id);

		String message = "";
		if(memberId != null) {
			_leaveDate = memberService.findById(memberId).getLeaveDate();
			message = _("modal.leave.info", memberService.findById(memberId).getFullname()).getString();
			
		} else {
			_leaveDate = null;
		}
		
		
		infoLabel = new Label("modal.leave.info", message);
		infoLabel.setOutputMarkupId(true);
		infoLabel.setEscapeModelStrings(false);
		DateTextFieldConfig conf = new DateTextFieldConfig();
		conf.autoClose(true);
		leaveDate = new DateTextField("leavedate", new PropertyModel<Date>(
				this, "_leaveDate"), conf);
		
		leaveDate.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Just for updating the model object
				
			}
		});
		
		add(infoLabel);
		add(leaveDate);
	}
	
	public Date getEnteredDate() {
		return _leaveDate;
	}
}
