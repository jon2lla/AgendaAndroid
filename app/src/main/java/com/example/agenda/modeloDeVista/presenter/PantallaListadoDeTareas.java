package com.example.agenda.modeloDeVista.presenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.agenda.R;
import com.example.agenda.dao.DaoTarea;
import com.example.agenda.dao.DaoUsuario;
import com.example.agenda.modeloDeVista.ReproductorSonidos;
import com.example.agenda.modeloDeVista.dialogos.DialogoAcercaDe;
import com.example.agenda.modeloDeVista.dialogos.DialogoBorrar;
import com.example.agenda.modeloDeVista.dialogos.DialogoBorrarInterface;
import com.example.agenda.modelo.manejoDeDatos.Encriptador;
import com.example.agenda.modelo.Tarea;
import com.example.agenda.modelo.Usuario;
import com.example.agenda.modeloDeVista.recyclerView.AdaptadorContenedor;
import com.example.agenda.modeloDeVista.recyclerView.ContenedorTarea;
import com.example.agenda.modeloDeVista.recyclerView2.PantallaListado;
import com.example.agenda.modeloDeVista.recyclerView2.RVClickInterface;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class PantallaListadoDeTareas extends AppCompatActivity implements DialogoBorrarInterface, View.OnClickListener, View.OnTouchListener {

    private ArrayList<ContenedorTarea> tareasArchivadas;
    private ArrayList<ContenedorTarea> listaContTareas;
    private ArrayList<ContenedorTarea> listaSeleccionados;
    private RecyclerView rv;
    private AdaptadorContenedor rva;
    private RecyclerView.LayoutManager lm;

    private Button btnTodas, btnHechas, btnPendientes;
    private Usuario usuario;
    private Tarea tarea;
    private DaoUsuario daoU;
    private DaoTarea daoT;
    private ArrayList<Tarea> listaTareas;
    private int posicionActual;

    private boolean selecMultiple = false;
    private boolean selecBorrar = false;
    private SharedPreferences prefs;
    int posicionBorrado;
    boolean rvCreado = false;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_listado_de_tareas);
        prefs = Encriptador.getSPEncriptado(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        Log.i("PREFERENCES LISTARU", prefs.getString("usuario", "Default"));
        Log.i("PREFERENCES LISTARP", prefs.getString("passwd", "Default"));
        recibirParametros();
        setearBotones();

        listaContTareas = new ArrayList<>();
        listaSeleccionados = new ArrayList<>();

        crearListaCont();
        crearRecyclerView();

        final SwipeRefreshLayout swipeL = findViewById(R.id.swipeLayout);
        swipeL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rva.notifyDataSetChanged();
                swipeL.setRefreshing(false);
            }
        });
    }

    public boolean recibirParametros(){
        daoU = new DaoUsuario(this);

        //Recogemos los parametros recibidos de la activity anterior
        Bundle extras = getIntent().getExtras();
        usuario = daoU.getUsuarioById(extras.getInt("idUsuario"));

        daoT = new DaoTarea(this, usuario);
        listaTareas = daoT.selectTareas();
        return true;
    }


    //ASIGNACION DE BOTONES


    public void setearBotones(){

        btnTodas = findViewById(R.id.btnTodas);
        btnTodas.setOnTouchListener(this);
        btnTodas.setPressed(true);
        btnHechas = findViewById(R.id.btnHechas);
        btnHechas.setOnTouchListener(this);
        btnPendientes = findViewById(R.id.btnPendientes);
        btnPendientes.setOnTouchListener(this);
        ImageButton btnAniadirTarea = findViewById(R.id.btnAniadir);
        btnAniadirTarea.setOnClickListener(this);


    }


    public boolean removeItem(int position){
        Tarea t;
        Log.i("POSICION TAREA BORRADA", String.valueOf(position));
        t = listaContTareas.get(position).getTarea();
        listaContTareas.remove(position);


        Log.i("TAREA BORRADA", t.toString());
        if(daoT.deleteTarea(t) && !selecMultiple){
            Toast.makeText(this,"Tarea borrada con exito", Toast.LENGTH_SHORT).show();
        }
        rva.notifyItemRemoved(position);
        return true;
    }

    public boolean modoSeleccion(){
        for(ContenedorTarea ct : listaContTareas){
            if(!selecMultiple){
                ct.modoSelecMultiple();

            }
            else{
                ct.setIcoBorrar();
            }
            rva.notifyDataSetChanged();
        }
        if(!selecMultiple){
            selecMultiple = true;
        }
        else{
            selecMultiple = false;
        }

        return true;
    }

    public boolean seleccionar(int position){
        ContenedorTarea ct = listaContTareas.get(position);
        if(!ct.isSeleccionado()){
            ct.modoSeleccionado();
        }
        else{
            ct.modoSelecMultiple();
        }
        rva.notifyItemChanged(position);
        return true;
    }

    private boolean crearListaCont() {
        listaContTareas.clear();
        listaTareas = daoT.selectTareas();
        for(Tarea t : listaTareas) {
            if (btnTodas.isPressed() || (btnHechas.isPressed() && t.getRealizada() > 0) || (btnPendientes.isPressed() && t.getRealizada() == 0)) {
                ContenedorTarea ct = new ContenedorTarea(t);
                listaContTareas.add(ct);
            }
        }

        return true;
    }

    public boolean crearListaSeleccionados(){
        listaSeleccionados.clear();
        for(ContenedorTarea ct : listaContTareas){
            if(ct.getModoIcono() == 3){
                listaSeleccionados.add(ct);
            }
        }
        return true;
    }

    public boolean borrarSeleccionados(){
        for(ContenedorTarea ct : listaSeleccionados){
            crearListaCont();
            crearListaSeleccionados();
            if(ct.getModoIcono() == 3){
                removeItem(listaContTareas.indexOf(ct));
            }
            crearListaCont();
            crearListaSeleccionados();

            rva.notifyDataSetChanged();

        }
        return true;
    }


    //METODO PARA AÑADIR TAREAS DE PRUEBA

    private void addTarea(){
        Tarea t = new Tarea("Cumple Herranz", "Mandarle WhatsApp", "18/11/2020", Float.parseFloat("0"), 4, 0);
        daoT.insertTarea(t);
        t = new Tarea("Cambiar ruedas al golf", "Hablar con Baskonia", "17/04/2020", Float.parseFloat("200"), 1, 0);
        daoT.insertTarea(t);
        t = new Tarea("Salir a cenar", "Cuando acabe el puto confinamiento", "17/04/2020", Float.parseFloat("50"), 2, 0);
        daoT.insertTarea(t);
        t = new Tarea("Llamar a Abu", "Tiene mi pendrive", "17/04/2020", Float.parseFloat("0"), 3, 0);
        daoT.insertTarea(t);
        t = new Tarea("Comprar tabaco", "Cuando salga de clase", "17/04/2020", Float.parseFloat("5.15"), 4, 0);
        daoT.insertTarea(t);
    }


    //RECYCLERVIEW



    private boolean crearRecyclerView() {
        rv = findViewById(R.id.recyclerView);
//        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rva = new AdaptadorContenedor(listaContTareas);

        for(ContenedorTarea ct : listaContTareas){
            Log.i("TAREAS EN CONT", ct.getTarea().toString());
         }

        DividerItemDecoration did = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(did);

        rv.setLayoutManager(lm);
        rv.setAdapter(rva);


        registerForContextMenu(rv);

        ItemTouchHelper ith = new ItemTouchHelper(scb);
        ith.attachToRecyclerView(rv);
        //LISTENERS


        //Accion: Mantener pulsado un contenedor
        rva.setOnLongItemClickListener(new AdaptadorContenedor.onLongItemClickListener() {
            @Override
            public void ItemLongClicked(View v, int position) {
                posicionActual = position;
                ImageView iv = findViewById(R.id.icoPrio);
                iv.showContextMenu();
            }
        });

        //Accion: Hacer click en un contenedor
        rva.setOnItemClickListener(new AdaptadorContenedor.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {

                posicionActual = position;

                if(selecBorrar){
                    ContenedorTarea ct = listaContTareas.get(posicionBorrado);
                    ct.modoSeleccionado();
                    ct.setIcoBorrar();
                    selecBorrar = false;
                    rva.notifyItemChanged(posicionBorrado);
                }
                else{
                    if(selecMultiple) {
                        Log.i("TAREA INDEX OF:", String.valueOf(position));

                        seleccionar(position);
                    }
                    else{
                        ContenedorTarea ct = listaContTareas.get(position);
                        tarea = ct.getTarea();
                        lanzarPantallaListarTarea(null);
                        Log.i("TAREA INDEX OF:", String.valueOf(position));
                    }
                }
            }


            //Accion: Hacer click en el boton de la basura
            @Override
            public void onDeleteClick(int position) {
                posicionActual = position;
                ContenedorTarea ct = listaContTareas.get(position);
                if(selecMultiple){
                    seleccionar(position);
                }
                else if(ct.getModoIcono() != 4){
                    ContenedorTarea ct2 = listaContTareas.get(posicionBorrado);
                    ct2.modoSeleccionado();
                    ct2.setIcoBorrar();
                    rva.notifyItemChanged(posicionBorrado);
                    posicionBorrado = position;
                    selecBorrar = true;
                    ct.modoBorrado();
                    rva.notifyItemChanged(position);
                }
                else {
                    removeItem(position);
                    reproductor(1);
                    start();
                }
            }
        });
        return true;
    }

    //Creamos un simplecallback para gestionar el uso de los gestos
    ContenedorTarea tareaBorrada = null;

    //Le decimos al scb que gestos va a gestionar
    ItemTouchHelper.SimpleCallback scb = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT) {
        @Override

        //Al arrastrar y soltar un contenedor tendremos que notificar al RV del cambio de posiciones
        public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(listaContTareas, fromPosition, toPosition);

            rv.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }


        //Al swipear un contenedor hacia a la izquierda borraremos la tarea, produciremos un sonido y mostraremos el snackbar para deshacer
        //Añadiremos la tarea recien eliminada en caso de que el usuario haga click en deshacer
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();
            posicionActual = position;
            switch (direction){
                case ItemTouchHelper.LEFT:
                    tareaBorrada = listaContTareas.get(position);
                    removeItem(position);
                    reproductor(1);
                    start();
                    Snackbar.make(rv, tareaBorrada.getTarea().getNomTarea(), Snackbar.LENGTH_SHORT).setAction("Deshacer", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listaContTareas.add(position, tareaBorrada);
                            daoT.insertTarea(tareaBorrada.getTarea());
                            rva.notifyItemInserted(position);
                        }
                    }).show();
                    break;

            //Gestiona el swipeo hacia la derecha, para archivar tareas
            //   AUN SIN IMPLEMENTAR

               /*case ItemTouchHelper.RIGHT:
                    final ContenedorTarea ct = listaContTareas.get(position);
                    tareasArchivadas.add(ct);

                    listaContTareas.remove(position);
                    rva.notifyItemRemoved(position);

                    Snackbar.make(rv, ct.getTarea().getNomTarea() + ", Archivada", Snackbar.LENGTH_SHORT).setAction("Deshacer", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tareasArchivadas.remove(tareasArchivadas.lastIndexOf(ct));
                            listaContTareas.add(position, ct);
                            rva.notifyItemInserted(position);
                        }
                    }).show();
                    break;*/
            }
        }

        //Gestiona las animaciones a la hora de swipear
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(PantallaListadoDeTareas.this, c,rv, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(PantallaListadoDeTareas.this, R.color.delete_color))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
//                  .addSwipeRightBackgroundColor(ContextCompat.getColor(PantallaListadoDeTareas.this, R.color.delete_color))
//                  .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .create()
                    .decorate();


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAniadir:
                addTarea();
                crearListaCont();
                rva.notifyDataSetChanged();
                break;
        }
    }

    //Gestiona la visualizacion de los tres grupos de tareas
    //TODAS - HECHAS - PENDIENTES
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){

            case R.id.btnTodas:
                btnTodas.setPressed(true);
                btnHechas.setPressed(false);
                btnPendientes.setPressed(false);
                crearListaCont();
                rva.notifyDataSetChanged();
                break;
            case R.id.btnHechas:
                btnTodas.setPressed(false);
                btnHechas.setPressed(true);
                btnPendientes.setPressed(false);
                crearListaCont();
                rva.notifyDataSetChanged();
                break;
            case R.id.btnPendientes:
                btnTodas.setPressed(false);
                btnHechas.setPressed(false);
                btnPendientes.setPressed(true);
                crearListaCont();
                rva.notifyDataSetChanged();
                break;
        }
        return true;
    }


    //CONTEXT MENU


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opciones_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcionCntxt1:
                this.tarea = daoT.getTareaByPosition(posicionActual);
                lanzarPantallaModificarTarea(null);
                break;
            case R.id.opcionCntxt2:
                removeItem(posicionActual);
                break;

        }
        btnTodas.setPressed(true);
        return true;
    }



    //ACTION BAR



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnAcercaDe:
                mostrarDialogoAcercaDe(null);
                break;

            case R.id.btnCambiarPasswd:
                lanzarPantallaModificarPasswd(null);
                break;

            case R.id.btnCrearTarea:
                lanzarPantallaCrearTarea(null);
                break;
            case R.id.btnEnviarCorreo:
                enviarCorreo();
                break;
            case R.id.btnCerrarSesion:
                cerrarSesion();
                break;
            case R.id.borrar_tareas:
                mostrarDialogoBorrar(null);
                break;

            case R.id.btnMultiselect:
                modoSeleccion();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    public boolean enviarCorreo(){
        ArrayList<Tarea> listaTareas = new ArrayList<Tarea>();
        for (ContenedorTarea ct : listaContTareas){

            listaTareas.add(ct.getTarea());

        }
        Intent i = new Intent(Intent.ACTION_SEND);

        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.txtAsunto));
        String str = "";
        for(Tarea t : listaTareas){
            str = str + t.toStringFormateado(this) + " \n";

        }
        i.putExtra(Intent.EXTRA_TEXT, str);
