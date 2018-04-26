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

import ExtractionMethod.ExtractionMethod;
import ExtractionMethod.NaiveExtractionMethod;
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
	
	public Collection<ExtractedRelation>extract() {
		
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
		 Collection<ExtractedRelation> extractedRelations=new LinkedList<>();
		 structuredText.getTextSequences().forEach(textSequence -> {
			//LinkedHashSet<String> wordSet = textSequence.getWordsSet();
			 ArrayList<String> words = textSequence.getWords();
			 
			 for(Integer patternIdx : textSequence.getPatternsIdx()) {
				 String word = words.get(patternIdx);
				 ExtractedRelation extractedRelation=extractFrom(textSequence, pattern_map.get(word), patternIdx);
				 if(extractedRelation != null) {
					 extractedRelations.add(extractedRelation);
				 }
			 }
		
		 });
		 cleanExtractedRelation(extractedRelations);
		 
		 return extractedRelations;
		 
	}
	
	public ExtractedRelation extractFrom(TextSequence textSequence, RelationPattern pattern, int pattern_word_idx) {
		ExtractionMethod extractionMethod=new NaiveExtractionMethod();
		return extractionMethod.extract(textSequence, pattern, pattern_word_idx);
	}

	public void cleanExtractedRelation(Collection<ExtractedRelation> extractedRelations){
		String[] prefixToErase={"L'","l'","L’"};
		for(ExtractedRelation extractedRelation : extractedRelations){
			for(String prefix : prefixToErase){
				String obj=extractedRelation.getObject();
				String subj=extractedRelation.getSubject();
				if(obj.startsWith(prefix)){
					extractedRelation.setObject(obj.substring(prefix.length(),obj.length()));
				}
				if(subj.startsWith(prefix)){
					extractedRelation.setSubject(subj.substring(prefix.length(),subj.length()));
				}
			}
		}
		//return extractedRelations;
	}
	
	
	
}