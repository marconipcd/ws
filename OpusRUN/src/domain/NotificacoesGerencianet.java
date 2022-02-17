package domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="notificacoes_gerencianet")
public class NotificacoesGerencianet {

	@Id
	@Column(name="ID")
	private Integer id;
	
	@Column(name="COD_TRANSACAO")
	private String cod_transacao;
	
	@Column(name="COD_BOLETO")
	private String cod_boleto;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="DATA")
	private Date data;
	
	public NotificacoesGerencianet(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCod_transacao() {
		return cod_transacao;
	}

	public void setCod_transacao(String cod_transacao) {
		this.cod_transacao = cod_transacao;
	}

	public String getCod_boleto() {
		return cod_boleto;
	}

	public void setCod_boleto(String cod_boleto) {
		this.cod_boleto = cod_boleto;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	
}
