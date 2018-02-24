using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[ExecuteInEditMode]
public class Sphere : MonoBehaviour {

	public Vector3 position;
	public float alpha = 0.0f;

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		position = transform.position;
	}
}
