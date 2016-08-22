package at.ums.luna.umslfs.actividades;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.adaptadores.ListaClientesAdapter;
import at.ums.luna.umslfs.adaptadores.RecyclerItemClickListener;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.Clientes;

public class ListaClientes extends AppCompatActivity {

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
    private List<Clientes> mClientes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);

        /*
        Codigo para obtenrer los datos de la DB
         */
        mOperacionesBaseDatos = new OperacionesBaseDatos(this);
        mOperacionesBaseDatos.abrir();

//        mClientes = mOperacionesBaseDatos.verListaClientesCompleta();
//        mClientes = mOperacionesBaseDatos.verListaClientesServidor();


        // Llamamos la taraea Async
        new ListClientAsync().execute();

    }

    public void initAdapter(){

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador

        adapter = new ListaClientesAdapter(mClientes); //para el SQLite
        recycler.setAdapter(adapter);

        //Este metodo esta implementando la clase RecyclerItemClickListener que he creado
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Clientes clienteElegido = mClientes.get(position);

                        Toast.makeText(ListaClientes.this,"el Email es " + clienteElegido.getEmail().toString() , Toast.LENGTH_LONG).show();

//                        Intent intento = new Intent(ListaClientes.this, FormularioClientes.class);
//                        intento.putExtra("idCliente",String.valueOf(clienteElegido.getIdCliente()));
//                        intento.putExtra("nombre",clienteElegido.getNombre());
//                        intento.putExtra("direccion",clienteElegido.getDireccion());
//                        intento.putExtra("telefono",clienteElegido.getTelefono());
//
//                        startActivity(intento);

                    }
                })
        );

    }

    private class ListClientAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //obtenemos el listado de Clientes de Backendless
            mClientes = mOperacionesBaseDatos.verListaClientesServidor();
            Log.i("JUANJO", "Se han obtenido " + mClientes.size() +  " registros");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //inicializamos el RecyclerView y el adapter con la data obtenida
            initAdapter();
        }
    }


}
