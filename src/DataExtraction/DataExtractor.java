package DataExtraction;

import TextStructure.TextSequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import DataExtraction.wikipedia.WikipediaDataExtractor;

/**
 *
 */
public interface DataExtractor {

    /**
     *
     * @return
     */
    public abstract LinkedList<TextSequence> getTextSequences(String src);


    public abstract Collection<LinkedList<TextSequence>> extractAll(Collection<String> data_sources,int limit);
}