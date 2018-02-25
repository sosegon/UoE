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
    
	void FK(GameObject go, string label) {
		Vector3 rotation = go.transform.localEulerAngles;

		GUILayout.Label (label, EditorStyles.boldLabel);
		// Unity presents the X euler angle between 0 and 90
		// and 270 and 360
		/*float x = EditorGUILayout.Slider ("X", rotation.x, 0, 90);
		float y = EditorGUILayout.Slider ("Y", rotation.y, 0, 360);
		float z = EditorGUILayout.Slider ("Z", rotation.z, 0, 360);
	*/
		float x = EditorGUILayout.FloatField ("X: ", rotation.x);
		float y = EditorGUILayout.FloatField ("Y: ", rotation.y);
		float z = EditorGUILayout.FloatField ("Z: ", rotation.z);

		go.transform.localRotation = Quaternion.identity;
		Quaternion qx = Quaternion.AngleAxis (x, new Vector3 (1, 0, 0));
		Quaternion qy = Quaternion.AngleAxis (y, new Vector3 (0, 1, 0));
		Quaternion qz = Quaternion.AngleAxis (z, new Vector3 (0, 0, 1));
		Quaternion q = qz * qy * qx;
		go.transform.Rotate (q.eulerAngles);
	}

    void OnGUI()
    {
        GUILayout.Label ("Options FK", EditorStyles.boldLabel);
        
        // FK for shoulder
		FK (GameObject.Find("Left_Upper_Arm_Joint_01"), "Shoulder");

        // FK for elbow
		FK (GameObject.Find("Left_Forearm_Joint_01"), "Elbow");

		// FK for wrist        
		FK (GameObject.Find("Left_Wrist_Joint_01"), "Wrist");

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
