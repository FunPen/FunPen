package fr.funpen.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;


public class Network {
	private static Network 			instance = null;
	
	private Server					server;
	
	private Network() {
		server = Server.getInstance();
	}
	
	public static Network getInstance() {
		if (instance == null)
			instance = new Network();
		return instance;
	}
	
	public void loadServerFromSettings() {
		String ip = ""; //TODO load from phone
		int port = -1; //TODO load from phone
		String servicePage = ""; //TODO load from phone
		
		server.setIp(ip);
		server.setPort(port);
		server.setServicePage(servicePage);
	}
	
	public void sendRequest(RequestCallback callback) {
		//TODO send request to server
		String response = "";
		callback.onResponse(response);
	}
	
	private void postData() {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(server.getIp() + ":" + server.getPort() + "/" + server.getServicePage());

	    try {
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair("id", "12345"));
	        nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        HttpResponse response = httpclient.execute(httppost);
	        
	        Log.i("FunPen", "POST response: " + response.getStatusLine());

	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }
	} 
	
	/*********** Inner Callback **************/
	public interface RequestCallback {
		public void onResponse(String response);
	}
}
