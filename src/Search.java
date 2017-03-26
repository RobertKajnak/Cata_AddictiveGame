import java.time.Year;
import java.util.ArrayList;

public class Search {
	
	private int status = 3;
	private static int stack = 0;

	
	public Search() {} // End of Constructor
	
	public int search(Point start, Point end, int[][] graph) {
		
		
		f(start, new ArrayList<Point>(), end, graph);
		
		return status;
	} // End of search
	
	public void f(Point point, ArrayList<Point> visited, Point end, int[][] graph) {
		//System.out.println(stack++);
		
		if (visited.contains(point)) return;
		else {
			visited.add(point);
			
			// Create list of neighbours
			ArrayList<Point> neighbors = new ArrayList<Point>();
			neighbors.add(new Point(point.x + 1, point.y, point.color));
			neighbors.add( new Point(point.x - 1, point.y, point.color));
			neighbors.add( new Point(point.x, point.y - 1, point.color));
			neighbors.add( new Point(point.x, point.y + 1, point.color));
			
			// If the next node is the end change status to 2, else recurse
			for (Point p : neighbors) {
				if (p.equals(end)) {
					status = 2;
					return;
				}
				else {
					int width = graph.length;
					
					// Only go there if it is ok
					if (1 <= p.x && p.x < graph.length && 1 <= p.y && p.y < graph[1].length && status != 2 && graph[p.x][p.y] == 0) {
						f(p, visited, end, graph);
					}
					else return;
				}
			}
		}
	} // End of f
} // End of Class Search
