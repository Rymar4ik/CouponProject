package util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

 public class StringDateConvertor {
	public static Date convert(String date) throws ParseException{
	SimpleDateFormat format = new SimpleDateFormat();
	format.applyPattern("YYYY-MM-DD");
	java.util.Date resultDate = format.parse(date);
	Date resSQLDate = new java.sql.Date(resultDate.getTime());
			
	return resSQLDate;
	}
}
