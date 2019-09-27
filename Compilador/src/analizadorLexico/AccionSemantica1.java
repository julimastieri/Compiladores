package analizadorLexico;

public class AccionSemantica1 implements AccionSemantica{

	public Token ejecutar(StringBuilder buffer, Character c) {
		buffer.append(c);
		return null;
	}

}
