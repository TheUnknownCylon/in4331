package idf;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class TDFReducer extends Reducer<Text, MapWritable, Text, Text> {

	public void reduce(Text key, Iterable<MapWritable> values, Context context) {
		
		StringBuffer solution = new StringBuffer();
		
		/**
		 * idf is the number of documents with the current term
		 */
		int df = 0;
		
		for(MapWritable row : values) {
			for(Writable docid : row.keySet()) {
				df++;
				solution.append("\"");
				solution.append(((Text)docid).toString());
				solution.append("\"");
				solution.append(":");
				solution.append(row.get(docid).toString());
				solution.append(" ");
			}
		}
		
		int n = new Integer(context.getConfiguration().get("documentcount"));
		double tdf = Math.log10(n / df);
		
		try {
			context.write(key, new Text(tdf+"\t"+solution.toString()));
		}
		catch (IOException e) {e.printStackTrace();}
		catch (InterruptedException e) {e.printStackTrace();}
	}
	
}
