using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class root : MonoBehaviour {

	private Rigidbody rb;
	public float speed;


	// Use this for initialization
	void Start () {
		rb = GetComponent<Rigidbody>();	
	}
	
	// Called just before performing physics calculations
	void FixedUpdate () {
		// float m_horizontal = Input.GetAxis("Horizontal");
		// float m_vertical = Input.GetAxis("Vertical");

		// Vector3 move = new Vector3(m_horizontal, 0.0f, m_vertical);
		// Vector3 position = rb.position;
		// rb.position = position + (move*speed*Time.deltaTime);

		

		// Debug.Log("move: " +  move.ToString());
	}
}
