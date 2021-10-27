package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Personaje barbariana;
	private Piso instancePisos;
	
	//ancho y alto del juego;
	private int heigth;
	private int width;
	private int extremoSuperior;
	private int extremoInferior;
	private int extremoDerecho;
	private int extremoIzquierdo;
	
	private boolean bloquearSalto=false;
	private int cantPisos;
	private Piso[] pisos;

	Juego(){
		//Inicializo los extremos
		this._setearExtremos();
		
		this.cantPisos = 4;
		
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Boss Rabbit Rabber - Grupo 3 - v1", this.heigth, this.width);
		
		//Inicializa el personaje
		this.barbariana = new Personaje(this, "Barbariana");
	
		// crea los pisos con sus respectivas ubicaciones
		this._inicializarPisos();
		
		// Inicia el juego!
		this.entorno.iniciar();
	}

	// Metodo que se ejecuta todo el tiempo
	public void tick(){
		
		//dibujar el personaje
		this.barbariana.dibujarse(entorno);
		
		//dibuja los pisos
		this._dibujarPisos();
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
			this.barbariana.moverDerecha(this);
		}
		if(this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
			this.barbariana.moverIzquierda(this);
		}
		//si presiono flecha arriba y el bloquearSalto=false: salta y bloquea el salto
		if (this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA) && !this.bloquearSalto) {
			this.barbariana.saltar(this);			
			this.bloquearSalto=true;
		}
		//si no esta presionando arriba, el salto esta bloqueado y esta tocando el piso: lo desbloquea
		if (!this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA) && this.bloquearSalto && this.barbariana.colisionPiso()) {
			this.bloquearSalto=false;				
		}
		
		if (this.entorno.estaPresionada(this.entorno.TECLA_ABAJO)) {
			this.barbariana.agachar();
		}
		this.barbariana.gravedad(this);
	}
	
	
	private void _setearExtremos() {
		this.heigth = 800;
		this.width = 600;
		this.extremoSuperior = 15;
		this.extremoIzquierdo = 20;
		this.extremoInferior = this.heigth - 225;
		this.extremoDerecho = this.width + 190;
	}
	
	private void _inicializarPisos() {
		Piso[] pisosList = new Piso[this.cantPisos];
		int xPiso = this.extremoIzquierdo;
		int yPiso = this.extremoInferior + 15;
		int anchoPiso = 2000;
		for(int i=0; i < this.cantPisos; i++) {
			Piso p = new Piso(xPiso,yPiso,anchoPiso,1);
			pisosList[i] = p;
			yPiso = yPiso - 150;
			xPiso = xPiso == this.extremoDerecho ? this.extremoIzquierdo : this.extremoDerecho;
			anchoPiso = this.width * 2;
		}
		this.pisos = pisosList;
	}
	
	private void _dibujarPisos() {
		for(Piso piso : this.pisos) {
			piso.dibujarse(entorno);
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
	
	public int getExtremoSuperior() {
		return extremoSuperior;
	}
	
	public void setExtremoSuperior(int extremoSuperior) {
		this.extremoSuperior = extremoSuperior;
	}
	
	
	public int getExtremoInferior() {
		return extremoInferior;
	}
	
	
	public void setExtremoInferior(int extremoInferior) {
		this.extremoInferior = extremoInferior;
	}
	
	
	public int getExtremoDerecho() {
		return extremoDerecho;
	}
	
	
	public void setExtremoDerecho(int extremoDerecho) {
		this.extremoDerecho = extremoDerecho;
	}
	
	
	public int getExtremoIzquierdo() {
		return extremoIzquierdo;
	}
	
	
	public void setExtremoIzquierdo(int extremoIzquierdo) {
		this.extremoIzquierdo = extremoIzquierdo;
	}
	
	public int getHeigth() {
		return heigth;
	}
	
	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args){
		Juego juego = new Juego();
	}
}
