package analizadorLexico;

public class Token {
	
	String lexema;
	String tipo;
	int id; 
	
	public Token(String l, String t, int id) {
		this.id = id;
		lexema = l;
		tipo = t;
	}
	
	public String getTipo() {
		return tipo;
	}

}