//        i.setType("vnd.android.cursor.dir/email");
        i.setType("message/rfc882");

        startActivity(i.createChooser(i, "Elige un correo"));
        return true;
    }

    public boolean cerrarSesion(){
        SharedPreferences prefs = Encriptador.getSPEncriptado(this);

        SharedPreferences.Editor editor = prefs.edit();

        Log.i("PREFERENCES CERRARU", prefs.getString("usuario", "Default"));
        Log.i("PREFERENCES CERRARP", prefs.getString("passwd", "Default"));

        editor.remove("usuario");
        editor.remove("passwd");

        Log.i("PREFERENCES CERRARU2", prefs.getString("usuario", "Default"));
        Log.i("PREFERENCES CERRARP2", prefs.getString("passwd", "Default"));
        editor.putBoolean("recordar", false);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), PantallaLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return true;
    }



    //MOSTRAR DIALOGOS



    public void mostrarDialogoBorrar(View oView) {
        DialogoBorrar dialogoBorrar = new DialogoBorrar();
        dialogoBorrar.show(getSupportFragmentManager(), "Dialogo borrar");
    }

    public void mostrarDialogoAcercaDe(View oView) {
        DialogoAcercaDe dialogoAcercaDe = new DialogoAcercaDe();
        dialogoAcercaDe.show(getSupportFragmentManager(), "Dialogo AcercaDe");
    }

    @Override
    public void onRespuesta(String psRespuesta, int borrar) {
        if(selecMultiple && borrar == 1) {
            borrarSeleccionados();
            modoSeleccion();

        }
        else if (!selecMultiple && borrar == 1) {
            daoT.deleteAllTarea(usuario);
            reproductor(2);
            start();

        }
        else{
            modoSeleccion();
        }
        btnTodas.setPressed(true);
        crearListaCont();
        rva.notifyDataSetChanged();
        Toast.makeText( this, psRespuesta, Toast.LENGTH_SHORT).show();
    }

    //REPRODUCTOR DE SONIDOS
    public boolean reproductor(int tipo){
        switch (tipo){
            case 1:
                mp = MediaPlayer.create(this, R.raw.can_open_2);
                break;
            case 2:
                mp = MediaPlayer.create(this, R.raw.paper_crumpled_2);
                break;
        }
        return true;
    }

    public boolean start(){
        mp.start();
        return true;
    }

    public boolean stop(){
        mp.stop();
        return true;
    }

    //CAMBIO DE ACTIVITIES



    public void lanzarPantallaCrearTarea(View view){
        Intent i = new Intent(this, PantallaCrearTarea.class);
        i.putExtra("idUsuario", usuario.getId());
        i.putExtra("tipoActividad", 1);
        startActivity(i);
        finish();
    }

    public void lanzarPantallaListarTarea(View view){
        Intent i = new Intent(this, PantallaCrearTarea.class);
        i.putExtra("idUsuario", usuario.getId());
        i.putExtra("idTarea", tarea.getId());
        i.putExtra("tipoActividad", 2);
        startActivity(i);
        finish();
    }

    public void lanzarPantallaModificarTarea(View view){
        Intent i = new Intent(this, PantallaCrearTarea.class);
        i.putExtra("idUsuario", usuario.getId());
        i.putExtra("idTarea", tarea.getId());
        i.putExtra("tipoActividad", 3);
        startActivity(i);
        finish();
    }
    public void lanzarPantallaModificarPasswd(View view){
        Intent i2 = new Intent(this, PantallaRegistrarUsuario.class);
        i2.putExtra("idUsuario", usuario.getId());
        i2.putExtra("tipoActividad", 2);
        startActivity(i2);
        finish();
    }

};


