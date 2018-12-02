package cassone.calcolo;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Matematica {

	public static double get_Area_Yg(Area a) {
		double[] co = new double[6];
		int ret;
		ArrayList<double[]> coord = new ArrayList<double[]>();

		if(a.isEmpty()) return 0;
		
		Rectangle2D r = a.getBounds();
		AffineTransform at = new AffineTransform();
		at.setToTranslation(-r.getMinX(), -r.getMinY());
		PathIterator pi = a.getPathIterator(at);

		do {
			ret = pi.currentSegment(co);
			switch (ret) {
			case PathIterator.SEG_MOVETO: {
				double[] point = new double[] {co[0],co[1]};
				coord.add(point);
			}break;
			case PathIterator.SEG_LINETO: {
				double[] point = new double[] {co[0],co[1]};
				coord.add(point);
			}break;
			case PathIterator.SEG_QUADTO: {
				double[] point = new double[] {co[2],co[3]};
				coord.add(point);
			}break;
			case PathIterator.SEG_CUBICTO: {
				double[] point = new double[] {co[4],co[5]};
				coord.add(point);
			}break;
			case PathIterator.SEG_CLOSE: {
				double[] p = coord.get(0);
				double[] point = new double[] {p[0],p[1]};
				coord.add(point);
			}
				break;
			}
			pi.next();
		} while (!pi.isDone());

		double f =0;
		for (int i = 0; i < coord.size()-1; i++) {
			double[] xi = coord.get(i);
			double[] xi_1 = coord.get(i+1);
			f+=xi[0]*xi_1[1]-xi_1[0]*xi[1];
		}
		
		return Math.abs(f/2);
	}

}
