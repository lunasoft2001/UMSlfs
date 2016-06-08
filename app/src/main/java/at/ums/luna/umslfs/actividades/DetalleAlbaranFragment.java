package at.ums.luna.umslfs.actividades;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.adaptadores.ListaAlbaranesDetalleAdapter;
import at.ums.luna.umslfs.adaptadores.ListaClientesAdapter;
import at.ums.luna.umslfs.adaptadores.RecyclerItemClickListener;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.Clientes;
import at.ums.luna.umslfs.modelos.DetalleAlbaranes;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleAlbaranFragment extends Fragment {


    /*
     Declarar instancias globales
      */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;


    private String codigoAlbaranObtenido;
    private OperacionesBaseDatos mOperacionesBaseDatos;
    private Context esteContexto;

    private List<DetalleAlbaranes> mDetalleAlbaranes;

    private TextView ultimaLinea;
    private int valorUltimaLinea;

    public DetalleAlbaranFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();

        Bundle args = getArguments();
        codigoAlbaranObtenido = args.getString("codigoObtenido");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_albaran, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refrescarDatos();

        //Codigo para el onClickListener
        getView().findViewById(R.id.botonNuevaLinea).setOnClickListener(mGlobal_onClickListener);
    }

    //Intents para cualquier botón de la actividad
    final View.OnClickListener mGlobal_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.botonNuevaLinea:
                    nuevaLinea();
                    break;
            }
        }
    };


    private void refrescarDatos() {
        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);

        valorUltimaLinea = mOperacionesBaseDatos.ultimaLineaAlbaran(codigoAlbaranObtenido);

        ultimaLinea = (TextView) getView().findViewById(R.id.tvNumeroLineas);


        ultimaLinea.setText(String.valueOf(valorUltimaLinea));

        // edicion
        mDetalleAlbaranes = mOperacionesBaseDatos.verListaDetalleAlbaran(codigoAlbaranObtenido);


        // Obtener el Recycler
        recycler = (RecyclerView) getView().findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(esteContexto);
        recycler.setLayoutManager(lManager);


        // Crear un nuevo adaptador

        adapter = new ListaAlbaranesDetalleAdapter(mDetalleAlbaranes);
        recycler.setAdapter(adapter);

        //Este metodo esta implementando la clase RecyclerItemClickListener que he creado
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(esteContexto, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        DetalleAlbaranes detalleElegido = mDetalleAlbaranes.get(position);

                        Toast.makeText(esteContexto,"Has marcado la linea " + String.valueOf(detalleElegido.getLinea()) , Toast.LENGTH_LONG).show();


                    }
                })
        );

    }


    public void nuevaLinea(){
        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);
        mOperacionesBaseDatos.nuevoDetalleAlbaran(valorUltimaLinea,codigoAlbaranObtenido);

    }

}
