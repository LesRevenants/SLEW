package ExtractionMethod;

/*import Relation.ExtractedRelation;
import Relation.RelationPattern;
import TextStructure.TextSequence;

import java.io.*;
import java.util.*;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.*;

public class DependencyExtractionMethod extends ExtractionMethod{
	
	private StanfordCoreNLP pipeline;
	
	public DependencyExtractionMethod() {
		Properties frenchProperties = StringUtils.argsToProperties(new String[]{"-props", "StanfordCoreNLP-french.properties"});
    	StanfordCoreNLP pipeline = new StanfordCoreNLP(frenchProperties);
	}

	@Override
	public ExtractedRelation extract(TextSequence textSequence, RelationPattern pattern, int pattern_word_idx) {
		StringBuilder sb=new StringBuilder();
		textSequence.getWords().forEach(word->sb.append(word));
		String text=sb.toString();
		Annotation frenchAnnotation = new Annotation(text);
    	pipeline.annotate(frenchAnnotation);
    	List<CoreMap> sentences = frenchAnnotation.get(CoreAnnotations.SentencesAnnotation.class);
    	CoreMap sentence=sentences.get(0);
		return null;
	}
	
	

}*/
