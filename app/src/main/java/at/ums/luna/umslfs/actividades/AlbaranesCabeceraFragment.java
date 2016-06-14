package at.ums.luna.umslfs.actividades;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.adaptadores.DatePickerFragment;
import at.ums.luna.umslfs.adaptadores.DialogoListaCampoClientes;
import at.ums.luna.umslfs.database.DBHelper;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.CabeceraAlbaranes;
import at.ums.luna.umslfs.modelos.Clientes;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbaranesCabeceraFragment extends Fragment {

    private String codigoAlbaranObtenido;

    private OperacionesBaseDatos mOperacionesBaseDatos;
    private Context esteContexto;

    TextView fecha;
    TextView idCliente;
    TextView nombreCliente;
    TextView direccionCliente;

    int textlength = 0;
    private EditText et;
    ListView lv;
    private ArrayList<String> array_sort = new ArrayList<String>();
    private String listview_array[];





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


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        refrescarDatos();

        //Codigo para el onClickListener
        getView().findViewById(R.id.botonCancelarAlbaran).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonBorrarAlbaran).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonActualizarAlbaran).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.imageButtonElegirCliente).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.tvFecha).setOnClickListener(mGlobal_onClickListener);

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
                    getActivity().finish();
                    break;
                case R.id.imageButtonElegirCliente:
                    seleccionarCliente();
                    break;
                case R.id.tvFecha:
                    seleccionarFecha();
                    break;

            }
        }
    };


    private void refrescarDatos() {
        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);
        CabeceraAlbaranes albaranActual = mOperacionesBaseDatos.obtenerCabeceraAlbaran(codigoAlbaranObtenido);

        fecha = (TextView)getView().findViewById(R.id.tvFecha);
        idCliente = (TextView)getView().findViewById(R.id.tvIdCliente);
        nombreCliente = (TextView)getView().findViewById(R.id.tvNombreCliente);
        direccionCliente = (TextView)getView().findViewById(R.id.tvDireccionCliente);

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


    }

    private void seleccionarCliente(){

        String[]camposMostrados = {DBHelper.Clientes.NOMBRE};

        final ArrayList<String> listaDeClientesInicial= mOperacionesBaseDatos.datosCampoClientes(camposMostrados);

        final Dialog dialog = new Dialog(esteContexto);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_lista_clientes, null);

        lv = (ListView) view.findViewById(R.id.lista1);
        et = (EditText) view.findViewById(R.id.etBusqueda);

        DialogoListaCampoClientes dialogo = new DialogoListaCampoClientes(esteContexto, listaDeClientesInicial);

        lv.setAdapter(dialogo);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String valorObtenido = listaDeClientesInicial.get(position).toString();

                Clientes clienteElegido = mOperacionesBaseDatos.obtenerClienteAlElegirEnDialogo(valorObtenido);


                idCliente.setText(String.valueOf(clienteElegido.getId()));
                nombreCliente.setText((clienteElegido.getNombre()));
                direccionCliente.setText(clienteElegido.getDireccion());

                Actualizar();

                dialog.cancel();
            }
        });
        dialog.setTitle(R.string.seleccione_cliente);
        dialog.setContentView(view);
        dialog.show();

        listview_array = listaDeClientesInicial.toArray(new String[0]);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength = et.getText().length();
                array_sort.clear();

                for (int i = 0; i < listview_array.length; i++) {
                    if (textlength <= listview_array[i].length()) {
                        if (et.getText().toString().equalsIgnoreCase((String) listview_array[i].subSequence(0, textlength))) {
                            array_sort.add(listview_array[i]);
                        }
                    }
                }

                DialogoListaCampoClientes dialogoFinal = new DialogoListaCampoClientes(esteContexto, array_sort);

                lv.setAdapter(dialogoFinal);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        String valorObtenido = array_sort.get(position).toString();

                        Clientes clienteElegido1 = mOperacionesBaseDatos.obtenerClienteAlElegirEnDialogo(valorObtenido);


                        idCliente.setText(String.valueOf(clienteElegido1.getId()));
                        nombreCliente.setText((clienteElegido1.getNombre()));
                        direccionCliente.setText(clienteElegido1.getDireccion());

                        Actualizar();

                        dialog.cancel();
                    }
                });

            }
        });


    }


    private void seleccionarFecha(){
        DatePickerFragment newFragment =  new  DatePickerFragment();
        newFragment.show(getFragmentManager(),"datePicker");


    }







}
