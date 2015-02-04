package com.mind.predictive.poc.hbase.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.log4j.Logger;

import com.mind.predictive.poc.mr.util.MRConstants;

public class HBaseUtil {
	static Logger logger = Logger.getLogger(HBaseUtil.class);
	private static final String NAME = "HBaseUtil";
	public static HTable htable;

	static class Uploader extends
			Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

		private long checkpoint = 100;
		private long count = 0;

		@Override
		public void map(LongWritable key, Text line, Context context)
				throws IOException {

			String[] lineArray = line.toString().split("\t\t");

			String[] values = new String[5];
			values[0] = MRConstants.HBASE_ROW;
			values[1] = MRConstants.HBASE_FAMILY;
			values[2] = "";
			values[3] = line.toString();
			values[4] = MRConstants.HBASE_COLUMN;
			// Extract each value
			byte[] row = Bytes.toBytes(values[0]);

			// Create Put
			Put put = new Put(row);

			byte[] family = Bytes.toBytes(values[1]);
			byte[] qualifier = Bytes
					.toBytes(String.valueOf(lineArray[1].trim()));
			byte[] value = Bytes.toBytes(lineArray[2].trim());

			put.add(family, qualifier, value);
			// put.add(family, qualifier, 1L, value);
			try {
				context.write(new ImmutableBytesWritable(row), put);
			} catch (InterruptedException e) {
				logger.error("--- ERROR --- " + e.getMessage());
			}

			// Set status every checkpoint lines
			if (++count % checkpoint == 0) {
				context.setStatus("Emitting Put " + count);
			}
		}
	}

	/**
	 * Job configuration.
	 */
	public static Job configureJob(Configuration conf, String[] args)
			throws IOException {
		Path inputPath = new Path(args[0]);
		String tableName = args[1];
		Job job = new Job(conf, NAME + "_" + tableName);
		job.setJarByClass(Uploader.class);
		FileInputFormat.setInputPaths(job, inputPath);
		// job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(Uploader.class);
		// No reducers. Just write straight to table. Call initTableReducerJob
		// because it sets up the TableOutputFormat.
		TableMapReduceUtil.initTableReducerJob(tableName, null, job);
		job.setNumReduceTasks(0);
		return job;
	}

	/**
	 * Main entry point.
	 * 
	 * @param args
	 *            The command line parameters.
	 * @throws Exception
	 *             When running the job fails.
	 */
	public static void run(String[] args) throws Exception {
		Configuration conf = HBaseConfiguration.create();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err
					.println("Wrong number of arguments: " + otherArgs.length);
			System.err.println("Usage: " + NAME + " <input> <tablename>");
			System.exit(-1);
		}
		Job job = configureJob(conf, otherArgs);
		job.waitForCompletion(true);
		// System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	public static int getHbaseRowCount(String rowName) {
		int noOfRows = 0;
		Configuration conf = HBaseConfiguration.create();

		byte[] rowkey = Bytes.toBytes(rowName);

		try {
			htable = new HTable(conf, MRConstants.HBASE_BIG_TABLE);
			// Check if the record exists
			Get get = new Get(rowkey);
			get.addFamily(Bytes.toBytes(MRConstants.HBASE_FAMILY));
			Result result = htable.get(get);

			logger.info("--- result.size() --- " + result.size());
			noOfRows = result.size();
		} catch (IOException e) {
			logger.error("---ERROR ---:" + e.getMessage());

		}

		return noOfRows;

	}

	public static Map<String, String> getRowsMap(String row) throws IOException {
		Map<String, String> rowMap = new HashMap<String, String>();
		try {
			byte[] rowkey = Bytes.toBytes(row);
			Get get = new Get(rowkey);
			get.addFamily(Bytes.toBytes(MRConstants.HBASE_FAMILY));
			Result result = htable.get(get);
			NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = result
					.getMap();

			for (Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> columnFamilyEntry : map
					.entrySet()) {
				NavigableMap<byte[], NavigableMap<Long, byte[]>> columnMap = columnFamilyEntry
						.getValue();
				for (Entry<byte[], NavigableMap<Long, byte[]>> columnEntry : columnMap
						.entrySet()) {
					NavigableMap<Long, byte[]> cellMap = columnEntry.getValue();
					for (Entry<Long, byte[]> cellEntry : cellMap.entrySet()) {
						String key = Bytes.toString(columnEntry.getKey());
						String value = Bytes.toString(cellEntry.getValue());
						String[] stringArr = value.split("	");
						if (stringArr[4].equalsIgnoreCase("N")) {
							rowMap.put(key, value);
						}

					}
				}

			}

		} finally {
			// htable.unlockRow(rowLock);
		}
		return rowMap;
	}

	public static void main(String[] args) {
		// In the next line change it according to your requirement
		// getHbaseRowCount("gender_MALE");

	}
}
