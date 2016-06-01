package at.ums.luna.umslfs.actividades;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.CabeceraAlbaranes;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbaranesCabeceraFragment extends Fragment {

    private String codigoAlbaranObtenido;
    private OperacionesBaseDatos mOperacionesBaseDatos;
    private Context esteContexto;

    public AlbaranesCabeceraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albaranes_cabecera, container, false);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Obtenemos los datos para mostrar en pantalla
         */
        Intent intento = getActivity().getIntent();
        Bundle bundle = intento.getExtras();
        if (bundle != null){
            codigoAlbaranObtenido = bundle.getString("codigoAlbaran");
        }

        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);
        CabeceraAlbaranes albaranActual = mOperacionesBaseDatos.obtenerCabeceraAlbaran(codigoAlbaranObtenido);


        String fechaCorrecta = albaranActual.getFechaFormateada().toString();
        Log.i("JUANJO", "Fecha correcta " + fechaCorrecta);

        TextView codigoAlbaran = (TextView)getView().findViewById(R.id.tvCodigoAlbaran);
        TextView fecha = (TextView)getView().findViewById(R.id.tvFecha);
        TextView idCliente = (TextView)getView().findViewById(R.id.tvIdCliente);
        TextView nombreCliente = (TextView)getView().findViewById(R.id.tvNombreCliente);
        TextView direccionCliente = (TextView)getView().findViewById(R.id.tvDireccionCliente);

        codigoAlbaran.setText(codigoAlbaranObtenido);
        fecha.setText(albaranActual.getFechaFormateada().toString());
        idCliente.setText(String.valueOf(albaranActual.getIdCliente()));
        nombreCliente.setText(albaranActual.getNombreCliente());
        direccionCliente.setText(albaranActual.getDireccionCliente());

    }

}
