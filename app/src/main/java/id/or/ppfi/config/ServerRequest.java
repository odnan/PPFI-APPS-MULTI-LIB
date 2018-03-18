package id.or.ppfi.config;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ServerRequest {
	private final static String TAG = "ServerRequest";

	private String serverUri = "http://masterdata.awd.web.id/index.php";

	public static final String urlGetMember = "JsonMemberPPFI/Member";


	public static final String urlGetRoleName = "JsonRoleName/Username/";
	public static final String urlGroupMember = "JsonViewMember/GroupCode/";
	public static final String urlGroupAll = "JsonGetGroupAll/Group/";
	public static final String urlGroupSC = "JsonGroupSC/Username/";
	public static final String urlGroupWaka = "JsonGetGroupWaka/GroupCodeWaka/";
	public static final String urlProvinsi = "JsonGetProvinsi/Provinsi/";
	public static final String urlMealPlan = "JsonMealPlan/Data/";
	public static final String urlGroupByUser = "JsonGroupPrima/Username/";
	public static final String urlGroupAccessByUser = "JsonGroupPrima/GroupAccess/";
	public static final String urlCideraTodayByUser = "JsonViewCidera/Username/";
	public static final String urlCideraByUserGroup = "JsonViewCidera/GroupCidera/";


	public static final String urlListPeriodization = "JsonPeriodPlan/DataByUsername/";

	public static final String urlSpinnerAllGroup = "JsonGroupPrima/SpinnerAllGroup/";
	public static final String urlSpinnerSingleGroup = "JsonGroupPrima/SpinnerSingleGroup/";
	public static final String urlSpinnerGroupWaka2 = "JsonGroupPrima/SpinnerAllGroupWaka2/";
	public static final String urlSpinnerGroupWaka3 = "JsonGroupPrima/SpinnerAllGroupWaka3/";
	public static final String urlSpinnerGroupWaka4 = "JsonGroupPrima/SpinnerAllGroupWaka4/";

	public static final String urlWellnessRate = "JsonQtyWellness/WellnessPercentage/";
	public static final String urlGenerateWellnessRate = "JsonQtyWellness/GenerateWellnessSUM/";
	public static final String urlWellnessRateByParamRate = "JsonQtyWellness/WellnessPercentageRate/";
	public static final String urlWellnessRateByParamGroupRate = "JsonQtyWellness/WellnessPercentageRateByGroup/";

	public ServerRequest() {
		super();
	}

	private String getString(int url) {
		// TODO Auto-generated method stub
		return null;
	}

	/* Mengirimkan GET request */
	public String sendGetRequest(String reqUrl) {
		HttpClient httpClient;
		HttpGet httpGet = new HttpGet(serverUri + "/" + reqUrl);
		InputStream is = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);
			httpClient = new DefaultHttpClient(params);
			Log.d(TAG, "executing...");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			StatusLine status = httpResponse.getStatusLine();
			if (status.getStatusCode() == HttpStatus.SC_OK
					&& httpResponse != null) {
				/* mengambil response string dari server */
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line = null;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line + "\n");
				}
				is.close();
			}
		} catch (Exception e) {
//			Log.d(TAG, e.getMessage());
		}

		return stringBuilder.toString();
	}
	
	public int sendPostRequest(String url){
		int replyCode = 99;
		HttpClient httpClient;
		HttpPost post = new HttpPost(this.serverUri+"/"+url);
		List<NameValuePair> value = new ArrayList<NameValuePair>();
		/* menambahkan parameter ke dalam request */
		try {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);
			httpClient = new DefaultHttpClient(params);
			post.setEntity(new UrlEncodedFormEntity(value));
			Log.d(TAG, "executing post...");
			HttpResponse httpResponse = httpClient.execute(post);
			StatusLine status = httpResponse.getStatusLine();
			if(status.getStatusCode() == HttpStatus.SC_OK){
				Log.d(TAG, "submitted sucessfully...");
				replyCode = status.getStatusCode();
			}
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
		}
		return replyCode;
	}

}