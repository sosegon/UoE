using UnityEngine;
using UnityEditor;
using System.Collections;

public class FKWindow : EditorWindow
{
    string myString = "Hello World";
    bool groupEnabled;
    bool myBool = true;
    float myFloat = 1.23f;

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
    [MenuItem("CAV/FKWindow")]
    public static void ShowWindow()
    {
        //Show existing window instance. If one doesn't exist, make one.
        EditorWindow.GetWindow(typeof(FKWindow));
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
    }

    void OnInspectorUpdate() {
    	// Vector3 lw_rotation2 = new Vector3(wrist_x, wrist_y, wrist_z);
    	// left_wrist.transform.localRotation = Quaternion.Euler(wrist_x, wrist_y, wrist_z);
    	// left_wrist.transform.eulerAngles = lw_rotation;
    }
}
