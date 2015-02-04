package com.mind.predictive.poc.mr.util;

public class MRConstants {

	public static final String PREDICTIVE_OUTPUT_FOLDER_PREFIX = System
			.getProperty("user.home") + "/Desktop/manu_output/Manufacturing";

	public static final String HBASE_FAMILY = "manufacturing";
	public static int SALES_FLAG = 0;
	public static int MONTHSALES_FLAG = 0;
	public static int FUEL_FLAG = 0;
	public static int DEFECTS_FLAG = 0;
	public static int monthcounter = 0;
	public static final String SALES = "sales";
	public static final String MONTHSALES = "monthsales";
	public static final String DEFECTS = "defects";
	public static final String FUEL = "fuel";
	public static String PREDICTIVE_COUNTRY = "";
	public static String PREDICTIVE_YEAR = "";
	public static String PREDICTIVE_MODEL = "";
	public static String WORD = "";

	public static String HBASE_BIG_TABLE = "manufacturingpoc";
	public static String outputPath = MRConstants.PREDICTIVE_OUTPUT_FOLDER_PREFIX;
	public static String HBASE_ROW = "";

	public static String DECISION_MAKING_FILE_PATH = "";

	public static int TOTAL_NO_OF_SALES;

	public static String BIGDATA_INPUT_PATH = "";

	public static final String javaVersion = System.getProperty("java.version");

	public static final String LESS_THAN = "lessthan";
	public static final String GREATER_THAN = "greaterthan";

	public static final String PROJECT_NAME = "Predictive Analytics by Hadoop & HBase";

	public static String HBASE_COLUMN = "";

	public static String PREDICTIVE_OUTPUT_FOLDER = null;

}
