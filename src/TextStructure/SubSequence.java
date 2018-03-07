package TextStructure;

public class SubSequence {
	private int beginIdx;
	private int endIdx;
	
	public int getBeginIdx() {
		return beginIdx;
	}
	public int getEndIdx() {
		return endIdx;
	}
	public SubSequence(int beginIdx, int endIdx) {
		super();
		this.beginIdx = beginIdx;
		this.endIdx = endIdx;
	}
	@Override
	public String toString() {
		return "SubSequenceIndex [beginIdx=" + beginIdx + ", endIdx=" + endIdx + "]";
	}
	
	
	
	
}
