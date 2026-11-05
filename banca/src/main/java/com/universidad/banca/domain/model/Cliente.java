package com.universidad.banca.domain.model;

public class Cliente {
    
    private Long id;
    private String usuario;
    private String clave;
    private String correo;
    private String telofono;
    private String direccion;

    public Cliente(Long id, String usuario, String clave, String correo, String telofono, String direccion) {
        this.id = id;
        this.usuario = usuario;
        this.clave = clave;
        this.correo = correo;
        this.telofono = telofono;
        this.direccion = direccion;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getTelofono() {
        return telofono;
    }
    public void setTelofono(String telofono) {
        this.telofono = telofono;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
