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
@aux2 DW ?
cadena1 DB "b es 40000",0
_a DW ?
cadena0 DB "a es 2",0
_b DD ?
_c DW ?
_d DD ?
_e DW 3,6,1
_f DD 3 DUP (?)
cadena6 DB "f[0] es 6",0
cadena5 DB "e[2] es 6",0
cadena4 DB "a es 6",0
cadena3 DB "d es 40000",0
cadena2 DB "c es 2",0
.code
start:
MOV AX,2
MOV _a,AX
MOV AX,_a
MOV BX,2
CMP AX,BX
JNE LabelElse0
invoke MessageBox, NULL, addr cadena0, addr TituloCadena, MB_OK
JMP LabelSiguiente0
LabelElse0:
LabelSiguiente0:
MOV EAX,40000
MOV _b,EAX
MOV EAX,_b
MOV EBX,40000
CMP EAX,EBX
JNE LabelElse1
invoke MessageBox, NULL, addr cadena1, addr TituloCadena, MB_OK
JMP LabelSiguiente1
LabelElse1:
LabelSiguiente1:
MOV AX,_a
MOV _c,AX
MOV AX,_c
MOV BX,2
CMP AX,BX
JNE LabelElse2
invoke MessageBox, NULL, addr cadena2, addr TituloCadena, MB_OK
JMP LabelSiguiente2
LabelElse2:
LabelSiguiente2:
MOV EAX,_b
MOV _d,EAX
MOV EAX,_d
MOV EBX,40000
CMP EAX,EBX
JNE LabelElse3
invoke MessageBox, NULL, addr cadena3, addr TituloCadena, MB_OK
JMP LabelSiguiente3
LabelElse3:
LabelSiguiente3:
MOV EDX,1
CMP EDX,3
JGE LabelError
CMP EDX,0 
JL LabelError
MOV EAX,2
MUL EDX
ADD EAX, offset _e
MOV BX,[EAX]
MOV _a,BX
MOV AX,_a
MOV BX,6
CMP AX,BX
JNE LabelElse4
invoke MessageBox, NULL, addr cadena4, addr TituloCadena, MB_OK
JMP LabelSiguiente4
LabelElse4:
LabelSiguiente4:
MOV EDX,2
CMP EDX,3
JGE LabelError
CMP EDX,0 
JL LabelError
MOV EAX,2
MUL EDX
ADD EAX, offset _e
MOV BX,_a
MOV [EAX],BX
MOV EDX,2
CMP EDX,3
JGE LabelError
CMP EDX,0 
JL LabelError
MOV EAX,2
MUL EDX
ADD EAX, offset _e
MOV AX,[EAX] 
MOV BX,6
CMP AX,BX
JNE LabelElse5
invoke MessageBox, NULL, addr cadena5, addr TituloCadena, MB_OK
JMP LabelSiguiente5
LabelElse5:
LabelSiguiente5:
MOV EDX,0
CMP EDX,3
JGE LabelError
CMP EDX,0 
JL LabelError
MOV EAX,4
MUL EDX
ADD EAX, offset _f
MOV @aux1,EBX
MOV EBX,EAX
MOV EAX,@aux1
MOV EDX,2
CMP EDX,3
JGE LabelError
CMP EDX,0 
JL LabelError
MOV EAX,2
MUL EDX
ADD EAX, offset _e
MOV ECX,0
MOV CX,[EAX]
CMP CX,0
JL LabelError
MOV [EBX],ECX
MOV EDX,0
CMP EDX,3
JGE LabelError
CMP EDX,0 
JL LabelError
MOV EAX,4
MUL EDX
ADD EAX, offset _f
MOV EBX,0
MOV BX,6
CMP BX,0
JL LabelError
MOV EAX,[EAX] 
CMP EAX,EBX
JNE LabelElse6
invoke MessageBox, NULL, addr cadena6, addr TituloCadena, MB_OK
JMP LabelSiguiente6
LabelElse6:
LabelSiguiente6:
LabelError:
invoke ExitProcess, 0
end start
