import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.TextSequence;
import TextStructure.WordPatriciaTrie;
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

//import org.apache.commons.lang3.RandomStringUtils;

import DataExtraction.DataExtractor;
import DataExtraction.RawTextExtractor;
import DataExtraction.wikipedia.WikipediaDataExtractor;
import Relation.RelationExtractor;
import Relation.RelationPatternReader;
import RequeterRezo.RequeterRezo;

public class Main {

	
	/*private static void testMC() {
		long tStart = System.currentTimeMillis();
        CompoundWordBuilder compoundWordBuilder = new CompoundWordBuilder();
        compoundWordBuilder.read("jdm-mc.txt",false);
        compoundWordBuilder.write("jdm-mc.ser");
        compoundWordBuilder.read("datas/jdm-mc.ser",true);
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        System.out.println("Time1 : "+tDelta + "  ms");

        //String[] sentence = {"il","existe","une","balancoire","ainsi","qu'une","radiographie","du","thorax","verte","grâce","à","une","technique","d'imagerie","médicale"};
        String[] sentence = {"radiographie","du","thorax","grâce","à","une","technique","d'imagerie","médicale","radiographie","du","thorax"};
        ArrayList<String> sentenceAsList = new ArrayList<>(Arrays.asList(sentence));
        
        tStart = System.currentTimeMillis();
        for(int j=0 ;j<1; j++){
            for(int i=sentence.length ;i<=sentence.length;i++){
                System.out.println(
                		compoundWordBuilder.replaceSequence( new TextSequence(sentenceAsList.subList(0,i)),3)
                );
            }
        }
        tEnd = System.currentTimeMillis();
        tDelta = tEnd - tStart;
        System.out.println("Time2 : "+tDelta + "  ms");
	}/
	
	private static void testWiki() {
		WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor("Marmotte");
		wikipediaDataExtractor.getTextSequences();
		/*for(TextSequence seq : wikipediaDataExtractor.getTextSequences()) {
			System.out.println(se);
		}
	}*/
	
	
	public static void run(DataExtractor dataExtractor, String patternPath, String jdmMcPath, boolean verbose) {
		long tStart = System.currentTimeMillis();
		RelationPatternReader relationPatternFactory = new RelationPatternReader(patternPath);
		System.out.println("Lecture des pattrons : "+(System.currentTimeMillis()-tStart) +"  ms");
		
		tStart = System.currentTimeMillis();
		CompoundWordBuilder compoundWordBuilder = new CompoundWordBuilder(jdmMcPath,true); 
		compoundWordBuilder.addToTrie(relationPatternFactory.getCompoundWords()); // add compound word from patterns into compound word dictionary
		System.out.println("Lecture des mots composées : "+(System.currentTimeMillis()-tStart) +"  ms");
		
		tStart = System.currentTimeMillis();
		StructuredText structuredText = new StructuredText(dataExtractor, compoundWordBuilder, relationPatternFactory.getCompoundWords());
		System.out.println("Structuration du texte : "+(System.currentTimeMillis()-tStart) +"  ms");
		
		RequeterRezo system_query = new RequeterRezo("72h",100000);
		RelationExtractor relationExtractor = new RelationExtractor(structuredText, system_query,relationPatternFactory);
		if(verbose) {
			System.out.println(structuredText.toString());
		}
		System.out.println("Relations extraites : \n");
		relationExtractor.extract();	
	    System.out.println("Temps d'extraction : "+(System.currentTimeMillis()-tStart) +"  ms");
		
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
    	WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor("Marmotte");
    	run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.ser",false);
    }
}
