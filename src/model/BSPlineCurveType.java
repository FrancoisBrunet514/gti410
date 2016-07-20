package model;

import java.awt.Point;
import java.util.List;

public class BSPlineCurveType extends CurveType {

	public BSPlineCurveType(String name) {
		super(name);
	}

	@Override
	public int getNumberOfSegments(int numberOfControlPoints) {
		if (numberOfControlPoints >= 4) {
			return numberOfControlPoints - 3;
		} else {
			return 0;
		}
	}

	@Override
	public int getNumberOfControlPointsPerSegment() {
		return 4;
	}

	@Override
	public ControlPoint getControlPoint(List controlPoints, int segmentNumber, int controlPointNumber) {
		int controlPointIndex = segmentNumber + controlPointNumber;
		return (ControlPoint)controlPoints.get(controlPointIndex);
	}

	@Override
	public Point evalCurveAt(List controlPoints, double t) {
		List tVector = Matrix.buildRowVector4(t*t*t, t*t, t, 1);
		
		List gVector = Matrix.buildColumnVector4(((ControlPoint)controlPoints.get(0)).getCenter(), 
			((ControlPoint)controlPoints.get(1)).getCenter(), 
			((ControlPoint)controlPoints.get(2)).getCenter(),
			((ControlPoint)controlPoints.get(3)).getCenter());
		Point p = Matrix.eval(tVector, matrix, gVector);
		return p;
	}
	
	private List bsplineMatrix = 
			Matrix.buildMatrix4(-1.0/6.0,  3.0/6.0, -3.0/6.0, 1.0/6.0, 
								 3.0/6.0, -6.0/6.0,  3.0/6.0, 0.0/6.0, 
								-3.0/6.0,  0.0/6.0,  3.0/6.0, 0.0/6.0, 
								 1.0/6.0,  4.0/6.0,  1.0/6.0, 0/6.0);
								 
		private List matrix = bsplineMatrix;

}
