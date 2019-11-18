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
cadena0 DB "Anda Bien a",0
_b DW ?
_c DD ?
_d DW 1,2,3
_w DD ?
_z DD 2,2,3
cadena3 DB "Anda Mal",0
cadena2 DB "Anda Bien c",0
.code
start:
MOV AX,1
MOV _b,AX
MOV EAX,0
MOV AX,2
CMP AX,0
JL LabelError
MOV _w,EAX
MOV AX,1
MOV DX,1
IMUL AX,DX
MOV @aux2,BX
MOV BX,AX
MOV AX,@aux2
MOV AX,_b
CWD
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
MOV EAX,0
MOV AX,2
CMP AX,0
JL LabelError
MOV @aux1,EBX
MOV EBX,EAX
MOV EAX,@aux1
MOV EAX,_w
CDQ
CMP EBX,0 
JE LabelError
DIV EBX
MOV _c,EAX
MOV EAX,0
MOV AX,1
CMP AX,0
JL LabelError
MOV EBX,_c
CMP EBX,EAX
JNE LabelElse1
invoke MessageBox, NULL, addr cadena2, addr TituloCadena, MB_OK
JMP LabelSiguiente1
LabelElse1:
invoke MessageBox, NULL, addr cadena3, addr TituloCadena, MB_OK
LabelSiguiente1:
LabelError:
invoke ExitProcess, 0
end start
