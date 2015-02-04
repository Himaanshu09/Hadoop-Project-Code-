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

public class PredictiveAnalyticsFuel {
	// public static String solution1;

	public static <T> void main(String[] args) {
		Configuration conf = HBaseConfiguration.create();
		HTable table = null;
		try {
			table = new HTable(conf, "manufacturingpoc");
			List<Get> gets = new ArrayList<Get>();
			byte[] cf1 = Bytes.toBytes("manufacturing");
			double rawpercentage = 0;
			DecimalFormat dec = new DecimalFormat("#.##");
			String[] fuelArr = new String[] { "DIESEL", "PETROL" };
			// byte[] qf2 = Bytes.toBytes("INDIA");
			// byte[] qf1 = Bytes.toBytes("US");
			// byte[] qf3 = Bytes.toBytes("BELGIUM");
			byte[] row1 = Bytes.toBytes(MRConstants.HBASE_ROW);
			Get get1 = new Get(row1);

			for (String fuel : fuelArr) {
				get1.addColumn(cf1, Bytes.toBytes(fuel));
			}

			// get1.addColumn(cf1, qf2);
			gets.add(get1);
			/*
			 * Get get2 = new Get(row1); get1.addColumn(cf1, qf2);
			 */
			// gets.add(get2);

			Result[] results = table.get(gets);
			System.out.println("First iteration...");
			List<String> prescripList2 = new ArrayList<String>();
			List<Integer> list = new ArrayList<Integer>();
			int maxValue = 0;
			String fuelCode = "";
			String tempFuelCode = "";
			StringBuilder prescriptionBuilder = new StringBuilder();
			for (Result result : results) {
				String row = Bytes.toString(result.getRow());
				System.out.print("Row: " + row + " ");
				byte[] val = null;
				for (String fuel : fuelArr) {
					if (result.containsColumn(cf1, Bytes.toBytes(fuel))) {
						val = result.getValue(cf1, Bytes.toBytes(fuel));
						tempFuelCode = Bytes.toString(Bytes.toBytes(fuel));
						System.out.println("Value: " + tempFuelCode
								+ Bytes.toString(val));
						int temp = Integer.valueOf(Bytes.toString(val));
						list.add(temp);
						if (temp > maxValue) {
							maxValue = temp;
							fuelCode = tempFuelCode;
						}
					}
				}

			}
			prescripList2.add("FUEL PREDICTION ");
			prescripList2.add("The most preferred fuel with total sales( "
					+ maxValue + ") of " + MRConstants.PREDICTIVE_MODEL
					+ " is " + fuelCode + " .");
			prescriptionBuilder.append("\t");
			// System.out.println("--- List --- " + list);

			// System.out.println("--- MAX VAL  --- " + maxValue +
			// " -- FUEL --- : "
			// + fuelCode);
			int sumValue = 0;
			for (Integer count : list) {
				sumValue = sumValue + count;
			}
			rawpercentage = (((double) maxValue / (double) sumValue) * 100.00);
			double percentage = Double.valueOf(dec.format(rawpercentage));

			prescripList2.add("\n" + "The Percentage of total sales in "
					+ fuelCode + " were " + percentage + "%");
			prescriptionBuilder.append("\n");
			prescripList2.add(" ");
			prescripList2.add(" ");
			PrescriptionGUI.prescriptionListofList.add(prescripList2);
			// solution1 = prescriptionBuilder.toString();
			// System.out.println("--- percentage --- " + percentage);
			// System.out.println("--- sumValue --- " + sumValue);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}