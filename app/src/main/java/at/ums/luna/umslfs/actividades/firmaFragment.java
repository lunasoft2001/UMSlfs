package at.ums.luna.umslfs.actividades;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import at.ums.luna.umslfs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class firmaFragment extends Fragment {

    public static final int SIGNATURE_ACTIVITY = 1;


    private Context esteContexto;

    private String tempDir;
    ImageView imagen;
    private String nombreFoto = "firma_juanjo.png";

    public firmaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firma, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        Button getSignature = (Button) getView().findViewById(R.id.botonFirma);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(esteContexto, capturarFirma.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";

        imagen = (ImageView) getView().findViewById(R.id.imageFirma);
        Bitmap bMap = BitmapFactory.decodeFile(tempDir + nombreFoto);

        imagen.setImageBitmap(bMap);


    }


    //    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        switch(requestCode) {
//            case SIGNATURE_ACTIVITY:
//                if (resultCode == 123) {
//
//                    Bundle bundle = data.getExtras();
//                    String status  = bundle.getString("status");
//                    if(status.equalsIgnoreCase("done")){
//                        Toast.makeText(esteContexto, "Signature capture successful!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//        }
//
//    }
}
