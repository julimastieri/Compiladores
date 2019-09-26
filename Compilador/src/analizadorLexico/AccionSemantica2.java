package analizadorLexico;


public class AccionSemantica2 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, char c) {
		
		 String lexema = buffer.toString();
	     Token token = AnalizadorLexico.tablaSimbolos.get(lexema);
	     
	     if ( token != null) //Si esta
	    	 return token;
	     
	     else { //si no esta
	    	 
			if (lexema.length() <= 25) {
				token = new Token(lexema, "identificador", AnalizadorLexico.ID);
				
			}else { //mayor a 25
				Error error = new Error("Warning", "identificador supera los 25 caracteres", AnalizadorLexico.cantLineas);
				AnalizadorLexico.errores.add(error);
				
				lexema = buffer.subSequence(0, 25).toString();
				token = new Token(lexema, "identificador", AnalizadorLexico.ID);
				
			}
			AnalizadorLexico.tablaSimbolos.put(lexema, token);
			return token;
		}
	     
	}

}
