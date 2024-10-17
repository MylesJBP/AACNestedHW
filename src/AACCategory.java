import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Myles Bohrer-Purnell
 *
 */
public class AACCategory implements AACPage {

	private AssociativeArray<String, String> catMap;
	private String catName;
	
	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.catMap = new AssociativeArray<String, String>();
		this.catName = name;
	}
	
	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try {
			catMap.set(imageLoc, text);
		} catch (NullKeyException e) {
			// exception thrown
		} // try/catch
	} // addItem

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		if (catMap.size() == 0) {
			return new String[]{};
		} // if
		String[] imagesList = new String[catMap.size()];
		for (int i = 0; i < catMap.size(); i++) {
			imagesList[i] = catMap.getKey(i);
		} // for
		return imagesList;
	} // getImageLocs()

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
    return this.catName;
	}

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc) {
		if (catMap.hasKey(imageLoc)) {
			try {
				return catMap.get(imageLoc);
			} catch (KeyNotFoundException e) {
				// catch exception, should never happen
			} // try/catch
		} // if
		throw new NoSuchElementException();
	} // select

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return this.catMap.hasKey(imageLoc);
	}
}
