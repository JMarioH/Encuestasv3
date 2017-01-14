package com.popgroup.encuestasv3.Model;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * Created by jesus.hernandez on 20/12/16.
 * modelo base respuestas del cuestionario
 */
@DatabaseTable
public class RespuestasCuestionario {

    public static final String ID="id";
    public static final String IDENCUESTA = "idEncuesta";
    public static final String FECHA = "fecha";
    public static final String IDTIENDA = "idTienda";
    public static final String IDESTABLECIMIENTO = "idEstablecimiento";
    public static final String IDPREGUNTA = "idPregunta";
    public static final String IDRESPUESTA = "idRespuesta";
    public static final String RESPUESTALIBRE = "respuestaLibre";
    public static final String IDARCHIVO = "idArchivo";
    public static final String FLAG = "flag";

    @DatabaseField(generatedId = true  , index = true ,columnName = ID)
    private int id;
    @DatabaseField(columnName = IDENCUESTA)
    private int idEncuesta;
    @DatabaseField(columnName = FECHA)
    private String fecha;
    @DatabaseField(columnName = IDTIENDA)
    private String  idTienda;
    @DatabaseField(columnName = IDESTABLECIMIENTO)
    private String idEstablecimiento;
    @DatabaseField(columnName = IDPREGUNTA)
    private String idPregunta;
    @DatabaseField(columnName = IDRESPUESTA)
    private String idRespuesta;
    @DatabaseField(columnName = RESPUESTALIBRE)
    private Boolean respuestLibre;
    @DatabaseField(columnName = IDARCHIVO)
    private String idArchivo;
    @DatabaseField(columnName = FLAG)
    private Boolean flag;

    public RespuestasCuestionario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public String getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(String idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(String idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(String idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public Boolean getRespuestLibre() {
        return respuestLibre;
    }

    public void setRespuestLibre(Boolean respuestLibre) {
        this.respuestLibre = respuestLibre;
    }

    public String getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(String idArchivo) {
        this.idArchivo = idArchivo;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
