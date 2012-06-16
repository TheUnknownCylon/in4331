package idf;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.xml.sax.SAXException;

import tpe.parser.Parser;
import tpe.parser.collectors.ResultsCollectorMemory;
import tpe.parser.nodes.TPENode;
import tpe.parser.nodes.TPENodeS;

public class TDFMapper extends Mapper<LongWritable, Text, Text, MapWritable> {
	
	private static TPENode xmlDataStruct = getXMLStruct();
	private static IntWritable one = new IntWritable(1);
	
	public void map(LongWritable key, Text xml, Context context) {
		//String text = value.toString();
		//StringTokenizer tokenizer = new StringTokenizer(text);
		
		ResultsCollectorMemory c = new ResultsCollectorMemory();
		try { Parser.parseText(xml.toString(), xmlDataStruct, c); }
		catch (IOException e) {e.printStackTrace();} 
		catch (SAXException e) {e.printStackTrace();}
		
		int docid = new Integer((int) key.get());

		//for each movie found in the document
		for(HashMap<String, String> row : c.getResultsByNodeName()) {
			String movietitle = row.get("movie/title");
			String summary = row.get("movie/summary");
			
			//normalize step 1: all to lowercase
			summary = summary.toLowerCase();
			
			//normalize step 2: remove all non alphanumerical and spaces
			summary = summary.replaceAll("[^A-Za-z0-9 ]", "");

			//TOkenize string, since we are going to loop over that keys
			StringTokenizer tokenizer = new StringTokenizer(summary);
			
			while(tokenizer.hasMoreTokens()) {
				String term = tokenizer.nextToken();
				try {
					MapWritable map = new MapWritable();
					map.put(new Text(movietitle), one);
					context.write(new Text(term), map);
				}
				catch (IOException e) {e.printStackTrace();}
				catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}
	
	
	/**
	 * Returns an xpath result datastructure for the solution of director-and-title.
	 * @return
	 */
	private static TPENode getXMLStruct() {
		TPENode nodeRoot        = new TPENodeS("movie");
		TPENode nodeTitle       = new TPENode("title", nodeRoot);
		TPENode nodeSummary     = new TPENode("summary", nodeRoot);
		
		nodeTitle.resultvalue   = true;
		nodeSummary.resultvalue = true;

		return nodeRoot;
	}
	
}
