package model;

import java.awt.Point;
import java.util.List;

public class BSPlineCurveType extends CurveType {

	public BSPlineCurveType(String name) {
		super(name);
	}

	@Override
	public int getNumberOfSegments(int numberOfControlPoints) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfControlPointsPerSegment() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ControlPoint getControlPoint(List controlPoints, int segmentNumber, int controlPointNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point evalCurveAt(List controlPoints, double t) {
		// TODO Auto-generated method stub
		return null;
	}

}
