package at.ums.luna.umslfs.modelos;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luna-aleixos on 30.05.2016.
 */
public class CabeceraAlbaranes {

    private int id;
    private String fecha;
    private int idCliente;
    private String idTrabajador;
    private String imagen;
    private String firma;
    private String codigoAlbaran;

    private String nombreCliente;
    private String direccionCliente;
    private String emailCliente;

    public CabeceraAlbaranes(int id, String fecha, int idCliente, String idTrabajador, String imagen,
                         String firma, String codigoAlbaran, String nombreCliente,
                         String direccionCliente, String emailCliente){
        this.id = id;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idTrabajador = idTrabajador;
        this.imagen = imagen;
        this.firma = firma;
        this.codigoAlbaran = codigoAlbaran;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.emailCliente = emailCliente;
    }

    public  CabeceraAlbaranes(){

    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {

        this.fecha = fecha;
    }

    public String getFechaFormateada() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyy-MM-dd");
        String fechaFormateada;


        try {
            Date date = fmt.parse(fecha);

            SimpleDateFormat fmtOut = new SimpleDateFormat("dd.MM.yyyy");
            fechaFormateada = fmtOut.format(date);

        } catch (ParseException e) {
            fechaFormateada = "Error de conversion";
        }
        return fechaFormateada;

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
