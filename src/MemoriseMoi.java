class MemoriseMoi extends Program {
    void init() {
        clearScreen();
        println(fileToString(7, newFile("titre.txt")));
        accueil();
    }

    String fileToString(int longueur, extensions.File fichier) {
        String res = "";
        for (int i = 0; i < longueur; i++) {
            res = res + fichier.readLine() + '\n';
        }
        return res;
    }

    void accueil() {
        text("green");
        println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■" + '\n' +
                "■           Choix           ■" + '\n' +
                "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        reset();
        println("1. Jouer" + '\n' +
                "2. Règles" + '\n' +
                "3. Quitter");
    }

    /*void animationTitre() {
        String titre = fileToString(7, newFile("titre.txt"));
        String[] lignes = titre.split("\n");
        for (int i = 0; i < lignes.length; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            println(lignes[i]);
        }
        println();
    }*/

    void afficherRegles() {
        clearScreen();
        println(fileToString(7, newFile("regles.txt")));
        println("1. Retour");
        int choix = readInt();
        while (choix != 1) {
            println("1. Retour");
            choix = readInt();
        }
        clearScreen();
        accueil();
    }

    void algorithm() {
        init();
        int choix = readInt();
        while(choix != 3) {
            if (choix == 2) {
                afficherRegles();
                accueil();
            }
        }

    }
}
