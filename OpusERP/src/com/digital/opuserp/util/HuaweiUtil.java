package com.digital.opuserp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.digital.opuserp.domain.RadAcct;

public class HuaweiUtil {

	private static String servidor = "172.17.0.71";
	
	public static boolean desconectarCliente(String usuario) {
					
		try{
			
				EntityManager em = ConnUtil.getEntity();
				Query q = em.createQuery("select r from RadAcct r where r.username =:u and r.acctstoptime is null order by r.id desc", RadAcct.class).setMaxResults(1);
				q.setParameter("u", usuario);			
				
				RadAcct sessao = q.getSingleResult() != null ? (RadAcct)q.getSingleResult() : null;			
				String session = sessao != null ? sessao.getAcctsessionid() : null;
				
				if(session != null){
				
						 HttpURLConnection httpClient = (HttpURLConnection) new URL("http://"+servidor+"/ssh/index.php?funcao=desconectar&session="+session).openConnection();
						 httpClient.setRequestMethod("GET");
						 httpClient.setDoOutput(true);			        
				
				         try (BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {
				
				             String line;
				             StringBuilder response = new StringBuilder();
				
				             while ((line = in.readLine()) != null) {
				                 response.append(line);
				             }
				
				             System.out.println(response.toString());				
				         }
				}
		         
		         return true;
         
		}catch(Exception e){
			e.printStackTrace();
			
			return false;
		}
         
	}

	public static String[] pegarInformacoes(String usuario){
		
			String[] rrr = new String[]{};
			
			try{
		
				
				HttpURLConnection httpClient = (HttpURLConnection) new URL("http://"+servidor+"/ssh/index.php?funcao=informacoes&username="+usuario).openConnection();
				httpClient.setRequestMethod("GET");
				httpClient.setDoOutput(true);			        

		        try (BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {
		
		            String line;
		            StringBuilder response = new StringBuilder();
		
		            while ((line = in.readLine()) != null) {
		                response.append(line);
		            }
		            
		            String[] res = response.toString().split("<br />");
		            
		            String uptime = res[56].split(":")[3]+":"+res[56].split(":")[4]+":"+res[56].split(":")[5];
		            String mac = res[17].split(":")[1];
		            String ip = res[18].split(":")[1];
		            String upload = res[89].split(":")[1];
		            String download = res[94].split(":")[1];	
		            
		            rrr = new String[]{uptime,mac,ip,upload,download};
		        }   
		            
		            
	        }catch (Exception e) {
				e.printStackTrace();
			}
		        
			return rrr;
	}

}
