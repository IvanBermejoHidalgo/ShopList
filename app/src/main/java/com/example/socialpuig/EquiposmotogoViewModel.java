package com.example.socialpuig;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class EquiposmotogoViewModel extends AndroidViewModel {
    EquiposmotogpRepositorio equiposmotogpRepositorio;

    MutableLiveData<List<Equiposmotogp>> listequiposmotogpMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Equiposmotogp> equiposmotogpSeleccionado = new MutableLiveData<>();

    public EquiposmotogoViewModel(@NonNull Application application) {
        super(application);

        equiposmotogpRepositorio = new EquiposmotogpRepositorio();

        listequiposmotogpMutableLiveData.setValue(equiposmotogpRepositorio.obtener());
    }

    MutableLiveData<List<Equiposmotogp>> obtener(){
        return listequiposmotogpMutableLiveData;
    }

    void insertar(Equiposmotogp equiposmotogp){
        equiposmotogpRepositorio.insertar(equiposmotogp, new EquiposmotogpRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Equiposmotogp> equiposmotogps) {
                listequiposmotogpMutableLiveData.setValue(equiposmotogps);
            }
        });
    }

    void eliminar(Equiposmotogp equiposmotogp){
        equiposmotogpRepositorio.eliminar(equiposmotogp, new EquiposmotogpRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Equiposmotogp> equiposmotogps) {
                listequiposmotogpMutableLiveData.setValue(equiposmotogps);
            }
        });
    }

    void seleccionar(Equiposmotogp equiposmotogp){
        equiposmotogpSeleccionado.setValue(equiposmotogp);
    }

    MutableLiveData<Equiposmotogp> seleccionado(){
        return equiposmotogpSeleccionado;
    }
}