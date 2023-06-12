package com.exalead.cv360.searchui.mvc.controller.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exalead.cv360.searchui.mvc.controller.entities.Category;
import com.exalead.cv360.searchui.mvc.controller.jdbc.connector.Connector;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;

public class CategoryRepository extends Repository<Category> {
	
	public CategoryRepository() {
		super();
	}
	
	//Front office updated
	public JSONObject getCategoriesByRoleAndSubject(int roleId, int subjectId, HttpServletResponse response) throws SQLException {
		
		String sql = "select distinct category.id, category.name, category.refSubject, category.createdAt, category.updatedAt "
				+ "from role "
				+ "inner join rolemotif ON role.id = rolemotif.refRole "
				+ "inner join motif ON rolemotif.refMotif = motif.id "
				+ "inner join category ON motif.refCategory = category.id "
				+ "where rolemotif.status = 1 "
				+ "and role.id = "+roleId
				+ " and category.refSubject = "+subjectId;
		
		JSONArray resultArray = new JSONArray();
		JSONObject resultat = new JSONObject();
		int rowCount=0;
		Connection conn = Connector.createCon(this.username, this.password, response);
		Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);    
	    while(rs.next()){
	    	rowCount++;
	        //Display values
	        JSONObject resultObj = new JSONObject();
	        resultObj.put(ConstantsHolder.CATEGORY_ID, rs.getInt(ConstantsHolder.CATEGORY_ID));
	        resultObj.put(ConstantsHolder.CATEGORY_NAME, rs.getString(ConstantsHolder.CATEGORY_NAME));
	        resultObj.put(ConstantsHolder.CATEGORY_REFSUBJECT, rs.getInt(ConstantsHolder.CATEGORY_REFSUBJECT));
	        resultObj.put(ConstantsHolder.CATEGORY_CREATEDAT, rs.getTimestamp(ConstantsHolder.CATEGORY_CREATEDAT));
	        resultObj.put(ConstantsHolder.CATEGORY_UPDATEDAT, rs.getTimestamp(ConstantsHolder.CATEGORY_UPDATEDAT));
	        resultArray.put(resultObj);
	         }
	    
		resultat.put(ConstantsHolder.DATA, resultArray);
		resultat.put(ConstantsHolder.ROWCOUNT, rowCount);
		return resultat; 
	}
	
	public JSONObject getAllBySubject(int subjectId, HttpServletResponse response) throws SQLException {
		
		String sql = "select distinct category.id, category.name, category.refSubject, category.createdAt, category.updatedAt "
				+ "from category "
				+ "where category.refSubject = "+subjectId;
		
		JSONArray resultArray = new JSONArray();
		JSONObject resultat = new JSONObject();
		int rowCount=0;
		Connection conn = Connector.createCon(this.username, this.password, response);
		Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);    
	    while(rs.next()){
	    	rowCount++;
	        //Display values
	        JSONObject resultObj = new JSONObject();
	        resultObj.put(ConstantsHolder.CATEGORY_ID, rs.getInt(ConstantsHolder.CATEGORY_ID));
	        resultObj.put(ConstantsHolder.CATEGORY_NAME, rs.getString(ConstantsHolder.CATEGORY_NAME));
	        resultObj.put(ConstantsHolder.CATEGORY_REFSUBJECT, rs.getInt(ConstantsHolder.CATEGORY_REFSUBJECT));
	        resultObj.put(ConstantsHolder.CATEGORY_CREATEDAT, rs.getTimestamp(ConstantsHolder.CATEGORY_CREATEDAT));
	        resultObj.put(ConstantsHolder.CATEGORY_UPDATEDAT, rs.getTimestamp(ConstantsHolder.CATEGORY_UPDATEDAT));
	        resultArray.put(resultObj);
	         }
	    
		resultat.put(ConstantsHolder.DATA, resultArray);
		resultat.put(ConstantsHolder.ROWCOUNT, rowCount);
		return resultat; 
	}
	
	public JSONArray getMotifsById(int categoryId, HttpServletResponse response) throws SQLException {
		
		String sql = "select distinct motif.id, motif.name, motif.refCategory, motif.createdAt, motif.updatedAt "
				+ "from category "
				+ "inner join motif ON category.id = motif.refCategory "
				+ "where category.id = "+categoryId;
		
		JSONArray resultArray = new JSONArray();
		
		Connection conn = Connector.createCon(this.username, this.password, response);
		Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);
	    while(rs.next()){
	        JSONObject resultObj = new JSONObject();
	        resultObj.put(ConstantsHolder.MOTIF_ID, rs.getInt(ConstantsHolder.MOTIF_ID));
	        resultObj.put(ConstantsHolder.MOTIF_NAME, rs.getString(ConstantsHolder.MOTIF_NAME));
	        resultObj.put(ConstantsHolder.MOTIF_REFCATEGORY, rs.getInt(ConstantsHolder.MOTIF_REFCATEGORY));
	        resultObj.put(ConstantsHolder.MOTIF_CREATEDAT, rs.getTimestamp(ConstantsHolder.MOTIF_CREATEDAT));
	        if(rs.getTimestamp(ConstantsHolder.MOTIF_UPDATEDAT) != null) {
	        	resultObj.put(ConstantsHolder.MOTIF_UPDATEDAT, rs.getTimestamp(ConstantsHolder.MOTIF_UPDATEDAT));
	        }
	        resultArray.put(resultObj);
	         }
		return resultArray; 
	}
	
	public JSONArray getRoleMotifsById(int categoryId, HttpServletResponse response) throws SQLException {
		
		String sql = "select distinct rolemotif.id "
				+ "from category "
				+ "inner join motif ON category.id = motif.refCategory "
				+ "inner join rolemotif ON motif.id = rolemotif.refMotif "
				+ "where category.id = "+categoryId;
		
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
	
	public JSONArray getRequestsById(int categoryId, HttpServletResponse response) throws SQLException {
		
		String sql = "select distinct request.id, request.name, request.refInteraction, request.createdAt, request.updatedAt "
				+ "from category "
				+ "inner join motif ON category.id = motif.refCategory "
				+ "inner join request ON motif.id = request.refMotif "
				+ "where category.id = "+categoryId;
		
		JSONArray resultArray = new JSONArray();
		
		Connection conn = Connector.createCon(this.username, this.password, response);
		Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);
	    while(rs.next()){
	        JSONObject resultObj = new JSONObject();
	        resultObj.put(ConstantsHolder.REQUEST_ID, rs.getInt(ConstantsHolder.REQUEST_ID));
	        resultObj.put(ConstantsHolder.REQUEST_NAME, rs.getString(ConstantsHolder.REQUEST_NAME));
	        resultObj.put(ConstantsHolder.REQUEST_REFINTERACTION, rs.getInt(ConstantsHolder.REQUEST_REFINTERACTION));
	        resultObj.put(ConstantsHolder.REQUEST_CREATEDAT, rs.getTimestamp(ConstantsHolder.REQUEST_CREATEDAT));
	        if(rs.getTimestamp(ConstantsHolder.REQUEST_UPDATEDAT) != null) {
	        	resultObj.put(ConstantsHolder.REQUEST_UPDATEDAT, rs.getTimestamp(ConstantsHolder.REQUEST_UPDATEDAT));
	        }
	        
	        resultArray.put(resultObj);
	         }
		return resultArray; 
	}
	
	
	
	public Boolean idExists (int idC,  HttpServletResponse response) throws SQLException {
		Connection conn = Connector.createCon(this.username, this.password, response);
		String sql = "SELECT COUNT(*) FROM category WHERE id = ?";		
		PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, idC); 
        
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
