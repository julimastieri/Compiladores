package analizadorLexico;

import java.util.ArrayList;

public class Token {
	
	public static final String UNDEFINED = "no definido";
	public static final String USO_COLECCION = "Nombre de coleccion";
	public static final String USO_VARIABLE = "Variable";
	public static final String USO_VARIABLE_AUX = "Variable Auxiliar";
	public static final String USO_CONSTANTE = "Constante";
	public static final String USO_CADENA = "Cadena";
	public static final String USO_REF_MEM = "Referencia a memoria";
	
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
		tipoDeDato = UNDEFINED;
		uso = UNDEFINED;
		tamanio = 1;
		valoresIniciales = new ArrayList<String>();
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
		
		for (int i =0; i<valores.size(); i++) {
			v = valores.get(i);
			if (v != "_") {
				valoresIniciales.add(v);
			}else {
				valoresIniciales.add("_");
			}
		}
	}
	
	public String getValorInicial(int pos) {
		if( ( pos >= 0) && (pos < valoresIniciales.size()) ) {
			return valoresIniciales.get(pos);
		} 
		return null;
	}
	
	public int getCantidadValoresIniciales() {
		return valoresIniciales.size();
	}
	
}
