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
import com.exalead.cv360.searchui.mvc.controller.entities.Subject;
import com.exalead.cv360.searchui.mvc.controller.repositories.CategoryRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.MotifRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.RequestRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.RoleMotifRepository;
import com.exalead.cv360.searchui.mvc.controller.utilities.CheckObjectKeys;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstructorBuilder;
import com.exalead.cv360.searchui.mvc.controller.utilities.Logger;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;

@CustomComponent(displayName = "Category Controller")
@Controller
public class CategoryController {

	private CategoryRepository categoryRepo = new CategoryRepository();
	private MotifRepository motifRepo = new MotifRepository();
	private RoleMotifRepository roleMotifRepo = new RoleMotifRepository();
	private RequestRepository requestRepo = new RequestRepository();
	
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public void addUserr(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
	
		
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
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} else {
			if(!CheckObjectKeys.checkJsonKeysCateogry(resultatObj)) {
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			} else {
				if(!CheckObjectKeys.checkJsonKeysCategoryAttributes(resultatObj)) {
					// log
					Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
					Utilities.sendResponse(response, resultat);
				} else {
					try {
						int refSubject = Integer.parseInt(Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.CATEGORY_REFSUBJECT))));
						Category category= new Category(
								Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.CATEGORY_NAME))),
								refSubject,
								Utilities.getCurrentDateTime());
						int generatedId = categoryRepo.create(category, response);
						long startTime = System.currentTimeMillis();			
						if (generatedId>=1) {
							JSONObject updatedObj = categoryRepo.read(generatedId, response);
							long endTime = System.currentTimeMillis();
							// log
							Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_ADD, String.valueOf(generatedId), ConstantsHolder.LOG_INFO, "The category with the name '"+Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.CATEGORY_NAME)))+"' has been successfully created");
							resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);			
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_CREATEDOBJ);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
							resultat.put(ConstantsHolder.CREATEDOBJ, updatedObj);
						    Utilities.sendResponse(response, resultat);
						   
						} else {
							// log
							Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_CREATEDOBJ);
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_CREATEDOBJ);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
							Utilities.sendResponse(response, resultat);
						}
					} catch(NumberFormatException e1) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_REFID);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
					} catch (JSONException e) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_JSONPARSING);
						resultat.put(ConstantsHolder.MESSAGE,ConstantsHolder.MSGERREUR_JSONPARSING);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_ACCESSFIELD);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
					} catch (SQLException e) {
						// log
						Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
						resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
						resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
						Utilities.sendResponse(response, resultat);
					}
				}
			}
		}
	}
	
	@RequestMapping(value = "/getCategories", method = RequestMethod.GET)
	public void getUsers(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
		
		JSONObject resultat = new JSONObject();
		
		try {
			long startTime = System.currentTimeMillis();
			JSONObject res= categoryRepo.getAll(response);
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
	
	@RequestMapping(value = "/getCategory/{id}", method = RequestMethod.GET)
	public void getUserById(ModelMap model,@PathVariable String id, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
		      
        JSONObject resultat = new JSONObject();	
		try {
			int idC = Integer.parseInt(String.valueOf((id)));
			long startTime = System.currentTimeMillis();
			if(categoryRepo.idExists(idC, response)) {	
				JSONObject res= categoryRepo.read(idC, response);
				long endTime = System.currentTimeMillis();
				resultat.put(ConstantsHolder.DATA, res);
				resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_GETOBJBYID);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
				Utilities.sendResponse(response, resultat);
			} else {
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDCATEGORYNOTEXIST);
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
	
	@RequestMapping(value = "/deleteCategory/{id}", method = RequestMethod.DELETE)
	public void deleteUser(ModelMap model,@PathVariable String id, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
        
        JSONObject resultat = new JSONObject();	
		try {
			int idC = Integer.parseInt(String.valueOf((id)));
			long startTime = System.currentTimeMillis();
			if(categoryRepo.idExists(idC, response)) {			
				JSONObject res= categoryRepo.read(idC, response);
				Boolean motifExist;
				Boolean requestExist;
				int motifCountRows=0;
				int requestCountRows=0;
				
				JSONArray motifList = categoryRepo.getMotifsById(idC, response);
				JSONArray roleMotifList = categoryRepo.getRoleMotifsById(idC, response);
				JSONArray requestList = categoryRepo.getRequestsById(idC, response);
				
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
				
				if(motifList.length()==0) {
					motifExist = false;
				} else {
					motifExist = true;
			        for (int i = 0; i < motifList.length(); i++) {
			             motifRepo.delete(motifList.getJSONObject(i).getInt(ConstantsHolder.MOTIF_ID), response); 
			             motifCountRows++;
			        }
			    }
				
				categoryRepo.delete(idC, response);	
				
				long endTime = System.currentTimeMillis();
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_DELETE, String.valueOf(idC), ConstantsHolder.LOG_INFO, "The category with the name '"+Utilities.eliminateSpecialCaracter(String.valueOf(res.get(ConstantsHolder.CATEGORY_NAME)))+"' has been successfully deleted");
				resultat.put(ConstantsHolder.DELETEDOBJ, res);
				if (motifExist && requestExist) {
					JSONObject resDeletedMotif = new JSONObject();
					resDeletedMotif.put(ConstantsHolder.DATA,motifList);
					resDeletedMotif.put(ConstantsHolder.ROWCOUNT,motifCountRows);
						
					JSONObject resDeletedRequests = new JSONObject();
					resDeletedRequests.put(ConstantsHolder.DATA,requestList);
					resDeletedRequests.put(ConstantsHolder.ROWCOUNT,requestCountRows);
							
					resultat.put(ConstantsHolder.DELETEDASSIGNEDMOT, resDeletedMotif);
					resultat.put(ConstantsHolder.DELETEDASSIGNEDREQ, resDeletedRequests);
					resultat.put(ConstantsHolder.DETAIL, ConstantsHolder.DELETECATEGORYWITHMOTIFANDREQ);
				} else if(motifExist) {
					JSONObject resDeletedMotif = new JSONObject();
					resDeletedMotif.put(ConstantsHolder.DATA,motifList);
					resDeletedMotif.put(ConstantsHolder.ROWCOUNT,motifCountRows);
					
					resultat.put(ConstantsHolder.DELETEDASSIGNEDMOT, resDeletedMotif);
					resultat.put(ConstantsHolder.DETAIL, ConstantsHolder.DELETECATEGORYWITHMOTIF);
				} else {
					resultat.put(ConstantsHolder.DETAIL, ConstantsHolder.DELETECATEGORY);
				}
				resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_DELETEDOBJ);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
				
				Utilities.sendResponse(response, resultat);
			} else {
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDCATEGORYNOTEXIST);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			}
			
		} catch(NumberFormatException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_DELETE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (JSONException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_DELETE, id, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (IllegalAccessException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_DELETE, id, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_ACCESSFIELD);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (SQLException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_DELETE, id, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		};
	}
	
	@RequestMapping(value = "/updateCategory", method = RequestMethod.PUT)
	public void updateUser(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {	
		
		JSONObject resultat = new JSONObject();
		JSONObject resultatObj = Utilities.generateJsonObj(request, response);
	
		if(resultatObj.keySet().isEmpty()) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_EMPTY);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} else {
			if(!CheckObjectKeys.checkJsonKeysCateogryUpdate(resultatObj)) {
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYS);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				Utilities.sendResponse(response, resultat);
			} else {
				if(!CheckObjectKeys.checkJsonKeysCategoryAttributesUpdate(resultatObj)) {
					// log
					Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_KEYSATTRIBUTES);
					resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
					Utilities.sendResponse(response, resultat);
				} else {
					try {
						int idC = Integer.parseInt(String.valueOf((resultatObj.get(ConstantsHolder.CATEGORY_ID))));

						Category category= ConstructorBuilder.categoryBuilder(resultatObj, idC);
						long startTime = System.currentTimeMillis();
						if (categoryRepo.idExists(idC, response)) {	
							JSONObject beforeUpdatedObj = categoryRepo.read(idC, response);
							categoryRepo.update(category, response);
							JSONObject updatedObj = categoryRepo.read(idC, response);
							long endTime = System.currentTimeMillis();
							JSONObject updatedValues = CheckObjectKeys.getUpdatedValues(beforeUpdatedObj,resultatObj);
							String message;
							if(updatedValues.keySet().isEmpty()) {
								message = "The category with the name '"+resultatObj.getString(ConstantsHolder.CATEGORY_NAME)+"' has been successfully updated without any changes";
							} else {
								message = "The category with the name '"+resultatObj.getString(ConstantsHolder.CATEGORY_NAME)+"' has been successfully updated, ";
								message = CheckObjectKeys.updateFieldStringGenerator(updatedValues, message);
							}
							// log
							Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_UPDATE, String.valueOf(idC), ConstantsHolder.LOG_INFO, ""+message+"");
							resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_UPDATEDOBJ);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
							resultat.put(ConstantsHolder.UPDATEDOBJ, updatedObj);
						    Utilities.sendResponse(response, resultat);
						   
						} else {
							resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDCATEGORYNOTEXIST);
							resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
							Utilities.sendResponse(response, resultat);
						}		
							}catch(NumberFormatException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_REFID);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (JSONException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_UPDATE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_JSONPARSING);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (IllegalAccessException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_UPDATE, String.valueOf(resultatObj.getInt(ConstantsHolder.SUBJECT_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_ACCESSFIELD);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (NoSuchFieldException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_UPDATE, String.valueOf(resultatObj.getInt(ConstantsHolder.SUBJECT_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_NOSUCHFIELD);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_NOSUCHFIELD);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							} catch (SQLException e) {
								// log
								Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_CATEGORY, ConstantsHolder.OPERATION_UPDATE, String.valueOf(resultatObj.getInt(ConstantsHolder.SUBJECT_ID)), ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_SQLFAILED);
								resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
								resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
								Utilities.sendResponse(response, resultat);
							}
						}
					}
				}
	}
	
	
	@RequestMapping(value = "/getCategoriesByRoleAndSubject", method = RequestMethod.GET)
	public void getCategoriesByRoleAndSubject(ModelMap model,@RequestParam(required = true, value = "refRole") String refRole,@RequestParam(required = true, value = "refSubject") String refSubject, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
	
		JSONObject resultat = new JSONObject();
		
		try {
			int idR = Integer.parseInt(refRole); 
			int idS = Integer.parseInt(refSubject); 
			long startTime = System.currentTimeMillis();
			JSONObject res= categoryRepo.getCategoriesByRoleAndSubject(idR, idS, response);
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
	
	@RequestMapping(value = "/getAllBySubject", method = RequestMethod.GET)
	public void getAllBySubject(ModelMap model,@RequestParam(required = true, value = "refSubject") String refSubject, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
	
		JSONObject resultat = new JSONObject();
		
		try {
			int idS = Integer.parseInt(refSubject); 
			long startTime = System.currentTimeMillis();
			JSONObject res= categoryRepo.getAllBySubject(idS, response);
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
