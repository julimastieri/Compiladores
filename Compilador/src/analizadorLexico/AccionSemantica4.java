package analizadorLexico;

import java.io.IOException;

//Reconoce ctes y ulong. Verifica rango.
//Devuelve token

public class AccionSemantica4 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		long num = Long.valueOf(buffer.toString());
		String lexema = buffer.toString();
	    Token token = AnalizadorLexico.tablaSimbolos.get(lexema);
	    Integer id;
	    
		 try {
			AnalizadorLexico.fm.unread(c); //Devuelvo el caracter al buffer
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		if (num <= AnalizadorLexico.MAX_INT) { //si es constante
			
		    if (token != null) { //si ya esta
		    	token.incrementarContadorDeReferencias();
		    }
		    else {
		    	id = AnalizadorLexico.palabras_reservadas.get("cte");
		    	token = new Token(lexema, AnalizadorLexico.TIPO_CTE, id);
		    	AnalizadorLexico.tablaSimbolos.put(lexema, token);
		    }
		    return token;
			
		}else if (num <= AnalizadorLexico.MAX_LONG) { //si es ulong
			
			if (token != null) { //si ya esta
		    	token.incrementarContadorDeReferencias();
		    }
		    else {
		    	id = AnalizadorLexico.palabras_reservadas.get("cte");
		    	token = new Token(lexema, AnalizadorLexico.TIPO_CTE, id);
		    	AnalizadorLexico.tablaSimbolos.put(lexema, token);
		    }
			
			return token;
			
		} else {
			Error error = new Error("ERROR", "Constante fuera de rango. Fue reemplazado por el valor limite permitido del rango", AnalizadorLexico.cantLineas);
			AnalizadorLexico.errores.add(error);
			lexema = "" + AnalizadorLexico.MAX_LONG;
			
			if (token != null) {//si ya esta
				token.incrementarContadorDeReferencias();
			}
			else {
				id = AnalizadorLexico.palabras_reservadas.get("cte");
				token = new Token(lexema, AnalizadorLexico.TIPO_CTE, id);
				AnalizadorLexico.tablaSimbolos.put(lexema, token);
			}
			return token;
		}
		
	}

}
