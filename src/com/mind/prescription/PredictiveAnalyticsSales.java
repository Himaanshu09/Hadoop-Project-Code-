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

public class PredictiveAnalyticsSales {

	public static <T> void main(String[] args) {
		List<String> prescripList3 = new ArrayList<String>();
		Configuration conf = HBaseConfiguration.create();
		HTable table = null;
		try {

			table = new HTable(conf, "manufacturingpoc");
			List<Get> gets = new ArrayList<Get>();
			byte[] cf1 = Bytes.toBytes("manufacturing");
			double rawpercentage = 0;
			String[] yearsArr = new String[] { "2003", "2004", "2005", "2006",
					"2007", "2008", "2009", "2010", "2011", "2012", "2013" };
			// byte[] qf2 = Bytes.toBytes("INDIA");
			// byte[] qf1 = Bytes.toBytes("US");
			// byte[] qf3 = Bytes.toBytes("BELGIUM");
			byte[] row1 = Bytes.toBytes(MRConstants.HBASE_ROW);

			Get get1 = new Get(row1);

			for (String years : yearsArr) {
				get1.addColumn(cf1, Bytes.toBytes(years));
			}

			// get1.addColumn(cf1, qf2);
			gets.add(get1);
			/*
			 * Get get2 = new Get(row1); get1.addColumn(cf1, qf2);
			 */
			// gets.add(get2);
			DecimalFormat dec = new DecimalFormat("#.##");
			Result[] results = table.get(gets);
			System.out.println("First iteration...");
			List<Integer> list = new ArrayList<Integer>();
			int maxValue = 0;
			String yearsCode = "";
			String tempyearsCode = "";
			StringBuilder prescriptionBuilder = new StringBuilder();
			for (Result result : results) {
				String row = Bytes.toString(result.getRow());
				System.out.print("Row: " + row + " ");
				byte[] val = null;
				for (String years : yearsArr) {
					if (result.containsColumn(cf1, Bytes.toBytes(years))) {
						val = result.getValue(cf1, Bytes.toBytes(years));
						tempyearsCode = Bytes.toString(Bytes.toBytes(years));
						System.out.println("Value: " + tempyearsCode + "	"
								+ Bytes.toString(val));
						int temp = Integer.valueOf(Bytes.toString(val));
						list.add(temp);
						if (temp > maxValue) {
							maxValue = temp;
							yearsCode = tempyearsCode;
						}
					}
				}

			}
			prescripList3.add("SALES PREDICTION ");
			prescripList3.add("The Year with maximum Sales( " + maxValue
					+ ") of " + MRConstants.PREDICTIVE_MODEL + " is "
					+ yearsCode + ".");

			int sumValue = 0;
			for (Integer count : list) {
				sumValue = sumValue + count;
			}
			rawpercentage = (((double) maxValue / (double) sumValue) * 100.00);
			double percentage = Double.valueOf(dec.format(rawpercentage));

			prescripList3.add("\n" + "The Percentage of total Sales in "
					+ yearsCode + " were " + percentage + "%");
			prescripList3.add(" ");
			prescripList3.add(" ");
			PrescriptionGUI.prescriptionListofList.add(prescripList3);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
