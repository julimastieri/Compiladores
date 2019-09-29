package analizadorLexico;

public class Token {
	
	String lexema;
	String tipo;
	String tipoDeDato;
	int id; 
	
	public Token(String l, String t, int id) {
		this.id = id;
		lexema = l;
		tipo = t;
		tipoDeDato = "";
	}
	
	public String getTipo() {
		return tipo;
	}

	
	public String getLexema() {
		return lexema;
	}
	
	public String getTipoDeDato() {
		return tipoDeDato;
	}
	
	public void setTipoDeDato(String tdd) {
		tipoDeDato = tdd;
	}
}
