import java.io.*;
import java.util.*;

class Dijkstra{
//	define the edge size
	private static final int EDGESIZE = 7035;
//	define the spatial network
	private static Edge[] edges = new Edge[EDGESIZE];
//	define Comparator for Edges
	private static Comparator<Edge> comp = new Comparator<Edge>(){
		@Override
		public int compare(Edge o1, Edge o2) {
			if (o1.getDistance() < o2.getDistance()){
				return -1;
			}else{
				return 1;
			}
		};
	};
//	define Comparator for Nodes
	private static Comparator<Node> compnode = new Comparator<Node>() {
		
		@Override
		public int compare(Node o1, Node o2) {
			// TODO Auto-generated method stub
			return o1.getNodeId() - o2.getNodeId();
		}
	};
//	define point Departure Point
	private static Point departure;
//	define point Destination Point
	private static Point destination;
//	define Graph
	private static Graph g;
	
	
	public static void main(String args[]){
		readEdges();
		writeEdges();
		g = new Graph();
		g.init();
		g.setGraph();
		for(Node n : g.getNodeSet()){
			System.out.print(n.getNodeId());
			if(!n.getAdj().isEmpty()){
				for(Edge e : n.getAdj()){
					System.out.println("\t" + e.getEdgeId() + "\t" + e.getFstNode() + "\t" + e.getSndNode() + "\t" + e.getDistance());
				}
			}else{
				System.out.println();
			}

		}
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
	
//	define method Dijkstra
//	public static void Dijkstra(int source, int target){
//		
//	}
	
//	define class Edge
	public static class Edge{
		private int edgeId;
		private int fstNode;
		private int sndNode;
		private double distance;
		
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
	
//	define class Node
	public static class Node {
		private int NodeId;
		private Boolean visited = null;
		private Set<Edge> adj;
		
		public Node(int NodeId){
			this.NodeId = NodeId;
			this.adj = new TreeSet<Edge>(comp);
		}
		
		public void addAdj(Edge adjEdge){
			adj.add(adjEdge);
		}
		
		public int getNodeId(){
			return NodeId;
		}
		
		public Set<Edge> getAdj(){
			return adj;
		}
		
		public Boolean isVisited(){
			return visited;
		}
		
		public void visit(){
			visited = true;
		}
	}
	
//	define class Graph
	public static class Graph{
		private Set<Node> nodeSet = null;
		
		public Graph(){
			nodeSet = new TreeSet<Node>(compnode);
		}
		
		public Set<Node> getNodeSet(){
			return nodeSet;
		}
		
		public void init(){
			for (int i = 0; i < EDGESIZE; i++){
				Edge e = edges[i];
				Node n1 = new Node(e.getFstNode());
				Node n2 = new Node(e.getSndNode());
				nodeSet.add(n1);
				nodeSet.add(n2);
			}
		}

		public void setGraph(){
			for (int i = 0; i < EDGESIZE; i++){
				Edge e = edges[i];
				Iterator<Node> it = nodeSet.iterator();
				while(it.hasNext()){
					Node n = (Node)it.next();
					if(n.getNodeId() == e.getFstNode()){
						n.addAdj(e);
					}
					if(n.getNodeId() == e.getSndNode()){
						n.addAdj(e);
					}
				}
			}
		}
	}
	
//	define class Point
	public static class Point{
		private int fstNode;
		private int sndNode;
		private double position;
		
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