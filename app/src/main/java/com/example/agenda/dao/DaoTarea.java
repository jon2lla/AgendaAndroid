package com.example.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agenda.modelo.Tarea;
import com.example.agenda.modelo.Usuario;

import java.util.ArrayList;

public class DaoTarea {
    Usuario u;
    Context c;
    Tarea t;
    ArrayList<Tarea> lista;
    SQLiteDatabase sql;
    String bd = "BDUsuarios";
    String tabla = "create table if not exists tarea(id integer primary key autoincrement, nom_tarea text, descripcion text, fecha text, coste decimal, prioridad integer, realizada integer, id_usuario integer)";

    public DaoTarea(Context c, Usuario u){
        this.u = u;
        this.c = c;
        sql = c.openOrCreateDatabase(bd, c.MODE_PRIVATE, null);
        sql.execSQL(tabla);
    }

    public boolean insertTarea(Tarea t){
        Log.i("TAREA2", t.toString());

        ContentValues cv = new ContentValues();
        cv.put("nom_tarea", t.getNomTarea());
        cv.put("descripcion", t.getDescripcion());
        cv.put("fecha", t.getFecha());
        cv.put("coste", t.getCoste());
        cv.put("prioridad", t.getPrioridad());
        cv.put("realizada", t.getRealizada());
        cv.put("id_usuario", u.getId());

        return (sql.insert("tarea", null, cv) > 0);

    }

    public boolean deleteTarea(Tarea t){
        return (sql.delete("tarea", "id=" + t.getId(), null) > 0);
    }

    public boolean deleteAllTarea(Usuario u){
        return (sql.delete("tarea", "id_usuario=" + u.getId(), null) > 0);
    }

    public boolean updateTarea(Tarea t){
        if(getTareaById(t.getId()) != null){
            ContentValues cv = new ContentValues();
            cv.put("nom_tarea", t.getNomTarea());
            cv.put("descripcion", t.getDescripcion());
            cv.put("fecha", t.getFecha());
            cv.put("coste", t.getCoste());
            cv.put("prioridad", t.getPrioridad());
            cv.put("realizada", t.getRealizada());
            return (sql.update("tarea", cv,  "id=" + t.getId() + " and id_usuario=" + u.getId(), null) > 0);
        }
        else{
            return false;
        }
    }

    public int buscar(String t){
        int x = 0;
        lista = selectTareas();

        for(Tarea t2 : lista){
            if(t2.getNomTarea().equalsIgnoreCase(t)){
                x++;
            }
        }
        return x;
    }

    public ArrayList<Tarea> selectTareas(){
        lista = new ArrayList<Tarea>();
        lista.clear();

        Cursor cr = sql.rawQuery("select * from tarea where id_usuario=" + u.getId(), null);

        if (cr != null && cr.moveToFirst()){
            do{
                Tarea t2 = new Tarea();
                t2.setId(cr.getInt(0));
                t2.setNomTarea(cr.getString(1));
                t2.setDescripcion(cr.getString(2));
                t2.setFecha(cr.getString(3));
                t2.setCoste(cr.getFloat(4));
                t2.setPrioridad(cr.getInt(5));
                t2.setRealizada(cr.getInt(6));
                lista.add(t2);
            }while(cr.moveToNext());
        }

        return lista;
    }

    public Tarea getTareaByPosition(int position){
        lista = selectTareas();
        return lista.get(position);
    }

    public Tarea getTareaById(int id){
        lista = selectTareas();

        for (Tarea t2 : lista){
            if(t2.getId() == id){
                return t2;
            }
        }
        return null;
    }
}
