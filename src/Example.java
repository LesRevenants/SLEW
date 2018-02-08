//votre package


import RequeterRezo.Annotation;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo;
import util.JDM_Util;

import java.io.IOException;
import java.net.MalformedURLException;

public class Example {
	
	
    public static void main(String[] arg) throws IOException, MalformedURLException, InterruptedException {
       
    	

 
    	String sequence = "Le requin-tigre (Galeocerdo cuvier) est un requin et un poisson"
    			+ " de la famille des Carcharhinidés "
    			+ "et l'unique représentant du genre Galeocerdo.";
    	
    	String sequence2 = "Le chamois (Rupicapra rupicapra) est une espèce de "
    			+ "mammifères de la famille des Bovidés et de la sous-famille des Caprinés. ";
    	
    	RequeterRezo system_query = new RequeterRezo("36h",10000);
    	system_query.sauvegarder();
    			
    	System.out.println("###########  Text ########### ");
    	System.out.println(sequence2 +"\n");
    	
    	ExtractionPatternFactory patternFactory = new ExtractionPatternFactory("datas/patterns.txt"); 
    	System.out.println("########### Pattern list ########### ");
    	for(ExtractionPattern pattern : patternFactory.getPattern_datas()) {
    		System.out.println("\t"+pattern.toString());
    	}
    	
    	WordSequence wordSequence = new WordSequence(sequence2,3);
    	System.out.println("\n########### Word list ###########");
    	System.out.println(wordSequence.getWords_set() +"\n");
    	
    	System.out.println("########### Relations extracted ###########");

    	PatternExtractor patternExtractor = new PatternExtractor(wordSequence,system_query,6);
    	patternExtractor.extract(patternFactory.getPattern_datas());
    	
    	/*Mot jdm_word = system_query.requete("requin-tigre");
    	for(Annotation annotation : jdm_word.getAnnotations()){
        	if(annotation.getType_relation().equals("r_isa")){
        		System.out.println(annotation.toString());
        	}
        }*/
    	JDM_Util.similarity(system_query, "chamois");
    }
}