package analizadorLexico;

public class AccionSemantica6 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, char c) {
		
		String lexema = buffer.toString();
		Token token = new Token(lexema, AnalizadorLexico.TIPO_CADENA, AnalizadorLexico.CADENA);
		AnalizadorLexico.tablaSimbolos.put(lexema, token);
		return token;
		
	}

}
