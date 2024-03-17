package com.example.socialpuig;

import java.util.ArrayList;
import java.util.List;

public class EquiposmotogpRepositorio {
    List<Equiposmotogp> equiposmotogps = new ArrayList<>();

    interface Callback {
        void cuandoFinalice(List<Equiposmotogp> equiposmotogps);
    }

    EquiposmotogpRepositorio(){
        equiposmotogps.add(new Equiposmotogp("REPSOL HONDA HRC",R.drawable.honda, "Equipo de MotoGP. Diez veces campeones del mundo de constructores de MotoGP.", "Repsol Honda Team es desde 1995 el equipo oficial de la fábrica japonesa Honda-HRC, con el patrocinio principal de la petrolera española Repsol.","Como equipo ha conseguido 17 títulos de pilotos en la categoría reina (6 de Marc Márquez, 5 de Mick Doohan, 3 de Valentino Rossi, 1 de Álex Crivillé, 1 de Nicky Hayden y 1 de Casey Stoner). Actualmente sus pilotos son Marc Márquez y Joan Mir.","",R.drawable.hondados));
        equiposmotogps.add(new Equiposmotogp("YAMAHA",R.drawable.yamaha2, "Equipo de MotoGP. Siete veces campeones del mundo de constructores de MotoGP.", "Yamaha Motor Racing es un equipo japonés de motociclismo, que compite actualmente en MotoGP con el nombre de Monster Energy Yamaha MotoGP por motivos de patrocinio.","Sus pilotos para la presente temporada de MotoGP 2021, son el francés Fabio Quartararo y el italiano Franco Morbidelli. Desde 2002, sus pilotos compiten con las sucesivas versiones de la moto Yamaha YZF-M1. ","",R.drawable.fabiodos));
        equiposmotogps.add(new Equiposmotogp("DUCATI LENOVO",R.drawable.duc, "Equipo de MotoGP. Tres veces campeones del mundo de constructores de MotoGP.", "Ducati Corse es la división de equipo de carrera de Ducati que trata con la participación de la firma en carreras de motocicletas. Es dirigido por Claudio Domenicali y está emplazado dentro de Bolonia, en Borgo Panigale.","Ducati empezó a participar en competiciones en 1954, en 1988 empezó en el Campeonato Mundial de Superbikes.","",R.drawable.ducuno));
        equiposmotogps.add(new Equiposmotogp("RED BULL KTM",R.drawable.ktm, "Equipo de MotoGP. Ningún título de campeones del mundo de constructores de MotoGP.", "KTM Factory Racing es el equipo oficial de fábrica de KTM en el Campeonato del mundo de MotoGP","El equipo compite actualmente bajo el nombre de Red Bull KTM Factory Racing. A partir del año 2019 cuenta con el equipo GasGas Tech3 como equipo satélite.","",R.drawable.ktmdos));
        equiposmotogps.add(new Equiposmotogp("APRILIA",R.drawable.apri, "Equipo de MotoGP. Ningún título de campeones del mundo de constructores de MotoGP.", "Aprilia Racing Factory Team es el equipo oficial de fábrica de Aprilia que compite desde la temporada 2022 en el Campeonato del mundo de MotoGP.","Actualmente Aprilia tienen 4 motos, dos oficiales y dos satélites. Los pilotos de las oficiales son: Aleix Espargaró y Maverick Viñales. Los pilotos de las motos satélites son: Miguel Oliveira y Raúl Fernandez","",R.drawable.apriliados));
        //equiposf1s.add(new Equiposf1("",R.drawable.maxmax, "", "","","",R.drawable.maxdos));

    }

    List<Equiposmotogp> obtener() {
        return equiposmotogps;
    }

    void insertar(Equiposmotogp equiposmotogp, EquiposmotogpRepositorio.Callback callback){
        this.equiposmotogps.add(equiposmotogp);
        callback.cuandoFinalice(this.equiposmotogps);
    }

    void eliminar(Equiposmotogp equiposmotogp, EquiposmotogpRepositorio.Callback callback) {
        this.equiposmotogps.remove(equiposmotogp);
        callback.cuandoFinalice(this.equiposmotogps);
    }

}