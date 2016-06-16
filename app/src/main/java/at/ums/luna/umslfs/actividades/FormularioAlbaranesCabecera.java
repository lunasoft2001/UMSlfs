package at.ums.luna.umslfs.actividades;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.adaptadores.PdfManager;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.AlbaranCompleto;
import at.ums.luna.umslfs.modelos.CabeceraAlbaranes;
import at.ums.luna.umslfs.modelos.DetalleAlbaranes;

public class FormularioAlbaranesCabecera extends FragmentActivity {

    OperacionesBaseDatos mOperacionesBaseDatos;


    private String codigoAlbaranObtenido;
    private TextView numeroAlbaran;


    //PDF
    AlbaranCompleto invoiceObject = new AlbaranCompleto();
    private String INVOICES_FOLDER = "Invoices";
    private String FILENAME = "InvoiceSample.pdf";
    //Declaramos la clase PdfManager
    private PdfManager pdfManager = null;


    //fin pdf


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_albaranes_cabecera);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new AdaptadorPager(getSupportFragmentManager()));

        //Obtener el codigoCabecera
        Intent intento = getIntent();
        Bundle bundle = intento.getExtras();
        if (bundle != null){
            codigoAlbaranObtenido = bundle.getString("codigoAlbaran");
        }

        numeroAlbaran = (TextView) findViewById(R.id.tvDetalle);
        numeroAlbaran.setText(codigoAlbaranObtenido);

        mOperacionesBaseDatos = new OperacionesBaseDatos(this);


        // Comienza el codigo para hacer PDF

        //Creamos una factura desde nuestro código solo para poder generar el documento PDF
        //con esta información
        createInvoiceObject();


        try {
            //Instanciamos la clase PdfManager
            pdfManager = new PdfManager(FormularioAlbaranesCabecera.this);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        ImageButton create_pdf = (ImageButton) findViewById(R.id.botonCrearPDF);
        ImageButton read_pdf = (ImageButton)findViewById(R.id.botonVerPDF);
        ImageButton send_email_pdf = (ImageButton)findViewById(R.id.botonEnviarPDF);

        create_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create PDF document
                assert pdfManager != null;
                pdfManager.createPdfDocument(invoiceObject, codigoAlbaranObtenido);
            }
        });

        read_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert pdfManager != null;
                pdfManager.showPdfFile(INVOICES_FOLDER + File.separator + FILENAME,FormularioAlbaranesCabecera.this);
            }
        });

        send_email_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTo ="luna.ums@gmail.com";
                String emailCC ="juanjolunabowling@gmail.com";
                assert pdfManager != null;
                pdfManager.sendPdfByEmail(INVOICES_FOLDER + File.separator + FILENAME,emailTo,emailCC, FormularioAlbaranesCabecera.this);
            }
        });


        // finaliza el codigo para PDF


    }

    private class AdaptadorPager extends FragmentPagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return getString(R.string.CONTENIDOS);
                case 1:
                    return getString(R.string.DETALLE);
                case 2:
                    return getString(R.string.IMAGEN);
                case 3:
                    return getString(R.string.FIRMA);
                default:
                    return "---";
            }
        }

        public AdaptadorPager(FragmentManager fm) { super(fm);}

        @Override
        public Fragment getItem(int position) {

            Bundle args = new Bundle();
            args.putString("codigoObtenido", codigoAlbaranObtenido);


            switch (position){
                case 0:
                    AlbaranesCabeceraFragment f1 = new AlbaranesCabeceraFragment();
                    f1.setArguments(args);
                    return f1;
                case 1:
                    DetalleAlbaranFragment f2 = new DetalleAlbaranFragment();
                    f2.setArguments(args);
                    return f2;
                case 2:
                    FotoFragment f3 = new FotoFragment();
                    f3.setArguments(args);
                    return f3;
                case 3:
                    firmaFragment f4 = new firmaFragment();
                    f4.setArguments(args);
                    return f4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, R.string.albaran_cerrado, Toast.LENGTH_SHORT).show();
        finish();
    }


    //Crea el documento importando los datos
    private void createInvoiceObject(){


        CabeceraAlbaranes cabeceraActual =  mOperacionesBaseDatos.obtenerCabeceraAlbaran(codigoAlbaranObtenido);

        invoiceObject.codigoAlbaran= cabeceraActual.getCodigoAlbaran() ;
        invoiceObject.fecha= cabeceraActual.getFecha();
        invoiceObject.idCliente = cabeceraActual.getIdCliente();
        invoiceObject.nombreCliente = cabeceraActual.getNombreCliente();
        invoiceObject.direccionCliente = cabeceraActual.getDireccionCliente();
        invoiceObject.telefonoCliente = cabeceraActual.getTelefonoCliente();
        invoiceObject.emailCliente = cabeceraActual.getEmailCliente();


        invoiceObject.listaDetallesAlbaran = mOperacionesBaseDatos.verListaDetalleAlbaran(codigoAlbaranObtenido);


    }


}
