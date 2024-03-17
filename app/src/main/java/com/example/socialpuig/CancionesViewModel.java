package com.example.socialpuig;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

public class CancionesViewModel extends AndroidViewModel{
    CancionesRepositorio cancionesRepositorio;

    MutableLiveData<List<Canciones>> listCancionesMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Canciones> cancionesSeleccionado = new MutableLiveData<>();

    public CancionesViewModel(@NonNull Application application) {
        super(application);

        cancionesRepositorio = new CancionesRepositorio();

        listCancionesMutableLiveData.setValue(cancionesRepositorio.obtener());
    }

    MutableLiveData<List<Canciones>> obtener(){
        return listCancionesMutableLiveData;
    }

    void insertar(Canciones canciones){
        cancionesRepositorio.insertar(canciones, new CancionesRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Canciones> canciones) {
                listCancionesMutableLiveData.setValue(canciones);
            }
        });
    }

    void eliminar(Canciones canciones){
        cancionesRepositorio.eliminar(canciones, new CancionesRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Canciones> canciones) {
                listCancionesMutableLiveData.setValue(canciones);
            }
        });
    }

    void seleccionar(Canciones canciones){
        cancionesSeleccionado.setValue(canciones);
    }

    MutableLiveData<Canciones> seleccionado(){
        return cancionesSeleccionado;
    }
}
