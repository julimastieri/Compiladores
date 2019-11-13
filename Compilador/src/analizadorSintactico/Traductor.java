package analizadorSintactico;

import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Map;
import java.util.Map.Entry;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

public class Traductor {
	
	StringBuilder assembler;
	String [] registros  = {"L","L","L","L"};
	HashMap <Integer, String> hashRegs;
	
	public Traductor() {
		hashRegs = new HashMap<Integer, String>();
		hashRegs.put(0, "AX");
		hashRegs.put(1, "BX");
		hashRegs.put(2, "CX");
		hashRegs.put(3, "DX");
		
		assembler = new StringBuilder();
		assembler.append(".386" + "\n"); //386 instruction set
		assembler.append(".model flat, stdcall" + "\n"); //modelo flat para programas de windows
		assembler.append("option casemap :none" + "\n"); //etiquetas case sensitive
		assembler.append("include \\masm32\\include\\windows.inc" + "\n"); // Declaraciones de windows
		assembler.append("include \\masm32\\include\\kernel32.inc" + "\n"); //Contiene funcion de exit
		assembler.append("include \\masm32\\include\\user32.inc" + "\n");
		assembler.append("includelib \\masm32\\lib\\kernel32.lib" + "\n"); //librerias
		assembler.append("includelib \\masm32\\lib\\user32.lib" + "\n");
		
		cargarData();	
	}
	
	private void cargarData() {
		assembler.append(".data" + "\n");
		assembler.append("TituloCadena db \"Cadena\",0" + "\n");
		//assembler.append("CadenaPrueba db \"Hola\",0" + "\n");
		
	    for (Map.Entry<String, Token> entry : AnalizadorLexico.tablaSimbolos.entrySet() )
	    {
	        String lexema = entry.getKey();
	        Token token = entry.getValue();
	        String uso = token.getUso();
	        String tipoDeDato = token.getTipoDeDato();
	        
	        if ( (uso.equals(Token.USO_VARIABLE)) ) { 
	        	if (tipoDeDato.equals("int")) {
	        		assembler.append("_" + lexema + " DW ?" + "\n");
	        	} else if (tipoDeDato.equals("ulong")) {
	        		assembler.append("_" + lexema + " DD ?" + "\n");
	        	}
	        } else if ( (uso.equals(Token.USO_COLECCION)) ) {
	        	
	        	if (tipoDeDato.equals("int")) {
	        		if (token.getCantidadValoresIniciales() != 0) { //tiene valores iniciales
	        			int cantValoresInic = token.getCantidadValoresIniciales();
	        			
	        			assembler.append("_" + lexema + " DW ");
	        			for (int i=0; i<cantValoresInic; i++) {
	        				
	        				if (i != cantValoresInic - 1) { //no es el ultimo
	        					assembler.append(token.getValorInicial(i) + ",");
	        				} else { //es el ultimo (no lleva coma)
	        					assembler.append(token.getValorInicial(i));
	        				}	
	        			}
	        			assembler.append("\n");
	        			
	        		} else { //no tiene valores iniciales
	        			int tam = token.getTamanio();
	        			assembler.append("_" + lexema + " DW " + tam + " DUP ?" + "\n"); //_d DW 4 DUP ?
	        		}
	        	} else if (tipoDeDato.equals("ulong")) {
	        		if (token.getCantidadValoresIniciales() != 0) { //tiene valores iniciales
	        			int cantValoresInic = token.getCantidadValoresIniciales();
	        			
	        			assembler.append("_" + lexema + " DD ");
	        			for (int i=0; i<cantValoresInic; i++) {
	        				
	        				if (i != cantValoresInic - 1) { //no es el ultimo
	        					assembler.append(token.getValorInicial(i) + ",");
	        				} else { //es el ultimo (no lleva coma)
	        					assembler.append(token.getValorInicial(i));
	        				}	
	        			}
	        			
	        			assembler.append("\n");
	        			
	        		} else { //no tiene valores iniciales
	        			int tam = token.getTamanio();
	        			assembler.append("_" + lexema + " DD " + tam + " DUP ?" + "\n"); //_d DW 4 DUP ?
	        		}
	        	}
	        } else if ( (uso.equals(Token.USO_CADENA)) ) {
	        	assembler.append(lexema + " DB " + "\"" + lexema + "\"" +",0" + "\n" ); // mensaje db "mensaje"
	        }
	    }
	}
	
