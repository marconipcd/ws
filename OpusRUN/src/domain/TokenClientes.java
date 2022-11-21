package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="token_clientes")
public class TokenClientes {

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="CONTRATO_ID")
	private Integer contrato;
	
	@Column(name="TOKEN")
	private String token;
	
	public TokenClientes() {
		
	}
	
	

	public TokenClientes(Integer id, Integer contrato, String token) {
		super();
		this.id = id;
		this.contrato = contrato;
		this.token = token;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getContrato() {
		return contrato;
	}

	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}

