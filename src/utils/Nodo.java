package utils;

public class Nodo<T> {
	T elemento;
	Nodo<T> siguiente;
	
	public Nodo() {}
	
	public Nodo(T elemento) {
		this.elemento = elemento;
		this.siguiente = null;
	}
	
}
