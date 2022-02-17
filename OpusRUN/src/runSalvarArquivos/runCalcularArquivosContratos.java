package runSalvarArquivos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import domain.AcessoCliente;
import domain.ArquivosContrato2;

public class runCalcularArquivosContratos {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();
	
	public static void main(String[] args) {
	
		Query q = em.createQuery("select a from ArquivosContrato2 a ", ArquivosContrato2.class);
		List<ArquivosContrato2> contratos = q.getResultList();
		
		em.getTransaction().begin();
		for (ArquivosContrato2 arquivo : contratos) {
				AcessoCliente ac = em.find(AcessoCliente.class, arquivo.getContrato());
				ac.setArquivo_upload(String.valueOf(qtdArquivos(ac.getId())));
				System.out.println(ac.getId().toString());
		}
		em.getTransaction().commit();
	}
	
	private static Integer qtdArquivos(Integer contrato){
		
		Query q = em.createQuery("select a from ArquivosContrato2 a where a.contrato=:c", ArquivosContrato2.class);
		q.setParameter("c", contrato);
		
		return q.getResultList().size();
	}
}
