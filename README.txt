
Réalisation d'un diagramme de décisions binaire programmé en java.
le programme prend en entrée une formule booléenne et Retoune son diagramme de décision binaire.
un diagramme de décision binaire est  un graphe orienté et DAG ,Les feuilles de ce graphe contiennent une valeur booléenne
qui sera un noeud de décision, les noeuds qui ne sont pas des feuilles sont les variables de la formules, si une variable n'apparait pas 
dans le DDB, alors cette variable n'a pas d'impacte sur la "décision de la formule", les arcs représentent la décision de chaque variable du noeud
père, soit 0 ou 1.

le programme se décompose en deux parties principales:

1-Construction de l'arbre binaire complet :

il s'agit de construire un arbre complet de taille 2^(nombre de variable), chaque feuilles représente la valeur de la formule booléenne associée au chemin 
en partant du sommet, ce chemin est unique, le choix de descendre à gauche du noeud est équivalent à lui attribuer une valeur 0, ainsi que descendre 
à droite du noeud lui impose la valeur 1.

2- Réductoin de l'arbre complet:
il s'agit d'une réduction de cet arbre complet en un arbre réduit en suivant la dépendance du noeud avec son noeud "père", et ceci en parcourant
l'arbre et fusionner le sous graphes isomorphes de cet arbre en supprimant les sous graphes similaires, la réduction nous fournit un graphe orienté et DAG.

3- Main/test :
dans ce programme on offre la possibilité de tester la BDD en introduisant une formule booléenne et le programme vous déssinera l'arbre complet associé à 
la formule booléenne ainsi que sa rédustion. 

