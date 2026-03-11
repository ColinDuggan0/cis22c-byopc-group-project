/** linked list CIS 22C
* @author Felipa Mendez
*/

import java.util.NoSuchElementException;

public class LinkedList <T> {
   private class Node {
      private T data;
      private Node next;
      private Node prev;
      
      
      /**
      * default constructor
      */
      public Node(T data) {
         this.data = data;
         this.next = null;
         this.prev = null;
      }
      
   }
   
   private Node first;
   private Node last;
   private int length;
   private Node iterator;
  
   /**
   * default constructor
   */
   public LinkedList() {
      first = null;
      last = null;
      length = 0;
   }
   
   /**constructor
   *  @param  array to make list
   * edgecase if null or length 0
   */
   public LinkedList(T[] array) {
      if (array == null || array.length == 0) {
         return; // calls default constructor
      } else {
         
         length = 1; 
         first = new Node(array[0]);
         last = first; // make a ll of 1 element
         //add node to end
         for (int i = 1; i < array.length; i++) {
            Node newNode = new Node(array[i]); // new node
            last.next = newNode; //connect previous node to new node
            newNode.prev = last; //connect new node to previous node
            last = newNode; //make the new node last
            length++;//increment length
         }
         iterator = null;
      }
   }
   
   /**copy constructor
   * @param original linked list ot copy
   * 2 edge cases if null or length 0
   */
   public LinkedList(LinkedList<T> original) {
      if (original == null || original.length == 0) { // edge case null or empty list
         return;   
      } else {
         Node temp = original.first; // make variable for first node to copt
         while (temp != null) { //loop through list and add end
            addLast(temp.data); // copy data with addLast
            temp = temp.next; //go to next node in iriginal list
         }
         iterator = null;
      }
   }
   
   
   
   /**
   * toString
   * @return string rep ll 
   */
   @Override public String toString() {
      String result = "";
      Node temp = first;
      while (temp != null) {
         result += temp.data + " ";
         temp = temp.next;
      }
      return result + "\n";
   }
   
   /**isEmpty
   * @return boolean rep is empty
   */
   public boolean isEmpty() {
      if (length == 0) {
         return true;
      } else {
         return false;
      }
   }
   
   /**getLength
   * @return int rep length
   */
   public int getLength() {
      return length;
   }
   
   /**addFirst
   * @param T data to add to list
   */
   public void addFirst(T data) {
      Node dataNode = new Node(data);
      if (length == 0) {
         first = last = dataNode;
      } else {
         first.prev = dataNode;
         dataNode.next = first;
         first = dataNode;
      } 
      length++;
   }
   
   /**getFirst
   * @return data in frist node
   */
   public T getFirst() throws NoSuchElementException {
      if (first == null) {
         throw new NoSuchElementException(
           "getFirst(): LinkedList cannot be empty." 
         );   
      }   
      return first.data;
   } 
   
   /**getLast
   *@return data in last node
   */
   public T getLast() throws NoSuchElementException {
      if (last == null) {
         throw new NoSuchElementException(
            "getLast(): LinkedList cannot be empty." 
         );   
      }   
      return last.data;
   }
   
   /** addLast
   *@param T for data to add
   */
   public void addLast(T data) {
      Node dataNode = new Node(data);
      if (length == 0) {
         first = last = dataNode;
      } else {
         last.next = dataNode;
         dataNode.prev = last;
         last = dataNode;
      }
      length++;
   }
   
   /**
   * removeFirst
   * edge case one node
   * @precondition linkedlist cant be empty
   * @throws NoSuchElementException
   */
   public void removeFirst() throws NoSuchElementException {
      if (length == 0) {
         throw new NoSuchElementException(
            "removeFirst(): LinkedList cannot be empty."   
         );   
      } else if (length == 1) {
         first = last = null;
         iterator = null;
      } else {
         first = first.next;
         first.prev = null;
         iterator = null;
      }
      length--;
   }
   
   /**
   * removeLast
   * edge case one node
   * @precondition linkedlist cant be empty
   * @throws NoSuchElementException
   */
   public void removeLast() throws NoSuchElementException {
      if (length == 0) {
         throw new NoSuchElementException(
            "removeLast(): LinkedList cannot be empty."   
         );
      } else if (length == 1) {
         first = last = null;   
         iterator = null;
      } else {
         last = last.prev;
         last.next = null;
         iterator = null;
      }
      length--;
   }
   
   /** places iterator at first node
   *
   *
   */
   public void positionIterator() {
      iterator = first;
   }
   
