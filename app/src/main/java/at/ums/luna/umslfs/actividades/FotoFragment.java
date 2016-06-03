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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import at.ums.luna.umslfs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FotoFragment extends Fragment {

    private String nombreFoto = "foto_juanjo.jpg";
    private String tempDir;
    ImageView imagen;

    private Context esteContexto;
    private static int TAKE_PICTURE = 1;
    public static final int RESULT_OK = -1;

    public FotoFragment() {
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

        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";

        imagen = (ImageView) getView().findViewById(R.id.imageFoto);
        Bitmap bMap = BitmapFactory.decodeFile(tempDir + nombreFoto);

        imagen.setImageBitmap(bMap);



        ImageButton getSignature = (ImageButton) getView().findViewById(R.id.botonFoto);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imagesFolder = new File(
                        Environment.getExternalStorageDirectory(),getResources().getString(R.string.external_dir));
                imagesFolder.mkdirs();
                File image = new File(imagesFolder,nombreFoto);
                Uri uriSavedImage = Uri.fromFile(image);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(intent,TAKE_PICTURE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bMap = BitmapFactory.decodeFile(
                    tempDir + nombreFoto);
            imagen.setImageBitmap(bMap);

            Toast.makeText(esteContexto, "Foto capture successful!", Toast.LENGTH_SHORT).show();
        } else
        {Toast.makeText(esteContexto, "Foto capture error!", Toast.LENGTH_SHORT).show();

        }
    }
}
