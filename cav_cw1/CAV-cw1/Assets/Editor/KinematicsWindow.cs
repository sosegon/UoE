using UnityEngine;
using UnityEditor;
using System.Collections;
using System.Text.RegularExpressions;

public class KinematicsWindow : EditorWindow
{
    // Add menu item named "My Window" to the Window menu
    [MenuItem("CAV/Kinematics")]
    public static void ShowWindow()
    {
        //Show existing window instance. If one doesn't exist, make one.
		EditorWindow.GetWindow(typeof(KinematicsWindow));
    }

	Vector3 FK(GameObject go, string label, Vector3 v) {
		Vector3 rotation = go.transform.localEulerAngles;

		GUILayout.Label (label, EditorStyles.boldLabel);

		float x = EditorGUILayout.FloatField ("X: ", v.x);
		float y = EditorGUILayout.FloatField ("Y: ", v.y);
		float z = EditorGUILayout.FloatField ("Z: ", v.z);

		return new Vector3 (x, y, z);
	}

	void ApplyRotation(GameObject[] go, Vector3[] rot) {
		if (GUI.Button(new Rect (160, 9, 65, 20), "Rotate") == true) {
			for (int i = 0; i < go.Length; i++) {
				RotateGameObject (go [i], rot [i]);
			}
		}
	}

	void RotateGameObject(GameObject go, Vector3 rot) {
		go.transform.localRotation = Quaternion.identity;
		Quaternion qx = Quaternion.AngleAxis (rot.x, new Vector3 (1, 0, 0));
		Quaternion qy = Quaternion.AngleAxis (rot.y, new Vector3 (0, 1, 0));
		Quaternion qz = Quaternion.AngleAxis (rot.z, new Vector3 (0, 0, 1));
		Quaternion q = qz * qy * qx;
		go.transform.Rotate (q.eulerAngles);
	}

	Vector3 r_shoulder = Vector3.zero;
	Vector3 r_elbow = Vector3.zero;
	Vector3 r_wrist = Vector3.zero;
    void OnGUI()
	{
		GUILayout.Label ("Options FK", EditorStyles.boldLabel);
        
		// FK for shoulder
		GameObject shoulder = GameObject.Find ("Left_Upper_Arm_Joint_01");
		r_shoulder = FK (shoulder, "Shoulder", r_shoulder);

		// FK for elbow
		GameObject elbow = GameObject.Find("Left_Forearm_Joint_01");
		r_elbow = FK (elbow, "Elbow", r_elbow);
	
		// FK for wrist
		GameObject wrist = GameObject.Find("Left_Wrist_Joint_01");
		r_wrist = FK (wrist, "Wrist",  r_wrist);

		GameObject[] gos = new GameObject[3];
		Vector3[] rots = new Vector3[3];
		gos [0] = shoulder;
		rots [0] = r_shoulder;
		gos [1] = elbow;
		rots [1] = r_elbow;
		gos [2] = wrist;
		rots [2] = r_wrist;
	
		ApplyRotation (gos, rots);
		// IK
		string[] labels = new string[3];
		labels[0] = "Transpose";
		labels[1] = "Pseudo inverse";
		labels[2] = "Damped least squares";

		GUILayout.Label ("Options IK", EditorStyles.boldLabel);
		int selected = GameObject.Find ("Sphere").GetComponent<Sphere> ().option;
		selected = GUILayout.SelectionGrid(selected,labels,1,EditorStyles.radioButton);
		GameObject.Find ("Sphere").GetComponent<Sphere> ().option = selected;

		if (selected == 0) {
			float alpha = GameObject.Find ("Sphere").GetComponent<Sphere> ().alpha; 
			alpha = EditorGUILayout.FloatField ("Alpha: ", alpha);
			GameObject.Find ("Sphere").GetComponent<Sphere> ().alpha = alpha;
		} else if (selected == 2) {
			float lambda = GameObject.Find ("Sphere").GetComponent<Sphere> ().lambda; 
			lambda = EditorGUILayout.FloatField ("Lambda: ", lambda);
			GameObject.Find ("Sphere").GetComponent<Sphere> ().lambda = lambda;
		}

    }
}
