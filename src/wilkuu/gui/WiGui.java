package wilkuu.gui;

import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.) 
 */

public class WiGui extends WiWidget  {
	
	private static int instanceCount = 0; // static counter of all instances active 
	private boolean willResize = true;    // flag to rule over resizing of the widget Applet 
	private PVector appletSize;           // size of the applet before resize
	
	public static boolean LOG = true; 
	
	/**
	 * The version of the Library
	 */
	public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * A constructor for the GUI using just floats  
	 * 
	 * @param theParent The applet
	 * @param pX x coordinate of the 0,0 point 
	 * @param pY y coordinate of the 0,0 point 
	 * @param sX width 
	 * @param sY height
	 */
	public WiGui(PApplet theParent, float pX, float pY, float sX ,float sY) {
		this(theParent, new PVector(pX,pY),new PVector(sX,sY));
	}
	
	
	/**
	 * A constructor for the GUI using PVectors
	 * 
	 * 
	 * @param theParent the parent PApplet
	 * @param rootPosition the position of the GUIfloat sXfloat sX
	 * @param rootSize the size of the GUI
	 */
	public WiGui(PApplet theParent, PVector rootPosition, PVector rootSize) {
		super(rootPosition, rootSize);
	    setApplet(theParent);
	    setRoot(this);
	    instanceCount++;
	    System.out.println("[WiGui]: Version: " + VERSION + "\n Initializing instance #" + instanceCount );
	    
	    appletSize  = new PVector(applet.width,applet.height);
	    
	}
	// -- CONTROL 
	/**
	 * starts up the handles for the GUI to run
	 * 
	 */
	public void start() {
		   applet.registerMethod("draw",this);
		   //parent.registerMethod("keyEvent",this);
		   //parent.registerMethod("mouseEvent", this);
		}
	
	// -- GETTERS 
	 
	/**
	 * return the version of the Library.
	 * 
	 * @return String Version 
	 */
	public static String version() {
		return VERSION;
	}
	
	@Override
	public PVector getPixelSize() {
		return size.copy();
	}
	public PVector getPixelPos() {
		return pos.copy();
	}
	
	
	
	// -- SETTERS
	
	public void setApplet(PApplet newApplet) {
		applet = newApplet;
	}
	
	/**
	 *  Resizes using the saved applet size and the new applet size 
	 */
	public void appletResize() {
		WiLog("Resized applet caused a resize of the GUI\n");
		WiLog("Saved size: " + appletSize + '\n' );
		WiLog("New size: [" + applet.width + ", " + applet.height + "]\n");
		
		size.x = size.x / appletSize.x * applet.width;
		size.y = size.y / appletSize.y * applet.height;
		
		pos.x = pos.x / appletSize.x * applet.width;
		pos.y = pos.y / appletSize.y * applet.height;
		
	    appletSize = new PVector(applet.width, applet.height); 
	}
	
	// -- EVENTS / HANDLES 
	/**
	 * This is a registered method, don't call directly 
	 * @param ev The event of pressing the key
	 */
	public void keyEvent(KeyEvent ev) {
		// TODO Events not implemented
	}
	
	/**
	 * This is a registered method, don't call directly 
	 * @param ev The event of using the mouse 
	 */
	public void mouseEvent(MouseEvent ev) {
		// TODO Events not implemented
	}
	
	protected void display() {
		
	}
	protected void update() {
		if(willResize && (appletSize.x != applet.width || appletSize.y != applet.height))
			appletResize();
	}
	
	/**
	 * A handle of the standard processing draw loop
	 * 
	 */
	public void draw() {
		Thread updateThread = new Thread(() -> 
			this.h_update() // Run the update concurrent to the drawing, i wonder if this will have any grave consequences 
		); // TODO: This will **definitely** *not* cause any race conditions   		
		updateThread.start();
		
		h_display(); 
		
		try{
			updateThread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public void closeCustom() { 
		assert (instanceCount > 0);
	    instanceCount--;
	}
	
	
	// -- UTiLS
	
	public static void WiLog(String out) {
		if(LOG) {
			System.out.printf("{%s} [WiGui]: %s", java.time.LocalTime.now() , out);
		}
}

