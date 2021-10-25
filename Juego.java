package juego;


import java.awt.Color;
import java.awt.Font;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Barbarianna player;
	public int puntos;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Boss Rabbit Rabber - Grupo 9 - v1", 800, 600);
		
		// Inicializar lo que haga falta para el juego
		this.player= new Barbarianna(100,500);
		this.puntos= 0;
		// ...

		// Inicia el juego!
		this.entorno.iniciar();
		
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		
		// ...
		this.player.dibujarse(entorno);

		entorno.cambiarFont(Font.SANS_SERIF, 20, Color.RED);
		entorno.escribirTexto("Enemigos Eliminados: " + this.puntos, 20, 20);
		
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && player.getX() > 0 + player.getAncho()/2) {	 
			player.moverIzquierda();
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA) && player.getX() < entorno.ancho() - player.getAncho()/2) {
			player.moverDerecha();
		}
		
		if (entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			player.agacharse();
		}
		
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
			player.saltar();
		}
		

	}
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
		
	}
}
