
public class Rectangles {
	
	public static class Line {
		Point point1, point2;
		
		public Line(Point p1, Point p2) {
			super();
			this.point1 = p1;
			this.point2 = p2;
        }
		
		public double length() {       
		    return Math.sqrt((this.point2.y - this.point1.y) * (this.point2.y - this.point1.y) + (this.point2.x - this.point1.x) * (this.point2.x - this.point1.x));
		}
		
		public static boolean onSegment(Point p, Point q, Point r) { 
			return (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && 
		        q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y));
		}
		
		public static int orientation(Point p, Point q, Point r) { 
		    int val = (q.y - p.y) * (r.x - q.x) - 
		            (q.x - p.x) * (r.y - q.y); 
		  
		    if (val == 0) return 0; 
		  
		    return (val > 0)? 1: 2;
		} 
		
		public static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) { 
		    // Find the four orientations needed for general and 
		    // special cases 
		    int o1 = orientation(p1, q1, p2); 
		    int o2 = orientation(p1, q1, q2); 
		    int o3 = orientation(p2, q2, p1); 
		    int o4 = orientation(p2, q2, q1); 
		  
		    // General case 
		    if (o1 != o2 && o3 != o4) 
		        return true; 
		  
		    // Special Cases 
		    // p1, q1 and p2 are colinear and p2 lies on segment p1q1 
		    if (o1 == 0 && onSegment(p1, p2, q1)) return true; 
		  
		    // p1, q1 and q2 are colinear and q2 lies on segment p1q1 
		    if (o2 == 0 && onSegment(p1, q2, q1)) return true; 
		  
		    // p2, q2 and p1 are colinear and p1 lies on segment p2q2 
		    if (o3 == 0 && onSegment(p2, p1, q2)) return true; 
		  
		    // p2, q2 and q1 are colinear and q1 lies on segment p2q2 
		    if (o4 == 0 && onSegment(p2, q1, q2)) return true; 
		  
		    return false; // Doesn't fall in any of the above cases 
		} 
		
		public static boolean doIntersect(Line a, Line b) {
			return doIntersect(a.point1, a.point2, b.point1, b.point2);
		}
		
		@Override
		public String toString() {
			return "[" + this.point1.toString() + "," + this.point2.toString() + "]";
		}
		
	}
	
	public static class Point {
        int x, y;
        
        public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
        }
        
        @Override
        public String toString() {
        	return "(" + this.x + "," + this.y + ")";
        }
    }
	
	public static class Rectangle {
		
		public Point topLeftPoint, bottomRightPoint, topRightPoint, bottomLeftPoint;
		public Line topLine, bottomLine, leftLine, rightLine;
		
		public Rectangle(Point topLeft, Point bottomRight) {
			this.topLeftPoint = topLeft;
			this.bottomRightPoint = bottomRight;
			this.topRightPoint = new Point(bottomRight.x, topLeft.y);
			this.bottomLeftPoint = new Point(topLeft.x, bottomRight.y);
			
			this.topLine = new Line(this.topLeftPoint, this.topRightPoint);
			this.bottomLine = new Line(this.bottomLeftPoint, this.bottomRightPoint);
			this.rightLine = new Line(this.bottomRightPoint, this.topRightPoint);
			this.leftLine = new Line(this.bottomLeftPoint, this.topLeftPoint);
		}
		
		@Override
		public String toString() {
			String out = "";
			out = out + " " + topLeftPoint + "----------" + topRightPoint + "\n"; 
			out = out + "  |                 |\n";
			out = out + "  |                 |\n";
			out = out + "  |                 |\n";
			out = out + "  |                 |\n";
			out = out + " " + bottomLeftPoint + "----------" + bottomRightPoint + "\n";
			return  out;
		}
		
		// returns lines array [Top, Left, Bottom, Right]
		public Line[] getAllLines() {
			Line[] lines = {
					this.topLine,
					this.leftLine,
					this.bottomLine,
					this.rightLine,
			};
			return lines;
		}
	}
	
	public static  boolean doOverlap(Rectangle rectangle1, Rectangle rectangle2) {
        

		if (rectangle1.topLeftPoint.x > rectangle2.bottomRightPoint.x // rectangle1 is right to rectangle2
			|| rectangle1.bottomRightPoint.x < rectangle2.topLeftPoint.x // rectangle1 is left to rectangle2
			|| rectangle1.topLeftPoint.y < rectangle2.bottomRightPoint.y // rectangle1 is above rectangle2
			|| rectangle1.bottomRightPoint.y > rectangle2.topLeftPoint.y) // rectangle1 is below rectangle2 
		{ 
			// no intersection
			// no containment
			// no adjacent
			System.out.println("no intersection - no containment - no adjacent");
			return false; 
		} else { 
			
			
			if (isContainment(rectangle1, rectangle2) || isContainment(rectangle2, rectangle1)) {
				// containment
				System.out.println("containment");
			} else {
				if(isAdjacent(rectangle1, rectangle2)) {
					//adjacent (sub-line/proper/partial)
					
				} else {
					//  intersection
					//	intersection - no containment
					printIntersections(rectangle1, rectangle2);
				}
			}
			
			return true; 
		}
		
	}
	
	public static void printIntersections(Rectangle rectangle1, Rectangle rectangle2) {
		if (Line.doIntersect(rectangle1.topLine, rectangle2.leftLine)  ) {
			System.out.println("rectangle 1 top line intersect with rectangle 2 left line at : " + new Point(rectangle2.leftLine.point1.x, rectangle1.topLine.point1.y));
		}
		
		if (Line.doIntersect(rectangle1.topLine, rectangle2.rightLine)  ) {
			System.out.println("rectangle 1 top line intersect with rectangle 2 right line at : " + new Point(rectangle2.rightLine.point1.x, rectangle1.topLine.point1.y));
		}
		
		if (Line.doIntersect(rectangle1.bottomLine, rectangle2.leftLine)  ) {
			System.out.println("rectangle 1 bottom line intersect with rectangle 2 left line at : " + new Point(rectangle2.leftLine.point1.x, rectangle1.bottomLine.point1.y));
		}
		
		if (Line.doIntersect(rectangle1.bottomLine, rectangle2.rightLine)  ) {
			System.out.println("rectangle 1 bottom line intersect with rectangle 2 right line at : " + new Point(rectangle2.rightLine.point1.x, rectangle1.bottomLine.point1.y));
		}
		
		
		if (Line.doIntersect(rectangle1.leftLine, rectangle2.topLine)  ) {
			System.out.println("rectangle 1 left line intersect with rectangle 2 top line at : " + new Point(rectangle1.leftLine.point1.x, rectangle2.topLine.point1.y));
		}
		
		if (Line.doIntersect(rectangle1.leftLine, rectangle2.bottomLine)  ) {
			System.out.println("rectangle 1 left line intersect with rectangle 2 bottom line at : " + new Point(rectangle1.leftLine.point1.x, rectangle2.bottomLine.point1.y));
		}
		
		if (Line.doIntersect(rectangle1.rightLine, rectangle2.topLine)  ) {
			System.out.println("rectangle 1 right line intersect with rectangle 2 top line at : " + new Point(rectangle1.rightLine.point1.x, rectangle2.topLine.point1.y));
		}
		
		if (Line.doIntersect(rectangle1.rightLine, rectangle2.bottomLine)  ) {
			System.out.println("rectangle 1 right line intersect with rectangle 2 bottom line at : " + new Point(rectangle1.rightLine.point1.x, rectangle2.bottomLine.point1.y));
		}
	}
	
	public static boolean isAdjacent(Rectangle rectangle1, Rectangle rectangle2) {
		//adjacent (sub-line/proper/partial)
		if(rectangle1.leftLine.point1.x == rectangle2.rightLine.point1.x) {
			System.out.println("rectangle1 left side is adjacent to rectangle2 right side");
			printAdjacentType(rectangle1.leftLine, rectangle2.rightLine);
			return true;
		} else if(rectangle1.rightLine.point1.x == rectangle2.leftLine.point1.x) {
			System.out.println("rectangle1 right side is adjacent to rectangle2 left side");
			printAdjacentType(rectangle1.rightLine, rectangle2.leftLine);
			return true;
		} else if(rectangle1.bottomLine.point1.y == rectangle2.topLine.point1.y) {
			System.out.println("rectangle1 bottom side is adjacent to rectangle2 top side");
			printAdjacentType(rectangle1.bottomLine, rectangle2.topLine);
			return true;
		} else if(rectangle1.topLine.point1.y == rectangle2.bottomLine.point1.y) {
			System.out.println("rectangle1 top side is adjacent to rectangle2 bottom side");
			printAdjacentType(rectangle1.topLine, rectangle2.bottomLine);
			return true;
		} 
		
		return false;
		
	}
	
	public static void printAdjacentType(Line a, Line b) {
		if(isProperAdjacent(a, b)) {
			System.out.println("proper adjacent");
		} else if(isSubLine(a, b)) {
			System.out.println("sub-line adjacent");
		} else {
			System.out.println("partial adjacent");
		}
	}
	
	public static boolean isSubLine(Line a, Line b) {
		Line smaller, larger;
		
		if(a.length() < b.length()) {
			smaller = a;
			larger = b;
		} else {
			smaller = b;
			larger = a;
		}
		
		
		if(a.point1.x == a.point2.x) {
			// vertical line
			return ((smaller.point1.y >= larger.point1.y && smaller.point2.y <= larger.point2.y));	
		} else {
			return ((smaller.point1.x >= larger.point1.x && smaller.point2.x <= larger.point2.x));
		}
	}
	
	public static boolean isProperAdjacent(Line a, Line b) {
		return ((a.point1.x == b.point1.x && a.point1.y == b.point1.y &&
				a.point2.x == b.point2.x && a.point2.y == b.point2.y) || 
				(a.point1.x == b.point2.x && a.point1.y == b.point2.y &&
				a.point2.x == b.point1.x && a.point2.y == b.point1.y));
	}
	
	// is rectangle 1 inside rectangle 2 
	public static boolean isContainment(Rectangle rectangle1, Rectangle rectangle2) {
		return (rectangle1.topLeftPoint.x >= rectangle2.topLeftPoint.x && 
				rectangle1.bottomRightPoint.x <= rectangle2.bottomRightPoint.x &&
				rectangle1.topLeftPoint.y <= rectangle2.topLeftPoint.y &&
				rectangle1.bottomRightPoint.y >= rectangle2.bottomRightPoint.y);
	}

	public static void main(String[] args) {
		
		Point l1 = new Point(0, 10),r1 = new Point(10, 0);
		Point l2 = new Point(2, 15),r2 = new Point(8, 1);
		Rectangle rectangle1 = new Rectangle(l1, r1), rectangle2 = new Rectangle(l2, r2);
		
		System.out.println("rectangle 1: \n\n" + rectangle1 );
		System.out.println("rectangle 2: \n\n" + rectangle2 );
        
        if (doOverlap(rectangle1, rectangle2)) {
            System.out.println("Rectangles Overlap");
        } else {
            System.out.println("Rectangles Don't Overlap");
        }
	}
	
	

}
