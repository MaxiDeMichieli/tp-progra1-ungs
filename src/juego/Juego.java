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

	private int puntos;
	private int vidas = 3;

	private Lista<Proyectil> rayos;
	private Function<Nodo<Proyectil>, Void> moverRayoFunc = rayo -> {
		this.moverRayo(rayo);
		return null;
	};

	private int tickUltimoDino;
	private Lista<Velociraptor> dinos;
	private Function<Nodo<Velociraptor>, Void> procesarDinoFunc = dino -> {
		this.moverDino(dino);
		this.verificarImpactoADino(dino);
		this.dinos.mostrar();
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
		this.rayos = new Lista<Proyectil>();

		this.avatarVida = Herramientas.cargarImagen("vida.png");

		// Inicializa dinos
		this.dinos = new Lista<Velociraptor>();
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
		this._verificarImpactoAPersonaje();

		// metodo que maneja las acciones de acuerdo a la tecla que se presione en el
		// momento
		this._actualizarMovimientos();

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
		entorno.dibujarRectangulo(40, 570, 50, 50, 0, Color.RED);
		entorno.dibujarImagen(this.avatarVida, 40, 570, 0, 0.2);

		this.contadorTicks += 1;
	}

	/**
	 * De acuerdo a una tecla presioanda, ejecuta si accion correspondiente
	 */
	private void _actualizarMovimientos() {
		if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
			// chequea que barbariana no llegue a la computadora.
			// Si llega, termina el juego y gana
			if (this.computadora.estaTocando(this.barbariana.getX(), this.barbariana.getY())) {
				System.out.print("Ganaste!");
				System.exit(0);
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

	private Function<Velociraptor, Void> verificarImpactoAPersonajeFunc = dino -> {
		if (this.barbariana.esImpactado(dino.posicionExtremoIzquierdo(), dino.posicionExtremoDerecho(),
				dino.posicionCabeza(), dino.posicionPies())) {
			System.exit(0);
		}
		return null;
	};

	private void _verificarImpactoAPersonaje() {
		this.dinos.forEachElement(this.verificarImpactoAPersonajeFunc);
	}

	private void verificarImpactoADino(Nodo<Velociraptor> dino) {
		Function<Nodo<Proyectil>, Void> verificarImpactoARayosFunc = nodoRayo -> {
			Proyectil rayo = nodoRayo.getElemento();
			if (dino.getElemento().esImpactado(rayo.posicionExtremoIzquierdo(), rayo.posicionExtremoDerecho(),
					rayo.posicionYArriba(), rayo.posicionYAbajo())) {
				this.dinos.quitarPorId(dino.getId());
				this.rayos.quitarPorId(nodoRayo.getId());
				this.puntos += 1;
			}
			return null;
		};
		this.rayos.forEachNodo(verificarImpactoARayosFunc);
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

	private void moverRayo(Nodo<Proyectil> rayo) {
		rayo.getElemento().moverse();
		rayo.getElemento().dibujarse(this.entorno);
		if (!rayo.getElemento().estaEnPantalla(this.entorno)) {
			rayos.quitarPorId(rayo.getId());
		}
	}

	private void moverRayos() {
		this.rayos.forEachNodo(this.moverRayoFunc);
	}

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

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
