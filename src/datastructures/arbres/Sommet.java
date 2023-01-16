package datastructures.arbres;

public class Sommet extends Noeud {

    private char variable;
    private Noeud gauche, droite;

    public Sommet(char variable, Noeud gauche, Noeud droite) {
        this.variable = variable;
        this.gauche = gauche;
        this.droite = droite;
    }

    public char getVariable() {
        return variable;
    }

    public void setVariable(char variable) {
        this.variable = variable;
    }

    public Noeud getGauche() {
        return this.gauche;
    }

    public void setGauche(Noeud gauche) {
        this.gauche = gauche;
    }

    public Noeud getDroite() {
        return this.droite;
    }

    public void setDroite(Noeud droite) {
        this.droite = droite;
    }

}
