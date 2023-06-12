package com.exalead.cv360.searchui.mvc.controller.utilities;

import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class CheckObjectKeys {

	// Subject entity
	public static Boolean checkJsonKeysSubject(JSONObject jsonObject) {
		Iterator<String> keysIterator = jsonObject.keys();
		while (keysIterator.hasNext()) {
		    String key = keysIterator.next();
		    if(!(key.equals(ConstantsHolder.SUBJECT_NAME)) && !(key.equals(ConstantsHolder.SUBJECT_ID))) {
		    	return false;
		    }
		}
		return true;
	}
	
	public static boolean checkJsonKeysSubjectAttributes(JSONObject jsonObject) {
		if (!jsonObject.keySet().contains(ConstantsHolder.SUBJECT_NAME)) {
			return false;
		}
		return true;
	}
	
	public static Boolean checkJsonKeysSubjectUpdate(JSONObject jsonObject) {
		Iterator<String> keysIterator = jsonObject.keys();
		while (keysIterator.hasNext()) {
		    String key = keysIterator.next();
		    if(!(key.equals(ConstantsHolder.SUBJECT_NAME)) && !(key.equals(ConstantsHolder.SUBJECT_ID)) && !(key.equals(ConstantsHolder.SUBJECT_CREATEDAT)) && !(key.equals(ConstantsHolder.SUBJECT_UPDATEDAT))) {
		    	return false;
		    }
		}
		return true;
	}
	
	public static boolean checkJsonKeysSubjectAttributesUpdate(JSONObject jsonObject) {
		if (!jsonObject.keySet().contains(ConstantsHolder.SUBJECT_NAME) || !(jsonObject.keySet().contains(ConstantsHolder.SUBJECT_ID))) {
			return false;
		}
		return true;
	}

	//Category entity
	public static boolean checkJsonKeysCateogry(JSONObject jsonObject) {
		Iterator<String> keysIterator = jsonObject.keys();
		while (keysIterator.hasNext()) {
		    String key = keysIterator.next();
		    if(!(key.equals(ConstantsHolder.CATEGORY_NAME)) && !(key.equals(ConstantsHolder.CATEGORY_REFSUBJECT)) && !(key.equals(ConstantsHolder.CATEGORY_ID))) {
		    	return false;
		    }
		}
		return true;
	}
	
	public static boolean checkJsonKeysCategoryAttributes(JSONObject jsonObject) {
		if (!(jsonObject.keySet().contains(ConstantsHolder.CATEGORY_NAME)) || !(jsonObject.keySet().contains(ConstantsHolder.CATEGORY_REFSUBJECT)) ) {
			return false;
		}
		return true;
	}
	
	public static boolean checkJsonKeysCateogryUpdate(JSONObject jsonObject) {
		Iterator<String> keysIterator = jsonObject.keys();
		while (keysIterator.hasNext()) {
		    String key = keysIterator.next();
		    if(!(key.equals(ConstantsHolder.CATEGORY_NAME)) && !(key.equals(ConstantsHolder.CATEGORY_ID)) && !(key.equals(ConstantsHolder.CATEGORY_REFSUBJECT)) && !(key.equals(ConstantsHolder.CATEGORY_CREATEDAT)) && !(key.equals(ConstantsHolder.CATEGORY_UPDATEDAT))) {
		    	return false;
		    }
		}
		return true;
	}
	
	public static boolean checkJsonKeysCategoryAttributesUpdate(JSONObject jsonObject) {
		if (!(jsonObject.keySet().contains(ConstantsHolder.CATEGORY_NAME)) || !(jsonObject.keySet().contains(ConstantsHolder.CATEGORY_ID)) ) {
			return false;
		}
		return true;
	}
	
	//Motif entity
	public static boolean checkJsonKeysMotif(JSONObject jsonObject) {
		Iterator<String> keysIterator = jsonObject.keys();
		while (keysIterator.hasNext()) {
		    String key = keysIterator.next();
		    if(!(key.equals(ConstantsHolder.MOTIF_NAME)) && !(key.equals(ConstantsHolder.MOTIF_ID)) && !(key.equals(ConstantsHolder.MOTIF_REFCATEGORY))) {
		    	return false;
		    }
		}
		return true;
	}
	
	public static boolean checkJsonKeysMotifAttributes(JSONObject jsonObject) {
		if (!(jsonObject.keySet().contains(ConstantsHolder.MOTIF_NAME)) || !(jsonObject.keySet().contains(ConstantsHolder.MOTIF_REFCATEGORY)) ) {
			return false;
		}
		return true;
	}
	
	public static boolean checkJsonKeysMotifUpdate(JSONObject jsonObject) {
		Iterator<String> keysIterator = jsonObject.keys();
		while (keysIterator.hasNext()) {
		    String key = keysIterator.next();
		    if(!(key.equals(ConstantsHolder.MOTIF_NAME)) && !(key.equals(ConstantsHolder.MOTIF_ID)) && !(key.equals(ConstantsHolder.MOTIF_REFCATEGORY)) && !(key.equals(ConstantsHolder.MOTIF_CREATEDAT)) && !(key.equals(ConstantsHolder.MOTIF_UPDATEDAT))) {
		    	return false;
		    }
		}
		return true;
	}
	
	public static boolean checkJsonKeysMotifAttributesUpdate(JSONObject jsonObject) {
		if (!(jsonObject.keySet().contains(ConstantsHolder.MOTIF_NAME)) || !(jsonObject.keySet().contains(ConstantsHolder.MOTIF_ID)) ) {
			return false;
		}
		return true;
	}


	//Role entity
	public static boolean checkJsonKeysRole(JSONObject jsonObject) {
		Iterator<String> keysIterator = jsonObject.keys();
		while (keysIterator.hasNext()) {
		    String key = keysIterator.next();
		    if(!(key.equals(ConstantsHolder.ROLE_NAME)) && !(key.equals(ConstantsHolder.ROLE_ID))) {
		    	return false;
		    }
		}
		return true;
	}
	
	public static boolean checkJsonKeysRoleAttributes(JSONObject jsonObject) {
		if (!jsonObject.keySet().contains(ConstantsHolder.ROLE_NAME)) {
			return false;
		}
		return true;
	}
	
	public static boolean checkJsonKeysRoleUpdate(JSONObject jsonObject) {
		Iterator<String> keysIterator = jsonObject.keys();
		while (keysIterator.hasNext()) {
		    String key = keysIterator.next();
		    if(!(key.equals(ConstantsHolder.ROLE_NAME)) && !(key.equals(ConstantsHolder.ROLE_ID)) && !(key.equals(ConstantsHolder.ROLE_CREATEDAT)) && !(key.equals(ConstantsHolder.ROLE_UPDATEDAT))) {
		    	return false;
		    }
		}
		return true;
	}
	
	public static boolean checkJsonKeysRoleAttributesUpdate(JSONObject jsonObject) {
		if (!(jsonObject.keySet().contains(ConstantsHolder.ROLE_NAME)) || !(jsonObject.keySet().contains(ConstantsHolder.ROLE_ID)) ) {
			return false;
		}
		return true;
	}
	
	public static JSONObject getUpdatedValues(JSONObject obj1, JSONObject obj2) {
        JSONObject result = new JSONObject();

        // Get the iterator for the keys of obj1
        Iterator<String> iterator = obj1.keys();

        // Iterate through the keys
        while (iterator.hasNext()) {
            String key = iterator.next();
            // Check if obj2 contains the same key
            if (obj2.has(key)) {
                // Get the values for the current key in both JSONObjects
                Object value1 = obj1.get(key);
                Object value2 = obj2.get(key);
                // Compare the values and add them to the result if they are different
                if (!value1.equals(value2) && !key.equals("createdAt") && !key.equals("updatedAt")) {
                	JSONObject obj = new JSONObject();
                	obj.put(ConstantsHolder.BEFORE, value1);
                	obj.put(ConstantsHolder.AFTER, value2);
                    result.put(key, obj);
                }
            }
        }

        return result;
        
    }
	
	public static String updateFieldStringGenerator(JSONObject obj1, String str) {
        String result ="";

        // Get the iterator for the keys of obj1
        Iterator<String> iterator = obj1.keys();

        // Iterate through the keys
        while (iterator.hasNext()) {
            String key = iterator.next();
            JSONObject value1 = obj1.getJSONObject(key);
            if(result.equals("")) {
                result = result+"the field ["+key+"] has been updated from '"+value1.get(ConstantsHolder.BEFORE)+"' to '"+value1.get(ConstantsHolder.AFTER)+"'";
            } else {
                result = result+", the field ["+key+"] has been updated from '"+value1.get(ConstantsHolder.BEFORE)+"' to '"+value1.get(ConstantsHolder.AFTER)+"'";
            }
            
        }

        return str+result;
    }


}
