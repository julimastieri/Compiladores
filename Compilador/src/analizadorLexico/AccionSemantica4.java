package analizadorLexico;


public class AccionSemantica4 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, char c) {
		
		long num = Long.valueOf(buffer.toString());
		String lexema = buffer.toString();
	    Token token = AnalizadorLexico.tablaSimbolos.get(lexema);
	    
		if (num <= AnalizadorLexico.MAX_INT) { //si es constante
			
		    if (token != null) //si ya esta
		    	return token;
		    else {
		    	token = new Token(lexema, AnalizadorLexico.TIPO_CTE_ENTERA, AnalizadorLexico.CTE);
		    	AnalizadorLexico.tablaSimbolos.put(lexema, token);
		    	return token;
		    }
			
		}else if (num <= AnalizadorLexico.MAX_LONG) { //si es ulong
			
			if (token != null) //si ya esta
		    	return token;
		    else {
		    	token = new Token(lexema, AnalizadorLexico.TIPO_CTE_ULONG, AnalizadorLexico.CTE);
		    	AnalizadorLexico.tablaSimbolos.put(lexema, token);
		    	return token;
		    }
			
		} else {
			Error error = new Error("ERROR", "constante fuera de rango", AnalizadorLexico.cantLineas);
			AnalizadorLexico.errores.add(error);
			lexema = "" + AnalizadorLexico.MAX_LONG;
			token = new Token(lexema, AnalizadorLexico.TIPO_CTE_ULONG, AnalizadorLexico.CTE);
			return token;
		}
		
	}

}
