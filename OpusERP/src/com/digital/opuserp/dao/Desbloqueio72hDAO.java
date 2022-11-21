package com.digital.opuserp.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.digital.opuserp.OpusERP4UI;
import com.digital.opuserp.domain.AcessoCliente;
import com.digital.opuserp.domain.AgendamentoBloqueioDesbloqueio;
import com.digital.opuserp.domain.AlteracoesContrato;
import com.digital.opuserp.domain.HistoricoDesbloqueio72Horas;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.view.util.Notify;

public class Desbloqueio72hDAO {

	
	public static boolean checarSeBoletoPodeSerDesbloqueado(Integer cod_boleto){
		EntityManager em = ConnUtil.getEntity();
		
		Query q  = em.createQuery("select h from HistoricoDesbloqueio72Horas h where h.boleto=:b", HistoricoDesbloqueio72Horas.class);
		q.setParameter("b", cod_boleto);
		
		if(q.getResultList().size() > 0){
			return false;
		}
		
		
		return true;
	}
	
	
	public static boolean  desbloquear72horas(Integer codAcesso, Integer codBoleto){
		
//		boolean allow = false;												
//		
		EntityManager em = ConnUtil.getEntity();

try{
				final AcessoCliente ac = ContratosAcessoDAO.find(codAcesso);
				ac.setDesbloqueio_tmp(new Date());
								
				AgendamentoBloqueioDesbloqueio agendBloqueioDesbloqueio = new AgendamentoBloqueioDesbloqueio();
				agendBloqueioDesbloqueio.setContrato(ContratosAcessoDAO.find(codAcesso));
				agendBloqueioDesbloqueio.setData_cadastro(new Date());
				agendBloqueioDesbloqueio.setStatus("PENDENTE");
				agendBloqueioDesbloqueio.setUsuario(OpusERP4UI.getUsuarioLogadoUI().getUsername());
				
				DateTime dt48 = new DateTime().plusDays(4);
				agendBloqueioDesbloqueio.setData_agendado(dt48.toDate());
				agendBloqueioDesbloqueio.setTipo("BLOQUEIO");
				
				HistoricoDesbloqueio72Horas histDesbloqueio = new HistoricoDesbloqueio72Horas();
				histDesbloqueio.setContrato_id(codAcesso);
				histDesbloqueio.setData(new Date());
				histDesbloqueio.setUsuario_id(OpusERP4UI.getUsuarioLogadoUI().getId());
				histDesbloqueio.setBoleto(codBoleto); 
				
				em.getTransaction().begin();
					
					em.merge(agendBloqueioDesbloqueio);			
					em.merge(ac);
					em.persist(histDesbloqueio);
					
				em.getTransaction().commit();
				
				AlteracoesContratoDAO.save(new AlteracoesContrato(null, "DESBLOQUEIO 72h", ac, OpusERP4UI.getUsuarioLogadoUI(), new Date()));				
				CredenciaisAcessoDAO.DesbloquearContrato(codAcesso);	
				Notify.Show("Contrato Desbloqueado por 72h", Notify.TYPE_SUCCESS);
				
				return true;
}catch(Exception e){
	e.printStackTrace();
	return false;
}
				
//				return true;
//		}
//		
//		return false;
	}
}
