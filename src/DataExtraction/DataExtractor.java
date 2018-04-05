package DataExtraction;

import TextStructure.TextSequence;
import Util.Pair;

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
    public abstract Pair<String,LinkedList<TextSequence>> extract(String src);


    public abstract Collection<Pair<String,LinkedList<TextSequence>>> extractAll(Collection<String> data_sources,int limit);
}