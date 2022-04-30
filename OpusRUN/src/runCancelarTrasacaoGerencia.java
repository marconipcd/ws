import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import dao.ContasReceberDAO;
import domain.ContasReceber;


public class runCancelarTrasacaoGerencia {
	
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();

	public static void main(String[] args) {
		
		Query q = em.createQuery("select c from ContasReceber c where c.status like 'EXCLUIDO' "
				+ "AND c.transacao_gerencianet IS NOT NULL  ", ContasReceber.class);
		List<ContasReceber> boletos = q.getResultList();
		
		System.out.println(boletos.size());
		for (ContasReceber boleto : boletos) {
			boolean check = cancelarTransacao(boleto.getTransacao_gerencianet());
			
			if(check){
				System.out.println(boleto.getId().toString()+" - OK");
			}else{
				System.out.println(boleto.getId().toString()+" - ERRO");
			}
		}
		
	}
	
	private static boolean cancelarTransacao(String transacao) {
		
		if(transacao != null && !transacao.equals("")){
		
				try{
					
		            String url = "http://172.17.0.13/boleto/testeCancelamentoTransacao.php";

		            HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

		            //add reuqest header
		            httpClient.setRequestMethod("POST");
		            httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
		            httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		            
		            
		            String urlParameters = "cod="+Integer.parseInt(transacao);
		            		
		            httpClient.setDoOutput(true);
		            try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
		                wr.writeBytes(urlParameters);
		                wr.flush();
		            }

            		
		            try (BufferedReader in = new BufferedReader(
		                    new InputStreamReader(httpClient.getInputStream()))) {

		                String line;
		                StringBuilder response = new StringBuilder();

		                while ((line = in.readLine()) != null) {
		                    response.append(line);
		                }

		                if(response.toString() != null && !response.toString().equals("")){
		                	org.json.JSONObject json = new org.json.JSONObject(response.toString());    
		                	System.out.println(json);
		                }
		                
		                if(httpClient.getResponseCode() == 200){
		                	
		                	boolean check = ContasReceberDAO.retirarTransacaoBoleto(transacao);
		                	
//		                	if(check){
//		                		Notify.Show("Boleto cancelado com Sucesso!", Notify.TYPE_SUCCESS);
//		                	}else{
//		                		Notify.Show("Transação não encontrada no banco de dados..", Notify.TYPE_WARNING);
//		                	}
		                }
		            }
					
					
					
					return true;
						        
					    
			        
			        
					
					}catch(Exception e){
						e.printStackTrace();
						return false;
					}
		}
		
		return false;
    }

}
