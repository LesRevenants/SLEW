package util;

import java.io.IOException;
import java.util.ArrayList;

import RequeterRezo.Annotation;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo;


public class JDM_Util {
	
	public static int similarity(RequeterRezo system_query, String word1,String word2,String relation_type){
		assert(system_query != null);
		try {
			Mot jdm_word = system_query.requete(word1);
	        if(jdm_word == null){
	        	return 0;
	        }
            ArrayList<Annotation> annotations = jdm_word.getAnnotations();
            for(Annotation annotation : annotations){
            	if(annotation.getType_relation().equals(relation_type)  && annotation.getMot_sortant().equals(word2)  ){
            		System.out.println("\t("+word1+","+word2+") --> "+annotation.toString());
            	}	
            }
		        
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int similarity(RequeterRezo system_query, String word1){
		assert(system_query != null);
		try {
			Mot jdm_word = system_query.requete(word1);
	        if(jdm_word == null){
	        	return 0;
	        }
            ArrayList<Annotation> annotations = jdm_word.getAnnotations();
            for(Annotation annotation : annotations){
            	System.out.println(annotation.toString());
            }	
		        
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int similarity(RequeterRezo system_query, String word1,String word2){
		assert(system_query != null);
		try {
			Mot jdm_word = system_query.requete(word1);
	        if(jdm_word == null){
	        	return 0;
	        }
            ArrayList<Annotation> annotations = jdm_word.getAnnotations();
            for(Annotation annotation : annotations){
            	if( annotation.getMot_sortant().equals(word2)  ){
            		System.out.println("("+word1+","+word2+") :"+annotation.toString());
            	}	
            }
		        
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
