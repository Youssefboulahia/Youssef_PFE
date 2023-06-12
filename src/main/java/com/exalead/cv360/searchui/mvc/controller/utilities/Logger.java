package com.exalead.cv360.searchui.mvc.controller.utilities;

import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class Logger {
	
	public static void writeLog(String date, String login, String module, String operation, String idObj, String status, String message) {
		
		 String csvFilePath = ConstantsHolder.FILEPATH_BACKOFFICE;
			
		 try {
	            // Open the CSV file for writing
	            FileWriter fileWriter = new FileWriter(csvFilePath, true); // Append to existing file
	            @SuppressWarnings("deprecation")
				CSVWriter csvWriter = new CSVWriter(fileWriter,';');

	            String[] dataRow = {date, login, module, operation, idObj, status, message};
	            csvWriter.writeNext(dataRow);

	            // Close the CSV writer
	            csvWriter.close();

	            System.out.println("Data appended to CSV file successfully.");
	        } catch (IOException e) {
	            System.out.println("An error occurred while appending data to CSV file: " + e.getMessage());
	        }
	}

}
