import java.util.*;
import java.io.*;

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
		System.out.print(vertexId);
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
	private static SPD current;
	private static Queue<SPD> spd;
	private static double distance;
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

	public static void main(String arg[]){
		readEdges();
		readNodes();
		setNodes();
	
		
		Vertex v1 = null;
		Vertex v2 = null;
		
		for(Vertex v: nodes){
			if(v.getVertexId() == 0){
				v1 = v;
			}
		}
		for(Vertex v: nodes){
			if(v.getVertexId() == 6000){
				v2 = v;
			}
		}
		
		find(v1, v2);
		
		System.out.println("The shortest path distance is:");
		System.out.println(getMinDistance());

		List<Vertex> pathinfo = new LinkedList<>();
		pathinfo = getPath(v2, v1);
		System.out.println("The path is:");
		for(Vertex v:pathinfo){
			v.show();
			System.out.print("\t");
		}
	}

}
