package analizadorLexico;

//CADENAS
//Guarda la cadena en la TdeS

public class AccionSemantica6 implements AccionSemantica{
	
	private static int nroCadena = 0;

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		String cadena = buffer.toString();
		String lexema = "cadena"+nroCadena;
		nroCadena++;
		
		Integer id= AnalizadorLexico.palabras_reservadas.get("cadena");
		Token token = new Token(cadena, AnalizadorLexico.TIPO_CADENA, id);
		AnalizadorLexico.tablaSimbolos.put(lexema, token);
		token.setTipoDeDato(AnalizadorLexico.TIPO_CADENA);
		
		return token;
	}

}
