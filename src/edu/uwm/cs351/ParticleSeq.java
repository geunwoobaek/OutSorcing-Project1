// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

import java.awt.Color;

import edu.uwm.cs.junit.LockedTestCase;

/******************************************************************************
 * This class is a homework assignment;
 * A ParticleSeq is a collection of Particles.
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods of the sequence class 
 * (start, getCurrent, advance and isCurrent).
 * The semantics of the current element are slightly changed from the textbook.
 * This is especially the case with removeCurrent.  Please read the assignment sheet.
 *
 * @note
 *   (1) The capacity of one a sequence can change after it's created, but
 *   the maximum capacity is limited by the amount of free memory on the 
 *   machine. The constructor, addAfter, addBefore, clone, 
 *   and concatenation will result in an
 *   OutOfMemoryError when free memory is exhausted.
 *   <p>
 *   (2) A sequence's capacity cannot exceed the maximum integer 2,147,483,647
 *   (Integer.MAX_VALUE). Any attempt to create a larger capacity
 *   results in a failure due to an arithmetic overflow. 
 *   
 *   NB: Neither of these conditions require any work for the implementors (students).
 *
 * @see
 *   <A HREF="../../../../edu/colorado/collections/DoubleArraySeq.java">
 *   Java Source Code for the original class by Michael Main
 *   </A>
 *
 ******************************************************************************/
public class ParticleSeq implements Cloneable
{
	// Implementation of the ParticleSeq class:
	//   1. The number of elements in the sequences is in the instance variable 
	//      manyItems.  The elements may be Particle objects or nulls.
	//   2. For any sequence, the elements of the
	//      sequence are stored in data[0] through data[manyItems-1], and we
	//      don't care what's in the rest of data.
	//   3. We remember whether we're currently looking at an element, or whether
	//      it has been removed.  We use a boolean to remember this.
	//   3. If there is a current element, then it lies in data[currentIndex];
	//      if there is no current element, then currentIndex can be any index
	//      in the array or the same as _manyItems

	private Particle[ ] _data;
	private int _manyItems;
	private boolean _isCurrent;
	private int _currentIndex; 

	private static int INITIAL_CAPACITY = 1;

	private static boolean _doReport = true; // changed only by invariant tester
	
	private boolean _report(String error) {
		if (_doReport) System.out.println("Invariant error: " + error);
		else System.out.println("Caught problem: " + error);
		return false;
	}

	private boolean _wellFormed() {
		// Check the invariant.
		// 1. data is never null
		if (_data == null) return _report("data is null"); // test the NEGATION of the condition

		// 2. The data array is at least as long as the number of items
		//    claimed by the sequence.
		// TODO
		if (_data.length<_manyItems) return _report("The data array is at least as long as the number of items");

		// 3. currentIndex is never negative and never more than the number of
		//    items claimed by the sequence.
		// TODO
		if(_currentIndex < 0 || _currentIndex > _manyItems) 
			return _report("currentIndex is never negative and never more than the number of items claimed by the sequence.");

		// 4. if isCurrent is true, then current index must be a valid index
		//    (it cannot be equal to the number of items)
		
		// If no problems discovered, return true
		return true;
	}

	// This is only for testing the invariant.  Do not change!
	private ParticleSeq(boolean testInvariant) { }
	
	/**
	 * Initialize an empty sequence with an initial capacity of INITIAL_CAPACITY.
	 * The addAfter and addBefore methods work
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param - none
	 * @postcondition
	 *   This sequence is empty and has an initial capacity of INITIAL_CAPACITY
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for initial array.
	 **/   
	public ParticleSeq( )
	{
		// NB: NEVER assert the invariant at the START of the constructor.
		// (Why not?  Think about it.)
		// TODO: Implement this code.
		
		
		_data = new Particle[INITIAL_CAPACITY];
		//this(INITIAL_CAPACITY);
		assert _wellFormed() : "Invariant false at end of constructor";
	}


	/**
	 * Initialize an empty sequence with a specified initial capacity. Note that
	 * the addAfter and addBefore methods work
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param initialCapacity
	 *   the initial capacity of this sequence
	 * @precondition
	 *   initialCapacity is non-negative.
	 * @postcondition
	 *   This sequence is empty and has the given initial capacity.
	 * @exception IllegalArgumentException
	 *   Indicates that initialCapacity is negative.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Particle[initialCapacity].
	 **/   
	public ParticleSeq(int initialCapacity)
	{
		// TODO: Implement this code.
		if(initialCapacity < 0) throw new IllegalArgumentException();	
		
		_data = new Particle[initialCapacity];
		_manyItems = _currentIndex = 0;
		assert _wellFormed() : "Invariant false at end of constructor";
	}

	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size( )
	{
		assert _wellFormed() : "invariant failed at start of size";
		// TODO: Implement this code.
		return _manyItems;
		
		// size() should not modify anything, so we omit testing the invariant at the end
	}

