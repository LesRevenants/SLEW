import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;


/**
 * Describe a word sequence as structured datas
 *
 */
public class WordSequence {
	
	/** The sequence as raw data */
	private String data; 
	
	/** A list of each word into the sequence 
	 *  ex : "Le requin est un poisson" -> {Le,requin,est,un,poisson}
	 */
	private ArrayList<String> words_set;
	
	
	private int comb_size;
	
	/** A list of comb_size list  word | word is a combinaison of word
	 * each word is a combinaison of [0..comb_size] word
	 *  
	 *  ex : "Le requin est un poisson" with comb_size = 3 -> 
	 *  {Le,requin,est,un,poisson}
	 *  {Le requin, requin est,est un,un poisson}
	 *  {Le requin est,est un poisson}
	 *  
	 * */
	private ArrayList<ArrayList<String>> word_combs;
	
	
	public WordSequence(String phrase,int comb_size) {
		data = phrase;
		words_set = new ArrayList<>(Arrays.asList(phrase.split(" ")));	// split the phrase with " " in order to get all different words
		this.comb_size = comb_size;
		build_combs();
	}
	
	/**
	 * Build the list of combinaisons
	 * @return
	 */
	private  void build_combs(){
		word_combs= new ArrayList<>();
		for(int i=0 ;i< comb_size;i++){ // build a word composed of 1 word, then 2 word then 3 words ...
			word_combs.add(new ArrayList<>());
			for(int j=0 ;j<words_set.size(); j++){ // loop word list 
				StringBuilder composed_word = new StringBuilder();
				int last_word_idx = (j+i+1);
				
				/**
				 * Read from j nth word to last word and concat the k_nth word to the composed word
				 */
				for(int k=j; k<last_word_idx && k<words_set.size();k++){ 
					if(i > 0 && k < last_word_idx-1) {
						composed_word.append(words_set.get(k) +" "); // add a separation between two word only if there are more than 2 words and not last word
					}
					else {
						composed_word.append(words_set.get(k));
					}
					
				}
				word_combs.get(i).add(composed_word.toString()); // add the generated word
			}
		}
	}

	public String getData() {
		return data;
	}

	public ArrayList<String> getWords_set() {
		return words_set;
	}

	public ArrayList<ArrayList<String>> getWord_comb() {
		return word_combs;
	}
	
	public void display() {
		word_combs.forEach((l)->{
			l.forEach((l2)->{
				System.out.println(l2);
			});
			System.out.println();
		});
	}
	

	
}
