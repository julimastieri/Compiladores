package analizadorLexico;

public class CasillaMatriz {
	
    public AccionSemantica AS;
    public Integer estado = 0;

    public CasillaMatriz(AccionSemantica acc, Integer e) {
    	
        AS = acc;
        estado = e;
    }

}
