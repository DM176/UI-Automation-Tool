package com.virtualkeyboard.client.view;


import com.virtualkeyboard.client.models.AttributeTableModel;
import com.virtualkeyboard.client.models.EventTableModel;
import com.virtualkeyboard.client.util.Constant;
import com.virtualkeyboard.client.util.FormValue;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {

    LandingPageController landingPageController;

    public TableInfo(LandingPageController landingPageController) {
        this.landingPageController = landingPageController;
    }

    public TableInfo() {

    }

    @FXML
    TextField sNo;

    public List<TableColumn> initializeTableHeadersForEvents(TableView tableView) {

        List<TableColumn> tableColumnList = new ArrayList<>();

        /**
         * ****************SN0***************************
         */
        TableColumn sNo1 = new TableColumn("S/No");
        sNo1.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("sNo"));
        sNo1.setMaxWidth(900);
        sNo1.setStyle("-fx-alignment: CENTER;");
        tableColumnList.add(sNo1);

        /**
         * ****************Select Row***************************
         */
        TableColumn selectedRow = new TableColumn("Select");
        selectedRow.setCellValueFactory(new PropertyValueFactory<EventTableModel, CheckBox>("selectedRow"));
        selectedRow.setStyle("-fx-alignment: CENTER;");
        selectedRow.setMaxWidth(1000);
        tableColumnList.add(selectedRow);

        /**
         * ****************Events Head***************************
         */
        TableColumn eventsHead = new TableColumn("Events-head");
        eventsHead.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("eventsHead"));
        eventsHead.setMaxWidth(3000);
        eventsHead.setStyle("-fx-alignment: CENTER;");
        tableColumnList.add(eventsHead);


//    //****************Loop**************************

