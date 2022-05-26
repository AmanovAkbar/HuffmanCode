

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;

import java.util.PriorityQueue;

import java.io.*;
import javafx.util.Pair;

public class HuffmanCode {
	String pseudo;
	File file;
	DataInputStream in;
	final static int FILE_START = -3;
	HuffmanCode(File file){
		this.file = file;
		
	}

	public void compress() {
		// TODO Auto-generated method stub
		if(file.length()==0) {
			File nf = new File(file+".hfm");
			 try  { 
				 DataOutputStream out = new DataOutputStream(new FileOutputStream(nf));
				out.writeInt(FILE_START);
				out.writeInt(0);
				 out.close();
			 }catch (IOException x) {
			      System.err.println(x);
			    }
		}else{
		byte [] data = new byte[(int) file.length()];
		
		
		try {
				DataInputStream in = new DataInputStream(new FileInputStream( file));
				in.readFully(data);
				in.close();
			} catch (IOException x) {
			    System.err.println(x);
			}
		
		
		 int weights[] =new int[256];
		 for(byte c: data) {
			 weights[Byte.toUnsignedInt(c)]++;
		 }
		
		Comparator<Pair<Byte, Integer>> weightComparator = new Comparator<Pair<Byte, Integer>>() {

			@Override
			public int compare(Pair<Byte, Integer> p1, Pair<Byte, Integer> p2) {
				// TODO Auto-generated method stub
				return (p1.getValue()-p2.getValue());
			}
			
		};
		PriorityQueue <Pair<Byte, Integer>> symbolQueue = new PriorityQueue<>(255, weightComparator);
		PriorityQueue <Pair<Byte, Integer>> symbolQueue2 = new PriorityQueue<>(255, weightComparator);
		
			for(int i = 0; i < weights.length; ++i) {
				if(weights[i]>0) {
					symbolQueue.add(new Pair<Byte, Integer>((byte) i, weights[i]));
					
					//System.out.println("byte: " +(byte)i + " int " + weights[i]);
				}
				
			}
			symbolQueue.add(new Pair<Byte, Integer>(HuffmanNode.PSEUDO_EOF, 0));
			symbolQueue2.add(new Pair<Byte, Integer>(HuffmanNode.PSEUDO_EOF, 0));
		HuffmanTree tree = new HuffmanTree();
		
		
		buildTree(tree, symbolQueue);
		
	//	traverse(tree.root);
		
		Hashtable<Byte, String> encode = getEncodeCompress(tree);
		
		writeCompressed(encode, data, weights);
		}
		
	}

	
	
	private void writeCompressed(Hashtable<Byte, String> encode, byte[] data, int[] weights) {
		// TODO Auto-generated method stub
		
		
		File nf = new File(file+".hfm");
		 try  {
			 DataOutputStream out = new DataOutputStream(new FileOutputStream(nf));
			 out.writeInt(FILE_START);
			 
			 if(encode.size()==256){
				 out.writeInt(256);
			 }else {
				 out.writeInt(encode.size());
			 }
			
			 
			
			 
			 for(int i = 0; i < weights.length; ++i) {
				if(weights[i]>0) {
						System.out.println("i " + i  + " weight: " + weights[i]);
						
						out.writeByte(i);//symbol
						
						out.writeInt(weights[i]);//weight					}
				}
			 }
			 int bitCount = 0;
			
			 byte c=0;
			 for(int i = 0;i< data.length;++i) {
				 char[]code = encode.get(data[i]).toCharArray();
				 for(int j = 0; j < code.length;++j) {
					 if(code[j]=='1') {
						 c|=0x01;
					 }
					 if(bitCount==7) {
						 bitCount = 0;
						
						 out.writeByte(c);
						 c=0;
					 }else {
						 c<<=1;
						 bitCount++;
					 }
				 }
			 }
			 char[]code = pseudo.toCharArray();
			 for(int j = 0; j < code.length;++j) {
				 if(code[j]=='1') {
					 c|=0x01;
				 }
				 if(bitCount==7) {
					 bitCount = 0;
					
					 out.writeByte(c);
					 c=0;
				 }else {
					 c<<=1;
					 bitCount++;
				 }
			 }
			 if(bitCount!=0) {
				 c<<=7-bitCount;
				 
				 out.write(c);
			 }
			 
			 out.flush();
			 out.close();
				
				
			
			    } catch (IOException x) {
			      System.err.println(x);
			    }
		
		
		
		
		
		
		
		
		
	}

