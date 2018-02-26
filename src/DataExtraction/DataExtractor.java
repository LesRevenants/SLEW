package DataExtraction;

import TextStructure.TextSequence;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 */
public interface DataExtractor {

    /**
     *
     * @return
     */
    public LinkedList<TextSequence> getTextSequences();
}
