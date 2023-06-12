package com.exalead.cv360.searchui.mvc.controller.repositories;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import com.exalead.cv360.searchui.mvc.controller.entities.User;
import com.exalead.cv360.searchui.mvc.controller.jdbc.connector.Connector;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;
import com.exalead.cv360.searchui.mvc.controller.utilities.Logger;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class UserRepository {

	private final String username;
	private final String password;

    public UserRepository() {
    	this.username="root";
    	this.password="";
    }
	
    // lezemni table admin w table agent wa7adhom khater fel cas mtaa l'ajout bch ysir decalage fi exalead eli ygeneri erreur
	public int addUser(User user, HttpServletResponse response) {
		JSONObject resultat = new JSONObject();
		
		int generatedId = 0 ;
		try {
			String sql = "INSERT INTO user (login, password, displayname, isActive, isAdmin, refRole, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			Connection conn = Connector.createCon(this.username, this.password, response);
			// Convert LocalDateTime to Timestamp
	        Timestamp createdAt = Timestamp.valueOf(user.getCreatedAt());
	        Timestamp updatedAt = null;
	        // Generate a salt (a random string) to use during hashing
	        String salt = BCrypt.gensalt();
	        // Hash the password with the generated salt
	        String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
	        
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, user.getLogin());
			statement.setString(2, hashedPassword);
			statement.setString(3, user.getDisplayname());
			statement.setBoolean(4, user.getIsActive());
			statement.setBoolean(5, user.getIsAdmin());
			if(user.getIsAdmin()) {
				statement.setObject(6, null);	
			} else {
				statement.setInt(6, user.getRefRole());
			}		
			statement.setTimestamp(7, createdAt);
			statement.setTimestamp(8, updatedAt);
			 
			statement.executeUpdate();
	        ResultSet generatedKeys = statement.getGeneratedKeys();
	        if (generatedKeys.next()) {
	        generatedId = generatedKeys.getInt(1);
	        }
			conn.close();
			 
			  try {
	            // Load the XML file
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document document = builder.parse("/home/yboulahia/datadir/config/SecuritySources.xml");
	
	            // Find the specific SecuritySource element with name="users"
	            Element securitySourceElement;
	            if(user.getIsAdmin()) {
	            	securitySourceElement = findSecuritySourceElement(document, "admin");
	            } else {
	            	securitySourceElement = findSecuritySourceElement(document, "agent");
	            }
	            
	
	            if (securitySourceElement != null) {
	            	
	            	// Get the "Users" element within the "agents" security source
	                Element usersElement = null;
	                NodeList keyValueElements = securitySourceElement.getElementsByTagName("bee:KeyValue");
	                for (int i = 0; i < keyValueElements.getLength(); i++) {
	                    Element keyValueElement = (Element) keyValueElements.item(i);
	                    if (keyValueElement.getAttribute("key").equals("Users")) {
	                        usersElement = keyValueElement;
	                        break;
	                    }
	                }

	                if (usersElement != null) {
	                    // Get the list of "bee:KeyValue" elements within the "Users" element
	                    NodeList userElements = usersElement.getElementsByTagName("bee:KeyValue");

	                    // Find the last "bee:KeyValue" element in the list
	                    Element lastKeyValueElement = null;
	                    for (int i = userElements.getLength() - 1; i >= 0; i--) {
	                        Element keyValueElement = (Element) userElements.item(i);
	                        if (keyValueElement.hasAttribute("key") && !keyValueElement.hasAttribute("description") && !keyValueElement.hasAttribute("value") && !keyValueElement.hasAttribute("type")) {
	                            lastKeyValueElement = keyValueElement;
	                            break;
	                        }
	                    }

	                    // Determine the new key value by incrementing the value of the last "bee:KeyValue" element
	                    int newKeyValue = 0;
	                    if (lastKeyValueElement != null) {
	                        String lastKeyValue = lastKeyValueElement.getAttribute("key");
	                        try {
	                            newKeyValue = Integer.parseInt(lastKeyValue) + 1;
	                        } catch (NumberFormatException e) {
	                            // Handle the case where the last key value is not a valid integer
	                        }
	                    }

	                    // Create the new user element
	                    Element newUserElement = document.createElement("bee:KeyValue");
	                    newUserElement.setAttribute("key", String.valueOf(newKeyValue));
	
	                // Create the child elements for the user
	                createKeyValueElement(document, newUserElement, "Login", user.getLogin(),"string","");
	                createKeyValueElement(document, newUserElement, "Password", user.getPassword(),"string","");
	                createKeyValueElement(document, newUserElement, "DisplayName", user.getDisplayname(),"string","");
	                createKeyValueElement(document, newUserElement, "Tokens", "","","");
	
	                // Get the parent element where the new user should be added
	                Element usersElement1 = (Element) securitySourceElement.getElementsByTagName("bee:KeyValue").item(0);
	
	                // Append the new user element to the parent element
	                usersElement1.appendChild(newUserElement);
	
	                // Save the modified document back to the XML file
	                TransformerFactory transformerFactory = TransformerFactory.newInstance();
	                Transformer transformer = transformerFactory.newTransformer();
	                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	                DOMSource source = new DOMSource(document);
	                StreamResult result = new StreamResult(new File("/home/yboulahia/datadir/config/SecuritySources.xml"));
	                transformer.transform(source, result);
	                }
	                try {
	                            // Specify the command and arguments
	                            String[] command = {"/home/yboulahia/datadir/bin/cvcmd.sh", "applyConfig"};
	
	                            // Create the process builder
	                            ProcessBuilder processBuilder = new ProcessBuilder(command);
	
	                            // Run the command
	                            Process process = processBuilder.start();
	
	                            // Wait for the command to complete
	                            int exitCode = process.waitFor();
	
	                            if (exitCode == 0) {
	                                System.out.println("Command executed successfully.");
	                            } else {
	                                System.err.println("Command execution failed with exit code: " + exitCode);
	                            }
	                        } catch (IOException | InterruptedException e) {
	                            e.printStackTrace();
	                            generatedId=0;
	                        }
	                    	                		
	            } else {
	            	generatedId=0;
	                if (user.getIsAdmin()) {
	        			// log
	        			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SECURITYSOURCEADMIN);
	        			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SECURITYSOURCEADMIN);
	        			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
	        			Utilities.sendResponse(response, resultat);
	                } else {
	                	// log
	        			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SECURITYSOURCEAGENT);
	        			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SECURITYSOURCEAGENT);
	        			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
	        			Utilities.sendResponse(response, resultat);
	                }
	            }
	        } catch (ParserConfigurationException e) {
	        	// log
    			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_XMLPARSE);
	        	deleteByLogin(user.getLogin(),generatedId, response);
	        	generatedId=0;
				resultat.put(ConstantsHolder.MESSAGE, e);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
	        } catch (TransformerConfigurationException  e) {
	        	// log
    			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_XMLPARSE);
	        	deleteByLogin(user.getLogin(),generatedId, response);
	        	generatedId=0;
				resultat.put(ConstantsHolder.MESSAGE, e);
				resultat.put(ConstantsHolder.STATUS, e);
				Utilities.sendResponse(response, resultat);
	        } catch (IOException e) {
	        	// log
    			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_XMLPARSE);
	        	deleteByLogin(user.getLogin(),generatedId, response);
	        	generatedId=0;
				resultat.put(ConstantsHolder.MESSAGE, e);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
	        } catch (TransformerException e) {
	        	// log
    			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_XMLPARSE);
	        	deleteByLogin(user.getLogin(),generatedId, response);
	        	generatedId=0;
				resultat.put(ConstantsHolder.MESSAGE, e);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
	        } catch (SAXException e) {
	        	// log
    			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_XMLPARSE);
	        	deleteByLogin(user.getLogin(),generatedId, response);
	        	generatedId=0;
				resultat.put(ConstantsHolder.MESSAGE, e);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
	        } 		  
		if(user.getIsAdmin()) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, String.valueOf(generatedId), ConstantsHolder.LOG_INFO, "The admin with the login \""+user.getLogin()+"\" has been successfully created");
		} else {
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, String.valueOf(generatedId), ConstantsHolder.LOG_INFO, "The agent with the login \""+user.getLogin()+"\" has been successfully created");
		}
		
		} catch (SQLException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
			generatedId=0;
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		}
		return generatedId; 
		
	}
	
	public void userSetActiveState(String login, Boolean isActive , HttpServletResponse response) {
		// SQL update statement
        String sql = "UPDATE user SET isActive = ? WHERE login = ?";
        JSONObject resultat = new JSONObject();
        try {
			if(loginExists(login, response)) {
				try {
					Connection conn = Connector.createCon(this.username, this.password, response);
					PreparedStatement statement = conn.prepareStatement(sql);
		            // Set the updated values for the columns
					if(isActive) {
						statement.setInt(1, 1);
					} else {
						statement.setInt(1, 0);
					}		            
		            statement.setString(2, login);
		            
		            // Execute the update statement
		            int rowsAffected = statement.executeUpdate();
		            JSONObject obj = read(login , response);
		            if (rowsAffected > 0) {      	
		            	if(isActive) {
		        			// log
		        			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_DELETE, String.valueOf(obj.getInt(ConstantsHolder.USER_ID)), ConstantsHolder.LOG_INFO, "The user with the login \""+obj.getString(ConstantsHolder.USER_LOGIN)+"\" has been successfully set to active");
		        			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCES_USERSTATEACTIVE);
		        			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
		        			Utilities.sendResponse(response, resultat);
		            	} else {
		        			// log
		        			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_DELETE, String.valueOf(obj.getInt(ConstantsHolder.USER_ID)), ConstantsHolder.LOG_INFO, "The user with the login \""+obj.getString(ConstantsHolder.USER_LOGIN)+"\" has been successfully set to inactive");
		        			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCES_USERSTATEINACTIVE);
		        			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
		        			Utilities.sendResponse(response, resultat);
		            	}
		            } else {
	        			// log
	        			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_DELETE, String.valueOf(obj.getInt(ConstantsHolder.USER_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_USERUPDATEFAILS);
		    			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_USERUPDATEFAILS);
		    			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
		    			Utilities.sendResponse(response, resultat);
		            }
		        } catch (SQLException e) {
        			// log
        			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_DELETE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
					resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
					resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
					Utilities.sendResponse(response, resultat);
		        }
			} else {
    			// log
    			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_DELETE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_USERNOTEXISTS);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_USERNOTEXISTS);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			    Utilities.sendResponse(response, resultat);
			}
		} catch (SQLException e1) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_DELETE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
		    Utilities.sendResponse(response, resultat);
		}
        
	}
	
	
	
	 private static void createKeyValueElement(Document document, Element parentElement, String key, String value, String type, String description) {
	        Element keyValueElement = document.createElement("bee:KeyValue");
	        if(key.equals("Tokens")) {
		        keyValueElement.setAttribute("key", "Tokens");
		        keyValueElement.setAttribute("description", "");
		        parentElement.appendChild(keyValueElement);
	        } else {
		        keyValueElement.setAttribute("key", key);
		        keyValueElement.setAttribute("value", value);
		        keyValueElement.setAttribute("type", type);
		        keyValueElement.setAttribute("description", description);
		        parentElement.appendChild(keyValueElement);
	        }

	    }
	 
	    private static Element findSecuritySourceElement(Document document, String name) {
	        Element securitySourceListElement = document.getDocumentElement();
	        NodeList securitySourceElements = securitySourceListElement.getElementsByTagName("master:SecuritySource");

	        for (int i = 0; i < securitySourceElements.getLength(); i++) {
	            Element securitySourceElement = (Element) securitySourceElements.item(i);
	            if (securitySourceElement.getAttribute("name").equals(name)) {
	                return securitySourceElement;
	            }
	        }

	        return null;
	    }
	    
	    public void deleteByLogin(String login, int generatedId, HttpServletResponse response) {
	    		JSONObject resultat = new JSONObject();
	        	String query = "DELETE FROM user WHERE login = ?";
	        	Connection connection = Connector.createCon(this.username, this.password, response);
	        	try {
		        	PreparedStatement statement = connection.prepareStatement(query);
		            statement.setString(1, login);
		            statement.executeUpdate();
		            connection.close();
	        	} catch (SQLException e) {
	        		// log
	    			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_DELETE, String.valueOf(generatedId), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
	    			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
	    			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
	    			Utilities.sendResponse(response, resultat);
	        	}
	    }
	    
	    public void deleteById(int id, HttpServletResponse response) {
	    		JSONObject resultat = new JSONObject();
	        	String query = "DELETE FROM user WHERE id = ?";
	        	Connection connection = Connector.createCon(this.username, this.password, response);
	        	try {
		        	PreparedStatement statement = connection.prepareStatement(query);
		            statement.setInt(1, id);
		            statement.executeUpdate();
		            connection.close();
	        	} catch (SQLException e) {
	        		// log
	    			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, String.valueOf(id), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
	    			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
	    			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
	    			Utilities.sendResponse(response, resultat);
	        	}
	    }
	    
	    public JSONObject read(String login, HttpServletResponse response) throws SQLException, JSONException {
	    	
	    	String query = "SELECT * FROM user WHERE login = ?";	
	        JSONObject resultObj = new JSONObject();
	        	Connection connection = Connector.createCon(this.username, this.password, response);
	        	PreparedStatement statement = connection.prepareStatement(query);
	            statement.setString(1, login);
	            ResultSet resultSet = statement.executeQuery();
	            while (resultSet.next()) {
		        resultObj.put("id",resultSet.getInt("id"));
	            resultObj.put("login",resultSet.getString("login"));
	            resultObj.put("displayname",resultSet.getString("displayname"));
	            resultObj.put("isActive",resultSet.getInt("isActive")==1);
	            resultObj.put("isAdmin",resultSet.getInt("isAdmin")==1);
	            int refRole = resultSet.getInt("refRole");
	            Integer parsedRefRole = refRole != 0 ? refRole : null;
	            resultObj.put("refRole",parsedRefRole);
	            resultObj.put("createdAt",resultSet.getTimestamp("createdAt"));
	            Timestamp updatedAt = resultSet.getTimestamp("updatedAt");
	            if(updatedAt != null) {
	            	resultObj.put("updatedAt",updatedAt);
	            }
	            }
	            connection.close();
	         
	        
	        return resultObj; // Entity not found
	    }
	    
	    public JSONObject getAll(HttpServletResponse response) throws SQLException, JSONException {
	    	
	    	String query = "SELECT * FROM user";	
	        
	        	Connection connection = Connector.createCon(this.username, this.password, response);
	        	PreparedStatement statement = connection.prepareStatement(query);
	            ResultSet resultSet = statement.executeQuery();
	            int rowCount=0;
	            JSONObject res = new JSONObject();
	            JSONArray data = new JSONArray();
	            
	            while (resultSet.next()) {
	            	rowCount++;
	            	JSONObject resultObj = new JSONObject();
			        resultObj.put("id",resultSet.getInt("id"));
		            resultObj.put("login",resultSet.getString("login"));
		            resultObj.put("displayname",resultSet.getString("displayname"));
		            resultObj.put("isActive",resultSet.getInt("isActive")==1);
		            resultObj.put("isAdmin",resultSet.getInt("isAdmin")==1);
		            int refRole = resultSet.getInt("refRole");
		            Integer parsedRefRole = refRole != 0 ? refRole : null;
		            resultObj.put("refRole",parsedRefRole);
		            resultObj.put("createdAt",resultSet.getTimestamp("createdAt"));
		            Timestamp updatedAt = resultSet.getTimestamp("updatedAt");
		            if(updatedAt != null) {
		            	resultObj.put("updatedAt",updatedAt);
		            }
		            data.put(resultObj);
	            }
	            connection.close();
	         res.put(ConstantsHolder.ROWCOUNT, rowCount);
	         res.put(ConstantsHolder.DATA, data);
	        
	        return res; // Entity not found
	    }
	    
	    
		public Boolean loginExists (String login, HttpServletResponse response) throws SQLException {
			
			Connection conn = Connector.createCon(this.username, this.password, response);
			String sql = "SELECT COUNT(*) FROM user WHERE login = ?";	

			PreparedStatement statement = conn.prepareStatement(sql);
	        statement.setString(1, login); 
	        
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
