package analizadorLexico;

public class AccionSemantica8 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, char c) {
		
		buffer.setLength(0);
		Error e = new Error("ERROR", "Token invalido", AnalizadorLexico.cantLineas);
		AnalizadorLexico.errores.add(e);
		return null;
	}

}
