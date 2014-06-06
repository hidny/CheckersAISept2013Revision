package checkersAI;

//FOR TESTING
import java.util.ArrayList;
//END FOR TESTING

import pastPositionRecord.AnalysedPosition;


//pre: all nodes in the tree must have the same array length.
// Also, I declare that heights start at 0!
public class BalancedSearchTree {
	
	//TODO: make this a general object:
	private AnalysedPosition pos = null;
	
	//TODO: remove this. the value array should be encapsulate in private AnalysedPosition pos (or whatever general object applies)
	private int value[];
	
	private BalancedSearchTree leftSubTree;
	private BalancedSearchTree rightSubTree;
	
	//-1 if left is 1 level bigger, 0 if balance and 1 if 1 RST is 1 level bigger;
	private int heightOfLST = 0;
	private int heightOfRST = 0;
	
	//TESTING:
	public static void main(String args[]) {
		int test1[] = {1, 2, 3};
		int test2[] = {1, 2, 2};
		int test3[] = {1, 3, 2};
		AnalysedPosition test1p = new AnalysedPosition(0, false, 0.0, test1);
		AnalysedPosition test2p = new AnalysedPosition(0, false, 0.0, test2);
		AnalysedPosition test3p = new AnalysedPosition(0, false, 0.0, test3);
		
		BalancedSearchTree tree1 = new BalancedSearchTree(test1p);
		
		if(compare(tree1, test1p.getKey()) == 0 ) {
			
		} else {
			System.out.println("ERROR: test1 failed.");
		}
		if(compare(tree1, test2) > 0 ) {
			
		} else {
			System.out.println("ERROR: test2 failed.");
		}
		
		if(compare(tree1, test3) < 0 ) {
			
		} else {
			System.out.println("ERROR: test3 failed.");
		}
		
		addValueToTree(tree1, test2p);
		addValueToTree(tree1, test3p);
		
		System.out.println("Simple tree:");
		System.out.println(tree1);
		
		if(tree1.heightOfLST != 1) {
			System.out.println("ERROR: LST height is wrong: expected 1 and got: " + tree1.heightOfLST );
		}
		
		if(tree1.heightOfRST != 1) {
			System.out.println("ERROR: RST height is wrong: expected 1 and got: " + tree1.heightOfRST );
		}
		
		System.out.println("TEST2:");
		int input1[] = {1, 2, 3};
		int input2[] = {1, 2, 6};
		int input3[] = {1, 2, 9};
		int input4[] = {1, 2, 1};
		int input5[] = {1, 2, 4};
		int input6[] = {1, 2, 8};
		int input7[] = {1, 2, 2};
		int input8[] = {1, 2, 5};
		int input9[] = {1, 2, 7};
		AnalysedPosition input1p = new AnalysedPosition(0, false, 0.0, input1);
		AnalysedPosition input2p = new AnalysedPosition(0, false, 0.0, input2);
		AnalysedPosition input3p = new AnalysedPosition(0, false, 0.0, input3);
		AnalysedPosition input4p = new AnalysedPosition(0, false, 0.0, input4);
		AnalysedPosition input5p = new AnalysedPosition(0, false, 0.0, input5);
		AnalysedPosition input6p = new AnalysedPosition(0, false, 0.0, input6);
		AnalysedPosition input7p = new AnalysedPosition(0, false, 0.0, input7);
		AnalysedPosition input8p = new AnalysedPosition(0, false, 0.0, input8);
		AnalysedPosition input9p = new AnalysedPosition(0, false, 0.0, input9);
		
		tree1 = new BalancedSearchTree(input1p);
		//System.out.println("Here is the tree created after input1:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input2p);
		//System.out.println("Here is the tree created after input2:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input3p);
		//System.out.println("Here is the tree created after input3:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input4p);
		//System.out.println("Here is the tree created after input4:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input5p);
		//System.out.println("Here is the tree created after input5:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input6p);
		//System.out.println("Here is the tree created after input6:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input7p);
		//System.out.println("Here is the tree created after input7:");
		//System.out.println(tree1); 
		tree1 = addValueToTree(tree1, input8p);
		//System.out.println("Here is the tree created after input8:");
		//System.out.println(tree1); //GOOD!
		
		tree1 = addValueToTree(tree1, input9p);
		
		System.out.println("Height: " + tree1.getHeightOfTree() + " (expected 4)");
		
		System.out.println("Here is the tree created:");
		System.out.println(tree1);
		
	//ANOTHER TEST:	
		
		
		System.out.println("TEST3:");
		input1[2] = 1;
		input2[2] = 2;
		input3[2] = 3;
		input4[2] = 4;
		input5[2] = 5;
		input6[2] = 6;
		input7[2] = 7;
		input8[2] = 8;
		input9[2] = 9;
		
		tree1 = new BalancedSearchTree(input1p);
		//System.out.println("Here is the tree created after input1:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input2p);
		//System.out.println("Here is the tree created after input2:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input3p);
		//System.out.println("Here is the tree created after input3:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input4p);
		//System.out.println("Here is the tree created after input4:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input5p);
		//System.out.println("Here is the tree created after input5:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input6p);
		//System.out.println("Here is the tree created after input6:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input7p);
		//System.out.println("Here is the tree created after input7:");
		//System.out.println(tree1); 
		tree1 = addValueToTree(tree1, input8p);
		//System.out.println("Here is the tree created after input8:");
		//System.out.println(tree1); //GOOD!
		
		tree1 = addValueToTree(tree1, input9p);
		
		System.out.println("Height: " + tree1.getHeightOfTree() + " (expected 4)");
		
		System.out.println("Here is the tree created:");
		System.out.println(tree1);
		
		
//yet another TEST:	
		
		System.out.println("TEST4:");
		input1[2] = 1;
		input2[2] = 3;
		input3[2] = 2;
		
		tree1 = new BalancedSearchTree(input1p);
		System.out.println("Here is the tree created after input1:");
		System.out.println(tree1);
		tree1 = addValueToTree(tree1, input2p);
		System.out.println("Here is the tree created after input2:");
		System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input3p);
		//System.out.println("Here is the tree created after input3:");
		//System.out.println(tree1);
		
		
		System.out.println("Height: " + tree1.getHeightOfTree() + " (expected 2)");
		