   /** returns data in iterator node
   * @precondition iterator cant be null
   *@return data in node
   *@throws NullPointerException if iterator null
   */
   public T getIterator() throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("getIterator(): cannot get a null iterator.");   
      }
      return iterator.data;
   }
   
   /** return if iterator off end / null
   * @return if iterator off end
   *
   */
   public boolean offEnd() {
      if (iterator == null) {
         return true;
      } else {
         return false;   
      }
   }
   
   /** moves iterator forward one node to last
   * @precondiition iterator cant be null
   * @postcondition iterator points to next node
   * @throws NullPointerException if iterator null
   */
   public void advanceIterator() throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("advanceIterator(): cannot advance a null iterator.");
      }
      iterator = iterator.next;
   }
   
   
   /** move itererator one node backwards towards first
   * @precondiition iterator cant be null
   * @postcondition moves iterator backwards one
   * @throws NullPointerException if iterator null
   */
   public void reverseIterator() throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("reverseIterator(): cannot reverse a null iterator.");
      } 
      iterator = iterator.prev;
   }
   
   /** insert a new element after the iterator
   * @param data genereric data to add
   * @precondiition cant be null
   * @postcondition adds new node after iterator
   * @throws NullPointerException
   * edge case if iterator last / first / one node
   */
   public void addIterator(T data) throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("addIterator(): cannot insert after a null iterator.");   
      } else if (iterator == last) {
         addLast(data);
      } else {
         Node newNode = new Node(data);
         newNode.next = iterator.next;
         newNode.prev = iterator;
         iterator.next.prev = newNode;
         iterator.next = newNode;
         length++;
      }
   }
   
   
   /** remove element referenced by iterator
   * @precondiition iterator cant be null
   * @postcondition removes the iterator element from list
   * @throws  NullPointerException
   * edgecase if iterator first
   * edgecase if iterator last
   */
   public void removeIterator() throws NullPointerException {
      if (iterator == null) {
         throw new NullPointerException("removeIterator(): cannot remove a null iterator.");   
      } else if (iterator == first) {
         removeFirst();
      } else if (iterator == last) {
         removeLast();   
      } else {
         iterator.prev.next = iterator.next;
         iterator.next.prev = iterator.prev;
         iterator = null;
         length--;
      }
   }
   
   /** equals
   * @param obj an obj to compare
   * edge cases if not equal
   * @return boolean if equal
   */
   @SuppressWarnings("unchecked")
   @Override public boolean equals(Object obj) {
      //is address same
      if (obj == this) {
         return true;   
      } else if (!(obj instanceof LinkedList)) { //instanceof
         return false;
      } else { // cast linked list to check length
         LinkedList<T> ll = (LinkedList<T>) obj;
         
         if (ll.length != this.length) { //is length equal
            return false;   
         } else {//loop through list check data
            Node temp1 = this.first; //node variable for this list
            Node temp2 = ll.first; //node variable for comparason list
            while (temp1 != null) { // same length check if ends to end loop
               ///null pointer exception if data null
               if (temp1.data == null || temp2.data == null) {
                  if (temp1.data != temp2.data) {
                     return false;  //end loop if not eqaul
                  }
               } else if (!(temp1.data.equals(temp2.data))) {
                  return false; //end loop if not eqaul
               }
               temp1 = temp1.next;
               temp2 = temp2.next; // go to next node in list

            } // endwhile
            return true;
         }  //end if  
      }
   }
   
   /**clear clears as if nothing in list
   *
   */
   public void clear() {
      first = null;
      last = null;
      length = 0;   
      iterator = null;
   }
      
   /** returns each element in ll w numerical position
   * @return string rep num list
   */
   public String numberedListString() {
      if (isEmpty()) {
         return "\n";
      }
      positionIterator();
      String result = "";
      int count = 1;
      while (iterator != null) {
         result += count + ". " + iterator.data + "\n";
         count++;
         advanceIterator();
      }
      return result + "\n";
   }
   
   /** findIndex
   * @return int rep indes
   * @param data rep data
   */
   public int findIndex(T data) {
      if (isEmpty()) {
         return -1;   
      }
      Node temp = first;
      int count = 0;
      while (temp != null) {
         if (temp.data.equals(data)) {
            return count;   
         }
         temp = temp.next;
         count++;
      }
      
      return -1;
      
   }
   
   /** advanceIteratorToIndex
   * @param int index rep ind
   */ 
   public void advanceIteratorToIndex(int index) throws IndexOutOfBoundsException {
      if (index < 0 || index >= length) {
         throw new IndexOutOfBoundsException("");
      }
      positionIterator();
      for (int i = 0; i < index; i++) {
         advanceIterator();
      }
   }
   
}