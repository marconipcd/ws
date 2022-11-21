package scripts;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.joda.time.DateTime;

import domain.TopDesconectados;

public class runTopDesconectados {

	
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	
	public static void main(String[] args){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String data = sdf.format(new DateTime().minusDays(1).toDate())+" 01:00:00";
		
    	
    	
		EntityManager em = emf.createEntityManager();
		Query q  = em.createQuery("SELECT r.username, count(username), ac.STATUS_2, r.acctstoptime FROM radacct r, acesso_cliente ac "
    			+ "WHERE ac.LOGIN = r.username "
    			+ "AND r.acctstoptime >= :data "
    			+ "AND ac.STATUS_2 LIKE 'ATIVO' "
    			+ "AND ac.acctstoptime IS NOT NULL "
    			+ "GROUP BY username having count(username) > 1 "
    			+ "ORDER BY count(username)  DESC "
    			+ "LIMIT 0, 24");
		
		q.setParameter("data", data);
		
				
		
		List<Object[]> resultado = q.getResultList();		
		int i=1;
    	for (Object[] o: resultado) {			
    		
    		String login = o[0].toString();
    		String qtd = o[1].toString();
    		String status = o[2].toString();
    		String data2 = o[3].toString();
    		
    		
    		
    		i++;
		}
		
		
		
		
		
	}
}

