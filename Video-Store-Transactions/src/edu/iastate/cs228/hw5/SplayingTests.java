package edu.iastate.cs228.hw5;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the splaying functionality of the SplayTree<E> class
 * @author Aaron
 *
 */
public class SplayingTests extends SplayTree<NumericData> {
	
	@Before
	public void prepareSplayTree()
	{
		clear();
	}
	
	/**
	 * Sets up the tree for the rotation tests
	 * Should look like:
	 * 
	 *         0
	 * 	  -3       3
	 * -4	-1	 1	 4
	 */
	private void setupRotate()
	{
		addBST(new NumericData(0));
		addBST(new NumericData(3));
		addBST(new NumericData(-3));
		addBST(new NumericData(-4));
		addBST(new NumericData(-1));
		addBST(new NumericData(1));
		addBST(new NumericData(4));
	}
	
	protected boolean compareNodeInt(int value, Node data)
	{
		return data == null || value == data.data.data;
	}
	
	/*
	 * This section tests the functionality of each specific tree splay function
	 */

	// This inherently also tests zig in both directions
	
	@Test
	public void rotateRightTest()
	{
		setupRotate();
		zig(root.left);
		assertTrue("Root should be the rotated number", compareNodeInt(-3, root));
		assertTrue("Root.left, should be the left child of the rotated number", compareNodeInt(-4, root.left));
		assertTrue("Root.right, should be the original root", compareNodeInt(0, root.right));
		assertTrue("Root.right.left, should be the right child of the rotated number", compareNodeInt(-1, root.right.left));
		assertTrue("Root.right.right, should be the right child of the original root", compareNodeInt(3, root.right.right));
	}
	
	@Test
	public void rotateLeftTest()
	{
		setupRotate();
		zig(root.right);
		assertTrue("Root should be the rotated number", compareNodeInt(3, root));
		assertTrue("Root.left, should be the original root", compareNodeInt(0, root.left));
		assertTrue("Root.right, should be the right child of the rotated number", compareNodeInt(4, root.right));
		assertTrue("Root.left.right, should be the left child of the rotated number", compareNodeInt(1, root.left.right));
		assertTrue("Root.left.left, should be the left child of the original root", compareNodeInt(-3, root.left.left));
	}
	
	/**
	 * Tests the zig-zig operation with right rotations
	 * Should produce
	 * 
	 * -4
	 *    -3
	 *       -1
	 *          0
	 *            3
	 *          1   4
	 */
	@Test
	public void zigZigRightTest()
	{
		setupRotate();
		zigZig(root.left.left);
		assertTrue("Root should be the splayed number", compareNodeInt(-4, root));
		assertTrue("You need to rotate the target.parent first before target, don't just rotate target twice", 
				root.right.left == null);
	}
	
	/**
	 * Tests the zig-zig operation with left rotations
	 * Should produce
	 * 
	 * 			   4
	 * 			 3
	 * 		   1
	 * 		 0
	 * 	  -3
	 * -4	-1
	 */
	@Test
	public void zigZigLeftTest()
	{
		setupRotate();
		zigZig(root.right.right);
		assertTrue("Root should be the splayed number", compareNodeInt(4, root));
		assertTrue("You need to rotate the target.parent first before target, don't just rotate target twice", 
				root.left.right == null);
	}
	
	/**
	 * Tests the zig-zag operation with a left rotation and then a right rotation
	 * Should produce
	 * 
	 *       -1
	 *    -3    0
	 * -4         3
	 *          1   4
	 */
	@Test
	public void zigZagLeftRightTest()
	{
		setupRotate();
		zigZag(root.left.right);
		assertTrue("Root should be the splayed number", compareNodeInt(-1, root));
		assertTrue("Root.right should be the original root", compareNodeInt(0, root.right));
		assertTrue("Root.left should be the root of the left subtree of the first rotation", 
				compareNodeInt(-3, root.left));
		assertTrue("Root.left.left should be the rest of the left subtree of the first rotation", 
				compareNodeInt(-4, root.left.left));
	}
	
	/**
	 * Tests the zig-zag operation with a right rotation and then a left rotation
	 * Should produce
	 * 
	 *         1
	 *       0   3
	 *    -3       4
	 * -4    -1
	 */
	@Test
	public void zigZagRightLeftTest()
	{
		setupRotate();
		zigZag(root.right.left);
		assertTrue("Root should be the splayed number", compareNodeInt(1, root));
		assertTrue("Root.left should be the original root", compareNodeInt(0, root.left));
		assertTrue("Root.right should be the root of the left subtree of the first rotation", 
				compareNodeInt(3, root.right));
		assertTrue("Root.right.right should be the rest of the left subtree of the first rotation", 
				compareNodeInt(4, root.right.right));
	}

