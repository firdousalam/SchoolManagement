package com.kla.lms.klamp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kla.lms.klamp.entity.ResponseStatus;

@Component
public class Util {
	public static final Logger logger = LoggerFactory.getLogger(Util.class);

	private static final String DEFAULT_USER = "NA";
	private static final String DEFAULT_TIME_STAMP = "0000-00-00 00:00:00";

	private Util() {
		// default constructor
	}

	public static boolean isNullOrEmpty(String str) {
		boolean flag = true;
		if (str != null && !str.isEmpty())
			flag = false;
		return flag;
	}

	public static String getTimeStamp() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = Calendar.getInstance().getTime();
		return df.format(today);
	}
	
	public static String getDateOfEntry(String timestamp) {
		return String.format("%s/%s/%s", timestamp.substring(5, 7), timestamp.substring(8, 10), timestamp.substring(0, 4) );
	}

	public static String getDefaultTimestamp() {
		return DEFAULT_TIME_STAMP;
	}

	public static String getDefaultUser() {
		return DEFAULT_USER;
	}

	public static ResponseStatus setResponseStatus(int code, String message, String displayMessage) {
		ResponseStatus qrs = new ResponseStatus();
		qrs.setMessageCode(code);
		qrs.setMessage(message);
		qrs.setDisplayMessage(displayMessage);
		return qrs;
	}
	
	public static String cleanString(String str) {
		String result = str;
		try {
			result= URLDecoder.decode(str, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<?> convertObjectToList(Object obj) {
	    List<?> list = new ArrayList<>();
	    if (obj.getClass().isArray()) {
	        list = Arrays.asList((Object[])obj);
	    } else if (obj instanceof Collection) {
	        list = new ArrayList<>((Collection<?>)obj);
	    }
	    return list;
	}
}
