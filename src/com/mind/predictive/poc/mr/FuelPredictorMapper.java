package com.mind.predictive.poc.mr;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.mind.predictive.poc.mr.util.MRConstants;

public class FuelPredictorMapper extends
		Mapper<LongWritable, Text, Text, LongWritable> {
	private Text word = new Text();
	private LongWritable count = new LongWritable();

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// value is tab separated values: word, year, occurrences, #books,
		// #pages
		// we project out (word, occurrences) so we can sum over all years
		// MRConstants.PREDICTIVE_MODEL

		String[] split = value.toString().split("\t+" + "\t+");

		if (split[0].equals(MRConstants.PREDICTIVE_MODEL)) {

			word.set(split[0] + "\t" + "\t" + split[4] + "\t");

			if (split.length > 2) {
				try {
					count.set(Long.parseLong(split[5]));
					context.write(word, count);
				} catch (NumberFormatException e) {
					// cannot parse - ignore
				}
			}
		}
	}
}