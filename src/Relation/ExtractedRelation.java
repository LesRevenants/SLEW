package Relation;

import TextStructure.TextSequence;
import Util.UtilColor;
import Util.Utils;

import java.util.Objects;

public class ExtractedRelation{
	
	private String relation_type;
	private String linguisticPattern;
	private String object;
	private String subject;
	private TextSequence context;
	private int begin_idx;
	private int end_idx;
	private static final int windows_size=5;


	public ExtractedRelation(String relation_type, String linguisticPattern, String object, String subject, TextSequence context, int begin_idx, int end_idx) {
		this.relation_type = relation_type;
		this.linguisticPattern = linguisticPattern;
		this.object = object;
		this.subject = subject;
		this.context = context;
		this.begin_idx = (begin_idx-windows_size >= 0) ? begin_idx-windows_size : 0;
		this.end_idx = (end_idx + windows_size < context.getWords().size() ? end_idx +windows_size: context.getWords().size());
	}

	public ExtractedRelation(String relation_type, String linguisticPattern, String object, String subject) {
		super();
		this.relation_type = relation_type;
		this.linguisticPattern = linguisticPattern;
		this.object = object;
		this.subject = subject;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(relation_type);
		sb.append(" : ");
		sb.append(linguisticPattern);
		sb.append("(");
		sb.append(subject);
		sb.append(",");
		sb.append(object);
		sb.append(")");
		return sb.toString();
	}

	public String getContextAsStr(){
		StringBuilder sb=new StringBuilder("  -->    ");
		if(context != null){
			for(String word : context.getWords().subList(begin_idx,end_idx)) {
				sb.append(word+" ");
			}
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


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ExtractedRelation that = (ExtractedRelation) o;
		return Objects.equals(relation_type, that.relation_type) &&
				Objects.equals(linguisticPattern, that.linguisticPattern) &&
				Objects.equals(object, that.object) &&
				Objects.equals(subject, that.subject);
	}

	@Override
	public int hashCode() {

		return Objects.hash(relation_type, linguisticPattern, object, subject);
	}
}
