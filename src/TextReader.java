import java.util.LinkedList;
import java.util.List;


/**
 *  
 *
 */
public class TextReader {

	private String text;
	
	public TextReader(String text) {
		super();
		this.text = text;
	}


	/**
	 * Split a text into several textSequence delimited by a dot
	 * @return the list of different WordSequence
	 */
	public List<WordSequence> getAllWordSequence(){
		LinkedList<WordSequence> wordSequences = new LinkedList<>();
		String[] sequences = text.split("\\.");
		for(String seq : sequences) {
			wordSequences.add(new WordSequence(seq));
		}
		return wordSequences;
	}
}
