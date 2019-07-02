package edu.iastate.cs228.hw5;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author Mariya Karasseva mariyak
 *
 */

/**
 * 
 * This class implements a splay tree. Add any helper methods or implementation
 * details you'd like to include.
 *
 */

public class SplayTree<E extends Comparable<? super E>> extends AbstractSet<E> {
	protected Node root;
	protected int size;

	public class Node // made public for grading purpose
	{
		public E data;
		public Node left;
		public Node parent;
		public Node right;

		public Node(E data) {
			this.data = data;
		}

		@Override
		public Node clone() {
			return new Node(data);
		}
	}

	/**
	 * Default constructor constructs an empty tree.
	 */
	public SplayTree() {
		size = 0;
	}

	/**
	 * Needs to call addBST() later on to complete tree construction.
	 */
	public SplayTree(E data) {
		root = new Node(data);
		size = 1;
	}

	/**
	 * Copies over an existing splay tree. The entire tree structure must be
	 * copied. No splaying. Calls cloneTreeRec().
	 * 
	 * @param tree
	 */
	public SplayTree(SplayTree<E> tree) {
		root = cloneTreeRec(tree.root);
		size = tree.size;

	}

	/**
	 * Recursive method called by the constructor above.
	 * 
	 * @param subTree
	 * @return
	 */
	private Node cloneTreeRec(Node subTree) {
		if (subTree == null)
			return null;
		Node newNode = subTree.clone();
		Node newLeft = null;
		Node newRight = null;

		if (subTree.left != null) {
			newLeft = cloneTreeRec(subTree.left);
		}
		if (subTree.right != null) {
			newRight = cloneTreeRec(subTree.right);
		}

		newNode.left = newLeft;
		newNode.right = newRight;
		return newNode;
	}

