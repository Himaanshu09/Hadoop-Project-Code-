package com.mind.prescription;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.mind.predictive.poc.main.PrescriptionGUI;
import com.mind.predictive.poc.mr.util.MRConstants;

public class PredictiveAnalyticsMonth {

	public static <T> void main(String[] args) {

		Configuration conf = HBaseConfiguration.create();
		HTable table = null;
		try {
			table = new HTable(conf, "manufacturingpoc");
			List<Get> gets = new ArrayList<Get>();
			byte[] cf1 = Bytes.toBytes("manufacturing");
			double rawpercentage = 0;
			DecimalFormat dec = new DecimalFormat("#.##");
			String[] monthsArr = new String[] { "JAN", "FEB", "MARCH", "MAY",
					"APRIL", "JUNE", "JULY", "AUG", "SEPT", "OCT", "NOV", "DEC" };
			// byte[] qf2 = Bytes.toBytes("INDIA");
			// byte[] qf1 = Bytes.toBytes("US");
			// byte[] qf3 = Bytes.toBytes("BELGIUM");
			byte[] row1 = Bytes.toBytes(MRConstants.HBASE_ROW);

			Get get1 = new Get(row1);

			for (String months : monthsArr) {
				get1.addColumn(cf1, Bytes.toBytes(months));
			}

			// get1.addColumn(cf1, qf2);
			gets.add(get1);
			/*
			 * Get get2 = new Get(row1); get1.addColumn(cf1, qf2);
			 */
			// gets.add(get2);

			Result[] results = table.get(gets);
			System.out.println("First iteration...");
			List<String> prescripList4 = new ArrayList<String>();
			List<Integer> list = new ArrayList<Integer>();
			int maxValue = 0;
			String monthsCode = "";
			String tempmonthsCode = "";
			StringBuilder prescriptionBuilder = new StringBuilder();
			for (Result result : results) {
				String row = Bytes.toString(result.getRow());
				System.out.print("Row: " + row + " ");
				byte[] val = null;
				for (String months : monthsArr) {
					if (result.containsColumn(cf1, Bytes.toBytes(months))) {
						val = result.getValue(cf1, Bytes.toBytes(months));
						tempmonthsCode = Bytes.toString(Bytes.toBytes(months));
						System.out.println("Value: " + tempmonthsCode + "	"
								+ Bytes.toString(val));
						int temp = Integer.valueOf(Bytes.toString(val));
						list.add(temp);
						if (temp > maxValue) {
							maxValue = temp;
							monthsCode = tempmonthsCode;
						}
					}
				}

			}
			prescripList4.add("MONTH PREDICTION ");
			prescripList4.add("The Month with maximum Sales( " + maxValue
					+ ") of " + MRConstants.PREDICTIVE_MODEL + " is "
					+ monthsCode + ".");

			int sumValue = 0;
			for (Integer count : list) {
				sumValue = sumValue + count;
			}
			rawpercentage = (((double) maxValue / (double) sumValue) * 100.00);
			double percentage = Double.valueOf(dec.format(rawpercentage));
			prescripList4.add("\n" + "The Percentage of total Sales in  "
					+ monthsCode + " were " + percentage + "%");
			PrescriptionGUI.prescriptionListofList.add(prescripList4);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
