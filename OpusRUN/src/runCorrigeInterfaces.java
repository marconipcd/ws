import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import domain.Swith;


public class runCorrigeInterfaces {
	
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");

	public static void main(String[] args) {
		EntityManager em = emf.createEntityManager();
		
		Query q = em.createQuery("select s from Swith s", Swith.class);
		List<Swith> caixas = q.getResultList();
		
		em.getTransaction().begin();
		for (Swith swith : caixas) {
				String vlan = swith.getInterfaces();
				
				if(vlan != null){
					vlan = vlan.replace("VLAN-", "");
					swith.setInterfaces(vlan);
					em.merge(swith);
					
					System.out.println(vlan);
				}
		}
		em.getTransaction().commit();
	}

}
