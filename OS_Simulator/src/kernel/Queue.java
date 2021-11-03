package kernel;

public class Queue {
	private static int front, rear, capacity;
    public static PCB queue[];
 
    public Queue(int c)
    {
        front = rear = 0;
        capacity = c;
        queue = new PCB[capacity];
    }
    
 
    // function to insert an element
    // at the rear of the queue
     void queueEnqueue(PCB data)
    {
        // check queue is full or not
        if (capacity == rear) {
            System.out.printf("\nQueue is full\n");
            return;
        }
 
        // insert element at the rear
        else {
            queue[rear] = data;
            rear++;
        }
        return;
    }
 
    // function to delete an element
    // from the front of the queue
    static PCB queueDequeue()
    {
    	PCB removed = null;
    	
        // if queue is empty
        if (front == rear) {
            System.out.printf("\nQueue is empty\n");
            return null;
        }
 
        // shift all the elements from index 2 till rear
        // to the right by one
        else {
        	removed = queue[front];
        	
        	
            for (int i = 0; i < rear - 1; i++) {
                queue[i] = queue[i + 1];
            }
            
            
 
            // store null at rear indicating there's no element
            if (rear < capacity)
                queue[rear] = null;
 
            // decrement rear
            rear--;
        }
        return removed;
        
    }
 
    // print queue elements
    static void queueDisplay()
    {
        int i;
        if (front == rear) {
            System.out.printf("\nQueue is Empty\n");
            return;
        }
 
        // traverse front to rear and print elements
        for (i = front; i < rear; i++) {
        	if(queue[i]==null)
        		return;
            System.out.printf(" Process Pid: %d <-- ", queue[i].pid);
        }
        return;
    }
 
    // print front of queue
    static void queueFront()
    {
        if (front == rear) {
            System.out.printf("\nQueue is Empty\n");
            return;
        }
        System.out.printf("\nFront Element is: %d", queue[front].pid);
        return;
    }
    
    public int getSize() {
    	return this.rear;
    }

}
