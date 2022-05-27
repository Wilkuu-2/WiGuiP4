package wilkuu.gui;

/**
 *  ESide
 *  An enum of sides of the widget, used for margins
 * 
 */
public enum ESide {
	TOP(0),
	RIGHT(1),
	BOTTOM(2),
	LEFT(3);
	
	final int index; 
	
	ESide(int i) {
		index = i;
	}	
	
	
}
