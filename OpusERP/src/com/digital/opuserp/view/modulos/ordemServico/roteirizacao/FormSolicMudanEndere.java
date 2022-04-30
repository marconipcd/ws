package com.digital.opuserp.view.modulos.ordemServico.roteirizacao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.persistence.EntityManager;

import com.digital.opuserp.OpusERP4UI;
import com.digital.opuserp.domain.AcessoCliente;
import com.digital.opuserp.domain.Cliente;
import com.digital.opuserp.domain.Empresa;
import com.digital.opuserp.domain.Endereco;
import com.digital.opuserp.domain.Ose;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.DataUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.server.VaadinService;
import com.vaadin.server.StreamResource.StreamSource;

import elemental.html.ImageData;

public class FormSolicMudanEndere implements StreamSource {

	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	URL url  = getClass().getResource("/com/digital/opuserp/img/quadrado.png");	
	URL urlQuadVazio  = getClass().getResource("/com/digital/opuserp/img/quadrado_img_branco.png");
	
	Image imgQuad = Image.getInstance(url);
	Image imgQuadVazio = Image.getInstance(urlQuadVazio);
	
	byte[] logo;
	
	public FormSolicMudanEndere(Integer cod)throws Exception {

		EntityManager em = ConnUtil.getEntity();		
		Document doc = new Document(PageSize.A4, 50, 50, 14, 14);		
				
		try {
			PdfWriter.getInstance(doc, baos);
			doc.open();
			
			DataUtil dUtil = new DataUtil();
			
			
			Empresa empresa = em.find(Empresa.class, OpusERP4UI.getEmpresa().getId());
			logo = empresa.getLogo_empresa();
			Ose ose = em.find(Ose.class, cod);
			Cliente cliente = ose.getCliente();
			
			Endereco  end = null;
			if(ose.getEnd()!=null){
				end = ose.getEnd();				
			}

			AcessoCliente acesso = null;
			if(ose.getContrato()!=null){
			  acesso = ose.getContrato();
			}
	
			String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();			
			Image img_cabecalho = Image.getInstance(basepath + "/WEB-INF/boleto/TIMBRADO-CABECALHO.png");
			img_cabecalho.setWidthPercentage(30f);
			img_cabecalho.setAlignment(Element.ALIGN_RIGHT);
	
			PdfPCell p_cell_cabecalho = new PdfPCell();			
			p_cell_cabecalho.setPaddingBottom(0);			
			p_cell_cabecalho.addElement(img_cabecalho);
						
			p_cell_cabecalho.setBorderWidthBottom(0);
			p_cell_cabecalho.setBorderWidthTop(0);
			p_cell_cabecalho.setBorderWidthLeft(0);
			p_cell_cabecalho.setBorderWidthRight(0);
			
			PdfPTable tb_cabecalho = new PdfPTable(new float[] {1f});
			tb_cabecalho.setWidthPercentage(100f);
			tb_cabecalho.addCell(p_cell_cabecalho);		
			
			doc.add(tb_cabecalho);
			
			PdfPTable tb_cod_os = new PdfPTable(new float[] {1f});
			tb_cod_os.setWidthPercentage(100f);
			tb_cod_os.addCell(getPdfCell("OS: "+ose.getId().toString()+" ", Element.ALIGN_RIGHT, fConteudo, false, 15));			
			doc.add(tb_cod_os);
			
			PdfPTable tb_data_topo = new PdfPTable(new float[] {1f});
			tb_data_topo.setWidthPercentage(100f);
			tb_data_topo.addCell(getPdfCell("Belo Jardim, "+dUtil.getDataExtensoMes(new Date())+".", Element.ALIGN_RIGHT, fConteudo, false, 15));			
			doc.add(tb_data_topo);
									
			PdfPTable tb_titulo = new PdfPTable(new float[] {1f});
			tb_titulo.setWidthPercentage(100f);
			tb_titulo.addCell(getPdfCell("SOLICITAÇÃO MUDANÇA DE ENDEREÇO", Element.ALIGN_CENTER, fTitulo, false, 25));			
			doc.add(tb_titulo);
			
			PdfPTable tb_texto1 = new PdfPTable(new float[] {1f});
			tb_texto1.setWidthPercentage(100f);
			tb_texto1.addCell(getPdfCell("                       Eu "+cliente.getNome_razao()+", CPF: "+cliente.getDoc_cpf_cnpj()+" "
					+ "titular do Contrato de Acesso à Internet sob o nº "+acesso.getId().toString()+" , solicito à DIGITAL TECNOLOGIA E TELECOMUNICAÇÃO LTDA "
					+ "a mudança de endereço para o seguinte local : \n\n", Element.ALIGN_JUSTIFIED, fConteudo, false, 15));			
			doc.add(tb_texto1);
			
			PdfPTable tb_texto2 = new PdfPTable(new float[] {1f});
			tb_texto2.setWidthPercentage(100f);
			tb_texto2.addCell(getPdfCell(""
					+ "Novo Endereço: "+acesso.getEndereco().getEndereco()+" "
					+ "Nº: "+acesso.getEndereco().getNumero()+" \n"
					+ "Bairro: "+acesso.getEndereco().getBairro()+" "
					+ "CEP: "+acesso.getEndereco().getCep()+" \n"
					+ "Ponto Referência: "+acesso.getEndereco().getReferencia()+" \n"
					+ "Localidade: "+acesso.getEndereco().getCidade()+"\n\n" , Element.ALIGN_JUSTIFIED, fConteudoBold, false, 15));			
			doc.add(tb_texto2);
			
			PdfPTable tb_texto3 = new PdfPTable(new float[] {1f});
			tb_texto3.setWidthPercentage(100f);
			tb_texto3.addCell(getPdfCell("                       Estou ciente que a validação da conexão ao novo endereço, estará sujeita ao tipo de "
					+ "tecnologia existente no local e a viabilidade técnica. \n\n" , Element.ALIGN_JUSTIFIED, fConteudo, false, 15));			
			doc.add(tb_texto3);
			
			PdfPTable tb_acord = new PdfPTable(new float[] {1f});
			tb_acord.setWidthPercentage(100f);
			tb_acord.addCell(getPdfCell("De acordo: " , Element.ALIGN_LEFT, fConteudo, false, 65));			
			doc.add(tb_acord);
			
			PdfPTable tb_assinatura = new PdfPTable(new float[] {1f});
			tb_assinatura.setWidthPercentage(100f);
			tb_assinatura.addCell(getPdfCell("__________________________________________________________ \n Titular Solicitante" , Element.ALIGN_CENTER, fConteudo, false, 45));			
			doc.add(tb_assinatura);
			
			//String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();			
			Image img = Image.getInstance(basepath + "/WEB-INF/boleto/TIMBRADO-RODAPE.png");
			img.setWidthPercentage(100f);
						
			PdfPCell p_cell = new PdfPCell();			
			p_cell.setPaddingBottom(0);			
			p_cell.addElement(img);
			
			p_cell.setBorderWidthBottom(0);
			p_cell.setBorderWidthTop(0);
			p_cell.setBorderWidthLeft(0);
			p_cell.setBorderWidthRight(0);
			
			PdfPTable tb_rodape = new PdfPTable(new float[] {1f});
			tb_rodape.setWidthPercentage(100f);
			tb_rodape.addCell(p_cell);		
			
			doc.add(tb_rodape);
						
				
				
		} finally {
			if (doc.isOpen() && doc != null) {
				doc.close();
			}
		}

	}
		

