package analizadorLexico;

public class CasillaMatriz {
	
    public AccionSemantica AS;
    public int estado = 0;

    public CasillaMatriz(AccionSemantica acc, int e) {
    	
        AS = acc;
        estado = e;
    }

}
