package datastructures.fonctions;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Reference implements Expression {

    private char variable;

    public Reference(char variable) {
        this.variable = variable;
    }

    public char getVariable() {
        return this.variable;
    }

    public void setVariable(char variable) {
        this.variable = variable;
    }

    public boolean evaluer(Map<Character, Boolean> valeurs) {
        return valeurs.getOrDefault(this.variable, false);
    }

    public Set<Character> getVariables() {
        return Collections.singleton(this.variable);
    }

    public String toString() {
        return String.valueOf(this.variable);
    }
    
}
