/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author rmoss
 */
public class SPLSensorConstants {

    // Ladar sensor constants
    static final int imagesize = 32768;             // Size of image in pixels
    static final int numrows = 128;                 // Number of rows in frame
    static final int numcols = 256;                 // Number of columns in frame
    static final double xfov = 40.0;                // Field of view in X (Horizontal)
    static final double yfov = 20.0;                // Field of view in Y (vertical)
    static final double delta_range = 0.00625;      // meters per range count
    static final int araw_max = 1024;               // Max intenstiy (lower 10 bits of word)
    static final int araw_min = 0;                  // Max intenstiy (lower 10 bits of word)
    static final int rraw_max = 8192;               // Max Range (lower 13 bits of word)
    static final int rraw_min = 0;                  // Min Range
    
}