	/**
	 * The first element (if any) of this sequence is now current.
	 * @param - none
	 * @postcondition
	 *   The front element of this sequence (if any) is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert _wellFormed() : "invariant failed at start of start";
		// TODO: Implement this code.
		_currentIndex = 0;
		
		assert _wellFormed() : "invariant failed at end of start";
	}

	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element (a Particle or null) that can be retrieved with the 
	 * getCurrent method. This depends on the status of the cursor.
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean isCurrent( )
	{
		assert _wellFormed() : "invariant failed at start of isCurrent";
		// TODO: Implement this code.
		if(_currentIndex >= _manyItems) return false;
		else return true;
				
	}

	/**
	 * Accessor method to get the current element of this sequence. 
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @return
	 *   the current element of this sequence, possibly null
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public Particle getCurrent( )
	{
		assert _wellFormed() : "invariant failed at start of getCurrent";
		// TODO: Implement this code.
		if(!isCurrent()) throw new IllegalStateException();			
		return _data[_currentIndex];		
		
	}

	/**
	 * Return true if we are at the end of the sequence.
	 * This can happen in several different ways:
	 * <ol>
	 * <li> The sequence is empty.
	 * <li> The last element has just been removed.
	 * <li> We advanced from the last element.
	 * </ol>
	 * The code you write has no need to distinguish these situations.
	 * @precondition true
	 * @return true if at the end of the sequence
	 */
	public boolean atEnd() {
		assert _wellFormed() : "Invariant failed at start of atEnd";
		// TODO: Implement this code
		return (_currentIndex == _manyItems);
		
		// Our solution makes use of no conditionals (if/while/for/&&/||).
	}
	
	/**
	 * Move forward, so that the next element is now the current element in
	 * this sequence.
	 * @param - none
	 * @precondition
	 *   atEnd() returns false.
	 * @postcondition
	 *   If the current element was already the last element of this sequence 
	 *   (with nothing after it), then we will be at the end. 
	 *   Otherwise, the next element is current (next after the current
	 *   element, or what was next after the current element before it was removed).
	 * @exception IllegalStateException
	 *   If at the end.
	 **/
	public void advance( )
	{
		assert _wellFormed() : "invariant failed at start of advance";
		// TODO: Implement this code.
		if(!isCurrent()) throw new IllegalStateException();	
		
		if(!atEnd()) _currentIndex++;
		
		assert _wellFormed() : "invariant failed at end of advance";
	}

	/**
	 * Remove the current element from this sequence.
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this sequence.
	 *   There is no longer any current element.
	 *   If there was no following element, then we are at the end.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent( )
	{
		assert _wellFormed() : "invariant failed at start of removeCurrent";
		// TODO: Implement this code.
		// You will need to shift elements in the array.
		if(!isCurrent()) throw new IllegalStateException();	
		--_manyItems;
		if(isCurrent() && !atEnd()) {
			for(int i = _currentIndex; i<_manyItems; ++i) {
				_data[i] = _data[i+1];
			}
		}
		
		
		assert _wellFormed() : "invariant failed at end of removeCurrent";
	}

	/**
	 * Add a new element to this sequence, before the current element (if any).
	 * If the new element would take this sequence beyond its current capacity,
	 * then the capacity is increased before adding the new element.
	 * @param element
	 *   the new element that is being added, it is allowed to be null
	 * @postcondition
	 *   The element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   where the removed element was, or at the end. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the sequence's capacity.
	 * @note
	 *   An attempt to increase the capacity beyond
	 *   Integer.MAX_VALUE will cause the sequence to fail with an
	 *   arithmetic overflow.
	 **/
	public void addBefore(Particle element)
	{
		assert _wellFormed() : "invariant failed at start of addBefore";
		// TODO: Implement this code.
		++_manyItems;
		if(isCurrent()) {
			_data[_currentIndex] = element;
			for(int i = _currentIndex; i<_manyItems; ++i) {
				_data[i+1] = _data[i];
			}
		}
		assert _wellFormed() : "invariant failed at end of addBefore";
	}

