Pour éviter des duplications à l'infini des icônes,
une bibliothèque d'icônes "de base" est fournie au
développeur KB3.

ATTENTION AUX FORMATS DES ICONES QUI DOIVENT IMPERATIVEMENT ETRE EN 24*24 PIXELS 
SINON PLANTAGE DE KB3V3


Ces icônes portent des noms en anglais si elles sont bilingues ;
 si elles ne le sont pas, la version française porte le même nom
que la  version anglaise, avec un suffixe _f, de façon qu'elles se 
suivent par ordre alphabétique.

Exemple :

   and_gate
   and_gate_f


Le nom d'une icone doit être le plus descriptif possible, sans faire
référence à un contexte particulier : on dira "white_circle" plutôt que
"state" pour désigner une icône pouvant représenter un état dans un graphe
de Markov. IL NE DOIT PAS COMPORTER DE BLANC.


Tant qu'une icone n'existe qu'en une couleur, inutile de préciser cette 
couleur dans le nom. Idem pour la taille.

On aura des variantes par couleurs simples : 
white, black, yellow, blue, red, green, etc...

On aura au maximum trois tailles : small, med(ium), big.
La taille moyenne correspondra à une image inscrite dans un carré de 48x48 pixels
(2 fois la taille des .ico). Sera considéré comme "small" toute taille inférieure à cette
limite, et comme "big" toute taille supérieure. Ce qualificatif de taille ne s'applique
qu'à l'icone vectorielle : le même fichier .ico peut être associé à trois icônes 
vectorielles diffférentes.

Un nom donnera, dans cet ordre (et séparés par des "_") : 
	- la taille (obligatoire),
	- la couleur (facultatif),
	- la forme géométrique (obligatoire),
	- le suffixe éventuel _f pour la langue,

Exemples:  med_green_square, big_circle, and_gate


Cas des icônes de liens : pour les liens, il n'y a pas de fichier .sym. 

Le nom d'un fichier .ico de lien comprendra obligatoirement les éléments suivants, 
séparés par des "_" : 
	- type de ligne (solid, dash,dotted)
	- couleur (mêmes conventions que pour les icônes de noeuds)
	- type d'extrémités, sous la forme d'un abrégé, pris dans le tableau suivant :

		LINE_3_POINTS	l3p
		3_POINTS_LINE	3pl
		LINE_LINE	ll

	

	- épaisseur (entier : 1, 2,...)

Exemples: dotted_red_l3p_2, solid_blue_ll_1
