package com.example.socialpuig;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class Equiposf1ViewModel extends AndroidViewModel {
    Equiposf1Repositorio equiposf1Repositorio;

    MutableLiveData<List<Equiposf1>> listequiposf1MutableLiveData = new MutableLiveData<>();

    MutableLiveData<Equiposf1> equiposf1Seleccionado = new MutableLiveData<>();

    public Equiposf1ViewModel(@NonNull Application application) {
        super(application);

        equiposf1Repositorio = new Equiposf1Repositorio();

        listequiposf1MutableLiveData.setValue(equiposf1Repositorio.obtener());
    }

    MutableLiveData<List<Equiposf1>> obtener(){
        return listequiposf1MutableLiveData;
    }

    void insertar(Equiposf1 equiposf1){
        equiposf1Repositorio.insertar(equiposf1, new Equiposf1Repositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Equiposf1> equiposf1s) {
                listequiposf1MutableLiveData.setValue(equiposf1s);
            }
        });
    }

    void eliminar(Equiposf1 equiposf1){
        equiposf1Repositorio.eliminar(equiposf1, new Equiposf1Repositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Equiposf1> equiposf1s) {
                listequiposf1MutableLiveData.setValue(equiposf1s);
            }
        });
    }

    void seleccionar(Equiposf1 equiposf1){
        equiposf1Seleccionado.setValue(equiposf1);
    }

    MutableLiveData<Equiposf1> seleccionado(){
        return equiposf1Seleccionado;
    }
}

