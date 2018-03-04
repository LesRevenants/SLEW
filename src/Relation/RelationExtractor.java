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
		 
		 int windows_size=5;
			 
		 structuredText.getTextSequences().forEach(textSequence -> {
			//LinkedHashSet<String> wordSet = textSequence.getWordsSet();
			 ArrayList<String> words = textSequence.getWords();
			 
			 for(int i=0 ;i<words.size();i++) {
				 String word = words.get(i);
				 if(relationPatternsStr.contains(word)) {
					 //System.out.println(pattern_map.get(word).shortToString());
					 int start_idx=(i-windows_size) > 0 ? (i-windows_size) : 0; 
					 int end_idx=(i+1+windows_size) <words.size() ? (i+1+windows_size) : words.size()-1; 
					 
					 for(int j=start_idx;j<i;j++) {
						 for(int k=i+1;k<end_idx;k++) {
							 String object  = words.get(j);
							 String subject = words.get(k);
							 checkPattern(textSequence, object, j, pattern_map.get(word), subject, k);
							 //System.out.println("\t"+word + "("+object + "," +subject+")");
						 }
						
					 }
					 
					
				 }
			 }
		
		 });
		 
	}
	
	public boolean checkPattern(TextSequence textSequence,String object, int object_idx, RelationPattern pattern,String subject,int subject_idx) {
		// if jdm exist
		
		String objectGramPosition = textSequence.getWordsPositions().get(object_idx);
		String subjectGramPosition = textSequence.getWordsPositions().get(subject_idx);
		//System.out.println("\t"+pattern.relationType + "("+object + "," +subject+")("+objectGramPosition+","+subjectGramPosition+")");
		
		//if(pattern.)
		if(pattern.getSyntaxicContraint().getxConstraints().contains(objectGramPosition)) {
			if(pattern.getSyntaxicContraint().getyConstraints().contains(subjectGramPosition)) {
				System.out.println("\t"+pattern.getRelationType() + "("+object + "," +subject+")");
			}
		}
		
		/*SyntaxicContraint syntaxicContraint = pattern.getSyntaxicContraint();
		syntaxicContraint.getxConstraints().forEach(action);*/
		return true;
		
	}
	
	
	
	
}