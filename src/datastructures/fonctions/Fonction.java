package datastructures.fonctions;

import java.util.Set;
import java.util.stream.Collectors;

public class Fonction {

    private Expression valeur;

    public Fonction(Expression valeur) {
        this.valeur = valeur;
    }

    public Expression getValeur() {
        return valeur;
    }

    public void setValeur(Expression valeur) {
        this.valeur = valeur;
    }

    public Set<Character> getVariables() {
        return this.valeur.getVariables();
    }

    public String toString() {
        String parameters = this.getVariables().stream().map(String::valueOf).sorted().collect(Collectors.joining(", "));
        return "f(" + parameters + ") = " + this.valeur.toString();
    }

}
