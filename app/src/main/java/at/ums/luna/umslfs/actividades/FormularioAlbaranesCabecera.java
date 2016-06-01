package at.ums.luna.umslfs.actividades;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import at.ums.luna.umslfs.R;

public class FormularioAlbaranesCabecera extends FragmentActivity{


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
                    return "CONTENIDOS";
                case 1:
                    return "IMAGEN";
                case 2:
                    return "FIRMA";
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
