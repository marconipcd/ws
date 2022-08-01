package com.digital.opuserp.view.modulos.crm.crm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.DateTime;

import com.digital.opuserp.OpusERP4UI;
import com.digital.opuserp.domain.Crm;
import com.digital.opuserp.domain.Empresa;
import com.digital.opuserp.domain.Setores;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.DataUtil;
import com.digital.opuserp.view.modulos.relatorio.SearchParameters;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfCell;
import com.vaadin.server.StreamResource.StreamSource;

public class ExportRelatorioCrmRecorrente implements StreamSource {

	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	//Estilos de Fonts
	Font fCaptions = new Font(FontFamily.HELVETICA, 6);
	Font fCaptionsBold = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
	Font fCampo = new Font(FontFamily.HELVETICA, 5);
	Font fCampoBold = new Font(FontFamily.HELVETICA, 5, Font.BOLD);
	Font fConteudo = new Font(FontFamily.HELVETICA, 7, Font.BOLD);
	Font fTitulo  = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
	Font fSubTitulo  = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
	Font fCab = new Font(FontFamily.HELVETICA, 8);

	public ExportRelatorioCrmRecorrente(Setores setor, Date dt1, Date dt2, Integer qtd_recorrencia)throws Exception {

		EntityManager em = ConnUtil.getEntity();		
		
		Document doc;		
		doc = new Document(PageSize.A4, 24, 24, 24, 24);	

		try {

			PdfWriter writer = PdfWriter.getInstance(doc, baos);
			doc.open();
						
			

			// Cabeçalho
			Empresa empresa = em.find(Empresa.class, OpusERP4UI.getEmpresa().getId());
			DataUtil dtUtil = new DataUtil();
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");	
			String hora;
			hora = " às "+ sdf.format(new Date());

			String 	date = dtUtil.getDataExtenso(new Date());

			StringBuilder SbCabecalho = new StringBuilder();
			SbCabecalho.append("OPUS ERP4"+"\n"+"EMPRESA:"+"\n"+"EMISSÃO:"+"\n"+"OPERADOR:");
			
			StringBuilder SbCabecalhoVl = new StringBuilder();
			SbCabecalhoVl.append("\n"+empresa.getNome_fantasia()+"\n"+date+hora+"\n"+OpusERP4UI.getUsuarioLogadoUI().getUsername());
			
			Paragraph Pcabecalho = new Paragraph(SbCabecalho.toString(),fCab);
			Pcabecalho.setAlignment(Element.ALIGN_LEFT);	
			
			Paragraph PcabecalhoVl = new Paragraph(SbCabecalhoVl.toString(),fSubTitulo);										
			PcabecalhoVl.setAlignment(Element.ALIGN_LEFT);
			PcabecalhoVl.setSpacingAfter(20);
				
			PdfPCell cellCab = new PdfPCell();
			cellCab.setBorderWidth(0);
			cellCab.addElement(Pcabecalho);
			
			PdfPCell cellCabVl = new PdfPCell();
			cellCabVl.setBorderWidth(0);
			cellCabVl.addElement(PcabecalhoVl);

			PdfPTable tbCab = new PdfPTable(new float[]{0.25f,1f});
			tbCab.setWidthPercentage(100f);	
			tbCab.addCell(cellCab);
			tbCab.addCell(cellCabVl);
			
			doc.add(tbCab);

			// SUBTITULO
			Paragraph pExport = new Paragraph("RELATÓRIO DE CRMs RECORRENTES",fTitulo);
			pExport.setAlignment(Element.ALIGN_CENTER);
			pExport.setSpacingAfter(10);
			doc.add(pExport);
		
			// Estilos de Fonts
			Font fValores = new Font(FontFamily.COURIER, 6, Font.NORMAL);			
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyMMdd");
			
			Query q1 = em.createNativeQuery("select * from crm c where DATE_FORMAT(data_efetuado, '%y%m%d') >=:dt1 and "
					+ "DATE_FORMAT(data_efetuado, '%y%m%d') <=:dt2 and c.SETOR_ID=:s order by data_efetuado", Crm.class);
			q1.setParameter("dt1", sdf2.format(dt1));
			q1.setParameter("dt2", sdf2.format(dt2));
			q1.setParameter("s", setor.getId());
			
			List<Crm> crms = q1.getResultList();
			
			List<String> columns = new ArrayList<>();
			columns.add("ABERTURA");
			columns.add("CONTEUDO");
			columns.add("CONCLUSÃO");
			columns.add("T. TRAT.");
			columns.add("OPERADOR");
			columns.add("PLANO");
			columns.add("CONCENTRADOR");
			
			float[] f = new float[columns.size()];
			
			Integer i=0;
			for (Object c : columns) {
	
				if(c.toString().equals("PROTOCOLO")){
					f[i] = (0.12f);	
				}
				if(c.toString().equals("ABERTURA")){
					f[i] = (0.12f);	
				}
				if(c.toString().equals("HORA")){
					f[i] = (0.20f);	
				}
				if(c.toString().equals("SETOR")){
					f[i] = (0.35f);	
				}
				if(c.toString().equals("ASSUNTO")){
					f[i] = (0.40f);	
				}	
				if(c.toString().equals("CLIENTE")){
					f[i] = (0.50f);		
				}	
				if(c.toString().equals("CONTEUDO")){
					f[i] = (0.60f);		
				}
			    if(c.toString().equals("CONTATO")){
			    	f[i] = (0.45f);	
			    }         
			    if(c.toString().equals("FORMA CONTATO")){
			    	f[i] = (0.45f);	
			    }  
			    if(c.toString().equals("PREVISÃO")){
			    	f[i] = (0.23f);	
			    }	
			    if(c.toString().equals("CONCLUSÃO")){
			    	f[i] = (0.12f);	
			    }				   
			    if(c.toString().equals("FEEDBACK")){
			    	f[i] = (0.23f);	
			    }	
			    if(c.toString().equals("T. TRAT.")){
			    	f[i] = (0.05f);	
			    }	
			    if(c.toString().equals("OPERADOR")){
			    	f[i] = (0.12f);	
			    }	
			    if(c.toString().equals("STATUS")){
			    	f[i] = (0.20f);	
			    }			    
			    if(c.toString().equals("REAGENDAMENTO")){
			    	f[i] = (0.35f);	
			    }
			    if(c.toString().equals("CONTRATO")){
			    	f[i] = (0.15f);	
			    }
			    if(c.toString().equals("PLANO")){
			    	f[i] = (0.07f);	
			    }
			    if(c.toString().equals("CONCENTRADOR")){
			    	f[i] = (0.07f);	
			    }
			    i++;
     		  }		
			
			PdfPTable tbTopo = new PdfPTable(f);
			tbTopo.setWidthPercentage(100f);				
			
			for (String c : columns) {
				PdfPCell pCell = new PdfPCell(new Phrase(c.toString(), fCampoBold));
				pCell.setPaddingTop(2);
				pCell.setPaddingBottom(4);
				pCell.setBackgroundColor(new BaseColor(114, 131, 151));
				pCell.setBorderColor(new BaseColor(255, 255, 255));	
				pCell.setBorderWidth(1.5f);
				tbTopo.addCell(pCell);				
			}
				
			doc.add(tbTopo);				
			
			
			for (Crm crm : crms) {
				
						DateTime dNow = new DateTime(new Date());
						Date dLast12Months = dNow.minusMonths(12).toDate();
						Query q = em.createQuery("select c from Crm c where c.contrato=:contrato and c.setor=:setor and "
								+ "c.cliente=:cliente and c.data_agendado > :data", Crm.class);
						q.setParameter("setor", crm.getSetor());
						q.setParameter("cliente", crm.getCliente());
						q.setParameter("contrato", crm.getContrato());
						q.setParameter("data", dLast12Months);
						
											
										
						List<Crm> crms_cliente = q.getResultList();
						
						int i2 = 0;
						for (Crm c: crms_cliente) {
							
							String data_efetuado = c.getData_efetuado() != null ? sdf1.format(c.getData_efetuado()) : "";
							
							if(q.getResultList().size() > qtd_recorrencia){
								
								if(i2 == 0){
									PdfPTable tbConteudo2 = new PdfPTable(1);
									tbConteudo2.setWidthPercentage(100f);		
									tbConteudo2.setSpacingBefore(10f);
									tbConteudo2.addCell(getCell(crm.getCliente().getNome_razao())); 
									doc.add(tbConteudo2);	
								}
								
								PdfPTable tbConteudo = new PdfPTable(f);
								tbConteudo.setWidthPercentage(100f);					
														
								tbConteudo.addCell(getCell(sdf1.format(c.getData_cadastro())));
								tbConteudo.addCell(getCell(c.getConteudo()));
								tbConteudo.addCell(getCell(data_efetuado));
								tbConteudo.addCell(getCell(c.getTempo_atendimento()));
								tbConteudo.addCell(getCell(c.getOperador_tratamento()));								
								tbConteudo.addCell(getCell(c.getPlano()));
								tbConteudo.addCell(getCell(c.getConcentrador()));
								doc.add(tbConteudo);					
								
							}
							
							i2++;
						}
			}
			
			
			
			
		} finally {
			if (doc.isOpen() && doc != null) {
				doc.close();
			}
		}

	}
	
	private PdfPCell getCell(String s){
		PdfPCell pCell3 = new PdfPCell();
		pCell3.setPaddingTop(0);
		pCell3.setPaddingBottom(4);
		pCell3.setBackgroundColor(new BaseColor(232, 235, 237));
		pCell3.setBorderColor(new BaseColor(255, 255, 255));	
		pCell3.setBorderWidth(1.5f);
		
		pCell3.addElement(new Paragraph(s,fCampo));
		
		return pCell3;
	}
	
	private PdfPCell getCellTopo(String s){
		PdfPCell pCell3 = new PdfPCell();
		pCell3.setPaddingTop(0);
		pCell3.setPaddingBottom(4);
		pCell3.setBackgroundColor(new BaseColor(232, 235, 237));
		pCell3.setBorderColor(new BaseColor(255, 255, 255));	
		pCell3.setBorderWidth(1.5f);
		
		pCell3.addElement(new Paragraph(s,fCampoBold));
		
		return pCell3;
	}
	
	@Override
	public InputStream getStream() {
		// TODO Auto-generated method stub
		return new ByteArrayInputStream(baos.toByteArray());
	}

}
