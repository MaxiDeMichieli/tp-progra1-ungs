package juego;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.function.Function;

import entorno.Entorno;
import entorno.InterfaceJuego;
import utils.Lista;
import utils.Nodo;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Personaje barbariana;
	private Computadora computadora;
	private Velocirraptor[] dinos;

	// Ancho y alto del juego;
	private int height;
	private int width;

	private int cantPisos;
	private Piso[] pisos;

	private int contadorTicks;

	private int puntos;
	private int vidas = 3;

	private Lista<Rayo> rayos;
	private Function<Nodo<Rayo>, Void> moverRayo = rayo -> {
		rayo.getElemento().moverse();
		rayo.getElemento().dibujarse(this.entorno);
		if (!rayo.getElemento().estaEnPantalla(this.entorno)) {
			rayos.quitarPorId(rayo.getId());
		}
		return null;
	};

	Juego() {

		// Inicializa alto y ancho
		this.height = 600;
		this.width = 800;

		this.cantPisos = 5;

		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Grupo 9", this.width, this.height);

		// Inicializa el personaje
		this.barbariana = new Personaje("Barbariana");
		this.rayos = new Lista<Rayo>();

		// Inicializa dinos
		this.dinos = new Velocirraptor[5];

		for (int i = 0; i < dinos.length; i++) {
			this.dinos[i] = new Velocirraptor();
		}

		this.computadora = new Computadora(this.entorno);

		// crea los pisos con sus respectivas ubicaciones
		this._inicializarPisos();

		// Inicia el juego!
		this.entorno.iniciar();

	}

	// Metodo que se ejecuta todo el tiempo
	public void tick() {

		// dibujar el personaje
		this.barbariana.dibujarse(this.entorno);

		// dibuja los pisos
		this._dibujarPisos();

		// dibuja computadora
		this.computadora.dibujarse(this.entorno);

		// dibuja dinos
		for (int i = 0; i < dinos.length; i++) {
			if (this.dinos[i] != null)
				this.dinos[i].dibujarse(this.entorno);
			if (!this.dinos[i].colisionPiso(this.pisos))
				//this.dinos[i].gravedad();
				
				this.dinos[i].avanzar();
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
			this.barbariana.moverDerecha(this);
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
			this.barbariana.moverIzquierda(this);
		}
		// si presiono flecha arriba y salta
		if (this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA) && this.barbariana.colisionPiso(this.pisos)) {
			this.barbariana.saltar(this);
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_ABAJO)) {
			this.barbariana.agacharse();
		} else {
			this.barbariana.pararse();
		}

		if (this.entorno.estaPresionada('u') && this.barbariana.puedeSubirPiso(this.pisos)) {
			this.barbariana.subirPiso(this.pisos, this.barbariana.pisoActual(this.pisos) + 1);
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_ESPACIO) && this.barbariana.colisionPiso(this.pisos)
				&& this.barbariana.getUltimoDisparo() + 50 <= this.contadorTicks) {
			this.barbariana.disparar(this.rayos, this.contadorTicks);
		}

		if (this.rayos.largo() > 0) {
			this.moverRayos();
		}

		if (this.barbariana.getSaltando()) {
			this.barbariana.procesarSalto(this.contadorTicks);
		}

		if (!this.barbariana.colisionPiso(this.pisos) && !this.barbariana.getSaltando()) {
			this.barbariana.gravedad(this);
		}

		entorno.cambiarFont(Font.SANS_SERIF, 25, Color.RED);
		entorno.escribirTexto("Enemigos Eliminados: " + this.puntos, 15, 25);
		entorno.escribirTexto("Vidas: " + this.vidas, 80, 585);

		this.contadorTicks += 1;
	}

	private void _inicializarPisos() {
		Piso[] pisosList = new Piso[this.cantPisos];
		int xPiso = this.width;
		int yPiso = this.height - 50;
		int anchoPiso = this.width * 2;
		for (int i = 0; i < this.cantPisos; i++) {
			Piso p = new Piso(xPiso, yPiso, anchoPiso, i);
			pisosList[i] = p;
			yPiso = yPiso - 110;
			xPiso = xPiso == this.width ? 0 : this.width;
			anchoPiso = (this.width - 150) * 2;
		}
		this.pisos = pisosList;
	}

	private void _dibujarPisos() {
		for (Piso piso : this.pisos) {
			piso.dibujarse(this.entorno);
		}
	}

	public Piso[] getPisos() {
		return pisos;
	}

	public void setPisos(Piso[] pisos) {
		this.pisos = pisos;
	}

	public int getCantPisos() {
		return cantPisos;
	}

	public void setCantPisos(int cantPisos) {
		this.cantPisos = cantPisos;
	}

	public int getHeight() {
		return height;
	}

	public void setHeigth(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getContadorTicks() {
		return this.contadorTicks;
	}

	private void moverRayos() {
		this.rayos.forEachNodo(this.moverRayo);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
