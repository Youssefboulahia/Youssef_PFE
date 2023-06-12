package com.exalead.cv360.searchui.mvc.controller.utilities;

import java.util.Collection;

public class ConstantsHolder {

	public static final String USER_ID = "id";
	public static final String USER_LOGIN = "login";
	public static final String USER_PASSWORD = "password";
	public static final String USER_DISPLAYNAME = "displayname";
	public static final String USER_ISACTIVE = "isActive";
	public static final String USER_ISADMIN = "isAdmin";
	public static final String USER_REFROLE = "refRole";
	public static final String USER_CREATEDAT = "createdAt";
	public static final String USER_UPDATEDAT = "updatedAt";
	
	public static final String SUBJECT_ID = "id";
	public static final String SUBJECT_NAME = "name";
	public static final String SUBJECT_CREATEDAT = "createdAt";
	public static final String SUBJECT_UPDATEDAT = "updatedAt";
	
	public static final String CATEGORY_ID = "id";
	public static final String CATEGORY_NAME = "name";
	public static final String CATEGORY_REFSUBJECT = "refSubject";
	public static final String CATEGORY_CREATEDAT = "createdAt";
	public static final String CATEGORY_UPDATEDAT = "updatedAt";
	
	public static final String MOTIF_ID = "id";
	public static final String MOTIF_NAME = "name";
	public static final String MOTIF_REFCATEGORY = "refCategory";
	public static final String MOTIF_CREATEDAT = "createdAt";
	public static final String MOTIF_UPDATEDAT = "updatedAt";
	
	public static final String ROLE_MOTIF_ID = "id";
	public static final String ROLE_MOTIF_SATUS = "status";
	public static final String ROLE_MOTIF_REFROLE = "refRole";
	public static final String ROLE_MOTIF_REFMOTIF = "refMotif";
	public static final String ROLE_MOTIF_CREATEDAT = "createdAt";
	public static final String ROLE_MOTIF_UPDATEDAT = "updatedAt";
	
	public static final String ROLE_ID = "id";
	public static final String ROLE_NAME = "name";
	public static final String ROLE_CREATEDAT = "createdAt";
	public static final String ROLE_UPDATEDAT = "updatedAt";
	
	public static final String REQUEST_ID = "id";
	public static final String REQUEST_NAME = "name";
	public static final String REQUEST_REFINTERACTION = "refInteraction";
	public static final String REQUEST_CREATEDAT = "createdAt";
	public static final String REQUEST_UPDATEDAT = "updatedAt";

	public static final String ERROR = "ERROR : ";
	public static final String CONTENTTYPE = "Content-Type";
	public static final String APPLJSON = "application/json";
	public static final String UTF8 = "UTF-8"; 
	public static final String DEFAULT = "DEFAULT";

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final String MSGERREUR_KEYS = "Check JSON keys";
	public static final String MSGERREUR_KEYSATTRIBUTES = "Didn't recieve all of the required attributes";
	public static final String MSGERREUR_EMPTY = "Empty JSON";
	public static final String MSGERREUR_ACCESSFIELD = "Problem in accessing fields from generic class";
	public static final String MSGERREUR_SQLFAILED = "SQL query failed";
	public static final String MSGERREUR_DRIVERFAILED = "JDBC driver failed to load";
	public static final String MSGERREUR_DBCONNECTIONFAIL = "Database connection failed";
	public static final String MSGERREUR_IOFAILED1 = "Failed when writing data to the output stream of the HttpServletResponse object";
	public static final String MSGERREUR_IOFAILED2 = "Failed when flushing the output stream of the HttpServletResponse object";
	public static final String MSGERREUR_REFID = "Reference ID is not a valid Integer";
	public static final String MSGERREUR_JSONPARSING = "Error while parsing JSON object";
	public static final String MSGERREUR_CREATEDOBJ = "The object hasn't been created successfully";
	public static final String MSGERREUR_NOSUCHFIELD = "The class doesn't have the field you're trying to access";
	public static final String MSGERREUR_PASSEDID = "ID value is not valid";
	public static final String MSGERREUR_BOOL = "Recieved parameter is not a valid Boolean";
	public static final String MSGERREUR_IDSUBJECTNOTEXIST = "There's no existed subject with the passed ID";
	public static final String MSGERREUR_IDCATEGORYNOTEXIST = "There's no existed category with the passed ID";
	public static final String MSGERREUR_IDMOTIFNOTEXIST = "There's no existed motif with the passed ID";
	public static final String MSGERREUR_IDROLENOTEXIST = "There's no existed role with the passed ID";
	public static final String MSGERREUR_IDROLEMOTIFNOTEXIST = "There's no existed object with the passed ID";
	public static final String MSGERREUR_SECURITYSOURCEADMIN = "SecuritySource element with name='admin' not found";
	public static final String MSGERREUR_SECURITYSOURCEAGENT = "SecuritySource element with name='agent' not found";
	public static final String MSGERREUR_USEREXISTS = "Login exists";
	
