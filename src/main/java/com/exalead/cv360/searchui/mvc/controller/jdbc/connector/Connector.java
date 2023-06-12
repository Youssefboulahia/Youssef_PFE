package com.exalead.cv360.searchui.mvc.controller.jdbc.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;


public class Connector {

	
	static Connection con;
    public static Connection createCon(String username, String password, HttpServletResponse response){
        	
    		JSONObject resultat = new JSONObject();
        	try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_DRIVERFAILED);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			}
            
            String url ="jdbc:mysql://localhost:3306/call_center";
 
             try {
				con = DriverManager.getConnection(url,"root","");
			} catch (SQLException e) {
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_DBCONNECTIONFAIL);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			}
            
        return con;


    }
}
