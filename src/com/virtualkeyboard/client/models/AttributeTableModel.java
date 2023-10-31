package com.virtualkeyboard.client.models;

import com.virtualkeyboard.client.util.Constant;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.w3c.dom.Text;


public class AttributeTableModel {


    public ComboBox getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(ComboBox attributeType) {
        this.attributeType = attributeType;
    }

    public TextField getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(TextField attributeName) {
        this.attributeName = attributeName;
    }

    public TextField getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(TextField attributeValue) {
        this.attributeValue = attributeValue;
    }

    private ComboBox attributeType;

    private TextField attributeName;


    private TextField attributeValue;


    public AttributeTableModel() {


    }







}
