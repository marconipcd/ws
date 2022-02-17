package runSalvarArquivos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import domain.ArquivosOse2;
import domain.Ose;

public class runCalcularArquivosOrdensServico {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();
	
	public static void main(String[] args) {
	
		Query q = em.createQuery("select a from ArquivosOse2 a ", ArquivosOse2.class);
		List<ArquivosOse2> contratos = q.getResultList();
		
		em.getTransaction().begin();
		for (ArquivosOse2 arquivo : contratos) {
				Ose ac = em.find(Ose.class, arquivo.getOse());
				
				if(ac != null){
					ac.setArquivo_upload(String.valueOf(qtdArquivos(ac.getId())));
					System.out.println(ac.getId().toString());
				}
		}
		em.getTransaction().commit();
	}
	
	private static Integer qtdArquivos(Integer contrato){
		
		Query q = em.createQuery("select a from ArquivosOse2 a where a.ose=:c", ArquivosOse2.class);
		q.setParameter("c", contrato);
		
		return q.getResultList().size();
	}
}
