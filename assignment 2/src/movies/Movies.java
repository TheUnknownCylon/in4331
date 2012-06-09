package movies;

import java.io.IOException;

import myhelpers.Deletedir;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import wdm.xmlhelper.XMLInput;


public class Movies {

	private static String input = "datasets/movies_onefile";
	private static String output = "results/movies";
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		
		Deletedir.deleteDir(output);
		
		Configuration conf = new Configuration();
		conf.set("xmlinput.start", "<movie>");
		conf.set("xmlinput.end", "</movie>");
				
		Job job = new Job(conf, "Movies analyzer");
		job.setInputFormatClass(XMLInput.class);
		job.setMapperClass(MoviesMapper.class);
		job.setReducerClass(MoviesReducer.class);
		
		/* Job output */
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		/* Set the input and output */
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
				
		/* Define multiple outputs for the reducer*/
		MultipleOutputs.addNamedOutput(job, "titleAndActor", TextOutputFormat.class, Text.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "directorAndTitle", TextOutputFormat.class, Text.class, Text.class);
		
		/* Execute the job */
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}
}
