package analizadorSintactico;

import java.util.HashMap;
import java.util.Map;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

public class Traductor {
	
	StringBuilder assembler;
	String [] registros  = {"L","L","L","L"};
	HashMap <Integer, String> hashRegs;
	private int nroUltVarAux = 0;
	
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
		//assembler.append("MOV AX,@data" + "\n" + "MOV ds,AX" + "\n");
		
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
				generarDivision(nodo, raiz); //FALTA
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
			} else if ((nodo.getNombre().equals("CUERPO")) ) {
				nodo.reemplazar(nodo.getNombre());
				assembler.append("LabelSiguiente:" + "\n");
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("PRINT")) {
				generarPrint(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("FOREACH") || (nodo.getNombre().equals("IF")) || (nodo.getNombre().equals("ELSE")) ) {
				nodo.reemplazar(nodo.getNombre());
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("CONDICION_FOREACH")) {
				generarCondicionForeach(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("CUERPO_FOREACH")) {
				generarCuerpoForeach(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("CONVERSION")) {
				generarConversion(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre().equals("ELEM_COLEC")) {
				generarElemColec(nodo);
				imprimirArbolmod(raiz, "");
			}
			
			
			nodo = subIzquierdoConHojas(raiz);
			System.out.println("NODO:" + nodo.getNombre() );
			
		}
		assembler.append("LabelError:" + "\n");
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
	
	private int getNroReg (String nom) {
		
		if ((nom == "AX") || (nom == "EAX")) {
			return 0;
		} else if ((nom == "BX") || (nom == "EBX")) {
			return 1;
		}else if ((nom == "CX") || (nom == "ECX")) {
			return 2;
		} else if ((nom == "DX") || (nom == "EDX")) {
			return 3;
		} 
		return -1;
	}
	
	// -----------------------------------------------------------
	
	private void generarSuma (NodoArbol nodo) {
		
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		
		if (!(nodoIzq.esRegistro()) && !(nodoDer.esRegistro())) { //Si ninguno es registro son var o const los dos
		//Situacion 1 (VAR - VAR o CONST-CONST) o (VAR - CONST o CONST - VAR)
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			
			int reg = primerRegLibre();
			
			if (reg != -1) { // si hay algun registro libre
				registros[reg] = "O";
				
				if ((nodoIzq.getTipoDeDato().equals("ulong")) && (nodoDer.getTipoDeDato().equals("ulong")) ) { 
				// Situacion 1a (32 bits)
					
					//CASOS DE ELEM COLECCIONES
				    if ( (nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",[" + nodoIzq.getNombre() + "]\n"); //MOV AX,[BX]
					    assembler.append("ADD E" +hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD EAX, 40000
					    registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",[" + nodoIzq.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("ADD E" +hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD EAX, _b
						registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (nodoDer.esRefMem()) ) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("ADD E" +hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //ADD EAX, _b
						registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (nodoDer.esRefMem()) ) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("ADD E" +hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //ADD EAX, 40000
						registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					}
					// OTROS CASOS
					else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
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

					//CASOS DE ELEM COLECCIONES
				    if ( (nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV " + hashRegs.get(reg) + ",[" + nodoIzq.getNombre() + "]\n"); //MOV AX,[BX]
					    assembler.append("ADD " +hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD EAX, 40000
					    registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV " + hashRegs.get(reg) + ",[" + nodoIzq.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("ADD " +hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD EAX, _b
						registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (nodoDer.esRefMem()) ) {
						assembler.append("MOV " + hashRegs.get(reg) + ",[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("ADD " +hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //ADD EAX, _b
						registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (nodoDer.esRefMem()) ) {
						assembler.append("MOV " + hashRegs.get(reg) + ",[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("ADD " +hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //ADD EAX, 40000
						registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					}
					// OTROS CASOS				
					else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
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
					
					//CASOS DE ELEM COLECCIONES
				    if ( (nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",[" + nodoIzq.getNombre() + "]\n"); //MOV AX,[BX]
					    assembler.append("SUB E" +hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD EAX, 40000
					    registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",[" + nodoIzq.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("SUB E" +hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD EAX, _b
						registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (nodoDer.esRefMem()) ) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("SUB E" +hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //ADD EAX, _b
						registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (nodoDer.esRefMem()) ) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("SUB E" +hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //ADD EAX, 40000
						registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					}
					// OTROS CASOS		
					else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
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
					
					//CASOS DE ELEM COLECCIONES
				    if ( (nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
						assembler.append("MOV " + hashRegs.get(reg) + ",[" + nodoIzq.getNombre() + "]\n"); //MOV AX,[BX]
					    assembler.append("SUB " +hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD EAX, 40000
					    registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
						assembler.append("MOV " + hashRegs.get(reg) + ",[" + nodoIzq.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("SUB " +hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD EAX, _b
						registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (nodoDer.esRefMem()) ) {
						assembler.append("MOV " + hashRegs.get(reg) + ",[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("SUB " +hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //ADD EAX, _b
						registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (nodoDer.esRefMem()) ) {
						assembler.append("MOV " + hashRegs.get(reg) + ",[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[AX]
						assembler.append("SUB " +hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //ADD EAX, 40000
						registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
					}
					// OTROS CASOS
					else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
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
			String nombreRegNuevo = "";
			String nombreRegViejo = nodoChange.getNombre(); //EAX
			
			
			if (nodoChange.getNombre().charAt(0) == 'E') { //me fijo si es de 32 bits			
				nombreRegNuevo = "E" + hashRegs.get(nroRegNuevo); //ECX									
			}
			else { //sino es de 16 bits			
				nombreRegNuevo = hashRegs.get(nroRegNuevo);
			}
			
			nodoChange.reemplazar(nombreRegNuevo, nroRegNuevo);
			assembler.append("MOV @aux1," + nombreRegNuevo + "\n");
			if (nodoChange.esRefMem()) {
				assembler.append("MOV " + nombreRegNuevo + ",[" + nombreRegViejo + "]\n"); //paso el contenido de EAX al nuevo reg MOV ECX,EAX
				assembler.append("MOV " + nombreRegViejo + ",[@aux1]" + "\n");
			} else {
				assembler.append("MOV " + nombreRegNuevo + "," + nombreRegViejo + "\n"); //paso el contenido de EAX al nuevo reg MOV ECX,EAX
				assembler.append("MOV " + nombreRegViejo + ",@aux1" + "\n");
			}	
		}
	}
	
	private NodoArbol buscarNodoXNroReg(int nroReg, NodoArbol nodo) {//busca el nodo con el nro de reg dado, si no esta en el arbol retorna null
		if (nodo != null) {
			if( ((nodo.esRegistro) && (nodo.nroReg == nroReg)) || ((nodo.esRefMem()) && (nodo.nroReg == nroReg)) ) //es el que busco? lo retorno
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
		
		String nombreIzq = "";
		String nombreDer = "";
		
		//Si no son registros puedo guardar un nombre para luego ponerlo en el assembler
		if ( !(nodoIzq.esRegistro()) ) {
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			
			if (nodoIzq.esRefMem())
				nombreIzq = "[" + nodoIzq.getNombre() + "]";
			else if (opIzq.getUso().equals(Token.USO_CONSTANTE))
				nombreIzq = nodoIzq.getNombre();
			else if (opIzq.getUso().equals(Token.USO_VARIABLE))
				nombreIzq = "_" + nodoIzq.getNombre();
		}
		
		if ( !(nodoDer.esRegistro()) ) {
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			
			if (nodoDer.esRefMem())
				nombreDer = "[" + nodoDer.getNombre() + "]";
			else if (opDer.getUso().equals(Token.USO_CONSTANTE))
				nombreDer = nodoDer.getNombre();
			else if (opDer.getUso().equals(Token.USO_VARIABLE))
				nombreDer = "_" + nodoDer.getNombre();
		}
		
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
			
			//Liberar DX/EDX
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
				
				
				//CASOS DE ELEM COLECCIONES
			    if ( (nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
					assembler.append("MOV EAX,[" + nodoIzq.getNombre() + "]\n"); //MOV EAX,[BX]
					assembler.append("MOV EDX," + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("MUL EAX,EDX" + "\n"); 
				    registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
				} else if ((nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
					assembler.append("MOV EAX,[" + nodoIzq.getNombre() + "]\n"); //MOV EAX,[BX]
					assembler.append("MOV EDX,_" + nodoDer.getNombre() + "\n"); //MOV EDX,_b
					assembler.append("MUL EAX,EDX" + "\n"); 
					registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
				} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (nodoDer.esRefMem()) ) {
					assembler.append("MOV EAX,[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[BX]
					assembler.append("MOV EDX,_" + nodoIzq.getNombre() + "\n"); //MOV EDX,_b
					assembler.append("MUL EAX,EDX" + "\n"); 
					registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
				} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (nodoDer.esRefMem()) ) {
					assembler.append("MOV EAX,[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[BX]
					assembler.append("MOV EDX," + nodoIzq.getNombre() + "\n"); //MOV EDX,_b
					assembler.append("MUL EAX,EDX" + "\n"); 
					registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
				}
				// OTROS CASOS
				else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
					assembler.append("MOV EAX," + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
					assembler.append("MOV EDX," + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("MUL EAX,EDX" + "\n"); 
				} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
					assembler.append("MOV EAX,_" + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
					assembler.append("MOV EDX,_" + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("MUL EAX,EDX" + "\n");

				} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
					assembler.append("MOV EAX," + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
					assembler.append("MOV EDX,_" + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("MUL EAX,EDX" + "\n");
					
				} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
					assembler.append("MOV EAX,_" + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
					assembler.append("MOV EDX," + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("MUL EAX,EDX" + "\n");
					
				}
				
				registros[3] = "L";	
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
					
			} else if ((nodoIzq.getTipoDeDato().equals("int")) && (nodoDer.getTipoDeDato().equals("int"))) { 
			// Situacion 1b (16 bits)
				
				
				//CASOS DE ELEM COLECCIONES
			    if ( (nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
					assembler.append("MOV AX,[" + nodoIzq.getNombre() + "]\n"); //MOV EAX,[BX]
					assembler.append("MOV DX," + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("IMUL AX,DX" + "\n"); 
				    registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
				} else if ((nodoIzq.esRefMem()) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
					assembler.append("MOV AX,[" + nodoIzq.getNombre() + "]\n"); //MOV EAX,[BX]
					assembler.append("MOV DX,_" + nodoDer.getNombre() + "\n"); //MOV EDX,_b
					assembler.append("IMUL AX,DX" + "\n"); 
					registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
				} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (nodoDer.esRefMem()) ) {
					assembler.append("MOV AX,[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[BX]
					assembler.append("MOV DX,_" + nodoIzq.getNombre() + "\n"); //MOV EDX,_b
					assembler.append("IMUL AX,DX" + "\n"); 
					registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
				} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (nodoDer.esRefMem()) ) {
					assembler.append("MOV AX,[" + nodoDer.getNombre() + "]\n"); //MOV EAX,[BX]
					assembler.append("MOV DX," + nodoIzq.getNombre() + "\n"); //MOV EDX,_b
					assembler.append("IMUL AX,DX" + "\n"); 
					registros[getNroReg(nodoDer.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
				}
				// OTROS CASOS
				else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
					assembler.append("MOV AX," + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
					assembler.append("MOV DX," + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("IMUL AX,DX" + "\n"); 
				} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
					assembler.append("MOV AX,_" + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
					assembler.append("MOV DX,_" + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("MUL AX,DX" + "\n");

				} else if ((opIzq.getUso().equals(Token.USO_CONSTANTE)) && (opDer.getUso().equals(Token.USO_VARIABLE))) {
					assembler.append("MOV AX," + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
					assembler.append("MOV DX,_" + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("MUL AX,DX" + "\n");
					
				} else if ((opIzq.getUso().equals(Token.USO_VARIABLE)) && (opDer.getUso().equals(Token.USO_CONSTANTE))) {
					assembler.append("MOV AX,_" + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
					assembler.append("MOV DX," + nodoDer.getNombre() + "\n"); //MOV EDX,40000
					assembler.append("MUL AX,DX" + "\n");
					
				}
				assembler.append("shl EDX,16" + "\n");
				assembler.append("MOV DX,AX" + "\n");
				
				registros[0] = "L";
				//Actualizo el arbol
				nodo.reemplazar("EDX", 0);
			}
				
			registros[0] = "O"; 
						
			
		} else if ( (nodoIzq.esRegistro()) && !(nodoDer.esRegistro()) ) { 
		// Situacion 2 (REG - VAR/CONST)	
		
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
			
			//Liberar DX/EDX
			if (registros[3].equals("O")) {
				int proxLibre = primerRegLibre();
				if (proxLibre != -1) {
					changeRecord(raiz, 3, proxLibre);
				}
			}
			
		
			
			if (nodoDer.getTipoDeDato().equals("ulong")) { 
				// 32 bits
				
				//Genero codigo sobre el registro
				assembler.append("MOV EDX," + nombreDer +  "\n");
				assembler.append("MUL EAX,EDX" + "\n"); 
				
				registros[3] = "L";
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if (nodoDer.getTipoDeDato().equals("int")) {
				// 16 bits
				
				//Genero codigo sobre el registro
				assembler.append("MOV DX," + nombreDer + "\n");
				assembler.append("IMUL AX,DX" + "\n"); //IMUL AX,valor
				
				
				assembler.append("shl EDX,16" + "\n");
				assembler.append("MOV DX,AX" + "\n");
				
				registros[0] = "L";
				//Actualizo el arbol
				nodo.reemplazar("EDX", 0);
			}	

			
		} else if ( (nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
		//Situacion 3 (REG - REG)
			
			String registro1 = nodoIzq.getNombre();
			String registro2 = nodoDer.getNombre();
			int nroReg2 = nodoDer.getNroReg();
			int nroReg1 = nodoIzq.getNroReg();
			
			//Liberar DX/EAX
			if (!(registro1.equals("EDX")) || !(registro1.equals("DX")) || !(registro2.equals("EDX")) || !(registro2.equals("DX"))  ) {
				if (registros[3].equals("O")) {
					int proxLibre = primerRegLibre();
					if (proxLibre != -1) {
						changeRecord(raiz, 3, proxLibre);
					} 
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
						if (nroReg1 == 3) {
							changeRecord(raiz, 0, nroReg2); //lo que hay en reg2 ahora esta en EAX
							assembler.append("MUL EAX," + registro1 + "\n"); //MUL EAX,EBX
							registros[nroReg1] = "L";
						} else {
							changeRecord(raiz, 0, nroReg1); //lo que hay en reg1 ahora esta en EAX
							assembler.append("MUL EAX," + registro2 + "\n"); //MUL EAX,EBX
							registros[nroReg2] = "L";
						}
						
					} else if (registros[0].equals("L")) {
						assembler.append("MOV EAX," + registro1 + "\n");
						assembler.append("MUL EAX," + registro2 + "\n"); //MUL EAX,EBX
						
						registros[nroReg1] = "L"; //Libero el 1er registro
						registros[nroReg2] = "L"; //Libero el 2do registro
						registros[0] = "O";
					}
					
					
				}
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if ((nodoIzq.getTipoDeDato().equals("int")) && (nodoDer.getTipoDeDato().equals("int"))) {
				
				if (registro1.equals("AX")) {
					assembler.append("IMUL "+ registro1 + "," + registro2 + "\n"); //MUL EAX,EBX
					registros[nroReg2] = "L"; //Libero el 2do registro
					
				} else if (registro2.equals("AX")) {
					assembler.append("IMUL "+ registro2 + "," + registro1 + "\n"); //MUL EAX,EBX
					registros[nroReg1] = "L"; //Libero el 1er registro
					
				} else {
					//
					if (registros[0].equals("O")) {
						if (nroReg1 == 3) {
							changeRecord(raiz, 0, nroReg2); //lo que hay en reg2 ahora esta en EAX
							//registros[nroReg2] = "L"; //Libero solo el 2 porque el 1 esta ocupado con lo que habia en AX
						} else {
							changeRecord(raiz, 0, nroReg1); //lo que hay en reg1 ahora esta en EAX
							//registros[nroReg1] = "L"; //Libero solo el 2 porque el 1 esta ocupado con lo que habia en AX
						}
					
					} else if (registros[0].equals("L")) {
						assembler.append("MOV AX," + registro1 + "\n");
						registros[nroReg1] = "L"; //Libero el 1er registro
						registros[nroReg2] = "L"; //Libero el 2do registro
						registros[0] = "O";
					}
					assembler.append("MUL AX," + registro2 + "\n"); //MUL EAX,EBX
				}
				
				assembler.append("shl EDX,16" + "\n");
				assembler.append("MOV DX,AX" + "\n");
				
				registros[0] = "L";
				//Actualizo el arbol
				nodo.reemplazar("EDX", 0);
			}
				
			
		} else if ( !(nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
		// Situacion 4 (VAR/CONST - REG) (Operacion conmutativa)
			
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
				assembler.append("MOV EDX," + nombreIzq +  "\n");
				assembler.append("MUL EAX,EDX" + "\n"); //IMUL EAX,EDX
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if (nodoDer.getTipoDeDato().equals("int")) {
				// 16 bits
				
				//Genero codigo sobre el registro
				assembler.append("MOV DX," + nombreIzq + "\n");
				assembler.append("IMUL AX,DX" + "\n"); //IMUL AX,valor
				
				
				assembler.append("shl EDX,16" + "\n");
				assembler.append("MOV DX,AX" + "\n");
				
				registros[0] = "L";
				//Actualizo el arbol
				nodo.reemplazar("EDX", 0);
				
			}	
			
		}		
	}
	
	// -----------------------------------------------------------
	
	private void generarDivision (NodoArbol nodo, NodoArbol raiz) {

		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		String nombreIzq = "";
		String nombreDer = "";
		
		//Si no son registros puedo guardar un nombre para luego ponerlo en el assembler
		if ( !(nodoIzq.esRegistro()) ) {
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			
			if (nodoIzq.esRefMem())
				nombreIzq = "[" + nodoIzq.getNombre() + "]";
			else if (opIzq.getUso().equals(Token.USO_CONSTANTE))
				nombreIzq = nodoIzq.getNombre();
			else if (opIzq.getUso().equals(Token.USO_VARIABLE))
				nombreIzq = "_" + nodoIzq.getNombre();
		}
		
		if ( !(nodoDer.esRegistro()) ) {
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			
			if (nodoDer.esRefMem())
				nombreDer = "[" + nodoDer.getNombre() + "]";
			else if (opDer.getUso().equals(Token.USO_CONSTANTE))
				nombreDer = nodoDer.getNombre();
			else if (opDer.getUso().equals(Token.USO_VARIABLE))
				nombreDer = "_" + nodoDer.getNombre();
		}
		
		if (!(nodoIzq.esRegistro()) && !(nodoDer.esRegistro())) { //Si ninguno es registro son var o const los dos
		//Situacion 1 (VAR - VAR o CONST-CONST)
			
			//Liberar registro 0 (EAX/AX)
			if (registros[0].equals("O")) { //si el registro esta ocupado
				int proxLibre = primerRegLibre();
				if (proxLibre != -1)
					changeRecord(raiz, 0, proxLibre);
			}
			
			//Liberar DX/EDX
			if (registros[3].equals("O")) {
				int proxLibre = primerRegLibre();
				if (proxLibre != -1)
					changeRecord(raiz, 3, proxLibre);
			}
			
			if ( (nodo.getTipoDeDato().equals("ulong")) ){ 
			// Situacion 1a (32 bits)

				assembler.append("MOV EAX," + nombreIzq + "\n");
				assembler.append("MOV EDX," + nombreDer + "\n");
				assembler.append("DIV EAX,EDX" + "\n"); 
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);

			} else if (nodo.getTipoDeDato().equals("int")) {
				// Situacion 1b (16 bits) 
					
				assembler.append("CMP " + nombreDer + ",0" +"\n"); //comparo el valor de lo que hay en nodo derecho con 0
				assembler.append("JE LabelError" + "\n"); //si es igual a 0 ir al label error
				
				assembler.append("MOV AX," + nombreIzq + "\n");
				assembler.append("MOV DX," + nombreDer + "\n");
				assembler.append("IDIV AX,DX" + "\n"); 
				
				//Actualizo el arbol
				nodo.reemplazar("AX", 0);
			}
			
			registros[3] = "L";
			if (nodoIzq.esRefMem())	
				registros[getNroReg(nodoIzq.getNombre())] = "L";
		
			if (nodoDer.esRefMem())	
				registros[getNroReg(nodoDer.getNombre())] = "L";
			registros[0] = "O"; 
			
			
		} else if ( (nodoIzq.esRegistro()) && !(nodoDer.esRegistro()) ) { 
			// Situacion 2 (REG - VAR/CONST)	
			
				String registro = nodoIzq.getNombre();
				int nroReg = nodoIzq.getNroReg();
				
				//Mudo a AX lo que hay en el registro del nodo izquierdo 
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
				
				//Liberar DX/EDX
				if (registros[3].equals("O")) {
					int proxLibre = primerRegLibre();
					if (proxLibre != -1) {
						changeRecord(raiz, 3, proxLibre);
					}
				}
				
				if (nodo.getTipoDeDato().equals("ulong")) { 
					// 32 bits
					
					//Genero codigo sobre el registro
					assembler.append("MOV EDX," + nombreDer +  "\n");
					assembler.append("DIV EAX,EDX" + "\n"); 
					
					//Actualizo el arbol
					nodo.reemplazar("EAX", 0);
					
				} else if (nodo.getTipoDeDato().equals("int")) {
					// 16 bits
					
					assembler.append("CMP " + nombreDer + ",0" +"\n"); //comparo el valor de lo que hay en nodo derecho con 0
					assembler.append("JE LabelError" + "\n"); //si es igual a 0 ir al label error
					
					//Genero codigo sobre el registro
					assembler.append("MOV DX," + nombreDer + "\n");
					assembler.append("IDIV AX,DX" + "\n");
				
					//Actualizo el arbol
					nodo.reemplazar("AX", 0);
				}	
				
			registros[3] = "L";	
			if (nodoDer.esRefMem())	
				registros[getNroReg(nodoDer.getNombre())] = "L";	

		
		} else if ( !(nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
			
			// Situacion 3 (VAR/CONST - REG)	
			
			String registro = nodoDer.getNombre();
			int nroReg = nodoDer.getNroReg();
			
			//Mudo a AX lo que hay en el registro del nodo izquierdo 
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
			
			//Liberar DX/EDX
			if (registros[3].equals("O")) {
				int proxLibre = primerRegLibre();
				if (proxLibre != -1) {
					changeRecord(raiz, 3, proxLibre);
				}
			}
			
			if (nodo.getTipoDeDato().equals("ulong")) { 
				// 32 bits
				
				//Genero codigo sobre el registro
				assembler.append("MOV EDX," + nombreIzq +  "\n");
				assembler.append("DIV EAX,EDX" + "\n"); 
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if (nodo.getTipoDeDato().equals("int")) {
				// 16 bits
				
				assembler.append("CMP EAX,0" +"\n"); //comparo el valor de lo que hay en nodo derecho con 0
				assembler.append("JE LabelError" + "\n"); //si es igual a 0 ir al label error
				
				//Genero codigo sobre el registro
				assembler.append("MOV DX," + nombreIzq + "\n");
				assembler.append("IDIV AX,DX" + "\n");
			
				//Actualizo el arbol
				nodo.reemplazar("AX", 0);
			}	
			
		registros[3] = "L";	
		if (nodoIzq.esRefMem())	
			registros[getNroReg(nodoIzq.getNombre())] = "L";	
			
		} else if ( (nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
			// Situacion 4 (REG - REG)
			String registro1 = nodoIzq.getNombre();
			String registro2 = nodoDer.getNombre();
			int nroReg2 = nodoDer.getNroReg();
			int nroReg1 = nodoIzq.getNroReg();
			
			//Liberar DX
			if (!(registro1.equals("EDX")) || !(registro1.equals("DX")) || !(registro2.equals("EDX")) || !(registro2.equals("DX"))  ) {
				if (registros[3].equals("O")) {
					int proxLibre = primerRegLibre();
					if (proxLibre != -1) {
						changeRecord(raiz, 3, proxLibre);
					}
				}
			}
			
			if (nodo.getTipoDeDato().equals("ulong")) {
				if (registro1.equals("EAX")) {
					assembler.append("DIV "+ registro1 + "," + registro2 + "\n"); //MUL EAX,EBX
					registros[nroReg2] = "L"; //Libero el 2do registro
					
				} else if (registro2.equals("EAX")) {
					assembler.append("DIV "+ registro2 + "," + registro1 + "\n"); //MUL EAX,EBX
					registros[nroReg1] = "L"; //Libero el 1er registro
					
				} else {
						if (registros[0].equals("O")) {
							if (nroReg1 == 3) {
								changeRecord(raiz, 0, nroReg2); //lo que hay en reg2 ahora esta en EAX
								assembler.append("DIV EAX," + registro1 + "\n"); //MUL EAX,EBX
								registros[nroReg1] = "L"; //Libero el 1er registro
							} else {
								changeRecord(raiz, 0, nroReg1); //lo que hay en reg1 ahora esta en EAX
								assembler.append("DIV EAX," + registro2 + "\n"); //MUL EAX,EBX
								registros[nroReg2] = "L"; //Libero el 1er registro
							}
							
						} else if (registros[0].equals("L")) {
							assembler.append("MOV EAX," + registro1 + "\n");
							assembler.append("DIV EAX," + registro2 + "\n"); //MUL EAX,EBX
							
							registros[nroReg1] = "L"; //Libero el 1er registro
							registros[nroReg2] = "L"; //Libero el 2do registro
							registros[0] = "O";
						}
						
				}
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
			
			
			}if (nodo.getTipoDeDato().equals("int")) {
				
				assembler.append("CMP " + registro2 + ",0" +"\n"); //comparo el valor de lo que hay en nodo derecho con 0
				assembler.append("JE LabelError" + "\n"); //si es igual a 0 ir al label error
				
				if (registro1.equals("AX")) {
					assembler.append("IDIV "+ registro1 + "," + registro2 + "\n"); //MUL EAX,EBX
					registros[nroReg2] = "L"; //Libero el 2do registro
					
				} else if (registro2.equals("AX")) {
					assembler.append("IDIV "+ registro2 + "," + registro1 + "\n"); //MUL EAX,EBX
					registros[nroReg1] = "L"; //Libero el 1er registro
					
				} else {
						if (registros[0].equals("O")) {
							if (nroReg1 == 3) {
								changeRecord(raiz, 0, nroReg2); //lo que hay en reg2 ahora esta en EAX
								assembler.append("IDIV AX," + registro1 + "\n"); //MUL EAX,EBX
								registros[nroReg1] = "L"; //Libero el 1er registro
							} else {
								changeRecord(raiz, 0, nroReg1); //lo que hay en reg1 ahora esta en EAX
								assembler.append("IDIV AX," + registro2 + "\n"); //MUL EAX,EBX
								registros[nroReg2] = "L"; //Libero el 1er registro
							}
							
						} else if (registros[0].equals("L")) {
							assembler.append("MOV AX," + registro1 + "\n");
							assembler.append("IDIV AX," + registro2 + "\n"); //MUL EAX,EBX
							
							registros[nroReg1] = "L"; //Libero el 1er registro
							registros[nroReg2] = "L"; //Libero el 2do registro
							registros[0] = "O";
						}
						
				}
				//Actualizo el arbol
				nodo.reemplazar("AX", 0);
			}
		}	
	}
	
	// -----------------------------------------------------------
	
	private void generarAsignacion (NodoArbol nodo) {
		
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		
		if (nodoDer.esRegistro()) {
			//Situacion I : REG a la derecha
			if (nodoIzq.esRefMem()) {
				assembler.append("MOV [" + nodoIzq.getNombre() + "]," + nodoDer.getNombre() + "\n"); //MOV [BX],AX
				registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
			} else {
				assembler.append("MOV _" + nodoIzq.getNombre() + "," + nodoDer.getNombre() + "\n"); //MOV _a,AX
			}		
		    registros[nodoDer.getNroReg()] = "L";
		    
		} else {
			//Situacion II : CTE o VAR a la derecha
			int nroReg = primerRegLibre();
			Token tokenDerecha = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			
			if (nroReg != -1) {
				registros[nroReg] = "O";	
				
				String nombreReg = hashRegs.get(nroReg);
				
				if (tokenDerecha.getUso().equals(Token.USO_CONSTANTE)) {
					if (nodoDer.getTipoDeDato().equals("ulong")) {
						assembler.append("MOV E" + nombreReg + "," + nodoDer.getNombre()+ "\n"); //MOV ECX,40.000
						if (nodoIzq.esRefMem()) {
							assembler.append("MOV [" + nodoIzq.getNombre() + "],E" + nombreReg + "\n"); //MOV _a,ECX
							registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
						}else {
							assembler.append("MOV _" + nodoIzq.getNombre() + ",E" + nombreReg + "\n"); //MOV [BX],ECX
						}
									
					} else if (nodoDer.getTipoDeDato().equals("int")) {
						assembler.append("MOV " + nombreReg + "," + nodoDer.getNombre() + "\n"); //MOV CX,2
						if (nodoIzq.esRefMem()) {
							assembler.append("MOV [" + nodoIzq.getNombre() + "]," + nombreReg + "\n"); //MOV [BX],CX
							registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
						} else {
							assembler.append("MOV _" + nodoIzq.getNombre() + "," + nombreReg + "\n"); //MOV _a,CX
						}
						
					}
				} else if (tokenDerecha.getUso().equals(Token.USO_VARIABLE)) {
					if (nodoDer.getTipoDeDato().equals("ulong")) {
						assembler.append("MOV E" + nombreReg + ",_" + nodoDer.getNombre() + "\n"); //MOV ECX,_b
						if (nodoIzq.esRefMem()) {
							assembler.append("MOV [" + nodoIzq.getNombre() + "],E" + nombreReg + "\n"); //MOV [BX],ECX
							registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
						} else {
							assembler.append("MOV _" + nodoIzq.getNombre() + ",E" + nombreReg + "\n"); //MOV _a,ECX
						}					
						
					} else if (nodoDer.getTipoDeDato().equals("int")) {
						assembler.append("MOV " + nombreReg + ",_" + nodoDer.getNombre() + "\n"); //MOV CX,_b
						if (nodoIzq.esRefMem()) {
							assembler.append("MOV [" + nodoIzq.getNombre() + "]," + nombreReg + "\n"); //MOV [BX],CX
							registros[getNroReg(nodoIzq.getNombre())] = "L"; //Libero el registro que tenia la pos de memoria
						} else {
							assembler.append("MOV _" + nodoIzq.getNombre() + "," + nombreReg + "\n"); //MOV _a,CX
						}
						
					}
				}
				registros[nroReg] = "L";
				
			} else {
				System.out.println("No hay registros libres");
			}	
		}
		nodo.reemplazar(nodoIzq.getNombre());
	}
	
	
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
	
    private void generarThen (NodoArbol nodo) {
		assembler.append("JMP LabelSiguiente" + "\n");
		assembler.append("LabelElse:" + "\n");
		
		nodo.reemplazar(nodo.getNombre());
	}
	
	// -----------------------------------------------------------
	
	private void generarPrint (NodoArbol nodo) {		
		NodoArbol nodoMensaje = nodo.getNodoIzq();
		assembler.append("invoke MessageBox, NULL, addr " + nodoMensaje.getNombre() + ", addr " + nodoMensaje.getNombre() + ", MB_OK" + "\n");
		nodo.reemplazar(nodo.getNombre());
	}
	
	// -----------------------------------------------------------
	
	private void generarCondicionForeach (NodoArbol nodo) {
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
		int tamanioDeDato = 8;

		if (opDer.getTipoDeDato() == AnalizadorLexico.TIPO_DATO_ENTERO)
			tamanioDeDato = 4;

		String iterador = generarNombreVarAux();
		assembler.append("MOV " + iterador + ",0" + "\n"); // MOV iterador, 0 //pongo iterador en 0
		assembler.append("LabelCondForech:" + "\n"); //pongo Label
		
		assembler.append("CMP " + iterador + "," + opDer.getTamanio() + "\n"); //CMP iterador,3 (tamanio = 3)
		assembler.append("JGE LabelSiguienteForeach" + "\n"); //si es mayor igual a tamanio de 'b' corto

		//Hago asignacion a := b[iterador]; para la asignacion necesito un registro
		int regLibre = primerRegLibre(); 
		if (regLibre != -1) { // si hay algun registro libre
			registros[regLibre] = "O";

			//En aux3 guardo temporalmente el offset de b
			assembler.append("MOV @aux3,"+ iterador + "\n"); //MOV @aux3, iterador
			assembler.append("MUL @aux3,"+ tamanioDeDato + "\n"); //MUL @aux3, tamanioDeDato

			//Guardo la pos de mem en donde esta el elemento actual
			assembler.append("ADD @aux3, offset _"+ nodoDer.getNombre() + "\n");

			//Guardo el valor del elem actual en regLibre
			assembler.append("MOV " + hashRegs.get(regLibre) + ",[@aux3]" + "\n"); //MOV AX, [@aux3]

			//Paso el valor a "_a"
			assembler.append("MOV _" + nodoIzq.getNombre() + "," + hashRegs.get(regLibre) + "\n"); //MOV _a, AX

			//Incremento en uno el iterador y libero el registro
			assembler.append("ADD " + iterador + ",1" + "\n");
			registros[regLibre] = "L";
		}
		
		nodo.reemplazar(nodo.getNombre());
	}
	
	private String generarNombreVarAux() {
		String result = "@itForeach" + nroUltVarAux;
		nroUltVarAux++;
		return result;
	}
	
	// -----------------------------------------------------------
	
	private void generarCuerpoForeach(NodoArbol nodo) {
		assembler.append("JMP LabelCondForech" + "\n");
		assembler.append("LabelSiguienteForeach:" + "\n");
		//liberar registro en donde estaba el iterador
		nodo.reemplazar(nodo.getNombre());
	}
	
	// -----------------------------------------------------------
	
	private void generarConversion(NodoArbol nodo) {
		NodoArbol nodoIzq = nodo.getNodoIzq();
		Token token = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
		
		if (!(nodoIzq.getTipoDeDato().equals("ulong"))) { // si no es ulong
			
			int nroRegResult = primerRegLibre();
			String regResult = hashRegs.get(nroRegResult);
			
			if (nroRegResult != -1) {
				registros[nroRegResult] = "O";
				assembler.append("MOV E" + regResult + ",0" + "\n"); // MOV ECX,0 
			}
			
			if ( (nodoIzq.esRefMem()) || (nodoIzq.esRegistro()) || (token.getUso().equals(Token.USO_CONSTANTE))) {
				assembler.append("CMP " + nodoIzq.getNombre() + ",0" + "\n"); //CMP 20,0  CMP AX,0
			} else {
				assembler.append("CMP _" + nodoIzq.getNombre() + ",0" + "\n"); //CMP 20,0
			}
			
			assembler.append("JL LabelError" + "\n");
			
			if (nodoIzq.esRefMem()) {
				assembler.append("MOV " + regResult + ",[" + nodoIzq.getNombre() + "]\n"); // MOV CX,[BX]
			} else if ((nodoIzq.esRegistro()) || (token.getUso().equals(Token.USO_CONSTANTE))){ 
				assembler.append("MOV " + regResult + "," + nodoIzq.getNombre() + "\n"); // MOV CX,BX  MOV CX,20
			} else if (token.getUso().equals(Token.USO_VARIABLE)) {
				assembler.append("MOV " + regResult + ",_" + nodoIzq.getNombre() + "\n"); //MOV CX,_c
			}
			
			if ( (nodoIzq.esRefMem()) || (nodoIzq.esRegistro()) ) {
				assembler.append("MOV E" + nodoIzq.getNombre() + ",E" + regResult + "\n"); // MOV EBX, ECX
				nodo.reemplazar("E"+ nodoIzq.getNombre(), nodoIzq.getNroReg());
				registros[nroRegResult] = "L";
			} else { // CTE -> dejo el dato en ECX (el resultado)
				nodo.reemplazar("E"+ regResult, nroRegResult);
			}		
		
		} else { // ya es ulong
			nodo.reemplazar(nodoIzq.getNombre());
		}	
	}

	// -----------------------------------------------------------
	
	private void generarElemColec(NodoArbol nodo) {
		NodoArbol nodoIzq = nodo.getNodoIzq(); //b
		NodoArbol nodoDer = nodo.getNodoDer(); //2
		
		int nroReg = primerRegLibre();
		
		if (nroReg != -1) {
			registros[nroReg] = "O";
			
			if (nodoIzq.getTipoDeDato().equals("ulong")) {
				assembler.append("MOV E" + hashRegs.get(nroReg) + ", offset _" + nodoIzq.getNombre() + "\n"); //MOV EAX, offset _b
				assembler.append("MOV @aux2,8" + "\n");
				assembler.append("MUL @aux2," + nodoDer.getNombre() + "\n"); //MUL @aux2,2
				assembler.append("ADD E" +  hashRegs.get(nroReg) + ",@aux2" + "\n"); //ADD EAX, @aux2 (me desplazo hasta la pos)
				nodo.reemplazar("E" + hashRegs.get(nroReg));
				nodo.setTipoDeDato("ulong");
				nodo.setEsRefMem(nroReg);
			} else {
				assembler.append("MOV " + hashRegs.get(nroReg) + ", offset _" + nodoIzq.getNombre() + "\n"); //MOV AX, offset _b
				assembler.append("MOV @aux2,4" + "\n");
				assembler.append("MUL @aux2," + nodoDer.getNombre() + "\n"); //MUL @aux2,2
				assembler.append("ADD " +  hashRegs.get(nroReg) + ",@aux2" + "\n"); //ADD EAX, @aux2 (me desplazo hasta la pos)
				nodo.reemplazar(hashRegs.get(nroReg));
				nodo.setTipoDeDato("int");
				nodo.setEsRefMem(nroReg);				
			}
		}	
	}
	
	// -----------------------------------------------------------
}
