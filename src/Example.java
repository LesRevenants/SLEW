//votre package


import RequeterRezo.Annotation;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo;
import util.JDM_Util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Example {
	
	
    public static void main(String[] arg) throws IOException, MalformedURLException, InterruptedException {
       
    	

    	
    	String sequence = "Le requin-baleine (Rhincodon typus) est un poisson cartilagineux, "
    			+ "seul membre du genre Rhincodon et seule espèce actuelle de la famille des Rhincodontidae."
    			+ "Le chamois (Rupicapra rupicapra) est une espèce de "
    			+ "la sous-famille des Caprinés.";
    	
    	TextReader textReader = new TextReader(sequence);
    	RequeterRezo system_query = new RequeterRezo("36h",10000);
    	system_query.sauvegarder();
    	
    	System.out.println("###########  Text ########### ");
    	System.out.println(sequence+"\n");
    	
    	ExtractionPatternFactory patternFactory = new ExtractionPatternFactory("datas/patterns.txt"); 
    	System.out.println("########### Pattern list ########### ");
    	for(ExtractionPattern pattern : patternFactory.getPattern_datas()) {
    		System.out.println("\t"+pattern.toString());
    	}
    	
    	List<WordSequence> wordSequences = new ArrayList(textReader.getAllWordSequence()) ;
    	for(WordSequence wordSequence : wordSequences) {
    		System.out.println("\n\n\n########### Word list ###########");
        	for(int i=0;i<wordSequence.getWords_set().size();i++) {
        		System.out.println("\t"+i+":"+wordSequence.getWords_set().get(i) );
        	}
        	System.out.println("\n########### Relations extracted ###########");

        	PatternExtractor patternExtractor = new PatternExtractor(wordSequence,system_query,10);
        	patternExtractor.extract(patternFactory.getPattern_datas());
    	}
    	
    	//JDM_Util.similarity(system_query, "requin-baleine","Rhincodon");
    
    }
}