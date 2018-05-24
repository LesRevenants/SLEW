package TextStructure;



import Util.Pair;
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
     * The list of Grammatical positions of the words with TreeTagger
     */
    private ArrayList<String> TT_WordGramPos;
    
    private HashMap<String, ArrayList<Integer>> wordsByGramPositions;
    
    /**
     * Key/Value associative array [ a_multiple_word -> {a,multiple_word} ]
     */
    private HashMap<String, ArrayList<String>> words_replacements;
    
    /**
     *  Key/Value associative array [ a_multiple_word -> { pos(a),pos(multiple),pos(word)}
     */
    private HashMap<String,ArrayList<String>> compoundWordGramPositions;
    private ArrayList<Integer> compoundWordIdx;


    private HashSet<String> patterns;
    private ArrayList<Integer> patternsIdx;

    private HashMap<Integer,String> refs;

    private long size;


	public long getSize() {
		return size;
	}

	public TextSequence() {
    	 words = new ArrayList<>();
    	 words_replacements = new HashMap<>();
    	 TT_WordGramPos =new ArrayList<>();
    	 compoundWordGramPositions =new HashMap<>();
		 patterns=new HashSet<>();
    	 compoundWordIdx=new ArrayList<>();
    	 refs=new HashMap<>();
    	 size =0;
    }
    
    /*public TextSequence(ArrayList<String> words) {
        this();
        this.words = words;
    }*/

    public void setWord_list(HashMap<Pair<Integer,Integer>, ArrayList<String>> words_replacements,SemanticGraph graph){
		/*graph=new SemanticGraph();
		graph.a*/
	}


    private void set_size(){
    	words.forEach(word -> size += word.length());
	}

	public ArrayList<String> getTT_WordGramPos() {
		return TT_WordGramPos;
	}

	public ArrayList<Integer> getPatternsIdx() {
		return patternsIdx;
	}

	/**
	 *
	 * @param words the new words ( eventually with multiple words )
	 * @param tt_word_gram_pos  positions of words ( eventually with pos of word without replacement by compound word)
	 * @param words_replacements a_compound_word ->  {a,compound,word}
	 * @param patterns The set of patterns
	 */
	public void set(ArrayList<String> words, ArrayList<String> tt_word_gram_pos, HashMap<String,ArrayList<String>> words_replacements, HashSet<String> patterns) {
    	assert( words.size()>0 && words.size()==tt_word_gram_pos.size());

		this.words=words;
		this.compoundWordIdx=new ArrayList<>();
		this.words_replacements=words_replacements;

		this.patterns=new HashSet<>();
		this.patternsIdx=new ArrayList<>();

		if( words_replacements.isEmpty()){
			this.TT_WordGramPos=tt_word_gram_pos;
		}
		else{
			this.TT_WordGramPos=new ArrayList<>();
			int offset=0;
			for (int i = 0; i < words.size(); i++) {
				String word=words.get(i);
				if(words_replacements.containsKey(word)){ // if word is a compound word

					int compoundWordSize = words_replacements.get(word).size();
					if(patterns.contains(word)){ // if the compound word is a pattern
						this.patterns.add(word); // save pattern and pattern idx
						this.patternsIdx.add(i);
						this.TT_WordGramPos.add("PAT");
					}
					else{
						int j=i+compoundWordSize+offset;
						compoundWordGramPositions.put(word, new ArrayList<>(tt_word_gram_pos.subList(i+offset, j))); // save gram pos of each sub-word word from a compound_word
						if(compoundWordGramPositions.get(word).get(0).equals("NOM")){
							this.TT_WordGramPos.add("NOM");
						}
						else{
							this.TT_WordGramPos.add("CW");
						}
						this.compoundWordIdx.add(i);
					}
					offset += compoundWordSize-1;
				}

				else{
					this.TT_WordGramPos.add(tt_word_gram_pos.get(i+offset));
				}

			}
		}
	}



	/*public boolean setWordsGramPositions(ArrayList<String> TT_WordGramPos, HashSet<String> patterns) {
		wordsByGramPositions=new HashMap<>();
		int i=0;
		int offset=0;
		while(i<TT_WordGramPos.size() && (i-offset) < words.size()) {
			//if(i-offset == words.size())
			String word = words.get(i-offset);
			if(isCompoundWord(word)) {
				int compoundWordSize = words_replacements.get(word).size();
				if(patterns.contains(word)) {
					this.TT_WordGramPos.add("PAT");
					patternsIdx.add(i-offset);
				}
				else{
					this.TT_WordGramPos.add("CW");
					compoundWordIdx.add(i-offset);
				}

				compoundWordGramPositions.put(word, new ArrayList<>(TT_WordGramPos.subList(i, i+compoundWordSize)));
				i += compoundWordSize;
				offset += compoundWordSize-1;
			}
			else {
				String word_pos = TT_WordGramPos.get(i);
				this.TT_WordGramPos.add(word_pos);
				i++;
			}
			
		}
		
		for(i=0;i<words.size();i++) {
			String word_pos=this.TT_WordGramPos.get(i);
			if(!wordsByGramPositions.containsKey(word_pos)) {
				wordsByGramPositions.put(word_pos, new ArrayList<>());
			}
			wordsByGramPositions.get(word_pos).add(i);
		}
    	return true;
	}*/
    

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
    	return words_replacements.containsKey(word) && ! patterns.contains(word);
    }



    public ArrayList<String> getWordsPositions() {
		return TT_WordGramPos;
	}

	private void updateWordsPos(ArrayList<String> tt_word_gram_pos, HashSet<String> patterns){



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
		while (i<end && i<words.size()){
			String word=words.get(i);
			if(isCompoundWord(word)){
				ArrayList<String> cw_pos=compoundWordGramPositions.get(word);
				pos.addAll(cw_pos);
				i += cw_pos.size();
			}
			else{
				pos.add(TT_WordGramPos.get(i));
			}
			i++;
		}
		return pos;
	}

	public String toString(){
		/*int indent = 90; int compound = 0;
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
        	sb.append("["+ TT_WordGramPos.get(i)+"]");
        	sb.append("\n");
        	compound =0;
        }
        return sb.toString();*/
		return shortToString();
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
    	/*puncIdxs=new ArrayList<>();
    	for(int i = 0; i< TT_WordGramPos.size(); i++){
    		if(TT_WordGramPos.get(i).equals("PUNC")){
    			puncIdxs.add(i);
			}
		}*/
	}

	public HashMap<String, ArrayList<String>> getWords_replacements() {
		return words_replacements;
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

