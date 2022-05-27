package com.digital.opuserp.dao;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TopSappDAO {
	
	private static String endereco 			= "https://186.233.104.165:9910/";
	private static String usuario 				= "topsapp";
	private static String senha 				= "mkcolmeia";
	private static String identificador	= "OPUS";

	public static void login(){
		
		try{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
				      .url(endereco+"Login?usuario="+usuario+"&senha="+senha+"&identificador="+identificador)
				      .method("POST", body)				     
				      .build();
			
			Response response = client.newCall(request).execute();
			System.out.println(response.body().string());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void cadastrarCliente(){
		
		try{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
				      .url(endereco+"CadastrarCliente?usuario="+usuario+"&senha="+senha+"&identificador="+identificador)
				      .method("POST", body)				      
				      .build();
			
			Response response = client.newCall(request).execute();
			System.out.println(response.body().string());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
