using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[ExecuteInEditMode]
public class Sphere : MonoBehaviour {

	public float alpha = 16.0f;
	public float lambda = 0.1f;
	public int option = 0;

	// Update is called once per frame
	void Update () {
		Vector3 target_pos = transform.position;
		Vector3 end_effector_pos = GameObject.Find ("Right_Middle_Finger_Joint_01c").transform.position;
		Transform end_effector = GameObject.Find ("Right_Wrist_Joint_01").transform;
		Transform[] chain = Serializer.KinematicChainTransforms(end_effector,3);

		if (option == 0) {
			Serializer.IKJacobianTranspose (target_pos, end_effector_pos, chain, alpha);
		} else if (option == 1) {
			Serializer.IKJacobianPseudoInverse (target_pos, end_effector_pos, chain);
		} else if (option == 2) {
			Serializer.IKJacobianDampedLeastSquares (target_pos, end_effector_pos, chain, lambda);
		}
	}
}
