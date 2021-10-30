package juego;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;
import utils.Direccion;
import utils.Lista;

public class Velocirraptor {
	private int x, y;
	private int ancho, alto;
	private int velocidad;
	private Direccion direccion;
	private Image avatarIzq;
	private Image avatarDer;

	public Velocirraptor() {
		this.x = 770;
		this.y = 75;
		this.ancho = 30;
		this.alto = 60;
		this.velocidad = 2;
		this.direccion = Direccion.IZQUIERDA;
		this.avatarDer = Herramientas.cargarImagen("Dino.png");
		this.avatarIzq = Herramientas.cargarImagen("DinoIzq.png");
	}

	public void dibujarse(Entorno entorno) {
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.BLACK);
		if (this.direccion.equals(Direccion.IZQUIERDA))
			entorno.dibujarImagen(this.avatarIzq, this.x, this.y, 0, 1);
		if (this.direccion.equals(Direccion.DERECHA))
			entorno.dibujarImagen(this.avatarDer, this.x, this.y, 0, 1);		
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
