package com.virtualkeyboard.client.util;

import javafx.scene.control.ComboBox;

public  class FormValue {

    /**
     * populate event combo
     * @param eventValuesCombo
     * @return
     */
    public static ComboBox populateEventValues(ComboBox eventValuesCombo){

    eventValuesCombo.getItems().addAll(Constant.Events.SINGLE_CLICK

            ,Constant.Events.SCROLL
            ,Constant.Events.ENTER
            ,Constant.Events.INSERT_TEXT
            ,Constant.Events.MINIMIZE_ALL_TABS
            ,Constant.Events.DOUBLE_CLICK
            ,Constant.Events.RIGHT_CLICK
            ,Constant.Events.FOR_LOOP
            ,Constant.Events.OPEN_FILE
            ,Constant.Events.RESUME_AFTER
            ,Constant.Events.SCREENSHOT
            ,Constant.Events.COUNT
            ,Constant.Events.VERIFICATION
            ,Constant.Events.SUB_PROCESS

    );
    return eventValuesCombo;
}

    public static ComboBox populateObjectTypeValues(ComboBox objectCombo){

        objectCombo.getItems().addAll(

                 Constant.ObjectType.OBJECT
                ,Constant.ObjectType.LIST
                ,Constant.ObjectType.INTEGER
                ,Constant.ObjectType.STRING

        );
        return objectCombo;
    }


    public static ComboBox populateControlFlow(ComboBox objectCombo){

        objectCombo.getItems().addAll(

                "if failed skipped"
                ,"while("


        );
        return objectCombo;
    }

    public static ComboBox populateReferenceAlign(ComboBox objectCombo){

        objectCombo.getItems().addAll(

              Constant.BOTH
                ,Constant.ROW
                ,Constant.COL


        );
        return objectCombo;
    }


}


