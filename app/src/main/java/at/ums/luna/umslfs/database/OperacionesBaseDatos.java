package at.ums.luna.umslfs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import at.ums.luna.umslfs.database.DBHelper.Trabajadores;
import at.ums.luna.umslfs.modelos.Trabajador;

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
}
