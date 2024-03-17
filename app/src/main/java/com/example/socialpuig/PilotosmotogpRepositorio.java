package com.example.socialpuig;

import java.util.ArrayList;
import java.util.List;

public class PilotosmotogpRepositorio {
    List<PilotosMotogp> pilotosMotogps = new ArrayList<>();

    interface Callback {
        void cuandoFinalice(List<PilotosMotogp> pilotosMotogps);
    }

    PilotosmotogpRepositorio(){
        pilotosMotogps.add(new PilotosMotogp("MARC MÁRQUEZ",R.drawable.mmtres, "Piloto de MotoGP. Hexacampeón de MotoGP, en 2013, 2014, 2016, 2017, 2018, y 2019.", "Ha ganado ocho títulos del Campeonato del Mundo de Motociclismo en tres categorías diferentes: 125cc (2010), Moto2 (2012) y seis veces en MotoGP (2013, 2014, 2016, 2017, 2018 y 2019).","Desde 2013 es piloto del equipo Repsol Honda, acumulando 59 victorias y 101 podios en más de 150 carreras disputadas.","En su primera temporada en la categoría, se hizo con el Campeonato del Mundo de Motociclismo, convirtiéndose en el piloto más joven en ganar un campeonato de la máxima categoría de este deporte",R.drawable.mmdos));
        pilotosMotogps.add(new PilotosMotogp("FABIO QUARTARARO",R.drawable.fabio, "Piloto de MotoGP. Campeón de MotoGP, en 2021.", "Compite en el equipo Monster Energy Yamaha en el Campeonato del Mundo de Motociclismo de MotoGP. ","Antes de su entrada en la categoría reina, ganó seis títulos del Campeonato de España de Velocidad incluyendo dos títulos de Moto3 Junior en 2013 y 2014.","Se proclamó campeón del mundo de MotoGP la temporada 2021, siendo el primer piloto francés en lograrlo. Además, resultó subcampeón del mundo en la temporada 2022.", R.drawable.yamaha));
        pilotosMotogps.add(new PilotosMotogp("PECCO BAGNAIA",R.drawable.pecco, "Piloto de MotoGP. Campeón de MotoGP, en 2022.", "Francesco Bagnaia más conocido como Pecco Bagnaia, es un piloto de motociclismo italiano que compite en MotoGP con el equipo Ducati Lenovo.","Es dos veces campeón del Campeonato Mundial de Motociclismo, de Moto2 en 2018 y de MotoGP en 2022, donde resultó subcampeón del mundo en 2021.","", R.drawable.ducuno));
        pilotosMotogps.add(new PilotosMotogp("JOAN MIR",R.drawable.mir, "Piloto de MotoGP. Campeón de MotoGP, en 2020.","Ha ganado dos títulos mundiales del Campeonato del Mundo de Motociclismo en dos categorías diferentes: de Moto3 en 2017 y de MotoGP en 2020 con el Team Suzuki Ecstar, además de un tercer puesto en 2021.","Se convirtió en el sexto piloto de la marca en alzarse campeón de la máxima categoría del motociclismo tras Barry Sheene, Marco Lucchinelli, Franco Uncini, Kevin Schwantz y Kenny Roberts Jr.","Desde 2023 forma parte del Repsol Honda Team siendo compañero de Marc Márquez en la categoría reina. ",R.drawable.repuno));
        pilotosMotogps.add(new PilotosMotogp("MAVERICK VIÑALES",R.drawable.mav, "Piloto de MotoGP. Nueve victorias en MotoGP.", "Ha ganado dos títulos mundiales del Campeonato del Mundo de Motociclismo en dos categorías diferentes: de Moto3 en 2017 y de MotoGP en 2020 con el Team Suzuki Ecstar, además de un tercer puesto en 2021.","Se convirtió en el sexto piloto de la marca en alzarse campeón de la máxima categoría del motociclismo tras Barry Sheene, Marco Lucchinelli, Franco Uncini, Kevin Schwantz y Kenny Roberts Jr.","Desde 2023 forma parte del Repsol Honda Team siendo compañero de Marc Márquez en la categoría reina. ", R.drawable.mavdos));

    }

    List<PilotosMotogp> obtener() {
        return pilotosMotogps;
    }

    void insertar(PilotosMotogp pilotosMotogp, PilotosmotogpRepositorio.Callback callback){
        this.pilotosMotogps.add(pilotosMotogp);
        callback.cuandoFinalice(this.pilotosMotogps);
    }

    void eliminar(PilotosMotogp pilotosMotogp, PilotosmotogpRepositorio.Callback callback) {
        this.pilotosMotogps.remove(pilotosMotogp);
        callback.cuandoFinalice(this.pilotosMotogps);
    }

}