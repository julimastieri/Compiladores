package analizadorLexico;

import java.io.IOException;

//Para identificadores y palabras reservas.
//Retorna token

public class AccionSemantica2 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		 String lexema = buffer.toString();
		 Integer id_pr = AnalizadorLexico.palabras_reservadas.get(lexema);
		 Token token;
		 
		 try {
			AnalizadorLexico.fm.unread(c); //Devuelvo el caracter al buffer
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		 if (id_pr != null) { //encontró palabra reservada
			 token = new Token(lexema, AnalizadorLexico.TIPO_PALABRA_RESERVADA, id_pr);
			 return token;
		 
		 }else { //si no es palabra reservada
			 
			 token = AnalizadorLexico.tablaSimbolos.get(lexema); //busca en TdeS
			 
			 if ( token != null) { //Si esta en TdeS
		    	 token.incrementarContadorDeReferencias();
				 return token;
			 }		    	 
		     
		     else { //si no esta
		    	 
		    	Integer id_identif = AnalizadorLexico.palabras_reservadas.get("id");
		    	
				if (lexema.length() <= 25) {
					
					token = new Token(lexema, AnalizadorLexico.TIPO_ID, id_identif );
					
				}else { //mayor a 25
					
					Error error = new Error("Warning", "identificador supera los 25 caracteres", AnalizadorLexico.cantLineas);
					AnalizadorLexico.errores.add(error);
					
					lexema = buffer.subSequence(0, 25).toString();
					token = new Token(lexema,AnalizadorLexico.TIPO_ID, id_identif);
					
				}
				AnalizadorLexico.tablaSimbolos.put(lexema, token);
				return token;
			}
			 
		 }
			 
		 
		 
		 
	     
	     
	    
	     
	}

}
