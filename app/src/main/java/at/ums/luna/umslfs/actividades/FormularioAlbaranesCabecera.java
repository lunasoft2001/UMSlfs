package at.ums.luna.umslfs.actividades;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.database.DBHelper;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.modelos.CabeceraAlbaranes;

public class FormularioAlbaranesCabecera extends FragmentActivity{

    OperacionesBaseDatos mOperacionesBaseDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_albaranes_cabecera);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new AdaptadorPager(getSupportFragmentManager()));


        /*
        Codigo para obtener los valores a utilizar en la actividad
         */
        String codigoAlbaran = "";
        Intent intento = getIntent();
        Bundle bundle = intento.getExtras();
        if(bundle !=null){
            codigoAlbaran = bundle.getString("codigoAlbaran");
        }

        Log.i("JUANJO", codigoAlbaran);
        mOperacionesBaseDatos = new OperacionesBaseDatos(this);
        Cursor cursor = mOperacionesBaseDatos.obtenerCabeceraAlbaran(codigoAlbaran);
        cursor.moveToFirst();


        String fecha;

        fecha = cursor.getString(cursor.getColumnIndex(DBHelper.CabeceraAlbaranesColumnas.FECHA));

        Log.i("JUANJO", fecha);


        CabeceraAlbaranes albaranActual = mOperacionesBaseDatos.obtenerCabeceraAlbaran1(codigoAlbaran);


        String fechaCorrecta = albaranActual.getFechaFormateada().toString();
        Log.i("JUANJO", "Fecha correcta " + fechaCorrecta);


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
            switch (position){
                case 0:
                    return new AlbaranesCabeceraFragment();
                case 1:
                    return new AlbaranesCabeceraFragment();
                case 2:
                    return new AlbaranesCabeceraFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
