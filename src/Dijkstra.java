import java.util.*;
import java.io.*;

//define class Point
class Point{
	private Vertex fstVertex;
	private Vertex sndVertex;
	private double position;
	
	public Point(Vertex fstVertex, Vertex sndVertex, double position){
		this.fstVertex = fstVertex; 
		this.sndVertex = sndVertex;
		this.position = position;
	}
	
	public Vertex getFstVertex(){
		return fstVertex;
	}
	
	public Vertex getSndVertex(){
		return sndVertex;
	}
	public double getPosition(){
		return position;
	}
}

class Vertex implements Comparable<Vertex>{
	private int vertexId;
	private double minDistance;
	private Set<Edge> adjacents;
	boolean visited;
	
	public Vertex(int vertexId){
		this.vertexId = vertexId;
		minDistance = Double.MAX_VALUE;
		adjacents = new HashSet<Edge>();
		visited = false;
	}
	
	public void show(){
//		if(vertexId <= 6105){
			System.out.print(vertexId);
//		}
	}
	
	public void addAdjacents(Edge e){
		adjacents.add(e);
	}
	
	public Set<Edge> getAdjacents(){
		return adjacents;
	}
	
	public int getVertexId(){
		return vertexId;
	}
	
	public double getMinDistance(){
		return minDistance;
	}
	
	public void setMinDistance(double distance){
		minDistance = distance;
	}
	
	public boolean isVisited(){
		return visited;
	}
	
	public void visit(){
		visited = true;
	}
	
	public void notVisit(){
		visited = false;
	}
	
	@Override
	public int compareTo(Vertex o) {
		// TODO Auto-generated method stub
		return Double.compare(minDistance, o.minDistance);
	}
}

class Edge{
	int edgeId;
	Vertex fstVertex;
	Vertex sndVertex;
	double weight;
	
	public Edge(int edgeId, Vertex fstVertex, Vertex sndVertex, double weight){
		this.edgeId = edgeId;
		this.fstVertex = fstVertex;
		this.sndVertex = sndVertex;
		this.weight = weight;
	}
	
	public int getEdgeId(){
		return edgeId;
	}
	
	public Vertex getFstVertex(){
		return fstVertex;
	}
	
	public Vertex getSndVertex(){
		return sndVertex;
	}
	
	public double getWeight(){
		return weight;
	}
}

class SPD{
	private Vertex v;
	private double distance;
	public SPD(Vertex v, double distance){
		this.v = v;
		this.distance = distance;
	}
	public void setSPD(Vertex v, double distance){
		this.v = v;
		this.distance = distance;
	}
	public void setVertex(Vertex v){
		this.v = v;
	}
	public void setDistance(double distance){
		this.distance = distance;
	}
	public Vertex getVertex(){
		return v;
	} 
	public double getDistance(){
		return distance;
	}
}

class Dijkstra{
	private static int NODESIZE;
	private static Point departure;
	private static Point destination;
	private static SPD current;
	private static Queue<SPD> spd;
	private static double distance;
	private static Vertex depa;
	private static Vertex dest;
//	define the spatial network
	private static Set<Edge> edges = new HashSet<Edge>();
	private static Set<Vertex> nodes = new TreeSet<Vertex>(new Comparator<Vertex>(){
		@Override
		public int compare(Vertex o1, Vertex o2) {
			// TODO Auto-generated method stub
			return o1.getVertexId() - o2.getVertexId();
		}
	});
	
	private static Map<Vertex, Vertex> path = new HashMap<Vertex, Vertex>();
	
	public static Vertex getVertex(int id){
		Vertex result = null;
		for(Vertex v : nodes){
			if(v.getVertexId() == id){
				result = v;
				break;
			}
		}
		return result;
	}
	
