package analizadorLexico;

public class AccionSemantica3 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, char c) {
		
		buffer.append(c);
		int ascii = (int) c;
		Token token = new Token(buffer.toString(), AnalizadorLexico.TIPO_OPERADOR, ascii);
		return token;
				
	}

}
