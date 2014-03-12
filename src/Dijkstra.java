import java.io.*;
import java.util.*;

class Dijkstra{
//	define the edge size
	private static final int EDGESIZE = 7035;
//	define the spatial network
	private static Edge[] edges = new Edge[EDGESIZE];
	
	public static void main(String args[]){
		readEdges();
		writeEdges();
	}
//	read all the edges
	public static void readEdges(){
		String src = "src/spatial_network.txt";
		File f = new File(src);
		try{
			BufferedReader br = new BufferedReader(new FileReader(f));
			String temp = null;
			StringTokenizer st = null;
			int edgeId;
			int fstNode;
			int sndNode;
			double distance;
			int n = 0;
			while((temp = br.readLine()) != null){
				st = new StringTokenizer(temp);
				edgeId = Integer.parseInt(st.nextToken());
				fstNode = Integer.parseInt(st.nextToken());
				sndNode = Integer.parseInt(st.nextToken());
				distance = Double.parseDouble(st.nextToken());
				Edge edge = new Edge(edgeId, fstNode, sndNode, distance);
				edges[n] = edge;
				n++;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
//	write all the edges
	public static void writeEdges(){
		for(int n = 0; n < EDGESIZE; n++){
			writeEdge(n);
		}
	}
//	write one specific edge
	public static void writeEdge(int n){
		Edge wedge = edges[n];
		System.out.println(wedge.edgeId + "\t" + wedge.fstNode + "\t" + wedge.sndNode + "\t" + wedge.distance);
		
	}
//	define class Edge
	public static class Edge{
		int edgeId;
		int fstNode;
		int sndNode;
		double distance;
		
		public Edge(int edgeId, int fstNode, int sndNode, double distance){
			this.edgeId = edgeId;
			this.fstNode = fstNode;
			this.sndNode = sndNode;
			this.distance = distance;
		}
	}
}