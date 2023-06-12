package com.exalead.cv360.searchui.mvc.controller.controllers;

import java.io.IOException;


import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.exalead.cv360.customcomponents.CustomComponent;
import com.exalead.cv360.searchui.mvc.controller.entities.Motif;
import com.exalead.cv360.searchui.mvc.controller.entities.Role;
import com.exalead.cv360.searchui.mvc.controller.entities.Subject;
import com.exalead.cv360.searchui.mvc.controller.repositories.RequestRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.RoleMotifRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.RoleRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.UserRepository;
import com.exalead.cv360.searchui.mvc.controller.utilities.CheckObjectKeys;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;
import com.exalead.cv360.searchui.mvc.controller.utilities.Logger;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;

@CustomComponent(displayName = "Role Controller")
@Controller
public class RoleController {

	private RoleRepository roleRepo = new RoleRepository();
	private RoleMotifRepository roleMotifRepo = new RoleMotifRepository();
	private UserRepository userRepo = new UserRepository();
	
	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	public void addUserr(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		
		JSONObject resultat = new JSONObject();
		JSONObject resultatObj = Utilities.generateJsonObj(request, response);
		/*
		 1- check if the json object is empty
		 2- check if the keys are spelled correctly
		 3- check if all the required attributes have been sent
		 4- format the values of the json object
		 5- check if the object is created successfully and return the object
		 6- handle if there's an exception when parsing the JSON object
		 7- handle if there's an exception when accessing the class field
		 8- handle if there's an exception of the query if it doesn't go well
		 
		 9- handle IOException in sendResponse method in Utilities class if there's a raised exception or not
		 10- handle DB connection in JDBC Connector class
		 Every case treated generate a specific msg that shows the exact error
		 */
		if(resultatObj.keySet().isEmpty()) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} else {
			if(!CheckObjectKeys.checkJsonKeysRole(resultatObj)) {
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			} else {
				if(!CheckObjectKeys.checkJsonKeysRoleAttributes(resultatObj)) {
					// log
					Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
					Utilities.sendResponse(response, resultat);
				} else {
					try {
						Role role = new Role(
								Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.ROLE_NAME))),
								Utilities.getCurrentDateTime());
						long startTime = System.currentTimeMillis();	
						int generatedId = roleRepo.create(role, response);
						if (generatedId>=1) {
							JSONObject updatedObj = roleRepo.read(generatedId, response);
							long endTime = System.currentTimeMillis();
							// log
							Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_ADD, String.valueOf(generatedId), ConstantsHolder.LOG_INFO, "The role with the name '"+Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.ROLE_NAME)))+"' has been successfully created");
							resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_CREATEDOBJ);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
							resultat.put(ConstantsHolder.CREATEDOBJ, updatedObj);
						    Utilities.sendResponse(response, resultat);
						   
						} else {
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_CREATEDOBJ);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
							Utilities.sendResponse(response, resultat);
						}
					} catch (JSONException e) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_JSONPARSING);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_ACCESSFIELD);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
					} catch (SQLException e) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
					}
				}
			}
		}
	}
	@RequestMapping(value = "/getRoles", method = RequestMethod.GET)
	public void getUsers(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
		
		JSONObject resultat = new JSONObject();
		
		try {
			long startTime = System.currentTimeMillis();
			JSONObject res= roleRepo.getAll(response);
			long endTime = System.currentTimeMillis();
			resultat.put(ConstantsHolder.DATA, res.getJSONArray("data"));
			resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
			resultat.put(ConstantsHolder.ROWCOUNT,res.getInt("rows"));
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_GETALLOBJ);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
			Utilities.sendResponse(response, resultat);
		} catch (JSONException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (IllegalAccessException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (SQLException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		};
		
		
	}
	
	@RequestMapping(value = "/getRole/{id}", method = RequestMethod.GET)
	public void getUserById(ModelMap model,@PathVariable String id, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
        
        JSONObject resultat = new JSONObject();	
		try {
			int idR = Integer.parseInt(String.valueOf((id)));
			long startTime = System.currentTimeMillis();
			if(roleRepo.idExists(idR, response)) {
				JSONObject res= roleRepo.read(idR, response);
				long endTime = System.currentTimeMillis();
				resultat.put(ConstantsHolder.DATA, res);
				resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_GETOBJBYID);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
				Utilities.sendResponse(response, resultat);
			} else {
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDROLENOTEXIST);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			}
			
		} catch(NumberFormatException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (JSONException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (IllegalAccessException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (SQLException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		};
     
	}
	
	@RequestMapping(value = "/deleteRole/{id}", method = RequestMethod.DELETE)
	public void deleteUser(ModelMap model,@PathVariable String id, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
	
        JSONObject resultat = new JSONObject();	
		try {
			int idR = Integer.parseInt(String.valueOf((id)));
			long startTime = System.currentTimeMillis();
			if(roleRepo.idExists(idR, response)) {			
				JSONObject res= roleRepo.read(idR, response);
				Boolean userExist;
				int userCountRows=0;
				
				JSONArray roleMotifList = roleRepo.getRoleMotifsById(idR, response);
				JSONArray userList = roleRepo.getUsersById(idR, response);
				
				if(roleMotifList.length()>0) {
					for (int i = 0; i < roleMotifList.length(); i++) {
			             roleMotifRepo.delete(roleMotifList.getJSONObject(i).getInt(ConstantsHolder.ROLE_MOTIF_ID), response);       
			        	}
				}
				
				if(userList.length()==0) {
					userExist = false;
				} else {
					userExist = true;
			        for (int i = 0; i < userList.length(); i++) {
			             userRepo.deleteById(userList.getJSONObject(i).getInt(ConstantsHolder.MOTIF_ID), response); 
			             userCountRows++;
			        }
			    }
				
				roleRepo.delete(idR, response);	
				
				long endTime = System.currentTimeMillis();
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_DELETE, String.valueOf(idR), ConstantsHolder.LOG_INFO, "The role with the name '"+res.getString(ConstantsHolder.ROLE_NAME)+"' has been successfully deleted");
				resultat.put(ConstantsHolder.DELETEDOBJ, res);
				
				if(userExist) {
					JSONObject resDeletedUser = new JSONObject();
					resDeletedUser.put(ConstantsHolder.DATA,userList);
					resDeletedUser.put(ConstantsHolder.ROWCOUNT,userCountRows);
					
					resultat.put(ConstantsHolder.DELETEDASSIGNEDREQ, resDeletedUser);
					resultat.put(ConstantsHolder.DETAIL, ConstantsHolder.DELETEROLEWITHUSER);
				} else {
					resultat.put(ConstantsHolder.DETAIL, ConstantsHolder.DELETEROLE);
				}
				resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_DELETEDOBJ);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
				
				Utilities.sendResponse(response, resultat);
			} else {
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDMOTIFNOTEXIST);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			}
			
		} catch(NumberFormatException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_DELETE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (JSONException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_DELETE, id, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (IllegalAccessException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_DELETE, id, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_ACCESSFIELD);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (SQLException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_DELETE, id, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		};
	}
	
	
	@RequestMapping(value = "/updateRole", method = RequestMethod.PUT)
	public void updateUser(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {		
		
		JSONObject resultat = new JSONObject();
		JSONObject resultatObj = Utilities.generateJsonObj(request, response);
	
		if(resultatObj.keySet().isEmpty()) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} else {
			if(!CheckObjectKeys.checkJsonKeysRoleUpdate(resultatObj)) {
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			} else {
				if(!CheckObjectKeys.checkJsonKeysRoleAttributesUpdate(resultatObj)) {
					// log
					Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
					Utilities.sendResponse(response, resultat);
				} else {
					try {
						int idR = Integer.parseInt(String.valueOf((resultatObj.get(ConstantsHolder.ROLE_ID))));
						Role role= new Role(
								idR,
								Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.ROLE_NAME))),
								Utilities.getCurrentDateTime());
						
						long startTime = System.currentTimeMillis();
						if (roleRepo.idExists(idR, response)) {	
							JSONObject beforeUpdatedObj = roleRepo.read(idR, response);
							roleRepo.update(role, response);
							JSONObject updatedObj = roleRepo.read(idR, response);
							long endTime = System.currentTimeMillis();
							JSONObject updatedValues = CheckObjectKeys.getUpdatedValues(beforeUpdatedObj,resultatObj);
							String message;
							if(updatedValues.keySet().isEmpty()) {
								message = "The motif with the name '"+resultatObj.getString(ConstantsHolder.MOTIF_NAME)+"' has been successfully updated without any changes";
							} else {
								message = "The motif with the name '"+resultatObj.getString(ConstantsHolder.MOTIF_NAME)+"' has been successfully updated, ";
								message = CheckObjectKeys.updateFieldStringGenerator(updatedValues, message);
							}
							// log
							Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_UPDATE, String.valueOf(idR), ConstantsHolder.LOG_INFO, ""+message+"");
							resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_UPDATEDOBJ);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
							resultat.put(ConstantsHolder.UPDATEDOBJ, updatedObj);
						    Utilities.sendResponse(response, resultat);
						   
						} else {
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDROLENOTEXIST);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
							Utilities.sendResponse(response, resultat);
						}		
							}catch(NumberFormatException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_REFID);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (JSONException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_JSONPARSING);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (IllegalAccessException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_UPDATE, String.valueOf(resultatObj.getInt(ConstantsHolder.ROLE_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_ACCESSFIELD);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (NoSuchFieldException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_UPDATE, String.valueOf(resultatObj.getInt(ConstantsHolder.ROLE_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_NOSUCHFIELD);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_NOSUCHFIELD);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (SQLException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_ROLE, ConstantsHolder.OPERATION_UPDATE, String.valueOf(resultatObj.getInt(ConstantsHolder.ROLE_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							}
						}
					}
				}
	}

	
}