		System.out.println("Here is the tree created:");
		System.out.println(tree1);
		
		
		System.out.println("TEST5:");
		input1[2] = 3;
		input2[2] = 1;
		input3[2] = 2;
		
		tree1 = new BalancedSearchTree(input1p);
		//System.out.println("Here is the tree created after input1:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input2p);
		//System.out.println("Here is the tree created after input2:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input3p);
		//System.out.println("Here is the tree created after input3:");
		//System.out.println(tree1);
		
		
		System.out.println("Height: " + tree1.getHeightOfTree() + " (expected 2)");
		
		System.out.println("Here is the tree created:");
		System.out.println(tree1);
		
		
		System.out.println("TEST6:");
		input1[2] = 3;
		input2[2] = 6;
		input3[2] = 9;
		input4[2] = 2;
		input5[2] = 5;
		input6[2] = 8;
		input7[2] = 1;
		input8[2] = 4;
		input9[2] = 7;
		
		tree1 = new BalancedSearchTree(input1p);
		//System.out.println("Here is the tree created after input1:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input2p);
		//System.out.println("Here is the tree created after input2:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input3p);
		//System.out.println("Here is the tree created after input3:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input4p);
		//System.out.println("Here is the tree created after input4:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input5p);
		//System.out.println("Here is the tree created after input5:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input6p);
		//System.out.println("Here is the tree created after input6:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input7p);
		//System.out.println("Here is the tree created after input7:");
		//System.out.println(tree1); 
		tree1 = addValueToTree(tree1, input8p);
		//System.out.println("Here is the tree created after input8:");
		//System.out.println(tree1); //GOOD!
		
