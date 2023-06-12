package com.exalead.cv360.searchui.mvc.controller.controllers;

import java.io.IOException;



import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.exalead.cv360.customcomponents.CustomComponent;
import com.exalead.cv360.searchui.mvc.controller.entities.User;
import com.exalead.cv360.searchui.mvc.controller.repositories.RequestRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.InteractionRepository;
import com.exalead.cv360.searchui.mvc.controller.repositories.UserRepository;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;

@CustomComponent(displayName = "User Controller")
@Controller
public class InteractionController {

	private InteractionRepository caseRepo = new InteractionRepository();
	

	
}
