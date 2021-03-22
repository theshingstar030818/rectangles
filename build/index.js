"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Rectangle = exports.Line = exports.Point = void 0;
console.log('Hello rectangles!');
var Point = /** @class */ (function () {
    function Point(x, y) {
        this.x = x;
        this.y = y;
    }
    Point.prototype.toString = function () {
        return "(" + this.x + "," + this.y + ")";
    };
    return Point;
}());
exports.Point = Point;
var Line = /** @class */ (function () {
    function Line(p1, p2) {
        this.point1 = p1;
        this.point2 = p2;
    }
    Line.prototype.length = function () {
        return Math.sqrt((this.point2.y - this.point1.y) * (this.point2.y - this.point1.y) + (this.point2.x - this.point1.x) * (this.point2.x - this.point1.x));
    };
    Line.onSegment = function (p, q, r) {
        return (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
            q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y));
    };
    Line.orientation = function (p, q, r) {
        var val = (q.y - p.y) * (r.x - q.x) -
            (q.x - p.x) * (r.y - q.y);
        if (val == 0)
            return 0;
        return (val > 0) ? 1 : 2;
    };
    Line.doPointsIntersect = function (p1, q1, p2, q2) {
        // Find the four orientations needed for general and 
        // special cases 
        var o1 = this.orientation(p1, q1, p2);
        var o2 = this.orientation(p1, q1, q2);
        var o3 = this.orientation(p2, q2, p1);
        var o4 = this.orientation(p2, q2, q1);
        // General case 
        if (o1 != o2 && o3 != o4)
            return true;
        // Special Cases 
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1 
        if (o1 == 0 && this.onSegment(p1, p2, q1))
            return true;
        // p1, q1 and q2 are colinear and q2 lies on segment p1q1 
        if (o2 == 0 && this.onSegment(p1, q2, q1))
            return true;
        // p2, q2 and p1 are colinear and p1 lies on segment p2q2 
        if (o3 == 0 && this.onSegment(p2, p1, q2))
            return true;
        // p2, q2 and q1 are colinear and q1 lies on segment p2q2 
        if (o4 == 0 && this.onSegment(p2, q1, q2))
            return true;
        return false; // Doesn't fall in any of the above cases 
    };
    Line.doIntersect = function (a, b) {
        return this.doPointsIntersect(a.point1, a.point2, b.point1, b.point2);
    };
    Line.prototype.toString = function () {
        return "[" + this.point1.toString() + "," + this.point2.toString() + "]";
    };
    return Line;
}());
exports.Line = Line;
var Rectangle = /** @class */ (function () {
    function Rectangle(topLeft, bottomRight) {
        this.topLeftPoint = topLeft;
        this.bottomRightPoint = bottomRight;
        this.topRightPoint = new Point(bottomRight.x, topLeft.y);
        this.bottomLeftPoint = new Point(topLeft.x, bottomRight.y);
        this.topLine = new Line(this.topLeftPoint, this.topRightPoint);
        this.bottomLine = new Line(this.bottomLeftPoint, this.bottomRightPoint);
        this.rightLine = new Line(this.bottomRightPoint, this.topRightPoint);
        this.leftLine = new Line(this.bottomLeftPoint, this.topLeftPoint);
    }
    Rectangle.prototype.toString = function () {
        var out = "";
        out = out + " " + this.topLeftPoint + "----------" + this.topRightPoint + "\n";
        out = out + "  |                 |\n";
        out = out + "  |                 |\n";
        out = out + "  |                 |\n";
        out = out + "  |                 |\n";
        out = out + " " + this.bottomLeftPoint + "----------" + this.bottomRightPoint + "\n";
        return out;
    };
    // returns lines array [Top, Left, Bottom, Right]
    Rectangle.prototype.getAllLines = function () {
        var lines = [
            this.topLine,
            this.leftLine,
            this.bottomLine,
            this.rightLine,
        ];
        return lines;
    };
    Rectangle.doOverlap = function (rectangle1, rectangle2) {
        if (rectangle1.topLeftPoint.x > rectangle2.bottomRightPoint.x // rectangle1 is right to rectangle2
            || rectangle1.bottomRightPoint.x < rectangle2.topLeftPoint.x // rectangle1 is left to rectangle2
            || rectangle1.topLeftPoint.y < rectangle2.bottomRightPoint.y // rectangle1 is above rectangle2
            || rectangle1.bottomRightPoint.y > rectangle2.topLeftPoint.y) // rectangle1 is below rectangle2 
         {
            // no intersection
            // no containment
            // no adjacent
            console.log("no intersection - no containment - no adjacent");
            return false;
        }
        else {
            if (Rectangle.isContainment(rectangle1, rectangle2) || Rectangle.isContainment(rectangle2, rectangle1)) {
                // containment
                console.log("containment");
            }
            else {
                if (Rectangle.isAdjacent(rectangle1, rectangle2)) {
                    //adjacent (sub-line/proper/partial)
                }
                else {
                    //  intersection
                    //	intersection - no containment
                    Rectangle.printIntersections(rectangle1, rectangle2);
                }
            }
            return true;
        }
    };
    Rectangle.printIntersections = function (rectangle1, rectangle2) {
        if (Line.doIntersect(rectangle1.topLine, rectangle2.leftLine)) {
            console.log("rectangle 1 top line intersect with rectangle 2 left line at : " + new Point(rectangle2.leftLine.point1.x, rectangle1.topLine.point1.y));
        }
        if (Line.doIntersect(rectangle1.topLine, rectangle2.rightLine)) {
            console.log("rectangle 1 top line intersect with rectangle 2 right line at : " + new Point(rectangle2.rightLine.point1.x, rectangle1.topLine.point1.y));
        }
        if (Line.doIntersect(rectangle1.bottomLine, rectangle2.leftLine)) {
            console.log("rectangle 1 bottom line intersect with rectangle 2 left line at : " + new Point(rectangle2.leftLine.point1.x, rectangle1.bottomLine.point1.y));
        }
        if (Line.doIntersect(rectangle1.bottomLine, rectangle2.rightLine)) {
            console.log("rectangle 1 bottom line intersect with rectangle 2 right line at : " + new Point(rectangle2.rightLine.point1.x, rectangle1.bottomLine.point1.y));
        }
        if (Line.doIntersect(rectangle1.leftLine, rectangle2.topLine)) {
            console.log("rectangle 1 left line intersect with rectangle 2 top line at : " + new Point(rectangle1.leftLine.point1.x, rectangle2.topLine.point1.y));
        }
        if (Line.doIntersect(rectangle1.leftLine, rectangle2.bottomLine)) {
            console.log("rectangle 1 left line intersect with rectangle 2 bottom line at : " + new Point(rectangle1.leftLine.point1.x, rectangle2.bottomLine.point1.y));
        }
        if (Line.doIntersect(rectangle1.rightLine, rectangle2.topLine)) {
            console.log("rectangle 1 right line intersect with rectangle 2 top line at : " + new Point(rectangle1.rightLine.point1.x, rectangle2.topLine.point1.y));
        }
        if (Line.doIntersect(rectangle1.rightLine, rectangle2.bottomLine)) {
            console.log("rectangle 1 right line intersect with rectangle 2 bottom line at : " + new Point(rectangle1.rightLine.point1.x, rectangle2.bottomLine.point1.y));
        }
    };
    Rectangle.isAdjacent = function (rectangle1, rectangle2) {
        //adjacent (sub-line/proper/partial)
        if (rectangle1.leftLine.point1.x == rectangle2.rightLine.point1.x) {
            console.log("rectangle1 left side is adjacent to rectangle2 right side");
            this.printAdjacentType(rectangle1.leftLine, rectangle2.rightLine);
            return true;
        }
        else if (rectangle1.rightLine.point1.x == rectangle2.leftLine.point1.x) {
            console.log("rectangle1 right side is adjacent to rectangle2 left side");
            this.printAdjacentType(rectangle1.rightLine, rectangle2.leftLine);
            return true;
        }
        else if (rectangle1.bottomLine.point1.y == rectangle2.topLine.point1.y) {
            console.log("rectangle1 bottom side is adjacent to rectangle2 top side");
            this.printAdjacentType(rectangle1.bottomLine, rectangle2.topLine);
            return true;
        }
        else if (rectangle1.topLine.point1.y == rectangle2.bottomLine.point1.y) {
            console.log("rectangle1 top side is adjacent to rectangle2 bottom side");
            this.printAdjacentType(rectangle1.topLine, rectangle2.bottomLine);
            return true;
        }
        return false;
    };
    Rectangle.printAdjacentType = function (a, b) {
        if (this.isProperAdjacent(a, b)) {
            console.log("proper adjacent");
        }
        else if (this.isSubLine(a, b)) {
            console.log("sub-line adjacent");
        }
        else {
            console.log("partial adjacent");
        }
    };
    Rectangle.isSubLine = function (a, b) {
        var smaller;
        var larger;
        if (a.length() < b.length()) {
            smaller = a;
            larger = b;
        }
        else {
            smaller = b;
            larger = a;
        }
        if (a.point1.x == a.point2.x) {
            // vertical line
            return ((smaller.point1.y >= larger.point1.y && smaller.point2.y <= larger.point2.y));
        }
        else {
            return ((smaller.point1.x >= larger.point1.x && smaller.point2.x <= larger.point2.x));
        }
    };
    Rectangle.isProperAdjacent = function (a, b) {
        return ((a.point1.x == b.point1.x && a.point1.y == b.point1.y &&
            a.point2.x == b.point2.x && a.point2.y == b.point2.y) ||
            (a.point1.x == b.point2.x && a.point1.y == b.point2.y &&
                a.point2.x == b.point1.x && a.point2.y == b.point1.y));
    };
    // is rectangle 1 inside rectangle 2 
    Rectangle.isContainment = function (rectangle1, rectangle2) {
        return (rectangle1.topLeftPoint.x >= rectangle2.topLeftPoint.x &&
            rectangle1.bottomRightPoint.x <= rectangle2.bottomRightPoint.x &&
            rectangle1.topLeftPoint.y <= rectangle2.topLeftPoint.y &&
            rectangle1.bottomRightPoint.y >= rectangle2.bottomRightPoint.y);
    };
    return Rectangle;
}());
exports.Rectangle = Rectangle;
var l1 = new Point(0, 10), r1 = new Point(10, 0);
var l2 = new Point(2, 15), r2 = new Point(8, 1);
var rectangle1 = new Rectangle(l1, r1), rectangle2 = new Rectangle(l2, r2);
console.log("rectangle 1: \n\n" + rectangle1);
console.log("rectangle 2: \n\n" + rectangle2);
if (Rectangle.doOverlap(rectangle1, rectangle2)) {
    console.log("Rectangles Overlap");
}
else {
    console.log("Rectangles Don't Overlap");
}
