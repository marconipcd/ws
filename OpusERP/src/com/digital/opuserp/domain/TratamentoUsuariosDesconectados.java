package com.digital.opuserp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.jcabi.aspects.Timeable;

@Entity
@Table(name="tratamento_usuarios_desconectados")
public class TratamentoUsuariosDesconectados {

	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="CONTRATO")
	private Integer contrato;
	
	@Column(name="QTD")
	private Integer qtd;
	
	@Column(name="OPERADOR")
	private String operador;
	
	@Column(name="STATUS")
	private String status;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name="DATA_CONCLUSAO")
	private Date data_conclusao;
	
	public TratamentoUsuariosDesconectados(){
		
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

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getData_conclusao() {
		return data_conclusao;
	}

	public void setData_conclusao(Date data_conclusao) {
		this.data_conclusao = data_conclusao;
	}
	
	
}
