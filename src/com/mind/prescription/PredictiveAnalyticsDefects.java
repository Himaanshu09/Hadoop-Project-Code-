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

public class PredictiveAnalyticsDefects {
	// public static String solution;

	public static void main(String[] args) {
		List<String> prescripList1 = new ArrayList<String>();
		Configuration conf = HBaseConfiguration.create();
		HTable table = null;
		try {
			table = new HTable(conf, "manufacturingpoc");
			List<Get> gets = new ArrayList<Get>();
			byte[] cf1 = Bytes.toBytes("manufacturing");
			double rawpercentage = 0;
			String[] countryArr = new String[] { "CHINA", "BELGIUM", "UK",
					"US", "CANADA", "INDIA", "GERMANY", "ITALY", "RUSSIA",
					"POLAND" };
			// byte[] qf2 = Bytes.toBytes("INDIA");
			// byte[] qf1 = Bytes.toBytes("US");
			// byte[] qf3 = Bytes.toBytes("BELGIUM");
			byte[] row1 = Bytes.toBytes(MRConstants.HBASE_ROW);

			Get get1 = new Get(row1);
			for (String country : countryArr) {
				get1.addColumn(cf1, Bytes.toBytes(country));
			}

			// get1.addColumn(cf1, qf2);
			gets.add(get1);
			/*
			 * Get get2 = new Get(row1); get1.addColumn(cf1, qf2);
			 */
			// gets.add(get2);

			Result[] results = table.get(gets);
			// System.out.println("First iteration...");
			List<Integer> list = new ArrayList<Integer>();
			int maxValue = 0;
			DecimalFormat dec = new DecimalFormat("#.##");
			String countryCode = "";
			String tempCountryCode = "";
			// StringBuilder prescriptionBuilder = new StringBuilder();
			for (Result result : results) {
				String row = Bytes.toString(result.getRow());
				System.out.print("Row: " + row + " ");
				byte[] val = null;
				for (String country : countryArr) {
					if (result.containsColumn(cf1, Bytes.toBytes(country))) {
						val = result.getValue(cf1, Bytes.toBytes(country));
						tempCountryCode = Bytes
								.toString(Bytes.toBytes(country));
						System.out.println("Value: " + tempCountryCode
								+ Bytes.toString(val));
						int temp = Integer.valueOf(Bytes.toString(val));
						list.add(temp);
						if (temp > maxValue) {
							maxValue = temp;
							countryCode = tempCountryCode;
						}
					}
				}

			}
			// StringBuilder prescriptionBuilder = new StringBuilder();
			// System.out.println("--- List --- " + list);
			prescripList1.add("DEFECTS PREDICTION ");
			prescripList1.add("The Country with maximum defects( " + maxValue
					+ ") of " + MRConstants.PREDICTIVE_MODEL + " is "
					+ countryCode + ".");
			// prescriptionBuilder.append("\t");
			// System.out.println("--- MAX VAL  --- " + maxValue +
			// " -- COUNTRY --- : " + countryCode);
			int sumValue = 0;
			for (Integer count : list) {
				sumValue = sumValue + count;
			}
			rawpercentage = (((double) maxValue / (double) sumValue) * 100.00);
			double percentage = Double.valueOf(dec.format(rawpercentage));
			prescripList1.add("\n" + "The Percentage of total defects in "
					+ countryCode + " were "
					+ (String.format("output: %.2f", percentage)) + "%");
			// prescriptionBuilder.append("\n");
			prescripList1.add(" ");
			prescripList1.add(" ");
			PrescriptionGUI.prescriptionListofList.add(prescripList1);

			// solution = prescriptionBuilder.toString();

			// System.out.println("--- sumValue --- " + sumValue);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
