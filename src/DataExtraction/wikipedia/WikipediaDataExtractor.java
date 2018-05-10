package DataExtraction.wikipedia;

import java.util.Collection;
import java.util.LinkedList;

import DataExtraction.DataExtractor;
import DataExtraction.RawTextExtractor;
import TextStructure.TextSequence;
import Util.Pair;

public class WikipediaDataExtractor implements DataExtractor {
	

	private Wiki wiki;
	private String text;
	
	public WikipediaDataExtractor() {
		wiki=Wiki.createInstance("fr.wikipedia.org");
	}

	public String getText() {
		return text;
	}

	@Override
	public Pair<String, LinkedList<TextSequence>> extract(String articleUrl) {
		try {
			String yolo2 = wiki.getRenderedText(articleUrl);
			if(yolo2==null){
				return null;
			}
			
			yolo2 = yolo2.replace("{", ""); yolo2 = yolo2.replace("}", "");
			yolo2 = yolo2.replace("[", ""); yolo2 = yolo2.replace("]", "");
			yolo2 = yolo2.replace("*", ""); //yolo2 = yolo2.replace("'", "");
			String[] lines = yolo2.split(System.getProperty("line.separator"));
			yolo2 = yolo2.replace("'''", ""); yolo2 = yolo2.replace("]", "");
			String content = "";
			for(String l : lines) {
				CharSequence k = "<p>";
				CharSequence li =" <li>";
				if(( l.contains(k) || l.contains(li))  && !l.matches("<!--[a-zA-Z]*-->")) {
					if(!l.equals("\n")){
					content = content + l + "\n";
					}
				}
			}
			
			content = content.replaceAll("\n\n", "\n");
			content = content.replaceAll("(n>[0-9]+<s)", "(n><s)");
			content = content.replaceAll("&#160;", "");
			content = content.replaceAll("(<[^>]*>)", "");	
			
			
			
			text=content;
			RawTextExtractor rawTextExtractor = new RawTextExtractor();
			Pair<String,LinkedList<TextSequence>> data_src = rawTextExtractor.extract(content);

			data_src.setLeft(articleUrl);
			return data_src;
		} catch (Exception  | UnknownError e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<Pair<String,LinkedList<TextSequence>>> extractAll(Collection<String> data_sources, int limit) {
		Collection<Pair<String,LinkedList<TextSequence>>> allSequences=new LinkedList<>();
		for(String src : data_sources){
			if(src != null){
				Pair<String,LinkedList<TextSequence>> pair = extract(src);
				if(pair != null){
					allSequences.add(pair);
				}
			}
		}
		return allSequences;
	}
}
