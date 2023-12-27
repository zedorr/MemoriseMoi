import extensions.CSVFile;

class MemoriseMoi extends Program {
    String pseudo;
    int score = 0;
    int pointsJoueur = 0;
    int pointsBot = 0;
    
    /**
     * Affiche le menu principal
     */
    void init() {
        accueil();
        println("1. Jouer" + '\n' +
                "2. Règles" + '\n' +
                "3. Scores" + '\n' +
                "4. Quitter");
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
    
    /**
     * Affiche le menu d'accueil
     */
    void accueil() {
        clearScreen();
        println(fileToString("ressources/titre.txt"));
        text("green");
        println(fileToString("ressources/choix.txt"));
        reset();
    }

    /**
     * Affiche les règles
     */
    void afficherRegles() {
        clearScreen();
        println(fileToString("ressources/regles.txt"));
        println("1. Retour");
        int choix = readStringNb(); 
        while (choix != 1) { 
            println("1. Retour");
            choix = readStringNb();
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
     * retourne le proprietaire de la carte
     */
    String getProprietaire(Carte carte) {
        return carte.proprietaire;
    }

    /**
     * @param carte
     * @param proprietaire
     * Change le proprietaire de la carte
     */
    void setProprietaire(Carte carte, String proprietaire) {
        carte.proprietaire = proprietaire;
    }

    /**
     * @param paquet
     * Affiche le paquet de cartes (4 cartes par colonnes)
     */
    void afficherPaquet(Carte[][] paquet) {
        clearScreen();
        String affichage = "";
        for (int i = 0; i < length(paquet); i++) {
            affichage = affichage + " _____________________ " + " _____________________ " + " _____________________ " + " _____________________ " + '\n';
            affichage = affichage + "|                     |" + "|                     |" + "|                     |" + "|                     |" + '\n';
            for (int j = 0; j < 4; j++) {
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
                    if (paquet[i][j].proprietaire == "joueur") {
                        affichage = affichage + "| " + ANSI_GREEN + espacesGauche + valeur + espacesDroite + ANSI_RESET + " |";
                    } else if (paquet[i][j].proprietaire == "bot") {
                        affichage = affichage + "| " + ANSI_RED + espacesGauche + valeur + espacesDroite + ANSI_RESET + " |";
                    } else {
                        affichage = affichage + "| " + espacesGauche + valeur + espacesDroite + " |";
                    }
                } else {
                    affichage = affichage + "|          ?          |";
                }
            }
            int indice = i+1; 
            affichage = affichage + "  " + indice + '\n';
            affichage = affichage + "|                     |" + "|                     |" + "|                     |" + "|                     |" + '\n';
            affichage = affichage + " ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ " + " ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ " + " ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ " + " ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ " + '\n';
            
        }
        affichage = affichage + "          a            " + "           b           " + "          c            " + "           d           " + '\n';
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

    /**
     * @param paquet
     * @param question 
     * @param reponse
     * @param question2
     * @param reponse2
     * @return Carte[][]
     * Ajoute 2 couple de cartes au paquet
     */
    Carte[][] add(Carte[][] paquet, Carte question, Carte reponse, Carte question2, Carte reponse2) {
        Carte[][] res = new Carte[length(paquet,1)+1][4];
        for (int i = 0; i < length(paquet); i++) {
            for (int j = 0; j < 4; j++) {
                res[i][j] = paquet[i][j];
            }
        }
        res[length(paquet)][0] = question;
        res[length(paquet)][1] = reponse;
        res[length(paquet)][2] = question2;
        res[length(paquet)][3] = reponse2;
        return res;
    }

    /**
     * @param jeuDeCartes
     * @param question
     * @param reponse
     * @param question2
     * @param reponse2
     * Ajoute 2 couple de cartes au jeu de cartes
     */
    JeuDeCartes ajouterCarte(JeuDeCartes jeuDeCartes, Carte question, Carte reponse, Carte question2, Carte reponse2) {
        jeuDeCartes.nbCartes+=4;
        jeuDeCartes.nbCartesRestantes+=4;
        jeuDeCartes.paquet = add(jeuDeCartes.paquet, question, reponse, question2, reponse2);
        return jeuDeCartes;
    }

    /**
     * @param nomFichier
     * @return tableau à 2 dimensions de cartes
     */
    Carte[][] loadCartes(String nomFichier) {
        CSVFile f = loadCSV(nomFichier);
        int rowCount = rowCount(f);    
        Carte[][] res = new Carte[rowCount][4];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount(f, i); j++) {
                if (j < 2) {
                    res[i][j] = newCarte(getCell(f, i, j), i);
                } else {
                    res[i][j] = newCarte(getCell(f, i, j), i+rowCount);
                }
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
        return (int)(random() * max);
    }

    /**
     * @param paquet
     * @return Carte[][]
     * Mélange le paquet de cartes
     */
    void melanger(Carte[][] paquet) {
        for (int i = 0; i < length(paquet,1); i++) {
            for (int j = 0; j < 4; j++) {
                int randomLigne = randomInt(length(paquet));
                int randomColonne = randomInt(4);
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
            j = ajouterCarte(j, paquet[i][0], paquet[i][1], paquet[i][2], paquet[i][3]);
        }
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
                "2. Contre l'ordinateur" + '\n' +
                "3. Retour");

        int choix = readStringNb();
        if (choix == 1) {
            while(jeuDeCartes.nbCartesRestantes > 0) {
                tourDeJeuSeul(jeuDeCartes);
            }
        } else if (choix == 2) {
            while(jeuDeCartes.nbCartesRestantes > 0) {
                tourDeJeuBot(jeuDeCartes);
            }
        } else if (choix == 3) {
            clearScreen();
            init();
            choix = 1;
        } else {
            println("Veuillez saisir un nombre entre 1 et 3");
            choix = readStringNb();
        }
    }

    /**
     * @return int
     * Controle que l'entrée est bien un entier
     */
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

    /**
     * @param p
     * @return boolean
     * Vérifie si le jeu est fini
     */
    boolean jeuFini(Carte[][] p) {
        for (int i=0; i<length(p, 1); i++) {
            for (int j=0; j<length(p, 2); j++) {
                if (!p[i][j].estRetournee) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param jeuDeCartes
     * tour de jeu avec l'ordinateur
     */
    void tourDeJeuBot(JeuDeCartes jeuDeCartes) {

        println("A vous de jouer " + pseudo + " :");
        int choixLigne1 = saisieValideLigne(jeuDeCartes.paquet);
        int choixColonne1 = saisieValideColonne(jeuDeCartes.paquet, choixLigne1);
        retourner(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
        afficherPaquet(jeuDeCartes.paquet);

        int choixLigne2 = saisieValideLigne(jeuDeCartes.paquet);
        int choixColonne2 = saisieValideColonne(jeuDeCartes.paquet, choixLigne2);
        retourner(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
        afficherPaquet(jeuDeCartes.paquet);

        if (jeuDeCartes.paquet[choixLigne1][choixColonne1].numId == jeuDeCartes.paquet[choixLigne2][choixColonne2].numId) {
            appairer(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
            appairer(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
            jeuDeCartes.paquet[choixLigne1][choixColonne1].proprietaire = "joueur";
            jeuDeCartes.paquet[choixLigne2][choixColonne2].proprietaire = "joueur";
            jeuDeCartes.nbCartesRestantes -= 2;
            pointsJoueur=pointsJoueur+1;
        } else {
            retourner(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
            retourner(jeuDeCartes.paquet[choixLigne2][choixColonne2]);
        }

        delay(1000);

        if (jeuDeCartes.nbCartesRestantes > 0) {
            println("L'ordinateur joue : ");
            delay(1000);

            int[] saisie1 = saisieAleatoireBot(jeuDeCartes.paquet);
            int lig1 = saisie1[0];
            int col1 = saisie1[1];
            retourner(jeuDeCartes.paquet[lig1][col1]);
            afficherPaquet(jeuDeCartes.paquet);

            delay(1000);
            
            int[] saisie2 = saisieAleatoireBot(jeuDeCartes.paquet);
            int lig2 = saisie2[0];
            int col2 = saisie2[1];
            retourner(jeuDeCartes.paquet[lig2][col2]);
            
            afficherPaquet(jeuDeCartes.paquet);
            
            delay(1000);
            
            if (jeuDeCartes.paquet[lig1][col1].numId == jeuDeCartes.paquet[lig2][col2].numId) {
                appairer(jeuDeCartes.paquet[lig1][col1]);
                appairer(jeuDeCartes.paquet[lig2][col2]);
                jeuDeCartes.paquet[lig1][col1].proprietaire = "bot";
                jeuDeCartes.paquet[lig2][col2].proprietaire = "bot";
                jeuDeCartes.nbCartesRestantes -= 2;
                pointsBot=pointsBot+1;
            } else {
                retourner(jeuDeCartes.paquet[lig1][col1]);
                retourner(jeuDeCartes.paquet[lig2][col2]);
            }
        }

        if (jeuDeCartes.nbCartesRestantes == 0) {
            println("Fin de la partie !");
            delay(1000);
            if (pointsBot > pointsJoueur) {
                println("L'ordinateur a gagné :/");
                delay(2000);
            }
            if (pointsJoueur > pointsBot) {
                println("Bravo " + pseudo + " ! Vous avez gagné !");
                delay(2000);
                score=score+1;
            }
            if (pointsBot == pointsJoueur) {
                println("Egalité !");
                delay(2000);
            }
            pointsBot = 0;
            pointsJoueur = 0;
        }
    }

    /**
     * @param jeuDeCartes
     * tour de jeu avec seulement l'utilisateur
     */
    void tourDeJeuSeul(JeuDeCartes jeuDeCartes) {
        println("A vous de jouer " + pseudo + " :");
        int choixLigne1 = saisieValideLigne(jeuDeCartes.paquet);
        int choixColonne1 = saisieValideColonne(jeuDeCartes.paquet, choixLigne1);
        retourner(jeuDeCartes.paquet[choixLigne1][choixColonne1]);
        afficherPaquet(jeuDeCartes.paquet);

        int choixLigne2 = saisieValideLigne(jeuDeCartes.paquet);
        int choixColonne2 = saisieValideColonne(jeuDeCartes.paquet, choixLigne2);
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

        if (jeuDeCartes.nbCartesRestantes == 0) {
            println("Bravo " + pseudo + " ! Vous avez gagné !");
            delay(3000);
            score=score+1;
        }
    }

    /**
     * @param paquet
     * @return int[]
     * Retourne un tableau avec 2 valeurs, l'indice de la ligne à l'indice 0 et l'indice de la colonne à l'indice 1    
     */
    int[] saisieAleatoireBot(Carte[][] paquet) {
        int[] res = new int[]{-1,-1};
        int randomLigne = randomInt(length(paquet,1));
        int randomColonne = randomInt(2);
        while (paquet[randomLigne][randomColonne].estRetournee) {
            randomLigne = randomInt(length(paquet,1));
            randomColonne = randomInt(4);
        }
        res[0] = randomLigne;
        res[1] = randomColonne;
        return res;
    }

    /**
     * @param paquet
     * @return int
     * Retourne une saisie de ligne valide
     */
    int saisieValideLigne(Carte[][] paquet) {
        println("Ligne de la carte à retourner : ");
        int lignes = length(paquet, 1);
        int res = readStringNb();
        if (res < 1 || res >= lignes+1) {
            println("Veuillez saisir un nombre entre 1 et " + lignes);
            res = readStringNb();
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
        String res;
        println("Colonne de la carte à retourner : ");
        do {
            res = readString().toLowerCase();
            if (length(res) != 1 || charAt(res,0) < 'a' || charAt(res,0) > 'a'+colonnes-1) {
                println("Veuillez saisir une lettre entre a et " + (char)(colonnes-1+'a'));
            } else if (paquet[ligneChoisie][charAt(res,0)-'a'].estRetournee) {
                println("Cette carte a déjà été retournée. Veuillez choisir une autre carte.");
                return saisieValideColonne(paquet, saisieValideLigne(paquet));
            }
        } while (length(res) != 1 || charAt(res,0) < 'a' || charAt(res,0) >= 'a' + colonnes || paquet[ligneChoisie][charAt(res,0)-'a'].estRetournee);
        return (charAt(res,0) - 'a');
    }

    /**
     * @param s
     * @return int
     * Convertit un String en entier
     */
    int string2Int(String s) {
        int res = 0;
        for (int i = 0; i < length(s); i++) {
            res = res * 10 + (charAt(s, i) - '0');
        }
        return res;
    }

    /**
     * @param i
     * @return String
     * Convertit un entier en String
     */
    String int2String(int i) {
        return "" + i;
    }

    /**
     * @param paquet
     * @param question 
     * @param reponse
     * @return Carte[][]
     * Ajoute 2 couple de cartes au paquet
     */
    String[][] add(String[][] tab, String pseudo, String score) {
        String[][] res = new String[length(tab,1)+1][2];
        for (int i = 0; i < length(tab); i++) {
            for (int j = 0; j < 2; j++) {
                res[i][j] = tab[i][j];
            }
        }
        res[length(tab)][0] = pseudo;
        res[length(tab)][1] = score;
        return res;
    }

    /**
     * Enregistre le score du joueur dans un fichier csv
     */
    void enregistrerScore() {
        CSVFile f = loadCSV("ressources/scores.csv");
        int rowCount = rowCount(f);
        boolean joueurTrouve = false;
        String[][] res = new String[rowCount][2];
        for (int i = 0; i < rowCount; i++) {
            res[i][0] = getCell(f, i, 0);
            res[i][1] = getCell(f, i, 1);
            if (equals(res[i][0],pseudo)) {
                int scoreExistant = string2Int(res[i][1]);
                score = score + scoreExistant;
                res[i][1] = int2String(score);
                joueurTrouve = true;
            }
        }
        if (!joueurTrouve) {
            res = add(res, pseudo, int2String(score));
        }
        saveCSV(res, "ressources/scores.csv");
    }

    /**
     * Affiche le score des joueurs
     */
    void afficherScore() {
        clearScreen();
        CSVFile f = loadCSV("ressources/scores.csv");
        int rowCount = rowCount(f);
        String[][] res = new String[rowCount][2];
        for (int i = 0; i < rowCount; i++) {
            res[i][0] = getCell(f, i, 0);
            res[i][1] = getCell(f, i, 1);
        }
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < 2; j++) {
                print(res[i][j] + "   ");
            }
            println();
        }
        println();
        println("1. Retour");
        int choix = readStringNb();
        while (choix != 1) { 
            println("1. Retour");
            choix = readStringNb();
        }
        clearScreen();
        init();
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
            choix = readStringNb(); 
            if (choix == 1) {
                clearScreen();
                accueil();
                println("1. Français" + '\n' +
                        "2. Maths" + '\n' +
                        "3. Retour");
                choix = readStringNb();
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
                afficherScore();
            } else if (choix == 4) {
                println("");
            }
        } while (choix != 4);
    }

    /**
     * Demande au joueur si il veut enregistrer son score et affiche un texte d'au revoir
     */
    void finDeJeu() {
        clearScreen();
        println("Voulez-vous enregistrer votre score ? (o/n)");
        String choix = readString();
        if (equals(choix,"o")) {
            enregistrerScore();
        }
        auRevoir();
    }

    /**
     * Algorithme principal
     */
    void algorithm() {
        boucle();
        finDeJeu();
    }

    // TESTS
    
    void testInt2String() {
        assertEquals("123", int2String(123));
        assertEquals("0", int2String(0));
        assertEquals("1", int2String(1));
        assertEquals("10", int2String(10));
    }

    void testString2Int() {
        assertEquals(123, string2Int("123"));
        assertEquals(0, string2Int("0"));
        assertEquals(1, string2Int("1"));
        assertEquals(10, string2Int("10"));
    }

    void testJeuFini() {
        Carte[][] paquet = loadCartes("questions/test.csv");
        assertFalse(jeuFini(paquet));
        for (int i=0; i<length(paquet, 1); i++) {
            for (int j=0; j<length(paquet, 2); j++) {
                retourner(paquet[i][j]);
            }
        }
        assertTrue(jeuFini(paquet));
    }

    void testFileToString() {
        assertEquals("Bonjour\n" +
                "Comment\n" +
                "ça va\n" +
                "Bien et toi\n" +
                "Très bien merci\n" +
                "Au revoir\n", fileToString("ressources/test.txt"));
    }

    void testNewCarte() {
        Carte carte = newCarte("Bonjour", 1);
        assertEquals("Bonjour", carte.valeur);
        assertFalse(carte.estRetournee);
        assertFalse(carte.estAppariee);
    }

    void testRetourner() {
        Carte carte = newCarte("Bonjour", 1);
        retourner(carte);
        assertTrue(carte.estRetournee);
        retourner(carte);
        assertFalse(carte.estRetournee);
    }

    void testNewJeuDeCartes() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        assertEquals(0, jeuDeCartes.nbCartes);
        assertEquals(0, jeuDeCartes.nbCartesRestantes);
        assertEquals(0, length(jeuDeCartes.paquet));
    }

    void testAdd() {
        Carte[][] paquet = new Carte[0][0];
        paquet = add(paquet, newCarte("Bonjour", 1), newCarte("Hello", 1), newCarte("Comment", 2), newCarte("How", 2));
        assertEquals(1, length(paquet));
        assertEquals("Bonjour", paquet[0][0].valeur);
        assertEquals("Hello", paquet[0][1].valeur);
        assertEquals("Comment", paquet[0][2].valeur);
        assertEquals("How", paquet[0][3].valeur);
    }

    void testGenererPaquet() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        Carte[][] paquet = loadCartes("questions/test.csv");
        genererPaquet(jeuDeCartes, paquet);
        assertEquals(8, jeuDeCartes.nbCartes);
        assertEquals(8, jeuDeCartes.nbCartesRestantes);
        assertEquals("Bonjour", jeuDeCartes.paquet[0][0].valeur);
        assertEquals("Hello", jeuDeCartes.paquet[0][1].valeur);
        assertEquals("Comment", jeuDeCartes.paquet[0][2].valeur);
        assertEquals("How", jeuDeCartes.paquet[0][3].valeur);
    }

    void testLoadCartes() {
        Carte[][] paquet = loadCartes("questions/test.csv");
        assertEquals("Bonjour", paquet[0][0].valeur);
        assertEquals("Hello", paquet[0][1].valeur);
        assertEquals(paquet[0][2].numId, paquet[0][3].numId);
        assertEquals("Comment", paquet[0][2].valeur);
    }

    void testAjouterCarte() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Bonjour", 1), newCarte("Hello", 1), newCarte("Comment", 2), newCarte("How", 2));
        assertEquals(4, jeuDeCartes.nbCartes);
        assertEquals(4, jeuDeCartes.nbCartesRestantes);
        assertEquals("Bonjour", jeuDeCartes.paquet[0][0].valeur);
        assertEquals("Hello", jeuDeCartes.paquet[0][1].valeur);
        assertEquals("Comment", jeuDeCartes.paquet[0][2].valeur);
        assertEquals("How", jeuDeCartes.paquet[0][3].valeur);
    }

    void testRandomInt() {
        int random = randomInt(10);
        assertTrue(random >= 0 && random < 10);
    }
}
