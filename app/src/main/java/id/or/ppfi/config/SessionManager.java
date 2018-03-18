package id.or.ppfi.config;

import java.util.HashMap;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import id.or.ppfi.user.LoginActivity;

@SuppressLint("CommitPrefEdits")public class SessionManager {
	// Shared Preferences
		static SharedPreferences pref;		
		// Editor for Shared preferences
		Editor editor;		
		// Context
		Context _context;		
		// Shared pref mode
		int PRIVATE_MODE = 0;		
		// url to create new product	  
		// Sharedpref file name
		private static final String PREF_NAME = "AndroidHivePref";		
		// All Shared Preferences Keys
		private static final String IS_LOGIN = "IsLoggedIn";
		private static final String IS_ATHLETE = "isAthlete";
	// User name (make variable public to access from outside)
		public static final String KEY_USERNAME = "username";
		//public static final String KEY_GROUP_USERTYPE = "master_licence_user_groupcode";
		public static final String KEY_GROUP_USERGRP = "group_id";
		public static final String KEY_ROLE_NAME = "role_name";
		public static final String KEY_USER_FULLNAME = "name";
		public static final String KEY_ACCESS = "access_all";
		public static final String KEY_COVER = "cover_url";
		public static final String KEY_GROUP_ID = "master_group_id";
		public static final String KEY_GROUP_NAME = "master_group_name";
		public static final String KEY_ROLE_TYPE = "master_licence_user_groupcode";

		public static final String KEY_URL_PROFILE = "gambar";

		public static final String KEY_QTY_CIDERA = "total_cidera";
		public static final String KEY_QTY_GROUP = "total_group";
		public static final String KEY_APP_UPDATE = "app_version_update";

		public static final String KEY_TOTAL_ATLET = "total_atlet_sum";

		public static final String KEY_PHONE = "phone";
		public static final String KEY_EMAIL = "email";

		public static final String KEY_WEEKLY = "weekly";

		public static final String KEY_WELLNESS_DATE = "wellness_date";

		///GENERATE DATA WELLNESS PERSENTAGE
		public static final String KEY_WELLNESS_GREY = "wellness_grey";
		public static final String KEY_WELLNESS_RED = "wellness_red";
		public static final String KEY_WELLNESS_ORANGE = "wellness_orange";
		public static final String KEY_WELLNESS_YELLOW = "wellness_yellow";
		public static final String KEY_WELLNESS_GREEN = "wellness_green";
		public static final String KEY_WELLNESS_GREEN_ARROW = "wellness_green_arrow";

		public static final String KEY_WELLNESS_LAST_UPDATE = "wellness_last_update";

		public static final String KEY_WELLNESS_CIDERA = "total_cidera";
		
		// Constructor
		public SessionManager(Context context){
			this._context = context;
			pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
			editor = pref.edit();
		}
		
		/**
		 * Create login session
		 * */
		public void CreateSessionKode(String username){
			// Storing login value as TRUE
			editor.putBoolean(IS_LOGIN, true);
			
			// Storing name in pref
			editor.putString(KEY_USERNAME, username);
			
			// commit changes
			editor.commit();
		}	
		
		/**
		 * Get stored session data
		 * */
		public HashMap<String, String> GetSessionKode(){
			HashMap<String, String> user = new HashMap<String, String>();
			// user name
			user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

			// return user
			return user;
		}
		
		/**
		 * Create login session
		 * */
		public void CreateSessionUserFullName(String fullname){
			// Storing login value as TRUE
			editor.putBoolean(IS_LOGIN, true);
			
			// Storing name in pref
			editor.putString(KEY_USER_FULLNAME, fullname);
			
			// commit changes
			editor.commit();
		}	
		
		/**
		 * Get stored session data
		 * */
		public HashMap<String, String> GetSessionUserFullName(){
			HashMap<String, String> userName = new HashMap<String, String>();
			// user name
			userName.put(KEY_USER_FULLNAME, pref.getString(KEY_USER_FULLNAME, null));
			
			// return user
			return userName;
		}
		

		/**
		 * Create login session
		 * */
		public void CreateSessionGroupGRP(String grp){
			// Storing login value as TRUE
			editor.putBoolean(IS_LOGIN, true);
			
			// Storing name in pref
			editor.putString(KEY_GROUP_USERGRP, grp);
			
			// commit changes
			editor.commit();
		}	
		
		/**
		 * Get stored session data
		 * */
		public HashMap<String, String> GetSessionGroupGRP(){
			HashMap<String, String> groupC = new HashMap<String, String>();
			// user name
			groupC.put(KEY_GROUP_USERGRP, pref.getString(KEY_GROUP_USERGRP, null));
			
			// return user
			return groupC;
		}
		
		/**
		 * Create login session
		 * */
		public void CreateSessionAccess(String acc){
			// Storing login value as TRUE
			editor.putBoolean(IS_LOGIN, true);
			
			// Storing name in pref
			editor.putString(KEY_ACCESS, acc);
			
			// commit changes
			editor.commit();
		}	
		
		/**
		 * Get stored session data
		 * */
		public HashMap<String, String> GetSessionAccess(){
			HashMap<String, String> acc = new HashMap<String, String>();
			// user name
			acc.put(KEY_ACCESS, pref.getString(KEY_ACCESS, null));
			
			// return user
			return acc;
		}

	/**
	 * Create login session
	 * */
	public void CreateSessionCover(String acc){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_COVER, acc);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionCover(){
		HashMap<String, String> cvr = new HashMap<String, String>();
		// user name
		cvr.put(KEY_COVER, pref.getString(KEY_COVER, null));

		// return user
		return cvr;
	}
		
		public void checkLogin(){
			// Check login status
			if(!SessionManager.isLoggedIn()){
				// user is not logged in redirect him to Login Activity
				Intent i = new Intent(_context, LoginActivity.class);
				// Closing all the Activities
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				// Add new Flag to start new Activity
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				// Staring Login Activity
				_context.startActivity(i);
			}/*else {
				splash = new Splash();
				splash.menu();
			}*/
		}
		
		public void logoutUser(){
			// Clearing all data from Shared Preferences
			editor.clear();
			editor.commit();
			
			// After logout redirect user to Loing Activity
			Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Staring Login Activity
			_context.startActivity(i);
		}
		
		public void Clear(){
			// Clearing all data from Shared Preferences
			editor.clear();
			editor.commit();
			
		}
		
		public static boolean isLoggedIn(){
			return pref.getBoolean(IS_LOGIN, false);
		}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(IS_LOGIN, isLoggedIn);

		// commit changes
		editor.commit();

	}

	public static boolean isAthleteIn(){
		return pref.getBoolean(IS_ATHLETE, false);
	}

	public void setAthleteLogin(boolean isAthleteIn) {

		editor.putBoolean(IS_ATHLETE, isAthleteIn);

		// commit changes
		editor.commit();

	}


	/**
	 * Create login session
	 * */
	public void CreateSessionUrlProfile(String urlP){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_URL_PROFILE, urlP);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionUrlProfile(){
		HashMap<String, String> url = new HashMap<String, String>();
		// user name
		url.put(KEY_URL_PROFILE, pref.getString(KEY_URL_PROFILE, null));

		// return user
		return url;
	}

	public HashMap<String, String> ClearSessionUrlProfile(){
		HashMap<String, String> url = new HashMap<String, String>();
		// user name
		url.clear();
		// return user
		return url;
	}


	/**
	 * Create login session
	 * */
	public void CreateSessionRoleName(String role){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_ROLE_NAME, role);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionRoleName(){
		HashMap<String, String> role = new HashMap<String, String>();
		// user name
		role.put(KEY_ROLE_NAME, pref.getString(KEY_ROLE_NAME, null));

		// return user
		return role;
	}


	/**
	 * Create login session
	 * */
	public void CreateSessionGroupID(String role){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_GROUP_ID, role);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionGroupID(){
		HashMap<String, String> role = new HashMap<String, String>();
		// user name
		role.put(KEY_GROUP_ID, pref.getString(KEY_GROUP_ID, null));

		// return user
		return role;
	}

	public HashMap<String, String> ClearSessionGroupID(){
		HashMap<String, String> groupid = new HashMap<String, String>();
		// user name
		groupid.get(KEY_GROUP_ID);
		groupid.clear();
		// return user
		return groupid;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionGroupName(String role){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_GROUP_NAME, role);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionGroupName(){
		HashMap<String, String> role = new HashMap<String, String>();
		// user name
		role.put(KEY_GROUP_NAME, pref.getString(KEY_GROUP_NAME, null));

		// return user
		return role;
	}


	public HashMap<String, String> ClearSessionGroupName(){
		HashMap<String, String> groupname = new HashMap<String, String>();
		// user name
		groupname.get(KEY_GROUP_NAME);
		groupname.clear();
		// return user
		return groupname;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionRoleType(String roletype){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_ROLE_TYPE, roletype);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionRoleType(){
		HashMap<String, String> roletype = new HashMap<String, String>();
		// user name
		roletype.put(KEY_ROLE_TYPE, pref.getString(KEY_ROLE_TYPE, null));

		// return user
		return roletype;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionPhone(String phone){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_PHONE, phone);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionPhone(){
		HashMap<String, String> phone = new HashMap<String, String>();
		// user name
		phone.put(KEY_PHONE, pref.getString(KEY_PHONE, null));

		// return user
		return phone;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionEmail(String email){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_EMAIL, email);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionEmail(){
		HashMap<String, String> roletype = new HashMap<String, String>();
		// user name
		roletype.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

		// return user
		return roletype;
	}

	public void CreateSessionQtyCidera(String qtyC){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_QTY_CIDERA, qtyC);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionQtyCidera(){
		HashMap<String, String> url = new HashMap<String, String>();
		// user name
		url.put(KEY_QTY_CIDERA, pref.getString(KEY_QTY_CIDERA, null));

		// return user
		return url;
	}

	public void CreateSessionQtyGroupPrima(String x){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_QTY_GROUP, x);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionQtyGroupPrima(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.put(KEY_QTY_GROUP, pref.getString(KEY_QTY_GROUP, null));

		// return user
		return x;
	}

	public void CreateSessionTotalAtlet(String total){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_TOTAL_ATLET, total);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionTotalAtlet(){
		HashMap<String, String> url = new HashMap<String, String>();
		// user name
		url.put(KEY_TOTAL_ATLET, pref.getString(KEY_TOTAL_ATLET, null));

		// return user
		return url;
	}

	////

	public void CreateSessionNewVersionApp(String appUpdate){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_APP_UPDATE, appUpdate);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionNewVersionApp(){
		HashMap<String, String> url = new HashMap<String, String>();
		// user name
		url.put(KEY_APP_UPDATE, pref.getString(KEY_APP_UPDATE, null));

		// return user
		return url;
	}


	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWeekly(){
		HashMap<String, String> weekly = new HashMap<String, String>();
		// user name
		weekly.put(KEY_WEEKLY, pref.getString(KEY_WEEKLY, null));

		// return user
		return weekly;
	}

	public void CreateSessionWeekly(String weekly){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WEEKLY, weekly);

		// commit changes
		editor.commit();
	}


	public void CreateSessionWellnessDate(String qtyC){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WELLNESS_DATE, qtyC);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWellnessDate(){
		HashMap<String, String> url = new HashMap<String, String>();
		// user name
		url.put(KEY_WELLNESS_DATE, pref.getString(KEY_WELLNESS_DATE, null));

		// return user
		return url;
	}


	////GENERATE WELLNESS PERCENTAGE
	/**
	 * Create login session
	 * */
	public void CreateSessionWellnessGrey(String x){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WELLNESS_GREY, x);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWellnessGrey(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.put(KEY_WELLNESS_GREY, pref.getString(KEY_WELLNESS_GREY, null));

		// return user
		return x;
	}

	public HashMap<String, String> ClearSessionWellnessGrey(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.clear();
		// return user
		return x;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionWellnessRed(String x){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WELLNESS_RED, x);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWellnessRed(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.put(KEY_WELLNESS_RED, pref.getString(KEY_WELLNESS_RED, null));

		// return user
		return x;
	}

	public HashMap<String, String> ClearSessionWellnessRed(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.clear();
		// return user
		return x;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionWellnessOrange(String x){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WELLNESS_ORANGE, x);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWellnessOrange(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.put(KEY_WELLNESS_ORANGE, pref.getString(KEY_WELLNESS_ORANGE, null));

		// return user
		return x;
	}

	public HashMap<String, String> ClearSessionWellnessOrange(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.clear();
		// return user
		return x;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionWellnessYellow(String x){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WELLNESS_YELLOW, x);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWellnessYellow(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.put(KEY_WELLNESS_YELLOW, pref.getString(KEY_WELLNESS_YELLOW, null));

		// return user
		return x;
	}

	public HashMap<String, String> ClearSessionWellnessYellow(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.clear();
		// return user
		return x;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionWellnessGreen(String x){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WELLNESS_GREEN, x);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWellnessGreen(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.put(KEY_WELLNESS_GREEN, pref.getString(KEY_WELLNESS_GREEN, null));

		// return user
		return x;
	}

	public HashMap<String, String> ClearSessionWellnessGreen(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.clear();
		// return user
		return x;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionWellnessGreenArrow(String x){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WELLNESS_GREEN_ARROW, x);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWellnessGreenArrow(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.put(KEY_WELLNESS_GREEN_ARROW, pref.getString(KEY_WELLNESS_GREEN_ARROW, null));

		// return user
		return x;
	}

	public HashMap<String, String> ClearSessionWellnessGreenArrow(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.clear();
		// return user
		return x;
	}


	/**
	 * Create login session
	 * */
	public void CreateSessionWellnessLastUpdate(String x){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WELLNESS_LAST_UPDATE, x);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWellnessLastUpdate(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.put(KEY_WELLNESS_LAST_UPDATE, pref.getString(KEY_WELLNESS_LAST_UPDATE, null));

		// return user
		return x;
	}

	public HashMap<String, String> ClearSessionWellnessLastUpdate(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.clear();
		// return user
		return x;
	}

	/**
	 * Create login session
	 * */
	public void CreateSessionWellnessCidera(String x){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_WELLNESS_CIDERA, x);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> GetSessionWellnessCidera(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.put(KEY_WELLNESS_CIDERA, pref.getString(KEY_WELLNESS_CIDERA, null));

		// return user
		return x;
	}

	public HashMap<String, String> ClearSessionWellnessCidera(){
		HashMap<String, String> x = new HashMap<String, String>();
		// user name
		x.clear();
		// return user
		return x;
	}


}