	/**
	 * This function is here for grading purpose. It is not a good programming
	 * practice.
	 * 
	 * @return element stored at the tree root
	 */
	public E getRoot() {
		return root.data;
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * Clear the splay tree.
	 */
	@Override
	public void clear() {
		root = null;
		size = 0;
	}

	// ----------
	// BST method
	// ----------

	/**
	 * Adds an element to the tree without splaying. The method carries out a
	 * binary search tree addition. It is used for initializing a splay tree.
	 * 
	 * Calls link().
	 * 
	 * @param data
	 * @return true if addition takes place false otherwise (i.e., data is in
	 *         the tree already)
	 */
	public boolean addBST(E data) {
		if (root == null) {
			root = new Node(data);
			++size;
			return true;
		}

		Node current = root;
		while (true) {
			int comp = current.data.compareTo(data);
			if (comp == 0) {
				// data is already in the tree
				((Video) current.data).addNumCopies(((Video) data).getNumCopies());
				return false;
			} else if (comp > 0) {
				if (current.left != null) {
					current = current.left;
				} else {
					link(current, new Node(data));
					++size;
					return true;
				}
			} else {
				if (current.right != null) {
					current = current.right;
				} else {
					link(current, new Node(data));
					++size;
					return true;
				}
			}
		}
	}

	// ------------------
	// Splay tree methods
	// ------------------

	/**
	 * Inserts an element into the splay tree. In case the element was not
	 * contained, this creates a new node and splays the tree at the new node.
	 * If the element exists in the tree already, it splays at the node
	 * containing the element.
	 * 
	 * Calls link().
	 * 
	 * @param data
	 *            element to be inserted
	 * @return true if addition takes place false otherwise (i.e., data is in
	 *         the tree already)
	 */
	@Override
	public boolean add(E data) {
		if (data == null)
			return false;
		if (root == null) {
			root = new Node(data);
			++size;
			return true;
		}
		Node n = findEntry(data);
		if (n.data.compareTo(data) == 0) {
			splay(n);
			return false;
		}
		Node newNode = new Node(data);
		link(n, newNode);
		splay(newNode);
		++size;
		return true;

	}

	/**
	 * Determines whether the tree contains an element. Splays at the node that
	 * stores the element. If the element is not found, splays at the last node
	 * on the search path.
	 * 
	 * @param data
	 *            element to be determined whether to exist in the tree
	 * @return true if the element is contained in the tree false otherwise
	 */
	public boolean contains(E data) {
		if (root == null) {
			return false;
		}
		Node n = findEntry(data);
		if (n == null)
			return false;
		splay(n);
	
		return n.data.compareTo(data) == 0;
	}

	/**
	 * Finds the node that stores the data and splays at it.
	 *
	 * @param data
	 */
	public void splay(E data) {
		contains(data);
	}

	/**
	 * Removes the node that stores an element. Splays at its parent node after
	 * removal (No splay if the removed node was the root.) If the node was not
	 * found, the last node encountered on the search path is splayed to the
	 * root.
	 * 
	 * Calls unlink().
	 * 
	 * @param data
	 *            element to be removed from the tree
	 * @return true if the object is removed false if it was not contained in
	 *         the tree
	 */
	public boolean remove(E data) {
		if (root == null)
			return false;
		if (data == null)
			return false;
		Node n = findEntry(data);
		if (n.data.compareTo(data) == 0) {
			Node parent = n.parent;
			unlink(n);
			splay(parent);
	
			return true;
		}
		splay(n);
		
		return false;
	}

	/**
	 * This method finds an element stored in the splay tree that is equal to
	 * data as decided by the compareTo() method of the class E. This is useful
	 * for retrieving the value of a pair <key, value> stored at some node
	 * knowing the key, via a call with a pair <key, ?> where ? can be any
	 * object of E.
	 * 
	 * Calls findEntry(). Splays at the node containing the element or the last
	 * node on the search path.
	 * 
	 * @param data
	 * @return element such that element.compareTo(data) == 0
	 */
	public E findElement(E data) {
		Node node = findEntry(data);
		if (node == null)
			return null;
		E element = node.data;
		splay(node);
		
		if (element.compareTo(data) == 0)
			return element;
		return null;
	}

	/**
	 * Finds the node that stores an element. It is called by methods such as
	 * contains(), add(), remove(), and findElement().
	 * 
	 * No splay at the found node.
	 *
	 * @param data
	 *            element to be searched for
	 * @return node if found or the last node on the search path otherwise null
	 *         if size == 0.
	 */
	protected Node findEntry(E data) {
		if (size == 0 || root == null)
			return null;
		if (data == null)
			return null;
		Node current = root;
		while (true) {
			int comp = current.data.compareTo(data);
			if (comp == 0) {
				// data is already in the tree
				return current;
			} else if (comp > 0) {
				if (current.left != null) {
					current = current.left;
				} else {
					return current;
				}
			} else {
				if (current.right != null) {
					current = current.right;
				} else {
					return current;
				}
			}
		}
	}

	/**
	 * Join the two subtrees T1 and T2 rooted at root1 and root2 into one. It is
	 * called by remove().
	 * 
	 * Precondition: All elements in T1 are less than those in T2.
	 * 
	 * Access the largest element in T1, and splay at the node to make it the
	 * root of T1. Make T2 the right subtree of T1. The method is called by
	 * remove().
	 * 
	 * @param root1
	 *            root of the subtree T1
	 * @param root2
	 *            root of the subtree T2
	 * @return the root of the joined subtree
	 */
	protected Node join(Node root1, Node root2) {
		Node largest = root1;
		while (largest.right != null)
			largest = largest.right;
		splay(largest);
		largest.right = root2;
		return largest;
	}

	/**
	 * Splay at the current node. This consists of a sequence of zig, zigZig, or
	 * zigZag operations until the current node is moved to the root of the
	 * tree.
	 * 
	 * @param current
	 *            node to splay
	 */
	protected void splay(Node current) {

		if (current == null || current == root || current.parent == null)
			;
		else while (current.parent != null) {
			if (current.parent == root || current.parent.parent == null)
				zig(current);
			else if (current.parent.parent != null) {
				if (current == current.parent.left && current.parent == current.parent.parent.left)
					zigZig(current);
				else if (current == current.parent.right && current.parent == current.parent.parent.right)
					zigZig(current);
				else
					zigZag(current);
			}
		}
		root = current;
	}

	/**
	 * This method performs the zig operation on a node. Calls leftRotate() or
	 * rightRotate().
	 * 
	 * @param current
	 *            node to perform the zig operation on
	 */
	protected void zig(Node current) {
		if (current == null || current == root || current.parent == null)
			;
		else if (current == current.parent.left)
			rightRotate(current);
		else if (current == current.parent.right)
			leftRotate(current);
	}

	/**
	 * This method performs the zig-zig operation on a node. Calls leftRotate()
	 * or rightRotate().
	 * 
	 * @param current
	 *            node to perform the zig-zig operation on
	 */
	protected void zigZig(Node current) {
		if (current == null || current == root || current.parent == null)
			;
		else {
			if (current == current.parent.left) {
				rightRotate(current.parent);
				rightRotate(current);
			} else {
				leftRotate(current.parent);
				leftRotate(current);
			}
		}
	}

	/**
	 * This method performs the zig-zag operation on a node. Calls leftRotate()
	 * and rightRotate().
	 * 
	 * @param current
	 *            node to perform the zig-zag operation on
	 */
	protected void zigZag(Node current) {
		if (current == null || current == root || current.parent == null)
			;
		else {
			if (current == current.parent.right) {
				leftRotate(current);
				rightRotate(current);
			} else {
				rightRotate(current);
				leftRotate(current);
			}
		}
	}

	/**
	 * Carries out a left rotation at a node such that after the rotation its
	 * former parent becomes its left child.
	 * 
	 * Calls link().
	 * 
	 * @param current
	 */
	private void leftRotate(Node current) {
		Node x = current.parent;
		if (x.parent != null)
			link(x.parent, current);
		else current.parent=null;
		if (current.left != null)
			link(x, current.left);
		else
			x.right = null;
		link(current, x);
	}

	/**
	 * Carries out a right rotation at a node such that after the rotation its
	 * former parent becomes its right child.
	 * 
	 * Calls link().
	 * 
	 * @param current
	 */
	private void rightRotate(Node current) {
		Node x = current.parent;
		if (x.parent != null)
			link(x.parent, current);
		else current.parent=null;
		if (current.right != null)
			link(x, current.right);
		else
			x.left = null;
		link(current, x);
	}

	/**
	 * Establish the parent-child relationship between two nodes.
	 * 
	 * Called by addBST(), add(), leftRotate(), and rightRotate().
	 * 
	 * @param parent
	 * @param child
	 */
	private void link(Node parent, Node child) {
		if (parent == null || child == null)
			;
		else if (parent.data.compareTo(child.data) > 0)
			parent.left = child;
		else if (parent.data.compareTo(child.data) < 0)
			parent.right = child;
		child.parent = parent;
	}

	/**
	 * Removes a node n by replacing the subtree rooted at n with the join of
	 * the node's two subtrees.
	 * 
	 * Called by remove().
	 * 
	 * @param n
	 */
	private void unlink(Node n) {
		if (n == null)
			;
		else {
			Node parent = n.parent;
			if (n.left != null && n.right != null)
				if (parent == null)
					root = join(n.left, n.right);
				else
					link(parent, join(n.left, n.right));
			else if (n.left == null)
				if (parent != null)
					link(parent, n.right);
				else
					root = n.right;
			else if (parent != null)
				link(parent, n.left);
			else
				root = n.left;
		}
		--size;
	}

	/**
	 * Perform BST removal of a node.
	 * 
	 * Called by the iterator method remove().
	 * 
	 * @param n
	 */
	private void unlinkBST(Node n) {
		// first deal with the two-child case; copy
		// data from successor up to n, and then delete successor
		// node instead of given node n
		if (n.left != null && n.right != null) {
			Node s = successor(n);
			n.data = s.data;
			n = s; // causes s to be deleted in code below
		}

		// n has at most one child, not necessarily the right one.
		Node replacement = null;
		if (n.left != null) {
			replacement = n.left;
		} else if (n.right != null) {
			replacement = n.right;
		}

		// link replacement into tree in place of node n
		// (replacement may be null)
		if (n.parent == null) {
			root = replacement;
		} else {
			if (n == n.parent.left) {
				n.parent.left = replacement;
			} else {
				n.parent.right = replacement;
			}
		}

		if (replacement != null) {
			replacement.parent = n.parent;
		}

		--size;
	}

	/**
	 * Called by unlink() and the iterator method next().
	 * 
	 * @param n
	 * @return successor of n
	 */
	private Node successor(Node n) {
		if (n == null) {
			return null;
		} else if (n.right != null) {
			// leftmost entry in right subtree
			Node current = n.right;
			while (current.left != null) {
				current = current.left;
			}
			return current;
		} else {
			// we need to go up the tree to the closest ancestor that is
			// a left child; its parent must be the successor
			Node current = n.parent;
			Node child = n;
			while (current != null && current.right == child) {
				child = current;
				current = current.parent;
			}
			// either current is null, or child is left child of current
			return current;
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new SplayTreeIterator();
	}

	/**
	 * Write the splay tree according to the format specified in Section 2.2 of
	 * the project description.
	 * 
	 * Calls toStringRec().
	 *
	 */
	@Override
	public String toString() {
		return toStringRec(root, 0);
	}

	private String toStringRec(Node n, int depth) {
		String s = "";
		for (int i = 0; i < depth; ++i) {
			s += "    ";
		}

		if (n == null) {
			s += "null\n";
			return s;
		}

		s += n.data.toString() + "\n";
		s += toStringRec(n.left, depth + 1);
		s += toStringRec(n.right, depth + 1);

		return s;
	}

	/**
	 *
	 * Iterator implementation for this splay tree. The elements are returned in
	 * ascending order according to their natural ordering. The methods
	 * hasNext() and next() are exactly the same as those for a binary search
	 * tree --- no splaying at any node as the cursor moves. The method remove()
	 * behaves like the class method remove(E data) --- after the node storing
	 * data is found.
	 * 
	 */
	private class SplayTreeIterator implements Iterator<E> {
		Node cursor;
		Node pending;

		public SplayTreeIterator() {
			// start out at smallest value
			cursor = root;
			if (cursor != null) {
				while (cursor.left != null) {
					cursor = cursor.left;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return cursor != null;
		}

		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			pending = cursor;
			cursor = successor(cursor);
			return pending.data;
		}

		/**
		 * This method will join the left and right subtrees of the node being
		 * removed. It behaves like the class method remove(E data) after the
		 * node storing data is found.
		 * 
		 * Calls unlinkBST().
		 * 
		 */
		@Override
		public void remove() {
			if (pending == null)
				throw new IllegalStateException();

			// Remember, current points to the successor of
			// pending, but if pending has two children, then
			// unlinkNode(pending) will copy the successor's data
			// into pending and delete the successor node.
			// So in this case we want to end up with current
			// pointing to the pending node.
			if (pending.left != null && pending.right != null) {
				cursor = pending;
			}
			unlinkBST(pending);
			pending = null;
		}
	}
}
