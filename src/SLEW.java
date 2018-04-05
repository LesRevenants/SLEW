import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.TextSequence;
import TextStructure.WordPatriciaTrie;
import Util.Pair;
import Util.Utils;
import lib.org.ardverk.collection.PatriciaTrie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

//import org.apache.commons.lang3.RandomStringUtils;

import DataExtraction.DataExtractor;
import DataExtraction.RawTextExtractor;
import DataExtraction.wikipedia.WikipediaDataExtractor;
import JDM.ExistingRelations;
import Relation.ExtractedRelation;
import Relation.RelationDB;
import Relation.RelationExtractor;
import Relation.RelationPatternReader;
import Relation.WikiArticleDB;
import RequeterRezo.RequeterRezo;

public class SLEW {
	
	public SLEW(){
		
	}
	
	/**
	 * 
	 * @param dataExtractor
	 * @param patternPath
	 * @param jdmMcPath
	 * @param sources_file_path
	 * @param verbose
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public void run(DataExtractor dataExtractor, String patternPath, String jdmMcPath, String sources_file_path,boolean verbose, boolean isFile) {
		
		RelationPatternReader relationPatternReader = buildPatterns(patternPath);
		CompoundWordBuilder compoundWordBuilder = buildCWB(jdmMcPath, relationPatternReader);
		RequeterRezo system_query=buildRequeterRezo();
		RelationDB relationDB=new RelationDB("jdbc:mysql://venus/rcolin", "rcolin", "mysqlpwd");
		WikiArticleDB wikiArticleDB=new WikiArticleDB("jdbc:mysql://venus/rcolin", "rcolin", "mysqlpwd");
		
		System.out.println("Lectures sources : \n");
		long tStart = System.currentTimeMillis();
		
		try {	 
			Collection<String> articlesName = null;
			if(isFile){
				articlesName=Files.readAllLines(Paths.get(sources_file_path));
			}
			else{
				articlesName=new LinkedList<>();
				articlesName.add(sources_file_path);
			}
			
			 for(Pair<String,LinkedList<TextSequence>> data_src : dataExtractor.extractAll(articlesName,3)) {
				
				String data_key=data_src.getLeft();
				LinkedList<TextSequence> sequences=data_src.getRight();
				System.out.println("Structuration du texte : ");
					StructuredText structuredText=new StructuredText(
						sequences, 
						compoundWordBuilder, 
						relationPatternReader.getCompoundWords());
				
				tStart=Utils.display_ellapsed_time(tStart,"\t");;
				
				System.out.println("Extraction des relations : \n");
				RelationExtractor relationExtractor = new RelationExtractor(
						    structuredText,
							system_query,
							relationPatternReader
							);
				if(verbose) {
					System.out.println(structuredText.toString());
				}
				ExistingRelations existingRelations=new ExistingRelations();
				System.out.println("Relations extraites : \n");
				for(ExtractedRelation extractedRelation : relationExtractor.extract()) {
					if(! existingRelations.Requesting(extractedRelation)){
						System.out.println("\t"+extractedRelation.toString());
					}
					else{
						System.out.println("Existing relation : "+existingRelations);
					}
				}
				tStart=Utils.display_ellapsed_time(tStart,"");
			
			 }
				 
				 
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	

	
	/**
	 * 
	 * @param patternPath
	 * @param jdmMcPath
	 * @param verbose
	 * @param relationPatternFactory
	 * @param compoundWordBuilder
	 */
	
	private RelationPatternReader buildPatterns(String patternPath) {
		long tStart = System.currentTimeMillis();
		System.out.println("Lecture des patrons : ");
		RelationPatternReader relationPatternFactory = new RelationPatternReader(patternPath);
		Utils.display_ellapsed_time(tStart,"");
		return relationPatternFactory;
	}
	
	private CompoundWordBuilder buildCWB(String jdmMcPath,RelationPatternReader relationPatternReader) {
		long tStart = System.currentTimeMillis();
		System.out.println("Lecture des mots composés de JDM : ");
		CompoundWordBuilder compoundWordBuilder = new CompoundWordBuilder(jdmMcPath,true); 
		compoundWordBuilder.addToTrie(relationPatternReader.getCompoundWords()); // add compound word from patterns into compound word dictionary
		Utils.display_ellapsed_time(tStart,"");	
		return compoundWordBuilder;
	}
	
	private RequeterRezo buildRequeterRezo() {
		long tStart = System.currentTimeMillis();
		System.out.println("Initialisation du moteur requeterRezo : ");
		RequeterRezo system_query = new RequeterRezo("72h",100000);
		Utils.display_ellapsed_time(tStart,"");
		return system_query;
	}
	
	private void exportInJSONFile(ArrayList<ExtractedRelation> rex) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream("json.txt"), "utf-8"))) {
			writer.write("[");
			for(ExtractedRelation relex : rex) {
	   writer.write("\n\t{ "
			   			+ "\n\t\t x : " + relex.getObject() + ","
			   			+ "\n\t\t y : " + relex.getSubject() + ","
			   			+ "\n\t\t relation_type : " + relex.getRelation_type() + ","
	   					+ "\n\t\t predicate : " + relex.getLinguisticPattern() + "\n\t},");
			}
			writer.write("]");	
	}
		
	}

}
