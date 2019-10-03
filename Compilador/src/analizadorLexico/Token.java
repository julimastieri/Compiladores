package analizadorLexico;

public class Token {
	
	String lexema;
	String tipoDeToken;
	int id;
	int contadorDeReferencias;
	
	public Token(String l, String t, int id) {
		this.id = id;
		lexema = l;
		tipoDeToken = t;
		contadorDeReferencias = 1;
	}
	
	public String getTipoDeToken() {
		return tipoDeToken;
	}

	
	public String getLexema() {
		return lexema;
	}
	
	public void incrementarContadorDeReferencias() {
		this.contadorDeReferencias++;
	}

	
}
