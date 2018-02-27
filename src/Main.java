import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.TextSequence;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import DataExtraction.DataExtractor;
import DataExtraction.RawTextExtractor;
import DataExtraction.wikipedia.WikipediaDataExtractor;
import DataExtraction.wikipedia.WikipediaToText;
import Relation.RelationExtractor;
import Relation.RelationPatternFactory;
import RequeterRezo.RequeterRezo;

public class Main {

	
	private static void testMC() {
		long tStart = System.currentTimeMillis();
        CompoundWordBuilder compoundWordBuilder = new CompoundWordBuilder();
        /*compoundWordBuilder.read("jdm-mc.txt",false);
        compoundWordBuilder.write("jdm-mc.ser");*/
        compoundWordBuilder.read("datas/jdm-mc.ser",true);
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        System.out.println("Time1 : "+tDelta + "  ms");

        //String[] sentence = {"il","existe","une","balancoire","ainsi","qu'une","radiographie","du","thorax","verte","grâce","à","une","technique","d'imagerie","médicale"};
        String[] sentence = {"radiographie","du","thorax","grâce","à","une","technique","d'imagerie","médicale"};
        ArrayList<String> sentenceAsList = new ArrayList<>(Arrays.asList(sentence));

        tStart = System.currentTimeMillis();
        for(int j=0 ;j<1; j++){
            for(int i=sentence.length ;i<=sentence.length;i++){
                compoundWordBuilder.getCompoundWordFrom( new TextSequence(sentenceAsList.subList(0,i)),5);
            }
        }
        tEnd = System.currentTimeMillis();
        tDelta = tEnd - tStart;
        System.out.println("Time2 : "+tDelta + "  ms");
	}
	
	private static void testWiki() {
		WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor("Colibri");
		wikipediaDataExtractor.getTextSequences();
		/*for(TextSequence seq : wikipediaDataExtractor.getTextSequences()) {
			System.out.println(se);
		}*/
	}
	
	private static void testPatternWrite() {
		HashMap<String,String> relations = new HashMap<>();
		//relations.put("r_isa","datas/patterns/hypéromynie.txt");
		relations.put("r_holo","datas/patterns/Holonymie.txt");
		relations.put("r_cause","datas/patterns/Cause.txt");
		relations.put("r_effect","datas/patterns/Effet.txt");
		relations.put("r_isa","datas/patterns/Hypéromynie.txt");
		relations.put("r_mero","datas/patterns/Méronymie.txt");
		relations.put("r_poss","datas/patterns/Possession.txt");
		RelationPatternFactory factory = new RelationPatternFactory("datas/patterns/patterns.json");
		factory.merge(relations);
	}
	
	private static void testPatternRead() {
		long tStart = System.currentTimeMillis();
		RelationPatternFactory factory = new RelationPatternFactory("datas/patterns/patterns.json");
		factory.readPattern();
		long tEnd = System.currentTimeMillis();
		long   tDelta = tEnd - tStart;
	    System.out.println("testPatternRead() : "+tDelta + "  ms");
	}
	
	private static void test() {
		
		
		
		String text = "Le requin-baleine (Rhincodon typus) est un poisson cartilagineux, "
    			+ "seul membre du genre Rhincodon et seule espèce actuelle de la famille des Rhincodontidae."
    			+ "Le chamois (Rupicapra rupicapra) est une espèce de "
    			+ "la sous-famille des Caprinés.";
		
		
		RelationPatternFactory relationPatternFactory = new RelationPatternFactory("datas/patterns/patterns.json");
		
		CompoundWordBuilder compoundWordBuilder = new CompoundWordBuilder("datas/jdm-mc.ser"); 
		compoundWordBuilder.addToTrie(relationPatternFactory.getCompoundWords()); // add compound word from patterns into compound word dictionary
		
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<1000;i++) {
			sb.append(text);
		}
		System.out.println(text.length());
		
		DataExtractor dataExtractor = new RawTextExtractor(text);
		StructuredText structuredText = new StructuredText(dataExtractor, compoundWordBuilder);
		
		RequeterRezo system_query = new RequeterRezo("36h",10000);
		
		
		RelationExtractor relationExtractor = new RelationExtractor(structuredText, system_query,relationPatternFactory);
		
		long tStart = System.currentTimeMillis();
		relationExtractor.extract();	
		long tEnd = System.currentTimeMillis();
		long   tDelta = tEnd - tStart;
	    System.out.println("test() : "+tDelta + "  ms");
       
	}

    public static void main(String[] args){
    	//testMC();
    	//testWiki();
    	//testPatternWrite();
    	//testPatternRead();
    	test();
    }
}
