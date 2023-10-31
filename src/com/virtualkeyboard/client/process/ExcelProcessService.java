package com.virtualkeyboard.client.process;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import com.virtualkeyboard.client.models.CustomException;
import com.virtualkeyboard.client.models.Response;
import com.virtualkeyboard.client.models.EventTableModel;
import com.virtualkeyboard.client.util.Util;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import com.virtualkeyboard.client.models.EventProcess;
import com.virtualkeyboard.client.util.Constant;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;

/**
 * @author Hassam
 */
public class ExcelProcessService {

    /**
     * @return Loading all the process from Excel file :  Deprecated only working for chat
     * @throws IOException
     */
    public List<EventProcess> readProcessFlow(String excelPath, String sheetname) throws IOException, CustomException {
        FileInputStream fis = null;
        try {

            // File file = new File(Constant.path + "CaseFlowFormat.xlsx"); // creating a new file instance
            File file = new File(excelPath); // creating a new file instance

            fis = new FileInputStream(file); // obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
//            Integer sheetIndex=wb.getSheetIndex("events");
//            if(sheetIndex==null){
//                Exception exception=new Exception();
//                throw exception;
//            }
            XSSFSheet sheet = wb.getSheet(sheetname);
            List<EventProcess> eventProcesses = new ArrayList();
            EventProcess eventProcess = null;
            Map<String, Integer> headerMap = returnHeader(sheet);

            Iterator<Row> itr = sheet.iterator();
            itr.next();
            while (itr.hasNext()) {
                Row row = itr.next();
                eventProcess = new EventProcess();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each colum
                Iterator headerIterator = headerMap.entrySet().iterator();
                while (headerIterator.hasNext()) {
                    Map.Entry mapElement
                            = (Map.Entry) headerIterator.next();
                    //	System.out.println(mapElement.getKey());
                    Cell cell = row.getCell(headerMap.get(mapElement.getKey()));
                    if (cell != null) {
                        if (mapElement.getKey().equals(Constant.ExcelHeader.SNO)) {
                            eventProcess.setSequenceId(String.valueOf(cell.getNumericCellValue()));
                        } else if (mapElement.getKey().equals(Constant.ExcelHeader.EVENT)) {
                            eventProcess.setEvent(cell.getStringCellValue());
                        } else if (mapElement.getKey().equals(Constant.ExcelHeader.INSERT_TEXT)) {
                            eventProcess.setText(cell.getStringCellValue());
                        } else if (mapElement.getKey().equals(Constant.ExcelHeader.ACTIVE)) {
                            eventProcess.setActive(cell.getBooleanCellValue());
                        } else if (mapElement.getKey().equals(Constant.ExcelHeader.EMAIL)) {
                            eventProcess.setEmail(cell.getStringCellValue());
                        } else if (mapElement.getKey().equals(Constant.ExcelHeader.WAIT_TIME)) {
                            eventProcess.setWaitTime(cell.getNumericCellValue());
                        } else if (mapElement.getKey().equals(Constant.ExcelHeader.Image)) {
                            eventProcess.setIconName(cell.getStringCellValue());
                        } else if (mapElement.getKey().equals(Constant.ExcelHeader.SEARCH_TEXT)) {
                            eventProcess.setTargetText(cell.getStringCellValue());
                        }
                    }


                }

                if (eventProcess.getSequenceId() != null && eventProcess.getActive()) {
                    for (EventProcess eventProcess1 : eventProcesses) {

                        if (eventProcess1.getSequenceId().equals(eventProcess.getSequenceId())) {

                            CustomException customException = new CustomException();
                            customException.setMessage("S/No " + eventProcess1.getSequenceId() + " duplicate in the file ");
                            throw customException;
                        }
                    }

                    eventProcesses.add(eventProcess);
                }

            }

            return eventProcesses;
        } catch (Exception ex) {

            throw ex;
        } finally {
            if (fis != null)
                fis.close();
        }

    }


