package at.ums.luna.umslfs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.database.DBHelper.Trabajadores;
import at.ums.luna.umslfs.modelos.CabeceraAlbaranes;
import at.ums.luna.umslfs.modelos.Clientes;
import at.ums.luna.umslfs.modelos.DetalleAlbaranes;

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
        leer();
        return db.rawQuery("SELECT * FROM " + DBHelper.Tablas.TRABAJADOR, null);
    }

    public void actualizarTrabajador(String idNuevo, String nombreNuevo){
        abrir();

        ContentValues valores = new ContentValues();
        valores.put(Trabajadores.ID, idNuevo);
        valores.put(Trabajadores.NOMBRE, nombreNuevo);

        String whereClause = Trabajadores.CODIGO_INTERNO + " = " + 1;

        db.update(DBHelper.Tablas.TRABAJADOR, valores, whereClause, null);
        cerrar();
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
        leer();
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
        cerrar();

        return listaClientes;
    }

    public List<Clientes> verListaClientesCompletaPorNombre(){
        leer();
        Cursor cursor = db.query(DBHelper.Tablas.CLIENTES,todasColumnasClientes,null,null,null,null,DBHelper.Clientes.NOMBRE);

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
        cerrar();

        return listaClientes;
    }


    /**
     * ALBARANES CABECERA
     */

    public List<CabeceraAlbaranes> verListaAlbaranesCabeceraCompleta(){
        leer();
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
                "ON clientes.id = cabecera_albaranes.idCliente " +
                "ORDER BY codigoAlbaran DESC";

        Cursor cursor = db.rawQuery(querySimple,null,null);

        List<CabeceraAlbaranes> listaAlbaranes = new ArrayList<>();
        while (cursor.moveToNext()){

            CabeceraAlbaranes cabeceraAlbaranes = new CabeceraAlbaranes();
            cabeceraAlbaranes.setCodigoAlbaran(cursor.getString(cursor.getColumnIndex(DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN)));
            cabeceraAlbaranes.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CabeceraAlbaranesColumnas.FECHA)));
            cabeceraAlbaranes.setNombreCliente(cursor.getString(cursor.getColumnIndex("nombre")));
            listaAlbaranes.add(cabeceraAlbaranes);
        }

        cerrar();
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

        cerrar();

        return albaran;

    }

    public int ultimaCabeceraAlbaran(){
        leer();
        String sql = "SELECT Max(id) As idMaximo From cabecera_albaranes";

        Cursor c = db.rawQuery(sql,null);

        c.moveToFirst();

        int ultimoAlbaran = c.getInt(c.getColumnIndex("idMaximo"));

        cerrar();

        return ultimoAlbaran;
    }



    public void nuevaCabeceraAlbaran(int ultimoAlbaran, String idTrabajador){
        abrir();

        int nuevoAlbaran = ultimoAlbaran + 1;
        String nuevoCodigoAlbaran = idTrabajador + String.valueOf(nuevoAlbaran);

        ContentValues valores = new ContentValues();
        valores.put(DBHelper.CabeceraAlbaranesColumnas.ID, nuevoAlbaran);
        valores.put(DBHelper.CabeceraAlbaranesColumnas.ID_TRABAJADOR, idTrabajador);
        valores.put(DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN,nuevoCodigoAlbaran);
        valores.put(DBHelper.CabeceraAlbaranesColumnas.FECHA,obtenerFechaActual());
        valores.put(DBHelper.CabeceraAlbaranesColumnas.ID_CLIENTE,1);

        db.insert(DBHelper.Tablas.CABECERA_ALBARANES,null,valores);



    }

    private String obtenerFechaActual() {

        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd.MM.yyyy");
        return formateador.format(ahora);

    }

    public void actualizarCabeceraAlbaran(String[] idAlbaran, String fecha, int idCliente){
        abrir();
        ContentValues actualizar = new ContentValues();
        actualizar.put(DBHelper.CabeceraAlbaranesColumnas.FECHA, fecha);
        actualizar.put(DBHelper.CabeceraAlbaranesColumnas.ID_CLIENTE, idCliente);
        db.update(DBHelper.Tablas.CABECERA_ALBARANES, actualizar ,
                DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN + "=?", idAlbaran);
    }

    public void eliminarCabeceraAlbaran(String[] idAlbaran, Context context){
        abrir();
        db.delete(DBHelper.Tablas.CABECERA_ALBARANES,
                DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN + "= ?"  , idAlbaran);

        Toast.makeText(context,String.format(context.getString(R.string.albaran_eliminado),idAlbaran ),Toast.LENGTH_LONG).show();

    }



    /*
    DETALLE ALBARANES
     */

    public void nuevoDetalleAlbaran(int ultimaLinea, String codigoAlbaran){
        abrir();

        ultimaLinea++;

        ContentValues valores = new ContentValues();
        valores.put(DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN, codigoAlbaran);
        valores.put(DBHelper.DetalleAlbarenesColumnas.LINEA, ultimaLinea);

        db.insert(DBHelper.Tablas.DETALLE_ALBARANES,null,valores);

        Log.i("JUANJO", "Linea " + ultimaLinea + " creada en el albaran " + codigoAlbaran);
    }

    public int ultimaLineaAlbaran(String codigoAlbaran){
        leer();
        String[] args = {codigoAlbaran};

        String sql = "SELECT Max(linea) As lineaMaxima From detalle_albaranes " +
                "WHERE detalle_albaranes.codigoAlbaran =?";

        Cursor c = db.rawQuery(sql,args);

        c.moveToFirst();

        int ultimoAlbaran = c.getInt(c.getColumnIndex("lineaMaxima"));


        cerrar();

        Log.i("JUANJO", String.valueOf(ultimoAlbaran));

        return ultimoAlbaran;

    }

    public static final String[] todasColumnasDetalleAlbaran = {
            DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN,
            DBHelper.DetalleAlbarenesColumnas.LINEA,
            DBHelper.DetalleAlbarenesColumnas.DETALLE,
            DBHelper.DetalleAlbarenesColumnas.CANTIDAD,
            DBHelper.DetalleAlbarenesColumnas.TIPO
    };

    public List<DetalleAlbaranes> verListaDetalleAlbaran(String codigoAlbaran){
        leer();
        String[] args ={codigoAlbaran};
        String criterioSeleccion = DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN + "=?";


        Cursor cursor = db.query(DBHelper.Tablas.DETALLE_ALBARANES,todasColumnasDetalleAlbaran,criterioSeleccion,args,null,null,DBHelper.DetalleAlbarenesColumnas.LINEA);

        List<DetalleAlbaranes> listaDetalleAlbaranes = new ArrayList<>();
        while (cursor.moveToNext()){
            DetalleAlbaranes detalles = new DetalleAlbaranes();
            detalles.setCodigoAlbaran(cursor.getString(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN)));
            detalles.setLinea(cursor.getInt(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.LINEA)));
            detalles.setDetalle(cursor.getString(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.DETALLE)));
            detalles.setCantidad(cursor.getInt(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.CANTIDAD)));
            detalles.setTipo(cursor.getString(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.TIPO)));
            listaDetalleAlbaranes.add(detalles);
        }
        cerrar();

        return listaDetalleAlbaranes;
    }

}