	// -----------------------------------------------------------
	private NodoArbol subIzquierdoConHojas(NodoArbol nodo) {
		
		NodoArbol resultado;
		
		if (nodo.getNodoIzq() == null) {
			if (nodo.getNodoDer() == null) { //No tiene hijos
				return nodo;
			} else { //Tiene hijo derecho
				if (esHoja(nodo.getNodoDer())) { // Hijo derecho hoja
					return nodo;
				} else { //Hijo derecho no hoja
					resultado = subIzquierdoConHojas(nodo.getNodoDer());
				}
			}
			
		} else { // Tengo izquierdo
			if (nodo.getNodoDer() == null) { // No tiene derecho
				if (esHoja(nodo.getNodoIzq())) { // Izquierdo es hoja
					return nodo;
				}else { //Izquierdo no es hoja
					resultado = subIzquierdoConHojas(nodo.getNodoIzq());
				}
			}else { // Tengo dos hijos
				if (esHoja(nodo.getNodoIzq())) { 
					if (esHoja(nodo.getNodoDer())) { //Los dos son hojas
						return nodo;
					}else { // El derecho no es hoja pero el izquierdo si
						resultado = subIzquierdoConHojas(nodo.getNodoDer());
					}
				}else { //El izquierdo no es hoja
					resultado = subIzquierdoConHojas(nodo.getNodoIzq());
				}
			}	
		}
		return resultado;
	}
	
	// -----------------------------------------------------------
	
	private boolean esHoja (NodoArbol nodo) {

		if ((nodo.getNodoIzq() == null) && (nodo.getNodoDer()==null)) {
			return true;
		}
		return false;
	}
	
	// -----------------------------------------------------------
	// PARA PROBAR LOS REEMPLAZOS EN EL ARBOL
	public void imprimirArbolmod(NodoArbol nodo, String tabs) {
   	 
		System.out.println((tabs + nodo.getNombre() + "\n"));  //raiz
		
		if(nodo.nodoIzq!=null)
			imprimirArbolmod(nodo.getNodoIzq(), tabs + "\t");
	        
	    if(nodo.nodoDer!=null)
	        imprimirArbolmod(nodo.getNodoDer(), tabs + "\t");
	 
	}
	
	
	
