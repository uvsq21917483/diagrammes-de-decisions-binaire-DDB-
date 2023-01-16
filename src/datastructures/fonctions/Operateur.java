package datastructures.fonctions;

public interface Operateur extends Expression {

    enum Type {

        NON(OperateurNON.class),
        ET(OperateurET.class),
        OU(OperateurOU.class);

        private final Class<? extends Operateur> classe;

        Type(Class<? extends Operateur> classe) {
            this.classe = classe;
        }

        public Class<? extends Operateur> getClasse() {
            return classe;
        }

    }

}
