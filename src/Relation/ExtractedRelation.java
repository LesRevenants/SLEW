package Relation;

import TextStructure.TextSequence;
import Util.UtilColor;
import Util.Utils;

public class ExtractedRelation{
	
	private String relation_type;
	private String linguisticPattern;
	private String object;
	private String subject;
	private TextSequence context;
	private int begin_idx;
	private int end_idx;
	private static final int windows_size=3;


	public ExtractedRelation(String relation_type, String linguisticPattern, String object, String subject, TextSequence context, int begin_idx, int end_idx) {
		this.relation_type = relation_type;
		this.linguisticPattern = linguisticPattern;
		this.object = object;
		this.subject = subject;
		this.context = context;
		this.begin_idx = (begin_idx-windows_size >= 0) ? begin_idx-windows_size : 0;
		this.end_idx = (end_idx + windows_size < context.getWords().size() ? end_idx +windows_size: context.getWords().size());
	}

	/*public ExtractedRelation(String relation_type, String linguisticPattern, String object, String subject) {
		super();
		this.relation_type = relation_type;
		this.linguisticPattern = linguisticPattern;
		this.object = object;
		this.subject = subject;
	*/

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(UtilColor.ANSI_GREEN);
		sb.append(relation_type);
		sb.append(" : ");
		sb.append(linguisticPattern);
		sb.append("(");
		sb.append(subject);
		sb.append(",");
		sb.append(object);
		sb.append(UtilColor.ANSI_WHITE);
		sb.append(") --> ");
		sb.append(UtilColor.ANSI_RESET);
		for(String word : context.getWords().subList(begin_idx,end_idx)) {
			sb.append(word+" ");
		}
		return sb.toString();
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
