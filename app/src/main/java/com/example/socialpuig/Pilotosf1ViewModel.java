package com.example.socialpuig;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

public class Pilotosf1ViewModel extends AndroidViewModel{
    Pilotosf1Repositorio pilotosf1Repositorio;

    MutableLiveData<List<Pilotosf1>> listpilotosf1MutableLiveData = new MutableLiveData<>();

    MutableLiveData<Pilotosf1> pilotosf1Seleccionado = new MutableLiveData<>();

    public Pilotosf1ViewModel(@NonNull Application application) {
        super(application);

        pilotosf1Repositorio = new Pilotosf1Repositorio();

        listpilotosf1MutableLiveData.setValue(pilotosf1Repositorio.obtener());
    }

    MutableLiveData<List<Pilotosf1>> obtener(){
        return listpilotosf1MutableLiveData;
    }

    void insertar(Pilotosf1 pilotosf1){
        pilotosf1Repositorio.insertar(pilotosf1, new Pilotosf1Repositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Pilotosf1> pilotosf1) {
                listpilotosf1MutableLiveData.setValue(pilotosf1);
            }
        });
    }

    void eliminar(Pilotosf1 pilotosf1){
        pilotosf1Repositorio.eliminar(pilotosf1, new Pilotosf1Repositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Pilotosf1> pilotosf1) {
                listpilotosf1MutableLiveData.setValue(pilotosf1);
            }
        });
    }

    void seleccionar(Pilotosf1 pilotosf1){
        pilotosf1Seleccionado.setValue(pilotosf1);
    }

    MutableLiveData<Pilotosf1> seleccionado(){
        return pilotosf1Seleccionado;
    }
}
