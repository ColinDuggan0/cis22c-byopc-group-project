/**
 * Defines a max heap ADT backed by an ArrayList.
 * @author Howie Zeng
 * CIS 22C
 * @param <T> the data type stored in the heap
 */
import java.util.Comparator;
import java.util.NoSuchElementException;

public class Heap<T> {
   private int heapSize;
   private ArrayList<T> heap;
   private Comparator<T> cmp;
   
   /**
    * Constructs a Heap from an unordered ArrayList and a Comparator.
    * Sets heapSize, stores the parameters, inserts null at index 0,
    * and calls buildHeap().
    * @param data the unordered ArrayList of data
    * @param cmp the Comparator used to organize the heap
    */
   public Heap(ArrayList<T> data, Comparator<T> cmp) {
      heapSize = data.size();
      heap = data;
      this.cmp = cmp;
      heap.add(0, null);
      buildHeap();
   }
   
   /**
    * Bubbles an element down to its proper position in the heap.
    * @param index the index to heapify from
    */
   private void heapify(int index) {
      int left = getLeft(index);
      int right = getRight(index);
      int largest = index;
      
      if (left <= heapSize && cmp.compare(heap.get(left), heap.get(index)) > 0) {
         largest = left;
      }
      if (right <= heapSize && cmp.compare(heap.get(right), heap.get(largest)) > 0) {
         largest = right;
      }
      
      if (largest != index) {
         T temp = heap.get(index);
         heap.set(index, heap.get(largest));
         heap.set(largest, temp);
         heapify(largest);
      }
   }
   
   /**
    * Converts the ArrayList into a valid max heap.
    */
   public void buildHeap() {
      for (int i = heapSize / 2; i >= 1; i--) {
         heapify(i);
      }
   }
   
   /**
    * Returns the index of the left child of the element at index.
    * @param index the current index
    * @return the index of the left child
    * @throws IndexOutOfBoundsException when index is invalid
    */
   public int getLeft(int index) throws IndexOutOfBoundsException {
      if (index <= 0 || index > heapSize) {
         throw new IndexOutOfBoundsException("Error: index out of bounds.");
      }
      return 2 * index;
   }
   
   /**
    * Returns the index of the right child of the element at index.
    * @param index the current index
    * @return the index of the right child
    * @throws IndexOutOfBoundsException when index is invalid
    */
   public int getRight(int index) throws IndexOutOfBoundsException {
      if (index <= 0 || index > heapSize) {
         throw new IndexOutOfBoundsException("Error: index out of bounds.");
      }
      return 2 * index + 1;
   }
   
   /**
    * Returns the current heap size.
    * @return the number of elements in the heap
    */
   public int getHeapSize() {
      return heapSize;
   }
   
   /**
    * Returns the index of the parent of the element at index.
    * @param index the current index
    * @return the index of the parent
    * @throws IndexOutOfBoundsException when index is invalid
    */
   public int getParent(int index) throws IndexOutOfBoundsException {
      if (index <= 1 || index > heapSize) {
         throw new IndexOutOfBoundsException("Error: index out of bounds.");
      }
      return index / 2;
   }
   
   /**
    * Returns the maximum value in the heap.
    * @return the maximum value
    */
   public T getMax() {
      return heap.get(1);
   }

   /**
    * Inserts a value into the heap and restores heap order by bubbling up.
    * @param value the value to insert
    */
   public void insert(T value) {
      heap.add(value);
      heapSize++;

      int index = heapSize;
      while (index > 1) {
         int parent = getParent(index);
         if (cmp.compare(heap.get(index), heap.get(parent)) <= 0) {
            break;
         }

         T temp = heap.get(index);
         heap.set(index, heap.get(parent));
         heap.set(parent, temp);
         index = parent;
      }
   }

   /**
    * Removes and returns the maximum value from the heap.
    * @return the removed maximum value
    * @throws NoSuchElementException when the heap is empty
    */
   public T extractMax() {
      if (isEmpty()) {
         throw new NoSuchElementException("Error: Heap is empty.");
      }

      T max = heap.get(1);
      T last = heap.remove(heapSize);
      heapSize--;

      if (!isEmpty()) {
         heap.set(1, last);
         heapify(1);
      }

      return max;
   }

   /**
    * Indicates whether the heap currently has no elements.
    * @return true when heapSize is 0, false otherwise
    */
   public boolean isEmpty() {
      return heapSize == 0;
   }

   /**
    * Removes all elements from the heap while preserving 1-based indexing.
    */
   public void clear() {
      heap = new ArrayList<>();
      heap.add(null);
      heapSize = 0;
   }

   /**
    * Returns a max-to-min sorted snapshot without mutating this heap.
    * @return a sorted copy of the heap data
    */
   public ArrayList<T> heapSortSnapshot() {
      ArrayList<T> dataCopy = new ArrayList<>();
      for (int i = 1; i <= heapSize; i++) {
         dataCopy.add(heap.get(i));
      }

      Heap<T> copyHeap = new Heap<>(dataCopy, cmp);
      ArrayList<T> sorted = new ArrayList<>();
      while (!copyHeap.isEmpty()) {
         sorted.add(copyHeap.extractMax());
      }
      return sorted;
   }
   
   /**
    * Returns the element at the specified index.
    * @param index the index in the heap
    * @return the data stored at the index
    * @throws IndexOutOfBoundsException when index is invalid
    */
   public T getElement(int index) {
      if (index <= 0 || index > heapSize) {
         throw new IndexOutOfBoundsException("Error: index out of bounds.");
      }
      return heap.get(index);
   }
   
   /**
    * Returns the heap as a comma-separated String.
    * @return the String representation of the heap
    */
   @Override
   public String toString() {
      String result = "";
      
      for (int i = 1; i <= heapSize; i++) {
         result += heap.get(i);
         if (i < heapSize) {
            result += ", ";
         }
      }
      
      return result;
   }
}
