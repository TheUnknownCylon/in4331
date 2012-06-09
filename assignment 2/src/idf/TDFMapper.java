package idf;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TDFMapper extends Mapper<LongWritable, Text, Text, MapWritable> {
	
	private static IntWritable one = new IntWritable(1);
	
	public void map(LongWritable key, Text value, Context context) {
		String text = value.toString();
		StringTokenizer tokenizer = new StringTokenizer(text);
		
		int docid = new Integer(tokenizer.nextToken());
		
		while(tokenizer.hasMoreTokens()) {
			String term = tokenizer.nextToken();
			try {
				MapWritable map = new MapWritable();
				map.put(new IntWritable(docid), one);
				context.write(new Text(term), map);
			}
			catch (IOException e) {e.printStackTrace();}
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}
