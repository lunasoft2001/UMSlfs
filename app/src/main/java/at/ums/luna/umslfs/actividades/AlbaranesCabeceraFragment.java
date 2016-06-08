package at.ums.luna.umslfs.actividades;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.adaptadores.DialogoListaClientes;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.CabeceraAlbaranes;
import at.ums.luna.umslfs.modelos.Clientes;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbaranesCabeceraFragment extends Fragment{

    private String codigoAlbaranObtenido;

    private OperacionesBaseDatos mOperacionesBaseDatos;
    private Context esteContexto;

    TextView codigoAlbaran;
    TextView fecha;
    TextView idCliente;
    TextView nombreCliente;
    TextView direccionCliente;



    public AlbaranesCabeceraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();

        Bundle args = getArguments();
        codigoAlbaranObtenido = args.getString("codigoObtenido");




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albaranes_cabecera, container, false);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);






        refrescarDatos();


        //Codigo para el onClickListener
        getView().findViewById(R.id.botonCancelarAlbaran).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonBorrarAlbaran).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonActualizarAlbaran).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.imageButtonElegirCliente).setOnClickListener(mGlobal_onClickListener);

    }

    //Intents para cualquier botón de la actividad
    final View.OnClickListener mGlobal_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.botonCancelarAlbaran:
                    getActivity().finish();
                    break;
                case R.id.botonBorrarAlbaran:
                    Eliminar();
                    break;
                case R.id.botonActualizarAlbaran:
                    Actualizar();
                    break;
                case R.id.imageButtonElegirCliente:
                    seleccionarCliente();
                    break;

            }
        }
    };


    private void refrescarDatos() {
        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);
        CabeceraAlbaranes albaranActual = mOperacionesBaseDatos.obtenerCabeceraAlbaran(codigoAlbaranObtenido);

        codigoAlbaran = (TextView)getView().findViewById(R.id.tvCodigoAlbaran);
        fecha = (TextView)getView().findViewById(R.id.tvFecha);
        idCliente = (TextView)getView().findViewById(R.id.tvIdCliente);
        nombreCliente = (TextView)getView().findViewById(R.id.tvNombreCliente);
        direccionCliente = (TextView)getView().findViewById(R.id.tvDireccionCliente);

        codigoAlbaran.setText(codigoAlbaranObtenido);
        fecha.setText(albaranActual.getFecha().toString());
        idCliente.setText(String.valueOf(albaranActual.getIdCliente()));
        nombreCliente.setText(albaranActual.getNombreCliente());
        direccionCliente.setText(albaranActual.getDireccionCliente());
    }


    private void Eliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(esteContexto);
        builder.setMessage(esteContexto.getString(R.string.ContinuarBorrando))
                .setTitle(esteContexto.getString(R.string.Advertencia))
                .setCancelable(false)
                .setNegativeButton(esteContexto.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(esteContexto.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String [] codigoAlbAEliminar = {codigoAlbaranObtenido};

                                mOperacionesBaseDatos.eliminarCabeceraAlbaran(codigoAlbAEliminar, esteContexto);

                                getActivity().finish();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void Actualizar(){

        String [] idAlbaranActual = {codigoAlbaranObtenido};
        String fechaActual = fecha.getText().toString();
        int idClienteActual= Integer.parseInt(idCliente.getText().toString());

        mOperacionesBaseDatos.actualizarCabeceraAlbaran(idAlbaranActual,fechaActual,idClienteActual);

        getActivity().finish();
    }

    private void seleccionarCliente(){

        final List<Clientes> listaDeClientes= mOperacionesBaseDatos.verListaClientesCompletaPorNombre();

        final Dialog dialog = new Dialog(esteContexto);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_lista_clientes, null);

        ListView lv = (ListView) view.findViewById(R.id.lista1);
        DialogoListaClientes dialogo = new DialogoListaClientes(esteContexto, (ArrayList<Clientes>) listaDeClientes);

        lv.setAdapter(dialogo);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                idCliente.setText(String.valueOf(listaDeClientes.get(position).getId()));
                nombreCliente.setText(listaDeClientes.get(position).getNombre());
                direccionCliente.setText(listaDeClientes.get(position).getDireccion());

                //cierra Dialogo
                dialog.cancel();
            }
        });
        dialog.setTitle(R.string.seleccione_cliente);
        dialog.setContentView(view);
        dialog.show();



    }

}
