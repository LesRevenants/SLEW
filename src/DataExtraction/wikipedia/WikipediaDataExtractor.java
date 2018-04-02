package DataExtraction.wikipedia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import DataExtraction.DataExtractor;
import DataExtraction.RawTextExtractor;
import TextStructure.TextSequence;

public class WikipediaDataExtractor implements DataExtractor {
	

	private Wiki wiki;
	
	public WikipediaDataExtractor() {
		wiki=Wiki.createInstance("fr.wikipedia.org");
	}

	@Override
	public LinkedList<TextSequence> getTextSequences(String articleUrl) {
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
			RawTextExtractor rawTextExtractor = new RawTextExtractor();
			return rawTextExtractor.getTextSequences(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<LinkedList<TextSequence>> extractAll(Collection<String> data_sources, int limit) {
		Collection<LinkedList<TextSequence>> allSequences=new LinkedList<>();
		data_sources.forEach(src -> {
			allSequences.add(getTextSequences(src));
		});
		return allSequences;
	}
}
