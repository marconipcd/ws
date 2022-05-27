package com.digital.opuserp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.digital.opuserp.OpusERP4UI;
import com.digital.opuserp.domain.Empresa;
import com.digital.opuserp.domain.EmpresasUsuario;
import com.digital.opuserp.domain.ModuloUsuario;
import com.digital.opuserp.domain.SubModuloUsuario;
import com.digital.opuserp.domain.Usuario;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.StringUtil;

/**
 *
 * @author Marconi
 */
public class LoginDAO {
	
	public static Usuario fazerLogin(Usuario u){
		if(OpusERP4UI.rodarComApi){
			return fazerLogin_api(u);
		}else{
			return fazerLogin_db(u);
		}
	}
	
	public static Usuario fazerLogin_api(Usuario u){
			
			try{
				
				StringBuilder url = new StringBuilder();
				url.append(ApiDAO.end);
				
				String r = ApiDAO.execute(
						"fazer_login", 
						new String[]{"?username="+u.getUsername(),
								"&password="+u.getPassword()}, 
						ApiDAO.REQUEST_POST);
								
						
				JSONParser parser = new JSONParser();
				JSONArray array = (JSONArray) parser.parse(r);
				
				if(array.size() == 1){
					
					String cod = ((JSONObject)array.get(0)).get("id").toString();
					String usuario = ((JSONObject)array.get(0)).get("username").toString();
					String status = ((JSONObject)array.get(0)).get("status").toString();
					String email = ((JSONObject)array.get(0)).get("email").toString();
					String funcao = ((JSONObject)array.get(0)).get("funcao").toString();
					String password = ((JSONObject)array.get(0)).get("password").toString();
					String setor = ((JSONObject)array.get(0)).get("setor").toString();
					String visualizar_todos_crm = ((JSONObject)array.get(0)).get("visualizar_todos_crm").toString();
					
					Usuario us = new Usuario();
					us.setEmail(email);
					us.setFuncao(funcao);
					us.setPassword(password);
					us.setSetor(setor);
					us.setVisualizar_todos_crm(visualizar_todos_crm);
					us.setId(Integer.parseInt(cod));
					us.setUsername(usuario);
					us.setStatus(status);
					
					return us;
				}
				    
				 return null;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
    
    public static Usuario fazerLogin_db(Usuario u){
    	EntityManager em = ConnUtil.getEntity();
        try{             
            
            Query q = em.createQuery("select u from Usuario u where "
            		+ "u.username=:usuario AND "
            		+ "u.password=:senha AND "
            		+ "u.status='ATIVO'", Usuario.class);
            q.setParameter("usuario", u.getUsername());
            q.setParameter("senha", StringUtil.md5(u.getPassword()));
            
            if(q.getResultList().size() == 1){
            	return (Usuario) q.getSingleResult();
            }
            
            return null;
         
        }catch(Exception e){
        	e.printStackTrace();
            System.out.println("Login:"+u.getUsername()+" ou Senha:"+u.getPassword()+" Incorretos:");
            return null;            
        }
    }
    
    
    public static List<EmpresasUsuario> getEmpresasUsuario(Usuario u){
        
    //	if(OpusERP4UI.rodarComApi){
			//return getEmpresasUsuario_api(u);
	//	}else{
			return getEmpresasUsuario_db(u);
		//}
        
    }
    
//    public static List<EmpresasUsuario> getEmpresasUsuario_api(Usuario u){
//        
//    	try{
//	    	StringBuilder url = new StringBuilder();
//			url.append(ApiDAO.end);
//			
//			String r = ApiDAO.execute(
//					"empresas_usuario", 
//					new String[]{"?usuario="+u.getId()}, 
//					ApiDAO.REQUEST_POST);
//							
//					
//			JSONParser parser = new JSONParser();
//			JSONArray array = (JSONArray) parser.parse(r);
//			
//			
//			
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
//    }
    
    public static List<EmpresasUsuario> getEmpresasUsuario_db(Usuario u){
        
    	EntityManager em = ConnUtil.getEntity();
    	
    	try{
    		
    		Query q = em.createQuery("select e from EmpresasUsuario e where e.usuario=:user", EmpresasUsuario.class);
	        q.setParameter("user", u);
	        
	        return q.getResultList();
	        
    	}catch(Exception e){
    		return null; 
    	}
        
    }
    
    public static List<ModuloUsuario> getModulosUsuario(Usuario u){
        
    	EntityManager em = ConnUtil.getEntity();
    	
    	try{
    		
    		Query q = em.createQuery("select e from ModuloUsuario e where e.usuario=:user", ModuloUsuario.class);
	        q.setParameter("user", u);
	        
	        return q.getResultList();
	        
    	}catch(Exception e){
    		return null; 
    	}
        
    }
 
    public static List<SubModuloUsuario> getSubModulosUsuario(Usuario u){
        
    	EntityManager em = ConnUtil.getEntity();
    	
    	try{
    		
    		Query q = em.createQuery("select e from SubModuloUsuario e where e.usuario=:user", SubModuloUsuario.class);
	        q.setParameter("user", u);
	        
	        return q.getResultList();
	        
    	}catch(Exception e){
    		return null; 
    	}
        
    }
    
    
    public static List<Empresa> getEmpresas(){
    	
    	EntityManager em = ConnUtil.getEntity();
    	
    	try{
    	
    	Query q = em.createQuery("select e from Empresa e");
    	return q.getResultList();
    	}catch(Exception e){
    		return null;
    	}
        
    }
    
    public static boolean validarUsuario(String username){
    	EntityManager em = ConnUtil.getEntity();
    	
    	try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
	    	CriteriaQuery<Usuario> c = cb.createQuery(Usuario.class);
	    	Root<Usuario> user = c.from(Usuario.class);
	    	c.where(cb.equal(user.get("username"),cb.parameter(String.class,"name")));
	    	
	    	TypedQuery q = em.createQuery(c);
	    	q.setParameter("name", username);
	    	
	    	if (q.getResultList().size()>0){
	    		return false;
	    	}else{
	    		return true;
	    	}
		} catch (Exception e) {
			System.out.println("Erro ao Logar!");
			return false;
		}           	
    	
    }
        
}