	public String traducir (NodoArbol raiz) {
		assembler.append(".code" + "\n" + "start:" + "\n");
		
		NodoArbol nodo = subIzquierdoConHojas(raiz);
		System.out.println("NODO: " + nodo.getNombre());
		
		while ((nodo.getNombre() != "PROGRAMA")) { 
			
			
			if (nodo.getNombre().equals("+")) {
				generarSuma(nodo);
				imprimirArbolmod(raiz, "");	
			} else if (nodo.getNombre().equals("-")) {
				generarResta(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("*")) {
				generarMultiplicacion(nodo, raiz);//FALTA
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("/")) {
				generarDivision(nodo); //FALTA
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals(":=")) {
				generarAsignacion(nodo);
				imprimirArbolmod(raiz, "");
			} else if ((nodo.getNombre().equals(">=")) || (nodo.getNombre().equals("<=")) || (nodo.getNombre().equals("==")) || (nodo.getNombre().equals("<>")) || (nodo.getNombre().equals("<")) || (nodo.getNombre().equals(">"))){
				generarComparacion(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("CONDICION")) {
				generarCondicion(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("Sentencia Ejecutable")) {
				nodo.reemplazar(nodo.getNombre()); // Le elimino los hijos
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("THEN")) {
				generarThen(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("ELSE")) {
				generarElse(nodo);
				imprimirArbolmod(raiz, "");
			} else if ((nodo.getNombre().equals("CUERPO")) || (nodo.getNombre().equals("IF")) ) {
				nodo.reemplazar(nodo.getNombre());
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("PRINT")) {
				generarPrint(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("FOREACH")) {
				nodo.reemplazar(nodo.getNombre());
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("CONDICION_FOREACH")) {
				generarCondicionForeach(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("CUERPO_FOREACH")) {
				generarCuerpoForeach(nodo);
				imprimirArbolmod(raiz, "");
			}
			
			//faltan casos -> "ELEM_COLEC", "CONVERSION" 
			
			nodo = subIzquierdoConHojas(raiz);
			System.out.println("NODO:" + nodo.getNombre() );
			
		}
		assembler.append("invoke ExitProcess, 0" + "\n");
		assembler.append("end start" + "\n" );
		
		return assembler.toString();
	}
	
	// -----------------------------------------------------------
	
	private int primerRegLibre() {
		int i=0;
		while ((i<4) && (registros[i] != "L")) {
			i++;
		}
		if (i >= 4) { // no hay registros libres
			return -1;
		} else 
			return i;
	}
	
	// -----------------------------------------------------------
	
	private void generarSuma (NodoArbol nodo) {
		
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		
		if (!(nodoIzq.esRegistro()) && !(nodoDer.esRegistro())) { //Si ninguno es registro son var o const los dos
		//Situacion 1 (VAR - VAR o CONST-CONST) o (VAR - CONST o CONST - VAR)
			//Falta probar var - var
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			
			int reg = primerRegLibre();
			
			if (reg != -1) { // si hay algun registro libre
				registros[reg] = "O";
				
				if ((nodoIzq.getTipoDeDato().equals("ulong")) && (nodoDer.getTipoDeDato().equals("ulong")) ) { 
				// Situacion 1a (32 bits)
					
					if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
						assembler.append("ADD E" + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD EAX,30800
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_a
						assembler.append("ADD E" + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD EAX,_b
					} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX, 30m
						assembler.append("ADD E" + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD EAX,_b
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_b
						assembler.append("ADD E" + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD EAX,40m
					}
					
					//Actualizo el arbol
					nodo.reemplazar("E" + hashRegs.get(reg), reg);
					
				} else if ((nodoIzq.getTipoDeDato().equals("int")) && (nodoDer.getTipoDeDato().equals("int"))) { 
				// Situacion 1b (16 bits)

					if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV " + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV AX,40000
						assembler.append("ADD " + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD AX,30800
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV " + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV AX,_a
						assembler.append("ADD " + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD AX,_b
					} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV " + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV AX, 30m
						assembler.append("ADD " + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD AX,_b
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV " + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV AX,_b
						assembler.append("ADD " + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD AX,40m
					}
					
					//Actualizo el arbol
					nodo.reemplazar(hashRegs.get(reg), reg);
				}
				
				
			}else {
				System.out.println("No hay registros libres");
				// ver que pasa si no hay ningun registro libre
			}	
			
		} else if ( (nodoIzq.esRegistro()) && !(nodoDer.esRegistro()) ) { 
		// Situacion 2 (REG - VAR/CONST)	
			
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			String registro = nodoIzq.getNombre();
			int nroReg = nodoIzq.getNroReg();
			
			if (opDer.getUso().equals(Token.USO_CONSTANTE)) {
				assembler.append("ADD "+ registro + "," + nodoDer.getNombre() + "\n"); //ADD AX,3
			} else if (opDer.getUso().equals(Token.USO_VARIABLE)) {
				assembler.append("ADD "+ registro + ",_" + nodoDer.getNombre() + "\n"); //ADD AX,_b
			}
			
			//Actualizo el arbol
			nodo.reemplazar(registro, nroReg);
			
		} else if ( (nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
		//Situacion 3 (REG - REG)
			
			String registro1 = nodoIzq.getNombre();
			String registro2 = nodoDer.getNombre();
			int nroReg2 = nodoDer.getNroReg();
			int nroReg1 = nodoIzq.getNroReg();
			
			//Genero codigo sobre el primer registro
			assembler.append("ADD "+ registro1 + "," + registro2 + "\n"); //ADD AX,BX
			registros[nroReg2] = "L"; //Libero el 2do registro
			
			//Actualizo el arbol
			nodo.reemplazar(registro1, nroReg1);
			
		} else if ( !(nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
		// Situacion 4 (VAR/CONST - REG) (Operacion conmutativa)
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			String registro = nodoDer.getNombre();
			int nroReg = nodoDer.getNroReg();
			
			if (opIzq.getUso().equals(Token.USO_CONSTANTE)) {
				assembler.append("ADD "+ registro + "," + nodoIzq.getNombre() + "\n"); //ADD AX,3
			} else if (opIzq.getUso().equals(Token.USO_VARIABLE)) {
				assembler.append("ADD "+ registro + ",_" + nodoIzq.getNombre() + "\n"); //ADD AX,_b
			}
			
			//Actualizo el arbol
			nodo.reemplazar(registro, nroReg);	
		}		
	}
	
	// -----------------------------------------------------------
	
	private void generarResta (NodoArbol nodo) {
		
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		
		if (!(nodoIzq.esRegistro()) && !(nodoDer.esRegistro())) { //Si ninguno es registro son var o const los dos
		//Situacion 1 (VAR - VAR o CONST-CONST)
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
				
			int reg = primerRegLibre();
			
			if (reg != -1) { // si hay algun registro libre	
				registros[reg] = "O";
				
				if ((nodoIzq.getTipoDeDato().equals("ulong")) && (nodoDer.getTipoDeDato().equals("ulong"))) { 
				// Situacion 1a (32 bits)
					
					if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
						assembler.append("SUB E" + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //SUB EAX,30800
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_a
						assembler.append("SUB E" + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //SUB EAX,_b
					} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX, 30m
						assembler.append("SUB E" + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //SUB EAX,_b
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_b
						assembler.append("SUB E" + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //SUB EAX,40m
					}
					
					//Actualizo el arbol
					nodo.reemplazar("E" + hashRegs.get(reg), reg);
					
				} else if ((nodoIzq.getTipoDeDato().equals("int")) && (nodoDer.getTipoDeDato().equals("int"))) { 
				// Situacion 1b (16 bits)
					
					if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV " + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV AX,40000
						assembler.append("SUB " + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //SUB AX,30800
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV " + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV AX,_a
						assembler.append("SUB " + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //SUB AX,_b
					} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV " + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV AX, 30m
						assembler.append("SUB " + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //SUB AX,_b
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV " + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV AX,_b
						assembler.append("SUB " + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //SUB AX,40m
					}
					
					//Actualizo el arbol
					nodo.reemplazar(hashRegs.get(reg), reg);
				}
				
				
			}else {
				System.out.println("No hay registros libres");
				// ver que pasa si no hay ningun registro libre
			}	
			
		} else if ( (nodoIzq.esRegistro()) && !(nodoDer.esRegistro()) ) { 
		// Situacion 2 (REG - VAR/CONST)	
			
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			String registro = nodoIzq.getNombre();
			int nroReg = nodoIzq.getNroReg();
			
			if (opDer.getUso().equals(Token.USO_CONSTANTE)) {
				assembler.append("SUB "+ registro + "," + nodoDer.getNombre() + "\n"); //SUB AX,3
			} else if (opDer.getUso().equals(Token.USO_VARIABLE)) {
				assembler.append("SUB "+ registro + ",_" + nodoDer.getNombre() + "\n"); //SUB AX,_b
			}
			
			//Actualizo el arbol
			nodo.reemplazar(registro, nroReg);
			
		} else if ( (nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
		//Situacion 3 (REG - REG)
			
			String registro1 = nodoIzq.getNombre();
			String registro2 = nodoDer.getNombre();
			int nroReg2 = nodoDer.getNroReg();
			int nroReg1 = nodoIzq.getNroReg();
			
			//Genero codigo sobre el primer registro
			assembler.append("SUB "+ registro1 + "," + registro2 + "\n"); //SUB AX,BX
			registros[nroReg2] = "L"; //Libero el 2do registro
			
			//Actualizo el arbol
			nodo.reemplazar(registro1, nroReg1);
			
		} else if ( !(nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
		// Situacion 4 (VAR/CONST - REG) (Operacion NO conmutativa)
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			//Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			String registro = nodoDer.getNombre();
			int nroReg = nodoDer.getNroReg();
			
			int nuevoReg = primerRegLibre();
			
			if (nuevoReg != -1) { // Si hay algun registro libre
				
				if ( nodoDer.getTipoDeDato().equals("ulong")) { 
				// (32 bits)
					
					if (opIzq.getUso().equals(Token.USO_VARIABLE)) {
						assembler.append("MOV E" + hashRegs.get(nuevoReg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_b
					} else if (opIzq.getUso().equals(Token.USO_CONSTANTE)) {
						assembler.append("MOV E" + hashRegs.get(nuevoReg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX,40.000	
					}
					assembler.append("SUB E" + hashRegs.get(nuevoReg) + "," + registro + "\n"); //SUB EAX,EBX
						
					//Actualizo el arbol
					nodo.reemplazar("E" + hashRegs.get(nuevoReg), nuevoReg);
					
				} else if (nodoDer.getTipoDeDato().equals("int")) { 
				// (16 bits)
					
					if (opIzq.getUso().equals(Token.USO_VARIABLE)) {
						assembler.append("MOV " + hashRegs.get(nuevoReg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV AX,_b
					} else if (opIzq.getUso().equals(Token.USO_CONSTANTE)) {
						assembler.append("MOV " + hashRegs.get(nuevoReg) + "," + nodoIzq.getNombre() + "\n"); //MOV AX,4
					}
					assembler.append("SUB " + hashRegs.get(nuevoReg) + "," + registro + "\n"); //SUB AX,BX
					
					//Actualizo el arbol
					nodo.reemplazar(hashRegs.get(nuevoReg), nuevoReg);
				}
				
				registros[nroReg] = "L"; //Libero el primer registro
				
			} else {
				System.out.println("No hay registros libres");
				//ver que hacer
			}
			
		}		
	}
	
	
	// -----------------------------------------------------------
	
	private void changeRecord(NodoArbol raiz, int nroRegBusqueda, int nroRegNuevo) {//cambia el registro del nodo dado por nro de reg nuevo segun el nr de registro dado para la busqueda
		NodoArbol nodoChange = buscarNodoXNroReg(nroRegBusqueda, raiz);
		if (nodoChange != null) {
			if (nodoChange.getNombre().charAt(0) == 'E') { //me fijo si es de 32 bits
				String nombreRegViejo = nodoChange.getNombre(); //EAX
				String nombreRegNuevo = "E" + hashRegs.get(nroRegNuevo); //ECX
				nodoChange.reemplazar(nombreRegNuevo, nroRegNuevo);

				assembler.append("MOV @aux1," + nombreRegNuevo + "\n");
				assembler.append("MOV " + nombreRegNuevo + "," + nombreRegViejo + "\n"); //paso el contenido de EAX al nuevo reg MOV ECX,EAX
				assembler.append("MOV " + nombreRegViejo + ",@aux1" + "\n");
			}
			else { //sino es de 16 bits
				String nombreRegViejo = nodoChange.getNombre(); //AX
				String nombreRegNuevo = hashRegs.get(nroRegNuevo);
				nodoChange.reemplazar(nombreRegNuevo, nroRegNuevo);
				assembler.append("MOV @aux1," + nombreRegNuevo + "\n");
				assembler.append("MOV " + nombreRegNuevo + "," + nombreRegViejo + "\n"); //paso el contenido de EAX al nuevo reg MOV ECX,EAX
				assembler.append("MOV " + nombreRegViejo + ",@aux1" + "\n");
			}
		}
	}
	
	private NodoArbol buscarNodoXNroReg(int nroReg, NodoArbol nodo) {//busca el nodo con el nro de reg dado, si no esta en el arbol retorna null
		if (nodo != null) {
			if( (nodo.esRegistro) && (nodo.nroReg == nroReg) ) //es el que busco? lo retorno
				return nodo;
			else {//sino busco por derecha e izquierda
				NodoArbol resultIzq= buscarNodoXNroReg(nroReg, nodo.nodoIzq);
				NodoArbol resultDer = buscarNodoXNroReg(nroReg, nodo.nodoDer);
				
				if (resultIzq != null) //si encontre el que buscaba por la izquierda lo retorno
					return resultIzq;
				else //sino retorno lo que encontre por la derecha (si no lo encontro por el sub arbol derecho retorna null) 
					return resultDer;
			}
		}
		return null;
	}
	
	// -----------------------------------------------------------
	
	private void generarMultiplicacion (NodoArbol nodo, NodoArbol raiz) {
		
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		
		if (!(nodoIzq.esRegistro()) && !(nodoDer.esRegistro())) { //Si ninguno es registro son var o const los dos
		//Situacion 1 (VAR - VAR o CONST-CONST)
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			
			//Liberar registro 0 (EAX/AX)
			if (registros[0].equals("O")) { //si el registro esta ocupado
				int proxLibre = primerRegLibre();
				if (proxLibre != -1) {
					changeRecord(raiz, 0, proxLibre);
				} else {
					// si no hay ninguno libre: VER
				}
			}
			
			//Liberar DX/EAX
			if (registros[3].equals("O")) {
				int proxLibre = primerRegLibre();
				if (proxLibre != -1) {
					changeRecord(raiz, 3, proxLibre);
				} else {
					// si no hay ninguno libre: VER
				}
			}
			
			if ((nodoIzq.getTipoDeDato().equals("ulong")) && (nodoDer.getTipoDeDato().equals("ulong"))) { 
			// Situacion 1a (32 bits)
					
				assembler.append("MOV EAX," + opIzq.getValor() + "\n"); //MOV EAX,valor1
				assembler.append("IMUL EAX," + opDer.getValor() + "\n"); //IMUL EAX,valor2
					
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
					
			} else if ((nodoIzq.getTipoDeDato().equals("int")) && (nodoDer.getTipoDeDato().equals("int"))) { 
			// Situacion 1b (16 bits)
				
				assembler.append("MOV AX," + opIzq.getValor() + "\n"); //MOV AX,valor1
				assembler.append("IMUL AX," + opDer.getValor() + "\n"); //IMUL AX,valor2
					
				//Actualizo el arbol
				nodo.reemplazar("AX", 0);
			}
				
			registros[0] = "O"; 
						
			
		} else if ( (nodoIzq.esRegistro()) && !(nodoDer.esRegistro()) ) { 
		// Situacion 2 (REG - VAR/CONST)	
			
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			String registro = nodoIzq.getNombre();
			int nroReg = nodoIzq.getNroReg();
			
			//CHEQUEADO
			if ((registro != "AX") || (registro != "EAX")) {
				if (registros[0].equals("O")) {
					changeRecord(raiz, 0, nroReg);
				} else if (registros[0].equals("L")) {
					
					if (registro.charAt(0) == 'E') {
						assembler.append("MOV EAX," + registro + "\n");
					} else {
						assembler.append("MOV AX," + registro + "\n");
					}
					registros[nroReg] = "L";
					registros[0] = "O";
				}				
			}
			
			//Liberar DX/EAX
			if (registros[3].equals("O")) {
				int proxLibre = primerRegLibre();
				if (proxLibre != -1) {
					changeRecord(raiz, 3, proxLibre);
				} else {
					// si no hay ninguno libre: VER
				}
			}
			
			if (nodoDer.getTipoDeDato().equals("ulong")) { 
				// 32 bits
				
				//Genero codigo sobre el registro
				assembler.append("IMUL EAX," + opDer.getValor() + "\n"); //IMUL EAX,valor
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if (nodoDer.getTipoDeDato().equals("int")) {
				// 16 bits
				
				//Genero codigo sobre el registro
				assembler.append("IMUL AX," + opDer.getValor() + "\n"); //IMUL AX,valor
				
				//Actualizo el arbol
				nodo.reemplazar("AX", 0);
			}	

			
		} else if ( (nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
		//Situacion 3 (REG - REG)
			
			String registro1 = nodoIzq.getNombre();
			String registro2 = nodoDer.getNombre();
			int nroReg2 = nodoDer.getNroReg();
			int nroReg1 = nodoIzq.getNroReg();
			
			//Liberar DX/EAX
			if (registros[3].equals("O")) {
				int proxLibre = primerRegLibre();
				if (proxLibre != -1) {
					changeRecord(raiz, 3, proxLibre);
				} else {
					// si no hay ninguno libre: VER
				}
			}
			
			if ((nodoIzq.getTipoDeDato().equals("ulong")) && (nodoDer.getTipoDeDato().equals("ulong"))) {
				
				if (registro1.equals("EAX")) {
					assembler.append("MUL "+ registro1 + "," + registro2 + "\n"); //MUL EAX,EBX
					registros[nroReg2] = "L"; //Libero el 2do registro
					
				} else if (registro2.equals("EAX")) {
					assembler.append("MUL "+ registro2 + "," + registro1 + "\n"); //MUL EAX,EBX
					registros[nroReg1] = "L"; //Libero el 1er registro
					
				} else {
					//CHEQUEADO
					if (registros[0].equals("O")) {
						changeRecord(raiz, 0, nroReg1); //lo que hay en reg1 ahora esta en EAX
						registros[nroReg2] = "L"; //Libero solo el 2 porque el 1 esta ocupado con lo que habia en AX
					
					} else if (registros[0].equals("L")) {
						assembler.append("MOV EAX," + registro1 + "\n");
						
						registros[nroReg1] = "L"; //Libero el 1er registro
						registros[nroReg2] = "L"; //Libero el 2do registro
						registros[0] = "O";
					}
					assembler.append("MUL EAX," + registro2 + "\n"); //MUL EAX,EBX
					
				}
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if ((nodoIzq.getTipoDeDato().equals("int")) && (nodoDer.getTipoDeDato().equals("int"))) {
				
				if (registro1.equals("AX")) {
					assembler.append("MUL "+ registro1 + "," + registro2 + "\n"); //MUL EAX,EBX
					registros[nroReg2] = "L"; //Libero el 2do registro
					
				} else if (registro2.equals("AX")) {
					assembler.append("MUL "+ registro2 + "," + registro1 + "\n"); //MUL EAX,EBX
					registros[nroReg1] = "L"; //Libero el 1er registro
					
				} else {
					//
					if (registros[0].equals("O")) {
						changeRecord(raiz, 0, nroReg1); //lo que hay en reg1 ahora esta en AX
						registros[nroReg2] = "L"; //Libero solo el 2 porque el 1 esta ocupado con lo que habia en AX
					
					} else if (registros[0].equals("L")) {
						assembler.append("MOV AX," + registro1 + "\n");
						registros[nroReg1] = "L"; //Libero el 1er registro
						registros[nroReg2] = "L"; //Libero el 2do registro
						registros[0] = "O";
					}
					assembler.append("MUL AX," + registro2 + "\n"); //MUL EAX,EBX
				}
				//Actualizo el arbol
				nodo.reemplazar("AX", 0);
			}
				
			
		} else if ( !(nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
		// Situacion 4 (VAR/CONST - REG) (Operacion conmutativa)
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			String registro = nodoDer.getNombre();
			int nroReg = nodoDer.getNroReg();
			
			//chequeado
			if ((registro != "AX") || (registro != "EAX")) {
				if (registros[0].equals("O")) {
					changeRecord(raiz, 0, nroReg);
					
				} else if (registros[0].equals("L")) {
					
					if (registro.charAt(0) == 'E') {
						assembler.append("MOV EAX," + registro + "\n");
					} else {
						assembler.append("MOV AX," + registro + "\n");
					}
					registros[nroReg] = "L";
					registros[0] = "O";
				}	
			}
			
			//Liberar DX/EAX
			if (registros[3].equals("O")) {
				int proxLibre = primerRegLibre();
				if (proxLibre != -1) {
					changeRecord(raiz, 3, proxLibre);
				} else {
					// si no hay ninguno libre: VER
				}
			}
			
			
			if (nodoIzq.getTipoDeDato().equals("ulong")) { 
				// 32 bits
				//Genero codigo sobre el registro
				assembler.append("IMUL EAX," + opIzq.getValor() + "\n"); //IMUL EAX,valor
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if (nodoDer.getTipoDeDato().equals("int")) {
				// 16 bits
				//Genero codigo sobre el registro
				assembler.append("IMUL AX," + opIzq.getValor() + "\n"); //IMUL AX,valor
				
				//Actualizo el arbol
				nodo.reemplazar("AX", 0);
			}	
			
		}		
	}
	
	// -----------------------------------------------------------
	
	
	// -----------------------------------------------------------
	private void generarDivision (NodoArbol nodo) {
		
	}
	
	// -----------------------------------------------------------
	
	// -----------------------------------------------------------
	
	private void generarAsignacion (NodoArbol nodo) {
		
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		
		if (nodoDer.esRegistro()) {
			//Situacion I
			assembler.append("MOV _" + nodoIzq.getNombre() + "," + nodoDer.getNombre() + "\n"); //MOV _a,AX
		    registros[nodoDer.getNroReg()] = "L";
		    
		} else {
			//Situacion II
			int nroReg = primerRegLibre();
			Token tokenDerecha = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			
			if (nroReg != -1) {
				registros[nroReg] = "O";	
				
				String nombreReg = hashRegs.get(nroReg);
				
				if (tokenDerecha.getUso().equals(Token.USO_CONSTANTE)) {
					if (nodoDer.getTipoDeDato().equals("ulong")) {
						assembler.append("MOV E" + nombreReg + "," + nodoDer.getNombre()+ "\n"); //MOV ECX,40.000
						assembler.append("MOV _" + nodoIzq.getNombre() + ",E" + nombreReg + "\n"); //MOV _a,ECX
						
					} else if (nodoDer.getTipoDeDato().equals("int")) {
						assembler.append("MOV " + nombreReg + "," + nodoDer.getNombre() + "\n"); //MOV CX,2
						assembler.append("MOV _" + nodoIzq.getNombre() + "," + nombreReg + "\n"); //MOV _a,CX
					}
				} else if (tokenDerecha.getUso().equals(Token.USO_VARIABLE)) {
					if (nodoDer.getTipoDeDato().equals("ulong")) {
						assembler.append("MOV E" + nombreReg + ",_" + nodoDer.getNombre() + "\n"); //MOV ECX,_b
						assembler.append("MOV _" + nodoIzq.getNombre() + ",E" + nombreReg + "\n"); //MOV _a,ECX
						
					} else if (nodoDer.getTipoDeDato().equals("int")) {
						assembler.append("MOV " + nombreReg + ",_" + nodoDer.getNombre() + "\n"); //MOV CX,_b
						assembler.append("MOV _" + nodoIzq.getNombre() + "," + nombreReg + "\n"); //MOV _a,CX
					}
				}
				registros[nroReg] = "L";
				
			} else {
				//VER QUE PASA SI NO HAY NINGUNO LIBRE
			}	
		}
		nodo.reemplazar(nodoIzq.getNombre());
	}
	
	// -----------------------------------------------------------
	
	
	// -----------------------------------------------------------
	
	private void generarComparacion (NodoArbol nodo) {
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		Token tokenDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
		Token tokenIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
		String comparador = nodo.getNombre();
		
		if ((tokenDer.getUso().equals(Token.USO_VARIABLE)) && (tokenIzq.getUso().equals(Token.USO_VARIABLE)) ) {
			assembler.append("CMP _" + nodoIzq.getNombre() + ",_" + nodoDer.getNombre() + "\n");
		} else if ((tokenDer.getUso().equals(Token.USO_VARIABLE)) && !(tokenIzq.getUso().equals(Token.USO_VARIABLE))) {
			assembler.append("CMP " + nodoIzq.getNombre() + ",_" + nodoDer.getNombre() + "\n");
		} else if (!(tokenDer.getUso().equals(Token.USO_VARIABLE)) && (tokenIzq.getUso().equals(Token.USO_VARIABLE))) {
			assembler.append("CMP _" + nodoIzq.getNombre() + "," + nodoDer.getNombre() + "\n");
		} else {
			assembler.append("CMP " + nodoIzq.getNombre() + "," + nodoDer.getNombre() + "\n");
		}
		
		
		registros[nodoIzq.getNroReg()] = "L";
		registros[nodoDer.getNroReg()] = "L";
		
		nodo.reemplazar(comparador);
	}
	
	// -----------------------------------------------------------
	
	
	// -----------------------------------------------------------
	
	private void generarCondicion (NodoArbol nodo) {
		String comparador = nodo.getNodoIzq().getNombre();
		System.out.println("Comparador: " + comparador);
		
		if (comparador.equals(">=")) {
			assembler.append("JL LabelElse" + "\n");
		} else if (comparador.equals("<=")) {
			assembler.append("JG LabelElse" + "\n");
		} else if (comparador.equals("==")) {
			assembler.append("JNE LabelElse" + "\n");
		} else if (comparador.equals("<>")) {
			assembler.append("JE LabelElse" + "\n");
		} else if (comparador.equals("<")) {
			assembler.append("JGE LabelElse" + "\n");
		} else if (comparador.equals(">")) {
			assembler.append("JLE LabelElse" + "\n");
		}
		
		nodo.reemplazar(nodo.getNombre());
	}
	
	// -----------------------------------------------------------
	
	
	// -----------------------------------------------------------
	
    private void generarThen (NodoArbol nodo) {
		assembler.append("JMP LabelSiguiente" + "\n");
		assembler.append("LabelElse:" + "\n");
		
		nodo.reemplazar(nodo.getNombre());
	}
	
	// -----------------------------------------------------------
	
	private void generarElse (NodoArbol nodo) {
		assembler.append("LabelSiguiente:" + "\n");
		nodo.reemplazar(nodo.getNombre());
	}
	
	// -----------------------------------------------------------
	
	private void generarPrint (NodoArbol nodo) {
		//Token cadena = AnalizadorLexico.tablaSimbolos.get(nodo.getNodoDer().getNombre());
		//invoke MessageBox, NULL, addr contenido, addr titulo, MB_OK

		assembler.append("invoke MessageBox, NULL, addr " + "CadenaPrueba" + ", addr TituloCadena, MB_OK" + "\n");
		nodo.reemplazar(nodo.getNombre());
	}
	
	// -----------------------------------------------------------
	
	private void generarCondicionForeach (NodoArbol nodo) {
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
		Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
		
		//Pido reg en donde voy a guardar el nro de iteracion que se incrementa en 1, se inicializa en 0
		//Pongo Label
		//Comparo el tamaño de la coleccion con el nro de iteracion (aca agregar un JG si iteracion es mayor que tamaño)
		//asignacion a variable "_a" el valor del arreglo en el nro de iteracion
		
		/*
		int reg = primerRegLibre();
		String nombreReg = hashRegs.get(reg);
		if (reg != -1) { // si hay algun registro libre
			registros[reg] = "O"; 
			assembler.append("MOV " + nombreReg + "," + 0 + "\n"); //MOV AX,0
		}
		*/
		//iterador 
		assembler.append("MOV @aux2,0" + "\n"); //MOV AX,0
		
		
		assembler.append("LabelCondForech:" + "\n");
		
		//assembler.append("CMP " + nombreReg + "," + opDer.getTamanio() + "\n"); //CMP AX,3 (tamanio = 3)
		assembler.append("CMP @aux2," + opDer.getTamanio() + "\n"); //CMP aux2,3 (tamanio = 3)
		
		assembler.append("JG LabelSiguienteForeach" + "\n");
		
		//Hago asignacion a := b[aux2]
		int otroReg = primerRegLibre();
		String nombreOtroReg = hashRegs.get(otroReg);
		if (otroReg != -1) { // si hay algun registro libre
			registros[otroReg] = "O"; 
			//assembler.append("MOV " + nombreOtroReg + ",_" + opDer.getValorInicial(aux2) + "\n"); //MOV BX,_b[i] (nose como hacer b[i])
			assembler.append("MOV b[i]" + "\n");
			assembler.append("MOV _" + nodoIzq.getNombre() + "," + nombreOtroReg + "\n"); //MOV _a,BX
			//assembler.append("ADD " + nombreReg + "," + 1 + "\n");
			assembler.append("ADD @aux2,1" + "\n");
			registros[otroReg] = "L";
		}
		
		nodo.reemplazar(nodo.getNombre());
	}
	
	// -----------------------------------------------------------
	
	private void generarCuerpoForeach(NodoArbol nodo) {
		assembler.append("JMP LabelCondForech" + "\n");
		assembler.append("LabelSiguienteForeach:" + "\n");
		//liberar registro en donde estaba el iterador
		nodo.reemplazar(nodo.getNombre());
	}
	
	// -----------------------------------------------------------
}
