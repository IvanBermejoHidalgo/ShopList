package com.example.socialpuig;


import java.util.ArrayList;
import java.util.List;

public class Pilotosf1Repositorio {
    List<Pilotosf1> pilotosf1 = new ArrayList<>();

    interface Callback {
        void cuandoFinalice(List<Pilotosf1> pilotosf1);
    }

    Pilotosf1Repositorio(){
        pilotosf1.add(new Pilotosf1("MAX VERSTAPPEN",R.drawable.maxmax, "Piloto de Formula 1. Tricampeón de Formula 1, 2021, 2022 y 2023", "Fue campeón del Campeonato Mundial de Karting en 2013. Es piloto de Fórmula 1 desde 2015, debutando con la escudería Toro Rosso.","Ha sido tricampeón del Campeonato Mundial de Fórmula 1 en 2021, 2022 y 2023, y tercero en 2019 y 2020 con Red Bull Racing, donde corre desde 2016.","Es el cuarto piloto con mayor número de victorias en la historia de la categoría con 52 grandes premios ganados y el séptimo piloto con más podios: 96.",R.drawable.maxdos));
        pilotosf1.add(new Pilotosf1("FERNANDO ALONSO",R.drawable.fertres, "Piloto de Formula 1. Bicampeón de Formula 1, 2005 y 2006.", "Ha ganado dos veces el Campeonato Mundial de Fórmula 1 en 2005 y 2006 con el equipo Renault, resultó subcampeón en 2010, 2012 y 2013 con la escudería Ferrari, y obtuvo un tercer puesto en 2007 con McLaren.","Fue campeón del Campeonato Mundial de Resistencia de la FIA en 2019, las 24 Horas de Le Mans en 2018 y 2019, las 24 Horas de Daytona de 2019 y el Campeonato Mundial de Karting en 1996.","En su carrera ha competido para los equipos Minardi, Renault, McLaren, Ferrari, Alpine y Aston Martin en Fórmula 1.", R.drawable.ferdos));
        pilotosf1.add(new Pilotosf1("LEWIS HAMILTON",R.drawable.hamham, "Piloto de Formula 1. Heptacampeón de Formula 1, 2008, 2014, 2015, 2017, 2018, 2019 y 2020.", "En Fórmula 1 desde 2007 hasta 2012, fue piloto de la escudería McLaren, con la cual fue campeón en 2008 y subcampeón en 2007.","A partir de 2013, se convirtió en piloto de Mercedes, resultando campeón en 2014, 2015, 2017, 2018, 2019, 2020, igualando los 7 títulos mundiales de Michael Schumacher, y subcampeón en 2016 y 2021.","Ha logrado alzarse con más de 103 victorias en Grandes Premios a lo largo de su carrera en la Fórmula 1, superando en 2020 el récord de victorias en la historia de la competición.", R.drawable.meruno));
        pilotosf1.add(new Pilotosf1("CARLOS SAINZ",R.drawable.cartres, "Piloto de Formula 1. Dos victorias en Formula 1.","De 2010 a 2014 formó parte del Equipo Júnior de Red Bull, ganando este último año la Formula Renault 3.5 Series y debutando en Fórmula 1 al año siguiente con Toro Rosso.","Desde 2021 es piloto de la Scuderia Ferrari.","Ha logrado ganar en la máxima categoría dos veces, en 2022 y 2023, las dos con la Scuderia Ferrari.",R.drawable.caruno));
        pilotosf1.add(new Pilotosf1("CHARLES LECLERC",R.drawable.leclec, "Piloto de Formula 1. Cinco victorias en Formula 1.", "Fue campeón del Campeonato Mundial de Karting en 2011, de GP3 Series en 2016 y de Fórmula 2 en 2017 (estos dos últimos como debutante).","Debutó en la Fórmula 1 con el equipo Sauber en 2018, y desde 2019 es piloto de Scuderia Ferrari, resultando subcampeón del mundo en 2022.","Ha logrado ganar en la máxima categoría cinco veces, en 2019, y 2022, todas con la Scuderia Ferrari.", R.drawable.lecdos));

    }

    List<Pilotosf1> obtener() {
        return pilotosf1;
    }

    void insertar(Pilotosf1 pilotosf1, Callback callback){
        this.pilotosf1.add(pilotosf1);
        callback.cuandoFinalice(this.pilotosf1);
    }

    void eliminar(Pilotosf1 pilotosf1, Callback callback) {
        this.pilotosf1.remove(pilotosf1);
        callback.cuandoFinalice(this.pilotosf1);
    }

}