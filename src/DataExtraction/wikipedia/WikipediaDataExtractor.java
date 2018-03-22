package DataExtraction.wikipedia;

import java.util.ArrayList;
import java.util.LinkedList;

import DataExtraction.DataExtractor;
import DataExtraction.RawTextExtractor;
import TextStructure.TextSequence;

public class WikipediaDataExtractor implements DataExtractor {
	
	
	private String articleUrl;
	private Wiki wiki;
	
	public WikipediaDataExtractor(String articleName) {
		this.articleUrl = articleName;
		wiki=Wiki.createInstance("fr.wikipedia.org");
	}

	@Override
	public LinkedList<TextSequence> getTextSequences() {
		try {			
			String yolo2 = wiki.getRenderedText(articleUrl);
			yolo2 = yolo2.replace("{", ""); yolo2 = yolo2.replace("}", "");
			yolo2 = yolo2.replace("[", ""); yolo2 = yolo2.replace("]", "");
			yolo2 = yolo2.replace("*", ""); //yolo2 = yolo2.replace("'", "");
			String[] lines = yolo2.split(System.getProperty("line.separator"));
			yolo2 = yolo2.replace("'''", ""); yolo2 = yolo2.replace("]", "");
			String content = "";
			for(String l : lines) {
				//System.out.println(l);
				CharSequence k = "<p>";
				CharSequence m = "</p>";
				if(l.contains(k)  && !l.matches("<!--[a-zA-Z]*-->")) {
					if(!l.equals("\n")){
					content = content + l + "\n";
					//System.out.println(l);
					}
				}
			}
			content = content.replaceAll("\n\n", "\n");
			content = content.replaceAll("(n>[0-9]<s)", "(n><s)");
			content = content.replaceAll("&#160;", "");
			content = content.replaceAll("(<[^>]*>)", "");	
			RawTextExtractor rawTextExtractor = new RawTextExtractor(content);
			return rawTextExtractor.getTextSequences();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
