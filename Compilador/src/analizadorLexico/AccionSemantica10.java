package analizadorLexico;

//Mensaje de error cdo del estado 0 se lee "_", "}" u otro.

public class AccionSemantica10 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, Character c) {
		String mensajeError = "Caracter \""+ c + "\" invalido";
		Error error = new Error("ERROR", mensajeError, AnalizadorLexico.cantLineas);
   	 	AnalizadorLexico.errores.add(error);
		return null;
	}
}
