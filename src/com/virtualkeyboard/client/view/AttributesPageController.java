package com.virtualkeyboard.client.view;

import com.lowagie.text.Table;
import com.virtualkeyboard.client.models.AttributeTableModel;
import com.virtualkeyboard.client.util.FormValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AttributesPageController implements Initializable {

   @FXML
    TableView attributeTable;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableInfo tableInfo=new TableInfo();

        for (TableColumn tableColumn : tableInfo.initializeTableHeadersForAttributes(attributeTable)) {
            System.out.println("----" +tableColumn.getText());
            attributeTable.getColumns().add(tableColumn);
        }

        attributeTable.getItems().add(new AttributeTableModel());






    }


}
