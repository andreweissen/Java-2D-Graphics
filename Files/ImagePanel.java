/**
 * ImagePanel.java - Used for image-based <code>JPanel</code> objects
 * Begun 08/24/18
 * @author Andrew Eissen
 */
//package graphicsprojectone;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * In accordance with the
 * <a href="https://en.wikipedia.org/wiki/Single_responsibility_principle">single responsibility
 * principle</a>, this class contains all code related to the custom <code>JPanel</code>-extending
 * class required to display images in the user GUI. Originally, within the Project 1 template files
 * package, much of this code was previously stored in the same class as the GUI and all other
 * translation related code, a reality that made perusal of that package's contents difficult for
 * the author.
 * <br />
 * <br />
 * As such, all such code was moved herein to enable the author to create three (3) new instances of
 * the class as needed for display of each image in a separate panel in the GUI. Since the rubric
 * did not specify how or when the images are each to be displayed, the author made the decision to
 * display them all simultaneously in a separate panels to allow comparisons between
 * transformations.
 *
 * Extends <code>JPanel</code>
 * @see javax.swing.JPanel
 * @author Andrew Eissen
 */
final class ImagePanel extends JPanel {

  // Private class fields
  private Application parent;
  private int[][] imageArray;
  private BufferedImage bufferedImage;

  /**
   * Parameterized constructor
   *
   * @param parent <code>Application</code> for use in calling App methods
   * @param imageArray <code>int[][]</code> array containing image data
   */
  protected ImagePanel(Application parent, int[][] imageArray) {
    this.setApplication(parent);
    this.setImageArray(imageArray);
    this.setBufferedImage(new BufferedImage(parent.getImageSizeConstant(),
      parent.getImageSizeConstant(), BufferedImage.TYPE_INT_RGB));
    this.buildImage();
  }

  // Setters

  /**
   * Setter for <code>ImagePanel.parent</code>
   *
   * @param parent <code>Application</code>
   * @return void
   */
  private void setApplication(Application parent) {
    this.parent = parent;
  }

  /**
   * Setter for <code>ImagePanel.imageArray</code>
   *
   * @param imageArray <code>int[][]</code>
   * @return void
   */
  private void setImageArray(int[][] imageArray) {
    this.imageArray = imageArray;
  }

  /**
   * Setter for <code>ImagePanel.bufferedImage</code>
   *
   * @param bufferedImage <code>BufferedImage</code>
   * @return void
   */
  private void setBufferedImage(BufferedImage bufferedImage) {
    this.bufferedImage = bufferedImage;
  }

  // Getters

  /**
   * Getter for <code>ImagePanel.parent</code>
   *
   * @return parent <code>Application</code>
   */
  protected Application getApplication() {
    return this.parent;
  }

  /**
   * Getter for <code>ImagePanel.imageArray</code>
   *
   * @return imageArray <code>int[][]</code>
   */
  protected int[][] getImageArray() {
    return this.imageArray;
  }

  /**
   * Getter for <code>ImagePanel.bufferedImage</code>
   *
   * @return bufferedImage <code>BufferedImage</code>
   */
  protected BufferedImage getBufferedImage() {
    return this.bufferedImage;
  }

  // Utility methods

