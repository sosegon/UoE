using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[ExecuteInEditMode]
public class Sphere : MonoBehaviour {

	public Vector3 position;
	public float alpha = 16.0f;
	public float lambda = 0.1f;

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		position = transform.position;
	}
}
