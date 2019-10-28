package analizadorSintactico;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Error;

public class NodoArbol extends ParserVal{

	String nombre;
	String tipoDeDato;
	NodoArbol nodoIzq;
	NodoArbol nodoDer;
	
	
	public NodoArbol(String nombre, ParserVal nodoIzq, ParserVal nodoDer) {
		this.nombre = nombre;
		this.nodoIzq = (NodoArbol) nodoIzq;
		this.nodoDer = (NodoArbol) nodoDer;
	}
	
	public void setTipoDeDato(String tipoDeDato) { //AnalizadorLexico.TIPO_DATO_ENTERO; o AnalizadorLexico.TIPO_DATO_ULONG;
		this.tipoDeDato = tipoDeDato;
	}
	
	public void setTipoDeDato (NodoArbol nodo) {
		String tipoDeDato = nodo.getTipoDeDato();
		this.tipoDeDato = tipoDeDato;
	}
	
	public void setTipoDeDato(NodoArbol nodoIzq, NodoArbol nodoDer) {
		String tipoDeDatoIzq = nodoIzq.getTipoDeDato();
		String tipoDeDatoDer = nodoDer.getTipoDeDato();
		
		if (tipoDeDatoIzq == tipoDeDatoDer)
			this.tipoDeDato = tipoDeDatoIzq;
		else {
			Error error = new Error("ERROR", "Tipos de datos incompatibles", AnalizadorLexico.cantLineas);
			Parser.errores.add(error);
		}
	}
	
	public String getTipoDeDato() {
		return this.tipoDeDato;
	}
	
}
