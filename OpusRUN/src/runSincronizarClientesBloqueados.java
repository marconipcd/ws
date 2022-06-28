import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import util.HuaweiUtil;
import util.MikrotikUtil;
import domain.AcessoCliente;
import domain.RadUserGroup;


public class runSincronizarClientesBloqueados {


	
	public static void main(String[] args){
				
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
				EntityManager em = emf.createEntityManager();
				
								
				Query q = em.createQuery("select a from AcessoCliente a where a.status_2 ='BLOQUEADO'", AcessoCliente.class);
				List<AcessoCliente> contratos_bloqueados = q.getResultList();
				
				System.out.println(contratos_bloqueados.size()+" contratos bloqueados");
				
				em.getTransaction().begin();
				for (AcessoCliente contrato : contratos_bloqueados) {
					
					Query q0 = em.createQuery("select r from RadUserGroup r where r.username=:u", RadUserGroup.class);
					q0.setParameter("u", contrato.getLogin());
					List<RadUserGroup> rad_plano_cliente = q0.getResultList();
					for (RadUserGroup planos : rad_plano_cliente) {
						em.remove(planos);
					}
										
					String groupName = contrato.getPlano().getPlano_bloqueio().getContrato_acesso().getId().toString()+"_"+contrato.getPlano().getPlano_bloqueio().getNome();
					RadUserGroup rug = new RadUserGroup(null, contrato.getLogin(), groupName, "1");
					em.persist(rug); 
					
					if(contrato.getBase().getTipo().equals("mikrotik")){
						MikrotikUtil.desconectarCliente(contrato.getBase().getUsuario(), contrato.getBase().getSenha(), contrato.getBase().getEndereco_ip(), 
							Integer.parseInt(contrato.getBase().getPorta_api()), contrato.getLogin());
					}
					
//					if(contrato.getBase().getTipo().equals("huawei")){
//						HuaweiUtil.desconectarCliente(contrato.getLogin());
//					}
				}
				em.getTransaction().commit();
				
				
		}


}