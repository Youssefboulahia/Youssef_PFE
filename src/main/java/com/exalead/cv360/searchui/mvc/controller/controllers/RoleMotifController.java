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
import com.exalead.cv360.searchui.mvc.controller.exceptions.InvalidBooleanRepresentationException;
import com.exalead.cv360.searchui.mvc.controller.repositories.RoleMotifRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.MotifRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.RoleRepository;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;


@CustomComponent(displayName = "Filter Role Controller")
@Controller
public class RoleMotifController {

	private RoleMotifRepository filterRoleRepo = new RoleMotifRepository();
	private RoleRepository roleRepo = new RoleRepository();
	private MotifRepository motifRepo = new MotifRepository();
	

	@RequestMapping(value = "/getFilterRoles", method = RequestMethod.GET)
	public void getFilteRoles(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
        
		JSONObject resultat = new JSONObject();
		try {
			long startTime = System.currentTimeMillis();
			JSONObject resObj = filterRoleRepo.getAll(response);
			JSONArray result = resObj.getJSONArray("data");
			for(int i=0; i<result.length();i++) {
				JSONObject roleObj = roleRepo.read(result.getJSONObject(i).getInt("refRole"), response);
				JSONObject motifObj = motifRepo.read(result.getJSONObject(i).getInt("refMotif"), response);
				
				result.getJSONObject(i).put("refRole", roleObj);
				result.getJSONObject(i).put("refMotif", motifObj);
			}	
			long endTime = System.currentTimeMillis();
			
			resultat.put(ConstantsHolder.DATA, result);			
			resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
			resultat.put(ConstantsHolder.ROWCOUNT,resObj.getInt("rows"));
			
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
	
	@RequestMapping(value = "/updateFilterRole/{idRole}/{idMotif}", method = RequestMethod.PUT)
	public void updateFilterRole(ModelMap model,@PathVariable String idRole,@PathVariable String idMotif, @RequestParam(required = true, value = "status") String status, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws InvalidBooleanRepresentationException {
		JSONObject resultat = new JSONObject();

		try {   
			   int idR = Integer.parseInt(idRole); 
			   int idM = Integer.parseInt(idMotif); 
			   Boolean state = Boolean.parseBoolean(status);
			   if (!status.equals("true") && !status.equals("false")) {
		            throw new InvalidBooleanRepresentationException("Invalid boolean representation: " + state);
		        }
			   long startTime = System.currentTimeMillis();	
			   if(!roleRepo.idExists(idR, response)) {	
				   resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDROLENOTEXIST);
				   resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				   Utilities.sendResponse(response, resultat);
				   
			   } else if (!motifRepo.idExists(idM, response)) {
				   resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDMOTIFNOTEXIST);
				   resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				   Utilities.sendResponse(response, resultat);   
			   } else {
			       filterRoleRepo.updateFilterRole(idR, idM, state, response);
			       JSONObject updatedObj = filterRoleRepo.getRoleMotifByRef(idR, idM, response);
			       
			       JSONObject roleObj = roleRepo.read(updatedObj.getInt("refRole"), response);
				   JSONObject motifObj = motifRepo.read(updatedObj.getInt("refMotif"), response);
				   updatedObj.put("refRole", roleObj);
				   updatedObj.put("refMotif", motifObj);	   		
				   long endTime = System.currentTimeMillis();
				   
				   resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
			       resultat.put(ConstantsHolder.UPDATEDOBJ, updatedObj);
			       resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_UPDATEDOBJ);
			       resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
			       Utilities.sendResponse(response, resultat);
			   }
				
		}catch(SQLException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch(IllegalAccessException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch(NumberFormatException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch(InvalidBooleanRepresentationException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_BOOL);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (JSONException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} 
	}
	
	@RequestMapping(value = "/updateFilterRoles", method = RequestMethod.PUT)
	public void updateFilterRoles(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws InvalidBooleanRepresentationException {
		
		JSONObject resultat = new JSONObject();	
		JSONArray data = new JSONArray();
		JSONObject resultatObj = Utilities.generateJsonObj(request, response);
		long startTime = 0;
		long endTime = 0;
		// Loop through the array
        for (int i = 0; i < resultatObj.getJSONArray("data").length(); i++) {
            JSONObject jsonObject = resultatObj.getJSONArray("data").getJSONObject(i);

              
            try {   
 			   int idR = Integer.parseInt(jsonObject.getString("refRole")); 
 			   int idM = Integer.parseInt(jsonObject.getString("refMotif")); 
 			   Boolean state = Boolean.parseBoolean(jsonObject.getString("status"));
 			   if (!jsonObject.getString("status").equals("true") && !jsonObject.getString("status").equals("false")) {
 		            throw new InvalidBooleanRepresentationException("Invalid boolean representation: " + state);
 		        }
 			   startTime = System.currentTimeMillis();	
 			   if(!roleRepo.idExists(idR, response)) {	
 				   resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDROLENOTEXIST);
 				   resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
 				   Utilities.sendResponse(response, resultat);
 				   
 			   } else if (!motifRepo.idExists(idM, response)) {
 				   resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_IDMOTIFNOTEXIST);
 				   resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
 				   Utilities.sendResponse(response, resultat);   
 			   } else {
 			       filterRoleRepo.updateFilterRole(idR, idM, state, response);
 			       JSONObject updatedObj = filterRoleRepo.getRoleMotifByRef(idR, idM, response);
 			       
 			       JSONObject roleObj = roleRepo.read(updatedObj.getInt("refRole"), response);
 				   JSONObject motifObj = motifRepo.read(updatedObj.getInt("refMotif"), response);
 				   updatedObj.put("refRole", roleObj);
 				   updatedObj.put("refMotif", motifObj);	   		
 				   endTime = System.currentTimeMillis();
 				   
 				   data.put(updatedObj);

 			   }
 				
 		}catch(SQLException e) {
 			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
 			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
 			Utilities.sendResponse(response, resultat);
 		} catch(IllegalAccessException e) {
 			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_ACCESSFIELD);
 			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
 			Utilities.sendResponse(response, resultat);
 		} catch(NumberFormatException e) {
 			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_REFID);
 			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
 			Utilities.sendResponse(response, resultat);
 		} catch(InvalidBooleanRepresentationException e) {
 			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_BOOL);
 			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
 			Utilities.sendResponse(response, resultat);
 		} catch (JSONException e) {
 			resultat.put(ConstantsHolder.MESSAGE, e);
 			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
 			Utilities.sendResponse(response, resultat);
 		} 

        }
		   resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
		   resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_UPDATEDOBJ);
		   resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
		   resultat.put(ConstantsHolder.UPDATEDOBJS, data);
		   Utilities.sendResponse(response, resultat);
        

		
	}
	
}
