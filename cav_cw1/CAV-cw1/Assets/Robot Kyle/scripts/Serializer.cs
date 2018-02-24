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
	public static Matrix Jacobian(Vector3 target, Vector3 end_effector_position, Vector3 axis, Transform[] chain) {

		int n_angles = chain.Length;
		Matrix J = new Matrix(3, n_angles);
		Vector3 vv = axis;

		for(int i=0; i<chain.Length; i++) {
			Transform t = chain[i];

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
		Matrix JT = Transpose(J);
		JT = alpha * JT;
		return JT * change;
	}

	public static Matrix JacobianPseudoInverse(Matrix J, Matrix change) {
		Matrix Jsi = PseudoInverse (J);
		return Jsi * change;
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

	public static void IKJacobianTranspose(Vector3 target, Vector3 end_effector, Transform[] chain, float alpha=0.1f) {
		
		Matrix e = ChangeOfPosition(target, end_effector, chain);

		Vector3 x_axis = new Vector3 (1, 0, 0);
		Matrix x_J = Jacobian(target, end_effector, x_axis, chain);
		Matrix x_angles = JacobianTranspose(x_J, e, alpha);

		Vector3 y_axis = new Vector3 (0, 1, 0);
		Matrix y_J = Jacobian(target, end_effector, y_axis, chain);
		Matrix y_angles = JacobianTranspose(y_J, e, alpha);

		Vector3 z_axis = new Vector3 (0, 0, 1);
		Matrix z_J = Jacobian(target, end_effector, z_axis, chain);
		Matrix z_angles = JacobianTranspose(z_J, e, alpha);

		UpdateJoints (x_angles, y_angles, z_angles, chain);
	}

	public static void IKJacobianPseudoInverse(Vector3 target, Vector3 end_effector, Transform[] chain) {

		Matrix e = ChangeOfPosition(target, end_effector, chain);

		Vector3 x_axis = new Vector3 (1, 0, 0);
		Matrix x_J = Jacobian(target, end_effector, x_axis, chain);
		Matrix x_angles = JacobianPseudoInverse(x_J, e);

		Vector3 y_axis = new Vector3 (0, 1, 0);
		Matrix y_J = Jacobian(target, end_effector, y_axis, chain);
		Matrix y_angles = JacobianPseudoInverse(y_J, e);

		Vector3 z_axis = new Vector3 (0, 0, 1);
		Matrix z_J = Jacobian(target, end_effector, z_axis, chain);
		Matrix z_angles = JacobianPseudoInverse(z_J, e);

		UpdateJoints (x_angles, y_angles, z_angles, chain);
	}

	public static void UpdateJoints(Matrix x_angles, Matrix y_angles, Matrix z_angles, Transform[] chain){
		for(int i=0; i<chain.Length; i++){
			Transform t = chain[i];
			float x_delta_angle = x_angles.Values[i][0];
			float y_delta_angle = y_angles.Values[i][0];
			float z_delta_angle = z_angles.Values[i][0];

    		Vector3 eulerAngles = t.localRotation.eulerAngles;
			eulerAngles.x += x_delta_angle;
			eulerAngles.y += y_delta_angle;
			eulerAngles.z += z_delta_angle;
    		t.localRotation = Quaternion.identity;
    		t.Rotate(eulerAngles);
		}
	}

	public static Matrix PseudoInverse(Matrix J) {
		float[][] mat = J.Values;
		double[,] matt = Convert (mat);
		int m = mat.Length;
		int n = mat[0].Length;

		double[] w = new double[m];
		double[,] u = new double[m, m];
		double[,] vt = new double[n, n];


		alglib.rmatrixsvd (matt, m, n, 2, 2, 0, out w, out u, out vt);

		float[] W = Convert (w);
		float[][] U = Convert (u);
		float[][] Vt = Convert (vt);

		// invert diagonal values
		for (int i = 0; i < W.Length; i++) {
			if (W [i] >= 0.1) {
				W [i] = 1.0f / W [i];
			} else {
				W [i] = 0;
			}
		}

		// Create pseudo inverse diagonal matrix
		float[][] Dsi = new float[n][];
		for(int i = 0; i < n; i++) {
			Dsi[i] = new float[m];
			for (int j = 0; j < m; j++) {
				if (i == j) {
					Dsi [i] [j] = W [i];
				} else {
					Dsi [i] [j] = 0.0f;
				}
			}
		}

		// Create matrices to transpose y multiplication
		Matrix U_ = new Matrix(U);
		Matrix Vt_ = new Matrix (Vt);
		Matrix Dsi_ = new Matrix (Dsi);

		// Transpose U_ and Vt_
		Matrix Ut_ = Transpose(U_);
		Matrix V_ = Transpose(Vt_);

		Matrix DsiU = Dsi_ * Ut_;
		return V_ * DsiU;

	}

	public static float[][] Convert(double[,] mtx)
	{
		int rows = mtx.GetUpperBound(0) - mtx.GetLowerBound(0) + 1;
		int cols = mtx.GetUpperBound(1) - mtx.GetLowerBound(1) + 1;
		float[][] a = new float[rows][];
		for (int i = 0; i < rows; i++) {
			a [i] = new float[cols];
			for (int j = 0; j < cols; j++) {
				a [i] [j] = (float)mtx [i, j];
			}
		}

		return a;
	}

	public static double[,] Convert(float[][] mtx)
	{
		double[,] a = new double[mtx.Length, mtx [0].Length];
		for (int i = 0; i < mtx.Length; i++) {
			for (int j = 0; j < mtx [i].Length; j++) {
				a [i,j] = (double)mtx [i] [j];
			}
		}

		return a;
	}

	public static float[] Convert(double[] mtx) {
		float[] a = new float[mtx.Length];

		for (int i = 0; i < mtx.Length; i++) {
			a [i] = (float)mtx [i];
		}

		return a;
	}
}
