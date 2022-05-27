package SUELLEN;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.joda.time.DateTime;

import domain.AcessoCliente;
import domain.ContasReceber;
import domain.Crm;

public class runCorrecaoCrmBA215 {

	
	public static void main(String[] args){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
		EntityManager em = emf.createEntityManager();
				
		Query q = em.createNativeQuery("SELECT * FROM `crm` WHERE `TIPO_ROTINA` LIKE '%BA2-15%'" ,Crm.class);		
		List<Crm> crms = q.getResultList(); 
		
		em.getTransaction().begin();
		for (Crm crm : crms) {
			
			String cod_boleto = crm.getTipo_rotina().split("/")[1];
			ContasReceber boleto = em.find(ContasReceber.class, Integer.parseInt(cod_boleto));
			
			String cod_contrato = boleto.getN_doc().split("/")[0];
			AcessoCliente contrato = em.find(AcessoCliente.class, Integer.parseInt(cod_contrato));
			
			if(boleto != null && contrato != null)
			crm.setEnd(contrato.getEndereco());
			crm.setContrato(contrato);
			em.merge(crm); 
			
			System.out.println(crm.getId().toString());
			
		}
		em.getTransaction().commit();
			
	}
}
