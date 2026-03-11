/**
 * HashTable.java
 * @author felipa mendez
 * @author no partner
 * CIS 22C
 */

public class HashTable<T> {
    private int numElements;
    private ArrayList<LinkedList<T>> table;

    /** Constructors */
   /**
    * Constructs a HashTable with the specified number of buckets.
    * @param size the number of buckets in the table
    * @throws IllegalArgumentException if size <= 0
   */
   public HashTable(int size) throws IllegalArgumentException {
      if (size <= 0) {
         throw new IllegalArgumentException("");
      }
      table = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
         table.add(new LinkedList<>());  
      }
      numElements = 0;
      
   }
   
   /**
    * Constructs a HashTable with a specified number of buckets
    * and inserts the elements from the given array.
    *
    * @param array the array of elements to insert
    * @param size the number of buckets
    * @throws IllegalArgumentException if size <= 0
    */
   public HashTable(T[] array, int size) throws IllegalArgumentException {
      
      
      if (size <= 0) {
         throw new IllegalArgumentException("");
      }   
      
      table = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
         table.add(new LinkedList<>());   
      }
       
      numElements = 0; 
      
      if (array != null) {
         for (int i = 0; i < array.length; i++) {
            add(array[i]);   
         }
      }
      
      
   }
   
    /** Accessors */
   /**
    * Computes the hash index for a given object.
    * @param obj the object to hash
    * @return the bucket index where the object belongs
   */
   private int hash(T obj) {
      return Math.abs(obj.hashCode()) % table.size();
   }
   
   /**
    * Returns the number of elements stored in the table.
    * @return the total number of elements
    */
   public int getNumElements() {
      return numElements;   
   }
   
   /**
    * Returns the number of elements stored in a specific bucket.
    *
    * @param index the bucket index
    * @return the number of elements in that bucket
    * @throws IndexOutOfBoundsException if index is invalid
    */
   public int countBucket(int index) throws IndexOutOfBoundsException {
      if (index < 0 || index >= table.size()) { 
         throw new IndexOutOfBoundsException("");
      }
      LinkedList<T> ll = table.get(index);
      return ll.getLength();
   }
   
   /**
    * Determines whether the table contains the specified element.
    *
    * @param elmt the element to search for
    * @return true if the element exists, false otherwise
    * @throws NullPointerException if elmt is null
    */
   public boolean contains(T elmt) throws NullPointerException {
      if (elmt == null) {
         throw new NullPointerException("");
      }   
      int found = find(elmt);
      
      if (found == -1) {
        return false;   
      }
      
      return true;
   }

    /** Mutators */
    
   /**
    * Inserts a new element in the table at the end of the chain
    * of the correct bucket.
    *
    * @param elmt the element to insert
    * @precondition elmt != null
    * @throws NullPointerException when the precondition is violated
    */
   public void add(T elem) throws NullPointerException {
      if (elem == null) {
         throw new NullPointerException("");
      } 
      
      int index = hash(elem);
      //go to index in arl
      table.get(index).addLast(elem);
      //insert the value at end linked lsit
      numElements++;
   }

   /**
    * Calculates the current load factor of the table.
    * @return the load factor (numElements / number of buckets)
    */
   public double getLoadFactor() {
      return (double) numElements / table.size();   
   }
    /** Additional Methods */

   /**
    * Returns a String representation of a specific bucket.
    * @param bucket the index of the bucket to print
    * @return a String containing the elements in the bucket
    * @throws IndexOutOfBoundsException if bucket index is invalid
    */
   public String bucketToString(int bucket) throws IndexOutOfBoundsException {
      if (bucket < 0 || bucket >= table.size()) {
         throw new IndexOutOfBoundsException("");
      }
      
      return table.get(bucket).toString();
   }
   
   /**
    * Searches for an element in the table.
    * @param elmt the element to search for
    * @return the bucket index where the element is found, or -1 if not found
    * @throws NullPointerException if elmt is null
    */
   public int find(T elmt) throws NullPointerException {
      if (elmt == null) {
         throw new NullPointerException("");
      }
      int index = hash(elmt);
      
      LinkedList<T> ll = table.get(index);
      if (ll.findIndex(elmt) != -1) {
         return index;
      }

      return -1;
   }
   
   
   /**
    * Accesses a specified key in the Table.
    * @param elmt the key to search for
    * @return the value to which the specified key is mapped, or null if this table contains no mapping for the key.
    * @precondition elmt != null
    * @throws NullPointerException when the precondition is violated.
    */
   public T get(T elmt) {
      if (elmt == null) {
         throw new NullPointerException("");
      }
      
      int index = hash(elmt);
      LinkedList<T> list = table.get(index);
      int position = list.findIndex(elmt);
      
      if (position != -1) {
         list.positionIterator();
         list.advanceIteratorToIndex(position);
         return list.getIterator();  
      }
      
      return null;
   }
   
   /**
    * Removes the specified element from the table.
    *
    * @param elmt the element to remove
    * @return true if the element was removed, false otherwise
    * @throws NullPointerException if elmt is null
    */
   public boolean delete(T elmt) throws NullPointerException {
      if (elmt == null) {
         throw new NullPointerException("");
      }
      
      int index = hash(elmt);
      
      LinkedList<T> list = table.get(index);
      
      int position = list.findIndex(elmt);
      
      if (position > -1) {
        list.positionIterator();
        list.advanceIteratorToIndex(position);
        list.removeIterator();
        numElements--;
        return true;
      }
      return false;
      
      
   }
   
   /**
    * Removes all elements from the table.
    * After this call, the table will be empty.
    */
   public void clear() {
      
      for (int i = 0; i < table.size(); i++) {
         table.get(i).clear();
      }
      numElements = 0;
   }
   
   /**
    * Returns a String representation showing the first element
    * in each bucket.
    *
    * @return a formatted String of bucket indices and first elements
    */
   public String rowToString() {
      String result = "";
      for (int i = 0; i < table.size(); i++) {
         if (table.get(i).isEmpty()) {
            result += "Bucket " + i + ": empty";
         } else {
            result += "Bucket " + i + ": " + table.get(i).getFirst();
         }
         result += "\n";
      }
      return result;
         
   }
   
   /**
    * Returns a String representation of the entire table,
    * listing all elements in each bucket.
    *
    * @return a formatted String of all buckets and their contents
    */
   @Override public String toString() {
      String result = "";
      for (int i = 0; i < table.size(); i++) {
         if (!(table.get(i).isEmpty())) {
            result += table.get(i).toString();
         } 
      }
      return result += "\n";
   }
}


