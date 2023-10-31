package com.virtualkeyboard.client.models;


import javafx.scene.image.Image;

/**
 * 
 * @author Hassam
 * Model for loading all events from file
 */
public class EventProcess {

	private String parentId="";
	private String sequenceId;
	private String event;
	private String text;
	private Boolean active;

	private Boolean mandatory;

	private Boolean isEventsHead;

	private String iconName;
	private String email;

	private String targetText;
	private Double waitTime;


	private Image iconImage;

	private Image referenceImage;


	private String referenceAlign;



	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EventProcess(String event, String text, Boolean active, String iconName, String email,
						Double waitTime) {
		super();
		this.sequenceId = sequenceId;
		this.event = event;
		this.text = text;
		this.active = active;
		this.iconName = iconName;
		this.email = email;
		this.waitTime = waitTime;
		parentId="";
	}
	public EventProcess() {
		super();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Double getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(Double waitTime) {
		this.waitTime = waitTime;
	}
	public String getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Boolean getActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getTargetText() {
		return targetText;
	}

	public void setTargetText(String targetText) {
		this.targetText = targetText;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}


	public Image getIconImage() {
		return iconImage;
	}

	public void setIconImage(Image iconImage) {
		this.iconImage = iconImage;
	}

	public Image getReferenceImage() {
		return referenceImage;
	}

	public void setReferenceImage(Image referenceImage) {
		this.referenceImage = referenceImage;
	}

	public Boolean getEventsHead() {
		return isEventsHead;
	}

	public void setEventsHead(Boolean eventsHead) {
		isEventsHead = eventsHead;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getReferenceAlign() {
		return referenceAlign;
	}

	public void setReferenceAlign(String referenceAlign) {
		this.referenceAlign = referenceAlign;
	}
}
