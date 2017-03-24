package org.usfirst.frc.team1683.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class VisionProccessing {
	private static boolean USE_GENERATED_HISTOGRAM = false;
	private Mat hslThresholdOutput = new Mat();
	private int processFrames;
	// TODO: Tune these
	private final double[] lowerBound = {17, 91, 75}; // hls lower bound
	private final double[] upperBound = {90, 255, 255}; // hls upper bound
	// TODO: Tune this as well
	Rect roi = new Rect(400, 200, 100, 100); // region of interest, form (x, y, width, height) SHOULD TUNE!
	
	public void process(Mat source) {
		processFrames += 1;
		source = source.submat(roi);
		if (!USE_GENERATED_HISTOGRAM || processFrames < 20) {
			Imgproc.cvtColor(source, hslThresholdOutput, Imgproc.COLOR_BGR2HLS);
			Core.inRange(hslThresholdOutput, new Scalar(lowerBound), new Scalar(upperBound), hslThresholdOutput);
		} else {
			// use a histogram on autonomous startup
		}
	}
}
