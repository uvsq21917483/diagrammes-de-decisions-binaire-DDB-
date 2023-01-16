package datastructures.fonctions;

import java.util.Map;
import java.util.Set;

public interface Expression {

    boolean evaluer(Map<Character, Boolean> valeurs);
    Set<Character> getVariables();

}
