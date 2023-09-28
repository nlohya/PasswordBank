package fr.nlohya.passwordbank;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class PasswordBank {

    public static final String NORMAL_CHARS = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ";
    public static final String SPECIAL_CHARS = "&@#:/;.,?=+-_)!(§";
    public static final String NUMBER_CHARS = "123456789";

    public static final List<String> CHARS = List.of(NORMAL_CHARS, SPECIAL_CHARS, NUMBER_CHARS);


    public static void main(String[] args) throws IOException, ParseException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenue dans PasswordBank: ");

        boolean appRunning = true;

        while (appRunning) {
            System.out.println("Que voulez-vous faire ?");
            System.out.println("1 - Générer un mot de passe");
            System.out.println("2 - Récupérer un mot de passe");
            System.out.println("3 - Quitter l'application");

            String choice = scanner.nextLine();
            int choiceValue;

            try {
                choiceValue = Integer.parseInt(choice);
            } catch (Exception e) {
                System.out.println("Choix éronné... Veuillez rééssayer.");
                continue;
            }

            switch (choiceValue) {
                case 1 -> generatePassword(scanner);
                case 2 -> retrievePassword(scanner);
                case 3 -> {
                    System.out.println("À bientôt...");
                    appRunning = false;
                }
                default -> System.out.println("Choix éronné... Veuillez rééssayer.");
            }
        }
    }

    private static void retrievePassword(Scanner scanner) {
        System.out.println("Choisissez le nom du mot de passe à récupérer :");
        String passName = scanner.nextLine();

        String password = FileManager.readPassword(passName);

        if (password != null) {
            System.out.println("Votre mot de passe :");
            System.out.println(password);
        } else {
            System.out.println("Le mot de passe spécifié n'a pas été trouvé");
        }
    }

    public static int randomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max+1) - min) + min;
    }

    public static void generatePassword(Scanner scanner) throws IOException, ParseException {
        System.out.println("Choisissez le nom associé à votre mot de passe :");
        String passName = scanner.nextLine();

        System.out.println("Choisissez une taille pour votre mot de passe :");
        int passLength = scanner.nextInt();

        // Fix garbage scanner (stuck after nextInt)
        scanner.nextLine();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passLength; i++) {
            String charCollection = CHARS.get(randomNumber(0, CHARS.size() - 1));
            password.append(charCollection.charAt(randomNumber(0, charCollection.length() - 1)));
        }

        System.out.println("Voici votre mot de passe :");
        System.out.println(password);

        System.out.println("Souhaitez-vous stocker votre mot de passe ? O/N");
        String stockPass = scanner.nextLine();

        if (stockPass.equalsIgnoreCase("O")) {
            FileManager.writePassword(passName, password.toString());
            System.out.println("Votre mot de passe à été stocké (" + passName + ")");
        } else {
            System.out.println("Votre mot de passe n'a pas été stocké");
        }
    }
}
