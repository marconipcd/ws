package ADEMIR;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import dao.ContasReceberDAO;
import domain.AcessoCliente;
import domain.ContasReceber;

public class verificarBoletosGerencia {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();
	
	public static void main(String[] args) {
		
		Query q = em.createQuery("select c from AcessoCliente c where c.status_2 != 'ENCERRADO' ", AcessoCliente.class);
		List<AcessoCliente> contratos = q.getResultList();
		//System.out.println(contratos.size()+" contratos encontrados."); 
		
		BigDecimal p_concluido = new BigDecimal(0);
		List<String> boletos_duplicados = new ArrayList<String>();
		
		for (AcessoCliente contrato: contratos) {
			
			//System.out.println(contrato.getId().toString());
			
			String regexNova = "^"+contrato.getId().toString()+"/[0-9]{2}-[0-9]{2}/[0-9]{2}";
			Query q2 = em.createNativeQuery("SELECT * FROM "
					+ "contas_receber WHERE N_DOC REGEXP :r AND TRANSACAO_GERENCIANET IS NOT NULL AND "
					+ "DATA_VENCIMENTO >= '2022-04-01' AND DATA_VENCIMENTO <= '2022-04-30' "
					+ "GROUP BY DATA_VENCIMENTO HAVING COUNT(DATA_VENCIMENTO) > 1;", ContasReceber.class);
			
			q2.setParameter("r", regexNova);
			
			if(q2.getResultList().size() > 0){
				List<ContasReceber> boletos_dupli = q2.getResultList();
				
				for (ContasReceber o : (List<ContasReceber>) boletos_dupli) {
					boletos_duplicados.add(o.getN_doc()+" - "+o.getData_vencimento());
					
					System.out.println(o);
				}
				
				System.out.println(contrato.getId().toString()+" - contrato com boleto duplicado");
			}
			
		}
		
		for (String string : boletos_duplicados) {
			System.out.println(string);
		}
		
	}
}
