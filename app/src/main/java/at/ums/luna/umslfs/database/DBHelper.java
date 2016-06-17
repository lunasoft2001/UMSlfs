package at.ums.luna.umslfs.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by luna-aleixos on 24.05.2016.
 */
    //TODO Escribir correctamente las llaves de la dB
    //TODO importar los datos de los clientes y borrar el primer albaran

public class DBHelper extends SQLiteAssetHelper{
    private static int version = 1;
    private static String nombreDb = "ums_movil1.db";

    public DBHelper(Context contexto){
        super(contexto,nombreDb,null,version);
    }

    /**
     * Interfaces de las tablas para facilitar su entrada
     */

    public interface Tablas{
        String CABECERA_ALBARANES = "cabecera_albaranes";
        String DETALLE_ALBARANES = "detalle_albaranes";
        String CLIENTES = "clientes";
        String TRABAJADOR = "trabajador";
    }

    public interface CabeceraAlbaranesColumnas {
        String ID = "id";
        String FECHA = "fecha";
        String ID_CLIENTE = "idCliente";
        String ID_TRABAJADOR = "idTrabajador";
        String CODIGO_ALBARAN = "codigoAlbaran";
    }

    public interface DetalleAlbarenesColumnas{
        String CODIGO_ALBARAN = "codigoAlbaran";
        String LINEA = "linea";
        String DETALLE = "detalle";
        String CANTIDAD = "cantidad";
        String TIPO = "tipo";
    }

    public interface Clientes{
        String ID = "id";
        String NOMBRE = "nombre";
        String DIRECCION = "direccion";
        String TELEFONO = "telefono";
        String EMAIL = "email";
    }

    public interface Trabajadores {
        String CODIGO_INTERNO = "_id";
        String ID = "id";
        String NOMBRE = "nombre";
    }

}
