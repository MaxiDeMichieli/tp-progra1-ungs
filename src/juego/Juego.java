package juego;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.function.Function;
import entorno.Herramientas;
import entorno.Entorno;
import entorno.InterfaceJuego;
import utils.Lista;
import utils.Nodo;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Personaje barbariana;
	private Computadora computadora;
	private Image avatarVida;

	// Ancho y alto del juego;
	private int height;
	private int width;

	private int cantPisos;
	private Piso[] pisos;

	private int contadorTicks;

	private int puntos; // score
	private int eliminados; // dinos eliminados
	private int vidas = 3;

	// 1 = Gano
	// 0 = Perdio
	private int estadoJuego = -1;

	private int tickUltimoDino;
	private Lista<Proyectil> rayos;
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
		this.rayos = new Lista<Proyectil>();

		this.avatarVida = Herramientas.cargarImagen("vida.png");

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

			// dibuja dinos
			this._dibujarDinos();

			// Se verifica el impacto del dino no solo cuando se mueve a un lado, sino
			// tambien
			// cuando esta quieto el personaje.
			this._verificarImpactoDinoAPersonaje();

			// metodo que maneja las acciones de acuerdo a la tecla que se presione en el
			// momento
			this._actualizarMovimientos();

			if (this.rayos.largo() > 0) {
				this.procesarRayos();
			}

			if (this.lasers.largo() > 0) {
				this.moverLasers();
			}

			if (this.barbariana.getSaltando()) {
				this.barbariana.procesarSalto(this.contadorTicks);
			}

			if (!this.barbariana.colisionPiso(this.pisos) && !this.barbariana.getSaltando()) {
				this.barbariana.gravedad(this);
			}

			entorno.cambiarFont(Font.SANS_SERIF, 25, Color.RED);
			entorno.escribirTexto("Enemigos Eliminados: " + this.eliminados, 15, 25);
			entorno.escribirTexto("Vidas: " + this.vidas, 80, 585);
			entorno.escribirTexto("Score: " + this.puntos, this.width - 150, 585);
			entorno.dibujarRectangulo(40, 570, 50, 50, 0, Color.WHITE);
			entorno.dibujarImagen(this.avatarVida, 40, 570, 0, 0.2);

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
			if (this.computadora.estaTocando(this.barbariana.getX(), this.barbariana.getY())) {
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
			this.barbariana.disparar(this.rayos, this.contadorTicks);
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

	private void _dibujarDinos() {
		if (this.tickUltimoDino == -1 || (this.dinos.largo() <= 6 && this.tickUltimoDino + 300 < this.contadorTicks)) {
			this.dinos.agregarAtras(new Velociraptor());
			this.tickUltimoDino = this.contadorTicks;
		}
		this.procesarDinos();
	}

	private void _dibujarPisos() {
		for (Piso piso : this.pisos) {
			piso.dibujarse(this.entorno);
		}
	}

	private Function<Velociraptor, Void> verificarImpactoDinoAPersonajeFunc = dino -> {
		if (this.barbariana.esImpactado(dino.posicionExtremoIzquierdo(), dino.posicionExtremoDerecho(),
				dino.posicionCabeza(), dino.posicionPies())) {
			this.estadoJuego = 0;
		}
		return null;
	};

	private void _verificarImpactoDinoAPersonaje() {
		this.dinos.forEachElement(this.verificarImpactoDinoAPersonajeFunc);
	}

	private void _verificarImpactoLaserAPersonaje(Proyectil laser) {
		if (this.barbariana.esImpactado(laser.posicionExtremoIzquierdo(), laser.posicionExtremoDerecho(),
				laser.posicionYArriba(), laser.posicionYAbajo()))
			this.estadoJuego = 0;
	}

	private void verificarImpactoADino(Nodo<Velociraptor> dino) {
		Function<Nodo<Proyectil>, Void> verificarImpactoARayosFunc = nodoRayo -> {
			Proyectil rayo = nodoRayo.getElemento();
			if (dino.getElemento().esImpactado(rayo.posicionExtremoIzquierdo(), rayo.posicionExtremoDerecho(),
					rayo.posicionYArriba(), rayo.posicionYAbajo())) {
				this.dinos.quitarPorId(dino.getId());
				this.rayos.quitarPorId(nodoRayo.getId());
				this.eliminados++;
				this.puntos += 15;
			}
			return null;
		};
		this.rayos.forEachNodo(verificarImpactoARayosFunc);
	}

	private void verificarImpactoALaser(Nodo<Proyectil> rayo) {
		Function<Nodo<Proyectil>, Void> verificarImpactoARayosFunc = nodoLaser -> {
			Proyectil laser = nodoLaser.getElemento();
			if (rayo.getElemento().esImpactado(laser.posicionExtremoIzquierdo(), laser.posicionExtremoDerecho(),
					laser.posicionYArriba(), laser.posicionYAbajo())) {
				this.lasers.quitarPorId(nodoLaser.getId());
				this.rayos.quitarPorId(rayo.getId());
				this.puntos += 5;
			}
			return null;
		};
		this.lasers.forEachNodo(verificarImpactoARayosFunc);
	}

	private void moverRayo(Nodo<Proyectil> rayo) {
		rayo.getElemento().moverse();
		rayo.getElemento().dibujarse(this.entorno);
		if (!rayo.getElemento().estaEnPantalla(this.entorno)) {
			rayos.quitarPorId(rayo.getId());
		}
	}

	private void moverLaser(Nodo<Proyectil> laser) {
		laser.getElemento().moverse();
		laser.getElemento().dibujarse(this.entorno);
		if (!laser.getElemento().estaEnPantalla(this.entorno)) {
			lasers.quitarPorId(laser.getId());
		}
	}

	private void procesarRayos() {
		this.rayos.forEachNodo(this.procesarRayoFunc);
	}

	private Function<Nodo<Proyectil>, Void> procesarRayoFunc = rayo -> {
		this.moverRayo(rayo);
		this.verificarImpactoALaser(rayo);
		return null;
	};

	private void moverLasers() {
		this.lasers.forEachNodo(this.moverLaserFunc);
	}

	private Function<Nodo<Proyectil>, Void> moverLaserFunc = laser -> {
		this.moverLaser(laser);
		this._verificarImpactoLaserAPersonaje(laser.getElemento());
		return null;
	};

	private void moverDino(Nodo<Velociraptor> dino) {
		if (!dino.getElemento().colisionPiso(this.pisos))
			dino.getElemento().gravedad(this);
		dino.getElemento().avanzar(this.pisos);
		dino.getElemento().dibujarse(this.entorno);
		if (!dino.getElemento().estaEnPantalla(this.entorno)) {
			dinos.quitarPorId(dino.getId());
		}
	}

	private void procesarDinos() {
		this.dinos.forEachNodo(this.procesarDinoFunc);
	}

	private Function<Nodo<Velociraptor>, Void> procesarDinoFunc = dino -> {
		this.moverDino(dino);
		this.verificarImpactoADino(dino);
		dino.getElemento().disparar(this.lasers, this.contadorTicks);
		return null;
	};

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

	// Getter and setters

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

	public Image getAvatarVida() {
		return avatarVida;
	}

	public void setAvatarVida(Image avatarVida) {
		this.avatarVida = avatarVida;
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

	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}

	public Lista<Proyectil> getRayos() {
		return rayos;
	}

	public void setRayos(Lista<Proyectil> rayos) {
		this.rayos = rayos;
	}

	public Function<Nodo<Proyectil>, Void> getProcesarRayoFunc() {
		return procesarRayoFunc;
	}

	public void setProcesarRayoFunc(Function<Nodo<Proyectil>, Void> procesarRayoFunc) {
		this.procesarRayoFunc = procesarRayoFunc;
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

	public Function<Nodo<Velociraptor>, Void> getProcesarDinoFunc() {
		return procesarDinoFunc;
	}

	public void setProcesarDinoFunc(Function<Nodo<Velociraptor>, Void> procesarDinoFunc) {
		this.procesarDinoFunc = procesarDinoFunc;
	}

	public Lista<Proyectil> getLasers() {
		return lasers;
	}

	public void setLasers(Lista<Proyectil> lasers) {
		this.lasers = lasers;
	}

	public Function<Nodo<Proyectil>, Void> getMoverLaserFunc() {
		return moverLaserFunc;
	}

	public void setMoverLaserFunc(Function<Nodo<Proyectil>, Void> moverLaserFunc) {
		this.moverLaserFunc = moverLaserFunc;
	}

	public Function<Velociraptor, Void> getVerificarImpactoDinoAPersonajeFunc() {
		return verificarImpactoDinoAPersonajeFunc;
	}

	public void setVerificarImpactoDinoAPersonajeFunc(Function<Velociraptor, Void> verificarImpactoDinoAPersonajeFunc) {
		this.verificarImpactoDinoAPersonajeFunc = verificarImpactoDinoAPersonajeFunc;
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
