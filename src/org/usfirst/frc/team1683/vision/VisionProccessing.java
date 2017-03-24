package org.usfirst.frc.team1683.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class VisionProccessing {
	private static boolean USE_GENERATED_HISTOGRAM = false;
	private Mat hslThresholdOutput = new Mat(),
				sourceRoi = new Mat(),
				hslImage = new Mat();
	private int processFrames;
	// TODO: Tune these
	private final double[] lowerBound = {17, 91, 75}; // hls lower bound
	private final double[] upperBound = {90, 255, 255}; // hls upper bound
	// TODO: Tune this as well
	Rect roi = new Rect(400, 200, 100, 100); // region of interest, form (x, y, width, height) SHOULD TUNE!
	private static final double AVERAGE_PIXEL_THRESHOLD = 0.8; // fraction of pixels that need to be in the range for gear to be counted
	public void process(Mat source) {
		processFrames += 1;
		sourceRoi = source.submat(roi);
		if (!USE_GENERATED_HISTOGRAM || processFrames < 20) {
			Imgproc.cvtColor(sourceRoi, hslImage, Imgproc.COLOR_BGR2HLS);
			Core.inRange(hslImage, new Scalar(lowerBound), new Scalar(upperBound), hslThresholdOutput);
			// If the number of non-zero (white, inside the range) pixels is bigger than 0.8*10000
			if (Core.countNonZero(hslThresholdOutput) > AVERAGE_PIXEL_THRESHOLD * roi.area()) {
				// gear is seen
			} else {
				// gear is not seen
			}
		} else {
			// use a histogram on autonomous startup?
		}
	}
}
