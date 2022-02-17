package com.digital.opuserp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.jcabi.aspects.Timeable;

@Entity
@Table(name="agenda_renovacoes")
public class AgendaRenovacoes {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name="CONTRATO_ID")
	private AcessoCliente contrato;
	
	@Column(name="STATUS")
	private String status;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA")
	private Date data;
	
	public AgendaRenovacoes(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AcessoCliente getContrato() {
		return contrato;
	}

	public void setContrato(AcessoCliente contrato) {
		this.contrato = contrato;
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

	public AgendaRenovacoes(Integer id, AcessoCliente contrato, String status,
			Date data) {
		
		this.id = id;
		this.contrato = contrato;
		this.status = status;
		this.data = data;
	}
	
	
	
}