////    TableColumn loopCol= new TableColumn("Loop");
////    loopCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(null));
////    // upsertActionButton.setCellValueFactory(new PropertyValueFactory<TableHeaderModel,String>("upsertAction"));
////    loopCol.setCellFactory(param -> new TableCell<TableHeaderModel, Void>() {
////        private final HBox hbox = new HBox();
////        private final TextField startIndex =new TextField();
////        private final TextField lastIndex =new TextField();
////        {
////            startIndex.setPrefWidth(30);
////            lastIndex.setPrefWidth(30);
////
////            hbox.getChildren().addAll(startIndex,lastIndex);
////            // Set the spacing between the buttons
////            hbox.setSpacing(10);
////            // Center the HBox within the cell
////            hbox.setAlignment(Pos.CENTER);
////
////        }
////
////
////        @Override
////        protected void updateItem(Void item, boolean empty) {
////            super.updateItem(item, empty);
////            if (empty) {
////                setGraphic(null);
////            } else {
////                setGraphic(hbox);
////            }
////        }
////    });
////    tableColumnList.add( loopCol);
//
        /**
         ******************Event**************************
         */
        TableColumn eventComboCol = new TableColumn("Event*");
        eventComboCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("event"));
        eventComboCol.setMaxWidth(2200);
        eventComboCol.setStyle("-fx-alignment: CENTER;");
        tableColumnList.add(eventComboCol);

        /**
         ******************Sub-Process**************************
         */
        TableColumn subProcessComboCol = new TableColumn("Sub-Process");
        subProcessComboCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("subProcessName"));
        subProcessComboCol.setMaxWidth(2200);
        subProcessComboCol.setStyle("-fx-alignment: CENTER;");
        tableColumnList.add(subProcessComboCol);
        /**
         *********************Active************************
         */

        TableColumn activeCol = new TableColumn("Active");
        activeCol.setMaxWidth(1300);
        activeCol.setStyle("-fx-alignment: CENTER;");
        activeCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("active"));
        tableColumnList.add(activeCol);

        /**
         *********************Mandatory************************
         */

        TableColumn mandatoryCol = new TableColumn("Mandatory");
        mandatoryCol.setMaxWidth(1300);
        mandatoryCol.setStyle("-fx-alignment: CENTER;");
        mandatoryCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("mandatory"));
        tableColumnList.add(mandatoryCol);

        /**
         * *********************Image Upload********************************
         */


        TableColumn imageColumn = new TableColumn("Image");
        imageColumn.setMaxWidth(3000);
        imageColumn.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("iconImage"));
        imageColumn.setPrefWidth(200);
        tableColumnList.add(imageColumn);
        /**
         * *********************Reference-Image********************************
         */
        TableColumn referenceImageColumn = new TableColumn("Reference-Image");
        referenceImageColumn.setMaxWidth(3000);
        referenceImageColumn.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("referenceImage"));
        referenceImageColumn.setPrefWidth(200);
        tableColumnList.add(referenceImageColumn);
        /**
         * ********************Search Text***************************
         */
        TableColumn searchTextCol = new TableColumn("Search-Text");
        searchTextCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("searchText"));
        tableColumnList.add(searchTextCol);

        /**
         * ********************Insert Text***************************
         */
        TableColumn insertTextCol = new TableColumn("Insert-Text");
        insertTextCol.setPrefWidth(100);
        insertTextCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("insertText"));

        tableColumnList.add(insertTextCol);

        /**
         * ********************Description***************************
         */
        TableColumn descriptionCol = new TableColumn("Description");
        descriptionCol.setPrefWidth(100);
        descriptionCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("description"));
        descriptionCol.setVisible(false);
        tableColumnList.add(descriptionCol);


        /**
         * ********************Wait time***************************
         */
        TableColumn waitTimeCol = new TableColumn("Wait-Time");
        waitTimeCol.setMaxWidth(2000);
        waitTimeCol.setStyle("-fx-alignment: CENTER;");
        waitTimeCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("waitTime"));
        tableColumnList.add(waitTimeCol);
        //********************Status***************************
        TableColumn status = new TableColumn("Status");
        status.setMaxWidth(3000);
        status.setStyle("-fx-alignment: CENTER;");
        status.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("status"));
        tableColumnList.add(status);

        /**
         *************************Edit***************************
         */
        TableColumn upsertActionButton = new TableColumn("Action");
        upsertActionButton.setCellValueFactory(cellData -> new SimpleObjectProperty<>(null));
        // upsertActionButton.setCellValueFactory(new PropertyValueFactory<TableHeaderModel,String>("upsertAction"));
        upsertActionButton.setCellFactory(param -> new TableCell<EventTableModel, Void>() {

            private final HBox hbox = new HBox();
            private final Button editBtn = new Button("edit");
            private final Button removeBtn = new Button("remove");

            {
                editBtn.setOnAction(event -> {

                    landingPageController.sNo.setText(getTableView().getItems().get(getIndex()).getsNo());
                    landingPageController.active.setSelected(getTableView().getItems().get(getIndex()).getActive());
                    landingPageController.insertText.setText(getTableView().getItems().get(getIndex()).getInsertText());
                    landingPageController.eventType.setValue(getTableView().getItems().get(getIndex()).getEvent());

                    landingPageController.searchText.setText(getTableView().getItems().get(getIndex()).getSearchText());
                    landingPageController.waitTime.setText(getTableView().getItems().get(getIndex()).getWaitTime());
                    landingPageController.mondatoryCheckBox.setSelected(getTableView().getItems().get(getIndex()).getMandatory().equals(Constant.YES)?true:false);
                    landingPageController.eventsHead.setSelected(getTableView().getItems().get(getIndex()).getEventsHead().equals(Constant.YES)?true:false);
                    landingPageController.subProcessComboBox.setValue(getTableView().getItems().get(getIndex()).getSubProcessName());
                    if (getTableView().getItems().get(getIndex()).getIconImage() != null) {

                        landingPageController.iconImageView.setImage(getTableView().getItems().get(getIndex()).getIconImage().getImage());
                    }
                    if (getTableView().getItems().get(getIndex()).getReferenceImage() != null) {
                        landingPageController.referenceAlignCombo.setValue(getTableView().getItems().get(getIndex()).getReferenceAlign());
                        landingPageController.referenceImageView.setImage(getTableView().getItems().get(getIndex()).getReferenceImage().getImage());
                    }

                    landingPageController.addRowBtn.setText("Update");

                });
                removeBtn.setOnAction(event -> {
                    getTableView().getItems().remove(getIndex());
                    TableInfo.this.updateIndex(getTableView());
                    getTableView().refresh();


                });

                hbox.getChildren().addAll(editBtn, removeBtn);
                // Set the spacing between the buttons
                hbox.setSpacing(10);
                // Center the HBox within the cell
                hbox.setAlignment(Pos.CENTER);

            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                    hbox.setAlignment(Pos.CENTER);
                }
            }
        });
        tableColumnList.add(upsertActionButton);

        return tableColumnList;

    }


    public List<TableColumn> initializeTableHeadersForAttributes(TableView tableView) {

        List<TableColumn> tableColumnList = new ArrayList<>();



        /**
         *********************Attribute-Name************************
         */

        TableColumn attributeNameCol = new TableColumn("Attribute-Name");
        attributeNameCol.setStyle("-fx-alignment: CENTER;");
     //   attributeNameCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("attributeName"));
        attributeNameCol.setCellFactory(param -> new TableCell<AttributeTableModel, Void>() {

        private final TextField textField =new TextField();
        {

        }


        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(textField);
            }
        }
    });


        tableColumnList.add(attributeNameCol);

        /**
         ******************Attribute-Type**************************
         */
        TableColumn attributeTypeCol = new TableColumn("Attribute-Type");
      //  attributeTypeCol.setCellValueFactory(new PropertyValueFactory<AttributeTableModel, String>("attributeType"));

        attributeTypeCol.setStyle("-fx-alignment: CENTER;");

        attributeTypeCol.setCellFactory(param -> new TableCell<AttributeTableModel, Void>() {

            private final ComboBox comboBox =new ComboBox();
            {
                FormValue.populateObjectTypeValues(comboBox);

            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(comboBox);
                }
            }
        });

        tableColumnList.add(attributeTypeCol);
        /**
         *********************Attribute-Value************************
         */

        TableColumn attributeValueCol = new TableColumn("Attribute-Value");

        attributeValueCol.setStyle("-fx-alignment: CENTER;");
     //   attributeValueCol.setCellValueFactory(new PropertyValueFactory<EventTableModel, String>("attributeValue"));
        attributeValueCol.setCellFactory(param -> new TableCell<AttributeTableModel, Void>() {

            private final TextArea textArea =new TextArea();
            {
                textArea.setMaxHeight(300);
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(textArea);
                }
            }
        });

        tableColumnList.add(attributeValueCol);



        /**
         *************************Edit***************************
         */
        TableColumn upsertActionButton = new TableColumn("Action");
        upsertActionButton.setCellValueFactory(cellData -> new SimpleObjectProperty<>(null));
        // upsertActionButton.setCellValueFactory(new PropertyValueFactory<TableHeaderModel,String>("upsertAction"));
        upsertActionButton.setCellFactory(param -> new TableCell<AttributeTableModel, Void>() {

            private final HBox hbox = new HBox();
            private final Button addBtn = new Button("Add");
            private final Button removeBtn = new Button("remove");

            {
                addBtn.setOnAction(event -> {
                        getTableView().getItems().add(new AttributeTableModel());


                });
                removeBtn.setOnAction(event -> {

                    getTableView().getItems().remove(getIndex());
//            //        TableInfo.this.updateIndex(getTableView());
               //     getTableView().refresh();


                });

                hbox.getChildren().addAll(addBtn, removeBtn);
                // Set the spacing between the buttons
                hbox.setSpacing(10);
                // Center the HBox within the cell
                hbox.setAlignment(Pos.CENTER);

            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                    hbox.setAlignment(Pos.CENTER);
                }
            }
        });
        tableColumnList.add(upsertActionButton);

        return tableColumnList;

    }

    public void updateIndex(TableView<EventTableModel> tableView) {
        Integer index = 0;
        for (int i = 0; i < tableView.getItems().size(); i++) {
            index = i + 1;
            tableView.getItems().get(i).setsNo(String.valueOf(index));

        }
    }

}
