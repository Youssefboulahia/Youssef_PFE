package com.exalead.cv360.searchui.mvc.controller.utilities;

import java.io.BufferedReader;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class Utilities {
	
	public static String requestReaderToString(HttpServletRequest request) {
		StringBuilder jb = new StringBuilder();
		try {
			BufferedReader reader = request.getReader();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch (IOException e) {
			return "";
		}
		return jb.toString();
	}
	
	public static String hashPassword(String plainTextPassword){
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}
	
	public static void checkPassword(String plainPassword, String hashedPassword) {
		if (BCrypt.checkpw(plainPassword, hashedPassword))
			System.out.println("The password matches.");
		else
			System.out.println("The password does not match.");
	}
	
	public static JSONObject generateJsonObj(HttpServletRequest request, HttpServletResponse response) {
		try {
		String requestString = Utilities.requestReaderToString(request);
		JSONObject resultatObj = new JSONObject(requestString);
		return resultatObj;
		} catch (JSONException e) {
			JSONObject res = new JSONObject();
			res.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			res.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, res);
		}
		return null;
	}
	
	
	public static String eliminateSpecialCaracter(String value) {
		if (value != null) {
			return value.replaceAll("\t", " ").trim().replaceAll("\n", "").trim();
		}
		return "";
	}
	
	public static void sendResponse(HttpServletResponse response, JSONObject resultat) {
		response.setContentType(ConstantsHolder.APPLJSON);
        response.setCharacterEncoding(ConstantsHolder.UTF8);
        try {
			response.getWriter().print(resultat);
		} catch (IOException e) {
			System.out.println(ConstantsHolder.MSGERREUR_IOFAILED1);
		}
        try {
			response.getWriter().flush();
		} catch (IOException e) {
			System.out.println(ConstantsHolder.MSGERREUR_IOFAILED2);
		}
	}
	
	// return the created object after formating the received object
	public static JSONObject formattingJson(JSONObject jsonObject) {
		JSONObject resultat = new JSONObject();
		Iterator<String> keysIterator = jsonObject.keys();
		while (keysIterator.hasNext()) {
		    String key = keysIterator.next();
		    Object value = jsonObject.get(key);
		    resultat.put(key, eliminateSpecialCaracter(String.valueOf(value)));
		}
		return resultat;
	}
	
	//if the type in database is DATE then i need to work with java.sql.Date, if DATETIME i work with LocalDateTime
	public static LocalDateTime getCurrentDateTime() {
		LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        return localDateTime;
    }
	
	public static String getCurrentDateTimeForLogger() {
        LocalDateTime now = LocalDateTime.now();

        // Define the desired format with milliseconds
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // Format LocalDateTime to a string
        String formattedDateTime = now.format(formatter);

        return formattedDateTime;
	}

}
