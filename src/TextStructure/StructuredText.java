package TextStructure;


import java.util.*;

import Util.Pair;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;

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

        boolean use_corenlp=properties.containsKey("use_corenlp") && properties.getProperty("use_corenlp").equals("true");
        patterns = new HashSet<>(knownPatterns);
        WordPatriciaTrie patterns_trie=new WordPatriciaTrie();
        patterns.forEach(pat -> patterns_trie.put(pat,true));

        inTextSequences.stream()
                .filter(ts -> checkPatterns(ts,patterns_trie))
                .forEach(ts ->  updateCW(ts));


        if(use_corenlp){
            corenlpLoader=CorenlpLoader.getInstance();
            resolveCoreference(corenlpLoader.getPipeline());
        }
	}



	private boolean checkPatterns(TextSequence t0, WordPatriciaTrie patterns_trie){
        StringBuilder sb=new StringBuilder();
        t0.getWords().forEach(word->sb.append(word+" "));


        boolean found=false, begin_found=false;
        int i=0,j=0;
        String pattern="";

        while (!found && i<t0.getWords().size()){
            String word=t0.getWords().get(i);
            if(begin_found) {
                String new_word=pattern+"_"+word;
                if(patterns_trie.hasPrefix(new_word)){ // check if bigger pattern could be find
                    pattern=new_word;
                    j++;
                }
                else{
                    if(patterns_trie.containsKey(new_word)){
                        found=true;
                    }
                    else{
                        for(int k=0;k<j;k++){
                            int offset=t0.getWords().get(i-k).length();
                            int end_idx=new_word.length()-offset-1;
                            if(patterns_trie.containsKey(new_word.substring(0,end_idx))){
                                found=true;
                            }
                        }
                        if(! found){
                            begin_found=false;
                            pattern="";
                        }

                    }
                }
            }
            else{
                if(patterns_trie.hasPrefix(word)){
                    begin_found=true;
                    j++;
                    pattern=word;
                }
            }
            i++;
        }
        return found;
    }
	private void updateCW(TextSequence t0){

        ArrayList<String> positions = textSequenceAnalyser.getPositionsOf(t0);

        Collection<String> new_cw=compoundWordBuilder.getNewCompoundWordFrom(t0.getWords(),positions,false);
        compoundWordBuilder.addToTrie(new_cw);

        Pair<ArrayList<String>,HashMap<String,ArrayList<String>>> cw_pair= compoundWordBuilder.replaceSequence(t0, 8);
        ArrayList<String> new_words=cw_pair.getLeft();
        HashMap<String,ArrayList<String>> words_replacements=cw_pair.getRight();

        if(! words_replacements.isEmpty()){
            t0.set(new_words,positions,words_replacements,patterns);
            textSequences.add(t0);
        }


        /*Collection<String> new_cw=compoundWordBuilder.getNewCompoundWordFrom(t0.getWords(),t0.getWordsPositions(),false);





        textSequences.add(t0);
        total_size += t0.getSize();
        /*ArrayList<String> positions = textSequenceAnalyser.getPositionsOf(t0);
        Pair<ArrayList<String>,HashMap<String,ArrayList<String>>> cw_pair= compoundWordBuilder.replaceSequence(t0, 8);
        ArrayList<String> new_words=cw_pair.getLeft();
        HashMap<String,ArrayList<String>> words_replacements=cw_pair.getRight();
        t0.set(new_words,positions,words_replacements,patterns);
        Collection<String> new_cw=compoundWordBuilder.getNewCompoundWordFrom(t0.getWords(),t0.getWordsPositions(),false);

        if( ! t0.getPatternsIdx().isEmpty()){
           if( ! new_cw.isEmpty()){
                compoundWordBuilder.add(
            }
            textSequences.add(t0);
            total_size += t0.getSize();
        }*/


       /* t1.setWordsGramPositions(positions,patterns);
        t1.updateCompoundWordPos();

        compoundWordBuilder.addToTrie(new_cw);
        TextSequence t2=compoundWordBuilder.replaceSequence(t0,8);
        t2.updateCompoundWordPos();
        return t2;*/
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

                boolean found_before=false;
                boolean ref_found=false;
                if (subject_node != null) {
                    if (subject_node.value().toUpperCase().equals("IL") && i>0) { // if subject is a pronoun , first sentence -> can't find ref before

                        if ( refs.get(i-1) != null){
                            ref=refs.get(i-1);
                            found_before=true;
                        }
                        else{
                            SemanticGraph prev_graph = CorenlpLoader.getGraphFrom(textSequences.get(i - 1)); // the graph to read is the graph of the previous textSequence
                            IndexedWord prev_subject_node = CorenlpLoader.getNodeFromParentWithRelation(prev_graph, prev_graph.getFirstRoot(), "nsubj");
                            if(prev_subject_node != null){
                                descendants = CorenlpLoader.getOrderedDescendants(prev_graph, prev_subject_node);
                                int beg_idx=descendants.get(0).index()-1;
                                refs_pos=textSequences.get(i-1).getGramPositionsBetween(beg_idx,beg_idx+descendants.size());
                                ref_found=true;
                            }

                        }
                    }
                    else{
                        descendants= CorenlpLoader.getOrderedDescendants(graph, subject_node);
                        int beg_idx=descendants.get(0).index()-1;
                        refs_pos=textSequence.getGramPositionsBetween(beg_idx,beg_idx+descendants.size()-1);

                    }
                    if(! found_before && ref_found){
                        StringBuilder sb=new StringBuilder();
                        ArrayList<String> descendants_str=new ArrayList<>();
                        descendants.forEach(desc-> descendants_str.add(desc.value()));
                        LinkedList<String> cw=compoundWordBuilder.getNewCompoundWordFrom(descendants_str, refs_pos,true);
                        if(! cw.isEmpty()) {
                            ref = cw.get(0);
                        }
                    }
                    if(ref != null){
                        refs.set(i,ref);
                        for (Integer refIdx : refsIdx) {
                            textSequence.setRef(refIdx,ref);
                            System.out.println(i + ": "+ textSequence.getWords().get(refIdx) + " -> " + ref);
                        }
                    }

                }

            }
        }
    }


}




