/**
 * ArrayList.java
 * @author Felipa Mendez
 * @author no partner
 * CIS 22C
 */

public class ArrayList<E> {
	private E[] array;
	private int numElements;
	
   /** default constuctor
   *
   */
   @SuppressWarnings("unchecked")
   public ArrayList() {
      final int INITIAL_CAP = 10;
      array = (E[]) new Object[INITIAL_CAP];
      
      numElements = 0;
   }
   
   /** one ar const
   *@param int size rep size
   */
   @SuppressWarnings("unchecked")
   public ArrayList(int size) {
      final int INITIAL_CAP = size;
      array = (E[]) new Object[INITIAL_CAP];
      numElements = 0;
   }
   
   
   /** size
   * @return int rep size
   */
   public int size() {
      return numElements;   
   }
   
   /** atCapacity
   * @return boolean rep capac
   */
   public boolean atCapacity() {
      return numElements == array.length;
   }
   
   /** toString
   * @return string rep arr
   */
   @Override public String toString() {
      String result = "[";
      for (int i = 0; i < numElements - 1; i++) {
         result += array[i] + ", ";
      }
      if (numElements != 0) {
         result += array[numElements - 1];
      }
      return result + "]";
   }
   
   /** isEmpty
   * @return boolean rep is emp
   */
   public boolean isEmpty() {
      if (numElements == 0) {
         return true;
      } else {
         return false;
      }
   }
	
	/** add
	* @param E element rep elem to add
	*/
	@SuppressWarnings("unchecked")
	public void add (E element) {
	   if (numElements == array.length) {
	      E[] newArray = (E[]) new Object[array.length * 2];
	      for (int i = 0; i < array.length; i++) {
	         newArray[i] = array[i];
	      }
	      array = newArray;
	  }
	   
	   array[numElements] = element;
	   numElements++;
	}
	
	/** get
	* @param index rep index arr
	*@return element at index
	*/
	public E get(int index) throws IndexOutOfBoundsException {
	   if (index < 0 || index >= numElements) {
	      throw new IndexOutOfBoundsException(
	         "Error: Cannot get element at index " + index + "." 
	         + "\nOutside bounds of the ArrayList.\nIndex: " + index + ", Size: " + numElements + "\n"
	         );
	   } else {
	      return array[index];
      }
	 }
	
	/** add 2 arg
	* @param index int rep index
	* @param element generic rep element to insert
	*/
	@SuppressWarnings("unchecked")
	public void add(int index, E element) throws IndexOutOfBoundsException {
	   if (index < 0 || index > numElements) {
	      throw new IndexOutOfBoundsException(
	         "Error: Cannot add at index " + index + 
	         ".\nIndex is outside the bounds of the ArrayList.\nIndex: " + index 
	         + ", Size: " + numElements +"\n"
	         );
	   }
	   
	   if (array.length == numElements) {
	      E[] newArray = (E[]) new Object[array.length * 2];
	      
	      for (int i = 0; i < numElements; i++) {
	         newArray[i] = array[i];
	      }
	      array = newArray;
	      
	   }  
	   
      for (int i = numElements; i > index; i--) {
         array[i] = array[i - 1];
      }
      array[index] = element; 
      numElements++;
	   
	}
	
	/** resize
	*
	*/
	@SuppressWarnings("unchecked")
	public void reSize() {
	   E[] newArray = (E[]) new Object[array.length * 2];
	   
	   for (int i = 0; i < numElements; i++) {
	      newArray[i] = array[i];   
	   }
	   
	   array = newArray;
	}
	
	/** copy constructor
	* @param original generic arraylist to copy
	*/
	@SuppressWarnings("unchecked")
	public ArrayList(ArrayList<E> original) {
	   if (original == null || original.numElements == 0) {
	      final int INITIAL_CAP = 10;
         array = (E[]) new Object[INITIAL_CAP];
	      numElements = 0;
	  } else {
	      array = (E[]) new Object[original.array.length];
	      for(int i = 0; i < original.numElements; i++) {
	         array[i] = original.array[i];
	         
	      }
	      numElements = original.numElements;
	  }
	}
	
	/** set
	* @param index rep ind of arr
	* @param element rep eleemnt to insert
	*/
	public void set(int index, E element) throws IndexOutOfBoundsException {
	   if (index >= numElements || index < 0) {
         throw new IndexOutOfBoundsException(
            "Error: Cannot set at index outside bounds" 
            + " of ArrayList.\nIndex: "  + index + " , Size: " + numElements + "\n");
      }   
      array[index] = element;
	}
	
	
	/**contains
	* @param element rep key to look for
	* @return boolean rep is contained
	*/
	public boolean contains(E element) {
	   for (int i = 0; i < numElements; i++) {
	      if (element == array[i]) {
	         return true;   
	      }
      }
      return false;
	}
	
	/** indexOF
	* @param element rep search key
	* @return int represent index if found
	*/
	public int indexOf(E element) {
	   for (int i = 0; i < numElements; i++) {
	      if (element == array[i]) {
	         return i;   
	      }
	   }   
	   return -1;
	}
	
	/** remove
	* @param index rep index to remove
	*@return int rep index removed
	*/
	public E remove(int index) throws IndexOutOfBoundsException {
	   if (index >= numElements || index < 0) {
	      throw new IndexOutOfBoundsException(
	         "Error: Cannot remove element at index " 
	         + index + ".\nOutside bounds of the ArrayList.\nIndex: " + index + ", Size: " + numElements + "\n" 
         );
	   }   
	   E element = array[index];
	   for (int i = index; i < numElements; i++) {
	      array[i] = array[i + 1]; //index to remove equals element above it
      }
      numElements--;
      return element;
	}
	
	/**remove
	* @param boolean rep element removed
	*@return boolrsn reo remove
	*/
	public boolean remove(E element) {
	   for (int i = 0; i < numElements; i++) {
	      if (array[i].equals(element)) {
	         for (int j = i; j < numElements - 1; j++) {
	            array[j] = array[j + 1];   
	         }   
	         array[numElements - 1] = null;
	         numElements--;
	         return true;
	      }   
	   }   
	   return false;
	}
	
	/**equals
	*@param object obj rep obj to compare
	*@return boolean rep equal
	*/
	@SuppressWarnings("unchecked")
	@Override public boolean equals(Object obj) {
	   if (this == obj) {
	      return true;   
	   } else if (!(obj instanceof ArrayList)) {
	      return false;   
	   }
	   
	   ArrayList<E> la = (ArrayList<E>) obj;
	   if (this.numElements != la.numElements) {
	      return false;   
	   } else {
	      for (int i = 0; i < numElements; i++) {
	         if (!(this.array[i].equals(la.array[i]))) {
	            return false;   
	         }
	      } 
	      return true;
	   }
	}
	
	
	
}