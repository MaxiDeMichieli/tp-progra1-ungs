package juego;

import java.awt.Color;

import entorno.Entorno;

public class Computadora {
	private int x,y,ancho,alto;
	
	/**
	 * Por default se ubica a la computadora arriba a la derecha
	 */
	Computadora(Entorno e){
		this.x = e.ancho() - 50;
		this.y = 75;
		this.ancho = 50;
		this.alto = 50;
	}
	
	public void dibujarse(Entorno e) {
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.BLUE);
	}
	
	/**
	 * Retorna true si la posicion recibida esta tocando a la computadora
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean estaTocando(int x, int y) {
		return this.x <= x && this.y == y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}
	
}
