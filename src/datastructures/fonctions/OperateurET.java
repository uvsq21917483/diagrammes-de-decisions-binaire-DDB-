package datastructures.fonctions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OperateurET implements Operateur {

    private Expression gauche, droite;

    public OperateurET(Expression gauche, Expression droite) {
        this.gauche = gauche;
        this.droite = droite;
    }

    public Expression getGauche() {
        return this.gauche;
    }

    public void setGauche(Expression gauche) {
        this.gauche = gauche;
    }

    public Expression getDroite() {
        return this.droite;
    }

    public void setDroite(Expression droite) {
        this.droite = droite;
    }

    public boolean evaluer(Map<Character, Boolean> valeurs) {
        return this.gauche.evaluer(valeurs) && this.droite.evaluer(valeurs);
    }

    public Set<Character> getVariables() {
        Set<Character> variables = new HashSet<>();
        variables.addAll(this.gauche.getVariables());
        variables.addAll(this.droite.getVariables());
        return Collections.unmodifiableSet(variables);
    }

    public String toString() {
        return "(" + this.gauche.toString() + ") . (" + this.droite.toString() + ")";
    }

}
