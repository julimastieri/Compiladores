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
@aux1 DD ?
_a DW ?
_b DD ?
_c DW ?
_d DD ?
_e DW 3,2,5
_f DD 40600,50842
_g DW 4 DUP (?)
_h DD 3 DUP (?)
.code
start:
MOV AX,2
MOV _c,AX
MOV AX, offset _g
MOV @aux1,4
MUL @aux1,2
ADD AX,@aux1
MOV BX,_c
MOV [AX],BX
LabelError:
invoke ExitProcess, 0
end start
