package wilkuu.gui;

import processing.core.*;

public class WiLabel extends WiWidget implements PConstants{
	EAlignment alignH;
	EAlignment alignV;
	public String text;
	public float textSize = 11; 
	public float textMargin = 4;
	
	int color; 
	
	public WiLabel(PVector initPos, PVector initSize, String text, EAlignment alignH, EAlignment alignV, int color) {
		super(initPos, initSize); 
		setBorder(true,0xFF112233,10f);
		setBackground(true,0xFFFFFFFF);
		
		this.alignH = alignH;
		this.alignV = alignV;
		this.text   = text;
		this.color  = color;
	}
	
	public void update() {}
	public void display() {
		PVector sz = getPixelSize();
		
		WiLogln("Text gets displayed" + sz + " " + getPixelPos());
		//WiLogln("PixelSize: " + sz);
		// Use PGraphics instead of the PApplet since the applet refuses to render text for some reason;
	    PApplet applet = getApplet();
	   
		applet.textSize(textSize);
		applet.fill(color);
		applet.stroke(color);
		applet.strokeWeight(1);
		
		applet.textAlign(alignH.textAlign, alignV.textAlign);
		applet.text(text, textMargin,textMargin, sz.x - 2 * textMargin, sz.y - 2 * textMargin);
	    
	}
	
	public void resize() {}
	

}
