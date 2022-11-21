import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import dao.ContasReceberDAO;
import domain.AcessoCliente;
import domain.ContasReceber;


public class runCorrigirStatusDosBoletos {

	
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();

	
	public static void main(String[] args){
			Query q = em.createQuery("select a from AcessoCliente a where a.status_2 like 'BLOQUEADO'", AcessoCliente.class);
			List<AcessoCliente> contratos = q.getResultList();
			
			for (AcessoCliente acessoCliente : contratos) {
				//System.out.println(acessoCliente.getId());
				
				if(!podeBloquear(acessoCliente.getId().toString())){
					System.out.println(acessoCliente.getId().toString());
				}
			}
			
	}
	
	
	private static boolean podeBloquear(String contrato){
		String regexNova = "^"+contrato+"/[0-9]{2}-[0-9]{2}/[0-9]{2}";//1245/15-12/12		
		//String rProRata = contrato+"/PRORATA";
		
		Query qn3 = em.createNativeQuery(
				"select * from contas_receber cr where " +				
				"cr.status_2 ='ABERTO' " +
				"and cr.n_doc REGEXP :rNova "+	
				"and cr.data_vencimento < :dataHoje " +
				"and cr.bloquear = 'S' ",	ContasReceber.class);
		
		qn3.setParameter("dataHoje", new Date());		
		qn3.setParameter("rNova", regexNova);
		
		if(qn3.getResultList().size() > 0){
			return true;
		}else{
			return false;
		}
	}
}
