package com.virtualkeyboard.client.view;

import com.virtualkeyboard.client.models.EventProcess;
import com.virtualkeyboard.client.models.Response;
import com.virtualkeyboard.client.models.EventTableModel;
import com.virtualkeyboard.client.process.ExcelProcessService;
import com.virtualkeyboard.client.process.ProcessExecution;
import com.virtualkeyboard.client.util.Constant;
import com.virtualkeyboard.client.util.FormValue;
import com.virtualkeyboard.client.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Row;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class LandingPageController implements Initializable {

    @FXML
    private AnchorPane parentAnchorPane;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button startButton;
    @FXML
    private TextField sheetTextField;
    @FXML
    private TextField excelPathText;
    @FXML
    private Label chatResponse;
    @FXML
    private TableView eventTable;
    @FXML
    private Button executeProcess;
    @FXML
    private Tab automationTemplateTab;
    @FXML
    private Tab googleChatTab;
    @FXML
    Pane headerPane;
    @FXML
    ComboBox AutoComboForNewOrExisting;
    @FXML
    TextField saveProcessTF;
    @FXML
    Label saveProcessLabel;
    @FXML
    Button loadExcelBtn;
    @FXML
    CheckBox mondatoryCheckBox;
    @FXML
    TextField sNo;
    @FXML
    ComboBox eventType;
    @FXML
    Button iconBrowse;
    @FXML
    ImageView iconImageView;
    @FXML
    ImageView referenceImageView;
    @FXML
    Button referenceBrowse;
    @FXML
    TextField searchText;
    @FXML
    TextArea insertText;
    @FXML
    TextField waitTime;
    @FXML
    CheckBox active;
    @FXML
    Button addRowBtn;
    @FXML
    TextField excelPathTF;
    @FXML
    Button subEventsBtn;
    @FXML
    CheckBox eventsHead;
    @FXML
    TextField descriptionTextField;
    @FXML
    ListView<String> subProcessListView;
    @FXML
    Button parentProcessBtn;
    @FXML
    ComboBox referenceAlignCombo;
    @FXML
    ComboBox subProcessComboBox;

    @FXML
    ImageView chatImageView;
    @FXML
    Button chatBrowse;

    @FXML
    TextField widthResolution;

    @FXML
    TextField heightResolution;
    private TableInfo tableInfo = new TableInfo(this);

    private ProcessExecution processExecution = new ProcessExecution();


    private HashMap<String, List<EventTableModel>> subEventsHashMap = new HashMap<>();

    private List<EventTableModel> tempParentEventTable = new ArrayList<>();

    private String selectedProcess = Constant.PARENT_PROCESS;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        FileInputStream inputStream = null;
        try {

            //log0
            //   InputText.capslock();
            Image image = new Image(getClass().getResourceAsStream("/icons/logo.png"));

            logoImageView.setImage(image);
            sheetTextField.setText("sheet0");
            excelPathText.setText(Constant.path + "CaseFlowFormat.xlsx");
            AutoComboForNewOrExisting.getItems().add("Existing");
            AutoComboForNewOrExisting.getItems().add("New");
            headerPane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            FormValue.populateEventValues(eventType);
            FormValue.populateReferenceAlign(referenceAlignCombo);
            referenceAlignCombo.setValue("both");
            ObservableList<EventTableModel> data = FXCollections.observableArrayList(
            );
            eventTable.setItems(data);
            sNo.setText(String.valueOf(eventTable.getItems().size() + 1));
            parentProcessBtn.setDisable(true);
            widthResolution.setText(String.valueOf(Constant.RESOLUTION_WIDTH));
            heightResolution.setText(String.valueOf(Constant.RESOLUTION_HEIGHT));
            /********************************************************
             * *****************For Generic Automation++++++++++++++++++
             ++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            /**
             *  For import and export of Combo box
             */
            AutoComboForNewOrExisting.setOnAction(event -> {
                if (AutoComboForNewOrExisting.getValue().equals("Existing")) {

//                    if(!clearAllContent() && loadExcelBtn.getText().equals(Constant.Excel_Btn_States.SAVE)){
//                        AutoComboForNewOrExisting.setValue("New");
//                        return;
//                    };
                    loadExcelBtn.setText(Constant.Excel_Btn_States.BROWSE);
                    excelPathTF.setText("");
                    saveProcessLabel.setVisible(false);
                } else if (AutoComboForNewOrExisting.getValue().equals("New")) {
//                    if(!clearAllContent() && loadExcelBtn.getText().equals(Constant.Excel_Btn_States.UPDATE)){
//                        AutoComboForNewOrExisting.setValue("Existing");
//                        return;
//                    };
                    excelPathTF.setText("");
                    loadExcelBtn.setText(Constant.Excel_Btn_States.SAVE);
                    System.out.println("New");
                }
            });


            /**
             *  Load and Save of Excel File
             */
            loadExcelBtn.setOnAction(event -> {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select Excel File");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files", "*.xlsx"));
                    ExcelProcessService excelProcessService = new ExcelProcessService();
                    if (loadExcelBtn.getText().equals(Constant.Excel_Btn_States.BROWSE)) {
                        if (!clearAllContent()) return;
                        File file = fileChooser.showOpenDialog(loadExcelBtn.getScene().getWindow());
                        if (file == null) return;
                        List<EventTableModel> eventProcesses = excelProcessService.ImportProcessFlow(file, subEventsHashMap);
                        createSubEventBtns(subEventsHashMap);
                        eventTable.setItems(FXCollections.observableArrayList(eventProcesses));
                        loadExcelBtn.setText(Constant.Excel_Btn_States.UPDATE);
                        excelPathTF.setText(file.getAbsolutePath());
                        eventTable.refresh();
                    } else if (loadExcelBtn.getText().equals(Constant.Excel_Btn_States.SAVE)) {
                        fileChooser.setTitle("Save Excel file");
                        Stage stage = (Stage) loadExcelBtn.getScene().getWindow();
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files", "*.xlsx"));
                        File file = fileChooser.showSaveDialog(stage);
                        if (file == null) return;
                        excelProcessService.createExcelFromAutomationTemplate(eventTable, file, subEventsHashMap);
                        excelPathTF.setText(file.getAbsolutePath());
                        loadExcelBtn.setText(Constant.Excel_Btn_States.UPDATE);
                    } else if (loadExcelBtn.getText().equals(Constant.Excel_Btn_States.UPDATE)) {
                        if (!excelPathTF.getText().isEmpty()) {
                            loadParentEvents();

                            File file = new File(excelPathTF.getText());
                            excelProcessService.createExcelFromAutomationTemplate(eventTable, file, subEventsHashMap);
                            excelPathTF.setText(file.getAbsolutePath());
                            loadExcelBtn.setText(Constant.Excel_Btn_States.UPDATE);
                        } else {
                            Util.showPopupMessage(Constant.Status.INFO, "Information Dialog", null, "Excel Path is empty!", null).showAndWait();
                        }
                    }

                } catch (Exception ex) {
                    Util.showPopupMessage(Constant.Status.EXC, "Exception Dialog", "Look, an Exception Dialog", null, ex).showAndWait();
                }

            });


            /**
             * Loading table Header
             */
            for (TableColumn tableColumn : tableInfo.initializeTableHeadersForEvents(eventTable)) {
                System.out.println(tableColumn.getText());
                eventTable.getColumns().add(tableColumn);
            }


            /**
             * Execute Events
             */
            executeProcess.setOnAction(event -> {
                Stage stage = (Stage) executeProcess.getScene().getWindow();
                try {

                    stage.hide();
                    Thread.sleep(2000);
                    ObservableList<EventTableModel> items = eventTable.getItems();
                    List<EventTableModel> dataList = new ArrayList<>(items);
                    for(EventTableModel eventTableModel:dataList){
                        eventTableModel.setStatus("");
                    }
                    ExcelProcessService excelProcessService = new ExcelProcessService();
                    List<EventProcess> eventProcesses = excelProcessService.excuteProcessOfForm(dataList, subEventsHashMap);
                    ProcessExecution processExecution = new ProcessExecution();
                    eventProcesses = processExecution.executeProcess(eventProcesses);
                    updateResponsesOfAutomation(eventProcesses);
                    stage.show();


                    Util.showPopupMessage(Constant.Status.INFO, "Information Dialog", null, "Process Completed Successfully", null).showAndWait();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    stage.show();

                    //  Util.showPopupMessage(Constant.Status.EXC, "Exception Dialog", "Look, an Exception Dialog", null, ex).showAndWait();
                }
            });


            eventType.valueProperty().addListener((observable, oldValue, newValue) -> {
                // This code will be executed when the selection in the ComboBox changes.
                eventComboChecks();

            });
            iconBrowse.setOnAction(event -> {
                // Open a FileChooser dialog to select an image file
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Image File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                File file = fileChooser.showOpenDialog(iconBrowse.getScene().getWindow());
                if (file != null) {
                    System.out.println(file.getAbsolutePath());
                    Image iconImage = new Image(file.toURI().toString());
                    iconImageView.setImage(iconImage);
                    //Setting the image view parameters
                    iconImageView.setFitWidth(60);
                    iconImageView.setFitHeight(60);
                    //imageView.setFitWidth(100);
                    iconImageView.setPreserveRatio(true);
                }
            });

            referenceBrowse.setOnAction(event -> {
                // Open a FileChooser dialog to select an image file
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Image File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                File file = fileChooser.showOpenDialog(referenceBrowse.getScene().getWindow());
                if (file != null) {
                    System.out.println(file.getAbsolutePath());
                    Image iconImage = new Image(file.toURI().toString());
                    referenceImageView.setImage(iconImage);
                    //Setting the image view parameters
                    referenceImageView.setFitWidth(60);
                    referenceImageView.setFitHeight(60);
                    //imageView.setFitWidth(100);
                    referenceImageView.setPreserveRatio(true);
                }
            });


            addRowBtn.setOnAction(event -> {
                try {
                    if (addRowBtn.getText().equals("Add")) {

                        sNo.setText(String.valueOf(eventTable.getItems().size() + 1));

                    }

                    Response response = mapValueForAutomationPanel();

                    if (response.getStatus().equals(Constant.Status.FAIL)) {
                        Util.showPopupMessage(Constant.Status.INFO, "Information Dialog", null, response.getMessage(), null).showAndWait();
                        return;
                    }
                    System.out.println("Completed");
                    clearContent();
                    if (addRowBtn.getText().equals("Update")) {
                        addRowBtn.setText("Add");
                    }

                    System.out.println("Content clear");
                } catch (Exception ex) {
                    Util.showPopupMessage(Constant.Status.EXC, "Exception Dialog", "Look, an Exception Dialog", null, ex).showAndWait();
                }
            });


            /**
             * Sub event
             */
            subEventsBtn.setOnAction(event -> {

                ObservableList<EventTableModel> items = eventTable.getItems();
                List<EventTableModel> dataList = new ArrayList<>(items);

                List<EventTableModel> subEvents = dataList.stream()
                        .filter(item -> item.getSelectedRow().isSelected())
                        .collect(Collectors.toList());

                if (!subEvents.isEmpty()) {

                    Response response = Util.inputDialog(subEventsHashMap);
                    if (response.getStatus().equals(Constant.Status.SUCCESS)) {
                        System.out.println("Event Name " + response.getMessage());
                        subEventsHashMap.put(response.getMessage(), subEvents);

                        //  dataList.removeAll(subEvents);
                        eventTable.getItems().removeAll(subEvents);

                        if (selectedProcess.equals(Constant.PARENT_PROCESS)) {
                            dataList = new ArrayList<>(eventTable.getItems());
                            tempParentEventTable = dataList;
//                            System.out.println("Assigned to parent");
                        }
                        selectedProcess = response.getMessage();
                        tableInfo.updateIndex(eventTable);
                        subProcessListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                            @Override
                            public ListCell<String> call(ListView<String> param) {
                                return new ButtonListCell();
                            }
                        });
                        subProcessComboBox.getItems().add(response.getMessage());
                        subProcessListView.getItems().add(response.getMessage());
                        eventTable.refresh();
                    } else {
                        return;
                    }


                    //    subProcessListView.getItems().add(new Button(response.getMessage()));

                } else {
                    Util.showPopupMessage(Constant.Status.INFO, "Information Dialog", null, "Please select any event", null).showAndWait();
                }
            });


            /**
             * Reload Parent
             */
            parentProcessBtn.setOnAction(event -> {
                loadParentEvents();
            });

            /********************************************************
             * *****************For Chat Automation++++++++++++++++++
             ++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            /**
             * For chat automation
             */

            chatBrowse.setOnAction(event -> {

                // Open a FileChooser dialog to select an image file
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Image File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                File file = fileChooser.showOpenDialog(chatBrowse.getScene().getWindow());
                if (file != null) {
                    System.out.println(file.getAbsolutePath());
                    Image iconImage = new Image(file.toURI().toString());
                    chatImageView.setImage(iconImage);
                    //Setting the image view parameters
                    chatImageView.setFitWidth(150);
                    chatImageView.setFitHeight(200);
                    //imageView.setFitWidth(100);
                    chatImageView.setPreserveRatio(true);
                }
            });
            startButton.setOnAction(event -> {

                if(chatImageView.getImage()==null){
                    Util.showPopupMessage(Constant.Status.INFO, "Information Dialog", null, "Please select Message textField icon", null).showAndWait();
                    return;
                }
                //  System.out.println(automationComboBox.getValue());
                Stage stage = (Stage) startButton.getScene().getWindow();
                try {

                    stage.hide();
                    Thread.sleep(5000);
                    long startTime = System.currentTimeMillis();


                    Response response = processExecution.executeChatProcess(excelPathText.getText(), sheetTextField.getText(),chatImageView.getImage());
                  Util.showPopupMessage(Constant.Status.INFO, "Information Dialog", null, response.getMessage(), null).showAndWait();
//                      chatResponse.setVisible(true);
//                    chatResponse.setText(response.getMessage());
                    long endTime = System.currentTimeMillis(); // get end time

                    long duration = (endTime - startTime) / 60000; // calculate duration in minutes

                    System.out.println("Start time: " + new Date(startTime));
                    System.out.println("End time: " + new Date(endTime));
                    System.out.println("Duration: " + duration + " minutes");
                    stage.show();
                } catch (Exception ex) {
                    Util.showPopupMessage(Constant.Status.EXC, "Information Dialog", null, ex.getMessage(), null).showAndWait();
//
                }

                System.out.println("Button clicked!");
            });


            /********************************************************
             * *****************For Configuration++++++++++++++++++
             ++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            widthResolution.focusedProperty().addListener((observable, oldValue, newValue) -> {
                // This code will be executed when the focus of the TextField changes
                if (!newValue) {
                    // The TextField lost focus (clicked outside), so handle the event here
                    System.out.println("Status of empty " + widthResolution.getText().isEmpty());
                    if(widthResolution.getText().isEmpty())
                    {
                        Util.showPopupMessage(Constant.Status.INFO, "Information Dialog", null, "Resolution width cannot be empty!", null).showAndWait();
                    }else {
                        Constant.RESOLUTION_WIDTH=Integer.parseInt(widthResolution.getText());
                    }

                }
            });
            heightResolution.focusedProperty().addListener((observable, oldValue, newValue) -> {
                // This code will be executed when the focus of the TextField changes
                System.out.println("Width " + Constant.RESOLUTION_WIDTH);
                if(heightResolution.getText().isEmpty())
                {
                    Util.showPopupMessage(Constant.Status.INFO, "Information Dialog", null, "Resolution width cannot be empty!", null).showAndWait();
                }else if(newValue){
                    Constant.RESOLUTION_HEIGHT=Integer.parseInt(heightResolution.getText());
                }
            });




        } catch (Exception ex) {
            Util.showPopupMessage(Constant.Status.EXC, "Exception Dialog", "Look, an Exception Dialog", null, ex).showAndWait();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }


    }


    public void clearContent() {

        sNo.setText(String.valueOf(eventTable.getItems().size() + 1));
        eventType.setValue("");
        referenceAlignCombo.setValue(Constant.BOTH);
        subProcessComboBox.setValue("");
        iconImageView.setImage(null);
        referenceImageView.setImage(null);
        active.setSelected(true);
        insertText.setText("");
        searchText.setText("");
        descriptionTextField.setText("");
        waitTime.setText("1000");

    }

    public Response mapValueForAutomationPanel() throws Exception {
        Response response = new Response();
        Boolean exists = false;
        EventTableModel eventTableModel = null;
        for (Object obj : eventTable.getItems()) {
            eventTableModel = (EventTableModel) obj;
            if (eventTableModel.getsNo().equals(sNo.getText())) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            response = validationChecks();
            if (response.getStatus().equals(Constant.Status.FAIL)) {
                return response;
            }
            eventTableModel = new EventTableModel();
            eventTableModel.setEvent(eventType.getValue().toString());
            eventTableModel.setActive(active.isSelected());
            eventTableModel.setsNo(String.valueOf(eventTable.getItems().size() + 1));
            eventTableModel.setSearchText(searchText.getText());
            eventTableModel.setReferenceAlign(referenceAlignCombo.getValue().toString());
            eventTableModel.setInsertText(insertText.getText());
            eventTableModel.setDescription(descriptionTextField.getText());
            eventTableModel.setMandatory(mondatoryCheckBox.isSelected() ? Constant.YES : Constant.NO);
            eventTableModel.setEventsHead(eventsHead.isSelected() ? Constant.YES : Constant.NO);
            eventTableModel.setWaitTime(waitTime.getText());
            if (eventType.getValue().toString().equals(Constant.Events.SUB_PROCESS) && subProcessComboBox.getValue() != null) {
                //eventTableModel.setEventTableModel(new EventTableModel());
                eventTableModel.setSubProcessName(subProcessComboBox.getValue().toString());
            }
            if (iconImageView != null && iconImageView.getImage() != null) {
                //   tableModel.setIconImagePath(URLDecoder.decode(iconImageView.getImage().getUrl().replace("file:/", ""), "UTF-8"));
                eventTableModel.getIconImage().setImage(iconImageView.getImage());
            }
            if (referenceImageView != null && referenceImageView.getImage() != null) {
                //  tableModel.se(URLDecoder.decode(referenceImageView.getImage().getUrl().replace("file:/", ""), "UTF-8"));
                eventTableModel.getReferenceImage().setImage(referenceImageView.getImage());
            }

            eventTable.getItems().add(eventTableModel);
            response.setStatus(Constant.Status.SUCCESS);
            response.setObject(eventTableModel);

            return response;
        } else {
            response = validationChecks();
            if (response.getStatus().equals(Constant.Status.FAIL)) {
                return response;
            }
            eventTableModel.setEvent(eventType.getValue().toString());
            eventTableModel.setActive(active.isSelected());
            eventTableModel.setDescription(descriptionTextField.getText());
            eventTableModel.setsNo(sNo.getText());
            eventTableModel.setReferenceAlign(referenceAlignCombo.getValue().toString());
            eventTableModel.setSearchText(searchText.getText());
            eventTableModel.setMandatory(mondatoryCheckBox.isSelected() ? Constant.YES : Constant.NO);
            eventTableModel.setEventsHead(eventsHead.isSelected() ? Constant.YES : Constant.NO);
            eventTableModel.setInsertText(insertText.getText());
            eventTableModel.setWaitTime(waitTime.getText());
            eventTableModel.setStatus("");
            if (eventType.getValue().toString().equals(Constant.Events.SUB_PROCESS) && subProcessComboBox.getValue() != null) {
                //eventTableModel.setEventTableModel(new EventTableModel());
                eventTableModel.setSubProcessName(subProcessComboBox.getValue().toString());
            }
            if (iconImageView != null && iconImageView.getImage() != null) {
                //  tableModel.setIconImagePath(URLDecoder.decode(iconImageView.getImage().getUrl().replace("file:/", ""), "UTF-8"));
                eventTableModel.getIconImage().setImage(iconImageView.getImage());
            }
            if (referenceImageView != null && referenceImageView.getImage() != null) {
                //  tableModel.setIconImagePath(URLDecoder.decode(iconImageView.getImage().getUrl().replace("file:/", ""), "UTF-8"));
                eventTableModel.getReferenceImage().setImage(referenceImageView.getImage());
            }
            eventTable.refresh();
            response.setStatus(Constant.Status.SUCCESS);
            response.setObject(eventTableModel);
            return response;

        }

    }

    public void eventComboChecks() {

        iconBrowse.setDisable(false);
        searchText.setDisable(false);
        insertText.setDisable(false);
        subProcessComboBox.setDisable(false);
        referenceAlignCombo.setDisable(false);
        referenceBrowse.setDisable(false);
        if (eventType.getValue().equals(Constant.Events.ENTER) ||
                eventType.getValue().equals(Constant.Events.MINIMIZE_ALL_TABS)
                || eventType.getValue().equals(Constant.Events.SCREENSHOT) ||
                eventType.getValue().equals(Constant.Events.SUB_PROCESS)

        ) {
            iconBrowse.setDisable(true);
            referenceBrowse.setDisable(true);
            referenceAlignCombo.setDisable(true);
            referenceAlignCombo.setDisable(true);
            insertText.setDisable(true);
            searchText.setDisable(true);

        } else if (eventType.getValue().equals(Constant.Events.INSERT_TEXT) || eventType.getValue().equals(Constant.Events.OPEN_FILE)) {
            iconBrowse.setDisable(true);
            searchText.setDisable(true);
            subProcessComboBox.setDisable(true);
            referenceBrowse.setDisable(true);
            referenceAlignCombo.setDisable(true);

        } else if (eventType.getValue().equals(Constant.Events.SINGLE_CLICK) || eventType.getValue().equals(Constant.Events.DOUBLE_CLICK)
                || eventType.getValue().equals(Constant.Events.RIGHT_CLICK) || eventType.getValue().equals(Constant.Events.RESUME_AFTER)) {
            insertText.setDisable(true);
            subProcessComboBox.setDisable(true);

        }


    }

    private void updateResponsesOfAutomation(List<EventProcess> eventProcesses) {
        String processParentId = "";
        EventTableModel eventTableModel = null;
        for (EventProcess eventProcess : eventProcesses) {
            if (!eventProcess.getParentId().isEmpty() && processParentId.equals(eventProcess.getParentId())) continue;
            for (Object obj : eventTable.getItems()) {
                eventTableModel = (EventTableModel) obj;
                //Assuming every process is success

                if (eventTableModel.getsNo().equals(eventProcess.getSequenceId())) {
                    //for exception to display record in table vew
                    if (eventProcess.getEvent().equals(Constant.Events.SCREENSHOT) && eventProcess.getIconImage() != null) {
                        eventTableModel.getIconImage().setImage(eventProcess.getIconImage());
                    } else if (eventProcess.getEvent().equals(Constant.Events.COUNT)) {
                        eventTableModel.setInsertText(eventProcess.getText());
                    }
                    eventTableModel.setStatus(eventProcess.getStatus());
                    break;
                } else if (eventProcess.getSequenceId().contains("-")
                        && !eventProcess.getParentId().isEmpty()
                        && eventProcess.getMandatory()
                        && eventProcess.getStatus().equals(Constant.Status.FAIL) &&
                        eventTableModel.getsNo().equals(eventProcess.getSequenceId().split("-")[0])
                ) {
                    eventTableModel.setStatus(eventProcess.getStatus());
                    processParentId = eventProcess.getParentId();
                    //need make the sub process
                    break;


                }

            }


        }
        List<EventTableModel> dataList = new ArrayList<>(eventTable.getItems());
        for (EventTableModel eventTableModel1 : dataList) {
            if (eventTableModel1.getStatus() == null || eventTableModel1.getStatus().isEmpty()) {
                eventTableModel1.setStatus(Constant.Status.SUCCESS);
            }
        }


        eventTable.refresh();

    }

    public Response validationChecks() {
        Response response = new Response();
        if (eventType.getValue() == null || eventType.getValue().toString().isEmpty()) {
            response.setMessage("Please select any Event");
            response.setStatus(Constant.Status.FAIL);
            return response;
        } else if (waitTime.getText().toString().isEmpty()) {
            response.setMessage("Please insert any value in wait time");
            response.setStatus(Constant.Status.FAIL);
            return response;
        } else if ((eventType.getValue().equals(Constant.Events.RIGHT_CLICK) ||
                eventType.getValue().equals(Constant.Events.SINGLE_CLICK) ||
                eventType.getValue().equals(Constant.Events.DOUBLE_CLICK))
                && (searchText.getText().isEmpty() && iconImageView.getImage() == null)) {
            response.setStatus(Constant.Status.FAIL);
            response.setMessage("Search Text or Icon field is empty. Please populate before add");
            return response;
        }
        response.setStatus(Constant.Status.SUCCESS);
        return response;
    }


    public void stopButton() throws InterruptedException {
        Stage stopperStage = new Stage();
        stopperStage.initStyle(StageStyle.UTILITY);

        // Create a stop button
        Button stopButton = new Button("Stop Process");
        stopButton.setOnAction(event -> {
            // Stop your process here
            // processRunning = false;
            stopperStage.close();
        });

        // Add the stop button to a StackPane
        StackPane stopPane = new StackPane(stopButton);

        // Set the scene and show the stage
        Scene stopScene = new Scene(stopPane);
        stopperStage.setScene(stopScene);
        stopperStage.show();
        Thread.sleep(10000);
    }


    // Custom ListCell implementation with a button
    private class ButtonListCell extends ListCell<String> {
        private Button button;

        public ButtonListCell() {
            button = new Button("Select");
            button.setOnAction(event -> {
                if (selectedProcess.equals(Constant.PARENT_PROCESS)) {
                    List<EventTableModel> dataList = new ArrayList<>(eventTable.getItems());
                    tempParentEventTable = dataList;
                }
                // Handle button click event here
                String item = getItem();
                List<EventTableModel> subEvents = subEventsHashMap.get(item);
                eventTable.getItems().clear();

                Util.disableColumnsInTable(eventTable, false);
                eventTable.setItems(FXCollections.observableArrayList(subEvents));
                tableInfo.updateIndex(eventTable);
                eventTable.refresh();
                parentProcessBtn.setDisable(false);
                selectedProcess = item;
                System.out.println("Clicked on button for item: " + item);
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                button.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                setGraphic(button);
                setText(item);
            }
        }
    }

    public void createSubEventBtns(HashMap<String, List<EventTableModel>> subEventsHashMap) {
        Iterator<String> itr = subEventsHashMap.keySet().iterator();
        String name = "";
        while (itr.hasNext()) {
            name = itr.next();
            subProcessListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new ButtonListCell();
                }
            });
            subProcessComboBox.getItems().add(name);
            subProcessListView.getItems().add(name);
        }
    }

    private void loadParentEvents() {
        List<EventTableModel> dataList = new ArrayList<>(eventTable.getItems());
        if (!selectedProcess.isEmpty() && !selectedProcess.equals(Constant.PARENT_PROCESS)) {
            subEventsHashMap.put(selectedProcess, dataList);
        }
        selectedProcess = Constant.PARENT_PROCESS;
        eventTable.setItems(FXCollections.observableArrayList(tempParentEventTable));
        parentProcessBtn.setDisable(true);
        Util.disableColumnsInTable(eventTable, true);
    }

    public Boolean clearAllContent() {
        if (eventTable.getItems().size() > 0 && !Util.confirmationDialog()) {
            return false;
        }
        eventTable.getItems().clear();
        subEventsHashMap = new HashMap<>();
        subProcessListView.getItems().clear();
        selectedProcess = Constant.PARENT_PROCESS;
        eventTable.refresh();
        subProcessListView.refresh();
        subProcessComboBox.getItems().clear();
        return true;

    }
}
