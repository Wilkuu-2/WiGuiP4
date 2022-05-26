/**
 * 
 */
package wilkuu.gui;

import processing.core.*;
import java.util.ArrayList;

/**
 * @author wilkuu 
 *
 */


public abstract class WiWidget {
	private PVector pos;  //Fractional position on the parent
	private PVector size; //Fractional size on the parent widget;
	private PApplet applet;
	
	private WiWidget parent; 
	private WiGui root; 
	
	private boolean suicidal = false; 
	
	private ArrayList<WiWidget> children;
	
	/**
	 * Full constructor 
	 * 
	 * @param initPos The position of the 0,0 point of the widget 
	 * @param initSize The size of the widget related to the  parent or root 
	 * @param root The root of the GUI 
	 * @param parent The parent widget 
	 */
	public WiWidget(PVector initPos, PVector initSize, WiGui root, WiWidget parent){
		this(initPos,initSize);	

		setRoot(root);
		parent.addChild(this);
	}
	
	/**
	 * Parentless constructor 
	 * 
	 * @param initPos The position of the 0,0 point of the widget 
	 * @param initSize The size of the widget related to the  parent or root 
	 * @param root The root  of the GUI 
	 */
	public WiWidget(PVector initPos, PVector initSize, WiGui root){
		this(initPos, initSize);
		
		root.addChild(this);
	}
	/**
	 * Bare constructor with neither the parent nor the root
	 * 
	 * @param initPos The position of the 0,0 point of the widget 
	 * @param initSize The size of the widget related to the future parent or root 
	 */
	
	public WiWidget(PVector initPos, PVector initSize) {
		pos = initPos;
		size = initSize;
		children = new ArrayList<WiWidget>();
	}
	/**
	 * Bare constructor with the floats instead of the PVector's 
	 * 
	 * @param pX the x coordinate of the 0,0 point
	 * @param pY the y coordinate of the 0,0 point
	 * @param sX the relative width of the widget 
	 * @param sY the relative width of the widget 
	 */
	public WiWidget(float pX,float pY,float sX,float sY) {
		this(new PVector(pX,pY),new PVector(sX,sY));
	}
	/**
	 * Bare constructor with short float arrays instead of the PVector
	 * 
	 * @param pos
	 * @param size
	 */
	public WiWidget(float[] pos,float[] size) {
		this(new PVector(pos[0],pos[1]),new PVector(size[0],size[1]));
	}

	// -- GETTERS 
	/**
	 * Returns the parent of the widget
	 * @return WiWidget
	 */
	public WiWidget getParent() {
		return parent; 
	}
	/**
	 * Returns the root of the widget
	 * @return WiGui
	 */
	public WiGui getRoot() {
		return root; 
	}
	/**
	 * Gets the size in pixels
	 * @return PVector
	 */
	public PVector getPixelSize() {
		PVector pSize;
		if(parent != null) 
			// Recursively get the size of the parent 
			pSize = parent.getPixelSize();
		else
			pSize = root.rootSize;
		
		// Create a PVector from the vertical and horizontal sizes
		return new PVector(pSize.x * size.x, pSize.y * size.y);
		// TODO Add margins
		
	}
	
	/**
	 * Returns the position in pixels
	 * @return PVector
	 */
	public PVector getPixelPos() {
		PVector pSize;
		if(parent != null) 
			// Recursively get the size of the parent 
			pSize = parent.getPixelSize();
		else
			pSize = root.rootSize;
		
		// Create a PVector from the fractional vertical and horizontal positions and the parent size
		return new PVector(pSize.x * pos.x, pSize.y * pos.y);
		// TODO Add margins
		// TODO Maybe involve layering using the Z in position
	}
	
	boolean isSuicidal() {
		return suicidal;
	}
	
	// -- SETTERS 
	/**
	 * Sets the parent 
	 * @param newParent the new parent 
	 */
	void setParent(WiWidget newParent) {
		parent = newParent;
		setRoot(newParent.root);
	}
		
	/**
	 * Sets the root 
	 * @param newRoot the new root 
	 */
	void setRoot(WiGui newRoot) {
		root = newRoot;
		applet = root.parent;
	}
	// -- CHILDREN HANDLING
	/**
	 * Add a child to the widget
	 * @param child The child to be added 
	 */
	public void addChild(WiWidget child) {
		children.add(child);
		child.setParent(this);
	}
	/**
	 * Remove the child from the widget
	 * @param child The child to be removed 
	 */
	public void removeChild(WiWidget child) {
		children.remove(child);
		child.setParent(null);
	}
	// -- SIZE/RESIZE
	// TODO resize events 
	
	// -- HANDLES 
	/**
	 * The handle to display the widget and it's children
	 */
    public void h_display() {
    	applet.pushMatrix();
    	
    	PVector PixPos = getPixelPos();
    	applet.translate(PixPos.x, PixPos.y);
    	
    	display();
    	
    	for(WiWidget child : children)
    		child.h_display();
    	applet.popMatrix();
    }
    
	/**
	 *  Handle for updating the widget and it's children 
	 * */
    public void h_update() {
    	update();
    	
    	for(WiWidget child : children)     
    		child.h_update();
    	
    	children.removeIf(w -> w.suicidal); // Remove any widget that wish to be destroyed
    }
    
	/**
	 *  Handle for object cleanup, if necessary
	 *  relays the cleanup to it's children 
	 *  
	 */
    public void close() {
    	closeCustom();
    	
		for(WiWidget child : children) {
			child.close();
		}
		// Overwrite the array since it won't be necessary
		children = new ArrayList<WiWidget>();
		
		// After this operation the object can be removed
		suicidal = true; 
    }
    
	/**
	 *  Custom method for object cleanup, if necessary
	 *  
	 *  @return void
	 */
    protected void closeCustom() {}
	/**
	 *  Custom drawing method
	 * */
    protected abstract void display();	
	/**
	 *  Custom update method
	 */
    protected abstract void update();

	
	
	
	
	
}
