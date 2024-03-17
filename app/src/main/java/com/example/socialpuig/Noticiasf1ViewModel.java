package com.example.socialpuig;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

public class Noticiasf1ViewModel extends AndroidViewModel{
    Noticiasf1Repositorio noticiasf1Repositorio;

    MutableLiveData<List<Noticiasf1>> listNoticiasf1MutableLiveData = new MutableLiveData<>();

    MutableLiveData<Noticiasf1> noticiasf1Seleccionado = new MutableLiveData<>();

    public Noticiasf1ViewModel(@NonNull Application application) {
        super(application);

        noticiasf1Repositorio = new Noticiasf1Repositorio();

        listNoticiasf1MutableLiveData.setValue(noticiasf1Repositorio.obtener());
    }

    MutableLiveData<List<Noticiasf1>> obtener(){
        return listNoticiasf1MutableLiveData;
    }

    void insertar(Noticiasf1 noticiasf1){
        noticiasf1Repositorio.insertar(noticiasf1, new Noticiasf1Repositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Noticiasf1> noticiasf1) {
                listNoticiasf1MutableLiveData.setValue(noticiasf1);
            }
        });
    }

    void eliminar(Noticiasf1 noticiasf1){
        noticiasf1Repositorio.eliminar(noticiasf1, new Noticiasf1Repositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Noticiasf1> noticiasf1) {
                listNoticiasf1MutableLiveData.setValue(noticiasf1);
            }
        });
    }

    void seleccionar(Noticiasf1 noticiasf1){
        noticiasf1Seleccionado.setValue(noticiasf1);
    }

    MutableLiveData<Noticiasf1> seleccionado(){
        return noticiasf1Seleccionado;
    }
}
