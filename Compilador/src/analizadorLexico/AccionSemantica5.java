package analizadorLexico;


public class AccionSemantica5 implements AccionSemantica {

	public Token ejecutar(StringBuilder buffer, char c) {
		
		buffer.append(c);
		String lexema = buffer.toString();
		int id = AnalizadorLexico.id_tokens.get(lexema);
		Token token = new Token(lexema, AnalizadorLexico.TIPO_OPERADOR, id);
		
		return token;

	}

}
