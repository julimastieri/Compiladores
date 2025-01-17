package analizadorLexico;

import java.io.IOException;

//Para cuando tengo ':' solo o '=' solo
//Crea error token invalido

public class AccionSemantica8 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		 try {
			AnalizadorLexico.fm.unread(c); //Devuelvo el caracter al buffer y resto 1 si fue un nl
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		buffer.setLength(0);
		Error e = new Error("ERROR", "Se esperaba un '=' ", AnalizadorLexico.cantLineas);
		AnalizadorLexico.errores.add(e);
		return null;
	}

}