	private PdfPCell getPdfCellInstalacao(String s, Integer e, Font f, boolean fundoCinza, Integer padding_bottom){
		Paragraph p_lista_verificacao_ambiente = new Paragraph(s, f);	
		p_lista_verificacao_ambiente.setAlignment(e);		
		
		PdfPCell p_cell_lista_verif = new PdfPCell();
		if(fundoCinza){
			p_cell_lista_verif.setBackgroundColor(new BaseColor(216,216,216));
		}
		p_cell_lista_verif.setPaddingBottom(padding_bottom);		
		p_cell_lista_verif.setBorderWidthBottom(0);
		p_cell_lista_verif.setBorderWidthLeft(0);
		p_cell_lista_verif.setBorderWidthRight(0);
		p_cell_lista_verif.setBorderWidthTop(0);
		p_cell_lista_verif.addElement(p_lista_verificacao_ambiente);
		
		return p_cell_lista_verif;
	}
	
	private PdfPCell getPdfCell4(String s, Integer e, Font f, boolean fundoCinza, Integer padding_bottom,Integer padding_top){
		Paragraph p_lista_verificacao_ambiente = new Paragraph(s, f);	
		p_lista_verificacao_ambiente.setAlignment(e);		
		
		PdfPCell p_cell_lista_verif = new PdfPCell();
		if(fundoCinza){
			p_cell_lista_verif.setBackgroundColor(new BaseColor(216,216,216));
		}
		p_cell_lista_verif.setPaddingBottom(10);
		p_cell_lista_verif.setPaddingTop(padding_top);
		p_cell_lista_verif.setPaddingRight(20);
		
		p_cell_lista_verif.addElement(p_lista_verificacao_ambiente);
		
		return p_cell_lista_verif;
	}
	
