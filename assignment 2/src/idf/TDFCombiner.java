package idf;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;


/**
 * Merges all KEY <--> DOCID-count pairs
 */
public class TDFCombiner extends Reducer<Text, MapWritable, Text, MapWritable> {

	private static IntWritable zero = new IntWritable(0);
	
	public void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
		
		MapWritable map = new MapWritable();
		
		for(MapWritable row : values) {
			for(Writable docid : row.keySet()) {
				if(!map.containsKey(docid)) {
					map.put(docid, zero);
				}
				
				int toadd = ((IntWritable) row.get(docid)).get();
				int count = ((IntWritable) map.get(docid)).get(); 
				
				map.put(docid, new IntWritable(count+toadd));
			}
			
		}
		context.write(key, map);
	}

}
