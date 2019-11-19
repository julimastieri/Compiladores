.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
TituloCadena db "Cadena",0
MsgError db "Error en ejecucion",0
@aux1 DD ?
@aux2 DW ?
cadena1 DB " b es 58000 ",0
cadena0 DB " c es 3 ",0
_b DD ?
_c DW ?
_d DD ?
_g DW 1,2,3
_h DD 40000,58000
_i DW ?
cadena2 DB " g[1] es 10 ",0
.code
start:
MOV EAX,2
IMUL EAX,2
ADD EAX, offset _g
MOV BX,[EAX]
MOV _c,BX
MOV AX,_c
MOV BX,3
CMP AX,BX
JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
LabelSiguiente0:
MOV EAX,4
IMUL EAX,1
ADD EAX, offset _h
MOV EBX,[EAX]
MOV _b,EBX
MOV EAX,_b
MOV EBX,58000
CMP EAX,EBX
JNE LabelElse1
invoke MessageBox, NULL, addr cadena1, addr TituloCadena, MB_OK
JMP LabelSiguiente1
LabelElse1:
LabelSiguiente1:
MOV EAX,2
IMUL EAX,1
ADD EAX, offset _g
MOV BX,10
MOV [EAX],BX
MOV EAX,2
IMUL EAX,1
ADD EAX, offset _g
MOV EAX,[EAX]
MOV CX,10
CMP BX,CX
JNE LabelElse2
invoke MessageBox, NULL, addr cadena2, addr TituloCadena, MB_OK
JMP LabelSiguiente2
LabelElse2:
LabelSiguiente2:
LabelError:
invoke ExitProcess, 0
end start