	private PdfPCell getPdfCell(String s, Integer e, Font f, boolean fundoCinza, Integer padding_bottom,Integer padding_top){
		Paragraph p_lista_verificacao_ambiente = new Paragraph(s, f);	
		p_lista_verificacao_ambiente.setAlignment(e);		
		
		PdfPCell p_cell_lista_verif = new PdfPCell();
		if(fundoCinza){
			p_cell_lista_verif.setBackgroundColor(new BaseColor(216,216,216));
		}
		p_cell_lista_verif.setPaddingBottom(padding_bottom);
		p_cell_lista_verif.setPaddingTop(padding_top);		
		
		p_cell_lista_verif.addElement(p_lista_verificacao_ambiente);
		
		return p_cell_lista_verif;
	}
	private PdfPCell getPdfCell2(String s, Integer e, Font f, boolean fundoCinza, Integer padding_bottom){
		Paragraph p_lista_verificacao_ambiente = new Paragraph(s, f);	
		p_lista_verificacao_ambiente.setAlignment(e);		
		
		PdfPCell p_cell_lista_verif = new PdfPCell();
		p_cell_lista_verif.setBorderWidthLeft(0);
		p_cell_lista_verif.setBorderWidthRight(0);
		p_cell_lista_verif.setBorderWidthBottom(0);
		p_cell_lista_verif.setBorderWidthTop(0);
		
		if(fundoCinza){
			p_cell_lista_verif.setBackgroundColor(new BaseColor(216,216,216));
		}
		p_cell_lista_verif.setPaddingBottom(padding_bottom);		
		
		p_cell_lista_verif.addElement(p_lista_verificacao_ambiente);
		
		return p_cell_lista_verif;
	}
	
	private PdfPCell getPdfCell3(String s, Integer e, Font f, boolean fundoCinza, Integer padding_bottom){
		Paragraph p_lista_verificacao_ambiente = new Paragraph(s, new Font(FontFamily.HELVETICA, 12));	
		p_lista_verificacao_ambiente.setAlignment(e);		
		
		PdfPCell p_cell_lista_verif = new PdfPCell();
		p_cell_lista_verif.setBorderWidthLeft(1);
		p_cell_lista_verif.setBorderWidthRight(0);
				
		if(fundoCinza){
			p_cell_lista_verif.setBackgroundColor(new BaseColor(216,216,216));
		}
		p_cell_lista_verif.setPaddingBottom(padding_bottom);
		p_cell_lista_verif.setPaddingTop(20);	
		
		p_cell_lista_verif.addElement(p_lista_verificacao_ambiente);
		
		return p_cell_lista_verif;
	}
	private PdfPCell getPdfCell(String s, Integer e, Font f, boolean fundoCinza, Integer padding_bottom){
		Paragraph p_lista_verificacao_ambiente = new Paragraph(s, f);	
		p_lista_verificacao_ambiente.setAlignment(e);
		p_lista_verificacao_ambiente.setExtraParagraphSpace(5f);
		
		PdfPCell p_cell_lista_verif = new PdfPCell();
		if(fundoCinza){
			p_cell_lista_verif.setBackgroundColor(new BaseColor(216,216,216));
		}
		p_cell_lista_verif.setPaddingBottom(padding_bottom);			
		p_cell_lista_verif.addElement(p_lista_verificacao_ambiente);
		
		p_cell_lista_verif.setBorderWidthBottom(0);
		p_cell_lista_verif.setBorderWidthTop(0);
		p_cell_lista_verif.setBorderWidthLeft(0);
		p_cell_lista_verif.setBorderWidthRight(0);
		
		return p_cell_lista_verif;
	}

	Font fCaptions = new Font(FontFamily.HELVETICA, 6);
	Font fCaptionsBold = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
	Font fCampo = new Font(FontFamily.HELVETICA, 5);
	Font fCampoBold = new Font(FontFamily.HELVETICA, 5, Font.BOLD);
	Font fConteudo = new Font(FontFamily.HELVETICA, 12);
	Font fConteudoBold = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
	Font fTitulo  = new Font(FontFamily.HELVETICA, 18, Font.NORMAL);
	Font fSubTitulo  = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
	Font fCab = new Font(FontFamily.HELVETICA, 8);
	
