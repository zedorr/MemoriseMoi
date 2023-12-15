import extensions.CSVFile;

class MemoriseMoi extends Program {
    String pseudo;
    int score;
    
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
     * @param nomFichier
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
                "Au revoir\n", fileToString("ressources/test.txt"));
    }
    

    /**
     * Affiche le menu d'accueil
     */
    void accueil() {
        clearScreen();
        println(fileToString("ressources/titre.txt"));
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
        println(fileToString("ressources/regles.txt"));
        println("1. Retour");
        int choix = readStringNb(); // READ
        while (choix != 1) { 
            println("1. Retour");
            choix = readStringNb(); // READ
        }
        clearScreen();
        init();
    }


    /**
     * Affiche un texte d'au revoir
     */
    void auRevoir() {
        clearScreen();
        println(fileToString("ressources/aurevoir.txt"));
    }

    /**
     * @param valeur Valeur de la carte
     * @param numId Numéro d'identification de la carte
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

    void testRetourner() {
        Carte carte = newCarte("Bonjour", 1);
        retourner(carte);
        assertTrue(carte.estRetournee);
        retourner(carte);
        assertFalse(carte.estRetournee);
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
     * Affiche le paquet de cartes (2 colonnes)
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
     * @param question 
     * @param reponse
     * @return Carte[][]
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
     * @param jeuDeCartes
     * @param question
     * @param reponse
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
        assertEquals(1, jeuDeCartes.paquet[0][0].numId);
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
        assertEquals("Hello", paquet[0][1].valeur);
        assertEquals(0, paquet[0][0].numId);
        assertEquals(0, paquet[0][1].numId);
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

    /**
     * @param j
     * @param paquet
     * Génère un paquet de cartes
     */
    void genererPaquet(JeuDeCartes j, Carte[][] paquet) {
        for (int i = 0; i < length(paquet, 1); i++) {
            j = ajouterCarte(j, paquet[i][0], paquet[i][1]);
        }
    }

    void testGenererPaquet() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        Carte[][] paquet = loadCartes("questions/test.csv");
        genererPaquet(jeuDeCartes, paquet);
        assertEquals(4, jeuDeCartes.nbCartes);
        assertEquals(4, jeuDeCartes.nbCartesRestantes);
        assertEquals("Bonjour", jeuDeCartes.paquet[0][0].valeur);
        assertEquals("Hello", jeuDeCartes.paquet[0][1].valeur);
        assertEquals("Comment", jeuDeCartes.paquet[1][0].valeur);
        assertEquals("How", jeuDeCartes.paquet[1][1].valeur);
    }

    /**
     * @param matiere
     * Joue au jeu
     */
    void jouer(String matiere) {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        Carte[][] p = loadCartes("questions/"+matiere+".csv");
        genererPaquet(jeuDeCartes, p);
        melanger(jeuDeCartes.paquet);
        afficherPaquet(jeuDeCartes.paquet);
        println("1. Seul" + '\n' +
                "2. Contre l'ordinateur");
        int choix = readStringNb(); // READ
        if (choix == 1) {
            while(jeuDeCartes.nbCartesRestantes > 0) {
                tourDeJeuSeul(jeuDeCartes, score);
            }
        } else if (choix == 2) {
            while(jeuDeCartes.nbCartesRestantes > 0) {
                tourDeJeuBot(jeuDeCartes);
            }
        } else {
            println("Veuillez saisir un nombre entre 1 et 2");
            choix = readStringNb(); // READ
        }
    }
    
    void tourDeJeuBot(JeuDeCartes jeuDeCartes) {
        int pointsBot = 0;
        int pointsJoueur = 0;
        tourDeJeuSeul(jeuDeCartes, pointsJoueur);
        delay(1000);
        println("L'ordinateur joue : ");
        delay(500);
        int randomLigne = randomInt(length(jeuDeCartes.paquet));
        int randomColonne = randomInt(2);
        retourner(jeuDeCartes.paquet[randomLigne][randomColonne]);
        afficherPaquet(jeuDeCartes.paquet);
        delay(500);
        int randomLigne2 = randomInt(length(jeuDeCartes.paquet));
        int randomColonne2 = randomInt(2);
        retourner(jeuDeCartes.paquet[randomLigne2][randomColonne2]);
        afficherPaquet(jeuDeCartes.paquet);
        delay(500);
        if (jeuDeCartes.paquet[randomLigne][randomColonne].numId == jeuDeCartes.paquet[randomLigne2][randomColonne2].numId) {
            appairer(jeuDeCartes.paquet[randomLigne][randomColonne]);
            appairer(jeuDeCartes.paquet[randomLigne2][randomColonne2]);
            jeuDeCartes.nbCartesRestantes -= 2;
        } else {
            retourner(jeuDeCartes.paquet[randomLigne][randomColonne]);
            retourner(jeuDeCartes.paquet[randomLigne2][randomColonne2]);
        }
        if (jeuDeCartes.nbCartesRestantes == 0) {
            if (pointsBot > pointsJoueur) {
                println("L'ordinateur a gagné :/");
                delay(5000);
            }
            if (pointsBot < pointsJoueur) {
                println("Bravo " + pseudo + " ! Vous avez gagné !");
                delay(5000);
            }
        }
    }

    int readStringNb() {
        boolean bon = false;
        String entree = readString();
        while (!bon) {
            bon = true;
            int number = 0;
            for (int i = 0; i < length(entree); i++) {
                char c = charAt(entree, i);
                if (c < '0' || c > '9') {
                    bon = false;
                    break;
                }
                number = number * 10 + (c - '0');
            }
            if (!bon) {
                println("Veuillez saisir un nombre");
                entree = readString();
            } else {
                return number;
            }
        }
        return -1;
    }

    void tourDeJeuSeul(JeuDeCartes jeuDeCartes, int pointsJoueur) {
        println("A vous de jouer " + pseudo + " :");
        //println("Ligne de la carte à retourner : ");
        int choixLigne1 = saisieValideLigne(jeuDeCartes.paquet);
        //println("Colonne de la carte à retourner : ");
        int choixColonne1 = saisieValideColonne(jeuDeCartes.paquet, choixLigne1);
        retourner(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
        afficherPaquet(jeuDeCartes.paquet);
        //println("Ligne de la carte à retourner : ");
        int choixLigne2 = saisieValideLigne(jeuDeCartes.paquet);
        //println("Colonne de la carte à retourner : ");
        int choixColonne2 = saisieValideColonne(jeuDeCartes.paquet, choixLigne2);
        retourner(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
        afficherPaquet(jeuDeCartes.paquet);
        if (jeuDeCartes.paquet[choixLigne1][choixColonne1].numId == jeuDeCartes.paquet[choixLigne2][choixColonne2].numId) {
            appairer(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
            appairer(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
            jeuDeCartes.nbCartesRestantes -= 2;
            pointsJoueur++;
        } else {
            retourner(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
            retourner(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
        }
        if (jeuDeCartes.nbCartesRestantes == 0) {
            println("Bravo " + pseudo + " ! Vous avez gagné !");
            delay(5000);
        }
    }

    boolean estValide(int ligne, int colonne, Carte[][] paquet) {
        return ligne >= 0 && ligne < length(paquet) && colonne >= 0 && colonne < 2;
    }

    /**
     * @param paquet
     * @return int
     * Retourne une saisie de ligne valide
     */
    int saisieValideLigne(Carte[][] paquet) {
        println("Ligne de la carte à retourner : ");
        int lignes = length(paquet, 1);
        int res = readStringNb(); // READ
        if (res < 1 || res >= lignes+1) {
            println("Veuillez saisir un nombre entre 1 et " + lignes);
            res = readStringNb(); // READ
        }
        return res-1;
    }

    /**
     * @param paquet
     * @return int
     * Retourne une saisie de colonne valide
     */
    int saisieValideColonne(Carte[][] paquet, int ligneChoisie) {
        int colonnes = length(paquet, 2);
        int res;
        println("Colonne de la carte à retourner : ");
        do {
            res = readStringNb();
            if (res < 1 || res >= colonnes+1) {
                println("Veuillez saisir un nombre entre 1 et " + colonnes);
            } else if (paquet[ligneChoisie][res-1].estRetournee) {
                println("Cette carte a déjà été retournée. Veuillez choisir une autre carte.");
                return saisieValideColonne(paquet, saisieValideLigne(paquet));
            }
        } while (res < 1 || res >= colonnes+1 || paquet[ligneChoisie][res-1].estRetournee);
        return res-1;
    }

    /**
     * Boucle principale du jeu
     */
    void boucle() {
        clearScreen();
        accueil();
        println("Veuillez saisir votre pseudo : ");
        pseudo = readString();
        int choix = 0;
        do {
            init();
            choix = readStringNb(); // READ
            if (choix == 1) {
                clearScreen();
                accueil();
                println("1. Français" + '\n' +
                        "2. Maths" + '\n' +
                        "3. Retour");
                choix = readStringNb(); // READ
                if (choix == 1) {
                    jouer("français");
                } else if (choix == 2) {
                    jouer("maths");
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
