package analizadorLexico;

//CADENA MULTILINEA
//Error

public class AccionSemantica9 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		buffer.setLength(0);
		Error e = new Error("ERROR", "Falta cerrar cadena multilinea con '}' ", AnalizadorLexico.cantLineas);
		AnalizadorLexico.errores.add(e);
		return null;
	}

}
