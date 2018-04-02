package Util;

public class Utils {

	public static long display_ellapsed_time(long tStart,String msg) {
		System.out.println(msg+"Time : "+(System.currentTimeMillis()-tStart) +"  ms")	;
		return System.currentTimeMillis();
	}
}
