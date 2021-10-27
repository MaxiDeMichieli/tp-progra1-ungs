package juego;

import java.awt.Color;
import entorno.Entorno;


public class Piso {
	private int ancho;
	private int alto;
	private int x;
	private int y;
	private int numero;
	
	Piso(int x, int y, int ancho, int numero){
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = 10;
		this.numero = numero;
	}
	
	public void dibujarse(Entorno e) {
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.white);
	}

	public int posicionSuperior() {
		return this.y - this.alto / 2;
	}

	public int posicionInferior() {
		return this.y + this.alto / 2;
	}

	public int posicionExtremoIzquierdo() {
		return this.x - this.ancho / 2;
	}

	public int posicionExtremoDerecho() {
		return this.x + this.ancho / 2;
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

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
}
