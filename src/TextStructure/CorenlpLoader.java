package TextStructure;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.awt.dnd.InvalidDnDOperationException;
import java.util.*;

public class CorenlpLoader {

    private static CorenlpLoader instance;
    private  static StanfordCoreNLP pipeline;

    private CorenlpLoader() {
        Properties frenchProperties = StringUtils.argsToProperties("-props", "StanfordCoreNLP-french.properties");
        pipeline = new StanfordCoreNLP(frenchProperties);
    }

    public static CorenlpLoader getInstance() {
        if(instance != null) {
            return instance;
        }
        instance = new CorenlpLoader();
        return instance;
    }

    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }

    public static ArrayList<IndexedWord> getOrderedDescendants(SemanticGraph graph, IndexedWord node){
        ArrayList<IndexedWord>  descendants = new ArrayList<>(graph.descendants(node));
        Collections.sort(descendants,Comparator.comparing(IndexedWord::index));
        return descendants;
    }

    public static IndexedWord getNodeFromParentWithRelation(SemanticGraph graph, IndexedWord parent, String relationName){
        for(IndexedWord child : graph.getChildList(parent)) {
            SemanticGraphEdge edge = graph.getEdge(parent, child);
            if (edge.getRelation().getShortName().equals(relationName)) {
                return child;
            }
        }
        return null;
    }

    public static SemanticGraph getGraphFrom(TextSequence textSequence){
        String textSequenceTxt=textSequence.getWordsAsStr();
        Annotation annotation=new Annotation( textSequenceTxt);
        pipeline.annotate(annotation);
        List<CoreMap> sentences= annotation.get(CoreAnnotations.SentencesAnnotation.class);
        if(sentences != null && ! sentences.isEmpty()) {
            return sentences.get(0).get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
        }
        return null;
    }




}
