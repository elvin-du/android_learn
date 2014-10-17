package org.htz.eko.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class Http extends Thread{
	HttpClient httpClient;
	Http(){
		start();
	}

	public void run(){
		httpClient = new DefaultHttpClient();  
	}
	
	public HttpResponse Get(String url){
		HttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			response =  httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
}
