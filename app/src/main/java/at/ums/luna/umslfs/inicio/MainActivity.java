package at.ums.luna.umslfs.inicio;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.actividades.FormularioAlbaranesCabecera;
import at.ums.luna.umslfs.actividades.FormularioTrabajador;
import at.ums.luna.umslfs.actividades.ListaAlbaranesCabecera;
import at.ums.luna.umslfs.actividades.ListaClientes;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;

public class MainActivity extends AppCompatActivity {


    private OperacionesBaseDatos mOperacionesBaseDatos;
    private Cursor trabajadorActual;


    private TextView nombreTrabajador;
    private TextView tvUltimoAlbaran;


    private String idTrabajadorActual;
    private String nombreTrabajadorActual;
    private int ultimoAlbaran;

    private int ULTIMO_NUMERO_ALBARAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    @Override
    protected void onResume() {
        super.onResume();

        nombreTrabajador = (TextView)findViewById(R.id.tvTrabajador);
        tvUltimoAlbaran = (TextView) findViewById(R.id.tvMaxAlbaran);


        mOperacionesBaseDatos = new OperacionesBaseDatos(this);

        trabajadorActual = mOperacionesBaseDatos.verTrabajador();
        trabajadorActual.moveToFirst();

        //rellenamos ultimoAlbaran
        ultimoAlbaran = mOperacionesBaseDatos.ultimaCabeceraAlbaran();
        tvUltimoAlbaran.setText(String.valueOf(ultimoAlbaran));

        //rellenamos trabajadorActual
        idTrabajadorActual = trabajadorActual.getString(1);
        nombreTrabajadorActual = trabajadorActual.getString(2);

        nombreTrabajador.setText(nombreTrabajadorActual);

        //comprobacion si no esta configurado el trabajador
        if (idTrabajadorActual.equals("ßß")){
            Intent intento = new Intent(this, FormularioTrabajador.class);
            startActivity(intento);

        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        mOperacionesBaseDatos.cerrar();
    }

    public void botonListaClientes(View v) {
        Intent intento = new Intent(this, ListaClientes.class);
        startActivity(intento);

    }

    public void botonEditarTrabajador(View v){
        Intent intento = new Intent(this, FormularioTrabajador.class);
        intento.putExtra("id", idTrabajadorActual);
        intento.putExtra("nombre", nombreTrabajadorActual);
        startActivity(intento);
    }

    public void botonListaAlbaranes(View v){
        Intent intento = new Intent(this, ListaAlbaranesCabecera.class);
        intento.putExtra("idTrabajador", idTrabajadorActual);
        startActivity(intento);
    }

    public void botonNuevaTemporadaAlbaranComprobacion(View v){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText etPassword = new EditText(MainActivity.this);

        etPassword.setHint("password");

        builder.setMessage(getString(R.string.mensaje_nueva_temporada_pasword))
                .setTitle(this.getString(R.string.nueva_temporada))
                .setCancelable(false)
                .setView(etPassword)
                .setNegativeButton(this.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String introPass = etPassword.getText().toString();

                                if (introPass.equals("1234")){
                                    crearNuevaTemporadaAlbaran();


                                } else {
                                    Toast.makeText(MainActivity.this, R.string.pass_false,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void crearNuevaTemporadaAlbaran(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText etIdAlbaran = new EditText(MainActivity.this);


        etIdAlbaran.setHint("codigo");
        builder.setMessage(getString(R.string.mensaje_nueva_temporada_pasword))
                .setTitle(this.getString(R.string.nueva_temporada))
                .setCancelable(false)
                .setView(etIdAlbaran)
                .setNegativeButton(this.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int nuevoNumAlbaran = Integer.parseInt(etIdAlbaran.getText().toString());
                                nuevoNumAlbaran--;
                                mOperacionesBaseDatos.nuevaCabeceraAlbaran(nuevoNumAlbaran,idTrabajadorActual);

                                botonListaAlbaranes(null);


                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }


}