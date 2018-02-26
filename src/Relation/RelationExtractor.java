package Relation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import RequeterRezo.RequeterRezo;
import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;


public class RelationExtractor {
	
	private StructuredText structuredText;
	private RequeterRezo system_query;
	

	public RelationExtractor(StructuredText sequence,RequeterRezo system_query) {
		super();
		this.structuredText = sequence;
		this.system_query = system_query;
	}
	
	public void extract(RelationPatternFactory relationPatternFactory) {
		
		 LinkedList<String> relationPatternsStr = relationPatternFactory.readPatternAsString();
		 
		 structuredText.getTextSequences().forEach(textSequence -> {
			 textSequence.getWords().forEach(word -> {
				 if(relationPatternsStr.contains(word)) {
					 System.out.println("Pattern finded : "+word);
				 }
			 });
		 });
		 
	}
	
	
}