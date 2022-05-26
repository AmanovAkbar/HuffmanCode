import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;

import javafx.util.Pair;

public class HuffmanCodeFolder {
		final static int DIRECTORY_START = -1;
		final static int DIRECTORY_END = -2;
		final static int FILE_START = -3;
		final static int HUFF_END = -6;
		String pseudo = "";
		private DataInputStream in;
		private DataOutputStream out;
		File file;
		HuffmanCodeFolder(File f){
			file = f;
			
		}
		public void decompress(){
			 
			try {
				
				 in = new DataInputStream(new FileInputStream(file));
				int check = in.readInt();
				System.out.println(check);
				if(check == FILE_START) {
					
					in.close();
					HuffmanCode hfc = new HuffmanCode(file);
					hfc.decompress();
				}else {
					int si = file.getCanonicalPath().indexOf(".hfm");
					String dName = file.getCanonicalPath().substring(0, si);
					
					File cur = new File(dName);
					System.out.println(cur);
					cur.mkdir();
					boolean isfirst  = true;
					
					while (true) {
						if(check==DIRECTORY_START) {
							String s = "";
							while(true) {
								char c = in.readChar();
								if(c=='`') {
									break;
								}else {
									s=s+ Character.toString(c);
								}
							}
							if(isfirst) {
								isfirst = false;
							}else {
								System.out.println(s);
								cur = new File(cur.getCanonicalPath()+"/"+s);
								System.out.println(cur.toString());
								cur.mkdir();
							}
							
							
						
						
					}else if(check==FILE_START) {
						
						//cur = new File(cur.getParent());
						String s = "";
						while(true) {
							char c = in.readChar();
							if(c=='`') {
								break;
							}else {
								s=s+ Character.toString(c);
							}
						}
						System.out.println(s);
						
							
							cur = new File(cur.getCanonicalPath()+"/" +s);
					
						System.out.println(cur.toString());
						int size = in.readInt();
						
						System.out.println(size);
						if(size==0) {
							
							System.out.println("size==0");
							
								
								 DataOutputStream out = new DataOutputStream(new FileOutputStream(cur));
								 out.close();
								 cur = cur.getParentFile();
								 check = in.readInt();
								 continue;
						}
						int weights[] =new int[256];
						for(int i = 0; i<size; ++i) {
							
							int index =Byte.toUnsignedInt(in.readByte());
							int weight = in.readInt();				
							//System.out.println("i " + index  + " weight: " + weight);
							weights[index]=weight;
							
						}
						
						Comparator<Pair<Byte, Integer>> weightComparator = new Comparator<Pair<Byte, Integer>>() {
								@Override
							public int compare(Pair<Byte, Integer> p1, Pair<Byte, Integer> p2) {
							
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
							
							byte[] masks = { -128, 64, 32, 16, 8, 4, 2, 1 };
							
							
							
							 
							String cod = "";
							 DataOutputStream out = new DataOutputStream(new FileOutputStream(cur));
							boolean br=false;
							 byte b;
							while(true) {
									
								  b = in.readByte();
								 for (byte m : masks) {
								        if ((b & m) == m) {
								            cod+='1';
								        } else {
								            cod+='0';
								        }
								        if(pseudo.equals(cod)) {
						        			System.out.println("broken");
						        			System.out.println("cod: " + cod + "pseudo: " + pseudo);
						        			br=true;
						        			break;
						        		}
								        
								        if(encode.containsKey(cod)) {
								        	out.writeByte(encode.get(cod));
								        	cod = "";
								        
								        
								    }
								 }
								if(br) {
									break;
								}
								
							}
								 
							out.close();
							cur = cur.getParentFile();
							System.out.println(cur.toString()+ " HERE");
					}
					else if(check==DIRECTORY_END) {
						
						check=in.readInt();
						if(check==HUFF_END) {
							System.out.println("HUFF");
							continue;
						}else {
							System.out.println("notHUF");
							cur = cur.getParentFile();
							System.out.println(cur.getCanonicalPath());
							continue;
						}
					}else if(check==HUFF_END) {
						System.out.println("HUFFENDED!");
						break;
					}else {
						
						
						System.out.println("v rot ebal");
						return;
					}
					//System.out.println("HERE!");	
					check = in.readInt();	
					System.out.println(check);
				}
			}		
			}catch(IOException x) {
				
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
		public void compress() {
			File nf = new File(file+".hfm");
			try {
				out = new DataOutputStream(new FileOutputStream(nf));
				goThroughFiles(file);
				out.writeInt(HUFF_END);
				
				out.close();
			}catch(IOException x) {
				System.err.println(x);
			}
			
		}
		private void goThroughFiles(File f) throws IOException {
			out.writeInt(DIRECTORY_START);
			out.writeChars(f.getName() + '`');
			System.out.println(f.getName() + " directory name");
			for(File fEntry: f.listFiles()) {
				
				if(fEntry.isDirectory()) {
					goThroughFiles(fEntry);
				}else {
					compressFile(fEntry);
				}
			}
			out.writeInt(DIRECTORY_END);
			
		}
		private void compressFile(File fEntry) {
			if(fEntry.length()==0) {
				
				 try  {
					 out.writeInt(FILE_START);
					 out.writeChars(fEntry.getName());
					 out.writeChar('`');
					 out.writeInt(0);
					 
				 }catch (IOException x) {
				      System.err.println(x);
				    }
				 
			}else {
			byte [] data = new byte[(int) fEntry.length()];
			
			
			try {
					DataInputStream in = new DataInputStream(new FileInputStream( fEntry));
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
					
					return (p1.getValue()-p2.getValue());
				}
				
			};
			PriorityQueue <Pair<Byte, Integer>> symbolQueue = new PriorityQueue<>(255, weightComparator);
			
			int wc=0;
				for(int i = 0; i < weights.length; ++i) {
					
					if(weights[i]>0) {
						wc++;
						symbolQueue.add(new Pair<Byte, Integer>((byte) i, weights[i]));
						
						
					}
					
				}
				System.out.println("at read wc: " +wc);
				symbolQueue.add(new Pair<Byte, Integer>(HuffmanNode.PSEUDO_EOF, 0));
				
			HuffmanTree tree = new HuffmanTree();
			
			
			buildTree(tree, symbolQueue);
			
		//	traverse(tree.root);
			
			Hashtable<Byte, String> encode = getEncodeCompress(tree);
			System.out.println("encode size " + encode.size());
			writeCompressed(fEntry, encode, data, weights);
			}	
		}
		
		private void writeCompressed(File fEntry, Hashtable<Byte, String> encode, byte[] data, int[] weights) {
			
			
			
			
			 try  {
				
				 out.writeInt(FILE_START);
				 out.writeChars(fEntry.getName());
				 out.writeChar('`');
				System.out.println(fEntry.getName() + " Name of the file");
				System.out.println(encode.size());
				 if(encode.size()==256){
					 out.writeInt(256);
				 }else {
					 out.writeInt(encode.size());
				 }
				
				 
				int wC = 0;
				 
				 for(int i = 0; i < weights.length; ++i) {
					if(weights[i]>0) {
							System.out.println("i " + i  + " weight: " + weights[i]);
							wC++;
							out.writeByte(i);//symbol
							
							out.writeInt(weights[i]);//weight					}
					}
				 }
				 System.out.println(wC);
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
				//System.out.println(code);
				//System.out.println(pseudo);
				 for(int j = 0; j < code.length;++j) {
					 System.out.print(code[j]);
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
					
					 out.writeByte(c);
				 }
				
				
				
					
					
				
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
			
			//System.out.println(s);
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
				//System.out.println(root.symbol + " "+ root.weight);
				treeContainer.add(root);
			}
			
			
			
		} 
		
		

		private void buildTree(HuffmanTree tree, PriorityQueue<Pair<Byte, Integer>> symbolQueue) {
				 
			Comparator<HuffmanNode> nodeComparator = new Comparator<HuffmanNode>() {

				@Override
				public int compare(HuffmanNode n1, HuffmanNode n2) {
					
					return (n1.weight - n2.weight);
				}
				
			};
			int sqC=0;
		PriorityQueue <HuffmanNode> nodeHeap = new PriorityQueue<HuffmanNode>(256, nodeComparator); 
			 while(!symbolQueue.isEmpty()) {
				Pair<Byte, Integer> first = symbolQueue.poll();
				
				HuffmanNode n1 = new HuffmanNode(first.getKey(), first.getValue(), true); 
				nodeHeap.add(n1);
				sqC++;
			 }
			System.out.println("size if symbolQ " +sqC);
			 
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
}
