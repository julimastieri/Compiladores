package analizadorLexico;

public class AccionSemantica1 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, char c) {
		buffer.append(c);
		return null;
	}

}
