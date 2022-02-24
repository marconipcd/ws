package com.digital.opuserp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.digital.opuserp.domain.Usuario;
import com.digital.opuserp.util.ConnUtil;

public class UsuarioDAO {
	
EntityManager em;
	
	public UsuarioDAO() {
		
    em = ConnUtil.getEntity();
    
	}
	
	public static Usuario find(Integer id){
		EntityManager em = ConnUtil.getEntity();
		return em.find(Usuario.class, id);
	}
	
	public static List<Usuario> getUsuarios(){
		EntityManager em  = ConnUtil.getEntity();
		Query q = em.createQuery("select u from Usuario u", Usuario.class);
		
		return q.getResultList();
	}
	
	
	
	public static boolean save(Usuario usuario){
		
		try{
			EntityManager em  = ConnUtil.getEntity();
			
			em.getTransaction().begin();
			
					
			if(usuario.getId() != null){
				em.merge(usuario);
			}else{
				em.persist(usuario); 
			}
			
			em.getTransaction().commit();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}

	public boolean allowRemove(Integer codSetor, Integer codEmpresa){
			
			boolean allow = true;
			//Verificar se Tem Viculo com Usuario 			
			Query qAcesso = em.createQuery("select su from SetoresUsuario su where su.setor_id = :codSetor and su.empresa_id = :codEmpresa");										
			qAcesso.setParameter("codEmpresa", codEmpresa);
			qAcesso.setParameter("codSetor", codSetor);
			if(qAcesso.getResultList().size() > 0){
				allow = false;
			}
			return allow;
	}
	  
	public void changePassword(Usuario u){
		
		em.getTransaction().begin();
		em.merge(u);
		em.getTransaction().commit();
		
	}
			

	
}
