package datastructures.fonctions;

import java.util.Map;
import java.util.Set;

public class OperateurNON implements Operateur {

    private Expression expression;

    public OperateurNON(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public boolean evaluer(Map<Character, Boolean> valeurs) {
        return !this.expression.evaluer(valeurs);
    }

    public Set<Character> getVariables() {
        return this.expression.getVariables();
    }

    public String toString() {
        return "!(" + this.expression.toString() + ")";
    }

}
