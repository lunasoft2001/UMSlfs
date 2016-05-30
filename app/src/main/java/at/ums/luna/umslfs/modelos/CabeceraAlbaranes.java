package at.ums.luna.umslfs.modelos;

import java.util.Date;

/**
 * Created by luna-aleixos on 30.05.2016.
 */
public class CabeceraAlbaranes {

    private int id;
    private Date fecha;
    private int idCliente;
    private String idTrabajador;
    private String imagen;
    private String firma;
    private String codigoAlbaran;

    public CabeceraAlbaranes(int id, Date fecha, int idCliente, String idTrabajador, String imagen,
                         String firma, String codigoAlbaran){
    this.id = id;
    this.fecha = fecha;
    this.idCliente = idCliente;
    this.idTrabajador = idTrabajador;
    this.imagen = imagen;
    this.firma = firma;
    this.codigoAlbaran = codigoAlbaran;
    }

    public  CabeceraAlbaranes(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getCodigoAlbaran() {
        return codigoAlbaran;
    }

    public void setCodigoAlbaran(String codigoAlbaran) {
        this.codigoAlbaran = codigoAlbaran;
    }
}