	public static void readEdges(){
		String src = "src/spatial_network.txt";
		File f = new File(src);
		try{
			BufferedReader br = new BufferedReader(new FileReader(f));
			String temp = null;
			StringTokenizer st = null;
			int edgeId;
			Vertex fstVertex = null;
			Vertex sndVertex = null;
			double weight;
			while((temp = br.readLine()) != null){
				st = new StringTokenizer(temp);
				edgeId = Integer.parseInt(st.nextToken());
				fstVertex = new Vertex(Integer.parseInt(st.nextToken()));
				sndVertex = new Vertex(Integer.parseInt(st.nextToken()));
				weight = Double.parseDouble(st.nextToken());
				Edge edge = new Edge(edgeId, fstVertex, sndVertex, weight);
				edges.add(edge);
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void readNodes(){
		for(Edge e : edges){
			nodes.add(e.getFstVertex());
			nodes.add(e.getSndVertex());
		}
		NODESIZE = nodes.size();
	}
	
	public static void setNodes(){
		for(Edge e : edges){
			for(Vertex n:nodes){
				if(n.getVertexId() == e.getFstVertex().getVertexId()){
					n.getAdjacents().add(e);
				}
				if(n.getVertexId() == e.getSndVertex().getVertexId()){
					n.getAdjacents().add(e);
				}
			}
		}
	}
	
//	write all the edges
	public static void writeEdges(){
		for(Edge e : edges){
			System.out.println(e.getEdgeId() + "\t" + e.getFstVertex().getVertexId() + "\t" + e.getSndVertex().getVertexId() + "\t" + e.getWeight());
		}
	}
	
	public static double getMinDistance(){
		return distance;
	}
	
	public static void find(Vertex sour, Vertex dest){
		SPD temp = null;
		for(Vertex v : nodes){
			v.notVisit();
		}
		sour.visit();
	
		spd = new PriorityQueue<SPD>(1 , new Comparator<SPD>() {
			@Override
			public int compare(SPD o1, SPD o2) {
				// TODO Auto-generated method stub
				if(o1.getDistance() < o2.getDistance()){
					return -1;
				}
				else{
					return 1;
				}
			}
		});
		sour.setMinDistance(0.0);
		temp = new SPD(sour, 0.0);
		spd.add(temp);
		path.put(sour, sour);
		
//		check if the Queue is empty
		while(!spd.isEmpty()){
//			get first object from Queue
			current = spd.poll();
			
			Vertex v = current.getVertex();
			distance = current.getDistance();

//			mark the node as visited
			v.visit();

//			check if the node is destination
			if(v.getVertexId() == dest.getVertexId()){
				System.out.println("Find!");
				break;
			}
			
			else{
//				find every neighbor of this node
				for(Edge e : v.getAdjacents()){
					Vertex u = (e.getFstVertex().getVertexId() == v.getVertexId()) ? getVertex(e.getSndVertex().getVertexId()) : getVertex(e.getFstVertex().getVertexId());
					if(!u.isVisited()){
						path.put(u, v);
//						if neighbor node is not visited, mark as visited
						if(u.getMinDistance() > v.getMinDistance() + e.getWeight()){
							u.setMinDistance(v.getMinDistance() + e.getWeight());
							spd.add(new SPD(u, u.getMinDistance()));
						}
						u.visit();
					}else{
						if(u.getMinDistance() > v.getMinDistance() + e.getWeight()){
							u.setMinDistance(v.getMinDistance() + e.getWeight());
							spd.add(new SPD(u, u.getMinDistance()));
						}
						for(SPD a : spd){
							if(a.getVertex().getVertexId() == u.getVertexId()){
								if(u.getMinDistance() < a.getDistance()){
									a.setDistance(u.getMinDistance());
									path.put(u, v);
								}
							}
						}	
					}
				}
			}
		}
	}
	
	public static LinkedList<Vertex> getPath(Vertex target, Vertex source) {
	    LinkedList<Vertex> pathinfo = new LinkedList<Vertex>();
	    Vertex step = target;
	    // check if a path exists

	    if (path.get(step) == null) {
	      return null;
	    }
	    
	    pathinfo.add(step);
	    while (path.get(step) != null && step != source) {
	      step = path.get(step);
	      pathinfo.add(step);
	    }
	    // Put it into the correct order
	    Collections.reverse(pathinfo);
	    return pathinfo;
	  }
	
	public static void getPoints(){
		Scanner in = new Scanner(System.in);
		System.out.println("Please input Departure and Destination Points(separated by space):");
		departure = new Point(new Vertex(in.nextInt()), new Vertex(in.nextInt()), in.nextDouble());
		destination = new Point(new Vertex(in.nextInt()), new Vertex(in.nextInt()), in.nextDouble());
		in.close();
	}
	
	public static void modifyGraph(Point p1, Point p2){
		Vertex v1 = p1.getFstVertex();
		Vertex v2 = p1.getSndVertex();
		double position1 = p1.getPosition();
		Vertex v3 = p2.getFstVertex();
		Vertex v4 = p2.getSndVertex();
		double position2 = p2.getPosition();
		for(Edge e:edges){
			if((e.getFstVertex().getVertexId() == v1.getVertexId()) && (e.getSndVertex().getVertexId() == v2.getVertexId())){
				if((e.getWeight() == position1) || (e.getWeight() == 0)){
					if((e.getWeight() == position1)){
						depa = e.getSndVertex();
					}
					else if(position1 == 0){
						depa = e.getFstVertex();
					}
				}else{
					Vertex sour = new Vertex(nodes.size());
					Edge e1 = new Edge(edges.size(), v1, sour, position1);
					edges.add(e1);
					Edge e2 = new Edge(edges.size(), sour, v2, e.getWeight() - position1);
					edges.add(e2);
					nodes.add(sour);
					edges.remove(e);
					depa = sour;
				}
				break;
			}
		}
		for(Edge e:edges){
			if((e.getFstVertex().getVertexId() == v3.getVertexId()) && (e.getSndVertex().getVertexId() == v4.getVertexId())){
				if((e.getWeight() == position2) || (e.getWeight() == 0)){
					if((e.getWeight() == position2)){
						dest = e.getSndVertex();
					}
					else if(position2 == 0){
						System.out.println("AAAAA");
						dest = e.getFstVertex();
					}
				}else{
					Vertex res = new Vertex(nodes.size());
					Edge e1 = new Edge(edges.size(), v3, res, position2);
					edges.add(e1);
					Edge e2 = new Edge(edges.size(), res, v4, e.getWeight() - position2);
					edges.add(e2);
					nodes.add(res);
					edges.remove(e);
					dest = res;
				}
				break;
			}
		}
	}
	
	public static void main(String arg[]){
		readEdges();
		readNodes();
		
		getPoints();
		modifyGraph(departure, destination);
		setNodes();
		find(depa, dest);
		System.out.println("The shortest path distance is:");
		System.out.println(getMinDistance());
		List<Vertex> pathinfo = new LinkedList<>();
		pathinfo = getPath(dest, depa);
		System.out.println("The path is:");
		for(Vertex v:pathinfo){
			if(v.getVertexId() < NODESIZE){
				v.show();
				System.out.print("\t");
			}
		}
	}

}
