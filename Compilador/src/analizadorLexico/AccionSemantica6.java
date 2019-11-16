package analizadorLexico;

//CADENAS
//Guarda la cadena en la TdeS

public class AccionSemantica6 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		String lexema = buffer.toString();
		lexema = lexema.replace(" ", "_");
		System.out.println(lexema);
		Integer id= AnalizadorLexico.palabras_reservadas.get("cadena");
		Token token = new Token(lexema, AnalizadorLexico.TIPO_CADENA, id);
		AnalizadorLexico.tablaSimbolos.put(lexema, token);
		return token;
		
	}

}
