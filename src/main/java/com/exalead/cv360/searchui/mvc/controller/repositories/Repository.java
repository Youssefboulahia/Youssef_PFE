package com.exalead.cv360.searchui.mvc.controller.repositories;

import java.lang.reflect.Field;

import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exalead.cv360.searchui.mvc.controller.jdbc.connector.Connector;

public class Repository<T> {
	protected final String username;
	protected final String password;
    protected final String tableName;

    public Repository() {
    	this.username="root";
    	this.password="";
        this.tableName = this.extractTableName();
    }

    public int create(T entity, HttpServletResponse response) throws IllegalAccessException, SQLException {
        List<String> columns = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        
        int generatedId = 0 ;

        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            String columnName = field.getName();
            Object value;
            field.setAccessible(true);
            value = field.get(entity);
            columns.add(columnName);
            placeholders.add("?");
            values.add(value);
        }

        String query = "INSERT INTO " + tableName + " (" + String.join(", ", columns) + ") VALUES (" + String.join(", ", placeholders) + ")";
        	Connection connection = Connector.createCon(this.username, this.password, response);
        	PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            setStatementParameters(statement, values);
            statement.executeUpdate();
            
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
            generatedId = generatedKeys.getInt(1);
            }
            connection.close();
     
        return generatedId;
    }

    public JSONObject read(int id, HttpServletResponse response) throws SQLException, IllegalAccessException {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        JSONObject resultObj = new JSONObject();
        	Connection connection = Connector.createCon(this.username, this.password, response);
        	PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<T> entities = mapResultSetToList(resultSet);
           
            if(!entities.isEmpty()) {
            Field[] fields = entities.get(0).getClass().getDeclaredFields();
            for (Field field : fields) {
                String columnName = field.getName();
                Object value;
                field.setAccessible(true);
                value = field.get(entities.get(0));
                resultObj.put(columnName, value);
                            
            }
            connection.close();
         }
        
        return resultObj; // Entity not found
    }

    public int update(T entity, HttpServletResponse response) throws IllegalAccessException, SQLException, NoSuchFieldException{
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        int generatedId = 0 ;

        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            String columnName = field.getName();
            Object value;
            field.setAccessible(true);
            value = field.get(entity);
            
            if (columnName.equals("id" ) || columnName.equals("createdAt" )) {
                continue;
            }
            
            if (value != null) {
            	columns.add(columnName + " = ?");
                values.add(value);
            }

        }

        String query = "UPDATE " + tableName + " SET " + String.join(", ", columns) + " WHERE id = ?";
        	Connection connection = Connector.createCon(this.username, this.password, response);
        	PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            setStatementParameters(statement, values);
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            statement.setObject(columns.size() + 1, idField.get(entity));
            statement.executeUpdate();
            
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
            generatedId = generatedKeys.getInt(1);
            }
            connection.close();
      return generatedId;
    }

    public void delete(int id, HttpServletResponse response) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE id = ?";
        	Connection connection = Connector.createCon(this.username, this.password, response);
        	PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.close();

    }

    public JSONObject getAll(HttpServletResponse response) throws IllegalAccessException, SQLException {
        String query = "SELECT * FROM " + tableName;
        JSONObject res = new JSONObject();
        JSONArray resultArray = new JSONArray();
        		Connection connection = Connector.createCon(this.username, this.password, response);	
        		Statement statement = connection.createStatement();
            	ResultSet resultSet = statement.executeQuery(query);
                List<T> entities = mapResultSetToList(resultSet);
                int rowCount = 0;
            	
                for(T entity : entities) {    	
                	rowCount++;
	                Field[] fields = entity.getClass().getDeclaredFields();
	                JSONObject resultObj = new JSONObject();
	                for (Field field : fields) {
	                    String columnName = field.getName();
	                    Object value;
	                    field.setAccessible(true);
	                    value = field.get(entity);
	                    resultObj.put(columnName, value);               
	                }
	                resultArray.put(resultObj);
              }
              connection.close();
              res.put("rows", rowCount);
              res.put("data", resultArray);
        return res;
    }
    
    
    ///////// Obj mapping utility methods //////////

    private void setStatementParameters(PreparedStatement statement, List<Object> values) throws SQLException {
        for (int i = 0; i < values.size(); i++) {
            statement.setObject(i + 1, values.get(i));
        }
    }
    
    private String extractTableName() {
        Class<?> entityClass = ((Class<?>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
        return entityClass.getSimpleName().toLowerCase();
    }
    
    
    // dynamically map the resultset based on the select query to an entity and append it to a list
    private List<T> mapResultSetToList(ResultSet resultSet) {
        List<T> entities = new ArrayList<>();
        try {
            while (resultSet.next()) {
                T entity = createEntityInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    setEntityFieldValue(entity, columnName, value);
                }
                entities.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to map ResultSet to entity list", e);
        }
        return entities;
    }
    

    //  dynamically creates a new instance of the entity class by invoking its parameterless constructor using reflection. It ensures that the created instance matches the generic type T through a type cast.
    @SuppressWarnings("unchecked")
	private T createEntityInstance() throws Exception {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return entityClass.cast(entityClass.getDeclaredConstructor().newInstance());
    }

    // dynamically set the value of a field of the given entity
    private void setEntityFieldValue(T entity, String columnName, Object value) throws Exception {
        Field field = getField(entity.getClass(), columnName);
        if (field != null) {
            field.setAccessible(true);
            field.set(entity, value);
        }
    }

    // return the field of the entity based on the column name
    private Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getField(superclass, fieldName);
            }
            return null;
        }
    }
}



