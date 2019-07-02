/**
 * 
 */
package edu.iastate.cs228.hw5;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

/**
 * Used to Test the BST methods of the SplayTree<E> class as well as the other splayed alternatives
 * @author Aaron
 *
 */
public class BSTMethodTests extends SplayingTests {
	
	/**
	 * Builds a simple Splayed Tree for testing purposes
	 * Should look like
	 * 
	 *         0
	 *    -4      4
	 * -7    -2 2   7
	 */
	private void buildSimpleTestingTree()
	{
		addBST(new NumericData(0));
		addBST(new NumericData(4));
		addBST(new NumericData(-4));
		addBST(new NumericData(-7));
		addBST(new NumericData(-2));
		addBST(new NumericData(2));
		addBST(new NumericData(7));
		
		assertTrue("After constructing this tree, the size should be 7", size == 7);
	}
	
	// Testing existence methods
	
	/**
	 * Tests both the functionality of contains, and findElement
	 */
	@Test
	public void containsTest()
	{
		buildSimpleTestingTree();
		
		boolean c_res = contains(new NumericData(2));
		assertTrue("The tree does indeed contain '2'", c_res);
		assertTrue("After accessing the node should be splayed to the root", compareNodeInt(2, root));
		
		assertFalse("The tree does not contain '69420'", contains(new NumericData(69420)));
		assertTrue("After searching the closest node should be splayed to the root", compareNodeInt(7, root));
		
	}
	
	/**
	 * Tests the functionality of findEntry()
	 */
	@Test
	public void findEntryTest()
	{
		buildSimpleTestingTree();
		
		assertTrue("The tree does indeed contain '2'", findEntry(new NumericData(2)) != null);
		assertFalse("No splay after access", compareNodeInt(2, root));
		
		assertTrue("The tree does not contain '69420', closest value is returned", 
				compareNodeInt(7, findEntry(new NumericData(69420))));
		
		assertFalse("No splay after access", compareNodeInt(7, root));
		
		clear();
		assertTrue("If the tree has no elements, then return null", findEntry(new NumericData(2)) == null);
	}
	
	// Testing the add methods
	
	/**
	 * Tests the addBST() method, it should already work, considering that buildSimpleTestingTree should be working
	 * Tests for a Greater Than case where data.compareTo() returns a positive number
	 */
	@Test
	public void addBSTTestGT()
	{
		buildSimpleTestingTree();
		
		assertTrue("Failed to add element", addBST(new NumericData(9)));
		
		assertTrue("After adding an element the size should now be increased by one", size == 8);
		
		assertTrue("Inserted element should be all the way at the rightmost side", 
				compareNodeInt(9, root.right.right.right));
	}
	
	/**
	 * Tests the addBST() method, it should already work, considering that buildSimpleTestingTree should be working
	 * Tests for a Less Than case where data.compareTo() returns a negative number
	 */
	@Test
	public void addBSTTestLT()
	{
		buildSimpleTestingTree();
		
		assertTrue("Failed to add element", addBST(new NumericData(-9)));
		
		assertTrue("After adding an element the size should now be increased by one", size == 8);
		
		assertTrue("Inserted element should be all the way at the leftmost side", 
				compareNodeInt(-9, root.left.left.left));
	}
	
	/**
	 * Tests the addBST() method, it should already work, considering that buildSimpleTestingTree should be working
	 * Tests for a equivalent case where data.compareTo() returns a zero
	 */
	@Test
	public void addBSTTestEQ()
	{
		assertTrue("After clearing the tree the size should now be zero", size == 0);
		
		buildSimpleTestingTree();
		
		assertFalse("Duplicate elements should return false", addBST(new NumericData(7)));
	}
	
	/**
	 * Tests the addBST() method, it should already work, considering that buildSimpleTestingTree should be working
	 * Tests for a Greater Than case where data.compareTo() returns a positive number
	 * 
	 * If you fail this, check the SplayingTests methods to see which one you are failing
	 */
	@Test
	public void addTestGT()
	{
		buildSimpleTestingTree();
		
		assertTrue("Failed to add element", add(new NumericData(9)));
		
		assertTrue("After adding an element the size should now be increased by one", size == 8);
		
		assertTrue("Inserted element should be at the root", 
				compareNodeInt(9, root));
		assertTrue("Root.left should be the original root", compareNodeInt(0, root.left));
	}
	
	/**
	 * Tests the addBST() method, it should already work, considering that buildSimpleTestingTree should be working
	 * Tests for a Less Than case where data.compareTo() returns a negative number
	 * 
	 * If you fail this, check the SplayingTests methods to see which one you are failing
	 */
	@Test
	public void addTestLT()
	{
		buildSimpleTestingTree();
		
		assertTrue("Failed to add element", add(new NumericData(-9)));
		
		assertTrue("After adding an element the size should now be increased by one", size == 8);
		
		assertTrue("Inserted element should be at the root", 
				compareNodeInt(-9, root));
		assertTrue("Root.right should be the original root", compareNodeInt(0, root.right));
	}
	
	/**
	 * Tests the addBST() method, it should already work, considering that buildSimpleTestingTree should be working
	 * Tests for a equivalent case where data.compareTo() returns a zero
	 * 
	 * If you fail this, check the SplayingTests methods to see which one you are failing
	 */
	@Test
	public void addTestEQ()
	{
		buildSimpleTestingTree();
		
		assertFalse("Duplicate elements should return false", add(new NumericData(7)));
	}

	// Testing the remove methods
	
	/**
	 * Tests the functionality of remove() and unlink at the same time.
	 */
	@Test
	public void removeTest()
	{
		buildSimpleTestingTree();
		
		boolean c_res = remove(new NumericData(2));
		assertTrue("Removing '2' should be successful", c_res);
		assertTrue("After removing the node, its parent should be splayed to the root", compareNodeInt(4, root));
		
		assertFalse("The tree does not contain '69420', removal should fail", remove(new NumericData(69420)));
		assertTrue("After searching the closest node should be splayed to the root", compareNodeInt(7, root));
	}
	
	// unlinkBST() is tested in IteratorTests()
	
	// Iterator methods
	
	@Test(timeout=1000)
	public void IteratorTests()
	{
		buildSimpleTestingTree();
		Iterator<NumericData> iter = iterator();
		
		try
		{
			iter.remove();
			assertTrue("Calling remove when pending is null should throw an IllegalStateException", false);
		}
		catch(IllegalStateException e)
		{
			// Test passes.
		}
		
		NumericData prev = null;
		while(iter.hasNext())
		{
			NumericData n = iter.next();
			if(prev != null && prev.data == -2)
				assertTrue("After finding the highest element of a node's subtrees, jump up to its parent", 
						n.data == 0);
			
			else if(prev != null && n.data == 4)
			{
				iter.remove();
				
				assertTrue("Join should take a node's two subtrees and join them together", 
						compareNodeInt(7, root.right));
			}
			
			prev = n;
		}
		
		assertTrue("You didn't make it all the way through the tree", prev.data == 7);
	}
}
