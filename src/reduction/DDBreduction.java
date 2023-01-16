package reduction;
//importation nécéssaire pour creer la classe DDBreduction
import datastructures.arbres.Feuille;
import datastructures.arbres.Noeud;
import datastructures.arbres.Racine;
import datastructures.arbres.Sommet;
import datastructures.fonctions.Constante;
import datastructures.fonctions.Expression;
import datastructures.fonctions.Fonction;
import datastructures.fonctions.OperateurET;
import datastructures.fonctions.OperateurNON;
import datastructures.fonctions.OperateurOU;
import datastructures.fonctions.Reference;

public class DDBreduction {

    //attribut qu'on a besoin pour construire le DDBreduction
	private Fonction fonction;
	private Noeud noeud;
	
    //Constructeur d'un DDB reduit
	public DDBreduction(Fonction fonction){   //Dans une fonction, on a une expression tous court
		this.fonction=fonction; //
		this.reductionDDB(true); //ça veut dire que  l'arbre est reductible = (true)
	}
	
    //recupère le noeud du DDB réduit
	public Noeud getDDBreduction(){				
		return noeud;
	}

	//cette methode reduit le DDB en général qui recherche les sous graphes isomorphes et supprime les Noeuds Inutiles
    public void reductionDDB(boolean suppressionDeNoeudsDeDecisionInutiles) throws UnsupportedOperationException {
        Noeud terminal0 = new Feuille(false), terminal1 = new Feuille(true); //création de 2 nouvelles feuilles 0 & 1
        Noeud noeud = DDBreduction.rechercheSousGraphesIsomorphes(fonction.getValeur(), terminal0, terminal1, suppressionDeNoeudsDeDecisionInutiles);

        if (noeud instanceof Sommet) { //verifie si noeud est sommet
            Sommet sommet = (Sommet) noeud; //encapsulation de noeud sous format sommet
            //creation racine avec la variable est ces fils gauche&droite 
            this.noeud=new Racine(sommet.getVariable(), sommet.getGauche(), sommet.getDroite());
        } else //
            this.noeud=noeud; //sinon 
    }

    //cette methode rechreche les sous graphes isomorphes
    private static Noeud  rechercheSousGraphesIsomorphes(Expression expression, Noeud terminal0, Noeud terminal1, boolean suppressionDeNoeudsDeDecisionInutiles) throws UnsupportedOperationException {
        if (expression == null) return null; //si l'expression est nulle 
        if (expression instanceof Constante) { //verifie si l'interface de l'expression est de type Constante 
            Constante constante = (Constante) expression;  //convertion de type expression avec constante (encapsulation)
            return constante.getValeur() ? terminal1 : terminal0; //renvoie le noeud Terminal1 si la valeur = 1 si  sinon 0
        } else if (expression instanceof Reference) { //verifie si l'expression est heritié de Reference type
            Reference reference = (Reference) expression; //convertion de l'expression en reference
            if (suppressionDeNoeudsDeDecisionInutiles) { 
                //si la suppression de noeud Inutiles existe alors 
                //on continue sur le traitement de ces fils gauche&droite avec gauche = 0 et droite = 1 
                terminal0 = DDBreduction.suppressionDeNoeudsDeDecisionInutiles(terminal0, reference.getVariable(), false);
                terminal1 = DDBreduction.suppressionDeNoeudsDeDecisionInutiles(terminal1, reference.getVariable(), true);
   
                if (terminal0 instanceof Feuille && terminal1 instanceof Feuille)
                    if (((Feuille) terminal0).getValeur() == ((Feuille) terminal1).getValeur())
                        return terminal0;
            }
            return new Sommet(reference.getVariable(), terminal0, terminal1);

        } else if (expression instanceof OperateurNON) { //verifie si l'interface de l'expression est un OperateurNON
            OperateurNON operateur = (OperateurNON) expression;  //convertion de l'expression en OperateurNON
            return DDBreduction.rechercheSousGraphesIsomorphes(operateur.getExpression(), terminal1, terminal0, suppressionDeNoeudsDeDecisionInutiles);
        } else if (expression instanceof OperateurOU) {  //verifie si l'interface de l'expression est un OperateurOU
            OperateurOU operateur = (OperateurOU) expression; //convertion de l'expression en OperateurOU
            //creer un nouveau noeud pour stocker et preparer le noeud operateurOu de droite avec ces 2 fils
            Noeud fallback = DDBreduction.rechercheSousGraphesIsomorphes(operateur.getDroite(), terminal0, terminal1, suppressionDeNoeudsDeDecisionInutiles);

            //renvoie le noeud préparé pour la suite 
            return DDBreduction. rechercheSousGraphesIsomorphes(operateur.getGauche(), fallback, terminal1, suppressionDeNoeudsDeDecisionInutiles);
        
        } else if (expression instanceof OperateurET) { //verifie si l'interface de l'expression est un OperateurET
            OperateurET operateur = (OperateurET) expression; //convertion de l'expression en OperateurET
            //creer un nouveau noeud pour stocker et preparer le noeud operateurET de droite avec ces 2 fils
            Noeud fallback = DDBreduction.rechercheSousGraphesIsomorphes(operateur.getDroite(), terminal0, terminal1, suppressionDeNoeudsDeDecisionInutiles);

            //renvoie le noeud préparé pour la suite
            return DDBreduction.rechercheSousGraphesIsomorphes(operateur.getGauche(), terminal0, fallback, suppressionDeNoeudsDeDecisionInutiles);
        } else
            throw new UnsupportedOperationException("Unsupported Expression type: " + expression.getClass().getName());
    }
   
