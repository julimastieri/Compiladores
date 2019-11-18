package analizadorSintactico;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Error;
import analizadorLexico.Token;

public class NodoArbol extends ParserVal{

	private String nombre;
	private String tipoDeDato;
	private NodoArbol nodoIzq;
	private NodoArbol nodoDer;
	private Boolean esRegistro;
	private int nroReg;
	private Boolean esRefMem;
	private int nroIdentificador;
	
	
	public NodoArbol(String nombre, ParserVal nodoIzq, ParserVal nodoDer) {
		this.nombre = nombre;

		this.nodoIzq = nodoIzq != null ? (NodoArbol) nodoIzq : null;
		this.nodoDer = nodoDer != null ? (NodoArbol) nodoDer : null;
		esRegistro = false;
		esRefMem = false;
		nroIdentificador = 0;
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
		
		if (!tipoDeDatoIzq.equals(Token.UNDEFINED) && !(tipoDeDatoDer.equals(Token.UNDEFINED)))
			if (tipoDeDatoIzq.equals(tipoDeDatoDer))
				this.tipoDeDato = tipoDeDatoIzq;
			else {
				Error error = new Error("ERROR", "Tipo de dato " + tipoDeDatoIzq + " no es compatible con " + tipoDeDatoDer, AnalizadorLexico.cantLineas);
				Parser.errores.add(error);
				this.tipoDeDato = Token.UNDEFINED;
			}
	}
	
	
	public void setTipoDeDato(String tipoDeDatoIzq, String tipoDeDatoDer) {
		
		if (!tipoDeDatoIzq.equals(Token.UNDEFINED) && !(tipoDeDatoDer.equals(Token.UNDEFINED)))
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
	
	public void reemplazar(String nom) { //Reemplaza por un nombre indicado (variable, etc)
		nodoDer = null;
		nodoIzq = null;
		nombre = nom;
	}
	
	public void reemplazar(String nom, int num) { //Reemplaza por un registro
		nodoDer = null;
		nodoIzq = null;
		nombre = nom;
		nroReg = num;
		esRegistro = true;
	}
	
	public int getNroReg() {
		return nroReg;
	}
	
	public void setNroReg(int n) {
		nroReg = n;
	}
	
	public boolean esRefMem() {	
		return esRefMem;
	}
	
	public void setEsRefMem(int nroRegistro) {
		nroReg = nroRegistro;
		esRefMem = true;
	}
	
	public void setNroIdentificador(int nro) {
		nroIdentificador = nro;
	}
	
	public int getNroIdentificador() {
		return nroIdentificador;
	}
	
}
