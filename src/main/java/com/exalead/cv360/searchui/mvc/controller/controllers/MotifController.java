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
import org.springframework.web.bind.annotation.RequestParam;

import com.exalead.cv360.customcomponents.CustomComponent;
import com.exalead.cv360.searchui.mvc.controller.entities.Category;
import com.exalead.cv360.searchui.mvc.controller.entities.Motif;
import com.exalead.cv360.searchui.mvc.controller.entities.RoleMotif;
import com.exalead.cv360.searchui.mvc.controller.repositories.RoleMotifRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.MotifRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.RequestRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.RoleRepository;
import com.exalead.cv360.searchui.mvc.controller.utilities.CheckObjectKeys;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstructorBuilder;
import com.exalead.cv360.searchui.mvc.controller.utilities.Logger;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;

@CustomComponent(displayName = "Motif Controller")
@Controller
public class MotifController {

	private MotifRepository motifRepo = new MotifRepository();
	private RoleRepository roleRepository = new RoleRepository();
	private RoleMotifRepository roleMotifRepo = new RoleMotifRepository();
	private RequestRepository requestRepo = new RequestRepository();
	
	@RequestMapping(value = "/addMotif", method = RequestMethod.POST)
	public void addUserr(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
	
		int generatedId;	
		JSONObject resultat = new JSONObject();
		JSONObject resultatObj = Utilities.generateJsonObj(request, response);
		/*
		 1- check if the json object is empty
		 2- check if the keys are spelled correctly
		 3- check if all the required attributes have been sent
		 4- handle exception if refID is a valid integer or not
		 5- format the values of the json object
		 6- check if the object is created successfully and return the object
		 7- handle if there's an exception when parsing the JSON object
		 8- handle if there's an exception when accessing the class field
		 9- handle if there's an exception of the query if it doesn't go well
		 
		 10- handle IOException in sendResponse method in Utilities class if there's a raised exception or not
		 11- handle DB connection in JDBC Connector class
		 Every case treated generate a specific msg that shows the exact error
		 */
		if(resultatObj.keySet().isEmpty()) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} else {
			if(!CheckObjectKeys.checkJsonKeysMotif(resultatObj)) {
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			} else {
				if(!CheckObjectKeys.checkJsonKeysMotifAttributes(resultatObj)) {
					// log
					Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
					Utilities.sendResponse(response, resultat);
				} else {
					try {
						int refCategory = Integer.parseInt(Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.MOTIF_REFCATEGORY))));
						Motif motif= new Motif(
							Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.MOTIF_NAME))),
							refCategory,
							Utilities.getCurrentDateTime());				
						long startTime1 = System.currentTimeMillis();						
						generatedId = motifRepo.create(motif, response);
						long endTime1 = System.currentTimeMillis();
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_ADD, String.valueOf(generatedId), ConstantsHolder.LOG_INFO, "The motif with the name '"+Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.MOTIF_NAME)))+"' has been successfully created");
						if (generatedId>=1) {
							JSONObject updatedObj = motifRepo.read(generatedId, response);
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_CREATEDOBJ);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
							resultat.put(ConstantsHolder.CREATEDOBJ, updatedObj);
						    
						    // create rows for each role in role_motif table for the created motif
							long startTime2 = System.currentTimeMillis();
						    JSONArray listRoles = roleRepository.getAll(response).getJSONArray("data");
							for (int i=0; i < listRoles.length(); i++) {
								int roleId = listRoles.getJSONObject(i).getInt(ConstantsHolder.ROLE_ID);
								RoleMotif filterRole = new RoleMotif(
										false,
										roleId,
										generatedId,
										Utilities.getCurrentDateTime());
								roleMotifRepo.create(filterRole, response);
							}
							long endTime2 = System.currentTimeMillis();
							resultat.put(ConstantsHolder.EXECUTIONTIME,(endTime1 - startTime1) + (endTime2 - startTime2));	
							resultat.put(ConstantsHolder.DETAIL, ConstantsHolder.MSGSUCCES_ROLEMOTIF);
							Utilities.sendResponse(response, resultat);
						   
						} else {
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_CREATEDOBJ);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
							Utilities.sendResponse(response, resultat);
						}
					} catch(NumberFormatException e1) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_REFID);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
					} catch (JSONException e) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_JSONPARSING);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_ACCESSFIELD);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
					} catch (SQLException e) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
					}
				}
			}
		}
	}
	@RequestMapping(value = "/getMotifs", method = RequestMethod.GET)
	public void getUsers(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		
		JSONObject resultat = new JSONObject();
		
		try {
			long startTime = System.currentTimeMillis();
			JSONObject res= motifRepo.getAll(response);
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
	
	@RequestMapping(value = "/getMotif/{id}", method = RequestMethod.GET)
	public void getUserById(ModelMap model,@PathVariable String id, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
        
        JSONObject resultat = new JSONObject();	
		try {
			int idM = Integer.parseInt(String.valueOf((id)));
			long startTime = System.currentTimeMillis();
			if(motifRepo.idExists(idM, response)) {	
				JSONObject res= motifRepo.read(idM, response);
				long endTime = System.currentTimeMillis();
				resultat.put(ConstantsHolder.DATA, res);
				resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_GETOBJBYID);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
				Utilities.sendResponse(response, resultat);
			} else {
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDMOTIFNOTEXIST);
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
	
	@RequestMapping(value = "/deleteMotif/{id}", method = RequestMethod.DELETE)
	public void deleteUser(ModelMap model,@PathVariable String id, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
		
        JSONObject resultat = new JSONObject();	
		try {
			int idM = Integer.parseInt(String.valueOf((id)));
			long startTime = System.currentTimeMillis();
			if(motifRepo.idExists(idM, response)) {			
				JSONObject res= motifRepo.read(idM, response);
				Boolean requestExist;
				int requestCountRows=0;
				
				JSONArray roleMotifList = motifRepo.getRoleMotifsById(idM, response);
				JSONArray requestList = motifRepo.getRequestsById(idM, response);
				
				if(roleMotifList.length()>0) {
					for (int i = 0; i < roleMotifList.length(); i++) {
			             roleMotifRepo.delete(roleMotifList.getJSONObject(i).getInt(ConstantsHolder.ROLE_MOTIF_ID), response);       
			        	}
				}
				
				if(requestList.length()==0) {
					requestExist = false;
				} else {
					requestExist = true;
			        for (int i = 0; i < requestList.length(); i++) {
			             requestRepo.delete(requestList.getJSONObject(i).getInt(ConstantsHolder.MOTIF_ID), response); 
			             requestCountRows++;
			        }
			    }
				
				motifRepo.delete(idM, response);	
				
				long endTime = System.currentTimeMillis();
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_DELETE, String.valueOf(idM), ConstantsHolder.LOG_INFO, "The motif with the name '"+res.getString(ConstantsHolder.MOTIF_NAME)+"' has been successfully deleted");
				resultat.put(ConstantsHolder.DELETEDOBJ, res);
				
				if(requestExist) {
					JSONObject resDeletedREQ = new JSONObject();
					resDeletedREQ.put(ConstantsHolder.DATA,requestList);
					resDeletedREQ.put(ConstantsHolder.ROWCOUNT,requestCountRows);
					
					resultat.put(ConstantsHolder.DELETEDASSIGNEDREQ, resDeletedREQ);
					resultat.put(ConstantsHolder.DETAIL, ConstantsHolder.DELETEMOTIFWITHREQ);
				} else {
					resultat.put(ConstantsHolder.DETAIL, ConstantsHolder.DELETEMOTIF);
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
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_DELETE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (JSONException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_DELETE, id, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (IllegalAccessException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_DELETE, id, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_ACCESSFIELD);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (SQLException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_DELETE, id, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		};
	}
	
	@RequestMapping(value = "/updateMotif", method = RequestMethod.PUT)
	public void updateUser(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {	
		
		JSONObject resultat = new JSONObject();
		JSONObject resultatObj = Utilities.generateJsonObj(request, response);
	
		if(resultatObj.keySet().isEmpty()) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} else {
			if(!CheckObjectKeys.checkJsonKeysMotifUpdate(resultatObj)) {
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			} else {
				if(!CheckObjectKeys.checkJsonKeysMotifAttributesUpdate(resultatObj)) {
					// log
					Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
					Utilities.sendResponse(response, resultat);
				} else {
					try {
						int idM = Integer.parseInt(String.valueOf((resultatObj.get(ConstantsHolder.MOTIF_ID))));

						Motif motif = ConstructorBuilder.motifBuilder(resultatObj, idM);
						long startTime = System.currentTimeMillis();
						if (motifRepo.idExists(idM, response)) {
							JSONObject beforeUpdatedObj = motifRepo.read(idM, response);
							motifRepo.update(motif, response);
							JSONObject updatedObj = motifRepo.read(idM, response);
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
							Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_UPDATE, String.valueOf(idM), ConstantsHolder.LOG_INFO,""+message+"");
							resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_UPDATEDOBJ);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
							resultat.put(ConstantsHolder.UPDATEDOBJ, updatedObj);
						    Utilities.sendResponse(response, resultat);
						   
						} else {
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDMOTIFNOTEXIST);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
							Utilities.sendResponse(response, resultat);
						}		
							}catch(NumberFormatException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_REFID);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (JSONException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_JSONPARSING);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (IllegalAccessException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_UPDATE, String.valueOf(resultatObj.getInt(ConstantsHolder.MOTIF_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_ACCESSFIELD);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (NoSuchFieldException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_UPDATE, String.valueOf(resultatObj.getInt(ConstantsHolder.MOTIF_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_NOSUCHFIELD);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_NOSUCHFIELD);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (SQLException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_MOTIF, ConstantsHolder.OPERATION_UPDATE, String.valueOf(resultatObj.getInt(ConstantsHolder.MOTIF_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							}
						}
					}
				}
	}

	@RequestMapping(value = "/getMotifsByRoleAndCategory", method = RequestMethod.GET)
	public void getCategoriesByRoleAndCategory(ModelMap model,@RequestParam(required = true, value = "refRole") String refRole,@RequestParam(required = true, value = "refCategory") String refCategory, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
	
		JSONObject resultat = new JSONObject();
		
		try {
			int idR = Integer.parseInt(refRole); 
			int idC = Integer.parseInt(refCategory); 
			long startTime = System.currentTimeMillis();
			JSONObject res= motifRepo.getMotifsByRoleAndCategory(idR, idC, response);
			long endTime = System.currentTimeMillis();
			resultat.put(ConstantsHolder.DATA, res.getJSONArray(ConstantsHolder.DATA));
			resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
			resultat.put(ConstantsHolder.ROWCOUNT,res.getInt(ConstantsHolder.ROWCOUNT));
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_GETALLOBJ);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
			Utilities.sendResponse(response, resultat);
		} catch (SQLException e) {
			resultat.put(ConstantsHolder.MESSAGE, e);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch(NumberFormatException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (JSONException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		}
	}
	
	@RequestMapping(value = "/getAllByCategory", method = RequestMethod.GET)
	public void getAllByCategory(ModelMap model,@RequestParam(required = true, value = "refCategory") String refCategory, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
	
		JSONObject resultat = new JSONObject();
		
		try {
			int idC = Integer.parseInt(refCategory); 
			long startTime = System.currentTimeMillis();
			JSONObject res= motifRepo.getAllByCategory(idC, response);
			long endTime = System.currentTimeMillis();
			resultat.put(ConstantsHolder.DATA, res.getJSONArray(ConstantsHolder.DATA));
			resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
			resultat.put(ConstantsHolder.ROWCOUNT,res.getInt(ConstantsHolder.ROWCOUNT));
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_GETALLOBJ);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
			Utilities.sendResponse(response, resultat);
		} catch (SQLException e) {
			resultat.put(ConstantsHolder.MESSAGE, e);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch(NumberFormatException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (JSONException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		}
	}
	
}
