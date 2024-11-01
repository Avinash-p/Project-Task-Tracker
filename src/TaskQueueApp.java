//Author: Avinash Pandey
//Created date: 29th Oct 2024
//Last modified date: 29th Oct 2024
//Description: Application to create 3 queue of tasks: taskQueue, completedQueue, removedQueue
//Actions that can be performed on Queue: enqueue task, dequeue or complete top task, get top task without dequeueing it and search/print all tasks.
//Next feature: Add a date field on the task and sort queue everytime a new task is added

import java.util.ArrayList;
import java.util.Date;

public class TaskQueueApp {
    static Queue globalQueue = new Queue();
    static Queue taskQueue = new Queue();
    static Queue completedQueue = new Queue();
    static Queue removedQueue = new Queue();

    static public void newTask(String newTaskID, String newTitle, User newAssignee){
        ArrayList<Task> queueCopy = taskQueue.getQueue();
        for (Task r : queueCopy){
            if (newTaskID == r.getTaskID()){
                System.out.println("task with id \"" +r.getTaskID()+ "\" already exists. No new tasks were created.");
                return;
            }
        }
        Task newReq = new Task(newTaskID, newTitle, newAssignee);
        globalQueue.enqueue(newReq);
        taskQueue.enqueue(newReq);
        newAssignee.userQueue.enqueue(newReq);
    }

    static public void createNewTask(String newTaskID, String newTitle, User newAssignee, Date newTargetDate, int newPriority){
        ArrayList<Task> queueCopy = taskQueue.getQueue();
        for (Task r : queueCopy){
            if (newTaskID == r.getTaskID()){
                System.out.println("task with id \"" +r.getTaskID()+ "\" already exists. No new tasks were created.");
                return;
            }
        }
        Task newReq = new Task(newTaskID, newTitle, newAssignee, newTargetDate, newPriority);
        globalQueue.enqueue(newReq);
        taskQueue.enqueue(newReq);
        newAssignee.userQueue.enqueue(newReq);
    }

    static public void completeTask(Queue targetQueue){
        Task completedTask = targetQueue.completeTask();
        if (completedTask != null){
            completedQueue.enqueue(completedTask);
            System.out.println("task completed successfully.");
        }
        else {
            System.out.println("Nothing in queue to complete.");
        }
    }

    static public void dequeue(Queue targetQueue){
        Task removedTask = targetQueue.dequeue();
        if (removedTask != null){
            removedQueue.enqueue(removedTask);
            System.out.println("task dequeued successfully.");
        }
        else {
            System.out.println("Nothing in queue to dequeue.");
        }
    }

    static public Task findTask(String search){
        try{
            Task target = searchTaskHelper(search, globalQueue);
            target.printTask();
            return target;
        }catch(NullPointerException e){
            System.out.println("task " +search+ " does not exist in the system.");
        }
        return null;
    }

    static public Task findTask(String search, Queue targetQueue){
        try{
            Task target = searchTaskHelper(search, targetQueue);
            target.printTask();
            return target;
        }catch(NullPointerException e){
            System.out.println("task " +search+ " does not exist in the system.");
        }
        return null;
    }

    static public Task searchTaskHelper(String searchTerm, Queue targetQueue){
        for (Task r: targetQueue.getQueue()){
            if (r.getTaskID()==searchTerm){
                return r;
            }
        }
        return null;
    }
}

class Queue{
    private ArrayList<Task> queue = new ArrayList<Task>();

    public void enqueue(Task x){
        queue.add(x);
    }

    public ArrayList<Task> getQueue(){
        return new ArrayList<Task>(queue);
    }

    public Task getTopTask(){
        if (queue.size() != 0)
        {return queue.get(0);}
        return null;
    }

    public Task completeTask(){
        if (getTopTask() != null){
            Task targetTask = getTopTask();
            System.out.println("Marking task \"" + targetTask.getTaskID() + "\" as closed");
            targetTask = dequeueHelper();
            targetTask.markStatus("Completed");
            return targetTask;
        } else{
            System.out.println("Nothing in queue to complete.");
            return null;
        }
    }

    public Task dequeue(){
        Task currentTask;
        if (queue.size()==0){
            return null;
        }else{
            currentTask = dequeueHelper();
            currentTask.markStatus("Dequeued");
            return currentTask;
        }
    }

    public Task dequeueHelper(){
        Task target = queue.get(0);
        queue.remove(0);
        System.out.println("task removed from queue: " + target.getTaskID());
        return target;
    }

    public void printAllTasks(){
        if (queue.size()==0){
            System.out.println("Nothing in queue.");
            return;
        }
        System.out.println("Current tasks in queue:");
        for (Task r : queue){
            r.printTask();
        }
    }

    public Task dequeueTask(Task task){
        queue.remove(task);
        return task;
    }
}

class Task{
    private String taskID;
    private String status = "New";
    private String title;
    private User assignee;
    private Date targetDate;
    private int priority;
    
    Task(String newTaskID, String newTitle, User newAssignee){
        this.taskID = newTaskID;
        this.title = newTitle;
        this.assignee = newAssignee;
        System.out.println("New task created with id: " + taskID);
    }

    Task(String newTaskID, String newTitle, User newAssignee, Date newTargetDate, int newPriority){
        this.taskID = newTaskID;
        this.title = newTitle;
        this.assignee = newAssignee;
        this.targetDate = newTargetDate;
        this.priority = newPriority;
        System.out.println("New task created with id: " + taskID);
    }

    public String getTaskID(){
        return taskID;
    }

    public void markStatus(String x){
        status = x;
        if (status == "Completed"){
            this.assignee.userQueue.dequeueHelper();
        }
    }

    public void printTask(){
        System.out.println("task ID: "+taskID+", task status: "+status+", Title: "+title+", Assignee: "+assignee.getUsername()+", Target Date: "+targetDate+", Priority: "+priority);
    }

    public User getAssignee(){
        return assignee;
    }

    public void changeAssignee(User user){
        this.assignee = user;
    }
}
