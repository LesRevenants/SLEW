package TextStructure;



import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;

import java.util.*;

/**
 *
 */
public class TextSequence {

    /** A list of each word into the sequence
     *  ex : "Le requin est un poisson" -> {Le,requin,est,un,poisson}
     */
    private ArrayList<String> words;

	/**
     * The list of Grammatical positions of the words
     */
    private ArrayList<String> wordsGramPositions;
    
    private HashMap<String, ArrayList<Integer>> wordsByGramPositions;
    
    /**
     * Key/Value associative array [ a_multiple_word -> {a,multiple_word} ]
     */
    private HashMap<String, ArrayList<String>> words_replacements; 
    
    /**
     *  Key/Value associative array [ a_multiple_word -> { pos(a),pos(multiple),pos(word)}
     */
    private HashMap<String,ArrayList<String>> compoundWordGramPositions;


    private ArrayList<Integer> puncIdxs;


    
    private ArrayList<Integer> patternsIdx;

    private ArrayList<Integer> compoundWordIdx;

    private HashMap<Integer,String> refs;

    private long size;


	public long getSize() {
		return size;
	}

	public TextSequence() {
    	 words = new ArrayList<>();
    	 words_replacements = new HashMap<>();
    	 wordsGramPositions=new ArrayList<>();
    	 compoundWordGramPositions =new HashMap<>();
    	 patternsIdx=new ArrayList<>();
    	 compoundWordIdx=new ArrayList<>();
    	 refs=new HashMap<>();
    	 size =0;
    }
    
    /*public TextSequence(ArrayList<String> words) {
        this();
        this.words = words;
    }*/

    private void set_size(){
    	words.forEach(word -> size += word.length());
	}

	public ArrayList<String> getWordsGramPositions() {
		return wordsGramPositions;
	}

	public boolean setWordsGramPositions(ArrayList<String> wordsGramPositions, HashSet<String> patterns) {
		/*if(words.size() != wordsGramPositions.size()) {
    		return false;
    	}*/
		wordsByGramPositions=new HashMap<>();
		int i=0;
		int offset=0;
		while(i<wordsGramPositions.size() && (i-offset) < words.size()) {
			//if(i-offset == words.size())
			String word = words.get(i-offset);
			if(isCompoundWord(word)) {
				int compoundWordSize = words_replacements.get(word).size();
				if(patterns.contains(word)) {
					this.wordsGramPositions.add("PAT");
					patternsIdx.add(i-offset);
				}
				else{
					this.wordsGramPositions.add("CW");
					compoundWordIdx.add(i-offset);
				}

				compoundWordGramPositions.put(word, new ArrayList<>(wordsGramPositions.subList(i, i+compoundWordSize)));
				i += compoundWordSize;
				offset += compoundWordSize-1;
			}
			else {
				String word_pos = wordsGramPositions.get(i);
				this.wordsGramPositions.add(word_pos);
				i++;
			}
			
		}
		
		for(i=0;i<words.size();i++) {
			String word_pos=this.wordsGramPositions.get(i);
			if(!wordsByGramPositions.containsKey(word_pos)) {
				wordsByGramPositions.put(word_pos, new ArrayList<>());
			}
			wordsByGramPositions.get(word_pos).add(i);
		}
    	return true;
	}
    

	public TextSequence(Collection<String> words) {
        this();
        this.words.addAll(words);
        set_size();
    }

	/**
	 * Read words and add into this.words
	 * @param words
	 * Complexity :  O(2*|words|) : O(|words|)
	 */
    public TextSequence(String[] words){
    	this();
        for(String word : words){
            this.words.add(word);
        }
        set_size();
    }
    
    
    public ArrayList<String> getWords() {
        return words;
    }
    
   /* public void setWords_replacements(HashMap<String, ArrayList<String>> words_replacements) {
		this.words_replacements = words_replacements;
	}*/
    
    public boolean isCompoundWord(String word) {
    	return words_replacements.containsKey(word);
    }

