package id.or.ppfi.helper;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UpdateSetting {

	private static Context context;

	public UpdateSetting(Context context) {
		this.context = context;
	}
	
	
	public static String encryptPass(String pass) {
		try {
		    MessageDigest mdEnc;
			mdEnc = MessageDigest.getInstance("MD5");
		    mdEnc.update(pass.getBytes(), 0, pass.length());
		    String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
		    return md5;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
    
    public static int compareDate(String dateFormat, String date1, String date2) {
		Date firstDate = null, secondDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			firstDate = sdf.parse(date1);
			secondDate = sdf.parse(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
		return firstDate.compareTo(secondDate);
    }
	
    public static void errMsg(String msg) {
    	Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    
	public static void createFolder(String path){
		File f = new File(path);
    	
		try{
		
	    	if(f.exists()==false){
	            f.mkdirs();
	            System.out.println("Directory Created");
	        }
	        else{
	        	System.out.println("Directory is not created");
	        }
	    	
		}catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	}

    public static String getFormatDate(String oldFormat, String newFormat, String value) {
		SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
		Date d = null;
		try {
			d = sdf.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sdf.applyPattern(newFormat);
    	return sdf.format(d);
    }
    
    public static String getTwoDigit(int digit) {
    	if(digit < 10)
    		return "0" + digit;
		return Integer.toString(digit);
    }
    
    public static String getTodayFormat(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
    }
    
    public static Float getCurrentVersion() {
    	try {
			return Float.parseFloat(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return (float) -666.0;
    }    
    
    public Date JsonDateToDate(String jsonDate) {
        //  "/Date(1321867151710)/"
    	//ubah date format dari JSON ke format date yg bisa dibaca
        int idx1 = jsonDate.indexOf("(");
        int idx2 = jsonDate.indexOf(")");
        String s = jsonDate.substring(idx1+1, idx2);
        long l = Long.valueOf(s);
        return new Date(l);
    }
    
	public long getDiffTime(String dateServer, String dateDevice) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ppfi");

		Date d1 = null;
		Date d2 = null;
		long diff = 0;

		try {
			d1 = sdf.parse(dateServer);
			d2 = sdf.parse(dateDevice);

			//in milliseconds
			diff = d2.getTime() - d1.getTime();
			diff = Math.abs(diff / 1000);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(diff);
		
		return diff;
	}

	public static String addTime(int hour, int minute, int minutesToAdd, int month, int day, int year) {
        Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute);
        calendar.add(Calendar.MINUTE, minutesToAdd);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = sdf.format(calendar.getTime());
        return date;
    }
	
	public static String stripExtension (String str) {
        if (str == null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return str;

        return str.substring(0, pos);
    }
	
}
