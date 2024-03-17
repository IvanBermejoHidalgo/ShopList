package com.example.socialpuig;


import java.util.ArrayList;
import java.util.List;

public class Noticiasf1Repositorio {
    List<Noticiasf1> noticiasf1s = new ArrayList<>();

    interface Callback {
        void cuandoFinalice(List<Noticiasf1> noticiasf1s);
    }

    Noticiasf1Repositorio(){
        noticiasf1s.add(new Noticiasf1("MAX VERSTAPPEN CAMPEÓN DEL MUNDO 2023",R.drawable.verstappenuno, "El holandés se consagró triple campeón del mundo. Max Verstappen ganó su triple corona a falta de 6 carreras para finalizar la temporada.", "Tras una temporada perfecta, Max Verstappen logra su tercer mundial de una forma aplastante. El holandés ha ganado 17 carreras de 20. Quedan dos carreras para finalizar la temporada y es el máximo favorito.","Su compañero de equipo Checo Pérez no le pudo seguir el pulso. Max Verstappen ha demostrado un nivel estelar durante toda la temporada.",R.drawable.maxdos));
        noticiasf1s.add(new Noticiasf1("RED BULL CAMPEÓN DE CONSTRUCTORES 2023",R.drawable.reduno, "Red Bull gana su sexto título de constructores a falta de 7 carreras para finalizar la temporada tras ganar 15 de 16 carreras.", "Tras una temporada perfecta, Red Bull logra su sexto mundial, segundo consecutivo, de una forma aplastante. Los austriacos han ganado 19 carreras de 20. Quedan dos carreras para finalizar la temporada y son los máximos favoritos.","Max Verstappen le ha dado al equipo 17 carreras ganadas, siendo campeón del mundo, mientras que Checo Pérez solo ha dado 2 victorias.",R.drawable.reddos));
        noticiasf1s.add(new Noticiasf1("PODIO HEROICO DE FERNANDO ALONSO EN BRASIL",R.drawable.feruno, "Fernando Alonso vuelve al podio tras las mejoras  de Aston Martin. El asturiano consigue su octavo podio de esta temporada.", "Tan sólo 53 milésimas fue la ventaja que tuvo el asturiano sobre el de Red Bull al cruzar la línea de meta.","A sus 42 años, el español sigue marcando pautas que pocos pilotos pueden alcanzar, y a las que tantos tienen que aspirar. En el GP de Brasil produjo una nueva actuación extraordinaria, a la altura de los más grandes de todos los tiempos.",R.drawable.ferdos));
        noticiasf1s.add(new Noticiasf1("CARLOS SAINZ CONSIGUE SU TERCERA VICTORIA",R.drawable.caruno, "Carlos Sainz consigue su segunda victoria en Formula 1 y la única victoria no Red Bull de la temporada 2023.", "Carlos Sainz se llevó el triunfo, el segundo en su carrera deportiva, ambos con Ferrari, y Lando Norris volvió a quedarse a las puertas, preguntándose una vez más cuándo le va a tocar a él.","Una victoria de las que se recuerdan, dejando estéril a un Charles Leclerc que cruzó meta a más de 20’’ de Carlos.",R.drawable.cardos));
        noticiasf1s.add(new Noticiasf1("OSCAR PIASTRI CONSIGUE SU PRIMERA VICTORIA EN SPRINT",R.drawable.piuno, "Oscar Piastri consigue su primera victoria en Formula 1 en una sprint race y la única victoria en sprint no Red Bull de la temporada 2023.", "Los chicos de McLaren lograron amarrar un muy gran resultado, con la primera victoria al Sprint en F1 del piloto australiano, Oscar Piastri.","Lando Norris tuvo una mala salida y eso complicó su carrera, pero solo Max Verstappen logró doblegarle y relegarle a la tercera posición.",R.drawable.pidos));
        noticiasf1s.add(new Noticiasf1("MERCEDES SE CONSOLIDA EN LA SEGUNDA POSICIÓN",R.drawable.meruno, "El equipo Mercedes se consolida como segundo coche de la temporada por detrás de un imparable Red Bull.", "El equipo Mercedes consolida la segunda posición en el campeonato de constructores después de cambiar el coche en mitad de temporada.","El equipo alemán no empezó del todo bien la temporada, pero han traído muchas mejoras durante la temporada, lo que los hace consolidar la segunda posición.",R.drawable.merdos));

    }

    List<Noticiasf1> obtener() {
        return noticiasf1s;
    }

    void insertar(Noticiasf1 noticiasf1s, Callback callback){
        this.noticiasf1s.add(noticiasf1s);
        callback.cuandoFinalice(this.noticiasf1s);
    }

    void eliminar(Noticiasf1 noticiasf1s, Callback callback) {
        this.noticiasf1s.remove(noticiasf1s);
        callback.cuandoFinalice(this.noticiasf1s);
    }

}