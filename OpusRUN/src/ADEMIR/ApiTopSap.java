package ADEMIR;

import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import domain.Cliente;



public class ApiTopSap {

	public static void main(String[] args){

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
		EntityManager em = emf.createEntityManager();
		
		Cliente c = em.find(Cliente.class, 30312);
		liberarClienteNeo(c, "1q2w3e4r5t");
	}

	
	public static boolean liberarClienteNeo(Cliente c, String senha){
		
		try{
			
			//Autencia
			String r = autenticar();			
			JSONParser parser = new JSONParser();
			
			String sessao =  ((JSONObject) parser.parse(r)).get("sessao").toString();
			String id_usuario =  ((JSONObject) parser.parse(r)).get("id_usuario").toString();
			String resultado = ((JSONObject) parser.parse(r)).get("resultado").toString();
			
			if(resultado.equals("true")){
					//Envia cadastro do Cliente
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String data_formatada = sdf.format(c.getData_nascimento());
					
					String cliente = cadastrar_cliente(id_usuario,sessao ,
							c.getNome_razao(), 
							c.getDoc_cpf_cnpj(), 
							data_formatada, 
							c.getEndereco_principal().getEndereco(), 
							c.getEndereco_principal().getNumero(), 
							c.getEndereco_principal().getBairro(), 
							c.getEndereco_principal().getCidade(), 
							c.getEndereco_principal().getCep(), 
							senha);
					
					//JSONArray array_cliente = (JSONArray) parser.parse(cliente);
					
					String status = ((JSONObject)parser.parse(cliente)).get("status").toString();
					
					if(status.equals("true")){
						String dados =  ((JSONObject)parser.parse(cliente)).get("dados").toString();
						String cod_cliente = ((JSONObject)parser.parse(dados)).get("cliente_id").toString();
												
						//Vincula Serviço ao Cliente
						String result = cadastrar_cliente_servico(id_usuario, sessao, "OPUS", cod_cliente, "13", "1", "1", c.getEmail(), senha);						
						String status_servido =  ((JSONObject)parser.parse(result)).get("status").toString();
						
						if(status_servido.equals("true")){
							System.out.println(result);						
							return true;
						}else{
							return false;
						}
					}
			
			}
			
			return false;
		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private static OkHttpClient client = null;
    private static boolean ignoreSslCertificate = false;
    
    
	public static String autenticar(){
		try{
			
				OkHttpClient.Builder builder = new OkHttpClient.Builder();	        
		       
            	ignoreSslCertificate = true;
            	builder = configureToIgnoreCertificate(builder);        
		
            	OkHttpClient client = builder.build();							
				
				 MediaType mediaType = MediaType.parse("text/plain");
				 RequestBody body = RequestBody.create(mediaType, "");
				 Request request = new Request.Builder()
					      .url("https://186.233.104.165:9910/Login?usuario=topsapp&senha=mkcolmeia&identificador=OPUS")
					      .method("POST", body)
					      .build();
				Response response = client.newCall(request).execute();
				String r = response.body().string();
			
				return r;
					
			}catch(Exception e){
				e.printStackTrace();
				
				return null;
			}
	}
	public static String cadastrar_cliente(String idUsuario, String sessao, String nome, 
			String cpf_cnpj, String data_nascimento, String endereco, String numero, String bairro, String cidade, String cep, 
			String senha_central){
		try{
			
				 OkHttpClient.Builder builder = new OkHttpClient.Builder();	        
			       
	        	 ignoreSslCertificate = true;
	        	 builder = configureToIgnoreCertificate(builder);        
		
	        	 OkHttpClient client = builder.build();
        	
				 MediaType mediaType = MediaType.parse("text/plain");
				 RequestBody body = RequestBody.create(mediaType, "");
				 Request request = new Request.Builder()
					      .url("https://186.233.104.165:9910/CadastrarCliente?"
					      		+ "idUsuario="+idUsuario
					      		+"&sessao="+sessao
					      		+"&identificador=OPUS&"
					      		+ "nome="+nome
					      		+"&cpf_cnpj="+cpf_cnpj
					      		+"&data_nascimento="+data_nascimento
					      		+"&endereco="+endereco
					      		+"&numero="+numero
					      		+"&bairro="+bairro
					      		+"&cidade=1&"
					      		+ "cep="+cep+""
					      		+ "&senha_central="+senha_central)
					      		
					      .method("POST", body)
					      .build();
				Response response = client.newCall(request).execute();
				String r = response.body().string();
			
				return r;
					
			}catch(Exception e){
				e.printStackTrace();
				
				return null;
			}
	}
	public static String cadastrar_cliente_servico(String idUsuario, String sessao, String identificador, String cliente_id, 
			String servico_id, String plano_contas, String vencimento, String login, String senha){
		
		
		try{
			
			 OkHttpClient.Builder builder = new OkHttpClient.Builder();	        
		       
	       	 ignoreSslCertificate = true;
	       	 builder = configureToIgnoreCertificate(builder);        
		
	       	 OkHttpClient client = builder.build();
			 MediaType mediaType = MediaType.parse("text/plain");
			 RequestBody body = RequestBody.create(mediaType, "");
			 Request request = new Request.Builder()
				      .url("https://186.233.104.165:9910/CadastrarClienteServico?"
				      		+ "idUsuario="+idUsuario
				      		+"&sessao="+sessao
				      		+"&identificador=OPUS&"
				      		+ "cliente_id="+cliente_id
				      		+"&servico_id="+servico_id
				      		+"&plano_contas="+plano_contas
				      		+"&vencimento="+vencimento
				      		+"&login="+login
				      		+"&senha="+senha
				      		+"&tv_cas=1&"
				      		+ "base=1")
				      		
				      .method("POST", body)
				      .build();
			Response response = client.newCall(request).execute();
			String r = response.body().string();
		
			return r;
				
		}catch(Exception e){
			e.printStackTrace();
			
			return null;
		}
	}
	
	private static OkHttpClient.Builder configureToIgnoreCertificate(OkHttpClient.Builder builder) {
	    //    LOGGER.warn("Ignore Ssl Certificate");
	        try {

	            // Create a trust manager that does not validate certificate chains
	            final TrustManager[] trustAllCerts = new TrustManager[] {
	                    new X509TrustManager() {
	                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
	                                throws CertificateException {
	                        }

	                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
	                                throws CertificateException {
	                        }

	                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                            return new java.security.cert.X509Certificate[]{};
	                        }
	                    }
	            };

	            // Install the all-trusting trust manager
	            final SSLContext sslContext = SSLContext.getInstance("SSL");
	            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
	            // Create an ssl socket factory with our all-trusting manager
	            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

	            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
	            builder.hostnameVerifier(new HostnameVerifier() {
	                public boolean verify(String hostname, SSLSession session) {
	                    return true;
	                }
	            });
	        } catch (Exception e) {
	           // LOGGER.warn("Exception while configuring IgnoreSslCertificate" + e, e);
	        }
	        return builder;
	    }

}
