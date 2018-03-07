package Relation;

public class ExtractedRelation{
	
	private LinguisticPattern linguisticPattern;
	private String object;
	private int object_idx;
	private String subject;
	private int subject_idx;
	
	public ExtractedRelation(LinguisticPattern linguisticPattern, String object, int object_idx, String subject,
			int subject_idx) {
		super();
		this.linguisticPattern = linguisticPattern;
		this.object = object;
		this.object_idx = object_idx;
		this.subject = subject;
		this.subject_idx = subject_idx;
	}

	@Override
	public String toString() {
		return "\t"+linguisticPattern.getPattern() + "("+subject + "," +object+")";
	}
	
	

}
