package juego;

import java.awt.Color;

import entorno.Entorno;
import utils.Direccion;

public class Proyectil {
	private int x, y, ancho, alto;
	private Direccion direccion;
	private Color color;

	Proyectil(int x, int y, Direccion direccion, Color color) {
		this.x = x;
		this.y = y;
		this.ancho = 40;
		this.alto = 5;
		this.direccion = direccion;
		this.color = color;
	}

	public void dibujarse(Entorno e) {
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, this.color);
	}

	public void moverse() {
		if (direccion.equals(Direccion.DERECHA)) {
			this.x += 5;
		} else {
			this.x -= 5;
		}
	}

	public boolean esImpactado(int xIzq, int xDer, int yArr, int yAba) {
		boolean tocandoX = xIzq < this.posicionExtremoDerecho() && this.posicionExtremoIzquierdo() < xDer;
		boolean tocandoY = yAba > this.posicionYArriba() && this.posicionYAbajo() > yArr;
		if (tocandoX && tocandoY) {
			return true;
		}
		return false;
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

	public int posicionYArriba() {
		return this.y - this.alto / 2;
	}

	public int posicionYAbajo() {
		return this.y + this.alto / 2;
	}
}
