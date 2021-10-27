package utils;

public class Lista<T> {
	Nodo<T> primero;

	public Lista() {
		this.primero = null;
	}

	void mostrar() {
		Nodo<T> actual = this.primero;
		System.out.print("[");
		while (actual != null) {
			System.out.print(actual.elemento + " ");
			actual = actual.siguiente;
		}
		System.out.println("]");
	}

	void agregarAtras(T n) {
		Nodo<T> nuevo = new Nodo<T>(n);
		if (this.primero == null) {
			this.primero = nuevo;
		} else {
			Nodo<T> actual = this.primero;
			while (actual.siguiente != null) {
				actual = actual.siguiente;
			}
			actual.siguiente = nuevo;
		}
	}

	void agregarAdelante(T n) {
		Nodo<T> nuevo = new Nodo<T>(n);
		if (this.primero == null) {
			this.primero = nuevo;
		} else {
			nuevo.siguiente = this.primero;
			this.primero = nuevo;
		}
	}

	int largo() {
		Nodo<T> actual = this.primero;
		int cant = 0;
		while (actual != null) {
			cant++;
			actual = actual.siguiente;
		}
		return cant;
	}

	void quitarDePosicion(int pos) {
		Nodo<T> actual = this.primero;
		if (this.primero != null && pos == 0) {
			this.primero = this.primero.siguiente;
		}
		if (pos >= this.largo())
			return;
		int i = 1;
		while (actual != null && actual.siguiente != null) {
			if (i == pos) {
				actual.siguiente = actual.siguiente.siguiente;
				return;
			}
			actual = actual.siguiente;
			i++;
		}
	}
	
	T obtenerDePosicion(int pos) {
		Nodo<T> actual = this.primero;
		int i = 0;
		while (actual != null && actual.siguiente != null) {
			if (i == pos) {
				return actual.elemento;
			}
			actual = actual.siguiente;
			i++;
		}
		return null;
	}
}
