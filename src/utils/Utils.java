package utils;

import com.jme3.math.Vector3f;

public class Utils {

	
	public static boolean onDestination(Vector3f a, Vector3f b) {
		return approximativeEquals(a.x, b.x) && approximativeEquals(a.z, b.z);
	}
	
	public static boolean approximativeEquals(float a, float b) {
		return Math.abs(b - a) <= 3;
	}
	
	public static float distance(Vector3f a, Vector3f b) {
		Vector3f abis = a.clone();
		Vector3f bbis = b.clone();
		abis.y = bbis.y = 0;
		return abis.distance(bbis);
	}
}