    /**
     * @return Loading all the process from Excel file :  Inprocess Yet
     * @throws IOException
     */
    public List<EventTableModel> ImportProcessFlow(File file, HashMap<String, List<EventTableModel>> subEventHashMap) throws IOException, CustomException {
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(file); // obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            String sheetName = "";
            int numberOfSheets = wb.getNumberOfSheets();
            List<EventTableModel> mainEventProcesses = new ArrayList();
            // Iterate over each sheet and retrieve the sheet names
            for (int i = 0; i < numberOfSheets; i++) {
                sheetName = wb.getSheetName(i);


                XSSFSheet sheet = wb.getSheet(sheetName);
                List<EventTableModel> eventProcesses = new ArrayList();
                EventTableModel eventProcess = null;
                Map<String, Integer> headerMap = returnHeader(sheet);

                Iterator<Row> itr = sheet.iterator();
                itr.next();
                while (itr.hasNext()) {
                    Row row = itr.next();

                    eventProcess = new EventTableModel();
                    Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each colum
                    Iterator headerIterator = headerMap.entrySet().iterator();
                    while (headerIterator.hasNext()) {
                        Map.Entry mapElement
                                = (Map.Entry) headerIterator.next();
                        System.out.println("-- " + mapElement.getKey());
                        Cell cell = row.getCell(headerMap.get(mapElement.getKey()));
                        if (cell != null || mapElement.getKey().equals(Constant.ExcelHeader.Image) || mapElement.getKey().equals(Constant.ExcelHeader.Reference_Image)) {

                            if (mapElement.getKey().equals(Constant.ExcelHeader.SNO)) {
                                eventProcess.setsNo(String.valueOf(cell.getStringCellValue()));
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.EVENTS_HEAD)) {

                                eventProcess.setEventsHead(cell.getStringCellValue());
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.SUB_PROCESS)) {

                                eventProcess.setSubProcessName(cell.getStringCellValue());
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.EVENT)) {

                                eventProcess.setEvent(cell.getStringCellValue());
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.INSERT_TEXT)) {
                                eventProcess.setInsertText(cell.getStringCellValue());
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.ACTIVE)) {

                                eventProcess.setActive(Boolean.parseBoolean(cell.getStringCellValue()));
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.Mandatory)) {

                                eventProcess.setMandatory(cell.getStringCellValue());
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.DESCRIPTION)) {
                                eventProcess.setDescription(cell.getStringCellValue());
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.WAIT_TIME)) {

                                eventProcess.setWaitTime(String.valueOf(cell.getNumericCellValue()));
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.Image)) {


                                byte[] bytes = null;
                                XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                                List<XSSFShape> shapes = drawing.getShapes();
                                for (XSSFShape shape : shapes) {
                                    if (shape instanceof XSSFPicture) {
                                        XSSFPicture picture = (XSSFPicture) shape;
                                        XSSFClientAnchor anchor = picture.getClientAnchor();

                                        if (anchor.getRow1() == row.getRowNum() && Integer.parseInt(mapElement.getValue().toString()) == anchor.getCol1()) {

                                            XSSFPictureData pictureData = picture.getPictureData();
                                            bytes = pictureData.getData();
                                            break;
                                            // Do something with the image data
                                        }
                                    }
                                }


                                //   eventProcess.setIconImagePath("D:\\Upwork\\Intellij Projects\\ScriptedVirtualKeyboard\\EventFlow\\addTab.png");
                                if (bytes != null)
                                    eventProcess.getIconImage().setImage(new Image(new ByteArrayInputStream(bytes)));
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.Reference_Image)) {
                                System.out.println("Entered");
                                byte[] bytes = null;
                                XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                                List<XSSFShape> shapes = drawing.getShapes();
                                for (XSSFShape shape : shapes) {
                                    if (shape instanceof XSSFPicture) {
                                        XSSFPicture picture = (XSSFPicture) shape;
                                        XSSFClientAnchor anchor = picture.getClientAnchor();

                                        if (anchor.getRow1() == row.getRowNum() && Integer.parseInt(mapElement.getValue().toString()) == anchor.getCol1()) {

                                            XSSFPictureData pictureData = picture.getPictureData();
                                            bytes = pictureData.getData();
                                            // break;
                                            // Do something with the image data
                                        }
                                    }
                                }


                                //   eventProcess.setIconImagePath("D:\\Upwork\\Intellij Projects\\ScriptedVirtualKeyboard\\EventFlow\\addTab.png");
                                if (bytes != null)
                                    eventProcess.getReferenceImage().setImage(new Image(new ByteArrayInputStream(bytes)));
                            } else if (mapElement.getKey().equals(Constant.ExcelHeader.SEARCH_TEXT)) {
                                eventProcess.setSearchText(cell.getStringCellValue());
                            }
                        }


                    }

                    if (eventProcess.getsNo() != null) {
                        for (EventTableModel eventTableModel : eventProcesses) {

                            if (eventTableModel.getsNo().equals(eventProcess.getsNo())) {

                                CustomException customException = new CustomException();
                                customException.setMessage("S/No " + eventTableModel.getsNo() + " duplicate in the file ");
                                throw customException;
                            }
                        }
                        eventProcesses.add(eventProcess);
                    }

                }
                if (sheetName.equals("Sheet1")) {
                    mainEventProcesses = eventProcesses;

                } else {
                    subEventHashMap.put(sheetName, eventProcesses);
                }

            }
            return mainEventProcesses;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (fis != null)
                fis.close();
        }

    }

    /**
     * Update the status of chat content sent to user or not
     * @param eventProcesses
     * @param path
     * @param sheetName
     * @throws IOException
     * @throws CustomException
     */
    public void updateResponseInExcel(List<EventProcess> eventProcesses, String path, String sheetName) throws IOException, CustomException {
        FileInputStream fis = null;
        FileOutputStream outFile = null;
        try {

            File file = new File(path); // creating a new file instance
            fis = new FileInputStream(file); // obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet(sheetName);

            Map<String, Integer> headerMap = returnHeader(sheet);
            Iterator<Row> itr = sheet.iterator();
            itr.next();

            while (itr.hasNext()) {
                Row row = itr.next();
                for (EventProcess eventProcess : eventProcesses) {

                    if (row.getCell(headerMap.get(Constant.ExcelHeader.SNO)) != null && String.valueOf(row.getCell(headerMap.get(Constant.ExcelHeader.SNO)).getNumericCellValue()).equals(eventProcess.getSequenceId())) {

                        Iterator headerIterator = headerMap.entrySet().iterator();

                        while (headerIterator.hasNext()) {
                            Map.Entry mapElement
                                    = (Map.Entry) headerIterator.next();
                            Cell cell = row.getCell(headerMap.get(mapElement.getKey()));
                            if (cell == null) {
                                cell = row.createCell(headerMap.get(mapElement.getKey()));
                            }

                            if (mapElement.getKey().equals(Constant.ExcelHeader.STATUS) && !Util.returnEmptyIfNull(eventProcess.getStatus()).isEmpty()) {
                                cell.setCellValue(eventProcess.getStatus());
                                break;
                            }

                        }
                    }
                }


            }
            fis.close();
            outFile = new FileOutputStream(path);
            wb.write(outFile);
            outFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            CustomException ce = new CustomException();
            ce.setMessage("Error occurred while updating the response");
            throw ce;

        } finally {
            outFile.close();
            fis.close();
        }
    }


    /**
     * return header of given sheet name
     * @param sheet
     * @return
     */
    private Map<String, Integer> returnHeader(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell headerCell = headerRow.getCell(i);
            if (headerCell != null && headerCell.getCellType() == CellType.STRING) {
                headerMap.put(headerCell.getStringCellValue(), i);
            }
        }
        return headerMap;
    }


    /**
     * execute events in sequence wise
     * @param eventTableModels
     * @param subEventHashMap
     * @return
     * @throws Exception
     */
    public List<EventProcess> excuteProcessOfForm(List<EventTableModel> eventTableModels, HashMap<String, List<EventTableModel>> subEventHashMap) throws Exception {

        List<EventProcess> eventProcesses = new ArrayList<>();
        EventProcess eventProcess = null;
        for (EventTableModel eventTableModel : eventTableModels) {

            if (eventTableModel.getEvent().equals(Constant.Events.SUB_PROCESS) && eventTableModel.getActive() && !eventTableModel.getSubProcessName().isEmpty()) {
                if (!eventTableModel.getActive()) continue;
                for (EventTableModel eventTableModel1 : subEventHashMap.get(eventTableModel.getSubProcessName())) {
                    eventProcess = mapEvents(eventTableModel1);
                    eventProcess.setSequenceId(eventTableModel.getsNo() + "-" + eventTableModel1.getsNo());
                    eventProcess.setParentId(eventTableModel.getsNo());
                    eventProcesses.add(eventProcess);
                }
            } else {
                eventProcess = mapEvents(eventTableModel);
                eventProcesses.add(eventProcess);
            }

            //  eventProcess.setIconName("D:\\Upwork\\Intellij Projects\\ScriptedVirtualKeyboard\\EventFlow\\folder.PNG");

        }
        return eventProcesses;

    }

    /**
     * map event from table model to process model
     * @param eventTableModel
     * @return
     */
    private EventProcess mapEvents(EventTableModel eventTableModel) {
        EventProcess eventProcess = null;
        eventProcess = new EventProcess();
        eventProcess.setEvent(eventTableModel.getEvent());
        eventProcess.setTargetText(eventTableModel.getSearchText());
        eventProcess.setActive(eventTableModel.getActive());
        eventProcess.setMandatory(eventTableModel.getMandatory().equals(Constant.YES) ? true : false);
        eventProcess.setEventsHead(eventTableModel.getEventsHead().equals(Constant.YES) ? true : false);
        eventProcess.setSequenceId(eventTableModel.getsNo());
        eventProcess.setReferenceAlign(eventTableModel.getReferenceAlign());
        eventProcess.setWaitTime(Double.valueOf(eventTableModel.getWaitTime()));
        eventProcess.setTargetText(eventTableModel.getSearchText());
        eventProcess.setText(eventTableModel.getInsertText());
        //   eventProcess.setIconName(tableHeaderModel.getTargetImageIcon() != null ? tableHeaderModel.getTargetImageIcon().getUrl() : null);

        eventProcess.setIconName(eventTableModel.getIconImagePath() != null ? eventTableModel.getIconImagePath() : null);
        eventProcess.setIconImage(eventTableModel.getIconImage() != null ? eventTableModel.getIconImage().getImage() : null);
        eventProcess.setReferenceImage(eventTableModel.getReferenceImage() != null ? eventTableModel.getReferenceImage().getImage() : null);
        return eventProcess;

    }

    /**
     * create excel from event table
     * @param automationTable
     * @param file
     * @param subEventHashMap
     * @return
     * @throws IOException
     */
    public Response createExcelFromAutomationTemplate(TableView automationTable, File file, HashMap<String, List<EventTableModel>> subEventHashMap) throws IOException {
        try {
            Workbook workbook = new XSSFWorkbook();
            String sheetName = "Sheet1";
            boolean isNext = false;
            List<EventTableModel> data = new ArrayList<>(automationTable.getItems());
            Iterator<String> itr = subEventHashMap.keySet().iterator();
            do {
                isNext = false;
                Sheet sheet = workbook.createSheet(sheetName);
                Row headerRow = sheet.createRow(0);
                ObservableList<TableColumn> columns = automationTable.getColumns();
                int colIncr = 0;
                for (TableColumn column : columns) {
                    if (column.getText().equals("Select")) {
                        continue;
                    }
                    String headerText = column.getText();
                    Cell cell = headerRow.createCell(colIncr);
                    cell.setCellValue(headerText);
                    colIncr++;
                }

                String cellData = "";
                double numericCellData = 0;
                Cell cell = null;

                for (int i = 0; i < data.size(); i++) {
                    EventTableModel rowData = data.get(i);
                    Row row = sheet.createRow(i + 1);
                    //for (int j = 0; j < rowData.size(); j++) {
                    //    String cellData = rowData.get(j);
                    //   Cell cell = row.createCell(j);
                    //  cell.setCellValue(cellData);
                    // }
                    cellData = rowData.getsNo();
                    cell = row.createCell(0);
                    cell.setCellValue(cellData);

                    cellData = rowData.getEventsHead();
                    cell = row.createCell(1);
                    cell.setCellValue(cellData);

                    cellData = rowData.getEvent();
                    cell = row.createCell(2);
                    cell.setCellValue(cellData);

                    cellData = rowData.getSubProcessName();
                    cell = row.createCell(3);
                    cell.setCellValue(cellData);


                    cellData = rowData.getActive().toString();
                    cell = row.createCell(4);
                    cell.setCellValue(cellData);

                    cellData = rowData.getMandatory().toString();
                    cell = row.createCell(5);
                    cell.setCellValue(cellData);


                    //  cellData = rowData.getIconImagePath();
                    cell = row.createCell(6);
                    // cell.setCellValue(cellData);

                    /**
                     * --------------------------------
                     */
                    if (rowData.getIconImage() != null && rowData.getIconImage().getImage() != null) {

                        BufferedImage image = SwingFXUtils.fromFXImage(rowData.getIconImage().getImage(), null);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", byteArrayOutputStream);
                        XSSFClientAnchor anchor = new XSSFClientAnchor();
                        anchor.setCol1(6); // starting column index
                        anchor.setRow1(row.getRowNum()); // starting row index
                        anchor.setCol2(7); // ending column index
                        anchor.setRow2(row.getRowNum() + 1); // ending row index
                        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                        int pictureIndex = workbook.addPicture(byteArrayOutputStream.toByteArray(), Workbook.PICTURE_TYPE_PNG);
                        XSSFPicture picture = drawing.createPicture(anchor, pictureIndex);

                    }
                    if (rowData.getReferenceImage() != null && rowData.getReferenceImage().getImage() != null) {

                        BufferedImage image = SwingFXUtils.fromFXImage(rowData.getReferenceImage().getImage(), null);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", byteArrayOutputStream);
                        XSSFClientAnchor anchor = new XSSFClientAnchor();
                        anchor.setCol1(7); // starting column index
                        anchor.setRow1(row.getRowNum()); // starting row index
                        anchor.setCol2(8); // ending column index
                        anchor.setRow2(row.getRowNum() + 1); // ending row index
                        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                        int pictureIndex = workbook.addPicture(byteArrayOutputStream.toByteArray(), Workbook.PICTURE_TYPE_PNG);
                        XSSFPicture picture = drawing.createPicture(anchor, pictureIndex);

                    }


                    cellData = rowData.getSearchText();
                    cell = row.createCell(8);
                    cell.setCellValue(cellData);

                    cellData = rowData.getInsertText();
                    cell = row.createCell(9);
                    cell.setCellValue(cellData);

                    cellData = rowData.getDescription();
                    cell = row.createCell(10);
                    cell.setCellValue(cellData);

                    numericCellData = Double.valueOf(rowData.getWaitTime());
                    cell = row.createCell(11);
                    cell.setCellValue(numericCellData);

                    cellData = rowData.getStatus();
                    cell = row.createCell(12);
                    cell.setCellValue(cellData);


                }
                sheetName = itr.hasNext() ? itr.next() : null;
                System.out.println("Sheet Name " + sheetName);
                if (sheetName != null && !sheetName.isEmpty()) {
                    data = subEventHashMap.get(sheetName);
                    isNext = true;
                }
            } while (isNext);

            //File file = fileChooser.showOpenDialog();
            if (file != null) {
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }

        } catch (Exception ex) {
            throw ex;
        }


        return null;

    }


}
