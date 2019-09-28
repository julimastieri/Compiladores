package analizadorLexico;

import java.io.IOException;

//No concatena y devuelve ASCII
//Retorna token (< , >)

public class AccionSemantica7 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		 try {
			AnalizadorLexico.fm.unread(c); //Devuelvo el caracter al buffer
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int ascii = (int) buffer.charAt(0);
		Token token = new Token(buffer.toString(), AnalizadorLexico.TIPO_OPERADOR, ascii);
		
		return token;
		
	}

}
