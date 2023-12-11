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
    Carte newCarte(String valeur, int numId) {
        Carte carte = new Carte();
        carte.valeur = valeur;
        carte.estRetournee = false;
        carte.estAppariee = false;
        carte.numId = numId;
        return carte;
    }

    void testNewCarte() {
        Carte carte = newCarte("Bonjour", 1);
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
     * @param paquet
     * Affiche le paquet de cartes
     */
    void afficherPaquet(Carte[][] paquet) {
        clearScreen();
        String affichage = "";
        for (int i = 0; i < length(paquet); i++) {
            affichage = affichage + " _____________________ " + " _____________________ " + '\n';
            affichage = affichage + "|                     |" + "|                     |" + '\n';
            for (int j = 0; j < 2; j++) {
                if (paquet[i][j].estRetournee) {
                    String valeur = paquet[i][j].valeur;
                    String espacesGauche = "";
                    String espacesDroite = "";
                    int espaces = (19 - length(valeur)) / 2; 
                    for (int k=0 ; k < espaces ; k++) {
                        espacesGauche = espacesGauche + " ";
                    }
                    for (int l=0; l < 19 - length(valeur) - espaces ; l++) {
                        espacesDroite = espacesDroite + " ";
                    }
                    affichage = affichage + "| " + espacesGauche + valeur + espacesDroite + " |";
                } else {
                    affichage = affichage + "|          ?          |";
                }
            }
            affichage = affichage + '\n';
            affichage = affichage + "|                     |" + "|                     |" + '\n';
            affichage = affichage + " ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ " + " ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ " + '\n';
        }
        println(affichage);
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

    void testAdd() {
        Carte[][] paquet = new Carte[0][0];
        paquet = add(paquet, newCarte("Bonjour", 1), newCarte("Hello", 1));
        assertEquals(1, length(paquet));
        assertEquals("Bonjour", paquet[0][0].valeur);
        assertEquals("Hello", paquet[0][1].valeur);
        paquet = add(paquet, newCarte("Comment", 2), newCarte("How", 2));
        assertEquals(2, length(paquet));
        assertEquals("Comment", paquet[1][0].valeur);
        assertEquals("How", paquet[1][1].valeur);
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
    
    void testAjouterCarte() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Bonjour", 1), newCarte("Hello", 1));
        assertEquals(2, jeuDeCartes.nbCartes);
        assertEquals(2, jeuDeCartes.nbCartesRestantes);
        assertEquals("Bonjour", jeuDeCartes.paquet[0][0].valeur);
        assertEquals("Hello", jeuDeCartes.paquet[0][1].valeur);
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Comment", 2), newCarte("How", 2));
        assertEquals(4, jeuDeCartes.nbCartes);
        assertEquals(4, jeuDeCartes.nbCartesRestantes);
        assertEquals("Comment", jeuDeCartes.paquet[1][0].valeur);
        assertEquals("How", jeuDeCartes.paquet[1][1].valeur);
    }

    /**
     * @param nomFichier
     * @return tableau à 2 dimensions de cartes
     */
    Carte[][] loadCartes(String nomFichier) {
        CSVFile f = loadCSV(nomFichier);
        Carte[][] res = new Carte[rowCount(f)][2];
        for (int i = 0; i < rowCount(f); i++) {
            for (int j = 0; j < 2; j++) {
                res[i][j] = newCarte(getCell(f, i, j), i);
            }
        }
        return res;
    }

    void testLoadCartes() {
        Carte[][] paquet = loadCartes("questions/test.csv");
        assertEquals("Bonjour", paquet[0][0].valeur);
        assertEquals("1", paquet[0][1].valeur);
        assertEquals("are you", paquet[5][0].valeur);
        assertEquals("3", paquet[5][1].valeur);
    }

    /**
     * @param max
     * @return int
     * Génère un nombre aléatoire entre 0 et max
     */
    int randomInt(int max) {
        return (int) (random() * max);
    }

    void testRandomInt() {
        int random = randomInt(10);
        assertTrue(random >= 0 && random < 10);
    }

    /**
     * @param paquet
     * @return Carte[][]
     * Mélange le paquet de cartes
     */
    void melanger(Carte[][] paquet) {
        for (int i = 0; i < length(paquet); i++) {
            for (int j = 0; j < 2; j++) {
                int randomLigne = randomInt(length(paquet));
                int randomColonne = randomInt(2);
                Carte temp = paquet[i][j];
                paquet[i][j] = paquet[randomLigne][randomColonne];
                paquet[randomLigne][randomColonne] = temp;
            }
        }
    }

    void genererPaquet(JeuDeCartes j, Carte[][] paquet) {
        for (int i=0; i<length(paquet, 1)-1; i++) {
            int id = (int) charAt(paquet[i][1].valeur, 0);
            j = ajouterCarte(j, newCarte(paquet[i][0].valeur, id), newCarte(paquet[i+1][0].valeur, id));
        }
    }

    // FAIRE LES TESTS

    void jouerFrancais() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        Carte[][] p = loadCartes("questions/français.csv");
        genererPaquet(jeuDeCartes, p);
        // TABLEAU AVEC UN TEXTE ET UN ID A COTE
        /*jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Bonjour", 1), newCarte("Hello", 1));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Comment", 2), newCarte("How", 2));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("ça va", 3), newCarte("are you", 3));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Bien et toi", 4), newCarte("Fine and you", 4));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Très bien merci", 5), newCarte("Very well thank you", 5));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Au revoir", 6), newCarte("Goodbye", 6));
        */melanger(jeuDeCartes.paquet);
        afficherPaquet(jeuDeCartes.paquet);
        while (jeuDeCartes.nbCartesRestantes > 0) {
            println("Ligne de la carte à retourner : ");
            int choixLigne1 = saisieValideLigne(jeuDeCartes.paquet);
            println("Colonne de la carte à retourner : ");
            int choixColonne1 = saisieValideColonne(jeuDeCartes.paquet);
            retourner(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
            afficherPaquet(jeuDeCartes.paquet);
            println("Ligne de la carte à retourner : ");
            int choixLigne2 = saisieValideLigne(jeuDeCartes.paquet);
            println("Colonne de la carte à retourner : ");
            int choixColonne2 = saisieValideColonne(jeuDeCartes.paquet);
            retourner(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
            afficherPaquet(jeuDeCartes.paquet);
            if (jeuDeCartes.paquet[choixLigne1][choixColonne1].numId == jeuDeCartes.paquet[choixLigne2][choixColonne2].numId) {
                appairer(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
                appairer(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
                jeuDeCartes.nbCartesRestantes -= 2;
            } else {
                retourner(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
                retourner(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
            }
        }
    }

    int saisieValideLigne(Carte[][] paquet) {
        int lignes = length(paquet, 1);
        int res = readInt();
        if (res < 1 || res >= lignes+1) {
            println("Veuillez saisir un nombre entre 1 et " + lignes);
            res = readInt();
        }
        return res-1;
    }

    int saisieValideColonne(Carte[][] paquet) {
        int colonnes = length(paquet, 2);
        int res = readInt();
        if (res < 1 || res >= colonnes+1) {
            println("Veuillez saisir un nombre entre 1 et " + colonnes);
            res = readInt();
        }
        return res-1;
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
