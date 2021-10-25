package juego;

import java.awt.Color;

import entorno.Entorno;


public class Barbarianna {
	public int x, y;
	private int ancho, alto;

	public Barbarianna(int x, int y) {
		this.x = x;
		this.y = y;
		this.ancho = 60;
		this.alto = 80;
	}

	public void dibujarse(Entorno entorno) {
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.blue);
	}

	public void moverIzquierda() {
		this.x -= 10;
	}

	public void moverDerecha() {
		this.x += 10;
	}
	
	public void agacharse() {
	}
	
	public void saltar() {
		
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

}
