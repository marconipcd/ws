package com.digital.opuserp.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.digital.opuserp.domain.AcessoCliente;
import com.digital.opuserp.domain.NfeMestre;

public class ScriptCorrecaoNfe {

	
	static EntityManager em;
	static EntityManagerFactory emf;
	public static void main(String[] args) {
		emf = Persistence.createEntityManagerFactory("OpusERP4");
		em = emf.createEntityManager();
		
		Query q = em.createNativeQuery("SELECT *  FROM `nfe_mestre` WHERE `DATA_EMISSAO` >= '2018-04-01' and `DATA_EMISSAO` <= '2018-04-31'", NfeMestre.class);
		
		em.getTransaction().begin();
		for (NfeMestre nfe : (List<NfeMestre>)q.getResultList()) {
			System.out.println(nfe.getId().toString());
			
			AcessoCliente contrato = nfe.getContrato();
			contrato.setEmitir_nfe("NFE-MOD21");
			contrato.setEmitir_nfe_c_boleto_aberto("SIM");
			contrato.setEmitir_nfe_automatico("SIM");
			contrato.setCfop_nfe(5307);
			em.merge(contrato);
		}
		em.getTransaction().commit();
	}
}
