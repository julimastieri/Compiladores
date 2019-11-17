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
cadena1 DB "Anda Mal",0
_a DW ?
cadena0 DB "Anda Bien",0
_b DW ?
_c DD ?
_d DW 1,2,3
.code
start:
MOV EAX,4
IMUL EAX,0
ADD EAX, offset _d
MOV @aux1, EAX
MOV EAX, @aux1
MOV AX,[EAX]
MOV _a,AX
CMP _a,1
JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
invoke MessageBox, NULL, addr cadena1, addr TituloCadena, MB_OK
LabelSiguiente0:
LabelError:
invoke ExitProcess, 0
end start
