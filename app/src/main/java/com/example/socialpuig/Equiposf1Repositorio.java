package com.example.socialpuig;

import java.util.ArrayList;
import java.util.List;

public class Equiposf1Repositorio {
    List<Equiposf1> equiposf1s = new ArrayList<>();

    interface Callback {
        void cuandoFinalice(List<Equiposf1> equiposf1s);
    }

    Equiposf1Repositorio(){
        equiposf1s.add(new Equiposf1("SCUDERIA FERRARI",R.drawable.ferrari, "Escudería de la Formula 1. Dieciséis veces campeones del mundo de constructores de Formula 1.", "Es la escudería más longeva y el equipo en activo más antiguo de la categoría, el más exitoso y el que ha conseguido más victorias, campeonatos de pilotos (15) y campeonatos de constructores (16). ","La primera participación de Ferrari en Fórmula 1 fue en el Gran Premio de Mónaco de 1950, con el Tipo 125 F1. Ferrari es considerado uno de los cuatro grandes equipos de Fórmula 1, junto con Williams, McLaren y Mercedes.","",R.drawable.caruno));
        equiposf1s.add(new Equiposf1("MERCEDES AMG F1",R.drawable.mercedes, "Escudería de la Formula 1. Ocho veces campeones del mundo de constructores de Formula 1.", "Participó inicialmente como constructor en Fórmula 1 en 1954 y 1955, y volvió a hacerlo desde 2010 hasta la actualidad.","Es la tercera escudería que posee más Campeonatos de Pilotos, con 9 (1954, 1955, 2014, 2015, 2016, 2017, 2018, 2019 y 2020); ganados por Lewis Hamilton (6), Juan Manuel Fangio (2) y Nico Rosberg (1).","",R.drawable.merdos));
        equiposf1s.add(new Equiposf1("RED BULL RACING",R.drawable.redbull, "Escudería de la Formula 1. Seis veces campeones del mundo de constructores de Formula 1.", "La compañía adquirió la escudería Jaguar Racing por cerca de US$110 000 000, cuando la propietaria anterior de este equipo Ford Motor Company, anunció su retirada de la máxima categoría del automovilismo.","Compite desde 2005 y en sus dos primeras temporadas participaba con licencia británica.","",R.drawable.reddos));
        equiposf1s.add(new Equiposf1("MCLAREN F1 TEAM",R.drawable.mclaren, "Escudería de la Formula 1. Ocho veces campeones del mundo de constructores de Formula 1.", "Fue fundada en 1963 por el piloto neozelandés Bruce McLaren, en sociedad con los estadounidenses Teddy Mayer y Tyler Alexander. ","A lo largo de los años ha obtenido 8 Campeonatos de Constructores (tercera en el historial), 12 Campeonatos de Pilotos (segunda), 183 victorias (segunda), 155 poles (segunda) y 489 podios (segunda).","",R.drawable.piuno));
        equiposf1s.add(new Equiposf1("WILLIAMS F1 TEAM",R.drawable.williams, "Escudería de la Formula 1. Nueve veces campeones del mundo de constructores de Formula 1.", "Williams Grand Prix Engineering Limited,  \u200B comúnmente conocido como Williams Racing, es un equipo de Fórmula 1 creado en 1977 por Frank Williams y Patrick Head.  Williams es considerado uno de los cuatro grandes equipos de Fórmula 1, junto con Ferrari, McLaren y Mercedes.","Siete pilotos han sido campeones mundiales con Williams: Alan Jones, Keke Rosberg, Nelson Piquet, Nigel Mansell, Alain Prost, Damon Hill y Jacques Villeneuve. A su vez, el equipo ganó 9 Campeonatos de Constructores entre 1980 y 1997.","",R.drawable.williams2));
        //equiposf1s.add(new Equiposf1("",R.drawable.maxmax, "", "","","",R.drawable.maxdos));

    }

    List<Equiposf1> obtener() {
        return equiposf1s;
    }

    void insertar(Equiposf1 equiposf1, Equiposf1Repositorio.Callback callback){
        this.equiposf1s.add(equiposf1);
        callback.cuandoFinalice(this.equiposf1s);
    }

    void eliminar(Equiposf1 equiposf1, Equiposf1Repositorio.Callback callback) {
        this.equiposf1s.remove(equiposf1);
        callback.cuandoFinalice(this.equiposf1s);
    }

}