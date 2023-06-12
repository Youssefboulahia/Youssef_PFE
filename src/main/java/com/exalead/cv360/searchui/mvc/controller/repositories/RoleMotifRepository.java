package com.exalead.cv360.searchui.mvc.controller.repositories;

import java.sql.Connection;

import java.sql.Statement;

import javax.servlet.http.HttpServletResponse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import com.exalead.cv360.searchui.mvc.controller.entities.RoleMotif;
import com.exalead.cv360.searchui.mvc.controller.jdbc.connector.Connector;

public class RoleMotifRepository extends Repository<RoleMotif> {

	public RoleMotifRepository() {
		super();
	}
	
	public void updateFilterRole(int idRole, int idMotif, Boolean status, HttpServletResponse response) throws SQLException {
			
			String sql = "UPDATE rolemotif "
					+ "SET status = "+status
					+ " WHERE refRole = "+idRole
					+ " and refMotif = "+idMotif;
			Connection conn = Connector.createCon(this.username, this.password, response);
			PreparedStatement statement = conn.prepareStatement(sql);	 
			statement.executeUpdate();
	
			conn.close();
			
		}
	
	public JSONObject getRoleMotifByRef(int idRole, int idMotif, HttpServletResponse response) throws SQLException {
		
		JSONObject resultObj = new JSONObject();
		String sql = "select * from rolemotif where "
				+ "refRole = "+idRole
				+ " and refMotif = "+idMotif;
		Connection conn = Connector.createCon(this.username, this.password, response);
		Statement statement = conn.createStatement();	 
		ResultSet rs = statement.executeQuery(sql);
	    while(rs.next()){
	        //Display values
	        resultObj.put("id", rs.getInt("id"));
	        resultObj.put("refRole", rs.getInt("refRole"));
	        resultObj.put("refMotif", rs.getInt("refMotif"));
	        resultObj.put("status", rs.getBoolean("status"));
	         }
	    conn.close();
	    return resultObj;
	 }
	
	public Boolean idExists (int idRM,  HttpServletResponse response) throws SQLException {
		Connection conn = Connector.createCon(this.username, this.password, response);
		String sql = "SELECT COUNT(*) FROM rolemotif WHERE id = ?";		
		PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, idRM); 
        
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
