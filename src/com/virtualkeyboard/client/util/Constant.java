package com.virtualkeyboard.client.util;
/**
 * 
 * @author Hassam
 *
 */
public class Constant {
	
	public static String path=System.getProperty("user.dir")+"/EventFlow/";
	//public static String path=System.getProperty("user.home")+"/EventFlow/";
	public static long RETRY=1500;
	public static String YES="Yes";
	public static String NO="No";
	public static String PARENT_PROCESS="Parent";

	public static String BOTH="both";
	public static String ROW="row";
	public static String COL="col";

	public static int RESOLUTION_WIDTH=1920;
	public static int RESOLUTION_HEIGHT=1080;
	
	public static class Events{
		public static String SINGLE_CLICK="single-click";

		public static String SCREENSHOT="screenshot";
		public static String SCROLL="scroll";
		public static String ENTER="enter";
		public static String OPEN_FILE="open-file";
		public static String INSERT_TEXT="insert-text";
		public static String MINIMIZE_ALL_TABS="minimize-all-tabs";
		public static String DOUBLE_CLICK="double-click";
		public static String RIGHT_CLICK="right-click";
		public static String VERIFICATION="Verification";

		public static String COUNT="count";
		public static String SUB_PROCESS="sub-process";

		public static String RESUME_AFTER="resume-after";

		public static String FOR_LOOP="for-loop";

	
	}
	
	
	public static class Status{
		public static String SUCCESS="Success";
		public static String FAIL="Fail";
		public static String ERROR="Error";
		public static String EXC="EXC";
		public static String INFO="Info";
		
	
	}

	public static class ObjectType{
		public static String STRING="String";
		public static String LIST="List";
		public static String OBJECT="Object";
		public static String INTEGER="Integer";



	}

	public static class ExcelHeader{
		public static String ACTIVE="Active";

		public static String DESCRIPTION="Description";
		public static String SUB_PROCESS="Sub-Process";
		public static String Mandatory="Mandatory";
		public static String WAIT_TIME="Wait-Time";
		public static String EVENT="Event*";
		public static String INSERT_TEXT ="Insert-Text";
		public static String SNO="S/No";

		public static String EVENTS_HEAD="Events-head";
		public static String EMAIL="Email";
		public static String STATUS="Status";
		public static String Image ="Image";
		public static String Reference_Image ="Reference-Image";
		public static String SEARCH_TEXT ="Search-Text";




	}


	public static class Excel_Btn_States{
		public static String BROWSE="Browse";
		public static String UPDATE="Update";
		public static String SAVE="Save";


	}

}
