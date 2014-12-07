% TAYLOR

syms x y z

% tolle funktion eingeben
f = x/(x^2 + 1)^(1/2)

% taylor im punkt 1 mit der 2. Ordnung: (->order +1 für matlab)
ftaylor = taylor(f, 'expansionPoint', 1, 'order', 3) 

% differenz der beiden Funktionen
d0 = inline(vectorize(abs(f-ftaylor)))

% wo ist die Abweichung der Taylor Funktion kleiner als 0.1 ? Also |f-ftaylor| < 0.1
% => Suche "Nullstellen" im Punkt 0.1
ezplot(d0) % um herauszufinden wo ungefähr die Nullstellen sind

% Nullstelle 1 im Punkt 0.1, näher bei 0
fzero(@(x) d0(x) - 0.1, 0)
% => ans = -0.718405165488136

% Nullstelle 1 im Punkt 0.1, näher bei 2
fzero(@(x) d0(x) - 0.1, 2)
% => ans = 2.003998784947980

% Damit ergibt sich, dass die Taylorfunktion beim Intervall [-0.718, 2.004]
% genau genug ist, bzw. Abweichung < 0.1





% ABSTAND zwischen zwei "Teilchen". Teilchen auf g ist immer um 1 weiter
% als bei f.
f = ...
g = ...
% differenz zwischen zwei punkten auf der Ebene
% d(x1,x2,y1,y2) = sqrt((x1-x2)^2 + (y1-y2)^2)
d= sqrt( ( x-(x+1) )^2  +  ( f(x) - g(x+1) )^2  )

% Extremalstellen herausfinden:
extrm = solve(diff(d))
% Gibt Punkte von x aus, wo Abstand minimal ist.

f(extrm) % In f und g einsetzen, um y Werte zu erhalten.

% Lösung: A(x, y), B(x+1, y)



% BESTIMMTE INTEGRATION zweier Funktionen 
% Funktionen:
syms x
f = ...
g = ...
% Schnittpunkte herausfinden
s = sort(solve(f==g, x))
% Wenn nötig, weitere Nullpunkte finden
n = fzero(f, 2)
% Einzelner Nullpunkt zu s hinzufügen
% Array IMMER sortieren!
s = sort([s; n])

% Für jeden Punkt in s die Flächen herausfinden
a0 = abs(double(int(f-g,s(1),s(2))))
a1 = abs(double(int(f-g,s(2),s(3))))
% usw. bis alle Flächen gerechnet

% Endergebnis:
a0+a1+a2+...


