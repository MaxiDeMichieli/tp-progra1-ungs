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
		this.x = 500;
		this.y = 15;
		this.ancho = 30;
		this.alto = 60;
		this.altoInicial = 60;
		this.name = name;
	}

	public void moverIzquierda(Juego j) {
		if (this.posicionExtremoIzquierdo() <= 0 || this.colisionPisoLateral(j.getPisos()))
			return;
		this.x = this.x - 5;
	}

	public void moverDerecha(Juego j) {
		if (this.posicionExtremoDerecho() >= j.getWidth() || this.colisionPisoLateral(j.getPisos()))
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

	public boolean colisionPiso(Piso[] pisos) {
		for (Piso piso : pisos) {
			if (piso.posicionSuperior() == this.posicionPies()
					&& this.posicionExtremoIzquierdo() <= piso.posicionExtremoDerecho()
					&& this.posicionExtremoDerecho() >= piso.posicionExtremoIzquierdo()) {
				this.saltando = false;
				return true;
			}
		}
		return false;
	}

	public boolean colisionPisoLateral(Piso[] pisos) {
		for (Piso piso : pisos) {
			if (piso.posicionSuperior() < this.posicionPies() && piso.posicionInferior() > this.posicionCabeza()
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
				this.saltando = false;
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

	public boolean puedeSubirPiso(Piso[] pisos) {
		int pisoActual = this.pisoActual(pisos);
		try {
			if (pisoActual != -1 && (this.posicionExtremoIzquierdo() >= pisos[pisoActual + 1].posicionExtremoDerecho()
					|| this.posicionExtremoDerecho() <= pisos[pisoActual + 1].posicionExtremoIzquierdo()))
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public void subirPiso(Piso[] pisos, int numeroDePiso) {
		try {
			Piso pisoASubir = pisos[numeroDePiso];
			if (Math.abs(pisoASubir.posicionExtremoDerecho() - this.x) < Math
					.abs(pisoASubir.posicionExtremoIzquierdo() - this.x)) {
				this.y = pisoASubir.posicionSuperior() - this.alto / 2;
				this.x = pisoASubir.posicionExtremoDerecho() - this.ancho / 2;
			}
			if (Math.abs(pisoASubir.posicionExtremoIzquierdo() - this.x) < Math
					.abs(pisoASubir.posicionExtremoDerecho() - this.x)) {
				this.y = pisoASubir.posicionSuperior() - this.alto / 2;
				this.x = pisoASubir.posicionExtremoIzquierdo() + this.ancho / 2;
			}
		} catch (Exception e) {
			return;
		}
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
