package datastructures.fonctions;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Constante implements Expression {

    private boolean valeur;

    public Constante(boolean valeur) {
        this.valeur = valeur;
    }

    public boolean getValeur() {
        return this.valeur;
    }

    public void setValeur(boolean valeur) {
        this.valeur = valeur;
    }

    public boolean evaluer(Map<Character, Boolean> valeurs) {
        return this.valeur;
    }

    public Set<Character> getVariables() {
        return Collections.emptySet();
    }

    public String toString() {
        return this.valeur ? "1" : "0";
    }

}
