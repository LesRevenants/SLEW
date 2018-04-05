package TextStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

public class TextSequenceAnalyser {
	
	private static TextSequenceAnalyser instance;
	private static TreeTaggerWrapper<String> tt;
	
	private TextSequenceAnalyser() {
		 System.setProperty("treetagger.home","lib/TreeTagger");
		 tt = new TreeTaggerWrapper<String>();
		 try {
		      tt.setModel("lib/TreeTagger/lib/french-utf8.par");
		 } catch (IOException  e) {
				e.printStackTrace();
		}
	}
	
	public static TextSequenceAnalyser getInstance() {
		if(instance != null) {
			return instance;
		}
		instance = new TextSequenceAnalyser();
		return instance;
	}
	
	public ArrayList<String> getPositionsOf(TextSequence textSequence){
		ArrayList<String> textSequencePositions = new ArrayList<>();
		 try {
		      tt.setModel("lib/TreeTagger/lib/french-utf8.par");
		      tt.setHandler((token, pos, lemma) -> {
		    	  textSequencePositions.add(pos);
		    	  //System.out.println(token + "\t\t" + pos );
		      });
		        
		      LinkedList<String> allWords = new LinkedList<>();
		      HashMap<String, ArrayList<String>> words_replacements =  textSequence.getWords_replacements();
		      ArrayList<String> words = textSequence.getWords();
		      words.forEach(word -> {
		      	  if(textSequence.isCompoundWord(word)){
		    		  allWords.addAll(words_replacements.get(word));
		    	  }
		    	  else {
		    		  allWords.add(word);
		    	  }
		      });
		      tt.process(allWords);
		   
		      
		    } catch (IOException | TreeTaggerException e) {
				e.printStackTrace();
			}
		return textSequencePositions;
	}
	
	

}
