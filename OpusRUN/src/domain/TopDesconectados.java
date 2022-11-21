package domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="top_desconectados")
public class TopDesconectados {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="CONTRATO")
	private Integer contrato;
	
	@Column(name="QTD")
	private Integer qtd;
	
	@Column(name="SITUACAO_CONTRATO")
	private String situacao_contrato;
	
		
	@Column(name="STATUS")
	private String status;
	
	@Column(name="OPERADOR_TRATAMENTO")
	private String operador_tratamento;
	
	@Column(name="DATA_CADASTRO")
	private Date data_cadastro;
	
	public TopDesconectados(){
		
	}

	public TopDesconectados(Integer id, Integer contrato, Integer qtd,
			String situacao_contrato, String status,
			String operador_tratamento, Date data_cadastro) {
		super();
		this.id = id;
		this.contrato = contrato;
		this.qtd = qtd;
		this.situacao_contrato = situacao_contrato;
		this.status = status;
		this.operador_tratamento = operador_tratamento;
		this.data_cadastro = data_cadastro;
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

	public Integer getQtd() {
		return qtd;
	}

	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}

	public String getSituacao_contrato() {
		return situacao_contrato;
	}

	public void setSituacao_contrato(String situacao_contrato) {
		this.situacao_contrato = situacao_contrato;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperador_tratamento() {
		return operador_tratamento;
	}

	public void setOperador_tratamento(String operador_tratamento) {
		this.operador_tratamento = operador_tratamento;
	}

	public Date getData_cadastro() {
		return data_cadastro;
	}

	public void setData_cadastro(Date data_cadastro) {
		this.data_cadastro = data_cadastro;
	}
	
	
}

