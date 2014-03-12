import java.io.*;
import java.util.*;

class Dijkstra{
//	define the edge size
	private static final int EDGESIZE = 7035;
//	define the spatial network
	private static Edge[] edges = new Edge[EDGESIZE];
//	define point Departure Point
	private static Point departure;
//	define point Destination Point
	private static Point destination;
	
	public static void main(String args[]){
		readEdges();
		writeEdges();
		getPoints();	
		readPoints();
	}
	
//	get Point from Input
	public static void getPoints(){
		Scanner in = new Scanner(System.in);
		System.out.println("Please input Departure and Destination Points(separated by space):");
		departure = new Point(in.nextInt(), in.nextInt(), in.nextDouble());
		destination = new Point(in.nextInt(), in.nextInt(), in.nextDouble());
		in.close();
	}
	
//	read points
	public static void readPoints(){
		System.out.println(departure.getFstNode() + "\t" + departure.getSndNode() + "\t" +departure.getPosition());
		System.out.println(destination.getFstNode() + "\t" + destination.getSndNode() + "\t" +destination.getPosition());
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
			br.close();
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
		System.out.println(wedge.getEdgeId() + "\t" + wedge.getFstNode() + "\t" + wedge.getSndNode() + "\t" + wedge.getDistance());
		
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
		
		public int getEdgeId(){
			return edgeId;
		}
		
		public int getFstNode(){
			return fstNode;
		}
		
		public int getSndNode(){
			return sndNode;
		}
		
		public double getDistance(){
			return distance;
		}
		
	}
	
//	define class Point
	public static class Point{
		int fstNode;
		int sndNode;
		double position;
		
		public Point(int fstNode, int sndNode, double position){
			this.fstNode = fstNode; 
			this.sndNode = sndNode;
			this.position = position;
		}
		
		public int getFstNode(){
			return fstNode;
		}
		
		public int getSndNode(){
			return sndNode;
		}
		public double getPosition(){
			return position;
		}
	}
}