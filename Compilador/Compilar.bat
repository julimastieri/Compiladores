echo off
:inicio
cls
set path=%PATH%;c:\masm32\bin
echo Introduzca el nombre del archivo (ej prueba.asm):
echo para salir: "exit"

cls
ml /Cp /coff "a.asm" /link /subsystem:console
pause
goto inicio
:salir
exit
