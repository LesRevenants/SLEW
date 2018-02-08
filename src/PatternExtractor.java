import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import RequeterRezo.Annotation;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo;
import util.JDM_Util;


/**
 * 
 * @author user
 *
 */
public class PatternExtractor {
	
	private WordSequence wordSequence;
	private RequeterRezo system_query;
	
	/** */
	private int window_size;
	

	public PatternExtractor(WordSequence sequence,RequeterRezo system_query,int window_size) {
		super();
		this.wordSequence = sequence;
		this.system_query = system_query;
		this.window_size = window_size;
	}
	
	public void extract(Collection<ExtractionPattern> extractionPatterns) {
		
		
		for(ExtractionPattern extractionPattern : extractionPatterns) { // loop for each extraction pattern
			
			/*
			 *  for each pattern which describe a pattern into natural language 
			 *  ex : "est un "describe r_isa, "est une" describe r_isa 
			 */
			for(String pattern_str : extractionPattern.getPatterns()) { 
				String[] sub_words = pattern_str.split(" "); 
				int pattern_str_idx = -1;
				
				if(sub_words.length == 1) { // check if pattern is not a composed word
					pattern_str_idx = wordSequence.getData().indexOf(pattern_str);
				}
				else { 
					/* search the pattern into word_sequence list of word of size | sub_word |   
					 * ex : "est un" -> search into combinaison with size 2 into sequence
					*/
					ArrayList<String> composed_words = wordSequence.getWord_comb().get(sub_words.length-1);
					pattern_str_idx = composed_words.indexOf(pattern_str);	 // get position of pattern
				}
				if((pattern_str_idx != -1)) {
					System.out.println(extractionPattern.getRelation() + " :"+pattern_str);
					check_relation(extractionPattern.getRelation(),pattern_str, pattern_str_idx, window_size);
				}
			}
		}
	}
	
	/**
	 * // check existence of a relation of  each word with each word 
	 * @param relation_type 
	 * @param relation_idx : the idx of relation into word sequence
	 * @param window_size : serch space [relation_idx-windows_size ; relation_idx+windows_size]
	 */
	private void check_relation(String relation_type,String extractedPatternName,int relation_idx, int window_size) {
		int limit = wordSequence.getWords_set().size();
		int start_idx = ( relation_idx - window_size ) > 0 ? (relation_idx-window_size) : 0; 
		int end_idx = ( relation_idx + window_size ) < limit  ? (relation_idx+window_size) : limit;
		
		for(int i=start_idx ;i<end_idx;i++) { 
			for(int j=i+1 ;j<end_idx;j++) {
				String x_word = wordSequence.getWords_set().get(i);
				String y_word = wordSequence.getWords_set().get(j);
				if(! x_word.equals(y_word)) {
					
					Mot jdm_word;
					try {
						jdm_word = system_query.requete(x_word);
						if(jdm_word == null){
				        	return ;
				        }
			            ArrayList<Annotation> annotations = jdm_word.getAnnotations();
			            //System.out.println(x_word + ":"+relation_type+":"+y_word);
			            for(Annotation annotation : annotations){
			            	
			            	if(annotation.getType_relation().equals(relation_type)  && annotation.getMot_sortant().equals(y_word)  ){
			            		ExtractedRelation  relation = new ExtractedRelation(extractedPatternName,annotation,relation_idx);
			            		System.out.println("\t" +relation.toString());
			            		//System.out.println("\t("+word1+","+word2+") --> "+annotation.toString());
			            	}	
			            }
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
	}
}
