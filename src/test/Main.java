package test;

import construction.DDBParser;

import reduction.DDBreduction;

import construction.InvalidExpressionException;
import datastructures.arbres.Feuille;
import datastructures.arbres.Noeud;
import datastructures.arbres.Racine;
import datastructures.arbres.Sommet;
import datastructures.fonctions.*;

import java.io.InvalidClassException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String source = args.length > 0 ? String.join(" ", args) : "x + y.t";
        DDBParser parser = new DDBParser(source);

        try {
            System.out.println("Source : " + parser.getSource());

            System.out.println("\nFonction :");
            System.out.println(parser.getFonction());

            System.out.println("\nDDB :");
            System.out.println(Main.afficher(parser.getDDB()));

            System.out.println("\nDDB reduit :");
            
            DDBreduction reduce=new DDBreduction(parser.getFonction());
     
            System.out.println(Main.afficher(reduce.getDDBreduction()));
        } catch (InvalidExpressionException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    private static String afficher(Noeud noeud) {
        if (noeud instanceof Sommet) {
            Sommet sommet = (Sommet) noeud;
            String[] gauche = Main.afficher(sommet.getGauche()).split("\n");
            String[] droite = Main.afficher(sommet.getDroite()).split("\n");

            String padGauche = String.join("", Collections.nCopies(gauche[0].length() - 1, " "));
            String padDroite = String.join("", Collections.nCopies(droite[0].length() - 1, " "));

            int lines = Math.max(gauche.length, droite.length);
            String[] tree = new String[lines];
            for (int i = 0; i < lines; i++) {
                tree[i] = i < gauche.length ? gauche[i] : String.join("", Collections.nCopies(gauche[0].length(), " "));
                tree[i] += " ";
                tree[i] += i < droite.length ? droite[i] : String.join("", Collections.nCopies(droite[0].length(), " "));
            }

            return padGauche + " " + sommet.getVariable() + " " + padDroite + "\n"
                    + padGauche + "/ \\" + padDroite + "\n"
                    + String.join("\n", tree);
        } else if (noeud instanceof Feuille) {
            Feuille feuille = (Feuille) noeud;
            return feuille.getValeur() ? "1" : "0";
        } else
            return noeud.toString();
    }
  
/*
    public static Noeud getSmartDDB(Fonction fonction, boolean simplify) throws UnsupportedOperationException {
        Noeud terminal0 = new Feuille(false), terminal1 = new Feuille(true);
        Noeud noeud = Main.convertNode(fonction.getValeur(), terminal0, terminal1, simplify);

        if (noeud instanceof Sommet) {
            Sommet sommet = (Sommet) noeud;
            return new Racine(sommet.getVariable(), sommet.getGauche(), sommet.getDroite());
        } else
            return noeud;
    }

    private static Noeud convertNode(Expression expression, Noeud terminal0, Noeud terminal1, boolean simplify) throws UnsupportedOperationException {
        if (expression == null) return null;
        if (expression instanceof Constante) {
            Constante constante = (Constante) expression;
            return constante.getValeur() ? terminal1 : terminal0;
        } else if (expression instanceof Reference) {
            Reference reference = (Reference) expression;
            if (simplify) {
                terminal0 = Main.simplify(terminal0, reference.getVariable(), false);
                terminal1 = Main.simplify(terminal1, reference.getVariable(), true);
   
                if (terminal0 instanceof Feuille && terminal1 instanceof Feuille)
                    if (((Feuille) terminal0).getValeur() == ((Feuille) terminal1).getValeur())
                        return terminal0;
            }
            return new Sommet(reference.getVariable(), terminal0, terminal1);
        } else if (expression instanceof OperateurNON) {
            OperateurNON operateur = (OperateurNON) expression;
            return Main.convertNode(operateur.getExpression(), terminal1, terminal0, simplify);
        } else if (expression instanceof OperateurOU) {
            OperateurOU operateur = (OperateurOU) expression;
            Noeud fallback = Main.convertNode(operateur.getDroite(), terminal0, terminal1, simplify);
            return Main.convertNode(operateur.getGauche(), fallback, terminal1, simplify);
        } else if (expression instanceof OperateurET) {
            OperateurET operateur = (OperateurET) expression;
            Noeud fallback = Main.convertNode(operateur.getDroite(), terminal0, terminal1, simplify);
            return Main.convertNode(operateur.getGauche(), terminal0, fallback, simplify);
        } else
            throw new UnsupportedOperationException("Unsupported Expression type: " + expression.getClass().getName());
    }
   
    private static Noeud simplify(Noeud noeud, char variable, boolean valeur) {
        if (noeud == null) return null;
        if (noeud instanceof Feuille)
            return noeud;
        else if (noeud instanceof Sommet) {
            Sommet sommet = (Sommet) noeud;
            if (sommet.getVariable() == variable)
                return Main.simplify(valeur ? sommet.getDroite() : sommet.getGauche(), variable, valeur);
            else {
                Noeud gauche = Main.simplify(sommet.getGauche(), variable, valeur);
                Noeud droite = Main.simplify(sommet.getDroite(), variable, valeur);
                if (gauche instanceof Feuille && droite instanceof Feuille)
                    if (((Feuille) gauche).getValeur() == ((Feuille) droite).getValeur())
                        return gauche;
                return new Sommet(sommet.getVariable(), gauche, droite);
            }
        } else
            throw new UnsupportedOperationException("Unsupported Node type: " + noeud.getClass().getName());
    }

}*/}