package com.example.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.agenda.modelo.Usuario;

import java.util.ArrayList;

public class DaoUsuario {

    Context c;
    Usuario u;
    ArrayList<Usuario> lista;
    SQLiteDatabase sql;
    String bd = "BDUsuarios";
    String tabla = "create table if not exists usuario(id integer primary key autoincrement, usuario text, pass text)";

    public DaoUsuario(Context c){
        this.c = c;
        sql = c.openOrCreateDatabase(bd, c.MODE_PRIVATE, null);
        sql.execSQL(tabla);
        u = new Usuario();
    }

    public boolean insertUsuario(Usuario u){
        if(buscar(u.getUsuario()) == 0){
            ContentValues cv = new ContentValues();
            cv.put("usuario", u.getUsuario());
            cv.put("pass", u.getPasswd());

            return (sql.insert("usuario", null, cv) > 0);
        }
        else{
            return false;
        }
    }

    public boolean updatePasswd(Usuario u, String p){
        if(buscar(u.getUsuario()) > 0){
            ContentValues cv = new ContentValues();
            cv.put("pass", p);
            return (sql.update("usuario", cv, "id=" + u.getId(), null) > 0);
        }
        else{
            return false;
        }
    }

    public int login(String u, String p){
        int a = 0;

        Cursor cr = sql.rawQuery("select * from usuario", null);

        if (cr != null && cr.moveToFirst()){
            do{
                if(cr.getString(1).equals(u) && cr.getString(2).equals(p)) {
                    a++;
                }
            }while(cr.moveToNext());
        }
        return a;
    }


    public int buscar(String u){
        int x = 0;
        lista = selectUsuarios();

        for(Usuario u2 : lista){
            if(u2.getUsuario().equalsIgnoreCase(u)){
                x++;
            }
        }
        return x;
    }

    public ArrayList<Usuario> selectUsuarios(){
        lista = new ArrayList<Usuario>();
        lista.clear();

        Cursor cr = sql.rawQuery("select * from usuario", null);

        if (cr != null && cr.moveToFirst()){
            do{
                Usuario u = new Usuario();
                u.setId(cr.getInt(0));
                u.setUsuario(cr.getString(1));
                u.setPasswd(cr.getString(2));
                lista.add(u);
            }while(cr.moveToNext());
        }

        return lista;
    }

    public Usuario getUsuario(String u, String p){
        lista = selectUsuarios();

        for (Usuario u2 : lista){
            if(u2.getUsuario().equals(u) && u2.getPasswd().equals(p)){
                return u2;
            }
        }
        return null;
    }

    public Usuario getUsuarioById(int id){
        lista = selectUsuarios();

        for (Usuario u2 : lista){
            if(u2.getId() == id){
                return u2;
            }
        }
        return null;
    }
}
