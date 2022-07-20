package ADEMIR;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import dao.ContasReceberDAO;
import domain.AcessoCliente;
import domain.Cliente;
import domain.Concentrador;
import domain.ContasReceber;

public class migrarClientesSanharoSaoBento {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	public static void main(String[] args) {
		
				EntityManager em = emf.createEntityManager();				
						
				Concentrador base = em.find(Concentrador.class, 108);
				
				Query q = em.createQuery("select a from AcessoCliente a where "
						+ "a.status_2 NOT LIKE 'ENCERRADO' "
						+ "AND a.base=:b", AcessoCliente.class);
				q.setParameter("b", base);
				
				List<AcessoCliente> contratos = q.getResultList();
				
				
				em.getTransaction().begin();				
						for (AcessoCliente acessoCliente : contratos) {
							Cliente c = acessoCliente.getCliente();
							c.setEmpresa(8);
							
							acessoCliente.setEmpresa_id(8);
							
							//Somente abertos
							//List<ContasReceber> boletos = ContasReceberDAO.getBoletoPorContrato(acessoCliente.getId());
							//REDE-20FTTH
							
							//Todos os boletos
							List<ContasReceber> boletos = ContasReceberDAO.procurarTodosBoletosDoAcessoPorContrato(acessoCliente.getId());
							
							if(boletos != null){
								for (ContasReceber contasReceber : boletos) {
									contasReceber.setEmpresa_id(8);
									em.merge(contasReceber);
								}
							}else{
								System.out.println(acessoCliente.getId()+" - SEM BOLETOS!");
							}
							
							em.merge(c);
							em.merge(acessoCliente);
							
							System.out.println(acessoCliente.getId().toString());
						}
				em.getTransaction().commit();
				
		
	}
}
