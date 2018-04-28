package Relation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.annolab.tt4j.TreeTaggerWrapper;

import ExtractionMethod.ExtractionMethod;
import ExtractionMethod.NaiveExtractionMethod;
import RequeterRezo.RequeterRezo;
import TextStructure.StructuredText;
import TextStructure.TextSequence;


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

			String x=extractedRelation.getX();
			extractedRelation.setX(x);
			extractedRelation.setX_end(x.length());
			String y=extractedRelation.getY();
			extractedRelation.setY(y);
			extractedRelation.setY_end(y.length());

			boolean x_find=false,y_find=false;
			for(String prefix : prefixToErase){
				if(!x_find && x.startsWith(prefix)){
					extractedRelation.setX_begin(prefix.length());
					x_find=true;
				}
				if(!y_find && y.startsWith(prefix)){
					extractedRelation.setY_begin(prefix.length());
					y_find=true;
				}
				if(y_find && x_find)
					break;
			}
		}
	}
	
}