	public static final String MSGSUCCESS_CREATEDOBJ = "The object is successfully created in the DataBase";
	public static final String MSGSUCCESS_UPDATEDOBJ = "The object is successfully updated in the DataBase";
	public static final String MSGSUCCESS_DELETEDOBJ = "The object is successfully deleted from the DataBase";
	public static final String MSGSUCCESS_GETOBJBYID = "The object is successfully retrieved from the DataBase";
	public static final String MSGSUCCESS_GETALLOBJ = "The objects are successfully retrieved from the DataBase";
	public static final String MSGSUCCESS_DELETE = "The objects are successfully deleted from the DataBase";;
	public static final String MSGSUCCES_ROLEMOTIF = "Successfully added rows in Role_Motif for the created motif";
	
	public static final String MESSAGE = "Message";
	public static final String STATUS_SUCCESS = "Success";
	public static final String STATUS_ERREUR = "Error";
	public static final String DATA = "Data";
	public static final String STATUS = "Status";
	public static final String DETAIL = "Detail";
	public static final String UPDATEDOBJ = "Updated object";
	public static final String CREATEDOBJ = "Created object";
	public static final String DELETEDOBJ = "Deleted object";
	public static final String EXECUTIONTIME = "Query execution time (ms)";
	public static final String ROWCOUNT = "Row counts";
	public static final String DELETEDASSIGNEDCAT = "Deleted assigned categories";
	public static final String DELETEDASSIGNEDMOT = "Deleted assigned motifs";
	public static final String DELETESUBJECTWITHCATANDMOTIF = "All the categories and motifs assigned to this subject have been deleted as well, This subject hasn't any assigned cases";
	public static final String DELETESUBJECTWITHCAT = "All the categories assigned to this subject have been deleted as well, This subject hasn't any assigned motifs";
	public static final String DELETESUBJECT = "This subject hasn't any assigned categories or motifs";
	public static final String DELETESUBJECTWITHCATANDMOTIFANDREQUEST = "All the categories, motifs and cases assigned to this subject have been deleted as well";
	public static final String DELETEDASSIGNEDREQ = "Deleted assigned cases";
	
	public static final String DELETECATEGORY = "This category hasn't any assigned motifs";
	public static final String DELETECATEGORYWITHMOTIFANDREQ = "All the motifs and cases assigned to this category have been deleted as well";
	public static final String DELETECATEGORYWITHMOTIF = "All the motifs assigned to this category have been deleted as well, This category hasn't any assigned cases";
	public static final String DELETEMOTIFWITHREQ = "All the cases assigned to this motif have been deleted as well";
	public static final String DELETEMOTIF = "This motif hasn't any assigned cases";
	public static final String DELETEROLEWITHUSER = "All the users assigned to this role have been deleted as well";
	public static final String DELETEROLE = "This role hasn't any assigned users";
	
	public static final String FILEPATH_BACKOFFICE = "/home/yboulahia/javasharedresources/logbackoffice.csv";
	public static final String MSGERREUR_CSVFILE = "An error occurred while reading the CSV file";
	public static final String LOG_INFO = "info";
	public static final String LOG_ERROR = "error";
	public static final String MODULE_USER = "User";
	public static final String MODULE_ROLE = "Role";
	public static final String MODULE_SUBJECT = "Subject";
	public static final String MODULE_CATEGORY = "Category";
	public static final String MODULE_MOTIF = "Motif";
	public static final String MODULE_CODIFICATION = "Codification";
	public static final String MODULE_INTERACTION = "Interaction";
	public static final String MODULE_CASE = "Case";
	public static final String OPERATION_ADD = "add";
	public static final String OPERATION_DELETE = "delete";
	public static final String OPERATION_UPDATE = "update";
	public static final String NOID = "-";
	public static final String MSGERREUR_XMLPARSE = "Failed attempt to add the user to SecuritySources.XML";
	public static final String MSGSUCCESS_LOGS = "Logs are successfully retrieved";
	public static final String MSGSUCCESS_LOGSDELETE = "Logs have been successfully deleted";
	public static final String MSGSUCCES_USERSTATEACTIVE = "The user has been successfully set to active";
	public static final String MSGSUCCES_USERSTATEINACTIVE = "The user has been successfully set to inactive";
	public static final String MSGERREUR_USERUPDATEFAILS = "Failed attempt to update the user";
	public static final String MSGERREUR_USERNOTEXISTS = "Login doesn't exist";
	public static final String BEFORE = "before";
	public static final String AFTER = "after";
	public static final String UPDATEDOBJS = "Updated Objects";


	
	
	
	
	

	
	
	

	


	private ConstantsHolder() {
		super();
	}
}
