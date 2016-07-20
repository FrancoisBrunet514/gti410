package model;

import java.awt.Point;
import java.util.List;

public class HermiteCurveType extends CurveType {

	public HermiteCurveType(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see model.CurveType#getNumberOfSegments(int)
	 */
	public int getNumberOfSegments(int numberOfControlPoints) {
		if (numberOfControlPoints >= 4) {
			return (int) Math.floor(numberOfControlPoints/4);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see model.CurveType#getNumberOfControlPointsPerSegment()
	 */
	public int getNumberOfControlPointsPerSegment() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see model.CurveType#getControlPoint(java.util.List, int, int)
	 */
	public ControlPoint getControlPoint(List controlPoints,
										int segmentNumber, int controlPointNumber) {
		int controlPointIndex = (segmentNumber * 4) + controlPointNumber;
	    return (ControlPoint)controlPoints.get(controlPointIndex);
	}

	/* (non-Javadoc)
	 * @see model.CurveType#evalCurveAt(java.util.List, double)
	 */
	public Point evalCurveAt(List controlPoints, double t) {
		List tVector = Matrix.buildRowVector4(t*t*t, t*t, t, 1);
		Point p1 = ((ControlPoint)controlPoints.get(0)).getCenter();
		Point p2 = ((ControlPoint)controlPoints.get(1)).getCenter();
		Point p3 = ((ControlPoint)controlPoints.get(2)).getCenter();
		Point p4 = ((ControlPoint)controlPoints.get(3)).getCenter();
		Point v1 = new Point(p2.x - p1.x, p2.y - p1.y);
		Point v2 = new Point(p4.x - p3.x, p4.y - p3.y);
		List gVector = Matrix.buildColumnVector4(p1, p4, v1, v2);
		Point p = Matrix.eval(tVector, matrix, gVector);
		return p;
	}

	private List hermitMatrix = 
		Matrix.buildMatrix4( 2, -2,  1,  1, 
						    -3,  3, -2, -1, 
							 0,  0,  1,  0, 
							 1,  0,  0,  0);
							 
	private List matrix = hermitMatrix;
}
