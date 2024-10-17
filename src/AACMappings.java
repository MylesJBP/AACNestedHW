import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & Myles Bohrer-Purnell
 *
 */
public class AACMappings implements AACPage {
	private AssociativeArray<String, AACCategory> catsAA;
	private AACCategory current;
	private AACCategory homeCat;

	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
		try {
			BufferedReader eyes = new BufferedReader(new FileReader(filename));
			this.catsAA = new AssociativeArray<String, AACCategory>();
			this.homeCat = new AACCategory("");
			this.current = homeCat;
			boolean invalid = false;
			String line = "";
			String recCat = "";
			while (invalid == false) {
				try {
					line = eyes.readLine();
				} catch (IOException e) {
					invalid = true;
				}
				if (line == null || line.equals("")) {
					invalid = true;
				} else if (invalid != true) {
					// break up line into parts
					String[] strArr = line.split(" ");
					// if creating a new category
					if (strArr[0].charAt(0) != '>') {
						try {
							// combine the category name
							String catStr = "";
							for(int i = 1; i < strArr.length; i++) {
								if (i < strArr.length - 1) {
									catStr = catStr.concat(strArr[i] + " ");
								} else {
									catStr = catStr.concat(strArr[i]);
								} // if/else
							} // for
							this.catsAA.set(strArr[0], new AACCategory(catStr));
							recCat = strArr[0];
						} catch (NullKeyException e) {
							// should not happen
						} // try/catch
						// else if creating a new internal category
					} else {
						// combine the category name
						String catStr = "";
						for(int i = 1; i < strArr.length; i++) {
							if (i < strArr.length - 1) {
								catStr = catStr.concat(strArr[i] + " ");
							} else {
								catStr = catStr.concat(strArr[i]);
							} // if/else
						} // for
						// set to most recent created category
						try {
							this.catsAA.get(recCat).addItem(strArr[0].substring(1), catStr);
						} catch (KeyNotFoundException e) {
							// shouldn't happen, but catch exception
						} // try/catch
					} // if/else
				} // if/else
			} // while
		} catch (FileNotFoundException e) {
			// should not happen
		} // try/catch
	}
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) {
		if (this.current.getCategory().equals("")) {
			try {
				this.current = this.catsAA.get(imageLoc);
				return "";
			} catch (Exception e) {
				// should not happen
			} // try/catch
		} // if
		return this.current.select(imageLoc);
	}
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		if ((this.current.getCategory()).equals("")) {
			if (catsAA.size() == 0) {
				return new String[]{};
			} // if
			String[] imagesList = new String[this.catsAA.size()];
			for (int i = 0; i < this.catsAA.size(); i++) {
				imagesList[i] = this.catsAA.getKey(i);
			} // for
			return imagesList;
		} // if			
		return this.current.getImageLocs();
	}
	
	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
		this.current = this.homeCat;
	}
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) {
		try {
			FileWriter eyes = new FileWriter(filename);
			// write each category name
			for (int i = 0; i < catsAA.size(); i++) {
				eyes.write(catsAA.getKey(i) + " " + catsAA.get(catsAA.getKey(i)).getCategory());
				String[] categoryLoc = catsAA.get(catsAA.getKey(i)).getImageLocs();
				// write all of the elements within each category
				for (int j = 0; j < categoryLoc.length; j++) {
					eyes.write(">" + categoryLoc[i] + " " + catsAA.get(catsAA.getKey(i)).select(categoryLoc[i]));
				} // for
			} // for
		} catch (Exception e) {
			// catch exceptions
		} // try/catch
	}
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		if (this.current.getCategory().equals("")) {
			try {
				catsAA.set(imageLoc, new AACCategory(text));
			} catch (NullKeyException e) {
				// just to check
			} // try/catch
		} else {
			this.current.addItem(imageLoc, text);
		} // if/else
	}


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	public String getCategory() {
		return this.current.getCategory();
	}


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		if (this.current.getCategory().equals("")) {
			return catsAA.hasKey(imageLoc);
		} else {
			return this.current.hasImage(imageLoc);
		} // if/else
	} // hasImage
}
