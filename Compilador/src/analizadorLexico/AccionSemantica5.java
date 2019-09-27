package analizadorLexico;

//Concatena y busca en las palabras_reservadas 
//Para ASIGNACION

public class AccionSemantica5 implements AccionSemantica {

	public Token ejecutar(StringBuilder buffer, Character c) {
		
		buffer.append(c);
		String lexema = buffer.toString();
		int id = AnalizadorLexico.palabras_reservadas.get(lexema);
		Token token = new Token(lexema, AnalizadorLexico.TIPO_OPERADOR, id);
		
		return token;

	}

}
