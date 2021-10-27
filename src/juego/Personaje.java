package juego;

import java.awt.Color;

import entorno.Entorno;

public class Personaje {
	private String name;
	private int x, y;
	private int ancho, alto, altoInicial;
	private String imagen = "./barbariana.jpeg";
	private boolean saltando;
	private int ultimoSalto;

	Personaje(String name) {
		this.x = 50;
		this.y = 115;
		this.ancho = 30;
		this.alto = 60;
		this.altoInicial = 60;
		this.name = name;
	}

	public void moverIzquierda(Juego j) {
		if (this.posicionExtremoIzquierdo() <= 0 || this.colisionPisoLateral(j.getPisos()) != -1)
			return;
		this.x = this.x - 5;
	}

	public void moverDerecha(Juego j) {
		if (this.posicionExtremoDerecho() >= j.getWidth() || this.colisionPisoLateral(j.getPisos()) != -1)
			return;
		this.x = this.x + 5;
	}

	public void gravedad(Juego j) {
		if (this.posicionPies() >= j.getHeight() || this.saltando)
			return;
		this.y += 2.5;
	}

	public void saltar(Juego j) {
		if (this.posicionCabeza() <= 0)
			return;
		this.ultimoSalto = j.getContadorTicks();
		this.saltando = true;
	}

	public void agacharse() {
		if (this.alto >= this.altoInicial) {
			this.alto = this.alto / 2;
			this.y += this.alto / 2;
		}
	}

	public void pararse() {
		if (this.alto < this.altoInicial) {
			this.y -= this.alto / 2;
			this.alto = this.altoInicial;
		}

	}

	public void dibujarse(Entorno e) {
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.GREEN);
	}

	public int colisionPiso(Piso[] pisos) {
		for (Piso piso : pisos) {
			if (piso.posicionSuperior() == this.posicionPies()
					&& this.posicionExtremoIzquierdo() <= piso.posicionExtremoDerecho()
					&& this.posicionExtremoDerecho() >= piso.posicionExtremoIzquierdo()) {
				this.saltando = false;
				return piso.getNumero();
			}
		}
		return -1;
	}

	public int colisionPisoLateral(Piso[] pisos) {
		for (Piso piso : pisos) {
			if (piso.posicionSuperior() < this.posicionPies() && piso.posicionInferior() > this.posicionCabeza()
					&& this.posicionExtremoIzquierdo() <= piso.posicionExtremoDerecho()
					&& this.posicionExtremoDerecho() >= piso.posicionExtremoIzquierdo()) {
				return piso.getNumero();
			}
		}
		return -1;
	}

	public void procesarSalto(int contadorTicks) {
		if (this.ultimoSalto + 16 < contadorTicks) {
			this.saltando = false;
			return;
		}
		this.y -= 2;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean getSaltando() {
		return this.saltando;
	}
}
