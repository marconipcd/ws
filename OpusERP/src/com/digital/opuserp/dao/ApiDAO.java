package com.digital.opuserp.dao;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiDAO {

	public static String end = "http://localhost:81/";
	public static String REQUEST_POST = "POST";
	public static String REQUEST_GET = "GET";
	
	public static String execute(String funcao, String[] parametros, String type_request){
		
		StringBuilder sb = new StringBuilder();
		for (String param : parametros) {
			sb.append(param);			
		}
		
		try{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
			      .url(ApiDAO.end+funcao+sb.toString())
			      .method(type_request, body)
			      .build();
			
			Response response = client.newCall(request).execute();
			String r = response.body().string();
			
			return r;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
