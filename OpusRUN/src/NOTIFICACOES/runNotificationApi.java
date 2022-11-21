package NOTIFICACOES;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import dao.ContasReceberDAO;
import domain.AcessoCliente;
import domain.ContasReceber;
import domain.TokenClientes;
import antlr.Token;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class runNotificationApi {
	
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	
	public static void main(String []args) throws IOException{
	    
				OkHttpClient client = new OkHttpClient().newBuilder().build();
			    MediaType mediaType = MediaType.parse("application/json");
			    
			    EntityManager em = emf.createEntityManager();		    
			    Query q1 = em.createQuery("select c from ContasReceber c where "
			    		+ "c.data_pagamento =:d", ContasReceber.class);
			    q1.setParameter("d", new Date());
			    
			    List<ContasReceber> boletos = q1.getResultList();
			    
			    for (ContasReceber boleto : boletos) {
					
			    	Integer codContrato = Integer.parseInt(boleto.getN_doc().split("/")[0]);
			    	Query q = em.createQuery("select t from TokenClientes t where "
			    			+ "t.contrato=:c", TokenClientes.class);
			    	q.setParameter("c", codContrato);
			    	
			    	if(q.getResultList().size() == 1){
			    		TokenClientes token = (TokenClientes)q.getSingleResult();
			    		
			    		RequestBody body = RequestBody.create(mediaType, "{\r\n    "
					    		+ "\"registration_ids\":[\r\n        "
					    		+ "\""+token.getToken()+"\"\r\n    ],\r\n    "
					    		+ "\"notification\":{\r\n        "
					    		+ "\"body\":\"Seu boleto está se vencendo hoje.\",\r\n        "
					    		+ "\"title\":\"Aviso\""
					    		+ "\r\n    }\r\n}");
					   	    
					    Request request = new Request.Builder()
					      .url("https://fcm.googleapis.com/fcm/send")
					      .method("POST", body)
					      .addHeader("Content-Type", "application/json")
					      .addHeader("Authorization", 
					    		  "key=AAAAVVWrbx4:APA91bE_kRbeOl410EFgtHdvpOfS-ugjpz7NLM_AJBRSLC74zfd-DAvEOVCQgTTMXJcx4Wm2LRjprLBopLcAlSvpOckvsFFxpAmqdteN-FR_2XaK1JFRZhLGWvxqWpjD6H8VJ5eb_yUI")
					      .build();
					    Response response = client.newCall(request).execute();
					    
					    System.out.println(response.body().string());
			    	}
			    	
				} 
	    
	  }

}
