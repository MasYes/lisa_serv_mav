/*
 * Copyright (c) 2008, intarsys consulting GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Public License as published by the
 * Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * by MasYes: Практически всё - тупой копипаст экзампла из библиотеки, так что все претензии не ко мне ^^
 */
package lisa;
import java.io.IOException;

import de.intarsys.pdf.parser.COSLoadException;
import de.intarsys.pdf.pd.PDDocument;
import de.intarsys.tools.locator.FileLocator;
import de.intarsys.pdf.content.CSDeviceBasedInterpreter;
import de.intarsys.pdf.content.CSException;
import de.intarsys.pdf.content.text.CSTextExtractor;
import de.intarsys.pdf.cos.COSVisitorException;
import de.intarsys.pdf.pd.PDPage;
import de.intarsys.pdf.pd.PDPageNode;
import de.intarsys.pdf.pd.PDPageTree;
import de.intarsys.pdf.tools.kernel.PDFGeometryTools;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.Iterator;
import java.util.Scanner;


/**
 * Extract complete text from document.
 *
 */
public class ExtractText{

	public static String parse(String file) {
		switch(file.substring(file.lastIndexOf("."))){
			case ".pdf" :	return parsePDF(file);
			case ".txt" :	return parseTXT(file);
			default : throw new UnsupportedFormatException();
		}
	}

	public static String parsePDF(String file) {
		ExtractText client = new ExtractText();
		try {
			return client.run(file);
		} catch (Exception e) {
			Common.createLog(e);
			return "";
		}
	}

	public static String parseTXT(String file) {
		File text = new File(file);
		try{
			System.gc();
			Scanner scan = new Scanner(text);
			String str = "";
			while(scan.hasNext()){
				str += scan.nextLine() + "\n";
			}
			return str;
		}
		catch(IOException e){
			Common.createLog(e);
			return "";
		}
	}

	protected void extractText(PDPageTree pageTree, StringBuilder sb) {
		if(pageTree.getCount()  > 30){
			throw new LargeFileException();
		}
		for (Iterator it = pageTree.getKids().iterator(); it.hasNext();) {
			PDPageNode node = (PDPageNode) it.next();
			if (node.isPage()) {
				try {
					CSTextExtractor extractor = new CSTextExtractor();
					PDPage page = (PDPage) node;
					AffineTransform pageTx = new AffineTransform();
					PDFGeometryTools.adjustTransform(pageTx, page);
					extractor.setDeviceTransform(pageTx);
					CSDeviceBasedInterpreter interpreter = new CSDeviceBasedInterpreter(
							null, extractor);
					interpreter.process(page.getContentStream(), page
							.getResources());
					sb.append(extractor.getContent());
				} catch (CSException e) {
					e.printStackTrace();
				}
			} else {
				extractText((PDPageTree) node, sb);
			}
		}
	}

	protected String extractText(String filename) throws COSVisitorException,
			IOException {
		PDDocument doc = getDoc();
		StringBuilder sb = new StringBuilder();
		extractText(doc.getPageTree(), sb);
		return sb.toString();
	}

	private String run(String file) throws Exception {
		try {
			open(file);
			return extractText(file);
		} finally {
			close();
		}

	}



		private PDDocument doc;

		protected PDDocument basicOpen(String pathname) throws IOException,
				COSLoadException {
			FileLocator locator = new FileLocator(pathname);
			return PDDocument.createFromLocator(locator);
		}

		protected void basicSave(PDDocument doc, String outputFileName)
				throws IOException {
			FileLocator locator = new FileLocator(outputFileName);
			doc.save(locator, null);
		}

		/**
		 * Close the current document.
		 *
		 * @throws IOException
		 */
		public void close() throws IOException {
			if (getDoc() != null) {
				getDoc().close();
			}
		}

		/**
		 * Create a new document.
		 */
		public void create() {
			// First create a new document.
			setDoc(PDDocument.createNew());
			// You could add more information about the environment:
			getDoc().setAuthor("intarsys consulting GmbH"); //$NON-NLS-1$
			getDoc().setCreator("intarsys PDF API"); //$NON-NLS-1$
		}

		/**
		 * The current document.
		 *
		 * @return The current document.
		 */
		public PDDocument getDoc() {
			return doc;
		}

		/**
		 * Open a document.
		 *
		 * @param pathname
		 *            The path name to the document.
		 * @throws COSLoadException
		 * @throws IOException
		 */
		public void open(String pathname) throws IOException, COSLoadException {
			setDoc(basicOpen(pathname));
		}

		/**
		 * Save current document to path.
		 *
		 * @param outputFileName
		 *            The destination path for the document.
		 * @throws IOException
		 */
		public void save(String outputFileName) throws IOException {
			basicSave(getDoc(), outputFileName);
		}

		/**
		 * Set the current document.
		 *
		 * @param doc
		 *            The new current document.
		 */
		protected void setDoc(PDDocument doc) {
			this.doc = doc;
		}

}


