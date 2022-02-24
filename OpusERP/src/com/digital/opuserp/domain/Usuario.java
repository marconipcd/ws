package com.digital.opuserp.domain;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.digital.opuserp.util.StringUtil;

/**
 *
 * @author Marconi
 */

@Entity
@Table(name="usuario")
@Cacheable(value=false)
public class Usuario {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="ID", nullable=false,unique=true)
    private Integer id;
    @Column(name="USERNAME", nullable=false,unique=false, length=50)
    private String username;
    @Column(name="PASSWORD_2", nullable=false,unique=false, length=32)
    private String password;
    @Column(name="FUNCAO", nullable=false,unique=false, length=20)
    private String funcao;
    
    @Column(name="SETOR", nullable=true)
    private String setor;
    
    @Column(name="EMAIL", nullable=true)
    private String email;
    
    @Column(name="VISUALIZAR_TODOS_CRMS", nullable=true)
    private String visualizar_todos_crm;
    
    @Column(name="STATUS")
    private String status;
        
    @OneToMany(mappedBy="usuario")   
    private List<EmpresasUsuario> empresas;
    
    @OneToMany(mappedBy="usuario")    
    private List<ModuloUsuario> modulos;
    
    @OneToMany(mappedBy="usuario")   
    private List<SubModuloUsuario> submodulos;
    

    
    public Usuario() {
    }
    
    public Usuario(Integer id) {
    	this.id = id;
    }


    public Usuario(String username, String password, String funcao) {
        this.username = username;
        this.password = password;
        this.funcao = funcao;
    }
    public Usuario(Integer id,String username, String password, String funcao) {
        
    	this.id = id;
    	this.username = username;
        this.password = password;
        this.funcao = funcao;
    }

 
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

        public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

        public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
    	
    	if(this.password != null && !this.password.equals(password)){
    		this.password = StringUtil.md5(password);
    	}
    	
    }

        public String getFuncao() {
        return funcao;
    }

	public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

	public String getVisualizar_todos_crm() {
		
		if(visualizar_todos_crm != null && visualizar_todos_crm.equals("0")){
			return  "NAO";
		}else{
			return "SIM";
		}
	}

	public void setVisualizar_todos_crm(String visualizar_todos_crm) {
		
		if(visualizar_todos_crm != null && visualizar_todos_crm.equals("SIM")){
			this.visualizar_todos_crm = "1";
		}else{
			this.visualizar_todos_crm = "0";
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	

//	public List<EmpresasUsuario> getEmpresas() {
//		return empresas;
//	}


//	public void setEmpresas(List<EmpresasUsuario> empresas) {
//		this.empresas = empresas;
//	}
    
	
//	public List<ModuloUsuario> getModulos() {
//		return modulos;
//	}
//
//	public void setModulos(List<ModuloUsuario> modulos) {
//		this.modulos = modulos;
//	}
//
//	public List<SubModuloUsuario> getSubmodulos() {
//		return submodulos;
//	}
//
//	public void setSubmodulos(List<SubModuloUsuario> submodulos) {
//		this.submodulos = submodulos;
//	}

   
    
    
    
}
