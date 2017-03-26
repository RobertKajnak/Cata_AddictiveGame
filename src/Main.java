import java.awt.List;
import java.awt.color.ICC_ColorSpace;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.naming.directory.DirContext;
import javax.print.attribute.standard.PrinterMessageFromOperator;

public class Main {
	static String dir = "Inputs\\";
	
	static ArrayList<Point> points;
	static Scanner sc;
	static int lastColor;
	
	public static int Manhattan(Point p1, Point p2) {
		return Math.abs(p1.x-p2.x) + Math.abs(p2.y - p1.y);
	}
	
	public static Point Move(Point point, char direction) {
		switch (direction) {
		case 'N':
			if (point.x==1)
				return null;
			return new Point(point.x-1, point.y, point.color);
		case 'S':
			if (point.x == Point.rMax)
				return null;
			return new Point(point.x+1, point.y,point.color);
		case 'E':
			if (point.y == Point.cMax)
				return null;
			return new Point(point.x,point.y+1, point.color);
		case 'W':
			if (point.y == 1)
				return null;
			return new Point(point.x, point.y-1,point.color);
		default:
			return null;
		}
	}
	
	public static int[][] CopyPointMap(int[][] old,int n, int m ){
		int[][] newMap = new int [n][m];
		for (int i=0;i<n;i++){
			for (int j=0;j<m;j++){
				newMap[i][j] = old[i][j];
			}
		}
		return newMap;
	}
	
	public static void main(String[] args) {
		String inputFile =dir + "level5-1.in";
		try {
			sc = new Scanner(new FileReader(inputFile));
			
			int numbertOfTests = sc.nextInt();
			
			for (int k = 0; k<numbertOfTests; k++){
				Point.rMax = sc.nextInt();
				Point.cMax = sc.nextInt() ;
				int n = sc.nextInt();
				points = new ArrayList<Point>();
				int[][] pointMap = new int[Point.rMax +1][Point.cMax+1];
				
				int pos = 0;
				int color = 0;
				int colorMax = 0;
				
				for (int i = 0; i < n; i++) {
					pos = sc.nextInt();
					color = sc.nextInt();
					
					if (color>colorMax){
						colorMax = color;
					}
					Point point =new Point(pos, color);
					points.add(point);
					pointMap[point.x][point.y] = point.color;
				}
				
				ArrayList<Integer> searchedColors= new ArrayList<Integer>();
				int numberOfPaths = sc.nextInt();
				for (int j=0; j<numberOfPaths;j++){
					int[][] tentative = checkPath(pointMap,searchedColors);
					if (tentative!=null){
						pointMap = tentative; 
					}
				}
				
				Point p1,p2;
				for (int i =1; i<=colorMax; i++){
					p1=p2=null;
					for (Point p : points){
						if (p.color == i){
							if (p1==null){
								p1 = p;
							}
							else{
								p2 = p;
							}
						}
					}
					if (!searchedColors.contains(i)){
						Search s = new Search();
						int a = s.search(p1, p2, pointMap);
						//System.out.println("Color " + p1.color + " is " + ((a == 3)? "not "  : "") + "connectable");
						System.out.print(a + " ");
					}
					else{
						System.out.print(1 + " ");
						//System.out.println("Color " + p1.color + " is already conected");
					}
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	static void DrawMatrix(int[][] pointMap,int numberOfPaths){
		System.out.println("Starting draw;");
		PrintMatrix(pointMap);
		ArrayList<Integer> scl= new ArrayList<>();
		for (int j=0; j<numberOfPaths;j++){
			int[][] tentative = checkPath(pointMap,scl);
			if (tentative!=null){
				pointMap = tentative; 
			}
		}

		PrintMatrix(pointMap);
		System.out.println("Finished drawing");
		
	}
	
	static void PrintMatrix(int[][] map){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(dir + "result.txt"));
			for (int i=0;i<=Point.rMax;i++){
				for (int j=0;j<=Point.cMax;j++){
					bw.write(map[i][j]==0?" ":("X"));
				}
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static int[][] checkPath( int[][] oldMap, ArrayList<Integer> searchedColors){
		char direction;
		String nextStuff = sc.next();
		//System.out.println(nextStuff);
		int colorToFind = Integer.parseInt(nextStuff);//sc.nextInt();
		//System.out.println(colorToFind + " is " + 1);
		searchedColors.add(colorToFind);
		Point  startingPoint = new Point(sc.nextInt(), colorToFind);
		Point currentPoint = new Point(startingPoint.x, startingPoint.y, startingPoint.color);
		int pathLength = sc.nextInt();
		int [][]pointMap = CopyPointMap(oldMap, Point.rMax+1, Point.cMax+1);
		
		boolean failed =false;
		for (int i=0;i<pathLength;i++){
			direction = sc.next().charAt(0);
			//System.out.println(direction);
			if (!failed){
				currentPoint = Move(currentPoint, direction);
			}
			
			if (currentPoint == null && !failed){
				//System.out.println(-1 + " " + (i+1) + " | out of map");
				failed = true;
			}
			if (!failed && pointMap[currentPoint.x][currentPoint.y]!=0){
				if (i==pathLength-1 && pointMap[currentPoint.x][currentPoint.y] == currentPoint.color && !currentPoint.equals(startingPoint)){
					
				}
				else{
					//System.out.println(-1 + " " + (i+1) + " Color already there");
					failed = true;
				}
			}
			if (!failed){
				pointMap[currentPoint.x][currentPoint.y] = currentPoint.color;
			}
		}
		
		//if (points.stream().filter(p -> p.equals(currentPoint) ).collect(Collectors.toList()).size()!=0){
		if (!points.contains(currentPoint) && !failed){	
			failed = true;
			//System.out.println(-1 + " " + pathLength + " Not correct point at destination");
		}
		if (!failed){
			//System.out.println(1 + " " + pathLength);
			return pointMap;
		}
		return null;
	}
}


class Point{
	public int x,y;
	public int pos;
	public int color;
	public static int rMax;
	public static int cMax;
		
	public Point(int x, int y, int color){
		this.x = x;
		this.y = y;
		this.pos = x * cMax +y;
		this.color = color;
	}
	public Point(int pos, int color) {
		this.pos = pos;
		x = (cMax-1+pos)/cMax;
		y = pos%cMax==0?cMax:pos%cMax;
		this.color = color;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + color;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (color != other.color)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	
}

/*2.
 * Point p1,p2;
for (int i =1; i<=colorMax; i++){
	p1=p2=null;
	for (Point p : points){
		if (p.color == i){
			if (p1==null){
				p1 = p;
			}
			else{
				p2 = p;
			}
		}
	}
	System.out.println( Manhattan(p1,p2));
}*/
/* 1.
Point p1, p2;
for (int i = 0; i < n; i++) {
	pos = sc.nextInt();
	p1 = new Point(pos,0);
	System.out.print(p1.x );
	System.out.println(" " + p1.y);
}*/