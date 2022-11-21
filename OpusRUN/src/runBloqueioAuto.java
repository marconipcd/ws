import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import util.HuaweiUtil;
import util.MikrotikUtil;
import dao.CredenciaisAcessoDAO;
import domain.AcessoCliente;
import domain.AgendamentoBloqueioDesbloqueio;
import domain.AlterarcoesContrato;
import domain.ContasReceber;
import domain.RadReply;
import domain.RadUserGroup;
import domain.Usuario;


public class runBloqueioAuto {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();
	
	public static void main(String[] args){
				
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Date data = new Date();
//		try{
//			data = sdf.parse("25/09/2022");
//		}catch(Exception e){
//			
//		}
		Query q = em.createQuery("select a from AgendamentoBloqueioDesbloqueio a where a.status=:s and a.data_agendado =:d", AgendamentoBloqueioDesbloqueio.class);
		q.setParameter("s", "PENDENTE");
		q.setParameter("d", data);
		
		for (AgendamentoBloqueioDesbloqueio a : (List<AgendamentoBloqueioDesbloqueio>)q.getResultList()) {
			
			try{				
			
					
					//if(sdf.parse(sdf.format(a.getData_agendado())).equals(sdf.parse(sdf.format(new Date())))){
						
						if(a.getTipo().equals("BLOQUEIO")){
							
							boolean podeBloquear =podeBloquear(a.getContrato().getId().toString()); 
							if(podeBloquear){
								//bloquear(a.getContrato());
								bloquearNoRadius(a.getContrato());
							}
							
							em.getTransaction().begin();
								
								if(podeBloquear){
									AcessoCliente contrato = a.getContrato();
									contrato.setStatus_2("BLOQUEADO");
									em.merge(contrato);
								}
							
								a.setData_execucao(new Date());
								a.setStatus("EXECUTADO");
								em.persist(new AlterarcoesContrato(null, "BLOQUEIO CONTRATO", a.getContrato(), new Usuario(100), new Date()));
							
							em.getTransaction().commit();
							System.out.println("Contrato Bloqueado");
							
							
						}
						
						if(a.getTipo().equals("DESBLOQUEIO")){
							desbloquear(a.getContrato());
							
							em.getTransaction().begin();
								
								a.setData_execucao(new Date());
								a.setStatus("EXECUTADO");
								em.persist(new AlterarcoesContrato(null, "DESBLOQUEIO CONTRATO", a.getContrato(), new Usuario(100), new Date()));
								
							em.getTransaction().commit();
							System.out.println("Contrato Desbloqueado");
						}
						
					//}
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void desbloquear(AcessoCliente acesso){
		
		em.getTransaction().begin();
		
			acesso.setStatus_2("ATIVO");
			
			Query qrr = em.createQuery("select rr from RadReply rr where rr.username = :usuario and rr.attribute = 'Framed-Pool' and rr.value = 'BLOQUEADO'", RadReply.class);
			qrr.setParameter("usuario", acesso.getLogin());						
			if(qrr.getResultList().size() >0){
						
				List<RadReply> marcacoes = qrr.getResultList(); 
				for(RadReply rr:marcacoes){
					em.remove(rr);
				}
			}
			
			Query qrr2 = em.createQuery("select rr from RadReply rr where rr.username = :usuario and rr.attribute = 'Framed-Pool' and rr.value = 'BLOQUEADO_TOTAL'", RadReply.class);
			qrr2.setParameter("usuario", acesso.getLogin());						
			if(qrr2.getResultList().size() >0){
						
				List<RadReply> marcacoes = qrr2.getResultList(); 
				for(RadReply rr:marcacoes){
					em.remove(rr);
				}
			}
			
			if(acesso.getEndereco_ip() != null && !acesso.getEndereco_ip().equals("")){
				em.persist(new RadReply(null, acesso.getLogin(), "Framed-IP-Address", "=", acesso.getEndereco_ip()));
			}
										
		em.getTransaction().commit();
		
		if(acesso.getBase().getTipo().equals("mikrotik")){
				MikrotikUtil.desconectarCliente(acesso.getBase().getUsuario(), acesso.getBase().getSenha(), acesso.getBase().getEndereco_ip(), Integer.parseInt(acesso.getBase().getPorta_api()), acesso.getLogin());
		}
		if(acesso.getBase().getTipo().equals("huawei")){
				HuaweiUtil.desconectarCliente(acesso.getLogin());
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
	
	public static void bloquear(AcessoCliente acesso){	

		CredenciaisAcessoDAO.bloquearContratoClienteTotal(acesso.getId());
	}
	private static void bloquearNoRadius(AcessoCliente acesso){
			
			//Retira IPs Fixados no RADIUS
//			Query q4 = em.createQuery("select rr from RadReply rr where rr.username =:username and rr.attribute =:attribute", RadReply.class);
//			q4.setParameter("username", acesso.getLogin());
//			q4.setParameter("attribute", "Framed-IP-Address");
//			
//			for (RadReply rr : (List<RadReply>)q4.getResultList()) {
//				em.remove(rr); 
//			}
//			
//			Query q5 = em.createQuery("select rr from RadReply rr where rr.username =:username and rr.attribute =:attribute", RadReply.class);
//			q5.setParameter("username", acesso.getLogin());
//			q5.setParameter("attribute", "Framed-Pool");
//			
//			for (RadReply rr : (List<RadReply>)q5.getResultList()) {
//				em.remove(rr); 
//			}
				
			if(acesso.getPlano().getPlano_bloqueio() != null){
					//Altera o Plano para plano de bloqueio
					Query q6 = em.createQuery("select r from RadUserGroup r where r.username=:login", RadUserGroup.class);
					q6.setParameter("login", acesso.getLogin());
					
					for (RadUserGroup r : (List<RadUserGroup>)q6.getResultList()) {
						em.remove(r); 
					}
					
					em.persist(new RadUserGroup(null, acesso.getLogin(), acesso.getPlano().getPlano_bloqueio().getContrato_acesso().getId().toString()+"_"+acesso.getPlano().getPlano_bloqueio().getNome(), "1"));
			}
			
			
			if(acesso.getBase().getTipo().equals("mikrotik")){
				MikrotikUtil.derrubarConexaoPPPOE(acesso.getBase().getUsuario(), acesso.getBase().getSenha(), acesso.getBase().getEndereco_ip(), Integer.parseInt(acesso.getBase().getPorta_api()), acesso.getLogin());
			}
			
			if(acesso.getBase().getTipo().equals("huawei")){
				HuaweiUtil.desconectarCliente(acesso.getLogin());
			}
		}
}