	/**
	 * Add a new element to this sequence, after the current element if any.
	 * If the new element would take this sequence beyond its current capacity,
	 * then the capacity is increased before adding the new element.
	 * @param element
	 *   the new element that is being added, may be null
	 * @postcondition
	 *   The element has been added to this sequence. If there was
	 *   a current element, then the new element is placed after the current
	 *   element. If there was no current element, then the new element is placed
	 *   where the element was, or at the end of the sequence.
	 *   In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the sequence's capacity.
	 * @note
	 *   An attempt to increase the capacity beyond
	 *   Integer.MAX_VALUE will cause the sequence to fail with an
	 *   arithmetic overflow.
	 **/
	public void addAfter(Particle element)
	{
		assert _wellFormed() : "invariant failed at start of addAfter";
		// TODO: Implement this code.
		assert _wellFormed() : "invariant failed at end of addAfter";
	}


	/**
	 * Place the contents of another sequence at the end of this sequence.
	 * @param addend
	 *   a sequence whose contents will be placed at the end of this sequence
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed at the end of 
	 *   this sequence. The current element of this sequence if any,
	 *   remains unchanged.   The addend is unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory to increase the size of this sequence.
	 * @note
	 *   An attempt to increase the capacity beyond
	 *   Integer.MAX_VALUE will cause an arithmetic overflow
	 *   that will cause the sequence to fail.
	 **/
	public void addAll(ParticleSeq addend)
	{
		assert _wellFormed() : "invariant failed at start of addAll";
		// TODO: Implement this code.
		// Recall that you can freely access private fields of the addend.
		assert _wellFormed() : "invariant failed at end of addAll";
	}   


	/**
	 * Change the current capacity of this sequence.
	 * @param minimumCapacity
	 *   the callers desired capacity.  The end result must have this
	 *   or a greater capacity.
	 * @postcondition
	 *   This sequence's capacity has been changed to at least minimumCapacity.
	 *   If the capacity was already at or greater than minimumCapacity,
	 *   then the capacity is left unchanged.
	 *   If the size is changed, it must be at least twice as big as before.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for: new array of minimumCapacity elements.
	 **/
	private void ensureCapacity(int minimumCapacity)
	{
		// TODO: Implement this code.
		// This is a private method: don't check invariants
	}

	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public ParticleSeq clone( )
	{  // Clone a ParticleSeq object.
		assert _wellFormed() : "invariant failed at start of clone";
		ParticleSeq answer;

		try
		{
			answer = (ParticleSeq) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		answer._data = _data.clone( ); // all that's needed for Homework #2

		assert _wellFormed() : "invariant failed at end of clone";
		assert answer._wellFormed() : "invariant failed for clone";
		
		return answer;
	}

	
	public static class TestInvariant extends LockedTestCase {
		private ParticleSeq hs;
		
		@Override
		public void setUp() {
			hs = new ParticleSeq(false);
			_doReport = false;
		}
		
		public void test1() {
			assertFalse(hs._wellFormed());
		}
		
		public void test2() {
			hs._data = new Particle[3];
			hs._manyItems = -1;
			assertFalse(hs._wellFormed());
		}
		
		public void test3() {
			hs._data = new Particle[3];
			hs._manyItems = 4;
			assertFalse(hs._wellFormed());
		}
		
		public void test4() {
			hs._data = new Particle[10];
			hs._manyItems = 4;
			_doReport = true;
			assertTrue(hs._wellFormed());
		}
		
		public void test5() {
			hs._data = new Particle[5];
			hs._manyItems = 4;
			hs._currentIndex = -1;
			assertFalse(hs._wellFormed());
		}
		
		public void test6() {
			hs._data = new Particle[3];
			hs._manyItems = 3;
			hs._currentIndex = 3;
			_doReport = true;
			assertTrue(hs._wellFormed());
		}
		
		public void test7() {
			hs._data = new Particle[5];
			hs._manyItems = 3;
			hs._currentIndex = 4;
			assertFalse(hs._wellFormed());
		}
		
		public void test8() {
			hs._data = new Particle[3];
			hs._manyItems = 2;
			hs._currentIndex = 2;
			_doReport = true;
			assertTrue(hs._wellFormed());
		}
		
		public void test9() {
			hs._data = new Particle[10];
			hs._manyItems = 5;
			hs._currentIndex = 5;
			hs._data[5] = new Particle(new Point(0,0),new Vector(10,20),10,Color.BLUE);
			hs._isCurrent = true;
			assertFalse(hs._wellFormed());
		}
	}
}

