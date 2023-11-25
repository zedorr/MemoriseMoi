import java.util.ArrayList;
import java.util.List;

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

    Carte newCarte(String valeur) {
        Carte carte = new Carte();
        carte.valeur = valeur;
        carte.estRetournee = false;
        carte.estAppariee = false;
        return carte;
    }

    void retourner(Carte carte) {
        carte.estRetournee = !carte.estRetournee;
    }

    boolean estAppariee(Carte carte) {
        return carte.estAppariee;
    }

    void appairer(Carte carte) {
        carte.estAppariee = true;
    }

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

    JeuDeCartes newJeuDeCartes() {
        JeuDeCartes jeuDeCartes = new JeuDeCartes();
        jeuDeCartes.nbCartes = 0;
        jeuDeCartes.nbCartesRetournees = 0;
        jeuDeCartes.nbCartesAppariees = 0;
        jeuDeCartes.nbCartesRestantes = 0;
        jeuDeCartes.paquet = new Carte[0];
        return jeuDeCartes;
    }

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

    void algorithm() {
        boucle();
        auRevoir();
    }
}
