package at.ums.luna.umslfs.actividades;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.Trabajador;

public class FormularioTrabajador extends AppCompatActivity {


    private OperacionesBaseDatos mOperacionesBaseDatos;

    private EditText idTrabajador;
    private EditText nombreTrabajador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_trabajador);


        idTrabajador = (EditText) findViewById(R.id.tvIdTrabajador);
        nombreTrabajador = (EditText) findViewById(R.id.tvNombreTrabajador);


        Intent intento = getIntent();

        Bundle bundle = intento.getExtras();
        if(bundle != null) {
            idTrabajador.setText(bundle.getString("id"));
            nombreTrabajador.setText(bundle.getString("nombre"));

        }

        mOperacionesBaseDatos = new OperacionesBaseDatos(this);
        mOperacionesBaseDatos.abrir();



        findViewById(R.id.botonActualizarTrabajador).setOnClickListener(mGlobal_onClickListener);

    }


    final View.OnClickListener mGlobal_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.botonActualizarTrabajador:
                    Actualizar();
                    break;
            }
        }
    };

    public void Actualizar(){

        String idNuevo = idTrabajador.getText().toString();
        String nombreNuevo = nombreTrabajador.getText().toString();

        mOperacionesBaseDatos.actualizarTrabajador(idNuevo, nombreNuevo);
        mOperacionesBaseDatos.cerrar();
        finish();
    }


}
