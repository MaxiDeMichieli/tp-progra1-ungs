package juego;

import java.awt.Color;
import java.awt.Font;
import entorno.Entorno;
import entorno.InterfaceJuego;
import utils.Lista;
import utils.Nodo;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Personaje barbariana;
	private Computadora computadora;
	 

	// Ancho y alto del juego;
	private int height;
	private int width;

	private int cantPisos;
	private Piso[] pisos;

	private int contadorTicks;

	private int puntos; // score
	private int eliminados; // dinos eliminados

	// 1 = Gano
	// 0 = Perdio
	private int estadoJuego = -1;

	private int tickUltimoDino;
	private Lista<Velociraptor> dinos;
	private Lista<Proyectil> lasers;

	Juego() {
		// Inicializa alto y ancho
		this.height = 600;
		this.width = 800;

		this.cantPisos = 5;

		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Grupo 9", this.width, this.height);

		// Inicializa el personaje
		this.barbariana = new Personaje("Barbariana");

		// Inicializa dinos
		this.dinos = new Lista<Velociraptor>();
		this.lasers = new Lista<Proyectil>();
		this.tickUltimoDino = -1;

		// Crea la computadora
		this.computadora = new Computadora(this.entorno);

		// crea los pisos con sus respectivas ubicaciones
		this._inicializarPisos();

		// Inicia el juego!
		this.entorno.iniciar();

	}

	// Metodo que se ejecuta todo el tiempo
	public void tick() {
		if (this.estadoJuego != -1) {
			if (this.estadoJuego == 1) {
				this.finDelJuego("Ganaste!", Color.GREEN);
			} else if (this.estadoJuego == 0) {
				this.finDelJuego("Perdiste", Color.RED);
			}
		} else {
			// dibujar el personaje
			this.barbariana.dibujarse(this.entorno);

			// dibuja los pisos
			this._dibujarPisos();

			// dibuja computadora
			this.computadora.dibujarse(this.entorno);

			// crea y procesa dinos
			this._crearDinos();
			if (this.dinos.largo() > 0) {
				this.procesarDinos();
			}

			if (this.lasers.largo() > 0) {
				this.procesarLasers();
			}

			// Se verifica el impacto del dino no solo cuando se mueve a un lado, sino
			// tambien
			// cuando esta quieto el personaje.
			this._verificarImpactoDinoAPersonaje();

			// metodo que maneja las acciones de acuerdo a la tecla que se presione en el
			// momento
			this._actualizarMovimientos();

			if (this.barbariana.getRayos().largo() > 0) {
				this.procesarRayos();
			}

			if (this.barbariana.getSaltando()) {
				this.barbariana.procesarSalto(this.contadorTicks);
			}

			if (!this.barbariana.colisionPiso(this.pisos) && !this.barbariana.getSaltando()) {
				this.barbariana.gravedad(this);
			}

			entorno.cambiarFont(Font.SANS_SERIF, 25, Color.RED);
			entorno.escribirTexto("Enemigos Eliminados: " + this.eliminados, 15, 25);
			entorno.escribirTexto("Score: " + this.puntos, this.width - 150, 585);

			this.contadorTicks += 1;
		}
	}

	/**
	 * De acuerdo a una tecla presioanda, ejecuta si accion correspondiente
	 */
	private void _actualizarMovimientos() {
		if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
			// chequea que barbariana no llegue a la computadora.
			// Si llega, termina el juego y gana
			if (this.computadora.estaTocando(this.barbariana.posicionExtremoIzquierdo(),
					this.barbariana.posicionExtremoDerecho(), this.barbariana.posicionCabeza(), this.barbariana.posicionPies())) {
				this.estadoJuego = 1;
			}
			this.barbariana.moverDerecha(this);
		}

		if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
			this.barbariana.moverIzquierda(this);
		}
		// si presiono flecha arriba salta
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
			this.barbariana.disparar(this.contadorTicks);
		}
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

	private void _crearDinos() {
		if (this.tickUltimoDino == -1 || (this.dinos.largo() <= 6 && this.tickUltimoDino + 300 < this.contadorTicks)) {
			this.dinos.agregarAtras(new Velociraptor());
			this.tickUltimoDino = this.contadorTicks;
		}
	}

	private void _dibujarPisos() {
		for (Piso piso : this.pisos) {
			piso.dibujarse(this.entorno);
		}
	}

	private void _verificarImpactoDinoAPersonaje() {
		this.dinos.forEachElement(dino -> {
			if (this.barbariana.esImpactado(dino.posicionExtremoIzquierdo(), dino.posicionExtremoDerecho(),
					dino.posicionCabeza(), dino.posicionPies())) {
				this.estadoJuego = 0;
			}
			return null;
		});
	}

	private void _verificarImpactoLaserAPersonaje(Proyectil laser) {
		if (this.barbariana.esImpactado(laser.posicionExtremoIzquierdo(), laser.posicionExtremoDerecho(),
				laser.posicionYArriba(), laser.posicionYAbajo()))
			this.estadoJuego = 0;
	}

	private void verificarImpactoRayoADino(Nodo<Velociraptor> nodoDino) {
		this.barbariana.getRayos().forEachNodo(nodoRayo -> {
			Proyectil rayo = nodoRayo.getElemento();
			if (nodoDino.getElemento().esImpactado(rayo.posicionExtremoIzquierdo(), rayo.posicionExtremoDerecho(),
					rayo.posicionYArriba(), rayo.posicionYAbajo())) {
				this.dinos.quitarPorId(nodoDino.getId());
				this.barbariana.getRayos().quitarPorId(nodoRayo.getId());
				this.eliminados++;
				this.puntos += 15;
			}
			return null;
		});
	}

	private void verificarImpactoRayoALaser(Nodo<Proyectil> nodoRayo) {
		this.lasers.forEachNodo(nodoLaser -> {
			Proyectil laser = nodoLaser.getElemento();
			if (nodoRayo.getElemento().esImpactado(laser.posicionExtremoIzquierdo(), laser.posicionExtremoDerecho(),
					laser.posicionYArriba(), laser.posicionYAbajo())) {
				this.lasers.quitarPorId(nodoLaser.getId());
				this.barbariana.getRayos().quitarPorId(nodoRayo.getId());
				this.puntos += 5;
			}
			return null;
		});
	}

	private void moverRayo(Nodo<Proyectil> nodoRayo) {
		nodoRayo.getElemento().moverse();
		nodoRayo.getElemento().dibujarse(this.entorno);
		if (!nodoRayo.getElemento().estaEnPantalla(this.entorno)) {
			this.barbariana.getRayos().quitarPorId(nodoRayo.getId());
		}
	}

	private void moverLaser(Nodo<Proyectil> nodoLaser, Lista<Proyectil> lista) {
		nodoLaser.getElemento().moverse();
		nodoLaser.getElemento().dibujarse(this.entorno);
		if (!nodoLaser.getElemento().estaEnPantalla(this.entorno)) {
			lista.quitarPorId(nodoLaser.getId());
		}
	}

	private void procesarRayos() {
		this.barbariana.getRayos().forEachNodo(rayo -> {
			this.moverRayo(rayo);
			this.verificarImpactoRayoALaser(rayo);
			return null;
		});
	}

	private void procesarLasers() {
		this.lasers.forEachNodo(nodoLaser -> {
			this.moverLaser(nodoLaser, this.lasers);
			this._verificarImpactoLaserAPersonaje(nodoLaser.getElemento());
			return null;
		});
	}

	private void procesarDinos() {
		this.dinos.forEachNodo(nodoDino -> {
			this.moverDino(nodoDino);
			this.verificarImpactoRayoADino(nodoDino);
			nodoDino.getElemento().disparar(this.lasers, this.contadorTicks);
			return null;
		});
	}

	private void moverDino(Nodo<Velociraptor> nodoDino) {
		Velociraptor dino = nodoDino.getElemento();
		if (!dino.colisionPiso(this.pisos))
			dino.gravedad(this);
		dino.avanzar(this.pisos);
		dino.dibujarse(this.entorno);
		if (!dino.estaEnPantalla(this.entorno)) {
			dinos.quitarPorId(nodoDino.getId());
		}
	}

	/**
	 * Ejecuta mensaje en pantalla y finaliza la ejecución.
	 */
	private void finDelJuego(String message, Color color) {
		entorno.cambiarFont(Font.SANS_SERIF, 30, color);
		entorno.escribirTexto(message, this.width / 2 - 50, this.height / 2);

		// ejecuta una funcion despues de determinado tiempo en ms (milisegundos)
		new java.util.Timer().schedule(new java.util.TimerTask() {
			public void run() {
				System.exit(0);
			}
		}, 4000);
	}

	// Getters and setters

	public Entorno getEntorno() {
		return entorno;
	}

	public void setEntorno(Entorno entorno) {
		this.entorno = entorno;
	}

	public Personaje getBarbariana() {
		return barbariana;
	}

	public void setBarbariana(Personaje barbariana) {
		this.barbariana = barbariana;
	}

	public Computadora getComputadora() {
		return computadora;
	}

	public void setComputadora(Computadora computadora) {
		this.computadora = computadora;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getCantPisos() {
		return cantPisos;
	}

	public void setCantPisos(int cantPisos) {
		this.cantPisos = cantPisos;
	}

	public Piso[] getPisos() {
		return pisos;
	}

	public void setPisos(Piso[] pisos) {
		this.pisos = pisos;
	}

	public int getContadorTicks() {
		return contadorTicks;
	}

	public void setContadorTicks(int contadorTicks) {
		this.contadorTicks = contadorTicks;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}


	public int getTickUltimoDino() {
		return tickUltimoDino;
	}

	public void setTickUltimoDino(int tickUltimoDino) {
		this.tickUltimoDino = tickUltimoDino;
	}

	public Lista<Velociraptor> getDinos() {
		return dinos;
	}

	public void setDinos(Lista<Velociraptor> dinos) {
		this.dinos = dinos;
	}

	public int getEstadoJuego() {
		return this.estadoJuego;
	}

	public void setEstadoJuego(int estado) {
		this.estadoJuego = estado;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
