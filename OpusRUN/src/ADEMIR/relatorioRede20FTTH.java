package ADEMIR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import util.Real;
import dao.ContasReceberDAO;
import domain.AcessoCliente;
import domain.Cliente;
import domain.ContasReceber;

public class relatorioRede20FTTH {

	public static void main(String[] args) {
		try{
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
			EntityManager em = emf.createEntityManager();
		
			Query q = em.createNativeQuery("SELECT * FROM acesso_cliente ac WHERE ac.STATUS_2 !='ENCERRADO' AND "
					+ "ac.STATUS_2 !='SUSPENSO' AND ac.BASES_ID=108", AcessoCliente.class);
			
			List<AcessoCliente> contratos = q.getResultList();
			System.out.println("Quantidade de Contratos: "+contratos.size());
			
			StringBuilder sb = new StringBuilder();		
			sb.append(new String("Cod, Cod Cartao, Cliente, Cpf, Rg, Orgao Emissor, Sexo, Telefone 1,Telefone 2,Telefone 3, "
					+ "Telefone 4, Data nascimento, Email, Plano, Valor Plano, Vencimento, Contrato, Concentrador, "
					+ "Onu Serial,Porta GPON,Material,Onu,Regime,Mac,Cto,Login,Usuario Endereço Fixo, Cep,Endereco, "
					+ "Bairro, Cidade, Ponto Referencia,Url Endereco, Status"));
			sb.append(System.getProperty("line.separator"));
			
			File f = new File("F:\\ContratosRede20FTTH.csv");
			BufferedWriter br = new BufferedWriter(new FileWriter(f));  
			 					
				if(f.canWrite()){
				
				for (AcessoCliente c : contratos) {
					
							List<ContasReceber> boletos = ContasReceberDAO.getBoletoPorContrato(c.getId());
							ContasReceber boleto = boletos.get(0);
							
							SimpleDateFormat sdf = new SimpleDateFormat("dd");
							String dia_vencimento = sdf.format(boleto.getData_vencimento());
											
							if(c != null){			
								
								String nome_onu = c.getOnu() != null && c.getOnu().getNome() != null ? c.getOnu().getNome() : "";
								
								sb.append(new String(
										c.getId().toString()+","+
										c.getCodigo_cartao()+","+
										c.getCliente().getNome_razao()+","+
										c.getCliente().getDoc_cpf_cnpj()+","+
										c.getCliente().getDoc_rg_insc_estadual()+","+
										c.getCliente().getOrgao_emissor_rg()+","+
										c.getCliente().getSexo()+","+
										c.getCliente().getDdd_fone1()+" "+c.getCliente().getTelefone1()+","+
										c.getCliente().getDdd_fone2()+" "+c.getCliente().getTelefone2()+","+ 
										c.getCliente().getDdd_cel1()+" "+c.getCliente().getCelular1()+","+
										c.getCliente().getDdd_cel2()+" "+c.getCliente().getCelular2()+","+
										c.getCliente().getData_nascimento()+","+
										c.getCliente().getEmail()+","+
										c.getPlano().getNome()+","+
										Real.formatStringToDB(c.getPlano().getValor())+","+
										dia_vencimento+","+
										c.getContrato().getNome()+","+
										c.getBase().getIdentificacao()+","+
										c.getOnu_serial()+","+
										c.getGpon()+","+
										c.getMaterial().getNome()+","+
										nome_onu+","+
										c.getRegime()+","+
										c.getEndereco_mac()+","+
										c.getSwith().getIdentificacao()+","+
										c.getLogin()+","+
										c.getSenha()+","+
										c.getEndereco_ip()+","+
										c.getEndereco().getCep()+","+
										c.getEndereco().getEndereco()+" "+c.getEndereco().getNumero()+","+
										c.getEndereco().getBairro()+","+
										c.getEndereco().getCidade()+","+
										c.getEndereco().getReferencia()+","+
										c.getEndereco().getLocalizacao()+","+
										c.getStatus_2()+","							
										
										));					
								sb.append(System.getProperty("line.separator"));							
								
							}
						
					
				}
				
				br.write(sb.toString());  
				br.close();		
			
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
