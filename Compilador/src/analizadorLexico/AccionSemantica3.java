package analizadorLexico;

//Concatena el operador y devuelve ASCII
//Retorna un token

public class AccionSemantica3 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, char c) {
		
		buffer.append(c);
		int ascii = (int) c;
		Token token = new Token(buffer.toString(), AnalizadorLexico.TIPO_OPERADOR, ascii);
		return token;
				
	}

}
