class MemoriseMoi extends Program {
    void init() {
        accueil();
        println("1. Jouer" + '\n' +
                "2. Règles" + '\n' +
                "3. Quitter");
    }

    String fileToString(int longueur, extensions.File fichier) {
        String res = "";
        for (int i = 0; i < longueur; i++) {
            res = res + fichier.readLine() + '\n';
        }
        return res;
    }

    void accueil() {
        clearScreen();
        println(fileToString(6, newFile("titre.txt")));
        text("green");
        println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■" + '\n' +
                "■           Choix           ■" + '\n' +
                "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        reset();
    }

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

    void auRevoir() {
        clearScreen();
        println(fileToString(6, newFile("aurevoir.txt")));
    }

    void algorithm() {
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
        auRevoir();
    }
}
