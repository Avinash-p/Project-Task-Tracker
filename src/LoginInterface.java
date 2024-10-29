import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginInterface {
    public static final Hasher HASHER = new Hasher(2384798);
    public static UserDataBase userDB = new UserDataBase();
    public static final Scanner SCANNER = new Scanner(System.in);
    public static void main (){
        while (true){
            System.out.println("Welcome! Enter a to create a user, enter b to login with existing user. Enter c to exit");
            String input = SCANNER.nextLine();
            if (input.equals("a")){
                userCreationFlow();
            }
            if (input.equals("b")){
                loginFlow();
            }
            if (input.equals("c")){
                break;
            }
        }
    }
    public static void userCreationFlow(){
        System.out.println("User creation flow! Enter a username: ");
        String temp1 = SCANNER.nextLine();
        System.out.println("Enter a password: ");
        String temp2 = SCANNER.nextLine();
        createNewUser(temp1, HASHER.hash(temp2));
    }

    public static void loginFlow(){
        System.out.println("Login flow! Enter your username: ");
        String temp1 = SCANNER.nextLine();
        System.out.println("Enter your password: ");
        String temp2 = SCANNER.nextLine();
        attemptLogin(temp1, HASHER.hash(temp2));
    }

    public static void createNewUser(String username, String password){
        User newUser = new User(username, password);
        userDB.addUserToDB(username, newUser);
    }
    public static void attemptLogin(String username, String password){
        if (userDB.attemptLogin(username, password)){
            System.out.println("Login success!");
        } else{
            System.out.println("Login failed!");
        }
    }
}

class User{
    private String username, password;
    User(String username, String password){
        this.username = username;
        this.password = password;
    }
    public void printUserDetails(){
        System.out.println("Username: "+username+"\nPassword: "+password);
    }
    public boolean checkPassword(String password){
        if (this.password.equals(password)){
            return true;
        }
        return false;
    }


}

class UserDataBase{
    private Map<String, User> allUsers = new HashMap<String, User>();
    public void addUserToDB(String username, User user){
        allUsers.put(username, user);
    }
    public void printAllUsers(){
        for (String i : allUsers.keySet()){
            allUsers.get(i).printUserDetails();
        }
    }
    public boolean attemptLogin(String username, String password){
        try{if (allUsers.get(username).checkPassword(password)){
            return true;
        }}catch(NullPointerException e){
            System.out.println("No such user in database! Register now!");
        }
        return false;
    }
}