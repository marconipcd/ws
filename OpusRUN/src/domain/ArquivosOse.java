package domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="arquivos_ose")
public class ArquivosOse {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="OSE_ID")
	private Integer ose;
	
	@Column(name="NOME")
	private String nome;
	
	@Lob
	@Column(name="ARQUIVO")
    private byte[] file;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA")
	private Date data;
	
	@Column(name="usuario")
	private String usuario;
	
	public ArquivosOse(){
		
	}
	
	public ArquivosOse(Integer id, Integer ose,String nome, byte[] file,
			Date data) {
		super();
		this.id = id;
		this.ose = ose;
		this.nome = nome;
		this.file = file;
		this.data = data;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Integer getOse() {
		return ose;
	}

	public void setOse(Integer ose) {
		this.ose = ose;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	
}
