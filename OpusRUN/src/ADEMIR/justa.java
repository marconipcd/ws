package ADEMIR;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import domain.AcessoCliente;
import domain.RadAcct;

public class justa {
	
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();

	public static void main(String[] args){
		
//			List<String> ips = new ArrayList<String>();
//			ips.add("-");
//			ips.add("100.103.0.11");
//			ips.add("100.103.0.123");
//			ips.add("100.103.0.139");
//			ips.add("100.103.0.155");
//			ips.add("100.103.0.171");
//			ips.add("100.103.0.187");
//			ips.add("100.103.0.203");
//			ips.add("100.103.0.219");
//			ips.add("100.103.0.235");
//			ips.add("100.103.0.251");
//			ips.add("100.103.0.27");
//			ips.add("100.103.0.43");
//			ips.add("100.103.0.59");
//			ips.add("100.103.0.75");
//			ips.add("100.103.0.91");
//			ips.add("100.103.1.107");
//			ips.add("100.103.1.11");
//			ips.add("100.103.1.123");
//			ips.add("100.103.1.139");
//			ips.add("100.103.1.155");
//			ips.add("100.103.1.171");
//			ips.add("100.103.1.187");
//			ips.add("100.103.1.203");
//			ips.add("100.103.1.219");
//			ips.add("100.103.1.235");
//			ips.add("100.103.1.251");
//			ips.add("100.103.1.27");
//			ips.add("100.103.1.43");
//			ips.add("100.103.1.59");
//			ips.add("100.103.1.75");
//			ips.add("100.103.1.91");
//			ips.add("100.103.2.107");
//			ips.add("100.103.2.11");
//			ips.add("100.103.2.123");
//			ips.add("100.103.2.139");
//			ips.add("100.103.2.155");
//			ips.add("100.103.2.171");
//			ips.add("100.103.2.187");
//			ips.add("100.103.2.203");
//			ips.add("100.103.2.219");
//			ips.add("100.103.2.235");
//			ips.add("100.103.2.251");
//			ips.add("100.103.2.27");
//			ips.add("100.103.2.43");
//			ips.add("100.103.2.59");
//			ips.add("100.103.2.75");
//			ips.add("100.103.2.91");
//			ips.add("100.103.3.107");
//			ips.add("100.103.3.11");
//			ips.add("100.103.3.123");
//			ips.add("100.103.3.139");
//			ips.add("100.103.3.155");
//			ips.add("100.103.3.171");
//			ips.add("100.103.3.187");
//			ips.add("100.103.3.203");
//			ips.add("100.103.3.219");
//			ips.add("100.103.3.235");
//			ips.add("100.103.3.251");
//			ips.add("100.103.3.27");
//			ips.add("100.103.3.43");
//			ips.add("100.103.3.59");
//			ips.add("100.103.3.75");
//			ips.add("100.103.3.91");
//			
//			
//			for (String ip: ips) {				
//				Query q = em.createQuery("select r from RadAcct r where r.framedipaddress=:ip and r.acctstarttime >='2022-07-01' and r.acctstarttime <='2022-07-21'  ", RadAcct.class);
//				q.setParameter("ip", ip);
//				
//				List<RadAcct> lista= q.getResultList();
//				for (RadAcct radAcct : lista) {
//					System.out.println(radAcct.getUsername());
//				}
//				
//			}
		
		
		List<String> ips= new ArrayList<>();
		ips.add("12169");
		ips.add("2921");
		ips.add("0660");
		ips.add("10971");
		ips.add("12113");
		ips.add("7909");
		ips.add("3185");
		ips.add("11057");
		ips.add("10871");
		ips.add("4317");
		ips.add("9627");
		ips.add("0498");
		ips.add("9077");
		ips.add("3875");
		ips.add("9572");
		ips.add("1619");
		ips.add("4153");
		ips.add("4412");
		ips.add("11068");
		ips.add("7862");
		ips.add("12336");
		ips.add("6717");
		ips.add("4746");
		ips.add("3161");
		ips.add("9468");
		ips.add("10341");
		ips.add("9563");
		ips.add("8859");
		ips.add("0231");
		ips.add("3502");
		ips.add("1195");
		ips.add("6278");
		ips.add("7216");
		ips.add("7653");
		ips.add("12300");
		ips.add("12511");
		ips.add("1638");
		ips.add("9522");
		ips.add("12336");
		ips.add("12295");
		ips.add("9114");
		ips.add("10209");
		ips.add("4170");
		ips.add("11059");
		ips.add("11083");
		ips.add("11135");
		ips.add("8865");
		ips.add("8735");
		ips.add("8476");
		ips.add("10206");
		ips.add("1238");

		
		StringBuilder sb = new StringBuilder();
		for (String login: ips) {
			
			Query q = em.createQuery("select a from AcessoCliente a where a.login=:login", AcessoCliente.class);
			q.setParameter("login", login);
			
			for (AcessoCliente contrato: (List<AcessoCliente>)q.getResultList()) {
				
				sb.append(contrato.getCliente().getNome_razao()+","+contrato.getCliente().getDoc_cpf_cnpj()+","+contrato.getCliente().getEndereco_principal().getEndereco()+","+contrato.getCliente().getEndereco_principal().getNumero()+","+contrato.getCliente().getEndereco_principal().getBairro()+","+contrato.getCliente().getEndereco_principal().getCidade()+","+contrato.getCliente().getEndereco_principal().getReferencia()+","+contrato.getCliente().getTelefone1()+"<br/>");
				sb.append(System.getProperty("line.separator"));	
			}
		}
		
		System.out.println(sb.toString());
		
			
		
		
		
	}

}
