package fr.nlohya.passwordbank;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class FileManager {

    public static String FILENAME = "password.json";

    public static void writePassword(String name, String password) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        FileReader fileReader = null;
        JSONArray currentData = null;

        try {
            fileReader = new FileReader(FILENAME);
        } catch (IOException ioException) {
            System.out.println("Le fichier n'existe pas encore.");
        }

        if (fileReader != null) {
            System.out.println("Fichier de mot de passe trouvé. Sauvegarde en cours");
            try {
                currentData = (JSONArray) parser.parse(fileReader);
            } catch (Exception ignored) {}
        }

        FileWriter fileWriter = new FileWriter(FILENAME);

        JSONObject passwordJson = new JSONObject();
        passwordJson.put(name, password);

        JSONArray passwords = new JSONArray();
        passwords.add(passwordJson);

        if (currentData != null) {
            for (Object currentDatum : currentData) {
                JSONObject dataJsonObj = (JSONObject) currentDatum;
                passwords.add(dataJsonObj);
            }
        }

        fileWriter.write(passwords.toJSONString());
        fileWriter.flush();
    }

    public static String readPassword(String passName) {
        JSONParser parser = new JSONParser();

        AtomicReference<String> retPass = null;

        FileReader fileReader = null;

        try {
            fileReader = new FileReader(FILENAME);

            JSONArray readJson = (JSONArray) parser.parse(fileReader);
            readJson.forEach(jsonObj -> {
                try {
                    JSONObject jsonObject = (JSONObject) parser.parse(jsonObj.toString());
                    retPass.set(jsonObject.get(passName).toString());
                } catch (Exception ignored) {}
            });
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
        try {
            return retPass.get();
        } catch (Exception ignored) {
            return null;
        }
    }
}
