package at.ums.luna.umslfs.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.adaptadores.ListaAlbaranesCabeceraAdapter;
import at.ums.luna.umslfs.adaptadores.RecyclerItemClickListener;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.CabeceraAlbaranes;

public class ListaAlbaranesCabecera extends AppCompatActivity {

    /*
  Declarar instancias globales
   */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;


    /*
    Variables SOLO para SQLite
     */
    OperacionesBaseDatos mOperacionesBaseDatos;
    private List<CabeceraAlbaranes> mCabeceraAlbaranes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_albaranes_cabecera);

        /*
        Codigo para obtener los datos de la DB
         */

        mOperacionesBaseDatos = new OperacionesBaseDatos(this);
        mOperacionesBaseDatos.abrir();

        mCabeceraAlbaranes = mOperacionesBaseDatos.verListaAlbaranesCabeceraCompleta();



        /*
        Obtener el Recycler
         */

        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        /*
        Usar un administrador para LinearLayout
         */
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        /*
        Crear un nuevo adaptador
         */

        adapter = new ListaAlbaranesCabeceraAdapter(mCabeceraAlbaranes);
        recycler.setAdapter(adapter);

        //Este metodo esta implementando la clase RecyclerItemClickListener que he creado
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        CabeceraAlbaranes cabeceraActual = mCabeceraAlbaranes.get(position);
                        Intent intento = new Intent(ListaAlbaranesCabecera.this,FormularioAlbaranesCabecera.class);
                        intento.putExtra("codigoAlbaran",cabeceraActual.getCodigoAlbaran());
                        startActivity(intento);

                    }
                })
        );

    }
}
