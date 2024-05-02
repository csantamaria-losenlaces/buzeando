package com.carlossantamaria.buzeando.objects;

public class User {

    String id_usr;
    String nombre;
    String apellidos;
    String dir;
    String cod_postal;
    String movil;
    String mail;
    String hash_pwd;

    public User(String id_usr, String nombre, String apellidos, String dir,
                String cod_postal, String movil, String mail, String hash_pwd) {
        this.id_usr = id_usr;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dir = dir;
        this.cod_postal = cod_postal;
        this.movil = movil;
        this.mail = mail;
        this.hash_pwd = hash_pwd;
    }

    public String getId_usr() {
        return id_usr;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDir() {
        return dir;
    }

    public String getCod_postal() {
        return cod_postal;
    }

    public String getMovil() {
        return movil;
    }

    public String getMail() {
        return mail;
    }

    public String getHash_pwd() {
        return hash_pwd;
    }

}