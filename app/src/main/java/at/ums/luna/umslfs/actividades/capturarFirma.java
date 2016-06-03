package at.ums.luna.umslfs.actividades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kyanogen.signatureview.SignatureView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import at.ums.luna.umslfs.R;

public class capturarFirma extends AppCompatActivity {

    SignatureView signatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturar_firma);

        signatureView = (SignatureView) findViewById(R.id.signature_view);

    }

    public void limpiar(View v){

        signatureView.clearCanvas();
    }

    public void guardarFirma(View v) {


        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(),getResources().getString(R.string.external_dir));
        imagesFolder.mkdirs();
        OutputStream fOut = null;
        File file = new File(imagesFolder,"firma_Juanjo.png");

        Bitmap bitmap = signatureView.getSignatureBitmap();

        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fOut);

            if (fOut != null){
                fOut.flush();
                fOut.close();

                if (bitmap !=null){
                    Toast.makeText(getApplicationContext(),"Firma guardada",Toast.LENGTH_LONG).show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();

    }

}
