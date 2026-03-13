import java.util.NoSuchElementException;

public class Queue<T> {
   private final LinkedList<T> list;

   public Queue() {
      list = new LinkedList<>();
   }

   public boolean isEmpty() {
      return list.isEmpty();
   }

   public void enqueue(T data) {
      list.addLast(data);
   }

   public void dequeue() {
      if (isEmpty()) {
         throw new NoSuchElementException("Queue is empty.");
      }
      list.removeFirst();
   }

   public T getFront() {
      if (isEmpty()) {
         throw new NoSuchElementException("Queue is empty.");
      }
      return list.getFirst();
   }
}
