package com.example.socialpuig;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PilotosmotogpViewModel extends AndroidViewModel {
    PilotosmotogpRepositorio pilotosmotogpRepositorio;

    MutableLiveData<List<PilotosMotogp>> listpilotosmotosMutableLiveData = new MutableLiveData<>();

    MutableLiveData<PilotosMotogp> pilotosmotogpSeleccionado = new MutableLiveData<>();

    public PilotosmotogpViewModel(@NonNull Application application) {
        super(application);

        pilotosmotogpRepositorio = new PilotosmotogpRepositorio();

        listpilotosmotosMutableLiveData.setValue(pilotosmotogpRepositorio.obtener());
    }

    MutableLiveData<List<PilotosMotogp>> obtener(){
        return listpilotosmotosMutableLiveData;
    }

    void insertar(PilotosMotogp pilotosMotogp){
        pilotosmotogpRepositorio.insertar(pilotosMotogp, new PilotosmotogpRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<PilotosMotogp> pilotosMotogps) {
                listpilotosmotosMutableLiveData.setValue(pilotosMotogps);
            }
        });
    }

    void eliminar(PilotosMotogp pilotosMotogp){
        pilotosmotogpRepositorio.eliminar(pilotosMotogp, new PilotosmotogpRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<PilotosMotogp> pilotosMotogps) {
                listpilotosmotosMutableLiveData.setValue(pilotosMotogps);
            }
        });
    }

    void seleccionar(PilotosMotogp pilotosMotogp){
        pilotosmotogpSeleccionado.setValue(pilotosMotogp);
    }

    MutableLiveData<PilotosMotogp> seleccionado(){
        return pilotosmotogpSeleccionado;
    }
}