	/*
	 * This section tests the splaying functionality as a whole
	 */
	
	// zig-zig, zig
	
	/**
	 * Performs a test of splay where the order of operations should be
	 * zig-zig, then zig
	 * Should produce
	 * 
	 *       -5
	 *           0
	 *       -4     3
	 *    -3      1   4
	 * -1
	 */
	@Test
	public void splayZiZiZigTest()
	{
		setupRotate();
		
		// We need an odd number of rotations, so we need to add one more element.
		addBST(new NumericData(-5));
		
		splay(root.left.left.left);
		assertTrue("Root should be the node that was splayed", compareNodeInt(-5, root));
		assertTrue("Root.right should be the original root", compareNodeInt(0, root.right));
		assertTrue("Root.right.left should be the right subtree of the rotated node"
				+" if this is wrong, then you probably zigged before you zig-zigged.",
				compareNodeInt(-4, root.right.left));
		assertTrue("Root.right.left.left should be null, you zigged before you zig-zigged", 
				root.right.left.left == null);
	}
	
	// zig-zag, zig-zig
	
	/**
	 * Tests the splay functionality where the order of operations should be
	 * zig-zag, zig-zig
	 * Should produce
	 * 
	 *      -5
	 * -6       -3
	 *      -4       0
	 *           -1     3
	 *                1   4
	 */
	@Test
	public void splayZiZaZiZi()
	{
		setupRotate();
		
		// We need an odd number of rotations and it needs to be non-linear
		addBST(new NumericData(-6));
		addBST(new NumericData(-5));
		
		splay(root.left.left.left.right);
		assertTrue("Root should be the node that was splayed", compareNodeInt(-5, root));
		assertTrue("Root.right should be the right subtree of the original root", compareNodeInt(-3, root.right));
		assertTrue("Root.left should be the original parent of the node that was splayed", compareNodeInt(-6, root.left));
		assertTrue("Root.right.left should be the grandparent of the rotated node",
				compareNodeInt(-4, root.right.left));
		assertTrue("Root.right.right should be the original root", 
				compareNodeInt(0, root.right.right));
	}
	
	
	// zig-zig, zig-zag
	
	
	/**
	 * Tests the functionality of the splay operation where the order of rotations is
	 * zig-zig, zig-zag
	 * Should produce
	 * 
	 *     -1
	 * -4      0
	 *    -2     3
	 * -3
	 */
	@Test
	public void splayZiZiZiZag()
	{
		// We can't use the original rotateSetup because it doesn't leave the ability to make a zig-zag
		// with a zig-zig beforehand
		
		addBST(new NumericData(0));
		addBST(new NumericData(3));
		
		// Sets up the path
		addBST(new NumericData(-4));
		addBST(new NumericData(-3));
		addBST(new NumericData(-2));
		addBST(new NumericData(-1));
		
		/*
		 * Test tree should look like
		 * 
		 *      0
		 * -4       3
		 *    -3
		 *       -2
		 *          -1
		 */
		
		splay(root.left.right.right.right);
		
		assertTrue("Root should be the splayed node", compareNodeInt(-1, root));
		assertTrue("Root.left should be the original root's left child", compareNodeInt(-4, root.left));
		assertTrue("Root.right should be the original root", compareNodeInt(0, root.right));
	}
	
	// zig-zag, zig
	
	/**
	 * Tests the splay operation's functionality where the order is
	 * zig-zag, zig
	 */
	@Test
	public void splayZiZaZig()
	{
		// We can't use the original rotateSetup because it doesn't leave the ability to make a zig-zag
		// with a zig-zig beforehand
		
		addBST(new NumericData(0));
		addBST(new NumericData(3));
		
		// Sets up the path
		addBST(new NumericData(-4));
		addBST(new NumericData(-6));
		addBST(new NumericData(-5));

		/*
		 * Test tree should look like
		 * 
		 *       0
		 *    -4   3
		 * -6
		 *    -5
		 */
		
		splay(root.left.left.right);
		
		assertTrue("Root should be the splayed node", compareNodeInt(-5, root));
		assertTrue("Root.left should be the splayed node's parent", compareNodeInt(-6, root.left));
		assertTrue("Root.right should be the original root", compareNodeInt(0, root.right));
		assertTrue("Root.right.left should be the splayed node's grandparent", compareNodeInt(-4, root.right.left));
		
	}
	
}
