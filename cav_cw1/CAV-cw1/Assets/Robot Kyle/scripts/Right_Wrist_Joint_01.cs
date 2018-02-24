using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[ExecuteInEditMode]
public class Right_Wrist_Joint_01 : MonoBehaviour {

	GameObject sphere;

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		Vector3 target_pos = GameObject.Find("Sphere").GetComponent<Sphere>().position;
		float alpha = GameObject.Find("Sphere").GetComponent<Sphere>().alpha;
		float lambda = GameObject.Find("Sphere").GetComponent<Sphere>().lambda;
		Vector3 end_effector_pos = GameObject.Find ("Right_Middle_Finger_Joint_01c").transform.position;
		Transform[] chain = Serializer.KinematicChainTransforms(transform,3);

		//Serializer.IKJacobianTranspose(target_pos, end_effector_pos,chain, alpha);
		//Serializer.IKJacobianPseudoInverse(target_pos, end_effector_pos,chain);
		Serializer.IKJacobianDampedLeastSquares(target_pos, end_effector_pos, chain, lambda);
	}

}
