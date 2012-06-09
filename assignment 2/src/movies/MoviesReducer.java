package movies;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class MoviesReducer extends Reducer<Text, Text, NullWritable, Text>{
	private MultipleOutputs<NullWritable, Text> mos;
	
	public void setup(Context context) {
		mos = new MultipleOutputs<NullWritable, Text>(context);
	}
	
	public void cleanup(Context context) throws IOException, InterruptedException {
		mos.close();
	}
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		for(Text value : values) {
			mos.write(key.toString(), null, value);
		}
	}
	
}
