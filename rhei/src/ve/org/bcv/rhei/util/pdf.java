/**
 * 
 */
package ve.org.bcv.rhei.util;

 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.common.io.Files;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 22/08/2015 13:39:41
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class pdf {

    /**
     * Contructor privato para que la clase no pueda ser instanciada.
     */
	 
    /**
     * Compagina un documento pdf en formato de libro para impresión duplex.
     * @param archivoPDF Archivo PDF original a ser compaginado.
     * @param paginaBlanca Indica si el documento lleva pagínas en blanco.
     * El método supone que el documento pdf siempre tiene solo una pagina en
     * blanco "cantPaginasFinales" antes del fin del doucmento.
     * @param cantPaginasIniciales Total de paginas iniciales fijas.
     * El método supondra que las "cantPaginasIniciales" de paginas iniciales
     * son fijas.
     * @param cantPaginasFinales Total de paginas finales fijas.
     * El método supondra que las "cantPaginasFinales" de paginas finales son
     * fijas.
     * @return Retona un arvhivo File que representa el archivo compaginado.
     * @throws Exception Si ocurre algún error se arroja esta
     * excepción.
     */
    public static final File compaginarPDFLibro(final File archivoPDF,
            final boolean paginaBlanca, final int cantPaginasIniciales,
            final int cantPaginasFinales)
            throws Exception {

        File pdfCompaginado = null;
        File pdfCompleto = null;
        File pdfPaginado = null;

        PdfReader reader = null;
        Document document = null;
        PdfCopy copy = null;
        PdfImportedPage page = null;
        try {
            pdfCompleto = File.createTempFile("pdfcompleto", ".pdf");
            pdfPaginado = File.createTempFile("pdfpaginado", ".pdf");
            pdfCompaginado = File.createTempFile("pdfcompaginado", ".pdf");

            pdfCompleto.deleteOnExit();
            pdfPaginado.deleteOnExit();

            reader = new PdfReader(new FileInputStream(archivoPDF));
            document = new Document(reader.getPageSizeWithRotation(1));
            copy = new PdfCopy(document, new FileOutputStream(
                    pdfCompleto));
            document.open();
            int paginas = reader.getNumberOfPages();

            int ultimaPagDoc = paginas;
            int auxLast = ultimaPagDoc;
            int auxFirst = 1;

            int auxPaginaBlanca = 0;
            if(paginaBlanca) {
                auxPaginaBlanca = auxLast - 1;
            }

            int cantDetalles = paginas - 3;
            int paginasImprimir = proxMultiplo(4, cantDetalles + 2);
            int cantBanca = paginasImprimir - (cantDetalles + 2);

            //AGREGANDO PRIMERA PAGINA
            page = copy.getImportedPage(reader, auxFirst);
            copy.addPage(page);
            //AGREGANDO DETALLE
            int paginaActual = 2;
            for(int i = 0; i < cantDetalles; i++) {
                page = copy.getImportedPage(reader, i + paginaActual);
                copy.addPage(page);
            }
            //AGREGANDO PAGINAS EN BLANCO
            for(int i = 0; i < cantBanca; i++) {
                page = copy.getImportedPage(reader, auxPaginaBlanca);
                copy.addPage(page);
            }
            //AGREGANDO ULTIMA PAGINA
            paginaActual = 1 + cantDetalles + cantBanca;
            page = copy.getImportedPage(reader, auxLast);
            copy.addPage(page);

            document.close();

            //--------------------------------------------PAGINANDO EL DOCUMENTO
            reader = new PdfReader(new FileInputStream(pdfCompleto));
            document = new Document(reader.getPageSizeWithRotation(1));
            copy = new PdfCopy(document, new FileOutputStream(
                    pdfPaginado));
            document.open();

            paginas = reader.getNumberOfPages();

            ultimaPagDoc = paginas;
            auxLast = ultimaPagDoc;
            auxFirst = 1;

            paginaActual = 1;
            while (auxLast >= auxFirst) {
                if (auxLast - auxFirst == 1) {
                    page = copy.getImportedPage(reader, auxFirst);
                    copy.addPage(page);
                    page = copy.getImportedPage(reader, auxLast);
                    copy.addPage(page);
                } else {
                    if (paginaActual % 2 == 0) {
                        page = copy.getImportedPage(reader, auxFirst);
                        copy.addPage(page);
                        page = copy.getImportedPage(reader, auxLast);
                        copy.addPage(page);
                    } else {
                        page = copy.getImportedPage(reader, auxLast);
                        copy.addPage(page);
                        page = copy.getImportedPage(reader, auxFirst);
                        copy.addPage(page);
                    }
                }
                auxLast--;
                auxFirst++;
                paginaActual++;
            }
            document.close();

            //------------------------------COMPAGINANDO EL DOCUMENTO DEFINITIVO
            reader = new PdfReader(new FileInputStream(pdfPaginado));
            paginas = reader.getNumberOfPages();

            Rectangle psize = reader.getPageSize(1);
            float width = psize.getHeight();
            float height = psize.getWidth();

            document = new Document(new Rectangle(width, height));
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream(pdfCompaginado));

            document.open();

            PdfContentByte cb = writer.getDirectContent();
            int i = 0;
            int p = 0;

            while (i < paginas) {
                document.newPage();
                p++;
                i++;
                PdfImportedPage page1 = writer.getImportedPage(reader, i);
                cb.addTemplate(page1, .68f, 0, 0, .68f, 20, -5);
                if (i < paginas) {
                    i++;
                    PdfImportedPage page2 = writer.getImportedPage(reader, i);
                    cb.addTemplate(page2, .68f, 0, 0, .68f, width / 2 + 20, -5);
                }
            }
            document.close();
        } catch (DocumentException ex) {
            ex.printStackTrace();
            throw new Exception("error.sis.compaginacion_pdf");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new Exception("error.sis.compaginacion_pdf");
        }
        return pdfCompaginado;
    }

    private static int proxMultiplo (int multiplo, int numero) {
        int auxNum = numero;
        while (auxNum % multiplo != 0) {
            auxNum++;
        }
        return auxNum;
    }

    public static File pegarArchivosPDF (List<File> archivosPDF, 
            final String prefijo)
            throws Exception {
        File archivoPDF = null;
        try {
            archivoPDF = File.createTempFile(prefijo + "_", ".pdf");

            PdfReader reader = new PdfReader(new FileInputStream((File)
                    archivosPDF.get(0)));
            Document document = new Document(reader.getPageSizeWithRotation(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(
                    archivoPDF));
            PdfImportedPage page = null;

            document.open();
            for (File file : archivosPDF) {
                reader = new PdfReader(new FileInputStream(file));
                int paginas = reader.getNumberOfPages();

                for(int i = 1; i <= paginas; i++) {
                    page = copy.getImportedPage(reader, i);
                    copy.addPage(page);
                }
            }
            document.close();
            return archivoPDF;
        } catch (IOException ex) {
        	ex.printStackTrace();
            throw new Exception("error.sis.generacion_pdf");
        } catch (DocumentException ex) {
        	ex.printStackTrace();
            throw new Exception("error.sis.generacion_pdf");
        }
    }
    
    public static File inputStreamToFile(InputStream initialStream) throws IOException{
    	    byte[] buffer = new byte[initialStream.available()];
    	    initialStream.read(buffer);
    	 
    	    File targetFile =  File.createTempFile("memorandom", ".pdf"); // File("src/main/resources/targetFile.tmp");
    	    Files.write(buffer, targetFile);
    	    return targetFile;
    }
    
    public static InputStream FileToInputStream(File initialFile ) throws IOException{
    	    InputStream targetStream = Files.asByteSource(initialFile).openStream();
    	    return targetStream;

    }
   


}
