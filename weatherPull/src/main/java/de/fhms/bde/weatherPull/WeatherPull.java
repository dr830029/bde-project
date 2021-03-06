package de.fhms.bde.weatherPull;

import java.util.Calendar;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class WeatherPull extends Configured implements Tool{
	
	
		public int run(String[] arg0) throws Exception {
			Job job = Job.getInstance();
			job.setJobName("WeatherDataPull");
			job.setJarByClass(this.getClass());
			
			Configuration conf = job.getConfiguration();
			conf.set("mapreduce.output.textoutputformat.separator", ",");
			
			Calendar now = Calendar.getInstance();
			
			String inputPath = "hdfs://quickstart.cloudera:8020/data/weather/input/places.csv";
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("hdfs://quickstart.cloudera:8020/data/weather/processing/");
			stringBuilder.append(now.get(Calendar.YEAR));
			stringBuilder.append("/");
			stringBuilder.append(now.get(Calendar.MONTH)+1);
			stringBuilder.append("/");
			stringBuilder.append(now.get(Calendar.DAY_OF_MONTH));
			stringBuilder.append("/");
			stringBuilder.append(now.get(Calendar.HOUR_OF_DAY));
			stringBuilder.append("/");
			stringBuilder.append(now.get(Calendar.MINUTE));
			String outputPath = stringBuilder.toString();

			
			job.setOutputKeyClass(Text.class);
			
			job.setOutputValueClass(Text.class);

			job.setMapperClass(MapClass.class);
			job.setReducerClass(ReduceClass.class);

			FileInputFormat.addInputPath(job, new Path(inputPath));
			FileOutputFormat.setOutputPath(job, new Path(outputPath));

			return job.waitForCompletion(true) ? 0: 1;
		}

		public static void main(String[] args) {
			WeatherPull wp = new WeatherPull();
			try {
				wp.run(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}