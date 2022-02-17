package com.digital.opuserp.util.boletos.transform;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.exception.GeracaoBoletoException;
import br.com.caelum.stella.boleto.transformer.BoletoWriter;

import br.com.caelum.stella.boleto.transformer.TextWriter;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Writer que sabe escrever num PDF usando IText como dependencia.
 * 
 * @author Cauê Guerra
 * @author Paulo Silveira
 * @author Leonardo Bessa
 */
public class PDFBoletoWriterUtil implements BoletoWriter, TextWriter {

	private static final int NORMAL_SIZE = 8;

	private static final int BIG_SIZE = 10;

	private InputStream stream;

	private final ByteArrayOutputStream bytes;

	private final Document document;

	private PdfWriter writer;

	private BaseFont fonteSimples;

	private BaseFont fonteBold;

	private final PdfContentByte contentByte;

	private final int scale = 1;

	private final PNGPDFTransformerHelperUtil writerHelper;
	
	

	public PDFBoletoWriterUtil(final Rectangle rectangle) {
		bytes = new ByteArrayOutputStream();
		document = new Document(rectangle);

		try {
			writer = PdfWriter.getInstance(document, bytes);

			fonteSimples = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
			fonteBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);

		} catch (DocumentException e) {
			throw new GeracaoBoletoException(e);
		} catch (IOException e) {
			throw new GeracaoBoletoException(e);
		}

		document.open();
		contentByte = writer.getDirectContent();
		document.newPage();
		writerHelper = new PNGPDFTransformerHelperUtil(this);
		
		
	}

	public PDFBoletoWriterUtil() {
		this(PageSize.A4);
	}

	@Override
	public InputStream toInputStream() {
		if (stream == null) {
			document.close();
			stream = new ByteArrayInputStream(bytes.toByteArray());
		}
		return stream;
	}

	@Override
	public void write(final float x, final float y, final String text) {
		write(x, y, text, fonteSimples, NORMAL_SIZE * scale);
	}

	@Override
	public void writeBold(final float x, final float y, final String text) {
		write(x, y, text, fonteBold, BIG_SIZE * scale);
	}

	private void write(final float x, final float y, final String text, final BaseFont font, final int size) {
		checkIfDocIsClosed();
		contentByte.beginText();

		contentByte.setFontAndSize(font, size);
		final float leftMargin = document.leftMargin();
		contentByte.setTextMatrix(x, y);
		contentByte.showText(text);

		contentByte.endText();
	}

	private void checkIfDocIsClosed() {
		if (stream != null) {
			throw new IllegalStateException("boleto ja gerado, voce nao pode mais escrever na imagem");
		}
	}

	@Override
	public void writeImage(final float x, final float y, final BufferedImage image, final float width,
			final float height) throws IOException {
		checkIfDocIsClosed();

		try {
			Image pdfImage = Image.getInstance(image, null);
			pdfImage.setAbsolutePosition(0, 0);
			pdfImage.scaleToFit(width, height);
			PdfTemplate template = contentByte.createTemplate(image.getWidth(), image.getHeight());
			template.addImage(pdfImage);
			final float leftMargin = document.leftMargin();
			
			//System.out.println("Largura:"+document.getPageSize().getWidth());
			//System.out.println("Altura:"+document.getPageSize().getHeight());
			
			
			contentByte.addTemplate(template, x, y);
		} catch (BadElementException e) {
			throw new GeracaoBoletoException(e);
		} catch (DocumentException e) {
			throw new GeracaoBoletoException(e);
		}
	}

	@Override
	public boolean newPage() {
		return document.newPage();
	}

	@Override
	public void write(final Boleto boleto) {
		writerHelper.transform(boleto);
	}
}
