package analizadorSintactico;

import java.util.HashMap;
import java.util.IllegalFormatCodePointException;

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
			
			
			if (nodo.getNombre() == "+") {
				generarSuma(nodo);
				imprimirArbolmod(raiz, "");	
			} else if (nodo.getNombre() == "-") {
				generarResta(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre() == "*") {
				generarMultiplicacion(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre() == "/") {
				generarDivision(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre() == ":=") {
				generarAsignacion(nodo);
				imprimirArbolmod(raiz, "");
			} else if ((nodo.getNombre().equals(">=")) || (nodo.getNombre().equals("<=")) || (nodo.getNombre().equals("==")) || (nodo.getNombre().equals("<>")) || (nodo.getNombre().equals("<")) || (nodo.getNombre().equals(">"))){
				generarComparacion(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre() == "CONDICION") {
				generarCondicion(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre() == "Sentencia Ejecutable") {
				nodo.reemplazar(nodo.getNombre()); // Le elimino los hijos
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre() == "THEN") {
				generarThen(nodo);
				imprimirArbolmod(raiz, "");
			} else if (nodo.getNombre() == "ELSE") {
				generarElse(nodo);
				imprimirArbolmod(raiz, "");
			} else if ((nodo.getNombre() == "CUERPO") || (nodo.getNombre() == "IF") ) {
				nodo.reemplazar(nodo.getNombre());
				imprimirArbolmod(raiz, "");
			} 
			
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
		//Situacion 1 (VAR - VAR o CONST-CONST)
			//Falta probar var - var
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			
			int reg = primerRegLibre();
			
			if (reg != -1) { // si hay algun registro libre
				registros[reg] = "O";
				
				if ((nodoIzq.getTipoDeDato() == "ulong") && (nodoDer.getTipoDeDato() == "ulong")) { 
				// Sitacion 1a (32 bits)
					
					if ((opIzq.getUso() == Token.USO_CONSTANTE) && (opDer.getUso() == Token.USO_CONSTANTE)) {
						assembler.append("MOV E" + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
						assembler.append("ADD E" + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD EAX,30800
					} else if ((opIzq.getUso() == Token.USO_VARIABLE) && (opDer.getUso() == Token.USO_VARIABLE)) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_a
						assembler.append("ADD E" + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD EAX,_b
					} else if ((opIzq.getUso() == Token.USO_CONSTANTE) && (opDer.getUso() == Token.USO_VARIABLE)) {
						assembler.append("MOV E" + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX, 30m
						assembler.append("ADD E" + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD EAX,_b
					} else if ((opIzq.getUso() == Token.USO_VARIABLE) && (opDer.getUso() == Token.USO_CONSTANTE)) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_b
						assembler.append("ADD E" + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD EAX,40m
					}
					
					//Actualizo el arbol
					nodo.reemplazar("E" + hashRegs.get(reg), reg);
					
				} else if ((nodoIzq.getTipoDeDato() == "int") && (nodoDer.getTipoDeDato() == "int")) { 
				// Situacion 1b (16 bits)

					if ((opIzq.getUso() == Token.USO_CONSTANTE) && (opDer.getUso() == Token.USO_CONSTANTE)) {
						assembler.append("MOV " + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV AX,40000
						assembler.append("ADD " + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //ADD AX,30800
					} else if ((opIzq.getUso() == Token.USO_VARIABLE) && (opDer.getUso() == Token.USO_VARIABLE)) {
						assembler.append("MOV " + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV AX,_a
						assembler.append("ADD " + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD AX,_b
					} else if ((opIzq.getUso() == Token.USO_CONSTANTE) && (opDer.getUso() == Token.USO_VARIABLE)) {
						assembler.append("MOV " + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV AX, 30m
						assembler.append("ADD " + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //ADD AX,_b
					} else if ((opIzq.getUso() == Token.USO_VARIABLE) && (opDer.getUso() == Token.USO_CONSTANTE)) {
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
			
			if (opDer.getUso() == Token.USO_CONSTANTE) {
				assembler.append("ADD "+ registro + "," + nodoDer.getNombre() + "\n"); //ADD AX,3
			} else if (opDer.getUso() == Token.USO_VARIABLE) {
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
			
			if (opIzq.getUso() == Token.USO_CONSTANTE) {
				assembler.append("ADD "+ registro + "," + nodoIzq.getNombre() + "\n"); //ADD AX,3
			} else if (opIzq.getUso() == Token.USO_VARIABLE) {
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
				
				if ((nodoIzq.getTipoDeDato() == "ulong") && (nodoDer.getTipoDeDato() == "ulong")) { 
				// Situacion 1a (32 bits)
					
					if ((opIzq.getUso() == Token.USO_CONSTANTE) && (opDer.getUso() == Token.USO_CONSTANTE)) {
						assembler.append("MOV E" + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX,40000
						assembler.append("SUB E" + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //SUB EAX,30800
					} else if ((opIzq.getUso() == Token.USO_VARIABLE) && (opDer.getUso() == Token.USO_VARIABLE)) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_a
						assembler.append("SUB E" + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //SUB EAX,_b
					} else if ((opIzq.getUso() == Token.USO_CONSTANTE) && (opDer.getUso() == Token.USO_VARIABLE)) {
						assembler.append("MOV E" + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX, 30m
						assembler.append("SUB E" + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //SUB EAX,_b
					} else if ((opIzq.getUso() == Token.USO_VARIABLE) && (opDer.getUso() == Token.USO_CONSTANTE)) {
						assembler.append("MOV E" + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_b
						assembler.append("SUB E" + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //SUB EAX,40m
					}
					
					//Actualizo el arbol
					nodo.reemplazar("E" + hashRegs.get(reg), reg);
					
				} else if ((nodoIzq.getTipoDeDato() == "int") && (nodoDer.getTipoDeDato() == "int")) { 
				// Situacion 1b (16 bits)
					
					if ((opIzq.getUso() == Token.USO_CONSTANTE) && (opDer.getUso() == Token.USO_CONSTANTE)) {
						assembler.append("MOV " + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV AX,40000
						assembler.append("SUB " + hashRegs.get(reg) + "," + nodoDer.getNombre() + "\n"); //SUB AX,30800
					} else if ((opIzq.getUso() == Token.USO_VARIABLE) && (opDer.getUso() == Token.USO_VARIABLE)) {
						assembler.append("MOV " + hashRegs.get(reg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV AX,_a
						assembler.append("SUB " + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //SUB AX,_b
					} else if ((opIzq.getUso() == Token.USO_CONSTANTE) && (opDer.getUso() == Token.USO_VARIABLE)) {
						assembler.append("MOV " + hashRegs.get(reg) + "," + nodoIzq.getNombre() + "\n"); //MOV AX, 30m
						assembler.append("SUB " + hashRegs.get(reg) + ",_" + nodoDer.getNombre() + "\n"); //SUB AX,_b
					} else if ((opIzq.getUso() == Token.USO_VARIABLE) && (opDer.getUso() == Token.USO_CONSTANTE)) {
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
			
			if (opDer.getUso() == Token.USO_CONSTANTE) {
				assembler.append("SUB "+ registro + "," + nodoDer.getNombre() + "\n"); //SUB AX,3
			} else if (opDer.getUso() == Token.USO_VARIABLE) {
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
				
				if ( nodoDer.getTipoDeDato() == "ulong" ) { 
				// (32 bits)
					
					if (opIzq.getUso() == Token.USO_VARIABLE) {
						assembler.append("MOV E" + hashRegs.get(nuevoReg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV EAX,_b
					} else if (opIzq.getUso() == Token.USO_CONSTANTE) {
						assembler.append("MOV E" + hashRegs.get(nuevoReg) + "," + nodoIzq.getNombre() + "\n"); //MOV EAX,40.000	
					}
					assembler.append("SUB E" + hashRegs.get(nuevoReg) + "," + registro + "\n"); //SUB EAX,EBX
						
					//Actualizo el arbol
					nodo.reemplazar("E" + hashRegs.get(nuevoReg), nuevoReg);
					
				} else if (nodoDer.getTipoDeDato() == "int" ) { 
				// (16 bits)
					
					if (opIzq.getUso() == Token.USO_VARIABLE) {
						assembler.append("MOV " + hashRegs.get(nuevoReg) + ",_" + nodoIzq.getNombre() + "\n"); //MOV AX,_b
					} else if (opIzq.getUso() == Token.USO_CONSTANTE) {
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
	
	private void generarMultiplicacion (NodoArbol nodo) {
		
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		
		if (!(nodoIzq.esRegistro()) && !(nodoDer.esRegistro())) { //Si ninguno es registro son var o const los dos
		//Situacion 1 (VAR - VAR o CONST-CONST)
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			
			//Liberar registro 0 (EAX, AX)
			if (registros[0] == "O") {
				int proxLibre = primerRegLibre();
				if (proxLibre != -1) {
					String registroLibre = hashRegs.get(proxLibre);
					// HAY QUE VER QUIEN ESTA OCUPANDO EL REG 0
					// MOV registroLibre, EAX (PASO EL CONTENIDO DE UN REG A OTRO)
					// Buscar el nodo del arbol que estaba usando EAX/AX y cambiarle el nombre por registroLibre y el nro por proxLibre
				
				} else {
					// si no hay ninguno libre: VER
				}
			}
			
			if ((nodoIzq.getTipoDeDato() == "ulong") && (nodoDer.getTipoDeDato() == "ulong")) { 
			// Sitacion 1a (32 bits)
					
				assembler.append("MOV EAX," + opIzq.getValor() + "\n"); //MOV EAX,valor1
				assembler.append("IMUL EAX," + opDer.getValor() + "\n"); //IMUL EAX,valor2
					
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
					
			} else if ((nodoIzq.getTipoDeDato() == "int") && (nodoDer.getTipoDeDato() == "int")) { 
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
				if (registros[0] == "O") {
					// MOV @aux1, registro
					// MOV registro, EAX (PASO EL CONTENIDO DE UN REG A OTRO)
					// M0V EAX, @aux1
					
					// Buscar el nodo del arbol que estaba usando EAX/AX y cambiarle el nombre por registro y el nro por nroReg
					
				} else if (registros[0] == "L") {
					// MOV EAX, registro
					registros[nroReg] = "L";
					registros[0] = "O";
				}				
			}
			
			if (nodoDer.getTipoDeDato() == "ULONG") { 
				// 32 bits
				//Genero codigo sobre el registro
				assembler.append("IMUL EAX," + opDer.getValor() + "\n"); //IMUL EAX,valor
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if (nodoDer.getTipoDeDato() == "INT") {
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
			
			
			if ((nodoIzq.getTipoDeDato() == "ULONG") && (nodoDer.getTipoDeDato() == "ULONG")) {
				if (registro1 == "EAX") {
					assembler.append("MUL "+ registro1 + "," + registro2 + "\n"); //MUL EAX,EBX
					registros[nroReg2] = "L"; //Libero el 2do registro
					
				} else if (registro2 == "EAX") {
					assembler.append("MUL "+ registro2 + "," + registro1 + "\n"); //MUL EAX,EBX
					registros[nroReg1] = "L"; //Libero el 1er registro
					
				} else {
					//CHEQUEADO
					if (registros[0] == "O") {
						// MOV @aux1, registro1
						// MOV registro1, EAX (PASO EL CONTENIDO DE UN REG A OTRO)
						// M0V EAX, @aux1
						
						// Buscar el nodo del arbol que estaba usando EAX/AX y cambiarle el nombre por registro1 y el nro por nroReg1
						registros[nroReg2] = "L"; //Libero solo el 2 porque el 1 esta ocupado con lo que habia en AX
					
					} else if (registros[0] == "L") {
						registros[nroReg1] = "L"; //Libero el 1er registro
						registros[nroReg2] = "L"; //Libero el 2do registro
						registros[0] = "O";
						// MOV EAX, registro1
					}
					assembler.append("MUL EAX," + registro2 + "\n"); //MUL EAX,EBX
					
				}
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if ((nodoIzq.getTipoDeDato() == "INT") && (nodoDer.getTipoDeDato() == "INT")) {
				
				if (registro1 == "AX") {
					assembler.append("MUL "+ registro1 + "," + registro2 + "\n"); //MUL EAX,EBX
					registros[nroReg2] = "L"; //Libero el 2do registro
					
				} else if (registro2 == "AX") {
					assembler.append("MUL "+ registro2 + "," + registro1 + "\n"); //MUL EAX,EBX
					registros[nroReg1] = "L"; //Libero el 1er registro
					
				} else {
					//
					if (registros[0] == "O") {
						// MOV @aux1, registro1
						// MOV registro1, EAX (PASO EL CONTENIDO DE UN REG A OTRO)
						// M0V EAX, @aux1
						
						// Buscar el nodo del arbol que estaba usando EAX/AX y cambiarle el nombre por registro1 y el nro por nroReg1
						registros[nroReg2] = "L"; //Libero solo el 2 porque el 1 esta ocupado con lo que habia en AX
					
					} else if (registros[0] == "L") {
						registros[nroReg1] = "L"; //Libero el 1er registro
						registros[nroReg2] = "L"; //Libero el 2do registro
						registros[0] = "O";
						// MOV EAX, registro1
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
				if (registros[0] == "O") {
					// MOV @aux1, registro
					// MOV registro, EAX (PASO EL CONTENIDO DE UN REG A OTRO)
					// M0V EAX, @aux1
					
					// Buscar el nodo del arbol que estaba usando EAX/AX y cambiarle el nombre por registro y el nro por nroReg
					
				} else if (registros[0] == "L") {
					// MOV EAX, registro
					registros[nroReg] = "L";
					registros[0] = "O";
				}	
			}
			
			if (nodoIzq.getTipoDeDato() == "ULONG") { 
				// 32 bits
				//Genero codigo sobre el registro
				assembler.append("IMUL EAX," + opIzq.getValor() + "\n"); //IMUL EAX,valor
				
				//Actualizo el arbol
				nodo.reemplazar("EAX", 0);
				
			} else if (nodoDer.getTipoDeDato() == "INT") {
				// 16 bits
				//Genero codigo sobre el registro
				assembler.append("IMUL AX," + opIzq.getValor() + "\n"); //IMUL AX,valor
				
				//Actualizo el arbol
				nodo.reemplazar("AX", 0);
			}	
			
		}		
	}
	
	// -----------------------------------------------------------
	
	private void generarDivision (NodoArbol nodo) {
		
	}
	
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
				
				if (tokenDerecha.getUso() == Token.USO_CONSTANTE) {
					if (nodoDer.getTipoDeDato() == "ulong") {
						assembler.append("MOV E" + nombreReg + "," + nodoDer.getNombre()+ "\n"); //MOV ECX,40.000
						assembler.append("MOV _" + nodoIzq.getNombre() + ",E" + nombreReg + "\n"); //MOV _a,ECX
						
					} else if (nodoDer.getTipoDeDato() == "int") {
						assembler.append("MOV " + nombreReg + "," + nodoDer.getNombre() + "\n"); //MOV CX,2
						assembler.append("MOV _" + nodoIzq.getNombre() + "," + nombreReg + "\n"); //MOV _a,CX
					}
				} else if (tokenDerecha.getUso() == Token.USO_VARIABLE) {
					if (nodoDer.getTipoDeDato() == "ulong") {
						assembler.append("MOV E" + nombreReg + ",_" + nodoDer.getNombre() + "\n"); //MOV ECX,_b
						assembler.append("MOV _" + nodoIzq.getNombre() + ",E" + nombreReg + "\n"); //MOV _a,ECX
						
					} else if (nodoDer.getTipoDeDato() == "int") {
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
	
	private void generarComparacion (NodoArbol nodo) {
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		Token tokenDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
		Token tokenIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
		String comparador = nodo.getNombre();
		
		if ((tokenDer.getUso() == Token.USO_VARIABLE) && (tokenIzq.getUso() == Token.USO_VARIABLE)) {
			assembler.append("CMP _" + nodoIzq.getNombre() + ",_" + nodoDer.getNombre() + "\n");
		} else if ((tokenDer.getUso() == Token.USO_VARIABLE) && !(tokenIzq.getUso() == Token.USO_VARIABLE)) {
			assembler.append("CMP " + nodoIzq.getNombre() + ",_" + nodoDer.getNombre() + "\n");
		} else if (!(tokenDer.getUso() == Token.USO_VARIABLE) && (tokenIzq.getUso() == Token.USO_VARIABLE)) {
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
	
	private void generarElse (NodoArbol nodo) {
		assembler.append("LabelSiguiente:" + "\n");
		nodo.reemplazar(nodo.getNombre());
	}
	
}
