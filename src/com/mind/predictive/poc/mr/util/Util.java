package com.mind.predictive.poc.mr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mind.predictive.poc.hbase.util.HBaseUtil;

public class Util {
	static Logger logger = Logger.getLogger(HBaseUtil.class);

	public static String dateFormat_yyyy_MM_dd_HH_mm_ss() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH-mm-ss");
		String dateFormatString = dateFormat.format(date);
		return dateFormatString;
	}

	public static void readPredictorFile(File file,
			List<String> predictiveConditonList) {

		FileReader fr = null;
		try {
			fr = new FileReader(file);

			BufferedReader br = new BufferedReader(fr);
			String s;

			while ((s = br.readLine()) != null) {
				logger.info("---predictors---" + s);
				predictiveConditonList.add(s.trim());
			}
		} catch (FileNotFoundException e) {
			logger.error("--- ERROR --- " + e.getMessage());
		} catch (IOException e) {
			logger.error("--- ERROR --- " + e.getMessage());
		} finally {
			try {
				fr.close();
			} catch (IOException e) {
				logger.error("--- ERROR --- " + e.getMessage());
			}
		}

	}

	public static String getDecisionX(String key) {

		String decision = "";
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(MRConstants.DECISION_MAKING_FILE_PATH));
			decision = p.getProperty(key);
		} catch (FileNotFoundException e) {
			logger.error("--- ERROR --- " + e.getMessage());
		} catch (IOException e) {
			logger.error("--- ERROR --- " + e.getMessage());
		}

		return decision;

	}

}
