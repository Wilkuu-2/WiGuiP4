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
	public static int defaultBorderColor = 0xFFFFFFFF;
	public static float defaultBorderThickness = 4; 
	private static int defaultBackgroundColor = 0xFF444444;
	
	public static boolean defaultDrawBorder = true;
	public static boolean defaultDrawBackground = true;
	
	private int borderColor;
	private float borderThickness; 
	private int backgroundColor;
	
	private boolean drawBorder;
	private boolean drawBackground;
	
	
	
	protected PVector pos;  //Fractional position on the parent;
	protected PVector size; //Fractional size on the parent widget;
	
	protected WiWidget parent; 
	
	private WiGui root; 
	
	protected float[] margins = {0,0,0,0};
	
	protected boolean suicidal = false; 
	
	protected ArrayList<WiWidget> children;
	public static boolean LOG = true; 
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
		
		backgroundColor = defaultBackgroundColor;
		borderColor     = defaultBorderColor;
		borderThickness = defaultBorderThickness;
		drawBackground  = defaultDrawBackground;
		drawBorder      = defaultDrawBorder;
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
	
	public PVector getSize() {
		return size.copy(); 
	}
	/**
	 * Returns the root of the widget
	 * @return WiGui
	 */
	public WiGui getRoot() {
		return root; 
	}
	
	public PApplet getApplet() {
		return root.getApplet();
	}
	
	/**
	 * Gets their internal/drawable size in pixels
	 * @return PVector
	 */
	public PVector getPixelSize() {
		PVector pSize;
		if(parent != null) 
			// Recursively get the size of the parent 
			pSize = parent.getPixelSize();
		else
			pSize = root.getSize();
		
		// Create a PVector from the vertical and horizontal sizes
		return new PVector(pSize.x * (size.x - margins[1] - margins[3]), 
						   pSize.y * (size.y - margins[0]  - margins[2]));
		// TODO Add margins
		
	}
	
	/**
	 * Returns the position in pixels
	 * @return PVector
	 */
	public PVector getPixelPos() {
		PVector pSize;
		pSize = parent.getPixelSize();
		// Create a PVector from the fractional vertical and horizontal positions and the parent size
		return new PVector(pSize.x * (pos.x + margins[3]), 
						   pSize.y * (pos.y + margins[0])).add(parent.getPixelPos());
		// TODO Add margins
		// TODO Maybe involve layering using the Z in position
	}
	
	boolean isSuicidal() {
		return suicidal;
	}
	
	// -- SETTERS 
	
	// Borders
	
	public void setBorder(boolean drawBorder, int borderColor, float borderThickness) {
		this.drawBorder = drawBorder;
		this.borderColor = borderColor;
		this.borderThickness = borderThickness;
	}
	
	// Background 
	public void setBackground(boolean drawBackground, int backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.drawBackground = drawBackground;
	}
	
	// Margins 
	/**
	 * Sets one of the margins
	 * @param side the side that needs to be set
	 * @param size the margin size in pixels 
	 */
	public void setMargin(ESide side, float size) {
		margins[side.index] = size; 
	}
	/**
	 * Sets all margins with a float array 
	 * @param marginsArray a array of margins {TOP,RIGHT,BOTTOM,LEFT}
	 */
	public void setMargins(float[] marginsArray) {
		margins = marginsArray.clone();
	}
	/**
	 * Sets all margins 
	 * @param top top margin 
	 * @param right right margin 
	 * @param bottom bottom margin 
	 * @param left left margin 
	 */
	public void setMargins(float top, float right, float bottom, float left) {
		margins = new float[]{top,right,bottom,left};
	}
	
	public void setUniformMargins(float margin) {
		margins = new float[]{margin,margin,margin,margin};
	}
	
	/**
	 * Sets the parent 
	 * @param newParent the new parent 
	 */
	public void setParent(WiWidget newParent) {
		parent = newParent;
		setRoot(newParent.root);
		
	}
		
	/**
	 * Sets the root 
	 * @param newRoot the new root 
	 */
	public void setRoot(WiGui newRoot) {
		root = newRoot;
		
		h_resize();
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
    	PApplet applet = getApplet();
    	applet.pushMatrix();
    	
    	PVector pixPos = getPixelPos();
    	PVector pixSize = getPixelSize();
    	applet.translate(pixPos.x, pixPos.y);
    	
    	applet.pushStyle();
    	
        if(drawBorder) {
        	applet.strokeWeight(borderThickness);
        	applet.stroke(borderColor);
        } else {
        	applet.noStroke();
        } 
        
        if(drawBackground) {
        	applet.fill(backgroundColor);
        } else {
        	applet.noFill();
        }
        
        if(drawBackground || drawBorder) {
        	applet.rect(0,0,pixSize.x,pixSize.y);
        }
    	
    	applet.popStyle();
    	
    	display();
    	
    	applet.popMatrix();
    	
    	for(WiWidget child : children)
    		child.h_display();
    	
    
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
    
    public void h_resize() {
    	resize();
    	
    	for(WiWidget child : children)     
    		child.h_resize();
    }
    
    // -- UTIL
    
	public static void WiLog(String out) {
		if(LOG) {
			System.out.printf("{%s} [WiGui]: %s", java.time.LocalTime.now() , out);
		}
	}
	public static void WiLogln(String out) {
		WiLog(out+'\n');
	}
    
    // -- HANDLES 
	
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
    
    protected void resize() {
    /**
    *  Custom resizing method
    */
    }

	
}
