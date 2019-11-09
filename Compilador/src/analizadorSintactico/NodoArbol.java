package analizadorSintactico;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Error;
import analizadorLexico.Token;

public class NodoArbol extends ParserVal{

	String nombre;
	String tipoDeDato;
	NodoArbol nodoIzq;
	NodoArbol nodoDer;
	Boolean esRegistro;
	
	
	public NodoArbol(String nombre, ParserVal nodoIzq, ParserVal nodoDer) {
		this.nombre = nombre;

		this.nodoIzq = nodoIzq != null ? (NodoArbol) nodoIzq : null;
		this.nodoDer = nodoDer != null ? (NodoArbol) nodoDer : null;
		esRegistro = false;
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
		
		if (tipoDeDatoIzq.equals(tipoDeDatoDer))
			this.tipoDeDato = tipoDeDatoIzq;
		else {
			Error error = new Error("ERROR", "Tipo de dato " + tipoDeDatoIzq + " no es compatible con " + tipoDeDatoDer, AnalizadorLexico.cantLineas);
			Parser.errores.add(error);
			this.tipoDeDato = Token.UNDEFINED;
		}
	}
	
	
	public void setTipoDeDato(String tipoDeDatoIzq, String tipoDeDatoDer) {
		
		if (tipoDeDatoIzq.equals(tipoDeDatoDer))
			this.tipoDeDato = tipoDeDatoIzq;
		else {
			Error error = new Error("ERROR", "Tipo de dato " + tipoDeDatoIzq + " no es compatible con " + tipoDeDatoDer, AnalizadorLexico.cantLineas);
			Parser.errores.add(error);
			this.tipoDeDato = Token.UNDEFINED;
		}
	}
	
	public String getTipoDeDato() {
		return this.tipoDeDato;
	}
	
	public NodoArbol getNodoDer() {
		return nodoDer;
	}
	
	public NodoArbol getNodoIzq() {
		return nodoIzq;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Boolean esRegistro() {
		return esRegistro;
	}
	
	public void reemplazar(String nom) {
		nodoDer = null;
		nodoIzq = null;
		nombre = nom;
		esRegistro = true;
	}
	
}
