import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.joda.time.Days;

import util.HuaweiUtil;
import util.MikrotikUtil;
import dao.ContasReceberDAO;
import dao.CredenciaisAcessoDAO;
import dao.IttvDAO;
import domain.AcessoCliente;
import domain.ContasReceber;
import domain.RadReply;
import domain.RadUserGroup;

public class runBloqueio {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();

	public static void main(String[] args) {
		
		boolean check_modulo_financeiro = true;			
		boolean check_modulo_acesso = true;
					
		if(check_modulo_financeiro && check_modulo_acesso){
			
			//Verifica Boletos Atrasados e Marca para Bloquear
			verificaBoletosAtrasadosMarcaParaBloquear();
			
			//Bloqueios
			executaBloqueioParcial_PorBoletoAtrasado();		
			executaBloqueioPorVencimentoDeContrato();
			executaBloqueioTotal();							
		}	
	}
		
	private static void executaBloqueioTotal() {
		//EXECUTA BLOQUEIO TOTAL DO CONTRATO
		Query q10 = em.createQuery("select r from RadReply r where r.value ='BLOQUEADO' ", RadReply.class);
		List<RadReply> loginsBloqueados = q10.getResultList(); 
		for (RadReply radReply : loginsBloqueados) {
			try{
							
				List<ContasReceber> boletosBloqueados = ContasReceberDAO.getBoletosBloqueado(Integer.parseInt(radReply.getUsername()));
				for (ContasReceber contasReceber2 : boletosBloqueados) {
					
					Integer qtdDiasAtrasados = Days.daysBetween(new DateTime(contasReceber2.getData_vencimento()), new DateTime()).getDays();
					AcessoCliente acesso = em.find(AcessoCliente.class,Integer.parseInt(radReply.getUsername()));
					
					//
					if(qtdDiasAtrasados >= acesso.getContrato().getBloqueio_total()){
						System.out.println(acesso.getId());
						CredenciaisAcessoDAO.bloquearContratoClienteTotal(acesso.getId());					
					}
				}
			}catch(Exception e){
				//System.out.println("Contrato: "+radReply.getUsername()+ "N??o encontrado!");
			}
		}
	}

