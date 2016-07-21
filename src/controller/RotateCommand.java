/*
   This file is part of j2dcg.
   j2dcg is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   j2dcg is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with j2dcg; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package controller;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.List;

import model.Shape;

/**
 * <p>Title: RotateCommand</p>
 * <p>Description: This is the class to execute de rotate command.</p>
 * <p>Copyright: Copyright (c) 2004 Jean-Fran�ois Barras, �ric Paquette</p>
 * <p>Company: (�TS) - �cole de Technologie Sup�rieure</p>
 * <p>Created on: 2004-03-19</p>
 * @version $Revision: 1.2 $
 */
public class RotateCommand extends AnchoredTransformationCommand {

	/**
	 * @param thetaDegrees the angle of (counter-clockwise) rotation in degrees
	 * @param anchor one of the predefined positions for the anchor point
	 */
	public RotateCommand(double thetaDegrees,
						 int anchor,
						 List<?> aObjects) {
		super(anchor);
		this.thetaDegrees = thetaDegrees;
		objects = aObjects;
	}
	
	/* (non-Javadoc)
	 * @see controller.Command#execute()
	 */
	public void execute() {
		System.out.println("command: rotate " + thetaDegrees +
                           " degrees around " + getAnchor() + ".");

		Iterator<?> i = objects.iterator();
		Shape shape;
		Point anchor;
		while(i.hasNext()) {
			shape = (Shape)i.next();
			anchor = this.getAnchorPoint(shape);
			
			mt.addMememto(shape);
			
			AffineTransform transform = shape.getAffineTransform();
			transform.rotate(Math.toRadians(thetaDegrees), anchor.getX(), anchor.getY());
			shape.setAffineTransform(transform);
		}
	}

	/* (non-Javadoc)
	 * @see controller.Command#undo()
	 */
	public void undo() {
		mt.setBackMementos();
	}

	private MementoTracker mt = new MementoTracker();
	private List<?> objects;
	private double thetaDegrees;
	
}