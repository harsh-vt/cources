package c2w1;


import java.io.*;
import java.util.*;

public class Scc {
	
	private ArrayList<ArrayList<Integer>> vertex; 
	private ArrayList<ArrayList<Integer>> backw; 
	private int[] lab_id; 
	private int[] top; 
	private int timestamp; 
	private int src;
	private boolean[] done; 
	
	public Scc(String inputFileName) throws FileNotFoundException{
		vertex = new ArrayList<ArrayList<Integer>>();
		backw = new ArrayList<ArrayList<Integer>>();
		Scanner in = new Scanner(new File(inputFileName));
		while (in.hasNextInt()){
			 int tail = in.nextInt();
			 int head = in.nextInt();
			 int max = Math.max(tail, head);
			 while (vertex.size() < max){
				 vertex.add(new ArrayList<Integer>());
				 backw.add(new ArrayList<Integer>());
			 }
			 vertex.get(tail-1).add(head-1);
			 backw.get(head-1).add(tail-1);
		}
	}
	
	public int[] computeSCC(){
		int[] top5 = new int[5];
		DFSLoop1();
		DFSLoop2();
		Arrays.sort(top);
		for (int i = 0; i < 5; i++){
			top5[i] = top[top.length - i - 1];
		}
		return top5;
	}
	
	public void DFSLoop1(){
		timestamp = 0;
		done = new boolean[backw.size()];
		lab_id = new int[vertex.size()];
		for (int i = backw.size()-1; i >= 0; i--){
			if (done[i] == false){
				DFS1(i);
			}
		}
	}
	
	public void DFSLoop2(){
		done = new boolean[vertex.size()];
		top = new int[vertex.size()];
		for (int i = lab_id.length - 1; i >= 0; i--){
			int node = lab_id[i];
			if (done[node] == false){
				src = node;
				DFS2(node);
			}
		}
	}
	
	public void DFS1(int node){
		done[node] = true;
		for (int head : backw.get(node)){
			if (done[head] == false){
				DFS1(head);
			}
		}
		lab_id[timestamp] = node;
		timestamp++;
	}
	
	public void DFS2(int node){
		done[node] = true;
		top[src] ++;
		for (int head : vertex.get(node)){
			if (done[head] == false){
				DFS2(head);
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException
	{
		Scc g = new Scc("C:\\Users\\MOJO-JOJO\\Desktop\\SCC.txt");
		int[] topSCCs = g.computeSCC();
		for (int n : topSCCs){
			System.out.print(n + " ");
		}
	}
}
