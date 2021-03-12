package javaexcel1;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Test2 {

    public static void lee() throws Exception {
        File test = new File(".");
//        String file = "A://PRUEBAS SISTEMA/HEAT NEW/01032021/Servicios Aplicados De Cobranza Mercantil Vd Sa De Cv IVR   Extrajudicial.xlsx";
        String file = "A://PRUEBAS SISTEMA/OFSC/SERVICIOS_APLICADOS_DE_COBRANZA_MERCANTIL_VD_01032021.xlsx";
        //test.getAbsolutePath()+"/src/main/resources/empty_cell Chinese name.xlsx";

        OPCPackage pkg = OPCPackage.open(file);
        XSSFReader r = new XSSFReader(pkg);
        InputStream in = r.getSheet("rId1");
        //View the converted xml original file to facilitate understanding of the processing during subsequent parsing,
        // Note: If you open the comment, the following parse() cannot read the content of the stream
        //Test2.streamOut(in);
        //The following is the SST index will be used
        SharedStringsTable sst = r.getSharedStringsTable();
        //sst.writeTo(System.out);
        XMLReader parser = XMLReaderFactory.createXMLReader();//"org.apache.xerces.parsers.SAXParser"
        List<List<String>> container = new ArrayList<>();
//envia la hoja al analisis
        parser.setContentHandler(new Myhandler(sst, container));
        InputSource inputSource = new InputSource(in);
        parser.parse(inputSource);
        in.close();
//imprimelos valores
        Test2.printContainer(container);
    }

    public static void printContainer(List<List<String>> container) {
        int contador = 1;
        for (List<String> stringList : container) {
            System.out.print(contador+"-");
            //System.out.print(contador + " tam : " + stringList.size());
         //   if (stringList.size() != 38) {
                for (String str : stringList) {
                    System.out.printf("%10s", str + " | ");
                }
          //  }
            /*  */
            contador++;
            System.out.println("");
        }
    }

    //Read the stream, view the contents of the file xml to analize
    public static void streamOut(InputStream in) throws Exception {
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            System.out.write(buf, 0, len);
            // System.out.println("");
        }
    }

    public static void main(String[] args) {
        Test2 ts = new Test2();
        try {
            ts.lee();
        } catch (Exception ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

class Myhandler extends DefaultHandler {

    //Take the value corresponding to the index of SST
    private SharedStringsTable sst;

    public void setSst(SharedStringsTable sst) {
        this.sst = sst;
    }

    //Save analysis results
    private List<List<String>> container;

    public Myhandler(SharedStringsTable sst, List<List<String>> container) {
        this.sst = sst;
        this.container = container;
    }

    private String lastContents;

    //Valid data rectangle area, A1:Y2
    private String dimension;

    //According to the dimension, the data length of each row is obtained
    private int longest;

    //The id of the last cell with content, to determine the empty cell
    private String lastRowid;

    //Row data save
    private List<String> currentRow;

    //The cell content is the index of SST
    private boolean isSSTIndex = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        System.out.println("startElement:"+qName);

        if (qName.equals("dimension")) {
            dimension = attributes.getValue("ref");
            longest = covertRowIdtoInt(dimension.substring(dimension.indexOf(":") + 1));
        }
        //Start of line
        if (qName.equals("row")) {
            currentRow = new ArrayList<>();
        }
        if (qName.equals("c")) {
            String rowId = attributes.getValue("r");
//             System.out.println(attributes.getValue("r") + " - ");//r=celda, v= value de celda 
            //Null cell judgment, add empty characters to the list
            if (lastRowid != null) {
                int gap = covertRowIdtoInt(rowId) - covertRowIdtoInt(lastRowid);
                for (int i = 0; i < gap - 1; i++) {
                    currentRow.add("");
                }
            } else {
                //The first cell may not be in the first column
                if (!"A1".equals(rowId)) {
                    for (int i = 0; i < covertRowIdtoInt(rowId) - 1; i++) {
                        currentRow.add("");
                    }
                }
            }
            lastRowid = rowId;
            //Judging that the value of the cell is the index of SST, and the characters method cannot be used directly
            if (attributes.getValue("t") != null && attributes.getValue("t").equals("s")) {
                isSSTIndex = true;
            } else {
                isSSTIndex = false;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        System.out.println("endElement:"+qName);
        //End of line, store one line of data
        if (qName.equals("row")) {
            // System.out.println("");
            //Determine whether the last cell is at the end and fill in the number of columns
            if (covertRowIdtoInt(lastRowid) < longest) {
                for (int i = 0; i < longest - covertRowIdtoInt(lastRowid); i++) {
                    currentRow.add("");
                }
            }
            container.add(currentRow);
            lastRowid = null;
        }
        //The cell content tag ends, the characters method will be called to process the content
        if (qName.equals("v")) {
            //The value of the cell is the index of SST
            if (isSSTIndex) {
                String sstIndex = lastContents.toString();
                try {
                    int idx = Integer.parseInt(sstIndex);
                    XSSFRichTextString rtss = new XSSFRichTextString(
                            sst.getEntryAt(idx));
                    lastContents = rtss.toString();
                    currentRow.add(lastContents);
                } catch (NumberFormatException ex) {
                    System.out.println(lastContents);
                }
            } else {
                currentRow.add(lastContents);
            }
        }
    }

    /**
     * Get element text data
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        lastContents = new String(ch, start, length);
    }

    /**
     * Column number to number AB7-->28 Column 28
     *
     * @param rowId
     * @return
     */
    public static int covertRowIdtoInt(String rowId) {
        int firstDigit = -1;
        for (int c = 0; c < rowId.length(); ++c) {
            if (Character.isDigit(rowId.charAt(c))) {
                firstDigit = c;
                break;
            }
        }
        //AB7-->AB
        //AB is the column number, 7 is the row number
        String newRowId = rowId.substring(0, firstDigit);
        int num = 0;
        int result = 0;
        int length = newRowId.length();
        for (int i = 0; i < length; i++) {
            //Take the lowest bit first, B
            char ch = newRowId.charAt(length - i - 1);
            //B represents the decimal number 2, and the ascii code is subtracted. Based on the ascii code of A, A represents 1, B represents 2
            num = (int) (ch - 'A' + 1);
            //The column number conversion is equivalent to the conversion of 26 hexadecimal numbers to decimal
            num *= Math.pow(26, i);
            result += num;
        }
        return result;

    }

/*    public static void main(String[] args) {
        //Myhandler mh = new Myhandler(sst, container)

        System.out.println(Myhandler.covertRowIdtoInt("AB7"));

    }
*/}
