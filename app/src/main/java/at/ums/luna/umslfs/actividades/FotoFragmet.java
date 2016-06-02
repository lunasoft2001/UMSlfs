package at.ums.luna.umslfs.actividades;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import at.ums.luna.umslfs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FotoFragmet extends Fragment {

    private String nombreFoto = "";
    private Context esteContexto;
    private static int TAKE_PICTURE = 1;


    public FotoFragmet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foto, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nombreFoto = Environment.getExternalStorageDirectory() + "/" +
                getResources().getString(R.string.external_dir) + "/" +
                "foto_juanjo" + ".png";

        ImageView iv = (ImageView) getView().findViewById(R.id.imageFoto);
       // iv.setImageResource(nombreFoto);


        Button getSignature = (Button) getView().findViewById(R.id.botonFoto);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri output = Uri.fromFile(new File(nombreFoto));
                intent.putExtra(MediaStore.EXTRA_OUTPUT,output);

                startActivityForResult(intent,TAKE_PICTURE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (data.hasExtra("data")) {
                ImageView iv = (ImageView) getView().findViewById(R.id.imageFoto);
                iv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
            } else{
                ImageView iv = (ImageView) getView().findViewById(R.id.imageFoto);
                iv.setImageBitmap(BitmapFactory.decodeFile(nombreFoto));
            }
        }




        super.onActivityResult(requestCode, resultCode, data);
    }
}
