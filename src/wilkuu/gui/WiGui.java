package wilkuu.gui;

import java.util.ArrayList;
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

public class WiGui {
	// myParent is a reference to the parent sketch
	PApplet parent;
	// rootPos is the position of the GUI 
	PVector rootPos; 
	// rootSize is the size of the GUI 
	PVector rootSize;
	// instanceCount is a static counter of all instances active 
	private static int instanceCount = 0;
	// The children widgets of this widget
	private static ArrayList<WiWidget> children;
	private boolean willResize = true; 
	
	
	private float appletSizeX; 
	private float appletSizeY; 
	
	/**
	 * The version of the Library
	 */
	public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @param theParent the parent PApplet
	 * @param rootPosition the position of the GUI
	 * @param rootSize the size of the GUI
	 */
	public WiGui(PApplet theParent, PVector rootPosition, PVector rootSize) {
	    parent = theParent;
	    rootPos = rootPosition;
	    this.rootSize = rootSize;
	    children = new ArrayList<WiWidget>();
	    instanceCount++;
	    System.out.println("[WiGui]: Version: " + VERSION + "\n Initializing instance #" + instanceCount );
	    
	    appletSizeX = parent.width;
	    appletSizeY = parent.height; 
	    
	}
	// -- CONTROL 
	/**
	 * starts up the handles for the GUI to run
	 * 
	 */
	public void start() {
		   parent.registerMethod("draw",this);
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
	
	// -- SETTERS
	/**
	 *  Resizes using the saved applet size and the new applet size 
	 */
	public void appletResize() {
		rootSize.x = rootSize.x / appletSizeX * parent.width;
		rootSize.y = rootSize.y / appletSizeY * parent.height;
		
		rootPos.x = rootPos.x / appletSizeX * parent.width;
		rootPos.y = rootPos.y / appletSizeY * parent.height;
		
	    appletSizeX = parent.width;
	    appletSizeY = parent.height; 
	}
	
	
	
	
	// -- CHILDREN HANDLING
	/**
	 * Add a child directly to the GUI
	 * @param child The child to be added 
	 */
	public void addChild(WiWidget child) {
		children.add(child);
		child.setRoot(this);
	}
	/**
	 * Add a child directly to the GUI
	 * @param child The child to be added 
	 */
	public void removeChild(WiWidget child) {
		children.remove(child);
		child.setRoot(null);
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
	
	/**
	 * A handle of the standard processing draw loop
	 * 
	 */	
	public void draw() {
		parent.pushMatrix();
		parent.translate(rootPos.x, rootPos.y);
		h_update();
		
		for(WiWidget child : children) {
			child.h_display();
		}
		parent.popMatrix();
	}
	
	/**
	 * A sub-handle of the standard processing draw loop that handles updating
	 * TODO Make this run concurrent to drawing 
	 *
	 */
	public void h_update() {
		if(willResize && (appletSizeX != parent.width || appletSizeY != parent.height))
			appletResize();
		
		
		for(WiWidget child : children) {
			child.h_update();
		}
		
		children.removeIf(w -> w.isSuicidal()); // Remove any widget that wish to be destroyed
		
	}
	
	/**
	 * Closes all widget if case they hold a resource, like a file. 
	 * 
	 */
	public void close() {
		for(WiWidget child : children) {
			child.close();
		}
	
		assert (instanceCount > 0);
	    instanceCount--;
	}
	
	
}

