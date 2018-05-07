package Relation;

import TextStructure.TextSequence;
import Util.UtilColor;

import java.util.Objects;

public class ExtractedRelation{


	private RelationPattern relation_type;

	/** The linguistric pattern */
	private String linguisticPattern;
	private int ling_pattern_idx;

	private String x;

	private int x_idx,x_begin, x_end;
	private String y;
	private int y_idx,y_begin, y_end;

	/** The textSequence whose extraction was made */
	private TextSequence context;


	/** sub-context sc = context[context_begin,context_end] */
	private int context_begin,context_end;

	/** The windows size */
	private static final int windows_size=5;


	public ExtractedRelation(RelationPattern relation_type, String linguisticPattern, String x, String y, TextSequence context, int context_begin, int context_end) {
		this.relation_type = relation_type;
		this.linguisticPattern = linguisticPattern;
		this.x = x;
		this.y = y;
		this.context = context;
		this.context_begin = (context_begin -windows_size >= 0) ? context_begin -windows_size : 0;
		this.context_end = (context_end + windows_size < context.getWords().size() ? context_end +windows_size: context.getWords().size());
	}



	public ExtractedRelation(RelationPattern relation_type, String linguisticPattern, String x, String y) {
		super();
		this.relation_type = relation_type;
		this.linguisticPattern = linguisticPattern;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(relation_type.getRelation_name());
		sb.append(" : ");
		sb.append(linguisticPattern);
		sb.append("(");
		sb.append(x.substring(x_begin,x_end));
		sb.append(",");
		sb.append(y.substring(y_begin,y_end));
		sb.append(")");
		return sb.toString();
	}

	public TextSequence getContext() {
		return context;
	}

	public String getContextAsStr(){
		StringBuilder sb=new StringBuilder();
		if(context != null){
			for(String word : context.getWords().subList(context_begin, context_end)) {
				if(word.equals(x)){
					sb.append(UtilColor.ANSI_GREEN+word+UtilColor.ANSI_RESET);
				}
				else if(word.equals(y)){
					sb.append(UtilColor.ANSI_GREEN+word+UtilColor.ANSI_RESET);
				}
				else if(word.equals(linguisticPattern)){
					sb.append(UtilColor.ANSI_RED +word+UtilColor.ANSI_RESET);
				}
				else{
					sb.append(word);
				}
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	public void setX_begin(int x_begin) {
		this.x_begin = x_begin;
	}

	public void setX_end(int x_end) {
		this.x_end = x_end;
	}

	public void setY_begin(int y_begin) {
		this.y_begin = y_begin;
	}

	public void setY_end(int y_end) {
		this.y_end = y_end;
	}

	public RelationPattern getRelation_type() {
		return relation_type;
	}

	public String getLinguisticPattern() {
		return linguisticPattern;
	}

	public String getX() {
		return x;
	}

	public String getY() {
		return y;
	}

	public void setX(String x) {
		this.x = x;
	}

	public void setY(String y) {
		this.y = y;
	}

	public void setRelation_type(RelationPattern relation_type) {
		this.relation_type = relation_type;
	}

	public void setLinguisticPattern(String linguisticPattern) {
		this.linguisticPattern = linguisticPattern;
	}

	public void setLing_pattern_idx(int ling_pattern_idx) {
		this.ling_pattern_idx = ling_pattern_idx;
	}

	public void setX_idx(int x_idx) {
		this.x_idx = x_idx;
	}

	public void setY_idx(int y_idx) {
		this.y_idx = y_idx;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ExtractedRelation that = (ExtractedRelation) o;
		return Objects.equals(relation_type, that.relation_type) &&
				Objects.equals(linguisticPattern, that.linguisticPattern) &&
				Objects.equals(x, that.x) &&
				Objects.equals(y, that.y);
	}

	@Override
	public int hashCode() {

		return Objects.hash(relation_type, linguisticPattern, x, y);
	}
}
