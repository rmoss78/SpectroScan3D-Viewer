/*
 * Short example of different ways of rotation for an object using JME3

 * Press T to toggle between Turntable and Trackball mode

 */
package Main;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class DragTest extends SimpleApplication {

    Geometry cube;
    boolean isDragging = false;
    Vector2f mouseCoords = new Vector2f(0, 0);
    Vector2f targetRotation = new Vector2f(0, 0);
    boolean turnTable = true;

    public static void main(String[] args) {
        DragTest app = new DragTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Get rid of default camera controls
        flyCam.setEnabled(false);
        // Random object taken from a test example
        Box boxshape1 = new Box(1f, 1f, 1f);        
        cube = new Geometry("My Textured Box", boxshape1);
        Material mat_stl = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex_ml = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
        mat_stl.setTexture("ColorMap", tex_ml);
        cube.setMaterial(mat_stl);
        rootNode.attachChild(cube);
        // Add left mouse button for dragging
        inputManager.addMapping("Drag", new MouseButtonTrigger(0));
        inputManager.addListener(actionListener, "Drag");
        // Add mode toggle button for explanation of rotation
        inputManager.addMapping("ModeToggle", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addListener(actionListener, "ModeToggle");
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (isDragging) {
            // Find change of mouse position for dragging direction and distance
            Vector2f movement = inputManager.getCursorPosition().subtract(mouseCoords).mult(0.01f);
            if (turnTable) { // Up stays up for visual feel, "Turntable"
                // Note: Improvements can be made here using quaternions in a better way, this is just as an example
                targetRotation.addLocal(movement);
                targetRotation.y = Math.min(Math.max(targetRotation.y, -FastMath.PI / 2), FastMath.PI / 2);
                cube.setLocalRotation(Quaternion.IDENTITY);	// Go back to starting UP
                cube.rotate(-targetRotation.y, 0, 0);           // Rotation about the X-Axis
                cube.rotate(0, targetRotation.x, 0);            // Rotation about the Y-Axis
            } else {    // True up as system considers it, "Trackball"
                cube.rotate(-movement.y, movement.x, 0);
            }
            // Store input data for later use
            mouseCoords = inputManager.getCursorPosition().clone();
        }
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String binding, boolean value, float tpf) {
            if (binding.equals("Drag")) {
                isDragging = value;
                // Forget about previous dragging position when initiating drag
                mouseCoords = new Vector2f(inputManager.getCursorPosition());
            } else if (binding.equals("ModeToggle") && value) {
                turnTable = !turnTable;
                // Reset rotation of the object
                cube.setLocalRotation(Quaternion.IDENTITY);
                // Reset target rotation for Turntable mode
                targetRotation = new Vector2f(0, 0);
                System.out.println("Turntable mode is: " + turnTable);
            }
        }
    };
}