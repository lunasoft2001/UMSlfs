package at.ums.luna.umslfs.inicio;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PushBroadcastMask;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.actividades.FormularioTrabajador;
import at.ums.luna.umslfs.actividades.ListaAlbaranesCabecera;
import at.ums.luna.umslfs.actividades.ListaClientes;
import at.ums.luna.umslfs.actividades.Preferencias;
import at.ums.luna.umslfs.database.OperacionesBaseDatos;
import at.ums.luna.umslfs.servidor.DefaultCallback;
import at.ums.luna.umslfs.servidor.Defaults;

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;

    private OperacionesBaseDatos mOperacionesBaseDatos;
    private Cursor trabajadorActual;


    private TextView tvUltimoAlbaran;
    private Toolbar toolbar;

    private String idTrabajadorActual;
    private String nombreTrabajadorActual;
    private String userActual;
    private String passActual;

    private int ultimoAlbaran;

//    private int ULTIMO_NUMERO_ALBARAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cargar settings por defecto
        PreferenceManager.setDefaultValues(this,R.xml.settings,false);

        //activamos la toolbar
        setToolbar();


        //permisos
        solicitarPermisos();


        //conectamos con el servidor
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        //activamos el servicio PUSH de backendless

        Backendless.Messaging.registerDevice("968560581630", "default", new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void aVoid) {

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(MainActivity.this, backendlessFault.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        tvUltimoAlbaran = (TextView) findViewById(R.id.tvMaxAlbaran);


        mOperacionesBaseDatos = new OperacionesBaseDatos(this);

//        trabajadorActual = mOperacionesBaseDatos.verTrabajador();
//        trabajadorActual.moveToFirst();

        //rellenamos ultimoAlbaran
        ultimoAlbaran = mOperacionesBaseDatos.ultimaCabeceraAlbaran();
        tvUltimoAlbaran.setText(String.valueOf(ultimoAlbaran));

        //rellenamos trabajadorActual
        mostrarPreferencias();
//        idTrabajadorActual = trabajadorActual.getString(1);
//        nombreTrabajadorActual = trabajadorActual.getString(2);

        toolbar.setTitle(String.format(this.getString(R.string.Trabajador), nombreTrabajadorActual));

        //comprobacion si no esta configurado el trabajador
        if (idTrabajadorActual.equals("ßß")){
//            Intent intento = new Intent(this, FormularioTrabajador.class);
            Intent intento = new Intent(this, Preferencias.class);
            startActivity(intento);

        }

        //registrarse en la base de datos.
//        registroUsuarioServidor();




    }

    @Override
    protected void onStop() {
        super.onStop();
        mOperacionesBaseDatos.cerrar();

    }

    private void registroUsuarioServidor() {
        Backendless.UserService.login(userActual,passActual,new DefaultCallback<BackendlessUser>(MainActivity.this)
        {
            @Override
            public void handleResponse(BackendlessUser response) {
                super.handleResponse(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                super.handleFault(fault);
                Intent intento = new Intent(MainActivity.this, Preferencias.class);
                startActivity(intento);
            }
        });
    }

    private void LogoutUsuarioServidor()
    {
        Backendless.UserService.logout( new DefaultCallback<Void>( this )
        {
            @Override
            public void handleResponse( Void response )
            {
                super.handleResponse( response );

            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
                if( fault.getCode().equals( "3023" ) ) // Unable to logout: not logged in (session expired, etc.)
                    handleResponse( null );
                else
                    super.handleFault( fault );
            }
        } );

    }

    private void solicitarPermisos() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
    }

    private void setToolbar(){
        //añadir la Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    public void botonListaClientes(View v) {
        Intent intento = new Intent(this, ListaClientes.class);
        startActivity(intento);

    }

    public void botonActualizarBdClientes(View v) {
        mOperacionesBaseDatos.actualizarClientes(this);
    }

    public void botonEditarTrabajador(View v){
//        Intent intento = new Intent(this, FormularioTrabajador.class);
//        intento.putExtra("id", idTrabajadorActual);
//        intento.putExtra("nombre", nombreTrabajadorActual);
//        startActivity(intento);
        Intent intento = new Intent(this, Preferencias.class);
        startActivity(intento);



    }

    public void botonListaAlbaranes(View v){
        Intent intento = new Intent(this, ListaAlbaranesCabecera.class);
        intento.putExtra("idTrabajador", idTrabajadorActual);
        startActivity(intento);
    }

    public void botonNuevaTemporadaAlbaranComprobacion(View v){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText etPassword = new EditText(MainActivity.this);

        etPassword.setHint("password");

        builder.setMessage(getString(R.string.mensaje_nueva_temporada_pasword))
                .setTitle(this.getString(R.string.nueva_temporada))
                .setCancelable(false)
                .setView(etPassword)
                .setNegativeButton(this.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String introPass = etPassword.getText().toString();

                                if (introPass.equals("1234")){
                                    crearNuevaTemporadaAlbaran();


                                } else {
                                    Toast.makeText(MainActivity.this, R.string.pass_false,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void crearNuevaTemporadaAlbaran(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText etIdAlbaran = new EditText(MainActivity.this);


        etIdAlbaran.setHint("codigo");
        builder.setMessage(getString(R.string.mensaje_nueva_temporada_pasword))
                .setTitle(this.getString(R.string.nueva_temporada))
                .setCancelable(false)
                .setView(etIdAlbaran)
                .setNegativeButton(this.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int nuevoNumAlbaran = Integer.parseInt(etIdAlbaran.getText().toString());
                                nuevoNumAlbaran--;
                                mOperacionesBaseDatos.nuevaCabeceraAlbaran(nuevoNumAlbaran,idTrabajadorActual);

                                botonListaAlbaranes(null);


                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    public void mostrarPreferencias(){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);

        idTrabajadorActual = pref.getString("prefijo","?");
        nombreTrabajadorActual = pref.getString("nombre","?");
        userActual = pref.getString("usuario","?");
        passActual = pref.getString("password","?");

    }

    public void botonEnviarPush(View v){

        if (idTrabajadorActual.equals("MC")) {

            DeliveryOptions deliveryOptions = new DeliveryOptions();

            deliveryOptions.setPushBroadcast(PushBroadcastMask.ANDROID);

            PublishOptions publishOptions = new PublishOptions();
            publishOptions.putHeader( "android-ticker-text", "You just got a push notification!" );
            publishOptions.putHeader( "android-content-title", "This is a notification title" );
            publishOptions.putHeader( "android-content-text", "Push Notifications are cool" );

            Backendless.Messaging.publish("Hi Devices!", publishOptions, deliveryOptions,
                    new AsyncCallback<MessageStatus>() {
                        @Override
                        public void handleResponse(MessageStatus messageStatus) {
                            Log.i("JUANJO", messageStatus.toString());
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            Log.i("JUANJO", backendlessFault.toString());

                        }
                    }

            );
        }

    }

}