    //cette methode supprime de Noeud de decision Inutiles qui prend en paramètre : noeud , variable , valeur
    //renvoie un Noeud de type static parce qu'à la fin du traitement on va avoir une feuille avec une valeur unique.

    private static Noeud suppressionDeNoeudsDeDecisionInutiles(Noeud noeud, char variable, boolean valeur) {
        if (noeud == null) return null;  //verifie si le noeud est nul
        if (noeud instanceof Feuille)  // verifie si noeud a une forme  feuille ou pas
            return noeud; //renvoie le noeud
        else if (noeud instanceof Sommet) {  //verifie si noeud a une forme  sommet 
            Sommet sommet = (Sommet) noeud;   //sinon on affecte le noeud dans un type sommet (encapsulation de type noeud on va dire)
            if (sommet.getVariable() == variable)
                //return appel recursif,  opération ternaire : si valeur = 1 alors filsdroite sinon val = 0 filsgauche
                return DDBreduction.suppressionDeNoeudsDeDecisionInutiles(valeur ? sommet.getDroite() : sommet.getGauche(), variable, valeur);
            else {
                //sinon la variable du sommet ne correspond pas au variable en cours
                // on continue sur le traitement de ces fils gauche&droite en appellant recursivement la méthode elle-même pour verifier le suivant
                Noeud gauche = DDBreduction.suppressionDeNoeudsDeDecisionInutiles(sommet.getGauche(), variable, valeur);
                Noeud droite = DDBreduction.suppressionDeNoeudsDeDecisionInutiles(sommet.getDroite(), variable, valeur);
                                                                                                                                              // __ __ __ __ __ __
                //et si on trouve à la fin du traitement de sommet, même type feuille et de                                                   |   a          a  |       
                if (gauche instanceof Feuille && droite instanceof Feuille)  //                                                                 |  / \  ==>   /   |           
                    if (((Feuille) gauche).getValeur() == ((Feuille) droite).getValeur())//verifie si la valeur de 2 feuilles filles est égale | 1   1      1    | 
                        return gauche;                     //renvoie juste le côté à gauche pour éviter la rédondance                      |__ __ __ __ __ __|                                                
                
                return new Sommet(sommet.getVariable(), gauche, droite);//sinon on creer un sommet avec les deux fils de valeurs differentes 1&0 ou 0&1
            }
        } else // cas hors type utilisé, sinon on renvoie un message d'erreur personnalisé
            throw new UnsupportedOperationException("Unsupported Node type: " + noeud.getClass().getName()); //anticipation d'erreur type.
    }

	
}