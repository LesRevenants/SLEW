package Relation;

public class ExtractedRelation{
	
	private String relation_type;
	private String linguisticPattern;
	private String object;
	private String subject;

	
	

	public ExtractedRelation(String relation_type, String linguisticPattern, String object, String subject) {
		super();
		this.relation_type = relation_type;
		this.linguisticPattern = linguisticPattern;
		this.object = object;
		this.subject = subject;
	}

	@Override
	public String toString() {
		return relation_type + ": "+ linguisticPattern + "("+subject + "," +object+")";
	}

	public String getRelation_type() {
		return relation_type;
	}

	public String getLinguisticPattern() {
		return linguisticPattern;
	}

	public String getObject() {
		return object;
	}

	public String getSubject() {
		return subject;
	}
	
	
	

}
