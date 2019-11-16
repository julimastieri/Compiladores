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
cadena1 DB "Anda Mal",0
_a DW ?
cadena0 DB "Anda Bien",0
_b DW ?
_c DD ?
_d DW 1,2,3
.code
start:
MOV AX,6
MOV _b,AX
MOV AX,2
MOV _a,AX
MOV AX,_b
MOV DX,_b
IMUL AX,DX
MOV @aux1,AX
MOV AX,AX
MOV AX,@aux1
MOV DX,_a
IMUL AX,DX
shl EDX,16
MOV DX,AX
MOV _a,EDX
CMP _a,38
JNE LabelElse
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente
LabelElse:
invoke MessageBox, NULL, addr cadena1, addr TituloCadena, MB_OK
LabelSiguiente:
LabelError:
invoke ExitProcess, 0
end start
