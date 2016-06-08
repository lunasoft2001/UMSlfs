package at.ums.luna.umslfs.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.DetalleAlbaranes;

public class FormularioAlbaranesDetalle extends AppCompatActivity {

    private TextView codigoAlbaran;
    private TextView linea;
    private TextView detalle;
    private TextView cantidad;
    private TextView tipo;

    private String valorCodigoAlbaran;
    private String valorLinea;

    private OperacionesBaseDatos mOperacionesBaseDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_albaranes_detalle);

        codigoAlbaran = (TextView) findViewById(R.id.tvCodigoAlbaran);
        linea = (TextView) findViewById(R.id.linea);
        detalle = (TextView) findViewById(R.id.detalle);
        cantidad = (TextView) findViewById(R.id.cantidad);
        tipo = (TextView) findViewById(R.id.tipo);

        Intent intento = getIntent();

        Bundle bundle = intento.getExtras();
        if(bundle != null) {

            valorCodigoAlbaran = bundle.getString("codigoAlbaran");
            valorLinea = bundle.getString("linea");

        }

        mOperacionesBaseDatos = new OperacionesBaseDatos(this);

        DetalleAlbaranes detalleAlbaranes = mOperacionesBaseDatos.obtenerDetalleAlbaran(valorCodigoAlbaran, valorLinea);

        codigoAlbaran.setText(detalleAlbaranes.getCodigoAlbaran());
        linea.setText(String.valueOf(detalleAlbaranes.getLinea()));
        detalle.setText(detalleAlbaranes.getDetalle());
        cantidad.setText(String.valueOf(detalleAlbaranes.getCantidad()));
        tipo.setText(detalleAlbaranes.getTipo());


        //Codigo para el onClickListener
        findViewById(R.id.botonCancelarDetalle).setOnClickListener(mGlobal_onClickListener);
        findViewById(R.id.botonBorrarDetalle).setOnClickListener(mGlobal_onClickListener);
        findViewById(R.id.botonActualizarDetalle).setOnClickListener(mGlobal_onClickListener);

    }

    //Intents para cualquier botón de la actividad
    final View.OnClickListener mGlobal_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.botonCancelarDetalle:
                    finish();
                    break;
                case R.id.botonBorrarDetalle:
                    Eliminar();
                    break;
                case R.id.botonActualizarDetalle:
                    Actualizar();
                    break;
            }
        }
    };

    private void Eliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.ContinuarBorrando))
                .setTitle(this.getString(R.string.Advertencia))
                .setCancelable(false)
                .setNegativeButton(this.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                mOperacionesBaseDatos.eliminarDetalleAlbaran(valorCodigoAlbaran,valorLinea, FormularioAlbaranesDetalle.this);

                                finish();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void Actualizar(){

        String detalleActual = detalle.getText().toString();
        double cantidadActual = Double.parseDouble(cantidad.getText().toString());
        String tipoActual = tipo.getText().toString();

        mOperacionesBaseDatos.actualizarDetalleAlbaran(valorCodigoAlbaran,valorLinea,detalleActual,
                cantidadActual,tipoActual);

        finish();
    }
}