		tree1 = addValueToTree(tree1, input9p);
		
		System.out.println("Height: " + tree1.getHeightOfTree() + " (expected 4)");
		
		System.out.println("Here is the tree created:");
		System.out.println(tree1);
		
		
		System.out.println("TEST7:");
		input1[2] = 1;
		input2[2] = 2;
		input3[2] = 5;
		input4[2] = 6;
		input5[2] = 4;
		input6[2] = 3;
		//input7[2] = 1;
		//input8[2] = 4;
		//input9[2] = 7;
		
		tree1 = new BalancedSearchTree(input1p);
		//System.out.println("Here is the tree created after input1:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input2p);
		//System.out.println("Here is the tree created after input2:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input3p);
		//System.out.println("Here is the tree created after input3:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input4p);
		//System.out.println("Here is the tree created after input4:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input5p);
		//System.out.println("Here is the tree created after input5:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input6p);
		//System.out.println("Here is the tree created after input6:");
		//System.out.println(tree1);
		
		System.out.println("Height: " + tree1.getHeightOfTree() + " (expected 3)");
		
		System.out.println("Here is the tree created:");
		System.out.println(tree1);
		
		
		System.out.println("TEST8:");
		input1[2] = 9;
		input2[2] = 8;
		input3[2] = 5;
		input4[2] = 4;
		input5[2] = 6;
		input6[2] = 7;
		//input7[2] = 1;
		//input8[2] = 4;
		//input9[2] = 7;
		
		tree1 = new BalancedSearchTree(input1p);
		//System.out.println("Here is the tree created after input1:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input2p);
		//System.out.println("Here is the tree created after input2:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input3p);
		//System.out.println("Here is the tree created after input3:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input4p);
		//System.out.println("Here is the tree created after input4:");
		//System.out.println(tree1);
		
		tree1 = addValueToTree(tree1, input5p);
		//System.out.println("Here is the tree created after input5:");
		//System.out.println(tree1);
		tree1 = addValueToTree(tree1, input6p);
		//System.out.println("Here is the tree created after input6:");
		//System.out.println(tree1);
		
		System.out.println("Height: " + tree1.getHeightOfTree() + " (expected 3)");
		
