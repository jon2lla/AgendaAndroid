package com.example.agenda.modelo;

public class Usuario {

    int id;
    String usuario, passwd;

    public Usuario(){

    }

    public Usuario(String usuario, String passwd) {
        this.usuario = usuario;
        this.passwd = passwd;
    }

    public boolean isNull(){
        if(usuario.equals("") || passwd.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String toString(){
        return "Usuario: " + usuario + " - Id: " + id;
    }

    //GETTERS & SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}
