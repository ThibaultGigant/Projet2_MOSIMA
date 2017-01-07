% Est-ce que l'agent est sur un High Point ?
% Vrai si les coordonnees X correspondent a une
% des coordonnees HP donnes en Atom
isOnHP(X):-hp(X).

% Meme idee que pour isOnHP
isOnLP(X):-lp(X).

% Vrai si l'agent ennemi est dans le champs de vision de l'agent allie
enemyInSight() :-
	jpl_call('sma.prolog.PrologStrategies',enemyInSight,[],R),
	jpl_is_true(R).

% Vrai s'il y a dans le champs de vue de l'agent allie
% un point plus haut son altitude
isThereHigherPoint() :-
	jpl_call('sma.prolog.PrologStrategies',isThereHigherPoint,[],R),
	jpl_is_true(R).

% Vrai s'il y a dans le champs de vue de l'agent allie
% un point plus bas son altitude
isThereLowerPoint() :-
	jpl_call('sma.prolog.PrologStrategies',isThereLowerPoint,[],R),
	jpl_is_true(R).

% Vrai si cela fait plus de Y temps que l'agent allie n'a pas bouge
noMoveTime(Y) :-
	jpl_call('sma.prolog.PrologStrategies',noMoveTime,[],R),
	Y > R.

% Vrai si cela fait plus de Y temps que l'agent allie n'a pas vu d'ennemi
noSeeTime(Y) :-
	jpl_call('sma.prolog.PrologStrategies',noSeeTime,[],R),
	Y > R.

% Renvoie les coordonnees de l'agent allie
allie(X) :-
	jpl_call('sma.prolog.PrologStrategies',allie,[],X).

% Renvoie les coordonnees de l'agent ennemi
enemy(Y) :-
	enemyInSight(),
	jpl_call('sma.prolog.PrologStrategies',enemy,[],Y).

% Vrai si l'agent ennemi est en position de force par rapport a l'agent allie
% Soit on donne les Coord(x,y,z) ici, soit ca sera recupere
% par un jpl_call dans isEnemyHigher
isEnemyInForce() :-
	allie(X),
	not(hp(X)),
	isEnemyHigher(X,_).

% Vrai si Y est plus haut que X
isEnemyHigher(X,Y) :-
	enemy(Y),
	X=coord(_,B,_),
	Y=coord(_,E,_),
	E > B.
