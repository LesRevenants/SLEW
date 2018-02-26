package TextStructure;

import lib.org.ardverk.collection.PatriciaTrie;
import lib.org.ardverk.collection.StringKeyAnalyzer;

/**
 * A specialization of PatriciaTrie with String as key and boolean as value
 * {@link PatriciaTrie}
 */
public class WordPatriciaTrie extends PatriciaTrie<String,Boolean>{

    /**
     *
     */
    public WordPatriciaTrie() {
        super(StringKeyAnalyzer.CHAR);
    }

    /**
     *
     * @param prefix : The prefix
     * @return if the trie has prefix as a prefix
     * Complexity : O( |prefix| )
     */
    public boolean hasPrefix(String prefix){
        TrieEntry<String,Boolean> prefixEntry =  subtree(prefix);
        return prefixEntry != null;
    }
}