	private Hashtable<Byte, String> getEncodeCompress(HuffmanTree tree) {
		Hashtable<Byte, String> res = new Hashtable<Byte, String>();
		 ArrayList<HuffmanNode> treeContainer = new ArrayList<>();
		 
		 traverse(treeContainer, tree.root);
		 System.out.println("treeContainer size " + treeContainer.size());
		 for(int i = 0; i < treeContainer.size(); ++i) {
			 String code = getCode(treeContainer.get(i), tree.root);
			 if(treeContainer.get(i).weight==0 && treeContainer.get(i).symbol==HuffmanNode.PSEUDO_EOF) {
					System.out.println("PSEUDO: " + code);
					 pseudo = code;
			 }else {
				 res.put(treeContainer.get(i).symbol, code);
			 }
			 
		 }
		
		System.out.println("encode size " +res.size());
		return res;
	}
	
	
	private String getCode(HuffmanNode desired, HuffmanNode root) {
		String s="";
		HuffmanNode current = desired;
		while(current!=root) {
			if(current==current.parent.leftChild) {
				s+="0";
			}else if(current==current.parent.rightChild) {
				s+="1";
			}
			current=current.parent;
		}
		//reversing array
			char[] t = s.toCharArray();
			for(int left = 0, right = t.length-1; left<right; left++, right--) {
				
				 char temp = t[left]; 
		            t[left] = t[right]; 
		            t[right]=temp; 
			}
		s = new String(t);
		System.out.println(s);
		return s;
	}

	private void traverse(ArrayList<HuffmanNode> treeContainer, HuffmanNode root) {
		if(root == null) {
			return;
		}
		//System.out.println(root.weight +" " + root.symbol);	
		traverse(treeContainer, root.leftChild);
		
		traverse(treeContainer, root.rightChild);
		if(root.isS) {
			treeContainer.add(root);
		}
		
		
	} 
	
	

	private void buildTree(HuffmanTree tree, PriorityQueue<Pair<Byte, Integer>> symbolQueue) {
			 
		Comparator<HuffmanNode> nodeComparator = new Comparator<HuffmanNode>() {

			@Override
			public int compare(HuffmanNode n1, HuffmanNode n2) {
				// TODO Auto-generated method stub
				return (n1.weight - n2.weight);
			}
			
		};
	PriorityQueue <HuffmanNode> nodeHeap = new PriorityQueue<HuffmanNode>(symbolQueue.size(), nodeComparator); 
		 while(!symbolQueue.isEmpty()) {
			Pair<Byte, Integer> first = symbolQueue.poll();
			
			HuffmanNode n1 = new HuffmanNode(first.getKey(), first.getValue(), true); 
			nodeHeap.add(n1);
		 }
		
		 
		 while (nodeHeap.size()>1) { 
			  	
	            HuffmanNode x = nodeHeap.peek(); 
	            nodeHeap.poll(); 
	            HuffmanNode y = nodeHeap.peek(); 
	            nodeHeap.poll(); 
	            
	            HuffmanNode f = new HuffmanNode(HuffmanNode.NAN, (x.weight+y.weight), false); 
	            x.parent = f;
	            y.parent = f;
	            f.leftChild = x; 
	  
	            f.rightChild =y; 
	  
	            tree.root = f; 
	         // System.out.println("x: "+ tree.root.leftChild.weight + " y: " + tree.root.rightChild.weight + " f :"+ tree.root.weight + " root.left " + tree.root.leftChild + " root.right " + tree.root.rightChild + 
	      // 	" tree root : " + tree.root);
	            nodeHeap.add(f); 
	           
	        }
		 
		
	}

