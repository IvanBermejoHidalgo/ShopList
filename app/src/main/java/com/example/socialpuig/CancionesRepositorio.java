package com.example.socialpuig;


import java.util.ArrayList;
import java.util.List;

public class CancionesRepositorio {
    List<Canciones> canciones = new ArrayList<>();
    List<Noticiasmotogp> noticiasmotogps = new ArrayList<>();

    interface Callback {
        void cuandoFinalice(List<Canciones> canciones);
    }

    CancionesRepositorio(){
        canciones.add(new Canciones("MAX VERSTAPPEN CAMPEÓN DEL MUNDO 2023",R.drawable.verstappenuno, "El holandés se consagró triple campeón del mundo. Max Verstappen ganó su triple corona a falta de 6 carreras para finalizar la temporada.", "Tras una temporada perfecta, Max Verstappen logra su tercer mundial de una forma aplastante. El holandés ha ganado 17 carreras de 20. Quedan dos carreras para finalizar la temporada y es el máximo favorito.","Su compañero de equipo Checo Pérez no le pudo seguir el pulso. Max Verstappen ha demostrado un nivel estelar durante toda la temporada.",R.drawable.maxdos));
        canciones.add(new Canciones("MARC MÁRQUEZ FICHA POR GRESINI PARA 2024",R.drawable.mmuno, "Marc Márquez ficha por Gresini para 2024. Abandona Honda tras 11 años juntos.", "Marc Márquez tras unas temporadas de lesiones y de no tener una moto para poder pelear del mundial, decide dejar Honda tras 11 años junto a la marca japonesa.","Ficha por Gresini que lleva una Ducati. Pasa de la peor moto de la parrilla a la mejor. Compartirá equipo con su hermano Álex Márquez.",R.drawable.mmdos));
        canciones.add(new Canciones("RED BULL CAMPEÓN DE CONSTRUCTORES 2023",R.drawable.reduno, "Red Bull gana su sexto título de constructores a falta de 7 carreras para finalizar la temporada tras ganar 15 de 16 carreras.", "Tras una temporada perfecta, Red Bull logra su sexto mundial, segundo consecutivo, de una forma aplastante. Los austriacos han ganado 19 carreras de 20. Quedan dos carreras para finalizar la temporada y son los máximos favoritos.","Max Verstappen le ha dado al equipo 17 carreras ganadas, siendo campeón del mundo, mientras que Checo Pérez solo ha dado 2 victorias.",R.drawable.reddos));
        canciones.add(new Canciones("LUCHA POR EL CAMPEONATO ENTRE DUCATIS",R.drawable.ducuno, "Jorge Martín y Pecco Bagnaia se juegan el título mundial, los dos con Ducati, una oficial y otra satélite.", "Jorge Martín y Pecco Bagnaia se juegan el título mundial. El español quiere su primer campeonato de MotoGP mientras que el italiano quiere el bicampeonato.","Los dos van con dos Ducati, el español va con una Ducati satélite y el italiano va con una oficial.",R.drawable.ducdos));
        canciones.add(new Canciones("PODIO HEROICO DE FERNANDO ALONSO EN BRASIL",R.drawable.feruno, "Fernando Alonso vuelve al podio tras las mejoras  de Aston Martin. El asturiano consigue su octavo podio de esta temporada.", "Tan sólo 53 milésimas fue la ventaja que tuvo el asturiano sobre el de Red Bull al cruzar la línea de meta.","A sus 42 años, el español sigue marcando pautas que pocos pilotos pueden alcanzar, y a las que tantos tienen que aspirar. En el GP de Brasil produjo una nueva actuación extraordinaria, a la altura de los más grandes de todos los tiempos.",R.drawable.ferdos));
        canciones.add(new Canciones("PEDRO ACOSTA TENDRÁ MOTO EN LA CATEGORÍA REINA",R.drawable.acouno, "Pedro Acosta tendrá moto en el equipo GAS GAS  de MotoGP tras conseguir el título mundial de Moto2.", "Tras una temporada espectacular, Pedro Acosta se consagra campeón del mundo de la categoría de Moto2. El español ha roto récords, 2 mundiales en 3 años en categorías inferiores.","Pedro Acosta tendrá moto en MotoGP en el equipo de Gas Gas, llevará una KTM después de la forzada salida de Pol Espargaró.",R.drawable.acodos));
        canciones.add(new Canciones("CARLOS SAINZ CONSIGUE SU TERCERA VICTORIA",R.drawable.caruno, "Carlos Sainz consigue su segunda victoria en Formula 1 y la única victoria no Red Bull de la temporada 2023.", "Carlos Sainz se llevó el triunfo, el segundo en su carrera deportiva, ambos con Ferrari, y Lando Norris volvió a quedarse a las puertas, preguntándose una vez más cuándo le va a tocar a él.","Una victoria de las que se recuerdan, dejando estéril a un Charles Leclerc que cruzó meta a más de 20’’ de Carlos.",R.drawable.cardos));
        canciones.add(new Canciones("ÁLEX MÁRQUEZ RECUPERA SU NIVEL EN DUCATI",R.drawable.aluno, "El español recupera su nivel en Gresini tras la salida de Honda.", "Tras pasar por los dos equipos de Honda en MotoGP y no conseguir resultados por el mal rendimiento de la moto, Álex Márquez consiguió una de las motos más preciadas de la parrilla.","El español ha conseguido buenos resultados lo que le ha ayudado a renovar con el equipo Gresini.",R.drawable.aldos));
        canciones.add(new Canciones("OSCAR PIASTRI CONSIGUE SU PRIMERA VICTORIA EN SPRINT",R.drawable.piuno, "Oscar Piastri consigue su primera victoria en Formula 1 en una sprint race y la única victoria en sprint no Red Bull de la temporada 2023.", "Los chicos de McLaren lograron amarrar un muy gran resultado, con la primera victoria al Sprint en F1 del piloto australiano, Oscar Piastri.","Lando Norris tuvo una mala salida y eso complicó su carrera, pero solo Max Verstappen logró doblegarle y relegarle a la tercera posición.",R.drawable.pidos));
        canciones.add(new Canciones("POL ESPARGARÓ FUERA DE MOTOGP",R.drawable.poluno, "Pol Espargaró se queda sin moto para la temporada 2024 después de que KTM suba de categoría a Pedro Acosta.", "Pol Espargaró después de la grave caída que sufrió a principio de temporada, no se ha recuperado totalmente.","Después de los malos resultados y con la obligación que tenía KTM de subir a Pedro Acosta, Pol Espargaró se ha quedado sin moto para el año que viene y seguramente no vuelva a la categoría.",R.drawable.poldos));
        canciones.add(new Canciones("MERCEDES SE CONSOLIDA EN LA SEGUNDA POSICIÓN",R.drawable.meruno, "El equipo Mercedes se consolida como segundo coche de la temporada por detrás de un imparable Red Bull.", "El equipo Mercedes consolida la segunda posición en el campeonato de constructores después de cambiar el coche en mitad de temporada.","El equipo alemán no empezó del todo bien la temporada, pero han traído muchas mejoras durante la temporada, lo que los hace consolidar la segunda posición.",R.drawable.merdos));
        canciones.add(new Canciones("REPSOL HONDA BUSCA PILOTO PARA 2024 ",R.drawable.repuno, "El Repsol Honda busca piloto para la temporada 2024 después de la salida de Marc Márquez.", "Tras la salida de Marc Márquez, el equipo Repsol Honda busca un piloto para suplir la gran baja del equipo.","Las posibilidades que tiene Honda es subir a Fermín Aldeguer de Moto2 a MotoGP, fichar a Fabio Di Giannantonio que queda libre, o fichar al hermano de Valentino Rossi, Luca Marini.",R.drawable.repdos));

    }

    List<Canciones> obtener() {
        return canciones;
    }

    void insertar(Canciones canciones, Callback callback){
        this.canciones.add(canciones);
        callback.cuandoFinalice(this.canciones);
    }

    void eliminar(Canciones canciones, Callback callback) {
        this.canciones.remove(canciones);
        callback.cuandoFinalice(this.canciones);
    }

}