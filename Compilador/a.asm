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
_d DD 2,2,3
_w DD ?
.code
start:
MOV EAX,0
MOV AX,6
CMP AX,0
JL LabelError
MOV @aux1,EBX
MOV EBX,EAX
MOV EAX,@aux1
MOV EAX,8
IMUL EAX,0
ADD EAX, offset _d
MOV @aux1,EBX
MOV EBX,EAX
MOV EAX,@aux1
MOV EDX,[EBX]
MUL EDX
MOV _c,EAX
MOV EAX,_c
MOV CX,12
CMP AX,CX
JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
invoke MessageBox, NULL, addr cadena1, addr TituloCadena, MB_OK
LabelSiguiente0:
LabelError:
invoke ExitProcess, 0
end start
