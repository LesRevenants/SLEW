import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.TextSequence;
import TextStructure.WordPatriciaTrie;
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
import Relation.ExtractedRelation;
import Relation.RelationExtractor;
import Relation.RelationPatternReader;
import RequeterRezo.RequeterRezo;

public class Main {

	
	/**
	 * 
	 * @param patternPath
	 * @param jdmMcPath
	 * @param verbose
	 * @param relationPatternFactory
	 * @param compoundWordBuilder
	 */
	
	public RelationPatternReader buildPatterns(String patternPath) {
		long tStart = System.currentTimeMillis();
		System.out.println("Lecture des pattrons : ");
		RelationPatternReader relationPatternFactory = new RelationPatternReader(patternPath);
		Utils.display_ellapsed_time(tStart,"");
		return relationPatternFactory;
	}
	
	public CompoundWordBuilder buildCWB(String jdmMcPath,RelationPatternReader relationPatternReader) {
		long tStart = System.currentTimeMillis();
		System.out.println("Lecture des mots composés de JDM : ");
		CompoundWordBuilder compoundWordBuilder = new CompoundWordBuilder(jdmMcPath,true); 
		compoundWordBuilder.addToTrie(relationPatternReader.getCompoundWords()); // add compound word from patterns into compound word dictionary
		Utils.display_ellapsed_time(tStart,"");	
		return compoundWordBuilder;
	}
	
	public RequeterRezo buildRequeterRezo() {
		long tStart = System.currentTimeMillis();
		System.out.println("Initialisation du moteur requeterRezo : ");
		RequeterRezo system_query = new RequeterRezo("72h",100000);
		Utils.display_ellapsed_time(tStart,"");
		return system_query;
	}
	
	
	/**
	 * 
	 * @param dataExtractor
	 * @param patternPath
	 * @param jdmMcPath
	 * @param sources_file_path
	 * @param verbose
	 */
	public void run(DataExtractor dataExtractor, String patternPath, String jdmMcPath, String sources_file_path,boolean verbose) {
		
		
		
		RelationPatternReader relationPatternReader = buildPatterns(patternPath);
		CompoundWordBuilder compoundWordBuilder = buildCWB(jdmMcPath, relationPatternReader);
		RequeterRezo system_query=buildRequeterRezo();
		
		
		System.out.println("Lectures sources : \n");
		long tStart = System.currentTimeMillis();
		 try {
			 for(LinkedList<TextSequence> sequences : dataExtractor.extractAll(Files.readAllLines(Paths.get(sources_file_path)),3)) {
				
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
				System.out.println("Relations extraites : \n");
				for(ExtractedRelation extractedRelation : relationExtractor.extract()) {
					System.out.println("\t"+extractedRelation.toString());
				}
			 	tStart=Utils.display_ellapsed_time(tStart,"");
			
				

				
			 }	
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	private static void test() {
		
		String text = "Le requin baleine est un poisson cartilagineux, "
    			+ "seul membre du genre Rhincodon et seule espèce actuelle de la famille des Rhincodontidae."
    			+ "Le chamois est une espèce de "
    			+ "la sous-famille des Caprinés."
    			+ "Les marmottes (Marmota) forment un genre de mammifère fouisseur de l'ordre des rongeurs."
				+ "L'espèce la plus connue en Europe est la marmotte vivant dans les montagnes (Marmota marmota)."
				+ "En Amérique du Nord, la Marmota monax, appelée familièrement « siffleux » au Québec, constitue l'espèce la plus courante.";
				      
	}

    public static void main(String[] args){
    	WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor();
    	Main main=new Main();
    	main.run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.ser","datas/wiki_articles_id",true);


    }
}
