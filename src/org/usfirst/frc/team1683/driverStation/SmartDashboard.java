package org.usfirst.frc.team1683.driverStation;

public class SmartDashboard {

	public SmartDashboard() {
	}

	/**
	 * Sends the value to SmartDashboard
	 *
	 * @param key
	 *            Value name
	 * @param val
	 *            double Input value
	 * @param isForDriver
	 *            true if you want to driver to see this value
	 */
	public static void sendData(String key, double val, boolean isForDriver) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Sends the value to SmartDashboard
	 *
	 * @param key
	 *            Value name
	 * @param val
	 *            int Input value
	 * @param isForDriver
	 *            true if you want to driver to see this value
	 */
	public static void sendData(String key, int val, boolean isForDriver) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Sends the value to SmartDashboard
	 *
	 * @param key
	 *            Value name
	 * @param val
	 *            String Input value
	 * @param isForDriver
	 *            true if you want to driver to see this value
	 */
	public static void sendData(String key, String val, boolean isForDriver) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Sends the value to SmartDashboard
	 *
	 * @param key
	 *            Value name
	 * @param val
	 *            boolean Input value
	 * @param isForDriver
	 *            true if you want to driver to see this value
	 */
	public static void sendData(String key, boolean val, boolean isForDriver) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Sends the integer to be stored in Preferences in the roboRIO
	 *
	 * @param key
	 *            Name of the value
	 * @param val
	 *            int Value to be sent
	 */
	public static void prefInt(String key, int val) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Sends the boolean to be stored in Preferences in the roboRIO
	 *
	 * @param key
	 *            Name of the value
	 * @param val
	 *            boolean Value to be sent
	 */
	public static void prefBoolean(String key, boolean val) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Sends the String to be stored in Preferences in the roboRIO
	 *
	 * @param key
	 *            Name of the value
	 * @param val
	 *            String Value to be sent
	 */
	public static void prefString(String key, String val) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Sends the double to be stored in Preferences in the roboRIO
	 *
	 * @param key
	 *            Name of the value
	 * @param val
	 *            double value to be sent
	 */
	public static void prefDouble(String key, double val) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/*
	 * Methods to get data from the roboRIO
	 */

	/**
	 * Receives the value from roboRIO Default is false if no boolean value is
	 * found with the key
	 *
	 * @param key
	 *            Value name
	 */
	public static boolean getBoolean(String key) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Receives the value from roboRIO Default is "null" if no string is found
	 * with the key
	 *
	 * @param key
	 *            Value name
	 * @return null if no string is found
	 */
	public static String getString(String key) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Receives the value from roboRIO Default is 0.0 if no double is found with
	 * the key
	 *
	 * @param key
	 *            Value name
	 * @return 0.0 if key is not found
	 */
	public static double getDouble(String key) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Receives the value from roboRIO Default is 0 if no integer found with the
	 * key
	 *
	 * @param key
	 *            Value name
	 * @return 0 if key is not found
	 */
	public static int getInt(String key) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
