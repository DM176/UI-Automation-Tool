package com.virtualkeyboard.client.models;

import com.virtualkeyboard.client.util.Constant;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;

import java.util.List;


public class EventTableModel {






    private  SimpleStringProperty sNo;

    private SimpleStringProperty subProcessName;




    private CheckBox selectedRow;



    private  SimpleStringProperty eventsHead;
    private  SimpleStringProperty event;


    private  Boolean active ;
    private  SimpleStringProperty searchText;


    private  SimpleStringProperty description;
    private ImageView iconImage;

    private ImageView referenceImage;



    private  SimpleStringProperty referenceAlign;

    private String iconImagePath;

    private SimpleStringProperty status;

    public String getMandatory() {
        return mandatory.get();
    }

    public SimpleStringProperty mandatoryProperty() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory.set(mandatory);
    }

    private SimpleStringProperty mandatory;



    public EventTableModel() {
        this.sNo = new SimpleStringProperty("");
        this.event = new SimpleStringProperty("");
        this.active = true;
        this.mandatory = new SimpleStringProperty(Constant.YES);
        this.searchText = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.insertText =new SimpleStringProperty("");
        this.waitTime = new SimpleStringProperty("2000");
        ImageView imageView=new ImageView();
        imageView.setFitWidth(90);
        imageView.setFitHeight(40);
        this.iconImage= imageView; //new ImageView();
        imageView=new ImageView();
        imageView.setFitWidth(90);
        imageView.setFitHeight(40);
        referenceImage=imageView;
        this.status=new SimpleStringProperty("");
        this.eventsHead=new SimpleStringProperty("");
        this.selectedRow=new CheckBox();
        selectedRow.setSelected(false);
        subProcessName=new SimpleStringProperty("");
        referenceAlign=new SimpleStringProperty("");
    }

    private  SimpleStringProperty insertText;
    private  SimpleStringProperty waitTime;


    public String getsNo() {
        return sNo.get();
    }

    public SimpleStringProperty sNoProperty() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo.set(sNo);
    }

    public String getEvent() {
        return event.get();
    }

    public SimpleStringProperty eventProperty() {
        return event;
    }

    public void setEvent(String event) {
        this.event.set(event);
    }

    public Boolean getActive() {
        return active;
    }

    public ImageView getReferenceImage() {
        return referenceImage;
    }

    public void setReferenceImage(ImageView referenceImage) {
        this.referenceImage = referenceImage;
    }


    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSearchText() {
        return searchText.get();
    }

    public SimpleStringProperty searchTextProperty() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText.set(searchText);
    }

    public String getInsertText() {
        return insertText.get();
    }

    public SimpleStringProperty insertTextProperty() {
        return insertText;
    }

    public void setInsertText(String insertText) {
        this.insertText.set(insertText);
    }

    public String getWaitTime() {
        return waitTime.get();
    }



    public SimpleStringProperty waitTimeProperty() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime.set(waitTime);
    }

    public ImageView getIconImage() {
        return iconImage;
    }

    public void setIconImage(ImageView iconImage) {
        this.iconImage = iconImage;
    }

    public String getIconImagePath() {
        return iconImagePath;
    }

    public void setIconImagePath(String iconImagePath) {
        this.iconImagePath = iconImagePath;
    }


    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getEventsHead() {
        return eventsHead.get();
    }

    public SimpleStringProperty eventsHeadProperty() {
        return eventsHead;
    }

    public void setEventsHead(String eventsHead) {
        this.eventsHead.set(eventsHead);
    }



    public String getDescription() {
        return description.get();
    }

    public CheckBox getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(CheckBox selectedRow) {
        this.selectedRow = selectedRow;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getSubProcessName() {
        return subProcessName.get();
    }

    public SimpleStringProperty subProcessNameProperty() {
        return subProcessName;
    }

    public void setSubProcessName(String subProcessName) {
        this.subProcessName.set(subProcessName);
    }

    public String getReferenceAlign() {
        return referenceAlign.get();
    }

    public SimpleStringProperty referenceAlignProperty() {
        return referenceAlign;
    }

    public void setReferenceAlign(String referenceAlign) {
        this.referenceAlign.set(referenceAlign);
    }


}
