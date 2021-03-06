package com.mind.predictive.poc.predictors;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import com.mind.predictive.poc.hbase.util.HBaseUtil;
import com.mind.predictive.poc.mr.MonthPredictorMapper;
import com.mind.predictive.poc.mr.util.MRConstants;
import com.mind.predictive.poc.mr.util.Util;
import com.mind.prescription.PredictiveAnalyticsMonth;

public class AggregateMonth extends Configured implements Tool {
	static Logger logger = Logger.getLogger(AggregateMonth.class);
	public static String outputPath = null;

	@Override
	public int run(String[] args) throws Exception {
		logger.info("-- manufacturing prediction started...");
		Job job = new Job(getConf());
		job.setJarByClass(getClass());
		job.setJobName(getClass().getSimpleName());

		FileInputFormat.addInputPath(job, new Path(
				MRConstants.BIGDATA_INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(
				MRConstants.PREDICTIVE_OUTPUT_FOLDER));

		job.setMapperClass(MonthPredictorMapper.class);
		job.setCombinerClass(LongSumReducer.class);
		job.setReducerClass(LongSumReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		MRConstants.PREDICTIVE_OUTPUT_FOLDER = MRConstants.PREDICTIVE_OUTPUT_FOLDER_PREFIX
				+ Util.dateFormat_yyyy_MM_dd_HH_mm_ss() + "_Month";
		ToolRunner.run(new AggregateMonth(), args);
		MRConstants.HBASE_ROW = MRConstants.PREDICTIVE_MODEL
				+ MRConstants.PREDICTIVE_COUNTRY + MRConstants.PREDICTIVE_YEAR
				+ "_Month";

		String[] hbaseArgs = new String[] {
				MRConstants.PREDICTIVE_OUTPUT_FOLDER,
				MRConstants.HBASE_BIG_TABLE };
		try {
			HBaseUtil.run(hbaseArgs);
			PredictiveAnalyticsMonth.main(null);
		} catch (Exception e) {

			logger.error("--- ERROR --- " + e.getMessage());
		}

		logger.info("-- Month prediction completed...");
		return;
	}
}
