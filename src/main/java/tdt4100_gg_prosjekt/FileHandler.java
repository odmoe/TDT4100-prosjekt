package tdt4100_gg_prosjekt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileHandler implements IFileHandler {


    private static FileHandler onlyInstance = new FileHandler();

    private FileHandler() throws IllegalArgumentException {
    }

    public static FileHandler getInstance() throws IllegalArgumentException {
        return onlyInstance;
    }
    

    @Override
    public List<List<String>> readFile(String filename, String splitOn) throws FileNotFoundException {
        List<List<String>> users = new ArrayList<List<String>>();
        File file = Paths.get(filename).toFile();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] lineInfo = line.split(splitOn);
            users.add(Arrays.asList(lineInfo));
        }
        scanner.close();
        return users;
    }

    @Override
    public void writeToFile(String filename, String write, boolean append) throws FileNotFoundException {
        try{
            FileWriter fileWriter = new FileWriter(filename, append);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter writer = new PrintWriter(bufferedWriter);
            writer.println(write);
            writer.flush();
            writer.close();
        
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Map<String,String> getUsersFromFile(String info) throws FileNotFoundException{
        List<List<String>> file = readFile("src/main/resources/tdt4100_gg_prosjekt/Users.txt", ":");
        Map<String, String> usersMap = new HashMap<>();

        if (info.equals("Password")){

            for (List<String> list : file){
                String username = list.get(0);
                String password = list.get(1);

                usersMap.put(username, password);
            }
        }

        if (info.equals("Balance")){

            for (List<String> list : file) {
                String username = list.get(0);
                String balance = list.get(2);

                usersMap.put(username, balance);
            }
        }
        return usersMap;
    }

    public void newUserToFile(String username, String password) throws FileNotFoundException{
        if (isValidUserNameAndPassWord(username, password)) {
            String newUser = username + ":" + password + ":" + "100";
            writeToFile("src/main/resources/tdt4100_gg_prosjekt/Users.txt", newUser, true); 
        }else{
            throw new IllegalArgumentException("Username or Password can't contain ':'");
        }
    }

    /*denne valideringen er så kort at denne kunne ha blitt inkludert i newUserToFile,
    men det er god praksis å ha egne valideringsmetoder
    */

    private boolean isValidUserNameAndPassWord(String username, String password) {
        return !(password.contains(":") || username.contains(":") );
    }

    public boolean checkUser(String username, String password){
        try {
            Map<String,String> usersMap = getUsersFromFile("Password");
            return usersMap.containsKey(username) && usersMap.get(username).equals(password);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean doesUserNameAlreadyExist(String s) throws FileNotFoundException{
        return getUsersFromFile("Password").containsKey(s);
    }

    public void newBalance(String username, String balance) throws FileNotFoundException{

        List<List<String>> file = readFile("src/main/resources/tdt4100_gg_prosjekt/Users.txt", ":");
        clearFile();

        for (List<String> line : file) {
            if (line.get(0).equals(username)) {
                line.set(2,balance+"");
            }

            String toAppendForThisIteration = line.get(0) + ":" + line.get(1) + ":" + line.get(2);
            if (file.indexOf(line) == 0) {
                writeToFile("src/main/resources/tdt4100_gg_prosjekt/Users.txt", toAppendForThisIteration,false);
            } else {
                writeToFile("src/main/resources/tdt4100_gg_prosjekt/Users.txt", toAppendForThisIteration,true);
            }
        } 

    }



    private void clearFile() throws FileNotFoundException{
        writeToFile("src/main/resources/tdt4100_gg_prosjekt/Users.txt", "", false);
    }
}
