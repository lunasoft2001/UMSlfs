package at.ums.luna.umslfs.adaptadores;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.modelos.AlbaranCompleto;
import at.ums.luna.umslfs.modelos.DetalleAlbaranes;


/**
 * Created by luna-aleixos on 14.06.2016.
 */
public class PdfManager {
    private static Context mContext;
    private static final String APP_FOLDER_NAME = "UMSlieferschein";
    private static final String INVOICES = "Invoices";
    private static Font catFont;
    private static Font subFont ;
    private static Font smallBold ;
    private static Font smallFont ;
    private static Font italicFont ;
    private static Font italicFontBold ;

    //Declaramos nuestra fuente base que se encuentra en la carpeta "assets/fonts" folder
    //Usaremos arialuni.ttf que permite imprimir en nuestro PDF caracteres Unicode Cirílicos (Ruso, etc)
    private static BaseFont unicode;

    private static String codigoAlbaranActual;
    //!!!Importante: La carpeta "assets/fonts/arialuni.ttf" debe estar creada en nuestro projecto en
    //la subcarpeta "PdfCreator/build/exploded-bundles/ComAndroidSupportAppcompactV71900.aar"
    //En el caso de que Android Studio la eliminara la copiamos manualmente
    //PdfCreator/build/exploded-bundles/ComAndroidSupportAppcompactV71900.aarassets/fonts/arialuni.ttf
    private static File fontFile = new File("assets/fonts/arialuni.ttf");

    private static String tempDir = Environment.getExternalStorageDirectory() + "/" + APP_FOLDER_NAME +"/"; //borrar

    //Constructor set fonts and get context
    public PdfManager(Context context) throws IOException, DocumentException {
        mContext = context;
        //Creamos los distintos estilos para nuestro tipo de fuente.
        unicode = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        catFont = new Font(unicode, 22,Font.BOLD, BaseColor.BLACK);
        subFont = new Font(unicode, 16,Font.BOLD, BaseColor.BLACK);
        smallBold = new Font(unicode, 12,Font.BOLD, BaseColor.BLACK);
        smallFont = new Font(unicode, 12,Font.NORMAL, BaseColor.BLACK);
        italicFont = new Font(unicode, 12,Font.ITALIC, BaseColor.BLACK);
        italicFontBold = new Font(unicode, 12,Font.ITALIC|Font.BOLD, BaseColor.BLACK);

        codigoAlbaranActual = "JJ160001";

    }

