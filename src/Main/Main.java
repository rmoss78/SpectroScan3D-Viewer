package Main;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.*;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.io.File;
import static Main.SPLSensorConstants.*;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.system.AppSettings;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Main extends SimpleApplication {

    Geometry points;
    Vector2f mouseCoords = new Vector2f(0, 0);
    boolean isDragging = false;

    public static void main(String[] args) {
        Main app = new Main();

        app.setShowSettings(false);

        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setBitsPerPixel(32);
        settings.setTitle("SpectroScan3D Viewer");
        app.setSettings(settings);

        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(15);
        flyCam.setZoomSpeed(15f);
//        flyCam.setDragToRotate(false);
        flyCam.setDragToRotate(true);
        // Get rid of default camera controls
//        flyCam.setEnabled(false);

//        Vector3f startCamPos = new Vector3f(0.0f, 0.0f, 1.58f);
//        Vector3f startCamPos = new Vector3f(0.0f, 0.0f, 2.0f);
//        cam.setLocation(startCamPos);

        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            File f = new File(decodedPath + "Main//" + "Frame0067");
            SPLFrame frame = new SPLFrame(f);
            Vector3f[] lineVerticies = frame.getCloud();
            plotPoints(lineVerticies, ColorRGBA.White);
        } catch (UnsupportedEncodingException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public void plotPoints(Vector3f[] verticies, ColorRGBA pointColor) {

        Mesh m = new Mesh();
        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(verticies));
        m.updateBound();

        // *************************************************************************
        // Show mesh vertices as colored points
        // *************************************************************************
        Mesh cMesh = m.clone();
        cMesh.setMode(Mesh.Mode.Points);
        cMesh.updateBound();
        cMesh.setStatic();

        points = new Geometry("Points", cMesh);

        Material matVC = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matVC.setBoolean("VertexColor", true);

        //We have imagesize vertices and 4 color values for each of them.
        float[] colorArray = new float[imagesize * 4];
        int colorIndex = 0;

        //Set custom RGBA value for each Vertex. Values range from 0.0f to 1.0f
        for (int i = 0; i < imagesize; i++) {
            // Red value (is increased by 0.9/imagesize on each next vertex)
            colorArray[colorIndex++] = 0.1f + (0.9f / imagesize * i);
            // Green value (is reduced by 0.9/imagesize on each next vertex)
            colorArray[colorIndex++] = 0.9f - (0.9f / imagesize * i);
            // Blue value (remains the same)
            colorArray[colorIndex++] = 0.5f;
            // Alpha value (no transparency set)
            colorArray[colorIndex++] = 1.0f;
        }

        // Set the color buffer
        cMesh.setBuffer(Type.Color, 4, colorArray);
        points.setMaterial(matVC);
        rootNode.attachChild(points);
        points.move(0, 0, 5.0f);

        inputManager.addMapping("Drag", new MouseButtonTrigger(0));
        inputManager.addListener(actionListener, "Drag");
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (isDragging) {
            // Find change of mouse position for dragging direction and distance
            Vector2f movement = inputManager.getCursorPosition().subtract(mouseCoords).mult(0.01f);
//            points.rotate(-movement.y, movement.x, 0);
            rootNode.rotate(-movement.y, movement.x, 0);
            // Store input data for later use
            mouseCoords = inputManager.getCursorPosition().clone();
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
        /* (optional) Make advanced modifications to frameBuffer and scene graph. */
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String binding, boolean value, float tpf) {
            if (binding.equals("Drag")) {
                isDragging = value;
                // Forget about previous dragging position when initiating drag
                mouseCoords = new Vector2f(inputManager.getCursorPosition());
            }
        }
    };
}