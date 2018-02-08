import RequeterRezo.Annotation;

public class ExtractedRelation {
	
	private String extractedPatternName;
	private Annotation annotation;
	private int pattern_idx;
	

	public String getExtractionPatternName() {
		return extractedPatternName;
	}

	public ExtractedRelation(String extractionPatternName, Annotation annotation, int pattern_idx) {
		super();
		this.extractedPatternName = extractionPatternName;
		this.annotation = annotation;
		this.pattern_idx = pattern_idx;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public int getPattern_idx() {
		return pattern_idx;
	}

	public String toString() {
		return "["+extractedPatternName +":idx="+pattern_idx + "] --> "+annotation.toString();
	}
	
	
	
	
}
