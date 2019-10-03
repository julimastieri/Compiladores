package analizadorLexico;

public class Token {
	
	String lexema;
	String tipoDeToken;
	int id; 
	
	public Token(String l, String t, int id) {
		this.id = id;
		lexema = l;
		tipoDeToken = t;

	}
	
	public String getTipoDeToken() {
		return tipoDeToken;
	}

	
	public String getLexema() {
		return lexema;
	}

	
}
