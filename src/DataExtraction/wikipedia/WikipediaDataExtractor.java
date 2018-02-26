package DataExtraction.wikipedia;

import java.util.ArrayList;
import java.util.LinkedList;

import com.bitplan.mediawiki.japi.Mediawiki;

import DataExtraction.DataExtractor;
import DataExtraction.RawTextExtractor;
import TextStructure.TextSequence;

public class WikipediaDataExtractor implements DataExtractor {
	
	private static String WIKIPEDIA_FR_ADDR = "https://fr.wikipedia.org/wiki/";
	
	private String articleUrl;
	
	public WikipediaDataExtractor(String articleName) {
		articleUrl = WIKIPEDIA_FR_ADDR+articleName;
	}

	@Override
	public LinkedList<TextSequence> getTextSequences() {
		try {
			Mediawiki wiki = new Mediawiki(articleUrl);
			String content = wiki.getPageContent("Main Page");
			System.out.println(content);
			RawTextExtractor rawTextExtractor = new RawTextExtractor(content);
			return rawTextExtractor.getTextSequences();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}

}
