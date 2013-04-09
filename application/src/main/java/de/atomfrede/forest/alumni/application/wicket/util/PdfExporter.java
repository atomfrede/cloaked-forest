package de.atomfrede.forest.alumni.application.wicket.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfExporter {

	public static final String PDF = "<html><h1>Hello World</h1></html>";

	public PdfExporter() {
	}

	public File generatePdfFile() {
		try {
			File tempPdfFile = File.createTempFile("members", "pdf");
			
			
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(PDF);
			renderer.layout();
			OutputStream outputStream = new FileOutputStream(tempPdfFile);
			renderer.createPDF(outputStream);
			

			// Finishing up
			renderer.finishPDF();
			
			return tempPdfFile;
		} catch (Exception e) {

		}
		
		return null;
	}
}
