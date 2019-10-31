package analizadorLexico;

import java.util.ArrayList;
import java.util.List;

public class Token {
	
	String lexema;
	String tipoDeToken;
	int id;
	int contadorDeReferencias;
	String tipoDeDato, uso;
	int tamanio;
	ArrayList<String> valoresIniciales;
	
	public Token(String l, String t, int id) {
		this.id = id;
		lexema = l;
		tipoDeToken = t;
		contadorDeReferencias = 1;
		tipoDeDato = "no definido";
		uso = "no definido";
		tamanio = 1;
	}
	
	public String getTipoDeToken() {
		return tipoDeToken;
	}

	public String getLexema() {
		return lexema;
	}
	
	public int getId() {
		return id;
	}
	
	public int getContadorDeReferencias() {
		return contadorDeReferencias;
	}
	
	public void incrementarContadorDeReferencias() {
		this.contadorDeReferencias++;
	}
	
	public void decrementarContadorDeReferencias() {
		this.contadorDeReferencias--;
	}
	
	public void setTipoDeDato(String tdd) {
		tipoDeDato = tdd;
	}
	
	public String getTipoDeDato() {
		return tipoDeDato;
	}
	
	public void setUso(String u) {
		uso = u;
	}
	
	public String getUso() {
		return uso;
	}
	
	public void setTamanio(int tam) {
		tamanio = tam;
	}
	
	public int getTamanio() {
		return tamanio;
	}

	public void setValoresIniciales(ArrayList<String> valores) {
		String v;
		valoresIniciales = new ArrayList<String>();
		
		for (int i =0; i<valores.size(); i++) {
			v = valores.get(i);
			if (v != "_") {
				valoresIniciales.add(v);
			}else {
				valoresIniciales.add("");
			}
		}
	}
	
	public String getValorInicial(int pos) {
		if( ( pos > 0) && (pos < valoresIniciales.size()) ) {
			return valoresIniciales.get(pos);
		} 
		return null;
	}
	
}
