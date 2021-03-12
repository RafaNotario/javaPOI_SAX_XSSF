/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package javaexcel1;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * XSSF and SAX (Event API) basic example.
 * See {@link XLSX2CSV} for a fuller example of doing
 *  XSLX processing with the XSSF Event code.
 */
@SuppressWarnings({"java:S106","java:S4823","java:S1192"})
public class FromHowTo {
    public void processFirstSheet(String filename) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ)) {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(sst);

            // process the first sheet
            try (InputStream sheet = r.getSheetsData().next()) {
				InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
			}
        }
    }

    public void processAllSheets(String filename) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ)) {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(sst);

            Iterator<InputStream> sheets = r.getSheetsData();
            while (sheets.hasNext()) {
                System.out.println("Processing new sheet:\n");
                try (InputStream sheet = sheets.next()) {
	InputSource sheetSource = new InputSource(sheet);
	parser.parse(sheetSource);
				}
                //System.out.println();
            }
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        XMLReader parser = XMLReaderFactory.createXMLReader();//XMLHelper.newXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private final SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private boolean inlineStr;
        private final LruCache<Integer,String> lruCache = new LruCache<>(50);

        private static class LruCache<A,B> extends LinkedHashMap<A, B> {
            private final int maxEntries;

            public LruCache(final int maxEntries) {
                super(maxEntries + 1, 1.0f, true);
                this.maxEntries = maxEntries;
            }

            @Override
            protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
                return super.size() > maxEntries;
            }
        }

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        @Override
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
              //  System.out.print(attributes.getValue("r") + " - ");
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                nextIsString = cellType != null && cellType.equals("s");
                inlineStr = cellType != null && cellType.equals("inlineStr");
            }else{
                System.out.print(attributes.getValue("r") + " - ");
            }
            // Clear contents cache
            lastContents = "";
        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString && !lastContents.trim().isEmpty()) {
                Integer idx = Integer.valueOf(lastContents);
                lastContents = lruCache.get(idx);
                if (lastContents == null && !lruCache.containsKey(idx)) {
                    lastContents = sst.getEntryAt(idx).getT();
                    //lastContents = sst.getItemAt(idx).getString();
                    lruCache.put(idx, lastContents);
                }
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if( name.equals("v") || name.isEmpty()){//|| (inlineStr && name.equals("c"))) {
                System.out.println(lastContents);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException { // NOSONAR
            lastContents += new String(ch, start, length);
        }
    }

    public static void main(String[] args) throws Exception {
        FromHowTo howto = new FromHowTo();
  //      howto.processFirstSheet(args[0]);
        howto.processAllSheets("A://PRUEBAS SISTEMA/HEAT NEW/01032021/Servicios Aplicados De Cobranza Mercantil Vd Sa De Cv IVR   Extrajudicial.xlsx");
    }
}

/*



package javaexcel1;

import java.io.InputStream;
import java.util.Iterator;

//import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.util.SAXHelper;
import org.xml.sax.helpers.XMLReaderFactory;

public class ExampleEventUserModel {

    public void processAllSheets(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);

        SharedStringsTable sst = r.getSharedStringsTable();
        StylesTable st = r.getStylesTable();
        XMLReader parser = fetchSheetParser(sst, st);

        Iterator<InputStream> sheets = r.getSheetsData();
        while(sheets.hasNext()) {
            System.out.println("Processing new sheet:\n");
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
            System.out.println("");
        }
    }


    public XMLReader fetchSheetParser(SharedStringsTable sst, StylesTable st) throws SAXException, ParserConfigurationException {
       //  XMLReader parser = XMLReaderFactory.createXMLReader();
       // ContentHandler handler = new XSSFReaderExample.SheetHandler(sharedStringsTable);
        
        XMLReader parser = SAXHelper.newXMLReader();
        ContentHandler handler = new SheetHandler(sst, st);
        parser.setContentHandler(handler);
        return parser;
    }


    private static class SheetHandler extends DefaultHandler {

        private SharedStringsTable sst;
        private StylesTable st;
        private String lastCharacters; // characters cache to collect character content between startElement and eneElement
        private String formula; // stores the formula, if any
        private String content; // stores the content, if any
        private boolean nextValueIsSSTString; // indicates that next value is from SharedStringsTable 
        private boolean nextValueIsStyledNumeric; // indicates that next value is a styled numeric value
        private XSSFCellStyle cellStyle; // stores the cell style, if any
        private DataFormatter formatter; // used to format the styled numeric values

        private SheetHandler(SharedStringsTable sst, StylesTable st) {
            this.sst = sst;
            this.st = st;
            this.formatter = new DataFormatter(java.util.Locale.US, true);
        }

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => start of cell
            if(name.equals("c")) {
                // print the cell reference
                System.out.print(attributes.getValue("r") + " - ");

                // get the cell type
                String cellType = attributes.getValue("t");

                // figure out if the value is an index in the SST
                this.nextValueIsSSTString = false;
                if(cellType != null && cellType.equals("s")) {
                    this.nextValueIsSSTString = true;
                } 

                // figure out if the cell has style
                this.cellStyle = null;
                String styleIdx = attributes.getValue("s");
                if (styleIdx != null) {
                    int styleIndex = Integer.parseInt(styleIdx);
                    this.cellStyle = st.getStyleAt(styleIndex);
                    // print that there is cell style for this cell
                    System.out.print("CellStyle: " + this.cellStyle + " - ");
                }

                // figure out if the value is an styled numeric value or date
                this.nextValueIsStyledNumeric = false;
                if(cellType != null && cellType.equals("n") || cellType == null) {
                    if (this.cellStyle != null) {
                        this.nextValueIsStyledNumeric = true;
                    }
                } 

            }

            // clear characters cache after each element
            this.lastCharacters = "";
        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {

            // f => end of formula in a cell
            if(name.equals("f")) {
                this.formula = lastCharacters;
                // print formula
                System.out.print("Formula: " + this.formula + " - ");
            }

            // v => end of value of a cell
            if(name.equals("v")) {

                this.content = this.lastCharacters;

                // process shared string value
                if(this.nextValueIsSSTString) {
                    int idx = Integer.parseInt(lastCharacters);
//                    this.content = sst.getItemAt(idx).getT();
                    this.content = sst.getEntryAt(idx).getT();
                    
                    nextValueIsSSTString = false;
                }

                // process styled numeric value
                if(this.nextValueIsStyledNumeric) {
                    String formatString = cellStyle.getDataFormatString();
                    int formatIndex = cellStyle.getDataFormat();                    
                    if (formatString == null) {
                        // formatString could not be found, so it must be a builtin format.
                        formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
                    }
                    double value = Double.valueOf(this.content);
                    this.content = formatter.formatRawCellContents(value, formatIndex, formatString);
                    nextValueIsStyledNumeric = false;
                }

            }

            // c => end of a cell
            if(name.equals("c")) {
                // print content
                System.out.println("Content: " + this.content);
                this.content = "";
            }
        }

        public void characters(char[] ch, int start, int length) {
            this.lastCharacters += new String(ch, start, length);
        }
    }

    public static void main(String[] args) throws Exception {
        ExampleEventUserModel example = new ExampleEventUserModel();
        //example.processAllSheets(args[0]);
        example.processAllSheets("ExcelExample.xlsx");
    }
}
*/