  /**
   * This method overrides the default implemented in the base <code>JPanel</code> Swing class. It
   * is not supposed to be called externally. Its inspiration originated with the similar method
   * evidenced in <code>AnimationStarter.java</code> and <code>CMSC405P1Template.java</code>, though
   * it was subsequently modified based on the author's reading of several relevant StackOverflow
   * threads and the <code>javax.swing.JPanel.paintComponent</code> documentation to take advantage
   * of the original method's implementation.
   * <br />
   * <br />
   * Unlike the aforementioned classes, this method does not make use of the utility method called
   * <code>applyWindowToViewportTransformation</code>, instead making use of a series of small
   * transforms to ensure the window and the associated transformations occurred as expected.
   * Initially, the program displayed all the proper transformations properly, but inverted; for
   * example, the positive Y-axis shift moved the image down the screen, and the associated rotation
   * operations moved it in the wrong direction. The author realized that this was due to the
   * default alignment of the default <code>Graphics2D</code> coordinate system's origin at the
   * upper-lefthand side of the panel, with the positive changes in y-axis values represented in the
   * downwards direction rather than the more conventional up.
   * <br />
   * <br />
   * As such, to make transforms appear logical to viewers used to "up" meaning an increase in Y
   * values, the author made use of several horizontal transforms to shift the coordinate system to
   * the more logical alternative. However, this resulted in inverted/mirrored images (like a
   * backwards letter "Z") that did not take the shape intended by the author. As such, after
   * undertaking the appropriate transform (be that rotation, translation, or the like) required by
   * <code>Application.performTransformation</code> and concatenating that transform with the
   * previous, the image was once again horizontally flipped in order to ensure that it maintained
   * its expected shape on the screen.
   * <br />
   * <br />
   * The author is uncertain as to whether or not this method is inefficient or in bad form. As it
   * stands, he has had issues understanding this week's material and the specifics of the Graphics
   * Java 2D paradigm. Though this method was intended to be a cleanup of the messy template package
   * file equivalent, the author is not sure if the methods implemented herein constitute good
   * graphics code.
   *
   * @see <a href="https://tinyurl.com/y7f22kq3">Java Code House: "Reflect or Flip Image"</a>
   * @see javax.swing.JPanel.paintComponent
   * @param g <code>Graphics</code>
   * @return void
   */
  @Override
  protected void paintComponent(Graphics g) {

    // Use old Graphics object and method per SO thread recommendations
    super.paintComponent(g);

    // Declarations
    int halfImageSize;
    Graphics2D g2;
    AffineTransform savedTransform;

    // Definitions
    halfImageSize = this.getApplication().getImageSizeConstant() / 2;
    g2 = (Graphics2D) g.create();
    savedTransform = new AffineTransform();

    // As per template package recommendations, apply antialiasing thingie
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Translate to the panel center (kinda janky)
    savedTransform.translate(this.getWidth() / 2.0, this.getHeight() / 2.0);

    // Flip horizontally so coordinate system is logical and transforms occur as user expects
    savedTransform.scale(1.0, -1.0);
    savedTransform.translate(0, -this.getBufferedImage().getHeight());

    // Undertake new transform then join old and new transforms
    savedTransform.concatenate(this.getApplication().getAffineTransform());

    // Then, flip horizontally again so images appear normal and not inverted/mirrored on screen
    savedTransform.scale(1.0, -1.0);
    savedTransform.translate(0, -this.getBufferedImage().getHeight());

    // Apply transformation to Graphics2D instance
    g2.transform(savedTransform);

    // Janky way to make the image sit in the middle properly; draw the image
    g2.drawImage(this.getBufferedImage(), -halfImageSize, -halfImageSize, this);

    // Release resources
    g2.dispose();
  }

  /**
   * This utility method is used to apply the color values listed in the <code>ImagePanel</code>
   * object's 2D <code>int</code> array <code>imageArray</code> to the <code>BufferedImage</code>
   * instance, as well as the associated pixels' coordinates. Despite the author's dislike for
   * nested loop structures, he cannot think of a better way to do this under the present
   * circumstances.
   *
   * @return void
   */
  private void buildImage() {

    // Declarations
    int imageSize;
    int[][] array;
    BufferedImage image;

    // Definitions (cache values)
    imageSize = this.getApplication().getImageSizeConstant();
    array = this.getImageArray();
    image = this.getBufferedImage();

    // Apply RGB values from array
    for (int x = 0; x < imageSize; x++) {
      for (int y = 0; y < imageSize; y++) {
        image.setRGB(x, y, array[x][y]);
      }
    }
  }
}