package analizadorSintactico;

import java.util.HashMap;

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
	
	public void traducir () {
		assembler.append(".code" + "\n" + "start:" + "\n");
		
		
		
		assembler.append("end start" + "\n" );
	}
	
	// -----------------------------------------------------------
	
	private void generarSuma (NodoArbol nodo) {
		
		NodoArbol nodoIzq = nodo.getNodoIzq();
		NodoArbol nodoDer = nodo.getNodoDer();
		
		if (!(nodoIzq.esRegistro()) && !(nodoDer.esRegistro())) { //Si ninguno es registro son var o const los dos
		//Situacion 1 (VAR - VAR o CONST-CONST)
			
			Token opIzq = AnalizadorLexico.tablaSimbolos.get(nodoIzq.getNombre());
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
				
			int reg = primerRegLibre();
			if (reg != -1) { // si hay algun registro libre	
				if ((nodoIzq.getTipoDeDato() == "ULONG") && (nodoDer.getTipoDeDato() == "ULONG")) { 
				// Sitacion 1a (32 bits)
					assembler.append("MOV E" + hashRegs.get(reg) + "," + opIzq.getValor() + "\n"); //MOV EAX,valor1
					assembler.append("ADD E" + hashRegs.get(reg) + "," + opDer.getValor() + "\n"); //ADD EAX,valor2
					//Actualizo el arbol
					nodo.reemplazar("E" + hashRegs.get(reg)); 
				} else if ((nodoIzq.getTipoDeDato() == "INT") && (nodoDer.getTipoDeDato() == "INT")) { 
				// Situacion 1b (16 bits)
					assembler.append("MOV " + hashRegs.get(reg) + "," + opIzq.getValor() + "\n"); //MOV AX,valor1
					assembler.append("ADD " + hashRegs.get(reg) + "," + opDer.getValor() + "\n"); //ADD AX,valor2
					//Actualizo el arbol
					nodo.reemplazar(hashRegs.get(reg));
				}
				registros[reg] = "O";
				
			}else {
				System.out.println("No hay registros libres");
				// ver que pasa si no hay ningun registro libre
			}	
			
		} else if ( (nodoIzq.esRegistro()) || (nodoDer.esRegistro()) ) { 
		// Situacion 2 (REG - VAR/CONST)	
		// Situacion 4 (VAR/CONST - REG) (Operacion conmutativa)	
			
			Token opDer = AnalizadorLexico.tablaSimbolos.get(nodoDer.getNombre());
			String registro = hashRegs.get(Integer.parseInt(nodoIzq.getNombre()));
			
			//Genero codigo sobre el registro
			assembler.append("ADD "+ registro + "," + opDer.getValor() + "\n"); //ADD AX,valor
			
			//Actualizo el arbol
			nodo.reemplazar(registro);
			
		} else if ( (nodoIzq.esRegistro()) && (nodoDer.esRegistro()) ) {
		//Situacion 3 (REG - REG)
			int nroReg1 = Integer.parseInt(nodoIzq.getNombre());
			int nroReg2 = Integer.parseInt(nodoDer.getNombre());
			String registro1 = hashRegs.get(nroReg1);
			String registro2 = hashRegs.get(nroReg2);
			
			//Genero codigo sobre el primer registro
			assembler.append("ADD "+ registro1 + "," + registro2 + "\n"); //ADD AX,BX
			registros[nroReg2] = "L"; //Libero el 2do registro
			
			//Actualizo el arbol
			nodo.reemplazar(registro1);
		} 	
		
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
	

	
}
