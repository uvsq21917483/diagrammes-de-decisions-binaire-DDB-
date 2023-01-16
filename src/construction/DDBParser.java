package construction;

import datastructures.arbres.Feuille;
import datastructures.arbres.Noeud;
import datastructures.arbres.Racine;
import datastructures.arbres.Sommet;
import datastructures.fonctions.*;

import java.util.*;
import java.util.stream.Collectors;

public class DDBParser {

    private String source;

    public DDBParser(String source) {
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    private static Expression parse(String source) throws InvalidExpressionException {
        return DDBParser.parse(source, 0);
    }

    private static Expression parse(String source, int offset) throws InvalidExpressionException {
        List<Expression> expressions = new ArrayList<>();
        List<Operateur.Type> operateurs = new ArrayList<>();

        Expression expression;
        Operateur.Type operateur;

        int pos = 0;
        do {
            int parentheses = 0, ouverture = -1;

            expression = null;
            boolean negation = false;
            for (; pos < source.length(); pos++) {
                char c = source.charAt(pos);

                if (parentheses == 0) {
                    // En dehors de parenthèses
                    if (c == '(') {
                        ouverture = pos;
                        parentheses++;
                    } else if (c == ')')
                        throw new InvalidExpressionException("Parenthèse fermante surnuméraire à la position " + (offset + pos));
                    else if (c == '!')
                        negation = !negation;
                    else if (c == '0' || c == '1') {
                        expression = new Constante(c == '1');
                        pos++;
                        break;
                    } else if (Character.isLetter(c)) {
                        expression = new Reference(c);
                        pos++;
                        break;
                    } else if (Character.isWhitespace(c))
                        continue;
                    else
                        throw new InvalidExpressionException("Caractère inconnu à la position " + (offset + pos));
                } else {
                    // À l'intérieur de parenthèses
                    if (c == '(')
                        parentheses++;
                    else if (c == ')') {
                        parentheses--;

                        if (parentheses == 0) {
                            String parenthese = source.substring(ouverture + 1, pos);
                            expression = DDBParser.parse(parenthese, offset + ouverture + 1);
                            pos++;
                            break;
                        }
                    } else
                        continue;
                }
            }

            if (parentheses > 0)
                throw new InvalidExpressionException("Parenthèse non fermée à la position " + (offset + ouverture));
            else if (negation && expression == null)
                throw new InvalidExpressionException("Négation d'une expression vide à la position " + (offset + pos));
            else if (expression == null)
                throw new InvalidExpressionException("Expression illégalement vide à la position " + (offset + pos));
            expressions.add(negation ? new OperateurNON(expression) : expression);

            operateur = null;
            for (; pos < source.length(); pos++) {
                char c = source.charAt(pos);

                if (c == '.') {
                    pos++;
                    operateur = Operateur.Type.ET;
                    break;
                } else if (c == '+') {
                    pos++;
                    operateur = Operateur.Type.OU;
                    break;
                } else if (Character.isWhitespace(c))
                    continue;
                else
                    throw new InvalidExpressionException("Opération inconnue à la position " + (offset + pos));
            }
            if (operateur != null)
                operateurs.add(operateur);
        } while(operateur != null);

        // Priorités des opérations :

        // 0. NON
        // Déjà traité

        // 1. ET
        for (int i = 0; i < operateurs.size(); i++) {
            if (operateurs.get(i) == Operateur.Type.ET) {
                Expression gauche = expressions.remove(i);
                Expression droite = expressions.remove(i);
                expressions.add(i, new OperateurET(gauche, droite));

                operateurs.remove(i);
                i--;
            }
        }

        // 2. OU
        for (int i = 0; i < operateurs.size(); i++) {
            if (operateurs.get(i) == Operateur.Type.OU) {
                Expression gauche = expressions.remove(i);
                Expression droite = expressions.remove(i);
                expressions.add(i, new OperateurOU(gauche, droite));

                operateurs.remove(i);
                i--;
            }
        }

        return expressions.get(0);
    }

    public Fonction getFonction() throws InvalidExpressionException {
        return new Fonction(DDBParser.parse(this.source));
    }

    public Noeud getDDB() throws InvalidExpressionException {
        Fonction fonction = this.getFonction();
        Noeud noeud = DDBParser.getDDB(fonction, new HashMap<>(), fonction.getVariables());

        if (noeud instanceof Sommet) {
            Sommet sommet = (Sommet) noeud;
            return new Racine(sommet.getVariable(), sommet.getGauche(), sommet.getDroite());
        } else
            return noeud;
    }

    private static Noeud getDDB(Fonction fonction, Map<Character, Boolean> valeurs, Set<Character> variables) {
        if (variables.isEmpty()) {
            boolean resultat = DDBParser.evaluerFonction(fonction, valeurs);
            return new Feuille(resultat);
        }

        Character variable = variables.stream().sorted().findFirst().get();
        Set<Character> restantes = Collections.unmodifiableSet(variables.stream().filter(c -> c != variable).collect(Collectors.toSet()));

        valeurs.put(variable, false);
        Noeud gauche = DDBParser.getDDB(fonction, valeurs, restantes);

        valeurs.put(variable, true);
        Noeud droite = DDBParser.getDDB(fonction, valeurs, restantes);

        return new Sommet(variable, gauche, droite);
    }

    private static boolean evaluerSousExpression(Expression expression, Map<Character, Boolean> valeurs) {
        return expression.evaluer(valeurs);
    }

    private static boolean evaluerFonction(Fonction fonction, Map<Character, Boolean> valeurs) {
        return DDBParser.evaluerSousExpression(fonction.getValeur(), valeurs);
    }

}
