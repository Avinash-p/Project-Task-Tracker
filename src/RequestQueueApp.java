//Author: Avinash Pandey
//Created date: 29th Oct 2024
//Last modified date: 29th Oct 2024
//Description: Application to create 3 queue of requests: requestQueue, completedQueue, removedQueue
//Actions that can be performed on Queue: enqueue request, dequeue or complete top request, get top request without dequeueing it and search/print all requests.
//Next feature: Add a date field on the request and sort queue everytime a new request is added

import java.util.ArrayList;

public class RequestQueueApp {
    static Queue globalQueue = new Queue();
    static Queue requestQueue = new Queue();
    static Queue completedQueue = new Queue();
    static Queue removedQueue = new Queue();
    public static void main(String[] args) throws Exception {
        newRequest("2");

        requestQueue.printAllRequests();

        System.err.println("\n");
        newRequest("3");
        newRequest("4");

        requestQueue.printAllRequests();
        Request currentRequest = requestQueue.getTopRequest();
        System.out.println("Current request: "+currentRequest.getRequestID());

        newRequest("1");
        newRequest("2");
        requestQueue.printAllRequests();
        dequeue(requestQueue);
        completeRequest(requestQueue);
        completeRequest(requestQueue);
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

    static public void newRequest(String newRequestID){
        ArrayList<Request> queueCopy = requestQueue.getQueue();
        for (Request r : queueCopy){
            if (newRequestID == r.getRequestID()){
                System.out.println("Request with id \"" +r.getRequestID()+ "\" already exists. No new requests were created.");
                return;
            }
        }
        Request newReq = new Request(newRequestID);
        globalQueue.enqueue(newReq);
        requestQueue.enqueue(newReq);
    }

    static public void completeRequest(Queue targetQueue){
        Request completedRequest = targetQueue.completeRequest();
        if (completedRequest != null){
            completedQueue.enqueue(completedRequest);
            System.out.println("Request completed successfully.");
        }
        else {
            System.out.println("Nothing in queue to complete.");
        }
    }

    static public void dequeue(Queue targetQueue){
        Request removedRequest = targetQueue.dequeue();
        if (removedRequest != null){
            removedQueue.enqueue(removedRequest);
            System.out.println("Request dequeued successfully.");
        }
        else {
            System.out.println("Nothing in queue to dequeue.");
        }
    }

    static public Request findRequest(String search){
        try{
            Request target = searchRequestHelper(search);
            target.printRequest();
            return target;
        }catch(NullPointerException e){
            System.out.println("Request " +search+ " does not exist in the system.");
        }
        return null;
    }

    static public Request searchRequestHelper(String searchTerm){
        for (Request r: globalQueue.getQueue()){
            if (r.getRequestID()==searchTerm){
                return r;
            }
        }
        return null;
    }
}

class Queue{
    private ArrayList<Request> queue = new ArrayList<Request>();
    Queue(){
        System.out.println("Queue initialized.");
    }

    public void enqueue(Request x){
        queue.add(x);
        System.out.println("Request added to queue.");
    }

    public ArrayList<Request> getQueue(){
        return new ArrayList<Request>(queue);
    }

    public Request getTopRequest(){
        if (queue.size() != 0)
        {return queue.get(0);}
        return null;
    }

    public Request completeRequest(){
        if (getTopRequest() != null){
            Request targetRequest = getTopRequest();
            System.out.println("Marking request \"" + targetRequest.getRequestID() + "\" as closed");
            targetRequest.markStatus("Completed");
            targetRequest = dequeueHelper();
            return targetRequest;
        } else{
            System.out.println("Nothing in queue to complete.");
            return null;
        }
    }

    public Request dequeue(){
        Request currentRequest;
        if (queue.size()==0){
            return null;
        }else{
            currentRequest = dequeueHelper();
            currentRequest.markStatus("Dequeued");
            return currentRequest;
        }
    }

    public Request dequeueHelper(){
        Request target = queue.get(0);
        queue.remove(0);
        System.out.println("Request removed from queue: " + target.getRequestID());
        return target;
    }

    public void printAllRequests(){
        if (queue.size()==0){
            System.out.println("Nothing in queue.");
            return;
        }
        System.out.println("Current requests in queue:");
        for (Request r : queue){
            r.printRequest();
        }
    }
}

class Request{
    private String requestID;
    private String status = "New";
    Request(String newRequestID){
        this.requestID = newRequestID;
        System.out.println("New Request created with id: " + requestID);
    }

    public String getRequestID(){
        return requestID;
    }

    public void markStatus(String x){
        status = x;
    }

    public void printRequest(){
        System.out.println("Request ID: "+requestID+", Request status: "+status);
    }
}
