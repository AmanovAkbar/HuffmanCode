
public class HuffmanNode {
	byte symbol;
	int weight;
	HuffmanNode parent;
	HuffmanNode leftChild;
	HuffmanNode rightChild;
	public final static byte NAN = Byte.MAX_VALUE;
	boolean isS;
	public final static byte PSEUDO_EOF = 0;
	HuffmanNode(byte s, int w, boolean b){
		symbol = s;
		weight = w;
		isS = b;
		
	}
	
	
}
