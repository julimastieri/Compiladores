package analizadorLexico;

public class AccionSemantica7 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, char c) {
		
		String lexema = buffer.toString();
		return AnalizadorLexico.tablaSimbolos.get(lexema);
		
	}

}
