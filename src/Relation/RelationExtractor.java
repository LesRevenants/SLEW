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

import RequeterRezo.RequeterRezo;
import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.WordPatriciaTrie;


public class RelationExtractor {
	
	private StructuredText structuredText;
	private RequeterRezo system_query;
	RelationPatternFactory relationPatternFactory;
	

	public RelationExtractor(StructuredText sequence,RequeterRezo system_query,RelationPatternFactory relationPatternFactory) {
		super();
		this.structuredText = sequence;
		this.system_query = system_query;
		this.relationPatternFactory = relationPatternFactory;
	}
	
	public void extract() {
		
		 LinkedHashSet<String> patterns = relationPatternFactory.readPatternAsString();
		 structuredText.getTextSequences().forEach(textSequence -> {
			
			 textSequence.getWords().forEach(word -> {
				 if(patterns.contains(word)) {
					 //System.out.println("Pattern finded : "+word);
				 }
			 });
			 
			 
		 });
		 
	}
	
	
}