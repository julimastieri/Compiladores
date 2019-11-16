package analizadorLexico;

//CADENAS
//Guarda la cadena en la TdeS

public class AccionSemantica6 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		String cadena = buffer.toString();
		String lexema = cadena.replace(" ", "");
		
		Token token = AnalizadorLexico.tablaSimbolos.get(lexema);
		if (token == null) { //si no esta en la TS
			Integer id= AnalizadorLexico.palabras_reservadas.get("cadena");
			token = new Token(cadena, AnalizadorLexico.TIPO_CADENA, id);
			System.out.println(cadena+"  "+lexema);
			
			AnalizadorLexico.tablaSimbolos.put(lexema, token);
			token.setTipoDeDato(AnalizadorLexico.TIPO_CADENA);
		} else {
			token.incrementarContadorDeReferencias();
		}
		
		return token;
		
	}

}
