package analizadorLexico;


public class AccionSemantica5 implements AccionSemantica {

	public Token ejecutar(StringBuilder buffer, char c) {
		
		buffer.append(c);
		String lexema = buffer.toString();
		
		return AnalizadorLexico.tablaSimbolos.get(lexema);

	}

}
