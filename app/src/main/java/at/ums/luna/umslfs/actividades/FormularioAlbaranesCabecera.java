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
