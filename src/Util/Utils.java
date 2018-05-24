package Util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static long display_ellapsed_time(long tStart,String msg) {
		System.out.println(msg+"Time : "+(System.currentTimeMillis()-tStart) +"  ms")	;
		return System.currentTimeMillis();
	}
	
	public static String getCurrentTimeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
