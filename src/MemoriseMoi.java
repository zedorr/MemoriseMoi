class MemoriseMoi extends Program {
    /**
     * Affiche le menu principal
     */
    void init() {
        accueil();
        println("1. Jouer" + '\n' +
                "2. Règles" + '\n' +
                "3. Quitter");
    }

    /**
     * @param longueur Nombre de lignes à lire
     * @param fichier  Fichier à lire
     * Convertit un fichier en String
     */
    String fileToString(int longueur, extensions.File fichier) {
        String res = "";
        for (int i = 0; i < longueur; i++) {
            res = res + fichier.readLine() + '\n';
        }
        return res;
    }
    
    /**
     * Affiche le menu d'accueil
     */
    void accueil() {
        clearScreen();
        println(fileToString(6, newFile("titre.txt")));
        text("green");
        println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■" + '\n' +
                "■           Choix           ■" + '\n' +
                "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        reset();
    }

    /**
     * Affiche les règles
     */
    void afficherRegles() {
        clearScreen();
        println(fileToString(17, newFile("regles.txt")));
        println("1. Retour");
        int choix = readInt();
        while (choix != 1) {
            println("1. Retour");
            choix = readInt();
        }
        clearScreen();
        init();
    }


    /**
     * Affiche un texte d'au revoir
     */
    void auRevoir() {
        clearScreen();
        println(fileToString(6, newFile("aurevoir.txt")));
    }

    /**
     * @param valeur Valeur de la carte
     * @return Carte
     * Crée une carte
     */
    Carte newCarte(String valeur) {
        Carte carte = new Carte();
        carte.valeur = valeur;
        carte.estRetournee = false;
        carte.estAppariee = false;
        return carte;
    }

    /**
     * @param carte
     * retourne la carte
     */
    void retourner(Carte carte) {
        carte.estRetournee = !carte.estRetournee;
    }

    /**
     * @param carte
     * @return boolean
     * Vérifie si la carte est appariee
     */
    boolean estAppariee(Carte carte) {
        return carte.estAppariee;
    }

    /**
     * @param carte
     * Appaire la carte
     */
    void appairer(Carte carte) {
        carte.estAppariee = true;
    }

    /**
     * @param carte
     * Affiche la carte selon si elle est retournée ou non
     */
    void afficher(Carte carte) {
        if (carte.estRetournee) {
            println("+-----------------------+");
            println("|                       |");
            String valeur = carte.valeur;
            int maxLength = 22;
            for (int i = 0; i < length(valeur); i += maxLength) {
                String line = substring(valeur, i, Math.min(i + maxLength, length(valeur)));
                println("| " + line + "|");
        }
        println("|                       |");
        println("+-----------------------+");
        } else {
            println("+---------------------+");
            println("|                     |");
            println("|  Carte face cachée  |");
            println("|                     |");
            println("+---------------------+");
        }
    }

    /**
     * Crée un jeu de cartes
     */
    JeuDeCartes newJeuDeCartes() {
        JeuDeCartes jeuDeCartes = new JeuDeCartes();
        jeuDeCartes.nbCartes = 0;
        jeuDeCartes.nbCartesRetournees = 0;
        jeuDeCartes.nbCartesAppariees = 0;
        jeuDeCartes.nbCartesRestantes = 0;
        jeuDeCartes.paquet = new Carte[0];
        return jeuDeCartes;
    }

    /**
     * Boucle principale du jeu
     */
    void boucle() {
        int choix = 0;
        do {
            init();
            choix = readInt();
            if (choix == 1) {
                clearScreen();
                accueil();
                println("1. Français" + '\n' +
                        "2. Histoire" + '\n' +
                        "3. Retour");
                choix = readInt();
                if (choix == 1) {
                    // Français
                } else if (choix == 2) {
                    // Histoire
                } else if (choix == 3) {
                    clearScreen();
                    init();
                    choix = 1;
                }
            } else if (choix == 2) {
                afficherRegles();
            } else if (choix == 3) {
                println("");
            }
        } while (choix != 3);
    }

    /**
     * Algorithme principal
     */
    void algorithm() {
        boucle();
        auRevoir();
    }
}
