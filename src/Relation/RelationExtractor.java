package Relation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

import RequeterRezo.RequeterRezo;
import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.WordPatriciaTrie;


public class RelationExtractor {
	
	private StructuredText structuredText;
	private RequeterRezo system_query;
	private RelationPatternReader relationPatternFactory;
	private TreeTaggerWrapper tt;
	

	public RelationExtractor(StructuredText sequence,RequeterRezo system_query,RelationPatternReader relationPatternFactory) {
		super();
		this.structuredText = sequence;
		this.system_query = system_query;
		this.relationPatternFactory = relationPatternFactory;
		
		 System.setProperty("treetagger.home","lib/TreeTagger");
		 tt = new TreeTaggerWrapper<String>();
		 try {
		      tt.setModel("lib/TreeTagger/lib/french-utf8.par");
		 } catch (IOException  e) {
				e.printStackTrace();
		}
	}
	
	public void extract() {
		
		 LinkedHashSet<String> patterns = relationPatternFactory.readPatternAsString();
		 structuredText.getTextSequences().forEach(textSequence -> {
			 ArrayList<String> words = textSequence.getWords();
			 
			 /*try {
			      tt.setModel("lib/TreeTagger/lib/french-utf8.par");
			      tt.setHandler((token, pos, lemma) -> {
			    	  System.out.println(token + "\t\t" + pos );
			      });
			        
			      tt.process(words);
			      
			    } catch (IOException | TreeTaggerException e) {
					e.printStackTrace();
				}*/
			 
			 for(int i=0 ;i<words.size();i++) {
				 String word = words.get(i);
				 if(patterns.contains(word)) {
					 
					 String object = (i>0) ? words.get(i+1) : "";
					 String subject = (i<words.size()-1) ?  words.get(i-1) : "";
					 
					 //System.out.println(word + "("+subject + "," +object+")");
				 }
			 }
		
		 });
		 
	}
	
	
}