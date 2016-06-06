package at.ums.luna.umslfs.inicio;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.actividades.FormularioTrabajador;
import at.ums.luna.umslfs.actividades.ListaAlbaranesCabecera;
import at.ums.luna.umslfs.actividades.ListaClientes;
import at.ums.luna.umslfs.database.DBHelper;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.Trabajador;

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
            Log.i("JUANJO", "MODO DEPURACION");
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

}