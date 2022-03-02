package com.digital.opuserp.view.modulos.relatorio.Rma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.digital.opuserp.OpusERP4UI;
import com.digital.opuserp.domain.Empresa;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.DataUtil;
import com.digital.opuserp.view.modulos.relatorio.SearchParameters;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.data.Container;
import com.vaadin.server.StreamResource.StreamSource;

public class ExportarRelatorioRma implements StreamSource {

	EntityManager em = ConnUtil.getEntity();
	
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	Document doc;

	//Estilos de Fonts
	Font fCaptions = new Font(FontFamily.HELVETICA, 6);
	Font fCaptionsBold = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
	Font fCampo = new Font(FontFamily.HELVETICA, 5);
	Font fCampoBold = new Font(FontFamily.HELVETICA, 5, Font.BOLD);
	Font fConteudo = new Font(FontFamily.HELVETICA, 7, Font.BOLD);
	Font fTitulo  = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
	Font fSubTitulo  = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
	Font fCab = new Font(FontFamily.HELVETICA, 8);
	Font fValores = new Font(FontFamily.COURIER, 6, Font.NORMAL);

	public ExportarRelatorioRma(String tipo,String order,String orientacao,String resumo, List<SearchParameters> lista, List<Object> columns, 
			Container container)throws Exception {

		
		
		if(orientacao.equals("RETRATO")){
			doc = new Document(PageSize.A4, 24, 24, 24, 24);
		}else{
			doc = new Document(PageSize.A4.rotate(), 24, 24, 24, 24);
		}
		
		try {

			PdfWriter writer = PdfWriter.getInstance(doc, baos);
			doc.open();
			
			doc.add(buildCabecalho());
			doc.add(buildTitulo("RELATÓRIO DE RMA"));			
			doc.add(buildFiltros(lista));			
			doc.add(buildConfigTipo(tipo, orientacao, order));		
			
			float[] f = configSizeColumn(columns);				

			doc.add(buildConteudoTopo(f, columns));			
			
			buildConteudo(container,columns, f);			
			
			Paragraph pQtdRegistro = new Paragraph(String.valueOf(container.getItemIds().size())+" Registros Encontrados",fCampo);
			pQtdRegistro.setAlignment(Element.ALIGN_LEFT);
			doc.add(pQtdRegistro);
					
			Paragraph pResumo = new Paragraph("RESUMO:",fCab);
			pResumo.setAlignment(Element.ALIGN_LEFT);
			PdfPCell pCellRe = new PdfPCell();
			pCellRe.setBorderWidth(0);
			pCellRe.addElement(pResumo);		
			
			Paragraph pResumoVl = new Paragraph(changeHeaderColumn(selectCampo(resumo)),fSubTitulo);
			pResumo.setAlignment(Element.ALIGN_LEFT);
			PdfPCell pCellReVl = new PdfPCell();
			pCellReVl.setBorderWidth(0);
			pCellReVl.addElement(pResumoVl);
			
			PdfPTable tbResu = new PdfPTable(new float[]{0.25f,1f});
			tbResu.setWidthPercentage(100f);	
			tbResu.addCell(pCellRe);
			tbResu.addCell(pCellReVl);
			tbResu.setSpacingBefore(20);
			tbResu.setSpacingAfter(10);
			
			doc.add(tbResu);
			
//			for (RmaDetalhe c :(List<RmaDetalhe>) qGroup.getResultList()) {
//				
//				Paragraph pResum = new Paragraph();
//				
//				if(selectCampo(resumo).equals("rma_mestre_id.fornecedor.razao_social") || 
//						selectCampo(resumo).equals("produto.nome") || 
//						selectCampo(resumo).equals("rma_mestre_id.status") || 
//						selectCampo(resumo).equals("rma_mestre_id.data_cadastro")){
//				
//					pResum = new Paragraph(c.getColuna(), fCaptionsBold);				
//					pResum.setAlignment(Element.ALIGN_LEFT);
//					
//				}else{
//
//					if(rootGroup.get(selectCampo(resumo)).getJavaType().equals(String.class)){
//						pResum = new Paragraph(c.getColuna(), fCaptionsBold);
//					}
//					
//					if(rootGroup.get(selectCampo(resumo)).getJavaType().equals(Date.class)){
//						SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy");
//						pResum = new Paragraph(smf.format(c.getColuna_date()), fCaptionsBold);					
//					}
//					pResum.setAlignment(Element.ALIGN_LEFT);
//				}
//				
//				PdfPCell pCellResumo = new PdfPCell();
//				pCellResumo.setPaddingTop(0);
//				pCellResumo.setPaddingBottom(4f);
//				pCellResumo.addElement(pResum);
//				pCellResumo.setBackgroundColor(new BaseColor(232, 235, 237));
//				pCellResumo.setBorderColor(new BaseColor(255, 255, 255));	
//				pCellResumo.setBorderWidth(1.5f);
//				
//				Paragraph pResumolVl = new Paragraph(c.getQtd().toString(),fCaptions);
//				pResumolVl.setAlignment(Element.ALIGN_RIGHT);
//				
//				PdfPCell pCellResumoVl = new PdfPCell();
//				pCellResumoVl.addElement(pResumolVl);
//				pCellResumoVl.setBackgroundColor(new BaseColor(232, 235, 237));
//				pCellResumoVl.setBorderColor(new BaseColor(255, 255, 255));	
//				pCellResumoVl.setBorderWidth(1.5f);
//				
//				Paragraph pResumoVazio = new Paragraph("");
//				
//				PdfPCell pCellResumoVazio = new PdfPCell();
//				pCellResumoVazio.addElement(pResumoVazio);
//				pCellResumoVazio.setBorderColor(new BaseColor(255, 255, 255));	
//				pCellResumoVazio.setBorderWidth(1.5f);
//				
//				PdfPTable tbResumo = new PdfPTable(new float[]{0.55f,0.08f,1f});
//				tbResumo.setWidthPercentage(100f);	
//				tbResumo.addCell(pCellResumo);
//				tbResumo.addCell(pCellResumoVl);
//				tbResumo.addCell(pCellResumoVazio);
//				
//				doc.add(tbResumo);			
//			}
			
			Paragraph ptotal = new Paragraph("TOTAL:",fCab);
			ptotal.setAlignment(Element.ALIGN_LEFT);
			PdfPCell pCellTotal = new PdfPCell();
			pCellTotal.setBorderWidth(0);	
			pCellTotal.addElement(ptotal);
			
			Paragraph pTotalVl = new Paragraph(""+0,fSubTitulo);
			pTotalVl.setAlignment(Element.ALIGN_RIGHT);
			PdfPCell pCellTotalVl = new PdfPCell();
			pCellTotalVl.setBorderWidth(0);
			pCellTotalVl.addElement(pTotalVl);
			
			Paragraph pTotalVazio = new Paragraph("");
			
			PdfPCell pCellTotalVazio = new PdfPCell();
			pCellTotalVazio.addElement(pTotalVazio);
			pCellTotalVazio.setBorderWidth(0);
			
			PdfPTable tbTotal = new PdfPTable(new float[]{0.55f,0.08f,1f});
			tbTotal.setWidthPercentage(100f);	
			tbTotal.addCell(pCellTotal);
			tbTotal.addCell(pCellTotalVl);
			tbTotal.addCell(pCellTotalVazio);
			tbTotal.setSpacingBefore(10);
			
			doc.add(tbTotal);
			
		} finally {
			if (doc.isOpen() && doc != null) {
				doc.close();
			}
		}

	}
	
	
	private void buildConteudo(Container container, List<Object> columns,float[] f) {
		try{
			Collection<?> itemIds = container.getItemIds();
			Collection<?> propertyIds = container.getContainerPropertyIds();
				
			for (Object itemId: itemIds) {
			
				PdfPTable tbConteudo = new PdfPTable(f);
				tbConteudo.setWidthPercentage(100f);
			
				for (Object propertyId : columns) {
								
						PdfPCell pCell = new PdfPCell();
						pCell.setPaddingTop(0);
						pCell.setPaddingBottom(4);
						pCell.setBackgroundColor(new BaseColor(232, 235, 237));
						pCell.setBorderColor(new BaseColor(255, 255, 255));	
						pCell.setBorderWidth(1.5f);
	
						String valor = container.getItem(itemId).getItemProperty(propertyId).getValue() != null ?
								container.getItem(itemId).getItemProperty(propertyId).getValue().toString() : "";
				
						Paragraph valorColuna = new Paragraph(valor,fCampo);									
						pCell.addElement(valorColuna);							 
					
						tbConteudo.addCell(pCell);			
				
				}
				
				doc.add(tbConteudo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}


	private Element buildConteudoTopo(float[] f, List<Object> columns) {
		PdfPTable tbTopo = new PdfPTable(f);
		tbTopo.setWidthPercentage(100f);
		for (Object c : columns) {
			PdfPCell pCell = new PdfPCell(new Phrase(changeHeaderColumn(c.toString()), fCampoBold));
			pCell.setPaddingTop(2);
			pCell.setPaddingBottom(4);
			pCell.setBackgroundColor(new BaseColor(114, 131, 151));
			pCell.setBorderColor(new BaseColor(255, 255, 255));	
			pCell.setBorderWidth(1.5f);
			tbTopo.addCell(pCell);
		}			
		
		return tbTopo;
	}


	private float[] configSizeColumn(List<Object> columns) {
		float[] f = new float[columns.size()];
		
		Integer i=0;
		for (Object c : columns) {

			if(changeHeaderColumn(c.toString()).equals("COD")){
				f[i] = (0.05f);		
			}	
			if(changeHeaderColumn(c.toString()).equals("FORNECEDOR")){
				f[i] = (0.40f);		
			}
			if(changeHeaderColumn(c.toString()).equals("PRODUTO")){
				f[i] = (0.45f);		
			}
			if(changeHeaderColumn(c.toString()).equals("STATUS RMA")){
				f[i] = (0.15f);		
			}
			if(changeHeaderColumn(c.toString()).equals("STATUS ITEM")){
				f[i] = (0.15f);		
			}
			if(changeHeaderColumn(c.toString()).equals("DATA")){
				f[i] = (0.15f);		
			}
			
			i++;
 		 }	
		
		return f;
	}


	private Element buildConfigTipo(String tipo, String orientacao, String order) {
		//TIPO		
		StringBuilder SbForm= new StringBuilder();
		StringBuilder SbVl= new StringBuilder();
		
		SbForm.append("TIPO:"+"\n"+"ORIENTAÇÃO:"+"\n"+"ORDENAÇÃO"+"\n");			
		SbVl.append(tipo+"\n"+orientacao+"\n"+changeHeaderColumn(selectCampo(order))+"\n");

		Paragraph formato = new Paragraph(SbForm.toString(),fCab);
		Paragraph ordenacao = new Paragraph(SbVl.toString(),fSubTitulo);
		
		PdfPCell pCellForm = new PdfPCell();
		pCellForm.setBorderWidth(0);
		pCellForm.addElement(formato);		
		
		PdfPCell pCellVl = new PdfPCell();
		pCellVl.setBorderWidth(0);
		pCellVl.addElement(ordenacao);
		
		PdfPTable tbform = new PdfPTable(new float[]{0.25f,1f});
		tbform.setWidthPercentage(100f);	
		tbform.addCell(pCellForm);
		tbform.addCell(pCellVl);
		tbform.setSpacingAfter(10);
		
		return tbform;
	}


	private Element buildFiltros(List<SearchParameters> lista) {
		//FiILTROS					
		StringBuilder SbTipo = new StringBuilder();
		StringBuilder SbOperad =  new StringBuilder();
		StringBuilder SbValor = new StringBuilder();
		
		if (lista.size() > 0) {
			for (SearchParameters s : lista) {	

				SbTipo.append(changeHeaderColumn(s.getCampo())+"\n");
				SbOperad.append(s.getOperador()+"\n");
				SbValor.append(s.getValor()+"\n");
				
			}
		}
		Paragraph pCampo = new Paragraph(SbTipo.toString(),fSubTitulo);
		pCampo.setAlignment(Element.ALIGN_LEFT);
		Paragraph pOperqador = new Paragraph(SbOperad.toString(),fCab);
		pOperqador.setAlignment(Element.ALIGN_LEFT);
		Paragraph pValor= new Paragraph(SbValor.toString(),fSubTitulo);
		pValor.setAlignment(Element.ALIGN_LEFT);

		PdfPCell pCellTipo = new PdfPCell();
		pCellTipo.setBorderWidth(0);
		pCellTipo.addElement(pCampo);	
		
		PdfPCell pCellOperador = new PdfPCell();
		pCellOperador.setBorderWidth(0);
		pCellOperador.addElement(pOperqador);		
		
		PdfPCell pCellValor = new PdfPCell();
		pCellValor.setBorderWidth(0);
		pCellValor.addElement(pValor);		
		
		PdfPTable tbTipo = new PdfPTable(new float[]{0.32f,0.30f,1f});
		tbTipo.setWidthPercentage(100f);	
		tbTipo.addCell(pCellTipo);
		tbTipo.addCell(pCellOperador);
		tbTipo.addCell(pCellValor);
		tbTipo.setSpacingAfter(10);
		
		return tbTipo;
	}


	private Element buildTitulo(String s) {
		Paragraph pExport = new Paragraph(s,fTitulo);
		pExport.setAlignment(Element.ALIGN_CENTER);
		pExport.setSpacingAfter(10);
		
		return pExport;
	}


	public static JFreeChart generatePieChart(String titulo, Map<String, Integer> lista) {
        DefaultPieDataset dataSet = new DefaultPieDataset();
              
        Set<String> chaves = lista.keySet();  
        for (String chave : chaves)  
        {            
        	System.out.println(chave + lista.get(chave));  
            dataSet.setValue(chave,lista.get(chave));            
        } 
        
        JFreeChart chart = ChartFactory.createPieChart(titulo, dataSet, false, true, false);

        return chart;
    }
	
	public static JFreeChart generateBarChart(String titulo, Map<String, Integer> lista) {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		             
        Set<String> chaves = lista.keySet();
        Integer i = 0;
        for (String chave : chaves)  
        {            
        	i++; 
            dataSet.setValue(lista.get(chave), lista.get(chave), chave);	           
        } 
        
        JFreeChart chart = ChartFactory.createBarChart(titulo,"Categoria","Valor",
                dataSet, PlotOrientation.VERTICAL, false, true, false);

        return chart;
    }

	private String changeHeaderColumn(String s) {

		String filtro = "";
		if(s.equals("id")){
			filtro = "COD";							
		}else if(s.equals("rma_mestre_id.fornecedor.razao_social")){
			filtro = "FORNECEDOR";							
		}else if(s.equals("produto.nome")){
			filtro = "PRODUTO";					
		}else if(s.equals("rma_mestre_id.status")){
			filtro = "STATUS RMA";					
		}else if(s.equals("status")){
			filtro = "STATUS ITEM";					
		}else if(s.equals("rma_mestre_id.data_cadastro")){
			filtro = "DATA";				
		}	
				
		return filtro;

	}
	
	public String selectCampo(String s) {
		
		String filtro = "";
		if(s.equals("Codigo")){
			filtro = "id";												
		}else if(s.equals("Fornecedor")){
			filtro = "rma_mestre_id.fornecedor.razao_social";					
		}else if(s.equals("Produto")){
			filtro = "produto.nome";				
		}else if(s.equals("Status do Rma")){
			filtro = "rma_mestre_id.status";
		}else if(s.equals("Status do Item")){
			filtro = "status";
		}else if(s.equals("Data")){
			filtro = "rma_mestre_id.data_cadastro";
		}		
		return filtro;
	}

	@Override
	public InputStream getStream() {
		// TODO Auto-generated method stub
		return new ByteArrayInputStream(baos.toByteArray());
	}
	
	private PdfPTable buildCabecalho(){
		
					Empresa empresa = em.find(Empresa.class, OpusERP4UI.getEmpresa().getId());
					DataUtil dtUtil = new DataUtil();
					
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");	
					String hora = " às "+ sdf.format(new Date());
					
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
					
					return tbCab;
	}
	

}
