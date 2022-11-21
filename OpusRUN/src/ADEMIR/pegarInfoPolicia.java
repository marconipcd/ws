package ADEMIR;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import domain.AcessoCliente;
import domain.RadAcct;

public class pegarInfoPolicia {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();
	
	public static void main(String[] args) {
		
		List<String> ip= new ArrayList<>();

		ip.add("100.103.4.11");
		ip.add("100.103.4.27");
		ip.add("100.103.4.43");
		ip.add("100.103.4.59");
		ip.add("100.103.4.75");
		ip.add("100.103.4.91");
		ip.add("100.103.4.107");
		ip.add("100.103.4.123");
		ip.add("100.103.4.139");
		ip.add("100.103.4.155");
		ip.add("100.103.4.171");
		ip.add("100.103.4.187");
		ip.add("100.103.4.203");
		ip.add("100.103.4.219");
		ip.add("100.103.4.235");
		ip.add("100.103.4.251");
		ip.add("100.103.5.11");
		ip.add("100.103.5.27");
		ip.add("100.103.5.43");
		ip.add("100.103.5.59");
		ip.add("100.103.5.75");
		ip.add("100.103.5.91");
		ip.add("100.103.5.107");
		ip.add("100.103.5.123");
		ip.add("100.103.5.139");
		ip.add("100.103.5.155");
		ip.add("100.103.5.171");
		ip.add("100.103.5.187");
		ip.add("100.103.5.203");
		ip.add("100.103.5.219");
		ip.add("100.103.5.235");
		ip.add("100.103.5.251");
		ip.add("100.103.6.11");
		ip.add("100.103.6.27");
		ip.add("100.103.6.43");
		ip.add("100.103.6.59");
		ip.add("100.103.6.75");
		ip.add("100.103.6.91");
		ip.add("100.103.6.107");
		ip.add("100.103.6.123");
		ip.add("100.103.6.139");
		ip.add("100.103.6.155");
		ip.add("100.103.6.171");
		ip.add("100.103.6.187");
		ip.add("100.103.6.203");
		ip.add("100.103.6.219");
		ip.add("100.103.6.235");
		ip.add("100.103.6.251");
		ip.add("100.103.7.11");
		ip.add("100.103.7.27");
		ip.add("100.103.7.43");
		ip.add("100.103.7.59");
		ip.add("100.103.7.75");
		ip.add("100.103.7.91");
		ip.add("100.103.7.107");
		ip.add("100.103.7.123");
		ip.add("100.103.7.139");
		ip.add("100.103.7.155");
		ip.add("100.103.7.171");
		ip.add("100.103.7.187");
		ip.add("100.103.7.203");
		ip.add("100.103.7.219");
		ip.add("100.103.7.235");
		ip.add("100.103.7.251");
		
		
		for (String s : ip) {
			
			//Query q = em.createQuery("select a from AcessoCliente a where a.endereco_ip=:ip", AcessoCliente.class);
			//q.setParameter("ip", s);
			
			Query q = em.createNativeQuery("select * from radacct a where "
					+ "a.framedipaddress=:ip and a.acctstarttime>='2022-08-04 00:01:00' "
					+ "and a.acctstarttime <='2022-08-05 23:59:00' group by a.username", RadAcct.class);
			q.setParameter("ip", s);
			
			List<RadAcct> contratos = q.getResultList();
			for (RadAcct r: contratos) {
				
				AcessoCliente contrato = em.find(AcessoCliente.class, Integer.parseInt(r.getUsername()));
				
				System.out.println(
						contrato.getCliente().getNome_razao()+","
						+contrato.getCliente().getDoc_cpf_cnpj()+","
						+contrato.getCliente().getEndereco_principal().getEndereco()+","
						+contrato.getCliente().getEndereco_principal().getNumero()+","
						+contrato.getCliente().getEndereco_principal().getBairro()+","
						+contrato.getCliente().getEndereco_principal().getCidade()+","
						+contrato.getCliente().getEmail()+","
						+contrato.getCliente().getDdd_fone1()+contrato.getCliente().getTelefone1()+","
						+contrato.getCliente().getDdd_fone2()+contrato.getCliente().getTelefone2()+","
						+contrato.getCliente().getDdd_cel1()+contrato.getCliente().getCelular1()+","
						+contrato.getCliente().getDdd_cel2()+contrato.getCliente().getCelular2()+","
						+r.getFramedipaddress()+","
				);
			}
		}
	}

}
