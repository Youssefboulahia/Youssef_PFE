package com.exalead.cv360.searchui.mvc.controller.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exalead.cv360.searchui.mvc.controller.entities.Role;
import com.exalead.cv360.searchui.mvc.controller.jdbc.connector.Connector;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;

public class RoleRepository extends Repository<Role>{

	public RoleRepository() {
		super();
	}
	
	public JSONArray getRoleMotifsById(int roleId, HttpServletResponse response) throws SQLException {
		
		String sql = "select distinct rolemotif.id "
				+ "from role "
				+ "inner join rolemotif ON role.id = rolemotif.refRole "
				+ "where role.id = "+roleId;
		
		JSONArray resultArray = new JSONArray();
		
		Connection conn = Connector.createCon(this.username, this.password, response);
		Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);
	    while(rs.next()){
	        JSONObject resultObj = new JSONObject();
	        resultObj.put(ConstantsHolder.ROLE_MOTIF_ID, rs.getInt(ConstantsHolder.ROLE_MOTIF_ID));
	        resultArray.put(resultObj);
	         }
		return resultArray; 
	}
	
	public JSONArray getUsersById(int roleId, HttpServletResponse response) throws SQLException {
		
		String sql = "select distinct user.id "
				+ "from motif "
				+ "inner join user ON role.id = user.refRole "
				+ "where role.id = "+roleId;
		
		JSONArray resultArray = new JSONArray();
		
		Connection conn = Connector.createCon(this.username, this.password, response);
		Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);
	    while(rs.next()){
	        JSONObject resultObj = new JSONObject();
	        resultObj.put(ConstantsHolder.ROLE_ID, rs.getInt(ConstantsHolder.ROLE_ID));
	        
	        resultArray.put(resultObj);
	         }
		return resultArray; 
	}
	
	public Boolean idExists (int idR,  HttpServletResponse response) throws SQLException {
		Connection conn = Connector.createCon(this.username, this.password, response);
		String sql = "SELECT COUNT(*) FROM role WHERE id = ?";		
		PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, idR); 
        
        ResultSet resultSet = statement.executeQuery();
             if (resultSet.next()) {
                 int count = resultSet.getInt(1);
                 if (count > 0) {
                     return true;
                 } 
             }
			return false;
         }
}
