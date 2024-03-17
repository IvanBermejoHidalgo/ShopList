package com.example.socialpuig;


import java.util.ArrayList;
import java.util.List;

public class NoticiasmotogpRepositorio {
    List<Noticiasmotogp> noticiasmotogps = new ArrayList<>();

    interface Callback {
        void cuandoFinalice(List<Noticiasmotogp> noticiasmotogps);
    }

    NoticiasmotogpRepositorio(){
        noticiasmotogps.add(new Noticiasmotogp("MARC MÁRQUEZ FICHA POR GRESINI PARA 2024",R.drawable.mmuno, "Marc Márquez ficha por Gresini para 2024. Abandona Honda tras 11 años juntos.", "Marc Márquez tras unas temporadas de lesiones y de no tener una moto para poder pelear del mundial, decide dejar Honda tras 11 años junto a la marca japonesa.","Ficha por Gresini que lleva una Ducati. Pasa de la peor moto de la parrilla a la mejor. Compartirá equipo con su hermano Álex Márquez.",R.drawable.mmdos));
        noticiasmotogps.add(new Noticiasmotogp("LUCHA POR EL CAMPEONATO ENTRE DUCATIS",R.drawable.ducuno, "Jorge Martín y Pecco Bagnaia se juegan el título mundial, los dos con Ducati, una oficial y otra satélite.", "Jorge Martín y Pecco Bagnaia se juegan el título mundial. El español quiere su primer campeonato de MotoGP mientras que el italiano quiere el bicampeonato.","Los dos van con dos Ducati, el español va con una Ducati satélite y el italiano va con una oficial.",R.drawable.ducdos));
        noticiasmotogps.add(new Noticiasmotogp("PEDRO ACOSTA TENDRÁ MOTO EN LA CATEGORÍA REINA",R.drawable.acouno, "Pedro Acosta tendrá moto en el equipo GAS GAS  de MotoGP tras conseguir el título mundial de Moto2.", "Tras una temporada espectacular, Pedro Acosta se consagra campeón del mundo de la categoría de Moto2. El español ha roto récords, 2 mundiales en 3 años en categorías inferiores.","Pedro Acosta tendrá moto en MotoGP en el equipo de Gas Gas, llevará una KTM después de la forzada salida de Pol Espargaró.",R.drawable.acodos));
        noticiasmotogps.add(new Noticiasmotogp("ÁLEX MÁRQUEZ RECUPERA SU NIVEL EN DUCATI",R.drawable.aluno, "El español recupera su nivel en Gresini tras la salida de Honda.", "Tras pasar por los dos equipos de Honda en MotoGP y no conseguir resultados por el mal rendimiento de la moto, Álex Márquez consiguió una de las motos más preciadas de la parrilla.","El español ha conseguido buenos resultados lo que le ha ayudado a renovar con el equipo Gresini.",R.drawable.aldos));
        noticiasmotogps.add(new Noticiasmotogp("POL ESPARGARÓ FUERA DE MOTOGP",R.drawable.poluno, "Pol Espargaró se queda sin moto para la temporada 2024 después de que KTM suba de categoría a Pedro Acosta.", "Pol Espargaró después de la grave caída que sufrió a principio de temporada, no se ha recuperado totalmente.","Después de los malos resultados y con la obligación que tenía KTM de subir a Pedro Acosta, Pol Espargaró se ha quedado sin moto para el año que viene y seguramente no vuelva a la categoría.",R.drawable.poldos));
        noticiasmotogps.add(new Noticiasmotogp("REPSOL HONDA BUSCA PILOTO PARA 2024 ",R.drawable.repuno, "El Repsol Honda busca piloto para la temporada 2024 después de la salida de Marc Márquez.", "Tras la salida de Marc Márquez, el equipo Repsol Honda busca un piloto para suplir la gran baja del equipo.","Las posibilidades que tiene Honda es subir a Fermín Aldeguer de Moto2 a MotoGP, fichar a Fabio Di Giannantonio que queda libre, o fichar al hermano de Valentino Rossi, Luca Marini.",R.drawable.repdos));

    }

    List<Noticiasmotogp> obtener() {
        return noticiasmotogps;
    }

    void insertar(Noticiasmotogp noticiasmotogps, Callback callback){
        this.noticiasmotogps.add(noticiasmotogps);
        callback.cuandoFinalice(this.noticiasmotogps);
    }

    void eliminar(Noticiasmotogp noticiasmotogps, Callback callback) {
        this.noticiasmotogps.remove(noticiasmotogps);
        callback.cuandoFinalice(this.noticiasmotogps);
    }

}