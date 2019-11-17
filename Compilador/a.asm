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
cadena1 DB "No Es 1",0
_a DW ?
cadena0 DB "Es 1",0
_b DW ?
_c DD ?
_d DW 1,2,3
@itForeach0 DW ?
.code
start:
MOV @itForeach0,0
LabelCondForech0:
CMP @itForeach0,3
JGE LabelSiguienteForeach0
MOV AX,@itForeach0
MUL AX,8
ADD AX, offset _d
MOV AX,[AX]
MOV _a,AX
ADD @itForeach0,1
CMP _a,1
JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
invoke MessageBox, NULL, addr cadena1, addr TituloCadena, MB_OK
LabelSiguiente0:
JMP LabelCondForech0
LabelSiguienteForeach0:
LabelError:
invoke ExitProcess, 0
end start
