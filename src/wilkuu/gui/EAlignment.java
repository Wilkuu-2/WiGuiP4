package wilkuu.gui;
import processing.core.*;

public enum EAlignment {
	CENTERH(PConstants.CENTER),
	CENTERV(PConstants.CENTER),
	LEFT(PConstants.LEFT),
	RIGHT(PConstants.RIGHT),
	TOP(PConstants.TOP),
	BOTTOM(PConstants.BOTTOM);
	
	final int textAlign; 
	
	EAlignment(int textAlign) { 
		this.textAlign = textAlign; 
	}
	
	public void applyTextAllignment(PApplet applet, EAlignment vertical) {
		if(this.textAlign != -10 && vertical.textAlign != -10) {
			applet.textAlign(textAlign, vertical.textAlign);
		}
	}
	
	PVector resolveStartPos(PVector size, PVector objSize, float [] margins) {
		PVector out = new PVector(0,0);
		
		switch(this) {
		case CENTERH: 
			out.x = (size.x / 2f) - (objSize.x / 2f);
			break;
		case CENTERV: 
			out.y = (size.y / 2f) - (objSize.y / 2f);
			break;
		case LEFT: 
			out.x = margins[3] * size.x;
			break;
		case RIGHT: 
			out.x = size.x - objSize.x - size.x * margins[1];
			break;
		case BOTTOM: 
			out.y = size.y - objSize.y - size.y * margins[2];
			break;
		case TOP: 
			out.y = margins[0] * size.y;
		} 
	   return out; 
	}
	
	public PVector getStartPos(EAlignment second, PVector size, PVector objSize, float[] margins) {
		return PVector.add(second.resolveStartPos(size, objSize, margins),this.resolveStartPos(size, objSize, margins));
	}
}
