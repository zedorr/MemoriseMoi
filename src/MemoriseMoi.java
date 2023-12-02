import extensions.CSVFile;

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
    String fileToString(String nomFichier) {
        extensions.File f = newFile(nomFichier);
        String res = "";
        if (ready(f)) {
            String line = "f";
            while (line != null) {
                line = readLine(f);
                res = res + line + '\n';
            }
        }
        return substring(res, 0, length(res)-5);
    }

    void testFileToString() {
        assertEquals("Bonjour\n" +
                "Comment\n" +
                "ça va\n" +
                "Bien et toi\n" +
                "Très bien merci\n" +
                "Au revoir\n", fileToString("test.txt"));
    }
    
    /**
     * Affiche le menu d'accueil
     */
    void accueil() {
        clearScreen();
        println(fileToString("titre.txt"));
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
        println(fileToString("regles.txt"));
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
        println(fileToString("aurevoir.txt"));
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

    void testNewCarte() {
        Carte carte = newCarte("Bonjour");
        assertEquals("Bonjour", carte.valeur);
        assertFalse(carte.estRetournee);
        assertFalse(carte.estAppariee);
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
    void afficherCarte(Carte carte) {
        if (carte.estRetournee) {
            println(carte.valeur);
        } else {
            println("■■■■■"+'\n'+
                    "■■■■■"+'\n'+
                    "■■■■■"+'\n'+
                    "■■■■■");
        }
    }

    /**
     * @param paquet
     * Affiche le paquet de cartes
     */
    void afficherPaquet(Carte[][] paquet) {
        for (int i = 0; i < length(paquet); i++) {
            for (int j = 0; j < length(paquet[i]); j++) {
                afficherCarte(paquet[i][j]);
                println();
            }
        }
    }
    
    /**
     * Crée un jeu de cartes
     */
    JeuDeCartes newJeuDeCartes() {
        JeuDeCartes jeuDeCartes = new JeuDeCartes();
        jeuDeCartes.nbCartes = 0;
        jeuDeCartes.nbCartesRestantes = 0;
        jeuDeCartes.paquet = new Carte[0][0];
        return jeuDeCartes;
    }

    void testNewJeuDeCartes() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        assertEquals(0, jeuDeCartes.nbCartes);
        assertEquals(0, jeuDeCartes.nbCartesRestantes);
        assertEquals(0, length(jeuDeCartes.paquet));
    }

    /**
     * @param paquet
     * @param carte
     * @return Carte[]
     * Ajoute un couple de cartes au paquet
     */
    Carte[][] add(Carte[][] paquet, Carte question, Carte reponse) {
        Carte[][] res = new Carte[length(paquet)+1][2];
        for (int i = 0; i < length(paquet); i++) {
            res[i][0] = paquet[i][0];
            res[i][1] = paquet[i][1];
        }
        res[length(paquet)][0] = question;
        res[length(paquet)][1] = reponse;
        return res;
    }

    /**
     * @param paquet
     * @param carte
     * Ajoute un couple de cartes au jeu de cartes
     */
    JeuDeCartes ajouterCarte(JeuDeCartes jeuDeCartes, Carte question, Carte reponse) {
        jeuDeCartes.nbCartes+=2;
        jeuDeCartes.nbCartesRestantes+=2;
        jeuDeCartes.paquet = add(jeuDeCartes.paquet, question, reponse);
        return jeuDeCartes;
    }
    
    Carte[][] loadCartes(String nomFichier) {
        CSVFile f = loadCSV(nomFichier);
        Carte[][] res = new Carte[rowCount(f)][2];
        for (int i = 0; i < rowCount(f); i++) {
            for (int j = 0; j < 2; j++) {
                res[i][j] = newCarte(getCell(f, i, j));
            }
        }
        return res;
    }

    /**
     * @param max
     * @return int
     * Génère un nombre aléatoire entre 0 et max
     */
    int randomInt(int max) {
        return (int) (random() * max);
    }

    /**
     * @param paquet
     * @return Carte[][]
     * Mélange le paquet de cartes
     */
    Carte[][] melanger(Carte[][] paquet) {
        for (int i = 0; i < length(paquet); i++) {
            int random = randomInt(length(paquet));
            Carte[] temp = paquet[i];
            paquet[i] = paquet[random];
            paquet[random] = temp;
        }
        return paquet;
    }

    void jouerFrancais() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Bonjour"), newCarte("Hello"));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Comment"), newCarte("How"));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("ça va"), newCarte("are you"));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Bien et toi"), newCarte("Fine and you"));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Très bien merci"), newCarte("Very well thank you"));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Au revoir"), newCarte("Goodbye"));
        melanger(jeuDeCartes.paquet);
        afficherPaquet(jeuDeCartes.paquet);
        while (jeuDeCartes.nbCartesRestantes > 0) {
            println("Ligne de la carte à retourner : ");
            int choixLigne1 = readInt();
            println("Colonne de la carte à retourner : ");
            int choixColonne1 = readInt();
            retourner(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
                afficherPaquet(jeuDeCartes.paquet);
                println("Ligne de la carte à retourner : ");
                int choixLigne2 = readInt();
                println("Colonne de la carte à retourner : ");
                int choixColonne2 = readInt();
                retourner(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
                afficherPaquet(jeuDeCartes.paquet);
                if (jeuDeCartes.paquet[choixLigne1][choixColonne1].valeur == jeuDeCartes.paquet[choixLigne2][choixColonne2].valeur) {
                    appairer(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
                    appairer(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
                    jeuDeCartes.nbCartesRestantes -= 2;
                } else {
                    retourner(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
                    retourner(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
                }
            }
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
                    jouerFrancais();
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
