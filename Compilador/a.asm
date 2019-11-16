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
_a DD ?
_b DW 1,2,3
_c DW ?
_w DD ?
_z DW 4 DUP ?
.code
start:
CMP 2,0
JE LabelError
MOV AX,10
MOV DX,2
IDIV AX,DX
MOV _c,AX
LabelError:
invoke ExitProcess, 0
end start
