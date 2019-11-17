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
cadena1 DB "correcto2",0
_a DW ?
cadena0 DB "correcto1",0
_b DD ?
_c DW ?
_d DD ?
_e DW 3,2,5
_f DD 40000,50842
_g DW 4 DUP (?)
_h DD 3 DUP (?)
_i DW ?
_j DD ?
.code
start:
MOV EAX,4
IMUL EAX,0
ADD EAX, offset _e
MOV @aux1,EBX
MOV EBX,EAX
MOV EAX,@aux1
MOV AX,3
MOV DX,2
IMUL AX,DX
MOV BX,[EBX]
ADD BX,AX
MOV _a,AX
MOV AX,_a
MOV BX,9
CMP AX,BX
JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
LabelSiguiente0:
MOV EAX,8
IMUL EAX,0
ADD EAX, offset _f
MOV @aux1,EBX
MOV EBX,EAX
MOV EAX,@aux1
MOV EAX,40000
MOV EDX,34000
MUL EDX
MOV EBX,[EBX]
ADD EBX,EAX
MOV _b,EAX
MOV EAX,_b
MOV EBX,1360040000
CMP AX,BX
JNE LabelElse1
invoke MessageBox, NULL, addr cadena1, addr TituloCadena, MB_OK
JMP LabelSiguiente1
LabelElse1:
LabelSiguiente1:
LabelError:
invoke ExitProcess, 0
end start
