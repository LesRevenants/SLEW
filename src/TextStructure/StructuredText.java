package TextStructure;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import DataExtraction.DataExtractor;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;

/**
 * 
 * @author user
 *
 */
public class StructuredText {

	private HashSet<String> patterns;
    private ArrayList<TextSequence> textSequences;
    private TextSequenceAnalyser textSequenceAnalyser;
    private CorenlpLoader corenlpLoader;
    private CompoundWordBuilder compoundWordBuilder;
    private long total_size;

    public long getTotal_size() {
        return total_size;
    }

    public StructuredText(Collection<TextSequence>  inTextSequences, CompoundWordBuilder compoundWordBuilder, LinkedList<String> knownPatterns, Properties properties) {
		super();
		this.textSequences = new ArrayList<>();
		textSequenceAnalyser = TextSequenceAnalyser.getInstance();
		this.compoundWordBuilder=compoundWordBuilder;

		ArrayList<ArrayList<String>> compoundWordRules=new ArrayList<>();
		/*try{
            Files.readAllLines(Paths.get("datas/mc_rules.txt")).forEach(
                    line -> compoundWordRules.add(new ArrayList<String>(Arrays.asList(line.split(";")))));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
		//compoundWordBuilder.loadRules("datas/mc_rules.txt");

        boolean use_corenlp=properties.containsKey("use_corenlp") && properties.getProperty("use_corenlp").equals("true");


		patterns = new HashSet<>(knownPatterns);

		inTextSequences.forEach(textSequence -> {
		        ArrayList<String> positions = textSequenceAnalyser.getPositionsOf(textSequence); //get positions fromm TT
                compoundWordBuilder.addToTrie(compoundWordBuilder.getNewCompoundWordFrom(textSequence.getWords(),positions,false));
                TextSequence newTextSequence=compoundWordBuilder.replaceSequence(textSequence, 8);
                newTextSequence.setWordsGramPositions(positions, patterns);
                newTextSequence.update_compoundWordPos();
                this.textSequences.add(newTextSequence);
                total_size +=newTextSequence.getSize();
		});

        if(use_corenlp){
            corenlpLoader=CorenlpLoader.getInstance();
            resolveCoreference(corenlpLoader.getPipeline());
        }

	}


	public ArrayList<TextSequence> getTextSequences() {
		return textSequences;
	}


    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	textSequences.forEach(textSequence -> {
    	    sb.append(textSequence.shortToString());
            sb.append("\n");
        });
    	return sb.toString();
    }

    public void resolveCoreference(StanfordCoreNLP coreNLP){

        ArrayList<String> refs=new ArrayList<>(textSequences.size());
        for(int i=0;i<textSequences.size();i++){
            refs.add(null);
        }

        for (int i = 0; i <textSequences.size() ; i++) {
            ArrayList<Integer> refsIdx=new ArrayList<>();

            TextSequence textSequence=textSequences.get(i);

            for (int j = 0; j <textSequence.getWords().size(); j++) { // search all pronouns
                String word=textSequence.getWords().get(j);
                if(word.toUpperCase().equals("IL")){
                    refsIdx.add(j);
                }
            }

            if( ! refsIdx.isEmpty()) { // pronouns was found
                String ref=null;

                ArrayList<IndexedWord> descendants=null;
                ArrayList<String> refs_pos=new ArrayList<>();

                SemanticGraph graph = CorenlpLoader.getGraphFrom(textSequence);
                IndexedWord subject_node = CorenlpLoader.getNodeFromParentWithRelation(graph, graph.getFirstRoot(), "nsubj");

                if (subject_node != null) {
                    if (subject_node.value().toUpperCase().equals("IL") && i>0) { // if subject is a pronoun , first sentence -> can't find ref before

                        if ( refs.get(i-1) != null){
                            ref=refs.get(i-1);
                        }
                        else{
                            SemanticGraph prev_graph = CorenlpLoader.getGraphFrom(textSequences.get(i - 1)); // the graph to read is the graph of the previous textSequence
                            IndexedWord prev_subject_node = CorenlpLoader.getNodeFromParentWithRelation(prev_graph, prev_graph.getFirstRoot(), "nsubj");
                            descendants = CorenlpLoader.getOrderedDescendants(prev_graph, prev_subject_node);
                            int beg_idx=descendants.get(0).index()-1;
                            refs_pos=textSequences.get(i-1).getGramPositionsBetween(beg_idx,beg_idx+descendants.size());
                        }
                    }
                    else{
                        descendants= CorenlpLoader.getOrderedDescendants(graph, subject_node);
                        int beg_idx=descendants.get(0).index()-1;
                        refs_pos=textSequence.getGramPositionsBetween(beg_idx,beg_idx+descendants.size());

                    }
                    if(ref == null){
                        StringBuilder sb=new StringBuilder();
                        ArrayList<String> descendants_str=new ArrayList<>();
                        descendants.forEach(desc-> descendants_str.add(desc.value()));
                        LinkedList<String> cw=compoundWordBuilder.getNewCompoundWordFrom(descendants_str, refs_pos,true);
                        if(! cw.isEmpty()) {
                            ref = cw.get(0);
                        }
                    }
                    refs.set(i,ref);
                    for (Integer refIdx : refsIdx) {
                        textSequence.setRef(refIdx,ref);
                        System.out.println(textSequence.getWords().get(refIdx) + " -> " + ref);
                        //System.out.println(co);
                    }
                }

            }
        }
    }


}




