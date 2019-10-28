package analizadorLexico;

public class Token {
	
	String lexema;
	String tipoDeToken;
	int id;
	int contadorDeReferencias;
	String tipoDeDato, uso;
	
	public Token(String l, String t, int id) {
		this.id = id;
		lexema = l;
		tipoDeToken = t;
		contadorDeReferencias = 1;
		tipoDeDato = "no definido";
		uso = "no definido";
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

	
}