		System.out.println("Here is the tree created:");
		System.out.println(tree1);
	}
	
	//pre: pos already has a key set.
	public BalancedSearchTree(AnalysedPosition pos) {
		this.pos = pos;
		
		//TODO: MAYBE I DON'T NEED HARD COPY!!
		//make a hard copy just in case...
		this.value = new int[pos.getKey().length];
		for(int i=0; i<value.length; i++) {
			this.value[i] = pos.getKey()[i];
		}
		//
		
		this.heightOfLST = 0;
		this.heightOfRST = 0;
		
	}
	
	//pre: the heights of the subtrees have already been updated.
	private void updateHeightOfLST() {
		heightOfLST = this.leftSubTree.getHeightOfTree();
	}
	
	//pre: the heights of the subtree have already been updated.
	private void updateHeightOfRST() {
		heightOfRST = this.rightSubTree.getHeightOfTree();
	}
	
	//post: returns the height of the tree.
	public int getHeightOfTree() {
		return Math.max(heightOfLST, heightOfRST) + 1;
	}
	
	private int getHeightOfLST() {
		return heightOfLST;
	}
	
	private int getHeightofRST() {
		return heightOfRST;
	}
	
	//pre: the subtrees are rebalanced and the tree is unbalanced.
	//post: rebalances the tree and returns the node the replaces the node that was underneath the parent.
	private static BalancedSearchTree rebalanceNode(BalancedSearchTree parent, BalancedSearchTree node) {
		BalancedSearchTree replacementNode = null;
		
		if(node.heightOfLST > node.heightOfRST + 1) {
			if(node.leftSubTree.heightOfLST >= node.leftSubTree.heightOfRST) {
				replacementNode = node.leftSubTree;
				doRightRotation(parent, node);
			} else {
				replacementNode = node.leftSubTree.rightSubTree;
				//Double right rotation: (left rotate the LST then right rotation.)
				doLeftRotation(node, node.leftSubTree);
				doRightRotation(parent, node);
			}
		} else if(node.heightOfLST + 1 < node.heightOfRST){
			
			if(node.rightSubTree.heightOfRST >= node.rightSubTree.heightOfLST) {
				replacementNode = node.rightSubTree;
				//simple left rotation
				doLeftRotation(parent, node);
			} else {
				replacementNode = node.rightSubTree.leftSubTree;
				//Double left rotation: (right rotate the RST then left rotation.)
				doRightRotation(node, node.rightSubTree);
				doLeftRotation(parent, node);
			}
		} else {
			System.out.println("ERROR: can't rebalance already balanced node! (BalancedSearchTree)");
			System.exit(1);
		}
		
		return replacementNode;
	}
	
	//pre: parent is the direct parent of the node
	private static void doLeftRotation(BalancedSearchTree parent, BalancedSearchTree node) {
		if(parent != null) {
			if(parent.leftSubTree == node) {
				parent.leftSubTree = node.rightSubTree;
			} else if(parent.rightSubTree == node) {
				parent.rightSubTree = node.rightSubTree;
			} else {
				System.out.println("ERROR: (BALANCEDSEARCHTREE) The parent is not the parent in left rotation");
				System.exit(1);
			}
		}
		//Adjust heights:
		node.heightOfRST = node.rightSubTree.heightOfLST;
		node.rightSubTree.heightOfLST = node.getHeightOfTree();
		//End adjust heights
		
		//Adjust pointers:
		BalancedSearchTree swap = node.rightSubTree;
		node.rightSubTree = node.rightSubTree.leftSubTree;
		swap.leftSubTree = node;
		//End Adjust pointers
	}
	
	//pre: parent is the direct parent of the node
	private static void doRightRotation(BalancedSearchTree parent, BalancedSearchTree node) {
		if(parent != null) {
			if(parent.leftSubTree == node) {
				parent.leftSubTree = node.leftSubTree;
			} else if(parent.rightSubTree == node) {
				parent.rightSubTree = node.leftSubTree;
			} else {
				System.out.println("ERROR: (BALANCEDSEARCHTREE) The parent is not the parent in right rotation");
				System.exit(1);
			}
		}
		//Adjust heights:
		node.heightOfLST = node.leftSubTree.heightOfRST;
		node.leftSubTree.heightOfRST = node.getHeightOfTree();
		
		//Adjust pointers:
		BalancedSearchTree swap = node.leftSubTree;
		node.leftSubTree = node.leftSubTree.rightSubTree;
		swap.rightSubTree = node;		
	}
	
	
	//Prints the balance tree for testing:
	public String toString() {
		String ret = "";
		for(int i=0; i<value.length; i++) {
			ret += value[i] + " ";
		}
		ret += " (" + this.heightOfLST + ") (" + this.heightOfRST + ")";
		
		if(leftSubTree == null && rightSubTree == null) {
			return ret;
		}
		ret += "\n";
		
		if(leftSubTree != null) {
			ret += "(Left subtree:\n" + leftSubTree + "\n";
		} else {
			ret += "(";
		}
		if(rightSubTree != null) {
			ret += "Right subtree:\n" + rightSubTree + ")\n";
		} else {
			ret += ")";
		}
		return ret;
	}

	//TODO: the compare functions should be encapsulated in the AnalysedPosition pos object (or whatever applies)
	//Compares the value of 2 tree nodes.
	//pre: the value array is of the same length.
	//post: return -1 if the value of tree is smaller than tree2,
		//	0 if they are the same
		// 1 otherwise.
	public static int compare(BalancedSearchTree tree, BalancedSearchTree tree2) {
		return compare(tree.value, tree2.value);
	}
	
	public static int compare(BalancedSearchTree tree, int value2[]) {
		return compare(tree.value, value2);
	}
	public static int compare(int value1[], int value2[]) {
		for(int i=0; i<value1.length; i++) {
			if(value1[i] < value2[i]) {
				return -1;
			} else if(value1[i] == value2[i]) {
				continue;
			} else {
				return +1;
			}
		}
		return 0;
	}
	
	//pre: the array is the same length as all the nodes in the tree and the value is not already in the tree.
	//post: adds value to tree using recursion.
	//		balances the tree and
	//		returns the node the replaces the node that was underneath the parent.
	//IMPORTANT: you must get the tree returned in order to avoid bad results.
	public static BalancedSearchTree addValueToTree(BalancedSearchTree root, AnalysedPosition pos) {
		return addValueToTree(null, root, pos);
	}
	
	private static BalancedSearchTree addValueToTree(BalancedSearchTree parent, BalancedSearchTree node, AnalysedPosition pos) {
		int value[] = pos.getKey();
		if(node == null) {
			return new BalancedSearchTree(pos);
		}
		
		int compare = compare(node, value);
		
		if(compare > 0) {
			if(node.leftSubTree != null) {
				node.leftSubTree = addValueToTree(node, node.leftSubTree, pos);
			} else {
				node.leftSubTree = new BalancedSearchTree(pos);
			}
			node.updateHeightOfLST();
		} else if(compare < 0) {
			if(node.rightSubTree != null) {
				node.rightSubTree = addValueToTree(node, node.rightSubTree, pos);
			} else {
				node.rightSubTree = new BalancedSearchTree(pos);
			}
			node.updateHeightOfRST();
		} else {
			System.out.println("ERROR (In Balanced Search Tree): an attempt to add a value that was already in the tree was already there.");
			System.exit(1);
		}
		
		if(Math.abs(node.getHeightOfLST() - node.getHeightofRST()) > 1) {
			node = rebalanceNode(parent, node);
		}
		
		return node;
	}
	
	
	//pre: the array must be the same length as the keys in every node.
	//post: returns the node that has the same key as in the param
	//		returns null otherwise.
	public AnalysedPosition search(int key[]) {
		int compare = compare(this, key);
		if(compare == 0) {
			return this.pos;
		} else if(compare < 0) {
			if(this.rightSubTree != null) {
				return this.rightSubTree.search(key);
			}
		} else {
			if(this.leftSubTree != null) {
				return this.leftSubTree.search(key);
			}
		}
		
		//At this point, the key is not in the tree.
		return null;
	}
	
	//FOR TESTING:
	public int sanityCheckGetNumberOfNodes() {
		int ret=1;
		if(this.leftSubTree != null) {
			ret += this.leftSubTree.sanityCheckGetNumberOfNodes();
		}
		
		if(this.rightSubTree != null) {
			ret += this.rightSubTree.sanityCheckGetNumberOfNodes();
		}
		
		return ret;
	}
	
	//REALLY SLOW
	public void sanityCheckSorted() {
		ArrayList<BalancedSearchTree> list = this.getList();
		
		for(int i=0; i<list.size() - 1; i++) {
			if(compare(list.get(i), list.get(i+1)) > 0) {
				System.err.println("ERROR: the tree is not sorted!");
				System.exit(1);
			}
		}
	}
	
	private ArrayList<BalancedSearchTree> getList() {
		ArrayList<BalancedSearchTree> ret = new ArrayList<BalancedSearchTree>();
		if(this.leftSubTree != null) {
			ret.addAll(this.leftSubTree.getList());
		}
		ret.add(this);
		if(this.rightSubTree != null) {
			ret.addAll(this.rightSubTree.getList());
		}
		return ret;
	}
	//END FOR SANITY
}
