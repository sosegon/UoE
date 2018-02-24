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

	// Target and end effector position are in world coord
	// The end effector position is the extreme point of the last link
	// not the position respect to the previous link (parent)
	public static Matrix Jacobian(Vector3 target, Vector3 end_effector_position, Transform[] chain) {

		int n_angles = chain.Length;
		Matrix J = new Matrix(3, n_angles);

		for(int i=0; i<chain.Length; i++) {
			Transform t = chain[i];

    		// Rotation around y axis
    		Vector3 vv = new Vector3(0,1,0);

			// TODO: I am not sure if it is the current transform or the parent
    		// Position of the end effector in local coords
			Vector3 ss = t.parent.worldToLocalMatrix.MultiplyPoint(end_effector_position);
			//Vector3 ss = t.worldToLocalMatrix.MultiplyPoint(end_effector_position);

    		// Position of the link in local coords
    		Vector3 pp = t.localPosition;

    		//Jacobian entry is a column
			Vector3 sspp = ss - pp;
    		Vector3 ds = Vector3.Cross(vv.normalized, sspp);
    		J.Values[0][i] = ds.x;
    		J.Values[1][i] = ds.y;
			J.Values[2][i] = ds.z;
			Debug.Log("link " + i + " ss " + ss.ToString() + " pp " + pp.ToString());
		}
		//Debug.Log("Jacobian: " + MatrixUtility.MatrixAsString(J.Values));
		return J;
	}

	public static Matrix ChangeOfPosition(Vector3 target, Vector3 end_effector_position, Transform[] chain) {
		// TODO: I am not sure if it is the current transform or the parent
		// target and end_effector_position in local coords of the end effector
		Vector3 tt = chain[0].parent.worldToLocalMatrix.MultiplyPoint(target);
		Vector3 ss = chain [0].parent.worldToLocalMatrix.MultiplyPoint (end_effector_position);
		//Vector3 tt = chain[0].worldToLocalMatrix.MultiplyPoint(target);
		//Vector3 ss = chain [0].worldToLocalMatrix.MultiplyPoint (end_effector_position);
		Vector3 ee = tt - ss;

		Matrix mm = new Matrix(3, 1);
		mm.Values[0][0] = ee.x;
		mm.Values[1][0] = ee.y;
		mm.Values[2][0] = ee.z;
		
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

	public static Matrix IKJacobianTranspose(Vector3 target, Vector3 end_effector, Transform[] chain, float alpha=0.1f) {
		Matrix J = Serializer.Jacobian(target, end_effector, chain);
		Matrix e = Serializer.ChangeOfPosition(target, end_effector, chain);
		return JacobianTranspose(J, e, alpha);
	}

	public static void UpdateJoints(Matrix angles, Transform[] chain){
		for(int i=0; i<chain.Length; i++){
			Transform t = chain[i];
			float delta_angle = angles.Values[i][0];

			// Rotation around y axis
    		Vector3 vv = new Vector3(0,1,0);

    		Vector3 eulerAngles = t.localRotation.eulerAngles;
    		eulerAngles.y += delta_angle;
    		t.localRotation = Quaternion.identity;
    		t.Rotate(eulerAngles);
		}
	}

}
