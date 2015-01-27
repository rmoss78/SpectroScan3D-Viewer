/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

//import algs4.StdIn;
//import edu.princeton.cs.introcs.StdIn;
import java.io.File;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static Main.SPLSensorConstants.*;
import com.jme3.math.Vector3f;
import ss3dviewer.BinaryReader;

/**
 *
 * @author rmoss
 */
//        lineVerticies[0] = new Vector3f(2,0,0);
public class SPLFrame {

    // Instance Variables
    private long delimiter;     // frame delimiter 0xfedcba98 (4275878552d)
    private long framecount;    // frame counter embedded in frame
    private File framefile;     
//    private PointCloud cloud;
//    private ArrayList<PointXYZI> cloud;
    private Vector3f[] cloud;
    
    // Constructor
    public SPLFrame(File f) {
        this.framefile = f;
        loadCloud(this.framefile);
    }    
    
//    public PointCloud getCloud(){
    public Vector3f[] getCloud(){
        return this.cloud;
    }
    
    public int getCloudSizePts(){
        //return this.cloud.size();
        return 0;
    }
    
    public long getDelimiter(){
        return this.delimiter;
    }
    
    public long getFrameCount(){
        return this.framecount;
    }
    
    public File getFrameFile(){
        return this.framefile;
    }
    
    // Load point cloud from binary file
    final public void loadCloud(File f){
        
        BinaryReader in = new BinaryReader(framefile);
//        this.cloud = new PointCloud(imagesize);
//        this.cloud = new ArrayList<>(imagesize);
        this.cloud = new Vector3f[imagesize];
        int j;
        int counter = 0;

        // Loop through all columns and rows
        for (int i = 0; i < numrows; i++) {
            for (int j1 = 0; j1 < numcols; j1++) {

                // Reverse alternating (odd) rows
                if ( (i%2) == 1 ) { j = numcols - j1 - 1; }
                else { j = j1; }
                
                double Theta = Math.toRadians((j - numcols / 2.0) / (numcols / 2.0) * xfov);
                double Phi = Math.toRadians((i - numrows / 2.0) / (numrows / 2.0) * yfov);

                // Read binary data from file
                int A = in.nextUByte() + (in.nextUByte() << 8);    // read amplitude value
                int R = in.nextUByte() + (in.nextUByte() << 8);    // read range value
                
                // Find Frame Delimiter.
		if( (i == 0) && (j == 0) ){
                    if( (A == 0xFEDC) && (R == 0xBA98) ){
                        this.delimiter = ((long)A << 16) + R; 
                        A = 0; 
                        R = 0;
                    }
                    else { System.out.println("Frame format error\n"); }
                }
                
                // Second pixel is embedded frame counter
		if( (i == 0) && (j == 1) ){
                    this.framecount = ((long)A << 16) + R; 
                }

                // Clamp pixels at max and min
                A = clamp(A, araw_min, araw_max);
                R = clamp(R, rraw_min, rraw_max);
                        
                // Convert raw binary to Ampl and Range
                double Range = R * delta_range;                  // Convert range to meters
                double Amplitude = (double) A / araw_max;        // Normalize amplitude to be 0-1

                // Convert polar to xyz
                double x = Range * cos(Phi) * sin(Theta);
                double y = Range * sin(Phi);
                double z = Range * cos(Phi) * cos(Theta);

//                PointXYZI point = new PointXYZI(x, y, z, A);
                Vector3f point = new Vector3f((float)x, (float)y, (float)(-1.0)*(float)z);
//                this.cloud.add(point);
                this.cloud[counter] = point;
                counter++;
            }
        }
    }
    
    // Clamp integer values at min and max
    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
    
//    // Test Client
//    public static void main(String[] args){
//        
//        //File f = new File("C:\\Work\\temp\\java exam\\Ss3DViewer\\src\\ss3dviewer\\Frame0067");
//        String s = StdIn.readString();
//        File f = new File(s);
//        
//        SPLFrame frame = new SPLFrame(f); 
//        
//        System.out.println("Cloud Size=" + frame.getCloudSizePts() + " Delim=" + frame.getDelimiter() + " Frame#=" + frame.getFrameCount() + " File=" + frame.getFrameFile());        
//    }

}
