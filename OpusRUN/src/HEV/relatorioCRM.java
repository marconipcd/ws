package HEV;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.joda.time.DateTime;

import util.Real;
import domain.Crm;


public class relatorioCRM {

	public static void main(String[] args) {
		
		try{
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
				EntityManager em = emf.createEntityManager();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				Query q1 = em.createNativeQuery("select * from crm c where DATE_FORMAT(data_efetuado, '%y%m%d') >='220522' and "
						+ "DATE_FORMAT(data_efetuado, '%y%m%d') <='220528'", Crm.class);
				
				List<Crm> crms = q1.getResultList();
				
				StringBuilder sb = new StringBuilder();		
				sb.append(new String("Cod#Data Efetuado#Setor#Contrato#Cliente#Assunto#Prioridade#Obs#Status#Operador#Forma contato"));
				sb.append(System.getProperty("line.separator"));
				
				File f = new File("F:\\CrmRecorrente.csv");
				BufferedWriter br = new BufferedWriter(new FileWriter(f));  
				 					
					if(f.canWrite()){				
						for (Crm crm : crms) {							
							DateTime dNow = new DateTime(new Date());
							Date dLast12Months = dNow.minusMonths(12).toDate();
							Query q = em.createQuery("select c from Crm c where c.contrato=:contrato and c.setor=:setor and c.cliente=:cliente and c.data_agendado > :data", Crm.class);
							q.setParameter("setor", crm.getSetor());
							q.setParameter("cliente", crm.getCliente());
							q.setParameter("contrato", crm.getContrato());
							q.setParameter("data", dLast12Months);
							
							List<Crm> crms_cliente = q.getResultList();
							for (Crm c: crms_cliente) {
								
								String data = c.getData_efetuado() != null ? sdf.format(c.getData_efetuado()) : "";
								
								if(q.getResultList().size() > 2){
									sb.append(new String(
											c.getId().toString()+"#"+
											data+"#"+
											c.getSetor().getNome()+"#"+
											c.getContrato().getId()+"#"+
											c.getCliente().getNome_razao()+"#"+
											c.getCrm_assuntos().getNome()+"#"+
											c.getNivel()+"#"+
											c.getConteudo().replaceAll("\\r\\n|\\n", "")+"#"+
											c.getStatus()+"#"+
											c.getOperador_tratamento()+"#"+
											c.getCrm_formas_contato().getNome()+"#"													
											));					
									sb.append(System.getProperty("line.separator"));	
								}
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
