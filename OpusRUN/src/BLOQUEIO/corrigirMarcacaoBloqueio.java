package BLOQUEIO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import util.HuaweiUtil;
import util.MikrotikUtil;
import domain.AcessoCliente;
import domain.RadReply;

public class corrigirMarcacaoBloqueio {

	
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();
	
	
	public static void main(String[] args) {
		Query q = em.createQuery("select a from AcessoCliente a where a.status_2='BLOQUEADO'", AcessoCliente.class);
		List<AcessoCliente> contratos_bloqueados = q.getResultList();
		
		em.getTransaction().begin();
		for (AcessoCliente contrato_bloqueado: contratos_bloqueados) {
			
			Query q1 = em.createQuery("select r from RadReply r where "
					+ "r.attribute='Framed-Pool' and "
					+ "r.value='BLOQUEADO' and "
					+ "r.username=:username", RadReply.class);
			
			q1.setParameter("username", contrato_bloqueado.getId().toString());
			
			List<RadReply> pool_bloqueio = q1.getResultList();
			for (RadReply radReply : pool_bloqueio) {
				em.remove(radReply);
			}
			
			Query q2 = em.createQuery("select r from RadReply r where "
					+ "r.attribute='Framed-IP-Address' and "					
					+ "r.username=:username", RadReply.class);
			
			q2.setParameter("username", contrato_bloqueado.getId().toString());
			
			List<RadReply> ips_fixo = q2.getResultList();
			for (RadReply radReply : ips_fixo) {
				em.remove(radReply);
			}
			
				
			//Se tiver ip fixo, coloca fixado no radius
			if(contrato_bloqueado.getEndereco_ip() != null){
				em.persist(new RadReply(null, contrato_bloqueado.getLogin(), "Framed-IP-Address", "=", contrato_bloqueado.getEndereco_ip()));
			}
			
			
			//Derrubar
			if(contrato_bloqueado.getBase().getTipo().equals("mikrotik")){
				MikrotikUtil.derrubarConexaoPPPOE(contrato_bloqueado.getBase().getUsuario(), contrato_bloqueado.getBase().getSenha(), contrato_bloqueado.getBase().getEndereco_ip(), Integer.parseInt(contrato_bloqueado.getBase().getPorta_api()), contrato_bloqueado.getLogin());
			}
			
//			if(contrato_bloqueado.getBase().getTipo().equals("huawei")){
//				HuaweiUtil.desconectarCliente(contrato_bloqueado.getLogin());
//			}
						
		}
		em.getTransaction().commit();
	}

}
