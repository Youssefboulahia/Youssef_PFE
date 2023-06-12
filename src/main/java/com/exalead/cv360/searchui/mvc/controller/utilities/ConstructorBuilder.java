package com.exalead.cv360.searchui.mvc.controller.utilities;

import org.json.JSONObject;

import com.exalead.cv360.searchui.mvc.controller.entities.Category;
import com.exalead.cv360.searchui.mvc.controller.entities.Motif;


public class ConstructorBuilder {

	public static Category categoryBuilder(JSONObject resultatObj, int idC) throws NumberFormatException {
		if(resultatObj.has(ConstantsHolder.CATEGORY_REFSUBJECT)) {
			int refSubject = Integer.parseInt(String.valueOf((resultatObj.get(ConstantsHolder.CATEGORY_REFSUBJECT))));
			
			Category category= new Category(
					idC,
					Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.CATEGORY_NAME))),
					refSubject,
					Utilities.getCurrentDateTime());
			return category;
		} else {
			Category category= new Category(
					idC,
					Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.CATEGORY_NAME))),
					Utilities.getCurrentDateTime());
			return category;
		}	
	}
	
	public static Motif motifBuilder(JSONObject resultatObj, int idM) throws NumberFormatException {
		if(resultatObj.has(ConstantsHolder.MOTIF_REFCATEGORY)) {
			int refSubject = Integer.parseInt(String.valueOf((resultatObj.get(ConstantsHolder.MOTIF_REFCATEGORY))));
			
			Motif motif= new Motif(
					idM,
					Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.MOTIF_NAME))),
					refSubject,
					Utilities.getCurrentDateTime());
			return motif;
		} else {
			Motif motif= new Motif(
					idM,
					Utilities.eliminateSpecialCaracter(String.valueOf(resultatObj.get(ConstantsHolder.MOTIF_NAME))),
					Utilities.getCurrentDateTime());
			return motif;
		}	
	}
}
