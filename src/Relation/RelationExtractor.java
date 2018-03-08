package Relation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import TextStructure.TextSequence;
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
		
		 HashMap<String,RelationPattern> pattern_map = new HashMap<>();
		 LinkedList<RelationPattern> patterns = relationPatternFactory.readPattern();
		 HashSet<String> relationPatternsStr=new HashSet<>();
		 
		 patterns.forEach(relationPattern -> {
			 relationPattern.getLinguisticPatterns().forEach(
					 linguisticPattern -> { 
						 relationPatternsStr.add(linguisticPattern.getPattern()); 
						 pattern_map.put(linguisticPattern.getPattern(), relationPattern);
					 }
			);
	 	});
		 
		 int windows_size=3;
			 
		 structuredText.getTextSequences().forEach(textSequence -> {
			//LinkedHashSet<String> wordSet = textSequence.getWordsSet();
			 ArrayList<String> words = textSequence.getWords();
			 
			 for(int i=0 ;i<words.size();i++) {
				 String word = words.get(i);
				 if(relationPatternsStr.contains(word)) {
					 //System.out.println(pattern_map.get(word).shortToString());
					 extractFrom(textSequence, pattern_map.get(word), i);	
				 }
			 }
		
		 });
		 
	}
	
	public ExtractedRelation extractFrom(TextSequence textSequence, RelationPattern pattern, int pattern_word_idx) {
		int subject_idx=pattern_word_idx-1;
		int object_idx=pattern_word_idx+1;
		int window = 10;
		
		String objectGramPosition = textSequence.getWordsPositions().get(object_idx);
		String subjectGramPosition = textSequence.getWordsPositions().get(subject_idx);
		
		int cpt = 0; 
		boolean found = false;
		//sujet
		while(cpt < window && !found){
			if(subject_idx <0 || object_idx >= textSequence.getWords().size()) {
				return null;
			}
			if(pattern.getSyntaxicContraint().getyConstraints().contains(subjectGramPosition)) found = true;
			else {
				subject_idx--;
				subjectGramPosition = textSequence.getWordsPositions().get(subject_idx);
			}
			cpt++;
		}
		if(!found) return null;
		else {
			found = false; cpt = 0;
			while(cpt < window && !found){
				if(subject_idx <0 || object_idx >= textSequence.getWords().size()) {
					return null;
				}
				if(pattern.getSyntaxicContraint().getxConstraints().contains(objectGramPosition)) found = true;
				else {
					object_idx++;
					objectGramPosition = textSequence.getWordsPositions().get(object_idx);
				}
				cpt++;
			}
		}
		if(!found) return null;
		System.out.println("\t"+pattern.getRelationType() + "("
				+textSequence.getWords().get(subject_idx) + " , "
				+textSequence.getWords().get(pattern_word_idx) + " , "
				+textSequence.getWords().get(object_idx)+")");
			/*if(pattern.getSyntaxicContraint().getxConstraints().contains(objectGramPosition)) {
				if(pattern.getSyntaxicContraint().getyConstraints().contains(subjectGramPosition)) {
					
				}
			}*/

		return null;
	}
	

	
	
	
}