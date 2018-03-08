import java.io.IOException;

public class WikipediaToText {

	Wiki test;
	
	public static void main(String[] arg) throws IOException {
		
		Wiki test = new Wiki();
		test.apiUrl = "fr.wikipedia.org";
		//test.fetch("fr.wikipedia.org", "format=xml&maxlag=5&action=query&prop=revisions&rvprop=content", "");
		String yolo2 = test.getPageText("pathologie");
		yolo2 = yolo2.replace("{", ""); yolo2 = yolo2.replace("}", "");
		yolo2 = yolo2.replace("[", ""); yolo2 = yolo2.replace("]", "");
		yolo2 = yolo2.replace("*", ""); //yolo2 = yolo2.replace("'", "");
		String[] lines = yolo2.split(System.getProperty("line.separator"));
		yolo2 = yolo2.replace("'''", ""); yolo2 = yolo2.replace("]", "");
		String finale = "";
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
				finale = finale + l + "\n";}
		}
		finale = finale.replaceAll("\n\n", "\n");
		System.out.print(finale);
		
	}
	
}
