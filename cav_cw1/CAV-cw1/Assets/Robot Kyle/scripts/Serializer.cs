using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Serializer {

	public static Transform[] KinematicChainTransforms(Transform leaf, int limit) {
		// Get the total number of links in the chain
		int n_links = 1; // we have the leaf at least
		Transform current = leaf;
		Transform temp;

		while(current.parent != null && n_links < limit) {
			n_links += 1;
			temp = current;
			current = temp.parent;
		}

		Transform[] chain = new Transform[n_links];
		chain[0] = leaf;
		int i = 0;
		current = leaf;

		while(current.parent != null && i < limit) {
			chain[i] = current;
			temp = current;
			current = temp.parent;
			i += 1;
		}

		return chain;
	}

	// Target is in world coord
	public static Matrix Jacobian(Vector3 target, Transform[] chain) {

		int n_angles = chain.Length;
		Matrix J = new Matrix(n_angles, 3);

		//End effector position in world coordinates
		//Vector3 s = chain[0].position;
		Vector3 s = target;

		for(int i=0; i<chain.Length; i++) {
			Transform t = chain[i];
			float angle = 0.0f;

			// Axis of rotation in local coords
    		// Vector3 vv = Vector3.zero;
    		// t.localRotation.ToAngleAxis(out angle, out vv);

    		// Rotation around y axis
    		Vector3 vv = new Vector3(0,1,0);

    		// Position of the end effector in local coords
    		Vector3 ss = t.parent.worldToLocalMatrix.MultiplyPoint(s);

    		// Position of the link in local coords
    		Vector3 pp = t.localPosition;

    		//Jacobian entry
    		Vector3 ds = Vector3.Cross(vv, ss - pp);
    		J.Values[i][0] = ds.x;
    		J.Values[i][1] = ds.y;
    		J.Values[i][2] = ds.z;
			Debug.Log("link " + i + " ss " + ss.ToString() + " pp " + pp.ToString());
		}
		//Debug.Log("Jacobian: " + MatrixUtility.MatrixAsString(J.Values));
		return J;
	}

	public static Matrix ChangeOfPosition(Vector3 target, Transform[] chain) {
		// target in local coords of the end effector
		Vector3 tt = chain[0].parent.worldToLocalMatrix.MultiplyPoint(target);
		tt = tt - chain[0].localPosition;

		Matrix mm = new Matrix(3, 1);
		mm.Values[0][0] = tt.x;
		mm.Values[1][0] = tt.y;
		mm.Values[2][0] = tt.z;
		
		return mm;
	}

	public static Matrix JacobianTranspose(Matrix J, Matrix change, float alpha=0.1f) {
		Matrix JT = Serializer.Transpose(J);
		JT = alpha * JT;
		return JT * change;
	}

	public static Matrix Transpose(Matrix m) {
		Matrix mm = new Matrix(m.GetColumns(), m.GetRows());

		for(int i=0; i<mm.Values.Length; i++) {
			for(int j=0; j<mm.Values[i].Length; j++) {
				mm.Values[i][j] = m.Values[j][i];
			}
		}

		return mm;
	}

	public static Matrix IKJacobianTranspose(Vector3 target, Transform[] chain, float alpha=0.1f) {
		Matrix J = Serializer.Jacobian(target, chain);
		Matrix e = Serializer.ChangeOfPosition(target, chain);
		return JacobianTranspose(J, e, alpha);
	}

	public static void UpdateJoints(Matrix angles, Transform[] chain){
		for(int i=0; i<chain.Length; i++){
			Transform t = chain[i];
			float delta_angle = angles.Values[i][0];

			float angle = 0.0f;

			// Axis of rotation in local coords
    		// Vector3 vv = Vector3.zero;
    		// t.localRotation.ToAngleAxis(out angle, out vv);

    		// Rotation around y axis
    		Vector3 vv = new Vector3(0,1,0);

    		// Apply rotation
    		//Quaternion q = Quaternion.AngleAxis(angle, vv);
    		Vector3 eulerAngles = t.localRotation.eulerAngles;
    		eulerAngles.y += delta_angle;
    		t.localRotation = Quaternion.identity;
    		t.Rotate(eulerAngles);
    		//Debug.Log("link " + i + " Delta " + delta_angle);
		}
	}

}
