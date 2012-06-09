package idf;

import java.io.IOException;

import myhelpers.Deletedir;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import wdm.xmlhelper.XMLInput;

public class TDF {
	private static String input = "datasets/book3.14";
	private static String output = "results/tdf";
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		
		Deletedir.deleteDir(output);
		
		Configuration conf = new Configuration();
		conf.set("documentcount", "7");
				
		Job job = new Job(conf, "IDF");
		job.setMapperClass(TDFMapper.class);
		job.setCombinerClass(TDFCombiner.class);
		job.setReducerClass(TDFReducer.class);
		
		/* Job output */
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MapWritable.class);
		
		/* Set the input and output */
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
				
		/* Define multiple outputs for the reducer*/
		//MultipleOutputs.addNamedOutput(job, "titleAndActor", TextOutputFormat.class, Text.class, Text.class);
		//MultipleOutputs.addNamedOutput(job, "directorAndTitle", TextOutputFormat.class, Text.class, Text.class);
		
		/* Execute the job */
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}
}
