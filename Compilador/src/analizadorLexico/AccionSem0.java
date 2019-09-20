package analizadorLexico;

public class AccionSem0 implements AccionSemantica{
	
	//Esta es para los casilleros que no tienen acciones semanticas. 
	//Por eso no hace nada

	public Token ejecutar(StringBuilder buffer, char c) {
		return null;
	}

}
