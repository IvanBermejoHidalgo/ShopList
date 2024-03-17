package com.example.socialpuig;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

public class NoticiasmotogpViewModel extends AndroidViewModel{
    NoticiasmotogpRepositorio noticiasmotogpRepositorio;

    MutableLiveData<List<Noticiasmotogp>> listNoticiasmotogpMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Noticiasmotogp> noticiasmotogpSeleccionado = new MutableLiveData<>();

    public NoticiasmotogpViewModel(@NonNull Application application) {
        super(application);

        noticiasmotogpRepositorio = new NoticiasmotogpRepositorio();

        listNoticiasmotogpMutableLiveData.setValue(noticiasmotogpRepositorio.obtener());
    }

    MutableLiveData<List<Noticiasmotogp>> obtener(){
        return listNoticiasmotogpMutableLiveData;
    }

    void insertar(Noticiasmotogp noticiasmotogp){
        noticiasmotogpRepositorio.insertar(noticiasmotogp, new NoticiasmotogpRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Noticiasmotogp> noticiasmotogp) {
                listNoticiasmotogpMutableLiveData.setValue(noticiasmotogp);
            }
        });
    }

    void eliminar(Noticiasmotogp noticiasmotogp){
        noticiasmotogpRepositorio.eliminar(noticiasmotogp, new NoticiasmotogpRepositorio.Callback() {
            @Override
            public void cuandoFinalice(List<Noticiasmotogp> noticiasmotogp) {
                listNoticiasmotogpMutableLiveData.setValue(noticiasmotogp);
            }
        });
    }

    void seleccionar(Noticiasmotogp noticiasmotogp){
        noticiasmotogpSeleccionado.setValue(noticiasmotogp);
    }

    MutableLiveData<Noticiasmotogp> seleccionado(){
        return noticiasmotogpSeleccionado;
    }
}
