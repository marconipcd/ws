package com.digital.opuserp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="beneficio")
public class Beneficio {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID") 
	private Integer id;
	
	@Column(name="NOME")
	private String nome;
	
	@Column(name="BENEFICIO_ADESAO") 
	private double beneficio_adesao;
	
	@Column(name="BENEFICIO_COMODATO")
	private double beneficio_comodato;
	
	@Column(name="ISENCAO_TAXA_INSTALACAO")
	private double isencao_taxa_instalacao;
	
	@Column(name="ISENCAO_MANU_SERVICO")
	private double isencao_manu_servico;
	
	public Beneficio(){
		
	}

	public Beneficio(Integer id,String nome, double beneficio_adesao,
			double beneficio_comodato, double isencao_taxa_instalacao,
			double isencao_manu_servico) {
		super();
		this.id = id;
		this.nome=nome;
		this.beneficio_adesao = beneficio_adesao;
		this.beneficio_comodato = beneficio_comodato;
		this.isencao_taxa_instalacao = isencao_taxa_instalacao;
		this.isencao_manu_servico = isencao_manu_servico;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome(){
		return nome;
	}
	
	public void setNome(String nome){
		this.nome = nome;
	}

	public double getBeneficio_adesao() {
		return beneficio_adesao;
	}

	public void setBeneficio_adesao(double beneficio_adesao) {
		this.beneficio_adesao = beneficio_adesao;
	}

	public double getBeneficio_comodato() {
		return beneficio_comodato;
	}

	public void setBeneficio_comodato(double beneficio_comodato) {
		this.beneficio_comodato = beneficio_comodato;
	}

	public double getIsencao_taxa_instalacao() {
		return isencao_taxa_instalacao;
	}

	public void setIsencao_taxa_instalacao(double isencao_taxa_instalacao) {
		this.isencao_taxa_instalacao = isencao_taxa_instalacao;
	}

	public double getIsencao_manu_servico() {
		return isencao_manu_servico;
	}

	public void setIsencao_manu_servico(double isencao_manu_servico) {
		this.isencao_manu_servico = isencao_manu_servico;
	}

		
}
