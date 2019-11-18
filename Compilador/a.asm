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
cadena1 DB "Anda Mal",0
_a DW ?
cadena0 DB "Anda Bien1",0
_b DW ?
_c DD ?
_d DW 2,2,3
_w DD ?
_z DD 32768,2,3
.code
start:
MOV AX,2
MOV _b,AX
MOV AX,2
CWD
MOV BX,2
CMP BX,0 
JE LabelError
IDIV BX 
MOV _a,AX
MOV AX,_a
MOV BX,1
CMP AX,BX
JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
invoke MessageBox, NULL, addr cadena1, addr TituloCadena, MB_OK
LabelSiguiente0:
LabelError:
invoke ExitProcess, 0
end start
