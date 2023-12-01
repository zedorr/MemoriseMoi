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

    void afficherPaquet(Carte[] paquet) {
        for (int i = 0; i < length(paquet); i++) {
            afficherCarte(paquet[i]);
            println();
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

    void testNewJeuDeCartes() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        assertEquals(0, jeuDeCartes.nbCartes);
        assertEquals(0, jeuDeCartes.nbCartesRetournees);
        assertEquals(0, jeuDeCartes.nbCartesAppariees);
        assertEquals(0, jeuDeCartes.nbCartesRestantes);
        assertEquals(0, length(jeuDeCartes.paquet));
    }

    /**
     * @param paquet
     * @param carte
     * Retourne un tableau de cartes
     */
    Carte[] add(Carte[] paquet, Carte carte) {
        Carte[] res = new Carte[length(paquet) + 1];
        for (int i = 0; i < length(paquet); i++) {
            res[i] = paquet[i];
        }
        res[length(paquet)] = carte;
        return res;
    }

    void testAdd() {
        Carte[] paquet = new Carte[0];
        paquet = add(paquet, newCarte("Bonjour"));
        assertEquals(1, length(paquet));
        assertEquals("Bonjour", paquet[0].valeur);
        paquet = add(paquet, newCarte("Comment"));
        assertEquals(2, length(paquet));
        assertEquals("Comment", paquet[1].valeur);
    }

    /**
     * @param paquet
     * @param carte
     * Ajoute une carte au jeu de cartes
     */
    JeuDeCartes ajouterCarte(JeuDeCartes jeuDeCartes, Carte carte) {
        jeuDeCartes.nbCartes++;
        jeuDeCartes.nbCartesRestantes++;
        jeuDeCartes.paquet = add(jeuDeCartes.paquet, carte);
        return jeuDeCartes;
    }

    void testAjouterCarte() {
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        assertEquals(0, jeuDeCartes.nbCartes);
        assertEquals(0, jeuDeCartes.nbCartesRestantes);
        assertEquals(0, length(jeuDeCartes.paquet));
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Bonjour"));
        assertEquals(1, jeuDeCartes.nbCartes);
        assertEquals(1, jeuDeCartes.nbCartesRestantes);
        assertEquals(1, length(jeuDeCartes.paquet));
        assertEquals("Bonjour", jeuDeCartes.paquet[0].valeur);
        jeuDeCartes = ajouterCarte(jeuDeCartes, newCarte("Comment"));
        assertEquals(2, jeuDeCartes.nbCartes);
        assertEquals(2, jeuDeCartes.nbCartesRestantes);
        assertEquals(2, length(jeuDeCartes.paquet));
        assertEquals("Comment", jeuDeCartes.paquet[1].valeur);
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
                    // jouerFrancais();
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
    void _algorithm() {
        //boucle();
        //auRevoir();
        /*Carte carte1 = newCarte("Capitale de la France");
        Carte carte2 = newCarte("Paris");
        JeuDeCartes jeuDeCartes = newJeuDeCartes();
        ajouterCarte(jeuDeCartes, carte1);
        ajouterCarte(jeuDeCartes, carte2);
        afficherPaquet(jeuDeCartes.paquet);
        carte1.estRetournee = true;
        afficherPaquet(jeuDeCartes.paquet);*/
    }
}
