package juego;
import java.awt.Color;

import entorno.Entorno;

public class Personaje {
	private String name;
	private int x, y;
	private int ancho, alto;
	private String imagen = "./barbariana.jpeg";
	
	Personaje(Juego instanceJuego, String name){
		this.x = instanceJuego.getExtremoIzquierdo();
		this.y = instanceJuego.getExtremoInferior();
		this.ancho = 30;
		this.alto = 30;
		this.name = name;
	}
	
	public void moverIzquierda(Juego j){
		if(this.x == j.getExtremoIzquierdo()) return;
		this.x = this.x - 5;
	}
	
	public void moverDerecha(Juego j) {
		if(this.x == j.getExtremoDerecho()) return;
		this.x = this.x + 5;
	}
	
	public void gravedad(Juego j) {
		if(this.y == j.getExtremoInferior()) return;
		this.y+=5;
	}
	public void saltar(Juego j) {
		if(this.y <= j.getExtremoSuperior()) return;
		this.y -= 20;
	}
	
	public void agachar() {
		
	}
	
	public void dibujarse(Entorno e) {
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.GREEN);
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
}
