package TextStructure;

//import com.google.common.io.Files;



import Util.Pair;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;


/**
 *
 */
public class CompoundWordBuilder {

	
	private String filePath;
	
    /**
     * A PatraiciaTrie which store the compound word list
     * Allow fast prefix retrieval
     * {@link WordPatriciaTrie}
     */
    private WordPatriciaTrie trie;

    /**
     * Flag which indicate if data have been read
     */
    private boolean dataLoaded;

    ArrayList<ArrayList<String>> compoundWordRules;


    public CompoundWordBuilder(){
       trie = new WordPatriciaTrie();
       dataLoaded = false;
       compoundWordRules=new ArrayList<>();
    }


    public void loadRules(String rulesPath){
        try{
            Files.readAllLines(Paths.get(rulesPath)).forEach(
                    line -> compoundWordRules.add(new ArrayList<String>(Arrays.asList(line.split(";")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public LinkedList<String> getNewCompoundWordFrom(ArrayList<String> words, ArrayList<String> positions, boolean stop_first){


        LinkedList<String> new_compound_words=new LinkedList<>();

        for (ArrayList<String> rule : compoundWordRules) {
            // compoundWordRules.forEach(rule -> { // generate new compound word with rules ( ex : NOM,NOM
            int j = 0;
            for (int i = 0; i < positions.size()-1; i++) {
                if (positions.get(i).equals(rule.get(j))) {
                    j++;
                } else {
                    j = 0;
                }

                if (j == rule.size()) {
                    StringBuilder new_compound_word = new StringBuilder();
                    for (int k = i - j + 1; k < i; k++) {
                        new_compound_word.append(words.get(k) + "_");
                    }

                    new_compound_word.append(words.get(i));
                   /* System.out.println("MC : "+new_compound_word.toString());
                    System.out.println("Rule : "+rule.toString());*/
                    new_compound_words.add(new_compound_word.toString());
                    j = 0;
                    if(stop_first){
                        return new_compound_words;
                    }
                }

            }
        }
        return new_compound_words;
    }

    public CompoundWordBuilder(String filePath,boolean serialized, String rulesPath) {
		this();
		this.filePath = filePath;
		read(filePath, serialized);
		loadRules(rulesPath);
	}


    /**
     * @param oldWords : A collection of word
     * @param limitSize : The maximum size of a compound word which can be finded
     * @return The list of compound word from the sentence
     * Complexity :
     */


     public Pair<ArrayList<String>, HashMap<String, ArrayList<String>>> replaceSequence(ArrayList<String> oldWords, int limitSize){
        if(limitSize < 2){
            throw new IllegalArgumentException("Limit size of a compoundWord must be >= 2 : "+limitSize);
        }

        ArrayList<String> newWords = new ArrayList<>(oldWords.size()); // reserve memory for  |old words| words
        
        /**
         * The list of replacement of each multiple_word. Ex replace(est_un) -> {est,un}
         */
        HashMap<String,ArrayList<String>> newTextSequenceReplacements = new HashMap<>();

        build(oldWords,"",0,0,limitSize,newWords,newTextSequenceReplacements);
        return new Pair<>(newWords,newTextSequenceReplacements);
    }
    
     public void build(ArrayList<String> oldWords, String prefixWord, int i, int compoundWordSize, int limitSize,
                       ArrayList<String> newWords,
                       HashMap<String,ArrayList<String>> newTextSequenceReplacements) {
    	 
    	 if(i== oldWords.size()) {
    	     if(compoundWordSize>0){
    	         if( trie.containsKey(prefixWord)) {
                     newWords.add(prefixWord);
                     newTextSequenceReplacements.put(prefixWord, new ArrayList<String>(Arrays.asList(prefixWord.split("_"))));
                 }
                 else{
                     newWords.addAll(Arrays.asList(prefixWord.split("_")));  // the multiple word does not exist so add each part of multiple word
                 }
             }
    		 return;
    	 }
    	 String word= oldWords.get(i);
    	 if(compoundWordSize == 0) {
    		 if(trie.hasPrefix(word)) {
    			 build(oldWords,word,i+1,compoundWordSize+1,limitSize,newWords,newTextSequenceReplacements);
    		 }
    		 else {
    			 newWords.add(word);
    			 build(oldWords,"",i+1,0,limitSize,newWords,newTextSequenceReplacements);
    		 }
    	 }
    	 else {
    		 if(compoundWordSize==limitSize) {
    			 if(trie.containsKey(prefixWord)) {
    				 newWords.add(prefixWord);
    		         newTextSequenceReplacements.put(prefixWord,new ArrayList<String>( Arrays.asList(prefixWord.split("_"))));
    		         build(oldWords,"",i+1,0,limitSize,newWords,newTextSequenceReplacements);
    			 }
    			 else {
                     newWords.addAll(Arrays.asList(prefixWord.split("_")));  // the multiple word does not exist so add each part of multiple word
    				 build(oldWords,"",i+1,0,limitSize,newWords,newTextSequenceReplacements);
    			 }
    		 }
    		 else {
    			 String newPrefix = prefixWord.concat("_").concat(word);
                 if( trie.hasPrefix(newPrefix)){ // check if word with newPrefix as prefix exists     
                     build(oldWords,newPrefix,i+1,compoundWordSize+1,limitSize,newWords,newTextSequenceReplacements);
                 }
                 else{ // no longer multiple-word found     
                	 if( (compoundWordSize > 1) ) {
                		 if(trie.containsKey(prefixWord)){  	// add the multiple_word if it exist
                			  newWords.add(prefixWord);
                              newTextSequenceReplacements.put(prefixWord,new ArrayList<String>( Arrays.asList(prefixWord.split("_"))));
                              build(oldWords,"",i,0,limitSize,newWords,newTextSequenceReplacements);
                		 }
                		 else {
                			 newWords.add(oldWords.get(i-compoundWordSize));
                			 build(oldWords,"",i-compoundWordSize+1,0,limitSize,newWords,newTextSequenceReplacements);
                		 }
                			                  
                    }
                 	else { //
                         newWords.add(prefixWord);
                         build(oldWords,"",i,0,limitSize,newWords,newTextSequenceReplacements);
                     }
                 }
    		 }
    		
    	 }
     }
    /**
     * Build the compound word list from a file
     * @param filePath : path of the file with store all compound words
     * @param serialized : If true -> read directly the formatted file , else read a non-serialized file and parse it
     * File reading is faster with serialized == true
     */
    public void read(String filePath,boolean serialized){
        try {
            if(!serialized){
                int i=0;
                Files.readAllLines(Paths.get(filePath), StandardCharsets.ISO_8859_1)
                        .stream()
                        .map(word -> word.split(";")[1])
                        .map(word -> word.replaceAll(" ","_"))
                        .forEach(word -> trie.put(word,true));
            }
            else{
                Files.readAllLines(Paths.get(filePath), StandardCharsets.ISO_8859_1)
                        .forEach( word -> trie.put(word,true));
            }
            dataLoaded = true;
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the trie into a formatted file
     * @param filePath : the file which sotre compound-word list
     */
    public void write(String filePath){
        if(! dataLoaded)
           throw new IllegalStateException("Attempting to write data before read them");
        try{
            String newline = System.getProperty("line.separator");
            OutputStreamWriter writer =
                    new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.ISO_8859_1);
           for(String key : trie.keySet()){
               writer.write(key+newline);
           }
           writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    
    
    public void addToTrie(Collection<String> compoundWords) {
    	compoundWords.forEach(word -> trie.put(word, true));
    }



	public WordPatriciaTrie getTrie() {
		return trie;
	}
    
    
}