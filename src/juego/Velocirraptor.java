package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;
import utils.Direccion;
import utils.Lista;

public class Velocirraptor {
	private int x, y;
	private int ancho, alto;
	private int velocidad;
	private Direccion direccion;

	public Velocirraptor() {
		this.x = 770;
		this.y = 75;
		this.ancho = 30;
		this.alto = 60;
		this.velocidad = 2;
		this.direccion = Direccion.IZQUIERDA;
	}

	public void dibujarse(Entorno entorno) {
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.red);
	}

	public void gravedad(Juego j) {
		if (this.posicionPies() >= j.getHeight())
			return;
		this.y += 2.5;
	}

	public void avanzar() {
		if (this.x >= 20 && this.direccion.equals(Direccion.IZQUIERDA))
			this.x -= velocidad;
		if (this.x == 20)
			this.direccion = Direccion.DERECHA;
		if (this.x <= 790 && this.direccion.equals(Direccion.DERECHA))
			this.x += velocidad;
		if (this.x == 790)
			this.direccion = Direccion.IZQUIERDA;
	}

	public boolean colisionPiso(Piso[] pisos) {
		for (Piso piso : pisos) {
			if (piso.posicionSuperior() == this.posicionPies()
					&& this.posicionExtremoIzquierdo() <= piso.posicionExtremoDerecho()
					&& this.posicionExtremoDerecho() >= piso.posicionExtremoIzquierdo()) {
				return true;
			}
		}
		return false;
	}

	public int posicionExtremoDerecho() {
		return this.x + this.ancho / 2;
	}

	public int posicionExtremoIzquierdo() {
		return this.x - this.ancho / 2;
	}

	public int posicionCabeza() {
		return this.y - this.alto / 2;
	}

	public int posicionPies() {
		return this.y + this.alto / 2;
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

	public int getVelocidad() {
		return velocidad;
	}
}
