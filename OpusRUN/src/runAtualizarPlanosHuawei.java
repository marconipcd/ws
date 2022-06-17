import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import domain.PlanoAcesso;
import domain.RadGroupReply;


public class runAtualizarPlanosHuawei {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	
	public static void main(String[] args) {
		
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select p from PlanoAcesso p ", PlanoAcesso.class);
		List<PlanoAcesso> planos = q.getResultList();
		
		em.getTransaction().begin();
		for (PlanoAcesso planoAcesso : planos) {
//			String up = planoAcesso.getRate_limit().split("/")[0].replaceAll("M", "")+"000000";
//			String dow = planoAcesso.getRate_limit().split("/")[1].replaceAll("M", "")+"000000";
//			
//			planoAcesso.setRate_limit_huawei_up(up);
//			planoAcesso.setRate_limit_huawei_down(dow);
//			em.merge(planoAcesso);
			PlanoAcesso cAdd = planoAcesso;
			em.persist(new RadGroupReply(null, cAdd.getContrato_acesso().getId().toString()+"_"+cAdd.getNome(), 
					"Huawei-Input-Average-Rate", "=", cAdd.getRate_limit_huawei_up()));
			
			em.persist(new RadGroupReply(null, cAdd.getContrato_acesso().getId().toString()+"_"+cAdd.getNome(), 
					"Huawei-Output-Average-Rate", "=", cAdd.getRate_limit_huawei_down()));
			
		}
		em.getTransaction().commit();
		
	}

}
