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
			String yolo2 = wiki.getPageText(articleUrl);
			yolo2 = yolo2.replace("{", ""); yolo2 = yolo2.replace("}", "");
			yolo2 = yolo2.replace("[", ""); yolo2 = yolo2.replace("]", "");
			yolo2 = yolo2.replace("*", ""); //yolo2 = yolo2.replace("'", "");
			String[] lines = yolo2.split(System.getProperty("line.separator"));
			yolo2 = yolo2.replace("'''", ""); yolo2 = yolo2.replace("]", "");
			String content = "";
			for(String l : lines) {
				CharSequence k = "Autres projets";		
				CharSequence m = "Références";		
				CharSequence n = "...";			
				CharSequence o = "Catégorie:";
				CharSequence p = "DEFAULTSORT";
				CharSequence q = "*";
				CharSequence r = "Section vide";
				CharSequence s = "article à sourcer";
				CharSequence t = "article à wikifier";
				CharSequence u = "voir homonymes";
				CharSequence v = "Fichier:";
				CharSequence w = "==";
				CharSequence x = "section à sourcer";
				CharSequence y = "|";
				CharSequence z = "http://";
				if(!l.contains(k) && !l.contains(m) && !l.contains(n) && !l.contains(o) && !l.contains(p) && !l.contains(q) && !l.contains(r) && !l.contains(s) && !l.contains(t) && !l.contains(u) && !l.contains(v) && !l.contains(w) && !l.contains(x) && !l.contains(y) && !l.contains(z) && !l.matches("<!--[a-zA-Z]*-->")) {
					if(!l.equals("\n"))
					content = content + l + "\n";}
			}
			content = content.replaceAll("\n\n", "\n");
			System.out.print(content);
			
			RawTextExtractor rawTextExtractor = new RawTextExtractor(content);
			return rawTextExtractor.getTextSequences();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