    //Generando el documento PDF
    public void createPdfDocument(AlbaranCompleto invoiceObject, String codigoAlbaranObtenido) {

        codigoAlbaranActual = codigoAlbaranObtenido;

        try {

            //Creamos las carpetas en nuestro dispositivo, si existen las eliminamos.
            String fullFileName = createDirectoryAndFileName();

            if(fullFileName.length()>0){
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fullFileName));

                document.open();

                //Creamos los metadatos del alchivo
                addMetaData(document);
                //Adicionamos el logo de la empresa
                addImage(document);
                //Creamos el título del documento
                addTitlePage(document, invoiceObject);
                //Creamos el contenido en form de tabla del documento
                addInvoiceContent(document,invoiceObject.listaDetallesAlbaran);
                //Creamos el total de la factura del documento
                addInvoiceTotal(document, invoiceObject);

                document.close();

                Toast.makeText(mContext, R.string.pdf_creado, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createDirectoryAndFileName(){

        String FILENAME = "InvoiceSample.pdf";
        String fullFileName ="";
        //Obtenemos el directorio raiz "/sdcard"
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(extStorageDirectory + File.separator + APP_FOLDER_NAME);

        //Creamos la carpeta "com.movalink.pdf" y la subcarpeta "Invoice"
        try {
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }
            File pdfSubDir = new File(pdfDir.getPath() + File.separator + INVOICES);

            if (!pdfSubDir.exists()) {
                pdfSubDir.mkdir();
            }

            fullFileName = Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER_NAME + File.separator + INVOICES + File.separator + FILENAME;

            File outputFile = new File(fullFileName);

            if (outputFile.exists()) {
                outputFile.delete();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return fullFileName;
    }

    //PDF library add file metadata function
    private static void addMetaData(Document document) {
        document.addTitle("movalink PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("movalink.com");
        document.addCreator("movalink.com");
    }

    //Creando el Título y los datos de la Empresa y el Cliente
    private static void addTitlePage(Document document, AlbaranCompleto invoiceObject)
            throws DocumentException {

        Paragraph preface = new Paragraph();
        // Adicionamos una línea en blanco
        addEmptyLine(preface, 1);
        // Adicionamos el títulos de la Factura y el número
        preface.add(new Paragraph(mContext.getResources().getString(R.string.invoice_number) + invoiceObject.codigoAlbaran, catFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.invoice_date) + invoiceObject.fecha, italicFont));

        //Adicionamos los datos de la Empresa
        preface.add(new Paragraph(mContext.getResources().getString(R.string.company) + " " + "UMS",smallFont));
        preface.add(new Paragraph("IndustrieWest..." ,smallFont));
        preface.add(new Paragraph("0699 122 944 31",smallFont));

        addEmptyLine(preface, 1);

        //Adicionamos los datos del Cliente
        preface.add(new Paragraph(mContext.getResources().getString(R.string.client_title), smallBold));

        preface.add(new Paragraph(mContext.getResources().getString(R.string.client_name) + " " + invoiceObject.nombreCliente,smallFont));
        preface.add(new Paragraph(invoiceObject.direccionCliente,smallFont));
        preface.add(new Paragraph(invoiceObject.telefonoCliente ,smallFont));
        preface.add(new Paragraph(invoiceObject.emailCliente));

        addEmptyLine(preface, 1);

        //Adicionamos el párrafo creado al documento
        document.add(preface);

        // Si queremos crear una nueva página
        //document.newPage();
    }

    //Creamos el contenido de la factura, las líneas con los artículos.
    private static void addInvoiceContent(Document document, List<DetalleAlbaranes> invoiceDetail) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        // Creamos una tabla con los títulos de las columnas
        createInvoiceTable(paragraph, invoiceDetail);
        // Adicionamos el párrafo al documento
        document.add(paragraph);

    }

    //Creamos el subtotal y el total de la factura.
    private static void addInvoiceTotal(Document document, AlbaranCompleto invoiceObject) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        // Adicionamos la tabla al párrafo
        createTotalInvoiceTable(paragraph, invoiceObject);
        // Adicionamos el párrafo al documento
        document.add(paragraph);

    }

    //Procedimiento para crear los títulos de las columnas de la factura.
    private static void createInvoiceTable(Paragraph tableSection, java.util.List<DetalleAlbaranes> invoiceDetails)
            throws DocumentException {

        int TABLE_COLUMNS = 5;
        //Instaciamos el objeto Pdf Table y creamos una tabla con las columnas definidas en TABLE_COLUMNS
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);// number of table columns

        //Definimos el ancho que corresponde a cada una de las 5 columnas
        float[] columnWidths = new float[]{80f, 200f, 50f, 80f, 100f};
        table.setWidths(columnWidths);

        //Definimos el ancho de nuestra tabla en %
        table.setWidthPercentage(100);

        // Aquí les dejos otras propiedades que pueden aplicar a la tabla
        // table.setBorderColor(BaseColor.GRAY);
        // table.setPadding(4);
        // table.setSpacing(4);
        // table.setBorderWidth(1);

        //Definimos los títulos para cada una de las 5 columnas
        PdfPCell cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.detail_code),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la primera columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.detail_description),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la segunda columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.detail_amount),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la tercera columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.detail_price),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la cuarta columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.detail_total),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la quinta columna
        table.addCell(cell);

        //Creamos la fila de la tabla con las cabeceras
        table.setHeaderRows(1);

        //Creamos las lineas con los artículos de la factura;
        for (DetalleAlbaranes orderLine : invoiceDetails) {
            createInvoiceLine(orderLine, table);
        }

        tableSection.add(table);
    }


    //Procedimiento para crear una lines vacía
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    //de Juanjo
    private static Bitmap redimensionarImagen(Bitmap mBitmap, int newWidth){

        float aspectRatio = mBitmap.getWidth()/ (float) mBitmap.getHeight();
        int width = newWidth;
        int height = Math.round(width/aspectRatio);
        Bitmap imagenReducida = Bitmap.createScaledBitmap(mBitmap,width,height,false);
        return  imagenReducida;

    }
    //de Juanjo
    public static Bitmap rodarImagen(Bitmap bMap, String nombreFoto) {

        String archivo = tempDir + nombreFoto;
        File imageFile = new File(archivo);
        Bitmap nuevoBitmap = bMap;

        try {
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotate = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate -= 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("JUANJO", "orientation = " + orientation + " - rotate = " + rotate);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);

            nuevoBitmap = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), bMap.getHeight(), matrix, true);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return nuevoBitmap;
    }

    private static void addFotoFirma(Document document, String nombreFotoFirma,
                                     int ancho, float horizontal, float vertical)
            throws IOException, DocumentException {

        Bitmap bitMap = BitmapFactory.decodeFile(tempDir + nombreFotoFirma);

        Bitmap logoReducido = redimensionarImagen(bitMap,ancho);

        Bitmap logoGirado = rodarImagen(logoReducido, nombreFotoFirma);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        logoGirado.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bitMapData = stream.toByteArray();
        Image image = Image.getInstance(bitMapData);
        //Posicionamos la imagen el el documento
        image.setAbsolutePosition(horizontal, vertical);
        document.add(image);
    }

    //Procedimiento para adicionar una imagen al documento PDF
    private static void addImage(Document document) throws IOException, DocumentException {

//        Bitmap bitMap = BitmapFactory.decodeFile(tempDir + "fotoJJ160001.jpg");
        Bitmap bitMap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ums_logo_completo);

        Bitmap logoReducido = redimensionarImagen(bitMap,120);

        Bitmap logoGirado = rodarImagen(logoReducido, "test.jpg");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        logoGirado.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bitMapData = stream.toByteArray();
        Image image = Image.getInstance(bitMapData);
        //Posicionamos la imagen el el documento
        image.setAbsolutePosition(400f, 650f);
        document.add(image);

        //agregar foto
        addFotoFirma(document,"foto"+codigoAlbaranActual+".jpg",240,100f,100f);

        //agregar firma
        addFotoFirma(document,"firma"+codigoAlbaranActual+".jpg",120,400f,0f);
    }


    //Procedimiento para crear las líneas de la factura en forma de tabla.
    private static void createInvoiceLine(DetalleAlbaranes invoiceLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        //Adicionamos celdas sin formato ni estilos, solo el valor
        table.addCell(String.valueOf(invoiceLine.getLinea()));
        table.addCell(invoiceLine.getDetalle());

        //Adicionamos celdas con formato y estilo: (font, align) para el correspondiente valor
        cell.setPhrase(new Phrase(String.valueOf(invoiceLine.getCantidad()),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //Adicionamos celdas con formato y estilo: (font, align)
        cell.setPhrase(new Phrase(String.valueOf(invoiceLine.getTipo()), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //Adicionamos celdas con formato y estilo: (font, align)
//        cell.setPhrase(new Phrase(String.valueOf(invoiceLine.total), smallFont));
//        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        table.addCell(cell);

    }


    //Procedimiento para crear los totales y subtotales de la factura en forma de tabla.
    //Misma lógica utilizada para crear los títulos de las columnas de la factura
    private static void createTotalInvoiceTable(Paragraph tableSection, AlbaranCompleto orderHeaderModel)
            throws DocumentException {

//        int TABLE_COLUMNS = 2;
//        PdfPTable table = new PdfPTable(TABLE_COLUMNS);
//
//        float[] columnWidths = new float[]{200f, 200f};
//        table.setWidths(columnWidths);
//
//        table.setWidthPercentage(100);
//
//        //Adicionamos el título de la celda
//        PdfPCell cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.invoice_subtotal),smallBold));
//        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        table.addCell(cell);
//
//        double subTotal = orderHeaderModel.total;
//        //Adicionamos el contenido de la celda con el valor subtotal
//        cell = new PdfPCell(new Phrase(String.valueOf(subTotal)));
//        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//
//        table.addCell(cell);
//
//        //Adicionamos el título de la celda
//        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.invoice_tax),smallBold));
//        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        table.addCell(cell);
//
//        //Adicionamos el contenido de la celda con el valor tax
//        cell = new PdfPCell(new Phrase(String.valueOf(0)));
//        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        table.addCell(cell);
//
//        //Adicionamos el título de la celda
//        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.detail_total),smallBold));
//        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        table.addCell(cell);
//
//        //Adicionamos el contenido de la celda con el valor total
//        cell = new PdfPCell(new Phrase(String.valueOf(orderHeaderModel.total)));
//        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        table.addCell(cell);
//
//        tableSection.add(table);

    }

    //Procedimiento para mostrar el documento PDF generado
    public void showPdfFile(String fileName, Context context){
        Toast.makeText(context, "Leyendo documento", Toast.LENGTH_LONG).show();

        String sdCardRoot = Environment.getExternalStorageDirectory().getPath();
        String path = sdCardRoot + File.separator + APP_FOLDER_NAME + File.separator + fileName;

        File file = new File(path);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
        }
    }

    //Procedimiento para enviar por email el documento PDF generado
    public void sendPdfByEmail(String fileName, String emailTo, String emailCC, Context context){

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Movalink PDF Tutorial email");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Working with PDF files in Android");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{emailCC});

        String sdCardRoot = Environment.getExternalStorageDirectory().getPath();
        String fullFileName = sdCardRoot + File.separator + APP_FOLDER_NAME + File.separator + fileName;

        Uri uri = Uri.fromFile(new File(fullFileName));
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setType("application/pdf");

        context.startActivity(Intent.createChooser(emailIntent, "Send email using:"));
    }

}
