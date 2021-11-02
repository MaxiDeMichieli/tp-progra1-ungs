package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;
import utils.Direccion;
import utils.Lista;

public class Velociraptor {
	private int x, y;
	private int ancho, alto;
	private int velocidad;
	private Direccion direccion;
	private Image avatarIzq;
	private Image avatarDer;
	private int proximoDisparo;

	public Velociraptor() {
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
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, new Color(0, 0, 0, 0));
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

	public void avanzar(Piso[] pisos) {
		int pisoActual = this.pisoActual(pisos);
		if ((this.x >= 20 || pisoActual == 0) && this.direccion.equals(Direccion.IZQUIERDA))
			this.x -= velocidad;
		if (this.x == 20 && pisoActual != 0)
			this.direccion = Direccion.DERECHA;
		if (this.x <= 790 && this.direccion.equals(Direccion.DERECHA))
			this.x += velocidad;
		if (this.x == 790)
			this.direccion = Direccion.IZQUIERDA;
	}

	public void disparar(Lista<Proyectil> lasers, int contadorTicks) {
		if (this.proximoDisparo < contadorTicks) {
			if (this.proximoDisparo != 0) {
				int y = randomEntre(0, 1) == 0 ? this.y - 10 : this.y + 15;
				lasers.agregarAtras(new Proyectil(this.x, y, this.direccion, Color.RED));
			}
			this.proximoDisparo = contadorTicks + this.randomEntre(150, 250);
		}
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

	public int pisoActual(Piso[] pisos) {
		for (Piso piso : pisos) {
			if (piso.posicionSuperior() == this.posicionPies()
					&& this.posicionExtremoIzquierdo() <= piso.posicionExtremoDerecho()
					&& this.posicionExtremoDerecho() >= piso.posicionExtremoIzquierdo()) {
				return piso.getNumero();
			}
		}
		return -1;
	}

	public boolean estaEnPantalla(Entorno entorno) {
		return this.posicionExtremoDerecho() > 0 && this.posicionExtremoIzquierdo() < entorno.ancho();
	}

	public boolean esImpactado(int xIzq, int xDer, int yArr, int yAba) {
		boolean tocandoX = xIzq < this.posicionExtremoDerecho() && this.posicionExtremoIzquierdo() < xDer;
		boolean tocandoY = yAba > this.posicionCabeza() && this.posicionPies() > yArr;
		return tocandoX && tocandoY;
	}

	private int randomEntre(int n1, int n2) {
		return (int) Math.floor(Math.random() * (n2 - n1 + 1) + n1);
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
