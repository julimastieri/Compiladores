package analizadorLexico;

public interface AccionSemantica {
	
	 public Token ejecutar (StringBuilder buffer, char c);
}
