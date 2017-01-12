% Ajout de predicats pour le or
or(X,_) :- X.
or(_,X) :- X.

and(X,Y) :- X,Y.

% Est-ce que l'agent est sur un High Point ?
% Vrai si  l'agent X est sur un HighPoint
isOnHP(X):-jpl_call('sma.prolog.PrologStrategies',isOnHP,[X],R), jpl_is_true(R).

% Meme idee que pour isOnHP
isOnLP(X):-jpl_call('sma.prolog.PrologStrategies',isOnLP,[X],R), jpl_is_true(R).

enemyCoord(X,Y) :-
        jpl_call('sma.prolog.PrologStrategies',enemyCoordY,[X],C),
		Y=C.

% Vrai si l'agent ennemi est dans le champs de vision de l'agent allie
enemyInSight(X) :-
        jpl_call('sma.prolog.PrologStrategies',enemyInSight,[X],R),
        jpl_is_true(R).

% Vrai s'il y a dans le champs de vue de l'agent X
% un point plus haut son altitude
isThereHigherPoint(X) :-
        jpl_call('sma.prolog.PrologStrategies',isThereHigherPoint,[X],R),
        jpl_is_true(R).

% Vrai s'il y a dans le champs de vue de l'agent X
% un point plus bas son altitude
isThereLowerPoint(X) :-
        jpl_call('sma.prolog.PrologStrategies',isThereLowerPoint,[X],R),
        jpl_is_true(R).

% Vrai si cela fait plus de Y temps que l'agent X n'a pas vu d'ennemi
noSeeTime(X,Y) :-
        jpl_call('sma.prolog.PrologStrategies',noSeeTime,[X],R),
        Y < R.

% Renvoie les coordonnees de l'agent X sous forme coord(x,y,z) dans Y
coordOf(X,Y) :-
        jpl_call('sma.prolog.PrologStrategies',coordYOf,[X],C),
		Y=C.

% Vrai si l'agent ennemi est en position de force par rapport a l'agent allie
enemyInForce(X) :-
        not(isOnHP(X)),
        isEnemyHigher(X).

% Vrai si l'agent Y est plus haut que l'agent X
isEnemyHigher(X) :-
		enemyInSight(X),
        coordOf(X, R),
		%jpl_call('sma.prolog.PrologStrategies', debug,[],@(void)),
        %R=B,
		enemyCoord(X,S),
        %S=E,
		%jpl_call('sma.prolog.PrologStrategies', debug,[R],@(void)),
		%jpl_call('sma.prolog.PrologStrategies', debug,[S],@(void)),
        R+5 < S.

% Vrai si l'agent X est en securite, Y correspondant a son ennemi
isSafe(X) :-
        or(noSeeTime(X, 3),
           and(enemyInSight(X),
               not(enemyInForce(X)))).

% Renvoie dans R le temps depuis que l'agent X a commence son role actuel
timeOfRole(X,R) :-
		jpl_call('sma.prolog.PrologStrategies', timeOfRole,[X],R).

timeSinceCreation(X, R) :-
		jpl_call('sma.prolog.PrologStrategies', timeSinceCreation,[X],R).

% Renvoie dans R le temps depuis que l'agent X n'a plus bouge
noMoveTime(X,R) :-
		jpl_call('sma.prolog.PrologStrategies', noMoveTime,[X],R).

nbHP(X, Y) :-
	jpl_call('sma.prolog.PrologStrategies', nbHP,[X],Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%% EXPLORER %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Nombre de HP detectes depuis le debut de l'exploration
newHPDetected(X,R) :- jpl_call('sma.prolog.PrologStrategies', newHPDetected,[X],R).

% Faut-il continuer l'exploration ?
critereExplorationDepasse(X) :-
	    newHPDetected(X,R),
        R < 5,
        timeOfRole(X, S),
        S < 30.

% Faut-il passer de explorateur a campeur ?
explorateurToCampeur(X) :-
        not(critereExplorationDepasse(X)),
        isSafe(X),
        not(enemyInSight(X)).

% Faut-il passer de explorateur a campeur ?
explorateurToAttack(X) :-
        enemyInSight(X),
        not(enemyInForce(X)).

% Faut-il passer de explorateur a campeur ?
explorateurToFuite(X) :-
        not(isSafe(X)).

% Choix du comportement a suivre
explorateur(X, campeur) :-
	timeSinceCreation(X,R),
	R > 20,
	explorateurToCampeur(X).

explorateur(X, attacker) :-
	timeSinceCreation(X,R),
	R > 20,
	explorateurToAttack(X).

explorateur(X, fuite) :-
	timeSinceCreation(X,R),
	R > 20,
	explorateurToFuite(X).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%% CAMPEUR %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Faut-il que l'agent X continue de camper ?
critereCampeurDepasse(X) :-
	isOnHP(X),
	noMoveTime(X, S),
	%jpl_call('sma.prolog.PrologStrategies', debug,[S],@(void)),
	S > 10.

critereCampeurDepasse(X) :-
	nbHP(X,Z),
	Z < 1.

% L'agent X, doit-il changer de Spot,
% sachant qu'il a deja campe a Y spots
switchCampingSpot(X, Y) :-
	Y < 3,
	nbHP(X,Z),
	Z > 1,
	isSafe(X),
	critereCampeurDepasse(X).
	%jpl_call('sma.prolog.PrologStrategies', debug,[],@(void)).

% L'agent X doit-il passer au role d'explorateur ?
% Z etant le nombre de points sur lesquels il a campe
campeurToExplorer(X, Z) :-
	isSafe(X),
	critereCampeurDepasse(X),
	nbHP(X,Y),
	or(Y < 2, Z > 2).

% L'agent cherche a fuir si il est en position de faiblesse, mais ne devrait pas arriver dans ce cas, car on est sense camper uniquement sur des positions de force.
campeurToFuite(X) :-
	enemyInForce(X).

% L'agent X doit-il chasser son adversaire ?
campeurToAttack(X) :-
	enemyInSight(X),
	not(enemyInForce(X)).

campeur(X, switch) :-
	jpl_call('sma.prolog.PrologStrategies', spots,[X],Z),
	switchCampingSpot(X,Z).

campeur(X, explorer) :-
	jpl_call('sma.prolog.PrologStrategies', spots,[X],Z),
	campeurToExplorer(X,Z).

campeur(X, fuite) :- campeurToFuite(X).
campeur(X, attacker) :- campeurToAttack(X).

%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%  Attacker  %%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%

% Criteres d'arret du role d'attaquant
critereAttackerDepasse(X) :-
	noSeeTime(X, 5).

attackerToFuite(X) :-
	enemyInForce(X).

attackerToCampeur(X) :-
	nbHP(X,Z),
	Z > 1,
	critereAttackerDepasse(X).

attackerToExplorer(X) :-
	nbHP(X,Z),
	Z < 2,
	critereAttackerDepasse(X).
	

attacker(X, fuite) :- attackerToFuite(X).
attacker(X, campeur) :- attackerToCampeur(X).
attacker(X, explorer) :- attackerToExplorer(X).

%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%  Fuyard  %%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%

fuite(X, campeur) :-
	nbHP(X,Z),
	Z > 1,
	noSeeTime(X,5).

fuite(X, attacker) :-
	enemyInSight(X),
	not(enemyInForce(X)).

fuite(X, explorer) :-
	nbHP(X,Z),
	Z < 2,
	noSeeTime(X,5).
	








