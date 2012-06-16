package movies;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.xml.sax.SAXException;

import tpe.parser.Parser;
import tpe.parser.collectors.ResultsCollectorMemory;
import tpe.parser.nodes.TPENode;
import tpe.parser.nodes.TPENodeS;

public class MoviesMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private static TPENode directorTitleDataStruct = getDirectorAndTitleDataStruct();	
	private static TPENode titleActorDataStruct    = getTitleActorDatastruct();	
	
	/**
	 * 
	 */
	public void map(LongWritable key, Text value, Context context) {
		String valuestring = value.toString();

		outputDirectorTitle(valuestring, context);
		outputTitleActor(valuestring, context);
	}
	
	/**
	 * 
	 * @param xml
	 * @param context
	 */
	private void outputDirectorTitle(String xml, Context context) {
		ResultsCollectorMemory c = new ResultsCollectorMemory();
		
		try { Parser.parseText(xml, directorTitleDataStruct, c); }
		catch (IOException e) {e.printStackTrace();} 
		catch (SAXException e) {e.printStackTrace();}

		for(HashMap<String, String> row : c.getResultsByNodeName()) {
			StringBuffer solution = new StringBuffer();
			solution.append(row.get("movie/director/first_name")+" ");
			solution.append(row.get("movie/director/last_name")+"\t");			
			solution.append(row.get("movie/title")+"\t");
			solution.append(row.get("movie/year"));
				
			try {
				context.write(new Text("directorAndTitle"), new Text(solution.toString()));
			}
			catch (IOException e) {e.printStackTrace();}
			catch (InterruptedException e) {e.printStackTrace();}
		}		
	}
		
	

	
	
	/**
	 * 
	 * @param xml
	 * @param context
	 */
	private void outputTitleActor(String xml, Context context) {
		ResultsCollectorMemory c = new ResultsCollectorMemory();
		
		try { Parser.parseText(xml, titleActorDataStruct, c); }
		catch (IOException e) {e.printStackTrace();} 
		catch (SAXException e) {e.printStackTrace();}
			for(HashMap<String, String> row : c.getResultsByNodeName()) {
			StringBuffer solution = new StringBuffer();
			solution.append(row.get("movie/title")+"\t");
			solution.append(row.get("movie/actor/first_name")+" ");
			solution.append(row.get("movie/actor/last_name")+"\t");
			solution.append(row.get("movie/actor/birth_date")+"\t");
			solution.append(row.get("movie/actor/role"));
			
			try {
				context.write(new Text("titleAndActor"), new Text(solution.toString()));
			}
			catch (IOException e) {e.printStackTrace();}
			catch (InterruptedException e) {e.printStackTrace();}
		}		
	}
	
	
	
	
	
	/**
	 * Returns an xpath result datastructure for the solution of director-and-title.
	 * @return
	 */
	private static TPENode getDirectorAndTitleDataStruct() {
		TPENode nodeRoot              = new TPENodeS("movie");
		TPENode nodeTitle             = new TPENode("title", nodeRoot);
		TPENode nodeDirector          = new TPENode("director", nodeRoot);
		TPENode nodeDirectorNameFirst = new TPENode("first_name", nodeDirector);
		TPENode nodeDirectorNameLast  = new TPENode("last_name", nodeDirector);
		TPENode nodeMovieyear         = new TPENode("year", nodeRoot);
		
		nodeTitle.resultvalue         = true;
		nodeDirectorNameFirst.resultvalue = true;
		nodeDirectorNameLast.resultvalue = true;
		nodeMovieyear.resultvalue     = true;
		
		return nodeRoot;
	}
	
	
	/**
	 * Returns an xpath result datastructure for the solution of title-and-actor.
	 * @return
	 */
	private static TPENode getTitleActorDatastruct() {
		TPENode nodeRoot           = new TPENodeS("movie");
		TPENode nodeTitle          = new TPENode("title", nodeRoot);
		TPENode nodeActor          = new TPENode("actor", nodeRoot);
		TPENode nodeActoryear      = new TPENode("birth_date", nodeActor);
		TPENode nodeActorNameFirst = new TPENode("first_name", nodeActor);
		TPENode nodeActorNameLast  = new TPENode("last_name", nodeActor);
		TPENode nodeActorRole      = new TPENode("role", nodeActor);
		
		nodeTitle.resultvalue      = true;
		nodeActoryear.resultvalue  = true;
		nodeActorNameFirst.resultvalue = true;
		nodeActorNameLast.resultvalue = true;
		nodeActorRole.resultvalue  = true;
		
		return nodeRoot;
	}
	
	
	
	
}
