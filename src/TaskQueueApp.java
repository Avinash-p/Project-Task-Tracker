//Author: Avinash Pandey
//Created date: 29th Oct 2024
//Last modified date: 29th Oct 2024
//Description: Application to create 3 queue of requests: requestQueue, completedQueue, removedQueue
//Actions that can be performed on Queue: enqueue request, dequeue or complete top request, get top request without dequeueing it and search/print all requests.
//Next feature: Add a date field on the request and sort queue everytime a new request is added

import java.util.ArrayList;
import java.util.Date;

public class TaskQueueApp {
    static Queue globalQueue = new Queue();
    static Queue requestQueue = new Queue();
    static Queue completedQueue = new Queue();
    static Queue removedQueue = new Queue();
    public static void main(String[] args) throws Exception {


        requestQueue.printAllRequests();

        System.err.println("\n");


        requestQueue.printAllRequests();
        Task currentRequest = requestQueue.getTopTask();
        System.out.println("Current request: "+currentRequest.getTaskID());


        requestQueue.printAllRequests();
        dequeue(requestQueue);
        completeTask(requestQueue);
        completeTask(requestQueue);
        System.out.println("\nRequest queue printout:");
        requestQueue.printAllRequests();
        System.out.println("\nCompleted queue printout:");
        completedQueue.printAllRequests();
        System.out.println("\nRemoved queue printout:");
        removedQueue.printAllRequests();
        findRequest("5");
        findRequest("2");
        findRequest("3");
        findRequest("4");
        findRequest("1");

    }

    static public void newTask(String newTaskID, String newTitle, User newAssignee){
        ArrayList<Task> queueCopy = requestQueue.getQueue();
        for (Task r : queueCopy){
            if (newTaskID == r.getTaskID()){
                System.out.println("Request with id \"" +r.getTaskID()+ "\" already exists. No new requests were created.");
                return;
            }
        }
        Task newReq = new Task(newTaskID, newTitle, newAssignee);
        globalQueue.enqueue(newReq);
        requestQueue.enqueue(newReq);
        newAssignee.userQueue.enqueue(newReq);
    }

    static public void createNewTask(String newTaskID, String newTitle, User newAssignee, Date newTargetDate, int newPriority){
        ArrayList<Task> queueCopy = requestQueue.getQueue();
        for (Task r : queueCopy){
            if (newTaskID == r.getTaskID()){
                System.out.println("Request with id \"" +r.getTaskID()+ "\" already exists. No new requests were created.");
                return;
            }
        }
        Task newReq = new Task(newTaskID, newTitle, newAssignee, newTargetDate, newPriority);
        globalQueue.enqueue(newReq);
        requestQueue.enqueue(newReq);
        newAssignee.userQueue.enqueue(newReq);
    }

    static public void completeTask(Queue targetQueue){
        Task completedRequest = targetQueue.completeTask();
        if (completedRequest != null){
            completedQueue.enqueue(completedRequest);
            System.out.println("Request completed successfully.");
        }
        else {
            System.out.println("Nothing in queue to complete.");
        }
    }

    static public void dequeue(Queue targetQueue){
        Task removedRequest = targetQueue.dequeue();
        if (removedRequest != null){
            removedQueue.enqueue(removedRequest);
            System.out.println("Request dequeued successfully.");
        }
        else {
            System.out.println("Nothing in queue to dequeue.");
        }
    }

    static public Task findRequest(String search){
        try{
            Task target = searchRequestHelper(search);
            target.printTask();
            return target;
        }catch(NullPointerException e){
            System.out.println("Request " +search+ " does not exist in the system.");
        }
        return null;
    }

    static public Task searchRequestHelper(String searchTerm){
        for (Task r: globalQueue.getQueue()){
            if (r.getTaskID()==searchTerm){
                return r;
            }
        }
        return null;
    }
}

class Queue{
    private ArrayList<Task> queue = new ArrayList<Task>();
    Queue(){
        System.out.println("Queue initialized.");
    }

    public void enqueue(Task x){
        queue.add(x);
        System.out.println("Request added to queue.");
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
            Task targetRequest = getTopTask();
            System.out.println("Marking task \"" + targetRequest.getTaskID() + "\" as closed");
            targetRequest = dequeueHelper();
            targetRequest.markStatus("Completed");
            return targetRequest;
        } else{
            System.out.println("Nothing in queue to complete.");
            return null;
        }
    }

    public Task dequeue(){
        Task currentRequest;
        if (queue.size()==0){
            return null;
        }else{
            currentRequest = dequeueHelper();
            currentRequest.markStatus("Dequeued");
            return currentRequest;
        }
    }

    public Task dequeueHelper(){
        Task target = queue.get(0);
        queue.remove(0);
        System.out.println("Request removed from queue: " + target.getTaskID());
        return target;
    }

    public void printAllRequests(){
        if (queue.size()==0){
            System.out.println("Nothing in queue.");
            return;
        }
        System.out.println("Current requests in queue:");
        for (Task r : queue){
            r.printTask();
        }
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
        System.out.println("New Request created with id: " + taskID);
    }

    Task(String newTaskID, String newTitle, User newAssignee, Date newTargetDate, int newPriority){
        this.taskID = newTaskID;
        this.title = newTitle;
        this.assignee = newAssignee;
        this.targetDate = newTargetDate;
        this.priority = newPriority;
        System.out.println("New Request created with id: " + taskID);
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
        System.out.println("Request ID: "+taskID+", Request status: "+status+", Title: "+title+", Assignee: "+assignee.getUsername()+", Target Date: "+targetDate+", Priority: "+priority);
    }
}
