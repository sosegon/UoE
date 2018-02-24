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
		Vector3 end_pos = transform.position;
		Transform[] chain = Serializer.KinematicChainTransforms(transform,3);
		Matrix angles = Serializer.IKJacobianTranspose(target_pos, chain, alpha);
		// Debug.Log(""+MatrixUtility.MatrixAsString(angles.Values));
		Serializer.UpdateJoints(angles, chain);
	}

}