	private static void executaBloqueioPorVencimentoDeContrato() {
		
		try{
				//EXECUTA BLOQUEIO POR VENCIMENTO DO CONTRATO DE ACESSO			
				Query q6 = em.createQuery("select a from AcessoCliente a where a.data_venc_contrato < :dataHoje and a.status_2 = 'ATIVO' ", AcessoCliente.class);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				q6.setParameter("dataHoje", sdf.parse(sdf.format(new Date())));
				List<AcessoCliente> contratosVencidos  = q6.getResultList();
		
				if(contratosVencidos.size() >0){
					for (AcessoCliente acessoCliente : contratosVencidos) {
						
						if(acessoCliente != null){
							
							DateTime dt1 = new DateTime(acessoCliente.getData_venc_contrato());
							DateTime dt2 = new DateTime(new Date());
												
							if(Days.daysBetween(dt1, dt2).getDays() >= 30 && acessoCliente.getContrato().getTipo_contrato().equals("POS-PAGO")){	
								
								System.out.println(acessoCliente.getId().toString());
								
								em.getTransaction().begin();
								
									//Muda Status do Acesso
									acessoCliente.setStatus_2("BLOQUEADO");	
									
									bloquearNoRadius(acessoCliente); 								
									
									//Derruba Cliente Caso Esteja Logado
									//MikrotikUtil.derrubarConexaoHOTSPOT(acesso.getBase().getUsuario(), acesso.getBase().getSenha(), acesso.getBase().getEndereco_ip(), Integer.parseInt(acesso.getBase().getPorta_api()), acesso.getLogin());
									
									if(acessoCliente.getBase().getTipo().equals("mikrotik")){
										MikrotikUtil.derrubarConexaoPPPOE(acessoCliente.getBase().getUsuario(), acessoCliente.getBase().getSenha(), acessoCliente.getBase().getEndereco_ip(), Integer.parseInt(acessoCliente.getBase().getPorta_api()), acessoCliente.getLogin());
									}
									
									if(acessoCliente.getBase().getTipo().equals("huawei")){
										HuaweiUtil.desconectarCliente(acessoCliente.getLogin());
									}
								
									//Mudar status Ittv para INACTIVE
									if(acessoCliente.getIttv_id() != null && !acessoCliente.getIttv_id().equals("")){
										IttvDAO.atualizarStatus(acessoCliente.getIttv_id(), "INACTIVE");
									}
									
								em.getTransaction().commit();
								
							}else if(Days.daysBetween(dt1, dt2).getDays() >= 1 && acessoCliente.getContrato().getTipo_contrato().equals("PRE-PAGO")){
								System.out.println(acessoCliente.getId().toString());
								
								em.getTransaction().begin();
								
									//Muda Status do Acesso
									acessoCliente.setStatus_2("BLOQUEADO");					
								
									//Cadastra Plano Novo
									//emr.persist(new RadUserGgroup(null, acesso.getLogin(), acesso.getContrato().getId().toString()+"_BLOQUEADO", "1"));
									
									//Retira IPs Fixados no RADIUS
									Query q7 = em.createQuery("select rr from RadReply rr where rr.username =:username and rr.attribute =:attribute", RadReply.class);
									q7.setParameter("username", acessoCliente.getLogin());
									q7.setParameter("attribute", "Framed-IP-Address");
									
									for (RadReply rr : (List<RadReply>)q7.getResultList()) {
										em.remove(rr); 
									}
									
									Query q8 = em.createQuery("select rr from RadReply rr where rr.username =:username and rr.attribute =:attribute", RadReply.class);
									q8.setParameter("username", acessoCliente.getLogin());
									q8.setParameter("attribute", "Framed-Pool");
									
									for (RadReply rr : (List<RadReply>)q8.getResultList()) {
										em.remove(rr); 
									}
									
									em.persist(new RadReply(null, acessoCliente.getLogin(), "Framed-Pool", "=", "BLOQUEADO"));					
									
									//Derruba Cliente Caso Esteja Logado
									//MikrotikUtil.derrubarConexaoHOTSPOT(acesso.getBase().getUsuario(), acesso.getBase().getSenha(), acesso.getBase().getEndereco_ip(), Integer.parseInt(acesso.getBase().getPorta_api()), acesso.getLogin());
									
									if(acessoCliente.getBase().getTipo().equals("mikrotik")){
											MikrotikUtil.derrubarConexaoPPPOE(acessoCliente.getBase().getUsuario(), acessoCliente.getBase().getSenha(), acessoCliente.getBase().getEndereco_ip(), Integer.parseInt(acessoCliente.getBase().getPorta_api()), acessoCliente.getLogin());
									}
									
									if(acessoCliente.getBase().getTipo().equals("huawei")){
											HuaweiUtil.desconectarCliente(acessoCliente.getLogin());
									}
								
								em.getTransaction().commit();
							}
							
						}
						
						//System.out.println("CONTRATO "+acessoCliente.getId()+" BLOQUEADO POR VENCIMENTO DE CONTRATO");
					}
				}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void executaBloqueioParcial_PorBoletoAtrasado() {
		
		try{
			Query qn3 = em.createNativeQuery(
					"select * from contas_receber cr where " +				
					"cr.status_2 ='ABERTO' " +
					"and cr.n_doc REGEXP '^[0-9]{1,}/[0-9]{2}-[0-9]{2}/[0-9]{2}' "+	
					"and cr.data_vencimento < :dataHoje " +
					"and cr.bloquear = 'S' " +
					"and cr.bloqueado is null " +
					
					"or cr.status_2 ='ABERTO' " +
					"and cr.n_doc REGEXP '^[0-9]{1,}/[0-9]{2}-[0-9]{2}/[0-9]{2}' "+	
					"and cr.data_vencimento < :dataHoje " +
					"and cr.bloquear = 'S' " +
					"and cr.bloqueado != 'S' " +
									
					"or " +
					"cr.status_2 ='ABERTO' " +
					"and cr.n_doc REGEXP '^[0-9]{1,}/[0-9]{2}/[0-9]{2}' "+
					"and cr.data_vencimento <= :dataHoje "+
					"and cr.bloquear = 'S' "+
					"and cr.bloqueado is null "+
					
					"or " +
					"cr.status_2 ='ABERTO' " +
					"and cr.n_doc REGEXP '^[0-9]{1,}/[0-9]{2}/[0-9]{2}' "+
					"and cr.data_vencimento <= :dataHoje "+
					"and cr.bloquear = 'S' "+
					"and cr.bloqueado != 'S' " +
					
					"or " +
					"cr.status_2 ='ABERTO' " +
					"and cr.n_doc REGEXP '^[0-9]{1,}/PRORATA' "+
					"and cr.data_vencimento <= :dataHoje "+
					"and cr.bloquear = 'S' "+
					"and cr.bloqueado != 'S' " + 
					
					"or " +
					"cr.status_2 ='ABERTO' " +
					"and cr.n_doc REGEXP '^[0-9]{1,}/PRORATA' "+
					"and cr.data_vencimento <= :dataHoje "+
					"and cr.bloquear = 'S' "+
					"and cr.bloqueado is null ",
					ContasReceber.class);
			
			qn3.setParameter("dataHoje", new Date());
			
			List<ContasReceber> boletosParaBloquear = qn3.getResultList();
			for (ContasReceber contasReceber : boletosParaBloquear) {
				
				String numeroDoc = contasReceber.getN_doc();
				Integer codAcesso = Integer.parseInt(numeroDoc.split("/")[0].toString());				
				AcessoCliente acesso = em.find(AcessoCliente.class,codAcesso);
				
				if(acesso != null){
					
					if(acesso.getStatus_2().equals("ATIVO")){
												
						if(acesso != null){
							
							em.getTransaction().begin();
							
								//Bloqueio no banco de dados
								acesso.setStatus_2("BLOQUEADO");					
									
								//Bloqueio no Radius
								bloquearNoRadius(acesso); 

								//Derruba Conexao
								if(acesso.getBase() != null && acesso.getBase().getUsuario() != null && acesso.getBase().getSenha() != null && acesso.getBase().getEndereco_ip() != null && acesso.getBase().getPorta_api() != null && acesso.getLogin() != null){
									
									if(acesso.getBase().getTipo().equals("mikrotik")){
											MikrotikUtil.derrubarConexaoPPPOE(acesso.getBase().getUsuario(), acesso.getBase().getSenha(), acesso.getBase().getEndereco_ip(), Integer.parseInt(acesso.getBase().getPorta_api()), acesso.getLogin());
									}
									
									if(acesso.getBase().getTipo().equals("huawei")){
											HuaweiUtil.desconectarCliente(acesso.getLogin());
									}
								}
							
							em.getTransaction().commit();
														
							//Bloqueio ITTV
							if(acesso.getIttv_id() != null && !acesso.getIttv_id().equals("")){
								IttvDAO.atualizarStatus(acesso.getIttv_id(), "INACTIVE");
							}
							
						}
						
						em.getTransaction().begin();
						contasReceber.setBloqueado("S");
						em.merge(contasReceber);
						em.getTransaction().commit();
						
						System.out.println(acesso.getId()+" : BLOQUEADO");
					}else if(acesso.getStatus_2().equals("BLOQUEADO")){						
						
						em.getTransaction().begin();
						contasReceber.setBloqueado("S");
						em.merge(contasReceber);
						em.getTransaction().commit();
						
						System.out.println(acesso.getId()+" : BLOQUEADO");
						
					}		
					 
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void bloquearNoRadius(AcessoCliente acesso){
		
		//Retira IPs Fixados no RADIUS
		Query q4 = em.createQuery("select rr from RadReply rr where rr.username =:username and rr.attribute =:attribute", RadReply.class);
		q4.setParameter("username", acesso.getLogin());
		q4.setParameter("attribute", "Framed-IP-Address");
		
		for (RadReply rr : (List<RadReply>)q4.getResultList()) {
			em.remove(rr); 
		}
		
		Query q5 = em.createQuery("select rr from RadReply rr where rr.username =:username and rr.attribute =:attribute", RadReply.class);
		q5.setParameter("username", acesso.getLogin());
		q5.setParameter("attribute", "Framed-Pool");
		
		for (RadReply rr : (List<RadReply>)q5.getResultList()) {
			em.remove(rr); 
		}
			
		if(acesso.getPlano().getPlano_bloqueio() != null){
				//Altera o Plano para plano de bloqueio
				Query q6 = em.createQuery("select r from RadUserGroup r where r.username=:login", RadUserGroup.class);
				q6.setParameter("login", acesso.getLogin());
				
				for (RadUserGroup r : (List<RadUserGroup>)q6.getResultList()) {
					em.remove(r); 
				}
				
				em.persist(new RadUserGroup(null, acesso.getLogin(), acesso.getPlano().getPlano_bloqueio().getContrato_acesso().getId().toString()+"_"+acesso.getPlano().getPlano_bloqueio().getNome(), "1"));
		}
	}
	
	private static void verificaBoletosAtrasadosMarcaParaBloquear(){
		
		Query qn = em.createNativeQuery("select * from contas_receber cr where " +				
				
				"cr.status_2 ='ABERTO' " +
				"and cr.n_doc REGEXP '^[0-9]{1,}-[a-z]{2}[0-9]{1,}' "+	
				"and cr.data_vencimento < :dataHoje " +
				"and cr.bloquear is null  " +
				
				"or cr.status_2 ='ABERTO' " +
				"and cr.n_doc REGEXP '^[0-9]{1,}/[0-9]{2}-[0-9]{2}/[0-9]{2}' "+	
				"and cr.data_vencimento < :dataHoje " +
				"and cr.bloquear is null  " +
				
				"or cr.status_2 ='ABERTO' " +
				"and cr.n_doc REGEXP '^[0-9]{1,}/[0-9]{2}-[0-9]{2}/[0-9]{2}' "+	
				"and cr.data_vencimento < :dataHoje " +
				"and cr.bloquear != 'S' " +
				
				"or " +
				"cr.status_2 ='ABERTO' " +
				"and cr.n_doc REGEXP '^[0-9]{1,}/[0-9]{2}/[0-9]{2}' "+
				"and cr.data_vencimento <= :dataHoje "+
				"and cr.bloquear is null  " +
				
				"or " +
				"cr.status_2 ='ABERTO' " +
				"and cr.n_doc REGEXP '^[0-9]{1,}/[0-9]{2}/[0-9]{2}' "+
				"and cr.data_vencimento <= :dataHoje "+
				"and cr.bloquear != 'S' " +
				
				"or " +
				"cr.status_2 ='ABERTO' " +
				"and cr.n_doc REGEXP '^[0-9]{1,}/PRORATA' "+
				"and cr.data_vencimento <= :dataHoje "+
				"and cr.bloquear != 'S' "+
				
				"or " +
				"cr.status_2 ='ABERTO' " +
				"and cr.n_doc REGEXP '^[0-9]{1,}/PRORATA' "+
				"and cr.data_vencimento <= :dataHoje "+
				"and cr.bloquear is null ",
				
				ContasReceber.class);
		
			qn.setParameter("dataHoje", new Date());
			List<ContasReceber> boletosAtrasados = qn.getResultList();
		
		em.getTransaction().begin();
		for (ContasReceber contasReceber : boletosAtrasados) {
			
			String numeroDoc = contasReceber.getN_doc();
			Integer codAcesso = Integer.parseInt(numeroDoc.split("/")[0].toString());				
			AcessoCliente acesso = em.find(AcessoCliente.class,codAcesso);
			
			if(acesso != null && acesso.getContrato().getTipo_contrato().equals("POS-PAGO")){
				
				Integer qtdDiasAtrasados = Days.daysBetween(new DateTime(contasReceber.getData_vencimento()), new DateTime()).getDays();
				
				//acesso.getContrato().getBloqueio()
				if(qtdDiasAtrasados >= acesso.getContrato().getBloqueio()){				
					System.out.println("Boleto"+contasReceber.getId().toString());
					contasReceber.setBloquear("S");
					em.merge(contasReceber);					
				}			
			}
		}			
		em.getTransaction().commit();
	}

}
