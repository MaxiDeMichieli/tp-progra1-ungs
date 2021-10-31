package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Computadora {
	private int x, y, ancho, alto;
	private Image avatar;

	/**
	 * Por default se ubica a la computadora arriba a la derecha
	 */
	Computadora(Entorno e) {
		this.x = e.ancho() - 50;
		this.y = 75;
		this.ancho = 50;
		this.alto = 50;
		this.avatar = Herramientas.cargarImagen("PC.png");
	}

	public void dibujarse(Entorno e) {
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.BLACK);
		e.dibujarImagen(this.avatar, this.x, this.y, 0, 1);
	}

	/**
	 * Retorna true si el rectángulo recibido está tocando a la computadora
	 * 
	 * @param xIzq
	 * @param xDer
	 * @param yArr
	 * @param yAba
	 * @return
	 */
	public boolean estaTocando(int xIzq, int xDer, int yArr, int yAba) {
		boolean tocandoX = xIzq < this.posicionExtremoDerecho() && this.posicionExtremoIzquierdo() < xDer;
		boolean tocandoY = yAba > this.posicionYArriba() && this.posicionYAbajo() > yArr;
		return tocandoX && tocandoY;
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

	public int posicionExtremoDerecho() {
		return this.x + this.ancho / 2;
	}

	public int posicionExtremoIzquierdo() {
		return this.x - this.ancho / 2;
	}

	public int posicionYArriba() {
		return this.y - this.alto / 2;
	}

	public int posicionYAbajo() {
		return this.y + this.alto / 2;
	}
}
