/*package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Gyro;

public class CurvedPath extends CurvedDrive {
	private Cubic[] xPath, yPath;
	private double[] lengths;

	public CurvedPath(TankDrive drive, Gyro gyro, PathPoint[] path) {
		super(drive, gyro);
		PathPoint.convertAbsoluteToRelative(path);
		if (path[0].getDistance() != 0)
			throw new IllegalArgumentException("The first point in the path must be the origin");
		double[][] xPathPoints = new double[path.length][2], yPathPoints = new double[path.length][2];
		lengths = new double[path.length - 1];
		double totalDistance = 0;
		for (int i = 0; i < path.length; ++i) {
			PathPoint p = path[i];
			xPathPoints[i][0] = totalDistance;
			xPathPoints[i][1] = p.getX();
			yPathPoints[i][0] = totalDistance;
			yPathPoints[i][1] = p.getY();
			totalDistance += p.getDistance();
			if (i != 0)
				lengths[i - 1] = totalDistance;
		}
		xPath = interpolateCubics(xPathPoints);
		yPath = interpolateCubics(yPathPoints);
	}

	private class Cubic {
		double a, b, c, d;

		Cubic(double[] coeff) {
			a = coeff[0];
			b = coeff[1];
			c = coeff[2];
			d = coeff[3];
		}

		double evaluate(double x) {
			return a * x * x * x + b * x * x + c * x + d;
		}

		double evalDeriv(double x) {
			return 3 * a * x * x + 2 * b * x + c;
		}

		double evalDeriv2(double x) {
			return 6 * a * x + 2 * b;
		}

		@Override
		public String toString() {
			return String.format("%.2ft^3 + %.2ft^2 + %.2ft + %.2f", a, b, c, d);
		}
	}

	private Cubic[] interpolateCubics(double[][] pts) {
		double slope = (pts[1][1] - pts[0][1]) / (pts[1][0] - pts[0][0]);
		Cubic[] cubics = new Cubic[pts.length - 1];
		int i;
		for (i = 0; i < pts.length - 2; ++i) {
			double x = pts[i][0], x2 = pts[i + 1][0], x3 = pts[i + 2][0];
			double[][] matr = new double[][] { { 3 * x * x, 2 * x, 1, 0, slope }, { x * x * x, x * x, x, 1, pts[i][1] },
					{ x2 * x2 * x2, x2 * x2, x2, 1, pts[i + 1][1] }, { x3 * x3 * x3, x3 * x3, x3, 1, pts[i + 2][1] } };
			cubics[i] = new Cubic(solveSystem(matr));
			slope = 3 * x2 * x2 * cubics[i].a + 2 * x2 * cubics[i].b + cubics[i].c;
		}
		double x = pts[i][0], x2 = pts[i + 1][0];
		double finalSlope = (pts[i + 1][1] - pts[i][1]) / (x2 - x);
		double[][] finalMatrix = new double[][] { { 3 * x * x, 2 * x, 1, 0, slope },
				{ x * x * x, x * x, x, 1, pts[i][1] }, { x2 * x2 * x2, x2 * x2, x2, 1, pts[i + 1][1] },
				{ 3 * x2 * x2, 2 * x2, 1, 0, finalSlope } };
		cubics[i] = new Cubic(solveSystem(finalMatrix));
		return cubics;
	}

	private double[] solveSystem(double[][] mat) {
		int n = mat.length;
		if (mat[0].length != (n + 1))
			throw new Error("Cannot solve system");
		for (int k = 0; k < n; ++k) {
			int maxCoeff = k;
			for (int i = k + 1; i < n; ++i) {
				if (Math.abs(mat[i][k]) > Math.abs(mat[maxCoeff][k]))
					maxCoeff = i;
			}
			double[] tmp = mat[k];
			mat[k] = mat[maxCoeff];
			mat[maxCoeff] = tmp;
			if (mat[k][k] == 0)
				throw new Error("Cannot solve system; no or infinitely many solutions exist");
			for (int i = k + 1; i < n; ++i) {
				double f = mat[i][k] / mat[k][k];
				for (int j = k + 1; j < n + 1; ++j) {
					mat[i][j] -= mat[k][j] * f;
				}
				mat[i][k] = 0;
			}
		}
		// mat is now in row-echelon
		// get solutions
		double[] solutions = new double[n];
		for (int k = 0; k < n; ++k) {
			int ind = n - k - 1;
			double t = mat[ind][n];
			for (int j = 0; j < k; ++j) {
				t -= mat[ind][n - j - 1] * solutions[n - j - 1];
			}
			solutions[ind] = t / mat[ind][ind];
		}
		return solutions;
	}

	// We cache one because it will often be called several (around 6) times, no
	// use fetching index every single time
	private double cachedLength = 0;
	private int cachedIndex = 0;

	private int getIndexOfCubic() {
		if (x == cachedLength)
			return cachedIndex;
		for (int i = 0; i < lengths.length; ++i) {
			if (x <= lengths[i]) {
				cachedLength = x;
				cachedIndex = i;
				return i;
			}
		}
		return -1;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < lengths.length; ++i) {
			result.append(String.format("Up to %.2f: x=", lengths[i]));
			result.append(xPath[i].toString() + " y=" + yPath[i].toString() + "\n");
		}
		return result.toString();
	}
}*/
