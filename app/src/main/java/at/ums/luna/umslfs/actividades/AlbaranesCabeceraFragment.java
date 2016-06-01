package at.ums.luna.umslfs.actividades;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import at.ums.luna.umslfs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbaranesCabeceraFragment extends Fragment {


    public AlbaranesCabeceraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albaranes_cabecera, container, false);

    }


    public void inflarCampos (String mCodigoAlbaran){
        TextView codigoAlbaran = (TextView)getView().findViewById(R.id.tvCodigoAlbaran);
        TextView fecha = (TextView)getView().findViewById(R.id.tvFecha);
        TextView idCliente = (TextView)getView().findViewById(R.id.tvIdCliente);
        TextView nombreCliente = (TextView)getView().findViewById(R.id.tvNombreCliente);
        TextView direccionCliente = (TextView)getView().findViewById(R.id.tvDireccionCliente);

        codigoAlbaran.setText(mCodigoAlbaran);
        fecha.setText("2");
        idCliente.setText("3");
        nombreCliente.setText("4");
        direccionCliente.setText("5");

    }

}
