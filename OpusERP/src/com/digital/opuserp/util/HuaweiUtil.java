package com.digital.opuserp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
		            
		            String uptime = res.length > 55 ?  res[56].split(":")[3]+":"+res[56].split(":")[4]+":"+res[56].split(":")[5] : "";
		            String mac = res.length > 16 ? res[17].split(":")[1] : "";
		            String ip = res.length > 17 ? res[18].split(":")[1] : "";
		            String upload = res.length > 88 ? res[89].split(":")[1] : "";
		            String download = res.length > 93 ? res[94].split(":")[1] : "";	
		            
		            rrr = new String[]{uptime,mac,ip,upload,download};
		        }   
		            
		            
	        }catch (Exception e) {
				e.printStackTrace();
			}
		        
			return rrr;
	}

	
	public static List<String[]> pegarClientesConectados(){
		try{
			
			List<String[]> clientes = new ArrayList<String[]>();
			
			HttpURLConnection httpClient = (HttpURLConnection) new URL("http://"+servidor+"/ssh/conectados.php").openConnection();
			httpClient.setRequestMethod("GET");
			httpClient.setDoOutput(true);			        

	        try (BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {
	
	            String line;
	            StringBuilder response = new StringBuilder();
	
	            int i = 1;
	            while ((line = in.readLine()) != null) {
	            	
	            	//if(i>13){
	            		response.append(line);
	            	///}
	            	i++;
	            }
	            
	            String[] res = response.toString().split("/-");
	            	  
	            int i2 = 1;
	            for (String s: res) {
	            	
	            	if(i2 < res.length){
	            	
	            	String ss = s;
	            	if(i2 == 1){
	            		ss = s.split("------------------------------------------------------------------------------<br />")[2];
	            	}
	            	
	            	
	            	String[] s1 = i2==1 ? ss.split("<br />") : ss.substring(15, ss.length()).split("<br />");
	            		            	
	            	if(s1[0] != "" && !s1[0].isEmpty()){
	            	
		            	String userId =s1[0].length() > 12 ? s1[0].substring(0, 13) : "";
		            	String username =s1[0].length() > 12 ? s1[0].substring(13, s1[0].length()) : "";
		            	String interfaces = s1.length>1 ?  s1[1] : "";
		            	String ip_address =s1.length>2 ? s1[2] : "";
		            	String mac =s1.length>4 ? s1[4] : "";
		            	String vlan =s1.length>5 ? s1[5] : "";
		            	
		            	if(username == "" || username.isEmpty() || userId == "" || userId.isEmpty()){
		            		System.out.println(userId);
		            	}
		            	
		            	//mac.replaceAll(" ", "").replaceAll("-", "");
		            	String endereco_mac = mac.replaceAll(" ", "").replaceAll("-", "");
		            			endereco_mac = 
		            			endereco_mac.substring(0, 2)+":"+		            	
		            			endereco_mac.substring(2, 4)+":"+
		            			endereco_mac.substring(4, 6)+":"+
		            			endereco_mac.substring(6, 8)+":"+
		            			endereco_mac.substring(8, 10)+":"+
		            			endereco_mac.substring(10, 12);
		            	
		            	clientes.add(new String[]{
		            			userId.replaceAll(" ", ""), 
		            			username.replaceAll(" ", ""),
		            			interfaces.replaceAll(" ", ""), 
		            			ip_address.replaceAll(" ", ""), 
		            			endereco_mac.toUpperCase(), 
		            			vlan.replaceAll(" ", "")});		
		            	
	            	}
	            	i2++;
	            	}
				}
	            	           
	        }   
	            
	        return clientes;
	            
        }catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
