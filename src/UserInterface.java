import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserInterface {
    public static final Hasher HASHER = new Hasher(2384798);
    public static UserDataBase userDB = new UserDataBase();
    public static final Scanner SCANNER = new Scanner(System.in);
    public static TaskQueueApp taskQueueApp = new TaskQueueApp();
    public static DateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
    public static void main (String[] args){
        createNewUser("a1", HASHER.hash("a1"));
        createNewUser("a2", HASHER.hash("a2"));
        createNewUser("a3", HASHER.hash("a3"));
        try{
            TaskQueueApp.createNewTask("1","1",userDB.getUser("a1"),dateFormatter.parse("11-01-2024"),1);
            TaskQueueApp.createNewTask("2","2",userDB.getUser("a1"),dateFormatter.parse("11-02-2024"),2);
            TaskQueueApp.createNewTask("3","3",userDB.getUser("a2"),dateFormatter.parse("11-03-2024"),3);
            TaskQueueApp.createNewTask("4","4",userDB.getUser("a2"),dateFormatter.parse("11-04-2024"),4);
            TaskQueueApp.createNewTask("5","5",userDB.getUser("a3"),dateFormatter.parse("11-05-2024"),5);
            TaskQueueApp.createNewTask("6","6",userDB.getUser("a3"),dateFormatter.parse("11-06-2024"),6);
        }catch(ParseException e){
            System.out.println("Date formatting error");
            return;
        }
        
        while (true){
            System.out.println("Welcome to main menu! Enter a for user management menu. Enter b for task management menu. Enter c to exit.");
            String input = SCANNER.nextLine();
            if (input.equals("a")){
                userManagement();
            }
            if (input.equals("b")){
                taskManagement();
            }
            if (input.equals("c")){
                break;
            }
        }
    }
    public static void userManagement(){
        while (true){
            System.out.println("Welcome to user management menu! Enter a to create a user. Enter b to login with existing user. Enter c to go back to main menu.");
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
    public static void taskManagement(){
        while (true){
            System.out.println("Welcome to task management menu! Enter a to create a new task. Enter b to get all tasks for any user and perform task actions. Enter c to go back to main menu.");
            String input = SCANNER.nextLine();
            if (input.equals("a")){
                createNewTaskWrapper();
            }
            if (input.equals("b")){
                getUserTasks();
                taskActionFlow();
            }
            if (input.equals("c")){
                break;
            }
        }
    }
    public static void taskActionFlow(){
        System.out.println("Login is required to access tasks!");
        User temp3 = loginFlow();
        if (temp3 == null){
            return;
        }
        while (true){
            System.out.println("Welcome to task actions menu! Enter a to complete a task. Enter b to remove a task from queue. Enter c to unassign a task from selected user. Enter d to go back to main menu.");
            String input = SCANNER.nextLine();
            if (input.equals("a")){
                completeTask(temp3);
            }
            if (input.equals("b")){
                removeTask(temp3);
            }
            if (input.equals("c")){
                unassignTask(temp3);
            }
            if (input.equals("d")){
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
    public static User loginFlow(){
        System.out.println("Login flow! Enter your username: ");
        String temp1 = SCANNER.nextLine();
        System.out.println("Enter your password: ");
        String temp2 = SCANNER.nextLine();
        if(attemptLogin(temp1, HASHER.hash(temp2))){
            return userDB.getUser(temp1);
        }else{
            return null;
        }
    }
    public static void createNewUser(String username, String password){
        User newUser = new User(username, password);
        userDB.addUserToDB(username, newUser);
    }
    public static boolean attemptLogin(String username, String password){
        if (userDB.attemptLogin(username, password)){
            System.out.println("Login success!");
            return true;
        } else{
            System.out.println("Login failed!");
            return false;
        }
    }
    public static void createNewTaskWrapper(){
        Date temp4;
        System.out.println("New task flow! Enter your new task id: ");
        String temp1 = SCANNER.nextLine();
        System.out.println("New task flow! Enter your new task title: ");
        String temp2 = SCANNER.nextLine();
        System.out.println("New task flow! Enter your username: ");
        String temp31 = SCANNER.nextLine();
        User temp3 = userDB.getUser(temp31);
        System.out.println("Enter your target date in MM-DD-YYYY format: ");
        String temp41 = SCANNER.nextLine();
        try{
            temp4 = dateFormatter.parse(temp41);
        }catch(ParseException e){
            System.out.println("Date formatting error");
            return;
        }
        System.out.println("Enter your task's priority: ");
        int temp5 = SCANNER.nextInt();
        TaskQueueApp.createNewTask(temp1, temp2, temp3, temp4, temp5);
    }
    public static void getUserTasks(){
        System.out.println("New task flow! Enter your username: ");
        String temp31 = SCANNER.nextLine();
        User temp3 = userDB.getUser(temp31);
        System.out.println("List of all tasks for user "+temp31+":");
        for (Task i : temp3.getUserTasks()){
            i.printTask();
        }
    }
    public static Task getTaskById(User user, String id){
        for (Task i : user.getUserTasks()){
            String x = i.getTaskID();
            if (x.equals(id)){
                System.out.println("Match found!");
                return i;
            }
        }
        return null;
    }
    public static void completeTask(User user){
        System.out.println("Select a task id from the above tasks");
        Task task = getTaskById(user, SCANNER.nextLine());
        if (task != null){
            task.markStatus("Completed");
            user.userQueue.dequeueTask(task);
            TaskQueueApp.requestQueue.dequeueTask(task);
            TaskQueueApp.completedQueue.enqueue(task);
        } else{
            System.out.println("Task not found in current user's queue. Action cannot be completed.");
        }
    }
    public static void removeTask(User user){
        System.out.println("Select a task id from the above tasks");
        Task task = getTaskById(user, SCANNER.nextLine());
        if (task != null){
            task.markStatus("Removed");
            user.userQueue.dequeueTask(task);
            TaskQueueApp.requestQueue.dequeueTask(task);
            TaskQueueApp.removedQueue.enqueue(task);
        } else{
            System.out.println("Task not assigned to current user. Action cannot be completed.");
        }
    }
    public static void unassignTask(User user){
        System.out.println("Select a task id from the above tasks");
        Task task = getTaskById(user, SCANNER.nextLine());
        if (task != null){
            user.userQueue.dequeueTask(task);
            task.changeAssignee(null);
        } else{
            System.out.println("Task not assigned to current user. Action cannot be completed.");
        }
    }
}

class User{
    private String username, password;
    public Queue userQueue = new Queue();
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
    public ArrayList<Task> getUserTasks(){
        return userQueue.getQueue();
    }
    public String getUsername(){
        return username;
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
    public User getUser(String username){
        try{
            return allUsers.get(username);
        } catch(NullPointerException e){
            System.out.println("No such user exists");
        }
        return null;
    }
}