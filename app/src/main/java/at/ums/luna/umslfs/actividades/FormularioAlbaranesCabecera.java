package at.ums.luna.umslfs.actividades;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;

public class FormularioAlbaranesCabecera extends FragmentActivity {

    OperacionesBaseDatos mOperacionesBaseDatos;


    private String codigoAlbaranObtenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_albaranes_cabecera);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new AdaptadorPager(getSupportFragmentManager()));

        //Obtener el codigoCabecera
        Intent intento = getIntent();
        Bundle bundle = intento.getExtras();
        if (bundle != null){
            codigoAlbaranObtenido = bundle.getString("codigoAlbaran");
        }

    }

    private class AdaptadorPager extends FragmentPagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return getString(R.string.CONTENIDOS);
                case 1:
                    return getString(R.string.IMAGEN);
                case 2:
                    return getString(R.string.FIRMA);
                default:
                    return "---";
            }
        }

        public AdaptadorPager(FragmentManager fm) { super(fm);}

        @Override
        public Fragment getItem(int position) {

            Bundle args = new Bundle();
            args.putString("codigoObtenido", codigoAlbaranObtenido);


            switch (position){
                case 0:
                    AlbaranesCabeceraFragment f1 = new AlbaranesCabeceraFragment();
                    f1.setArguments(args);
                    return f1;
                case 1:
                    FotoFragment f2 = new FotoFragment();
                    f2.setArguments(args);
                    return f2;
                case 2:
                    firmaFragment f3 = new firmaFragment();
                    f3.setArguments(args);
                    return f3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, R.string.albaran_cerrado, Toast.LENGTH_SHORT).show();
        finish();
    }


}
