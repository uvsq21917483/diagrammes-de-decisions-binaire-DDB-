package datastructures.arbres;

public class Feuille extends Noeud {

    private boolean valeur;

    public Feuille(boolean valeur) {
        this.valeur = valeur;
    }

    public boolean getValeur() {
        return this.valeur;
    }

    public void setValeur(boolean valeur) {
        this.valeur = valeur;
    }

}
