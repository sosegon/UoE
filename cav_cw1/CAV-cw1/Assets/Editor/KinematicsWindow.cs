using UnityEngine;
using UnityEditor;
using System.Collections;
using System.Text.RegularExpressions;

public class KinematicsWindow : EditorWindow
{
    bool groupEnabled;

    // Shoulder variables
    float shoulder_x = 0;
    float shoulder_y = 0;
    float shoulder_z = 0;

    // Elbow variables
    float elbow_x = 0;
    float elbow_y = 0;
    float elbow_z = 0;

    // Wrist variables
    float wrist_x = 0;
    float wrist_y = 0;
    float wrist_z = 0;
    GameObject left_wrist;

    // Add menu item named "My Window" to the Window menu
    [MenuItem("CAV/Kinematics")]
    public static void ShowWindow()
    {
        //Show existing window instance. If one doesn't exist, make one.
		EditorWindow.GetWindow(typeof(KinematicsWindow));
        Debug.Log("Show");
    }
    
    void OnGUI()
    {
        GUILayout.Label ("Options FK", EditorStyles.boldLabel);
        
        // Sliders for shoulder
        GUILayout.Label ("Shoulder", EditorStyles.boldLabel);
        shoulder_x = EditorGUILayout.Slider ("X", shoulder_x, 0, 90);
        shoulder_y = EditorGUILayout.Slider ("Y", shoulder_y, 0, 90);
        shoulder_z = EditorGUILayout.Slider ("Z", shoulder_z, 0, 90);
        
        // Sliders for elbow
        GUILayout.Label ("Elbow", EditorStyles.boldLabel);
        elbow_x = EditorGUILayout.Slider ("X", elbow_x, 0, 90);
        elbow_y = EditorGUILayout.Slider ("Y", elbow_y, 0, 90);
        elbow_z = EditorGUILayout.Slider ("Z", elbow_z, 0, 90);

        
        left_wrist = GameObject.Find("Left_Wrist_Joint_01");
        Quaternion rotation = left_wrist.transform.localRotation; 
        Vector3 lw_rotation = left_wrist.transform.localEulerAngles;
        Debug.Log(rotation.ToString());

        GUILayout.Label ("Wrist", EditorStyles.boldLabel);
        wrist_x = EditorGUILayout.Slider ("X", lw_rotation.x, 0, 360);
        wrist_y = EditorGUILayout.Slider ("Y", lw_rotation.y, 0, 360);
        wrist_z = EditorGUILayout.Slider ("Z", lw_rotation.z, 0, 360);

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
