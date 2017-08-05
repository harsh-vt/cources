package Course2.Week2;

import java.io.*;
import java.util.*;

public class Dijik {
	
	private ArrayList<ArrayList<int[]>> vertex;
	private HashSet<Integer> done;


	public int[] sPath(){
		int x = vertex.size();
		done = new HashSet<Integer>();
		int[] p = new int[x];
		done.add(1);
		p[0] = 0;
		while (done.size() < x){
			int q = -1;
			int len = 1000000;
			for (int point : done){				
				for (int[] edge : vertex.get(point - 1)){
					if (!done.contains(edge[0])){
						if (p[point-1] + edge[1] < len){
							q = edge[0];
							len = p[point-1] + edge[1];
						}
					}
				}
			}
			if (q != -1){
				done.add(q);
				p[q-1] = len;
			} else {
				for (int i = 0; i < x; i++){
					if (!done.contains(i+1)){
						p[i] = 1000000;
					}
				}
				break;
			}
		}		
		return p;
	}
	
	public Dijik(String inputFileName) throws FileNotFoundException{
		vertex = new ArrayList<ArrayList<int[]>>();
		Scanner in = new Scanner(new File(inputFileName));
		while (in.hasNextLine()){
			 vertex.add(new ArrayList<int[]>());
			 String[] line = in.nextLine().split("\t");
			 int node = Integer.parseInt(line[0]);
			 for (int i = 1; i < line.length; i++){
				 String[] edgeStr = line[i].split(",");
				 int[] edge = new int[2];
				 edge[0] = Integer.parseInt(edgeStr[0]);
				 edge[1] = Integer.parseInt(edgeStr[1]);
				 vertex.get(node - 1).add(edge);
			 }
		}		
		
	}
	public static void main(String[] args) throws FileNotFoundException{
		Dijik g = new Dijik("C:\\Users\\MOJO-JOJO\\Desktop\\dijkstraData.txt");
		int[] way = g.sPath();	
		System.out.println(way[6] + "\t" + way[36] + "\t" + way[58] + "\t" + 
						   way[81] + "\t" + way[98] + "\t" + way[114] + "\t" + 
						   way[132] + "\t" + way[164] + "\t" + way[187] + 
						   "\t" + way[196]);
	}
}
