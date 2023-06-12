package com.exalead.cv360.searchui.mvc.controller.controllers;


import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.exalead.cv360.customcomponents.CustomComponent;
import com.exalead.cv360.searchui.mvc.controller.entities.User;
import com.exalead.cv360.searchui.mvc.controller.exceptions.InvalidBooleanRepresentationException;
import com.exalead.cv360.searchui.mvc.controller.repositories.UserRepository;
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;
import com.exalead.cv360.searchui.mvc.controller.utilities.Logger;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;
import com.exalead.cv360.searchui.security.MashupSecurityManager;

@CustomComponent(displayName = "User Controller")
@Controller
public class UserController {

	private UserRepository userRepo = new UserRepository();
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public void addUserr(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
	
		JSONObject resultat = new JSONObject();
		JSONObject resultatObj = Utilities.generateJsonObj(request, response);
		User user= new User(
				String.valueOf(resultatObj.getString(ConstantsHolder.USER_LOGIN)),
				String.valueOf(resultatObj.getString(ConstantsHolder.USER_PASSWORD)),
				String.valueOf(resultatObj.getString(ConstantsHolder.USER_DISPLAYNAME)),
				resultatObj.getInt(ConstantsHolder.USER_ISACTIVE) == 1,
				resultatObj.getInt(ConstantsHolder.USER_ISADMIN) == 1,
				Integer.valueOf(String.valueOf(resultatObj.get(ConstantsHolder.USER_REFROLE))),
				Utilities.getCurrentDateTime());
			
		try {
			if(!userRepo.loginExists(resultatObj.getString(ConstantsHolder.USER_LOGIN), response)) {
				long startTime = System.currentTimeMillis();
				if(userRepo.addUser(user, response)>=1) {
					JSONObject createdObj = userRepo.read(user.getLogin(), response);
					long endTime = System.currentTimeMillis();
					resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
					resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_CREATEDOBJ);
					resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
					resultat.put(ConstantsHolder.CREATEDOBJ, createdObj);
				    Utilities.sendResponse(response, resultat);
				} else {
					resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
				    Utilities.sendResponse(response, resultat);
				}
			} else {
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, "Tryng to add user with an existed login \""+resultatObj.getString(ConstantsHolder.USER_LOGIN)+"\"");
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_USEREXISTS);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			    Utilities.sendResponse(response, resultat);
			} 
		} catch (JSONException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR,ConstantsHolder.MSGERREUR_JSONPARSING );
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
		    Utilities.sendResponse(response, resultat);
		} catch (SQLException e) {
			// log
			Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_ADD, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR,ConstantsHolder.MSGERREUR_SQLFAILED );
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
		    Utilities.sendResponse(response, resultat);
		}
	}
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.PUT)
	public void userSetActive(ModelMap model,@RequestParam(required = true, value = "login") String login, @RequestParam(required = true, value = "isActive") String isActive, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws InvalidBooleanRepresentationException {
		   Boolean activeState;
		   if (!isActive.equals("true") && !isActive.equals("false")) {
				// log
				Logger.writeLog(Utilities.getCurrentDateTimeForLogger(), "admin", ConstantsHolder.MODULE_USER, ConstantsHolder.OPERATION_DELETE, ConstantsHolder.NOID, ConstantsHolder.LOG_ERROR, ConstantsHolder.MSGERREUR_BOOL);
			    JSONObject resultat = new JSONObject();
				resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_BOOL);
				resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			    Utilities.sendResponse(response, resultat);
	        } else {
	 		   if(isActive.equals("true")) {
				   activeState = true;
			   } else {
				   activeState = false;
			   }
			   userRepo.userSetActiveState(login, activeState, response);
	        }
	}
	
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public void getUsers(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
		
		JSONObject resultat = new JSONObject();
		
		try {
			long startTime = System.currentTimeMillis();
			JSONObject res= userRepo.getAll(response);
			long endTime = System.currentTimeMillis();
			resultat.put(ConstantsHolder.DATA, res.getJSONArray(ConstantsHolder.DATA));
			resultat.put(ConstantsHolder.EXECUTIONTIME,endTime - startTime);
			resultat.put(ConstantsHolder.ROWCOUNT,res.getInt(ConstantsHolder.ROWCOUNT));
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_GETALLOBJ);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
			Utilities.sendResponse(response, resultat);
		} catch (JSONException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_JSONPARSING);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		} catch (SQLException e) {
			resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_SQLFAILED);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
			Utilities.sendResponse(response, resultat);
		};
	}
	
	@RequestMapping(value = "/getInstance", method = RequestMethod.GET)
	public void getInstanceAdmin(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		
		JSONObject resultat = new JSONObject();
		JSONObject obj = new JSONObject();
		String login = MashupSecurityManager.getSecurityModel(session).getLogin();
		try {
			obj = userRepo.read(login, response);
		} catch (SQLException e) {
			resultat.put(ConstantsHolder.DATA, e);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
		    Utilities.sendResponse(response, resultat);
		    System.out.println(e);
		}
		catch (JSONException e) {
			resultat.put(ConstantsHolder.DATA, e);
			resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
		    Utilities.sendResponse(response, resultat);
		    System.out.println(e);
		}
		resultat.put(ConstantsHolder.DATA, obj);
		resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
	    Utilities.sendResponse(response, resultat);
	   }
	
}