	public void decompress() {
		 
		int weights[] =new int[256];
	
		byte [] data = new byte[(int) file.length()];
		try  {
				in = new DataInputStream(new FileInputStream( file));
				int check = in.readInt();
				int size = in.readInt();
				if(size==0) {
					int si = file.getAbsolutePath().indexOf(".hfm");
					
					File nf = new File(file.getAbsolutePath().substring(0, si));
					
					
						 DataOutputStream out = new DataOutputStream(new FileOutputStream(nf));
						 out.close();
				}
				System.out.println("size = "+ size);
				for(int i = 0; i<size; ++i) {
					
					int index =Byte.toUnsignedInt(in.readByte());
					int weight = in.readInt();				
					System.out.println("i " + index  + " weight: " + weight);
					weights[index]=weight;
					
				}
				
			} catch (IOException x) {
				
			    System.err.println(x);
			}
	
			
			
		
		
		
		
	
		
		Comparator<Pair<Byte, Integer>> weightComparator = new Comparator<Pair<Byte, Integer>>() {

			@Override
			public int compare(Pair<Byte, Integer> p1, Pair<Byte, Integer> p2) {
				// TODO Auto-generated method stub
				return (p1.getValue()-p2.getValue());
			}
			
		};
		PriorityQueue <Pair<Byte, Integer>> symbolQueue = new PriorityQueue<>(255, weightComparator);
		PriorityQueue <Pair<Byte, Integer>> symbolQueue2 = new PriorityQueue<>(255, weightComparator);
		
			for(int i = 0; i < weights.length; ++i) {
				if(weights[i]>0) {
					symbolQueue.add(new Pair<Byte, Integer>((byte) i, weights[i]));
					symbolQueue2.add(new Pair<Byte, Integer>((byte) i, weights[i]));
					
				}
			}
			symbolQueue.add(new Pair<Byte, Integer>(HuffmanNode.PSEUDO_EOF, 0));
			symbolQueue2.add(new Pair<Byte, Integer>(HuffmanNode.PSEUDO_EOF, 0));
		HuffmanTree tree = new HuffmanTree();
		
		
		buildTree(tree, symbolQueue);
		
		Hashtable<String, Byte> encode = getEncodeDecompress(tree, symbolQueue2);
		
		writeDecompressed(encode, data);
		 	
	}

	private void writeDecompressed(Hashtable<String, Byte> encode, byte[] data) {
		String cur = "";
		byte[] masks = { -128, 64, 32, 16, 8, 4, 2, 1 };
		int si = file.getAbsolutePath().indexOf(".hfm");
		
		File nf = new File(file.getAbsolutePath().substring(0, si));
		boolean br = false;
		 try  {
			 DataOutputStream out = new DataOutputStream(new FileOutputStream(nf));
			 
			 while (true) {
				 byte b = in.readByte();
				 for (byte m : masks) {
				        if ((b & m) == m) {
				            cur+='1';
				        } else {
				            cur+='0';
				        }
				        if(cur.equals(pseudo)) {
				        	System.out.println("broken");
		        			System.out.println("cur: " + cur + " pseudo: " + pseudo);
		        			br=true;
		        			break;
				        }
				        if(encode.containsKey(cur)) {
				        	
				        		out.writeByte(encode.get(cur));
				        	//	System.out.println("written" + encode.get(cur));
					        	cur = "";
				        	
				        	
				        }
				    }
				if(br) {
					in.close();
					break;
				}
				
				
			}
			 
			
				out.flush();
				out.close();
			
			    } catch (IOException x) {
			    	
			      System.err.println(x);
			    }
		
		
		
		
		
		
	}

	private Hashtable<String, Byte> getEncodeDecompress(HuffmanTree tree, PriorityQueue<Pair<Byte, Integer>> symbolQueue2) {
		Hashtable<String, Byte> res = new Hashtable<String, Byte>();
		 ArrayList<HuffmanNode> treeContainer = new ArrayList<>();
		 
		 traverse(treeContainer, tree.root);
		 System.out.println("treeContainer size " + treeContainer.size());
		 for(int i = 0; i < treeContainer.size(); ++i) {
			 String code = getCode(treeContainer.get(i), tree.root);
			 if(treeContainer.get(i).weight==0 && treeContainer.get(i).symbol==HuffmanNode.PSEUDO_EOF) {
					System.out.println("PSEUDO: " + code);
					 pseudo = code;
					
				 }
			 res.put(code, treeContainer.get(i).symbol);
		 }
		
		System.out.println("encode size " +res.size());
		return res;
	}
}
