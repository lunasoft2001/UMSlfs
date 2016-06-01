package at.ums.luna.umslfs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ums.luna.umslfs.database.DBHelper.Trabajadores;
import at.ums.luna.umslfs.modelos.CabeceraAlbaranes;
import at.ums.luna.umslfs.modelos.Clientes;

/**
 * Created by luna-aleixos on 30.05.2016.
 */
public class OperacionesBaseDatos {
    DBHelper dbHelper;
    SQLiteDatabase db;

    public OperacionesBaseDatos(Context context){
        dbHelper = new DBHelper(context);
    }

    public void abrir(){
        Log.i("JUANJO", "BD abierta para escribir");
        db = dbHelper.getWritableDatabase();
    }

    public void leer(){
        Log.i("JUANJO", "BD abierta para leer");
        db = dbHelper.getReadableDatabase();
    }

    public void cerrar(){
        Log.i("JUANJO", "BD cerrada");
        dbHelper.close();
    }

    /**
     * TRABAJADORES
     */

    public Cursor verTrabajador(){
        abrir();
        return db.rawQuery("SELECT * FROM " + DBHelper.Tablas.TRABAJADOR, null);
    }

    public void actualizarTrabajador(String idNuevo, String nombreNuevo){

        ContentValues valores = new ContentValues();
        valores.put(Trabajadores.ID, idNuevo);
        valores.put(Trabajadores.NOMBRE, nombreNuevo);

        String whereClause = Trabajadores.CODIGO_INTERNO + " = " + 1;

        db.update(DBHelper.Tablas.TRABAJADOR, valores, whereClause, null);
    }

    /**
     * CLIENTES
     */
    public static final String[] todasColumnasClientes = {
            DBHelper.Clientes.ID,
            DBHelper.Clientes.NOMBRE,
            DBHelper.Clientes.DIRECCION,
            DBHelper.Clientes.TELEFONO,
            DBHelper.Clientes.EMAIL
    };

    public List<Clientes> verListaClientesCompleta(){
        Cursor cursor = db.query(DBHelper.Tablas.CLIENTES,todasColumnasClientes,null,null,null,null,null);

        List<Clientes> listaClientes = new ArrayList<>();
        while (cursor.moveToNext()){
            Clientes clientes = new Clientes();
            clientes.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.Clientes.ID)));
            clientes.setNombre(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.NOMBRE)));
            clientes.setDireccion(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.DIRECCION)));
            clientes.setTelefono(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.TELEFONO)));
            clientes.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.EMAIL)));
            listaClientes.add(clientes);
        }
        return listaClientes;
    }


    /**
     * ALBARANES CABECERA
     */

    public List<CabeceraAlbaranes> verListaAlbaranesCabeceraCompleta(){
        String query = "SELECT " +
                DBHelper.Tablas.CABECERA_ALBARANES + "." + DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN + "," +
                DBHelper.Tablas.CABECERA_ALBARANES + "." + DBHelper.CabeceraAlbaranesColumnas.FECHA + "," +
                DBHelper.Tablas.CLIENTES + "." + DBHelper.Clientes.NOMBRE +
                " FROM " + DBHelper.Tablas.CLIENTES + " INNER JOIN " + DBHelper.Tablas.CABECERA_ALBARANES +
                " ON " + DBHelper.Tablas.CLIENTES + "." + DBHelper.Clientes.ID + " = " +
                DBHelper.Tablas.CABECERA_ALBARANES + "." + DBHelper.CabeceraAlbaranesColumnas.ID_CLIENTE ;

        String querySimple = "SELECT cabecera_albaranes.codigoAlbaran, " +
                "cabecera_albaranes.fecha, clientes.nombre " +
                "FROM clientes INNER JOIN cabecera_albaranes " +
                "ON clientes.id = cabecera_albaranes.idCliente";

        Cursor cursor = db.rawQuery(querySimple,null,null);

        List<CabeceraAlbaranes> listaAlbaranes = new ArrayList<>();
        while (cursor.moveToNext()){

            CabeceraAlbaranes cabeceraAlbaranes = new CabeceraAlbaranes();
            cabeceraAlbaranes.setCodigoAlbaran(cursor.getString(cursor.getColumnIndex(DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN)));
            cabeceraAlbaranes.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CabeceraAlbaranesColumnas.FECHA)));
            cabeceraAlbaranes.setNombreCliente(cursor.getString(cursor.getColumnIndex("nombre")));
            listaAlbaranes.add(cabeceraAlbaranes);
        }

        return listaAlbaranes;
    }


    public CabeceraAlbaranes obtenerCabeceraAlbaran(String codigoAlbaran){
        leer();

        String sql = "SELECT cabecera_albaranes.codigoAlbaran, " +
                "cabecera_albaranes.fecha, clientes.nombre, " +
                "cabecera_albaranes.idCliente, " +
                "clientes.direccion, " +
                "clientes.email " +
                "FROM clientes INNER JOIN cabecera_albaranes " +
                "ON clientes.id = cabecera_albaranes.idCliente " +
                "WHERE cabecera_albaranes.codigoAlbaran =?";
        String[] selectionArgs = {codigoAlbaran};

        Cursor c = db.rawQuery(sql,selectionArgs);

        CabeceraAlbaranes albaran = new CabeceraAlbaranes();

        c.moveToFirst();

        albaran.setCodigoAlbaran(c.getString(c.getColumnIndex("codigoAlbaran")));
        albaran.setFecha(c.getString(c.getColumnIndex("fecha")));
        albaran.setNombreCliente(c.getString(c.getColumnIndex("nombre")));
        albaran.setIdCliente(c.getInt(c.getColumnIndex("idCliente")));
        albaran.setDireccionCliente(c.getString(c.getColumnIndex("direccion")));
        albaran.setEmailCliente(c.getString(c.getColumnIndex("email")));


        return albaran;

    }


}
