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
	
	public static int getLegalAction(Vector3f self, Vector3f target){
		Vector3f north = new Vector3f(0, 0, 1);
		Vector3f tempTarget = new Vector3f(target.x, 0, target.z);
		Vector3f tempSelf = new Vector3f(self.x, 0, self.z);
		Vector3f orientation = tempTarget.subtract(tempSelf);

		float angle = north.angleBetween(orientation);
		
		System.out.println("Angle : " + ((angle) * 180 / Math.PI));
		
		if (Math.abs(angle) > 7 * Math.PI / 8)
			return 5;
		else if (angle < -5 * Math.PI / 8)
			return 6;
		else if (angle < -3 * Math.PI / 8)
			return 7;
		else if (angle < -Math.PI / 8)
			return 8;
		else if (Math.abs(angle) < Math.PI / 8)
			return 1;
		else if (angle < 3 * Math.PI / 8)
			return 2;
		else if (angle < 5 * Math.PI / 8)
			return 3;
		else
			return 4;
		
		//return angle 
	}
}
