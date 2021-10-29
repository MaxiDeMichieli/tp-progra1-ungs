package juego;

import java.awt.Color;

import entorno.Entorno;
import utils.Direccion;

public class Rayo {
	private int x, y, ancho, alto;
	private Direccion direccion;

	Rayo(int x, int y, Direccion direccion) {
		this.x = x;
		this.y = y;
		this.ancho = 40;
		this.alto = 5;
		this.direccion = direccion;
	}

	public void dibujarse(Entorno e) {
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.YELLOW);
	}

	public void moverse() {
		if (direccion.equals(Direccion.DERECHA)) {
			this.x += 5;
		} else {
			this.x -= 5;
		}
	}
	
	public boolean estaTocando(int x, int y) {
		return this.x <= x && this.y == y;
	}

	public boolean estaEnPantalla(Entorno entorno) {
		return this.posicionExtremoDerecho() > 0 && this.posicionExtremoIzquierdo() < entorno.ancho();
	}

	public int posicionExtremoDerecho() {
		return this.x + this.ancho / 2;
	}

	public int posicionExtremoIzquierdo() {
		return this.x - this.ancho / 2;
	}
}