    public ArrayList<String> getWordsPositions() {
		return wordsGramPositions;
	}

	public boolean setWords_replacements(HashMap<String, ArrayList<String>> words_replacements) {
		if(words.isEmpty())
			return false;
		this.words_replacements = words_replacements;

		return true;
	}

	public void update_compoundWordPos(){
    	/*for(Map.Entry<String,ArrayList<String>> entry : compoundWordGramPositions.entrySet()){
			if(entry.getValue().get(0).equals("NOM")){

			}
		}*/
    	for(Integer cw_idx : compoundWordIdx){
    		String cw=words.get(cw_idx);
    		if(compoundWordGramPositions.get(cw).get(0).equals("NOM") ){
				wordsGramPositions.set(cw_idx,"NOM");
			}
		}
	}

	/**
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	public ArrayList<String> getGramPositionsBetween(int begin, int end){
    	int i=begin;
    	ArrayList<String> pos=new ArrayList<>();
    	if( ! (end > begin) ){
    		return pos;
		}
		while (i<end){
			String word=words.get(i);
			if(isCompoundWord(word)){
				ArrayList<String> cw_pos=compoundWordGramPositions.get(word);
				pos.addAll(cw_pos);
				i += cw_pos.size();
			}
			else{
				pos.add(wordsGramPositions.get(i));
			}
			i++;
		}
		return pos;
	}

	public String toString(){
		int indent = 90; int compound = 0;
        StringBuilder sb = new StringBuilder();
        for(int i=0 ;i<words.size(); i++) {
        	String word = words.get(i);
        	sb.append(i+":"+word);
        	ArrayList<String> word_replacement = words_replacements.get(word);
        	if(word_replacement != null){
        		sb.append(" : [");
        		compound = compound + 4;
        		for(int j=0;j<word_replacement.size();j++) {
        			sb.append(word_replacement.get(j)+"[");
        			compound += (word_replacement.get(j)+"[").length();
        			sb.append(compoundWordGramPositions.get(word).get(j)+"], ");
        			compound += (compoundWordGramPositions.get(word).get(j)+"], ").length();
        		}

        	
        		sb.append("]");
        		compound ++;
        	}
        	int temp = indent - word.length() - compound;
        	if(i<10){
        		for(int j = 0; j<temp; j++){
        			sb.append(" ");
        		}
        	} else {
        		for(int j = 0; j<temp-1; j++){
        			sb.append(" ");
        		}
        	}
        	sb.append("["+wordsGramPositions.get(i)+"]");
        	sb.append("\n");
        	compound =0;
        }
        return sb.toString();
    }
    
    public String shortToString() {
    	StringBuilder sb = new StringBuilder();
        for(int i=0 ;i<words.size(); i++) {
        	sb.append(words.get(i));
        	sb.append("\n");
        }
        return sb.toString();
    }

    private void buildPuncIdxs(){
    	puncIdxs=new ArrayList<>();
    	for(int i=0;i<wordsGramPositions.size();i++){
    		if(wordsGramPositions.get(i).equals("PUNC")){
    			puncIdxs.add(i);
			}
		}
	}

	public HashMap<String, ArrayList<String>> getWords_replacements() {
		return words_replacements;
	}

	public ArrayList<Integer> getPatternsIdx() {
		return patternsIdx;
	}

	public String getWordsAsStr(){
		StringBuilder sb=new StringBuilder();
		for(String word : words){
			if(isCompoundWord(word)){
				words_replacements.get(word).forEach(subword -> sb.append(subword+" "));
			}
			else{
				sb.append(word+" ");
			}

		}
		return sb.toString();
	}


    public void setRef(Integer idx, String ref){
		if(idx >= 0 && idx <words.size()){
			if(words.get(idx).equals("IL")){
				refs.put(idx,ref);
			}
		}
	}

	public boolean is_ref(Integer idx){
		return refs.containsKey(idx);
	}

	public String getRefReplacement(Integer idx){
		return refs.get(idx);
	}
	
    
    
}

