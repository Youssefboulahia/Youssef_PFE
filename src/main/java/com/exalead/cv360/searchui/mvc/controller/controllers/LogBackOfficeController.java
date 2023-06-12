package com.exalead.cv360.searchui.mvc.controller.controllers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
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
import com.exalead.cv360.searchui.mvc.controller.utilities.ConstantsHolder;
import com.exalead.cv360.searchui.mvc.controller.utilities.Utilities;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

@CustomComponent(displayName = "Log Controller")
@Controller
public class LogBackOfficeController {
	@RequestMapping(value = "/getLogs", method = RequestMethod.GET)
	public void addUserr(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		
		JSONObject resultat = new JSONObject();
		JSONArray data = new JSONArray();
		
		// log
		 String csvFilePath = ConstantsHolder.FILEPATH_BACKOFFICE;
		 int rowNbr = 0;
		 try {
	            // Create a CSVReader instance
	            @SuppressWarnings("deprecation")
				CSVReader csvReader = new CSVReader(new FileReader(csvFilePath), ';');

	            // Read the header row to get column names
	            String[] headerRow = csvReader.readNext();

	            // Process data rows
	            String[] nextRow;
	                       
	            while ((nextRow = csvReader.readNext()) != null) {
	            	JSONObject obj = new JSONObject();
	            	System.out.println(nextRow.toString());
	            	System.out.println(headerRow.toString());
	                for (int i = 0; i < headerRow.length; i++) {
	                    String columnName = headerRow[i];
	                    String columnValue = nextRow[i];
	                    obj.put(columnName, columnValue);
	                }
	                data.put(obj);
	                rowNbr++;
	            }

	            // Close the CSV reader
	            csvReader.close();
	            
	    		resultat.put(ConstantsHolder.ROWCOUNT, rowNbr);
	    		resultat.put(ConstantsHolder.DATA, data);
	    		resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_LOGS);
	    		resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
	    		Utilities.sendResponse(response, resultat);
	    		
	        } catch (IOException e) {
	    		resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_CSVFILE);
	    		resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
	    		Utilities.sendResponse(response, resultat);
	        }
	}
	
	@RequestMapping(value = "/deleteLogs", method = RequestMethod.DELETE)
	public void deleteLogs(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		
		JSONObject resultat = new JSONObject();
		JSONArray data = new JSONArray();
		
		// log
		 String csvFilePath = ConstantsHolder.FILEPATH_BACKOFFICE;
		 int rowNbr = 0;
		 try {
			// Create a CSVReader instance
			 @SuppressWarnings("deprecation")
			CSVReader csvReader = new CSVReader(new FileReader(csvFilePath), ';');

			 // Read the header row
			 String[] headerRow = csvReader.readNext();

			 // Create a new CSVWriter instance for writing the updated file
			 CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath + ".tmp"), ';', CSVWriter.NO_QUOTE_CHARACTER,
				        CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			 // Write the header row to the new file
			 csvWriter.writeNext(headerRow);

			 // Read and skip the remaining rows
			 String[] nextRow;
			 while ((nextRow = csvReader.readNext()) != null) {
			     // Skip the row
			 }

			 // Close the CSV reader and writer
			 csvReader.close();
			 csvWriter.close();

			 // Delete the original file
			 File originalFile = new File(csvFilePath);
			 originalFile.delete();

			 // Rename the temporary file to the original file name
			 File tempFile = new File(csvFilePath + ".tmp");
			 tempFile.renameTo(originalFile);
	            
	    		resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGSUCCESS_LOGSDELETE);
	    		resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_SUCCESS);
	    		Utilities.sendResponse(response, resultat);
	    		
	        } catch (IOException e) {
	    		resultat.put(ConstantsHolder.MESSAGE, ConstantsHolder.MSGERREUR_CSVFILE);
	    		resultat.put(ConstantsHolder.STATUS, ConstantsHolder.STATUS_ERREUR);
	        }
	}
}

