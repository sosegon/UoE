using UnityEngine;
using UnityEditor;
using System.Collections;

public class IKWindow : EditorWindow
{
    GameObject right_wrist;
    
    // Add menu item named "My Window" to the Window menu
    [MenuItem("CAV/IKWindow")]
    public static void ShowWindow()
    {
        //Show existing window instance. If one doesn't exist, make one.
        EditorWindow.GetWindow(typeof(IKWindow));
        Debug.Log("Show");
    }
    
    void OnGUI()
    {
       Debug.Log("OnGui IK");
    }

    void OnInspectorUpdate() {
    }
}
