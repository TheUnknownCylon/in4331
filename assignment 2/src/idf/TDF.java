package idf;

import java.io.IOException;

import myhelpers.Deletedir;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import wdm.xmlhelper.XMLInput;

public class TDF {
	private static String input = "datasets/movies";
	private static String output = "results/tdf";
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		
		Deletedir.deleteDir(output);
		
		Configuration conf = new Configuration();
		conf.set("xmlinput.start", "<movie>");
		conf.set("xmlinput.end", "</movie>");
		conf.set("documentcount", documentCount()+"");
				
		Job job = new Job(conf, "IDF");
		job.setInputFormatClass(XMLInput.class);
		job.setMapperClass(TDFMapper.class);
		job.setCombinerClass(TDFCombiner.class);
		job.setReducerClass(TDFReducer.class);
		
		/* Job output */
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MapWritable.class);
		
		/* Set the input and output */
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
				
		/* Execute the job */
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}
	
	/**
	 * Returns the number of documents in the input set.
	 * Note: for this to work, all documents should be placed in a separate file.
	 * @return
	 * @throws IOException 
	 */
	private static int documentCount() throws IOException {
		Configuration conf = new Configuration();
		Path inputPath = new Path(input);
		FileSystem fs = inputPath.getFileSystem(conf);
		FileStatus[] stat = fs.listStatus(inputPath);
		return stat.length;
	}
}