	private PdfPCell getPdfCellComCheckInstalacao(String s){
		
		PdfPCell tbRow2_cell1_Filtro_1_quad = new PdfPCell();
		tbRow2_cell1_Filtro_1_quad.setPaddingBottom(0);
		tbRow2_cell1_Filtro_1_quad.setPaddingTop(0); 
		tbRow2_cell1_Filtro_1_quad.setBorderWidthBottom(0);
		tbRow2_cell1_Filtro_1_quad.setBorderWidthTop(0);
		tbRow2_cell1_Filtro_1_quad.setBorderWidthLeft(0);
		tbRow2_cell1_Filtro_1_quad.setBorderWidthRight(0);
		tbRow2_cell1_Filtro_1_quad.addElement(imgQuad);
		
		PdfPCell tbCell_Texto = new PdfPCell();		
		tbCell_Texto.setPaddingBottom(0);
		tbCell_Texto.setPaddingTop(0);
		
		tbCell_Texto.setBorderWidthBottom(0);
		tbCell_Texto.setBorderWidthTop(0);
		tbCell_Texto.setBorderWidthLeft(0);
		tbCell_Texto.setBorderWidthRight(0);
		tbCell_Texto.addElement(new Paragraph(s, fCampo));
		
		PdfPTable tb_analise_preliminar_7_1_0 = new PdfPTable(new float[] {0.08f,1f});
		tb_analise_preliminar_7_1_0.setWidthPercentage(100f);										
		tb_analise_preliminar_7_1_0.addCell(tbRow2_cell1_Filtro_1_quad);
		tb_analise_preliminar_7_1_0.addCell(tbCell_Texto);
		
		PdfPCell tb_analise_preliminar_7_1_cell = new PdfPCell();
		tb_analise_preliminar_7_1_cell.setPaddingBottom(0);
		tb_analise_preliminar_7_1_cell.setPaddingTop(0);
		tb_analise_preliminar_7_1_cell.setBorderWidthBottom(0);
		tb_analise_preliminar_7_1_cell.setBorderWidthTop(0);
		tb_analise_preliminar_7_1_cell.setBorderWidthLeft(0);
		tb_analise_preliminar_7_1_cell.setBorderWidthRight(0);
		tb_analise_preliminar_7_1_cell.addElement(tb_analise_preliminar_7_1_0);
		
		return tb_analise_preliminar_7_1_cell;
	}
	
	private PdfPCell getPdfCellComCheck(String s){
		
		PdfPCell tbRow2_cell1_Filtro_1_quad = new PdfPCell();
		tbRow2_cell1_Filtro_1_quad.setPaddingBottom(0);
		tbRow2_cell1_Filtro_1_quad.setPaddingTop(0); 
		tbRow2_cell1_Filtro_1_quad.setBorderWidthBottom(0);
		tbRow2_cell1_Filtro_1_quad.setBorderWidthTop(0);
		tbRow2_cell1_Filtro_1_quad.setBorderWidthLeft(0);
		tbRow2_cell1_Filtro_1_quad.setBorderWidthRight(0);
		tbRow2_cell1_Filtro_1_quad.addElement(imgQuad);
		
		PdfPCell tbCell_Capacete_jugular = new PdfPCell();
		tbCell_Capacete_jugular.setPadding(0);
		tbCell_Capacete_jugular.setBorderWidthBottom(0);
		tbCell_Capacete_jugular.setBorderWidthTop(0);
		tbCell_Capacete_jugular.setBorderWidthLeft(0);
		tbCell_Capacete_jugular.setBorderWidthRight(0);
		tbCell_Capacete_jugular.addElement(new Paragraph(s, fCaptionsBold));
		
		
		
		PdfPTable tb_analise_preliminar_7_1_0 = new PdfPTable(new float[] {0.18f,1f});
		tb_analise_preliminar_7_1_0.setWidthPercentage(100f);										
		tb_analise_preliminar_7_1_0.addCell(tbRow2_cell1_Filtro_1_quad);
		tb_analise_preliminar_7_1_0.addCell(tbCell_Capacete_jugular);
		
		PdfPCell tb_analise_preliminar_7_1_cell = new PdfPCell();
		tb_analise_preliminar_7_1_cell.setBorderWidthBottom(0);
		tb_analise_preliminar_7_1_cell.setBorderWidthTop(0);
		tb_analise_preliminar_7_1_cell.setBorderWidthLeft(0);
		tb_analise_preliminar_7_1_cell.setBorderWidthRight(0);
		tb_analise_preliminar_7_1_cell.addElement(tb_analise_preliminar_7_1_0);
		
		return tb_analise_preliminar_7_1_cell;
	}
	
	@Override
	public InputStream getStream() {
		// TODO Auto-generated method stub
		return new ByteArrayInputStream(baos.toByteArray());
	}

}

