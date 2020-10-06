com (5 + 1) Function Tests
func void helloWorld
	call output "Hello World"
endfunc

func Integer add x y
	return x + y
endfunc

call helloWorld

var 0 in test typed Integer
var (test + 5) in parenthesis typed Integer
call output parenthesis
call add 5 1 in test
call output "Resultat :" test

call add 4 4 in test
call output "Resultat :" test

call add 27 33 in test
call output "Resultat :" test

com Var Initialisation
var "Hello World" in str typed String
var 10 in exemple typed Integer

com Tests On numbers
calc 5+10+exemple in exemple
call output "Resultat :" exemple

com Tests On Strings
call input "Enter \" something : " in str
call output str

com Tests On If
if exemple == 26
	call output "Yes !"
	if str == "moi"
		call output "Re Yes !"
	endif
endif
elif exemple == 27
	call output "Mother Fucker"
endelif
else
	call output "No..."
endelse

com Tests On Loop
calc 0 in exemple
while exemple < 5
	call output "i :" exemple
	calc exemple+1 in exemple
endwhile

for 0;1;5 in i
	call output "i:" i
endfor

com Test On Casts
call output "Init State"
calc 5 in exemple
call type exemple

cast exemple typed String
call output "Cast to String"
call type exemple

cast exemple typed Boolean
call output "Cast to Boolean"
call type exemple
call output exemple

com Test On Random
call random in exemple
call output "1er Random :" exemple

call random 5 10 in exemple
call output "2e Random :" exemple


com Test On Array
array arr sized 10 typed Integer
call output arr[0]
var 0 in size typed Integer
call sizeof arr in size
call output "Size :" size

calc "45" in arr[0]
call output "45 :" arr[0]
calc (arr[0] + 10) in arr[1]
call output "55 :" arr[1]
call output "50 :" (arr[0] + 5)

calc 78 in arr[1]
calc 1002515 in arr[2]
calc 741 in arr[4]

for 0;1;5 in i
	call output arr[i]
endfor

func void decompte x
call output x
calc x - 1 in x
if x > 0
call decompte x
endif
endfunc
call decompte 10

com End Program
call output "End"