/**
 * Application.java - Displays user GUI and handles animation logic and transformations
 * Begun 08/24/18
 * @author Andrew Eissen
 */
//package graphicsprojectone;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class is the central class of the program package, used to construct and display the user
 * GUI assembled in Swing and perform the necessary animations of the <code>ImagePanel</code>
 * objects in accordance with the required operations set forth in the Project 1 design rubric.
 * <br />
 * <br />
 * Though much of this class's content was inspired by the Project 1 template file package included
 * in the Week 2 handouts, the author saw fit to deviate in many places from the progression
 * employed therein, avoiding the excess of multiple static global variables as much as possible and
 * dividing some contents into separate classes as needed, in accordance with the
 * <a href="https://en.wikipedia.org/wiki/Single_responsibility_principle">single responsibility
 * principle</a>. Optimized and readable code was considered one of the primary goals of the
 * organizational choices and decisions made by the author, done to ensure that readers could easily
 * follow the progression of the transformations without much difficulty.
 * <br />
 * <br />
 * As the Project 1 rubric was vague in many places as to the specifics of the program, the author
 * was forced to extrapolate as required to implement necessary functionality. For instance, the
 * rubric only stated that three (3) images were to be created and displayed making use of the
 * <code>Graphics2D</code> framework and assembled as 2D arrays by individual methods. The rubric,
 * however, did not provide details as to the manner in which these images were to be shown. As a
 * result, the author made the executive decision to display them separately in individual
 * <code>ImagePanel</code> panels and undertake the required transformations on all images
 * simultaneously. Without a clear description either way, some decision had to be made.
 *
 * Extends <code>JFrame</code>
 * @see javax.swing.JFrame
 * @see <a href="https://google.github.io/styleguide/javaguide.html">Google Java Style Guide</a>
 * @author Andrew Eissen
 */
final class Application extends JFrame {

  /*
   * Table of contents
   *
   * Constructors       -> Line 089
   * Setters            -> Line 137
   * Getters            -> Line 209
   * GUI method         -> Line 286
   * Utility methods    -> Line 394
   * Animation methods  -> Line 500
   * Image methods      -> Line 671
   * TimerListener      -> Line 805
   */

  /** Project 1 template files package's default <code>setInterval</code>-esque delay period */
  private static final int TIMER_DELAY = 1600;

  /** Rubric requirement states images must be at least 25 pixels large; has own getter method */
  private static final int IMAGE_SIZE = 25;

  /** If true, permits iteration through individual transforms; enables unit test functionality */
  private static final boolean DEBUG = false;

  // Window fields
  private int windowHeight, windowWidth;
  private String windowTitle;

  // GUI fields
  private JFrame mainFrame;
  private JPanel mainPanel, upperPanel, buttonPanel, imagesPanel, logPanel;
  private ImagePanel leftImagePanel, centerImagePanel, rightImagePanel;
  private JButton startButton, pauseButton, clearButton;
  private JLabel leftLabel, rightLabel;
  private JTextArea logTextArea;
  private JScrollPane logScrollPane;
  private ArrayList<ImagePanel> imageArray;

  // Animation fields
  private int frameCounter;
  private AffineTransform affineTransform;
  private Timer animationTimer;

  // Constructors

  /**
   * Default constructor
   */
  protected Application() {

    // Window fields
    super("Java 2D Project");
    this.setWindowHeight(480);
    this.setWindowWidth(640);
    this.setWindowTitle("Java 2D Project");

    // Animation fields
    this.setImageArray(new ArrayList<>());
    this.setFrameCounter(0);
    this.setAffineTransform(new AffineTransform());
    this.setAnimationTimer(new Timer(Application.TIMER_DELAY, new Application.TimerListener()));

    // Construct interface
    this.constructGUI();
  }

  /**
   * Parameterized constructor
   *
   * @param windowHeight <code>int</code>
   * @param windowWidth <code>int</code>
   * @param windowTitle <code>String</code>
   */
  protected Application(int windowHeight, int windowWidth, String windowTitle) {

    // Window fields
    super(windowTitle);
    this.setWindowHeight(windowHeight);
    this.setWindowWidth(windowWidth);
    this.setWindowTitle(windowTitle);

    // Animation fields
    this.setImageArray(new ArrayList<>());
    this.setFrameCounter(0);
    this.setAffineTransform(new AffineTransform());
    this.setAnimationTimer(new Timer(Application.TIMER_DELAY, new Application.TimerListener()));

    // Construct interface
    this.constructGUI();
  }

  // Setters

  /**
   * Setter for <code>Application.windowHeight</code>
   *
   * @param windowHeight <code>int</code>
   * @return void
   */
  private void setWindowHeight(int windowHeight) {
    this.windowHeight = windowHeight;
  }

  /**
   * Setter for <code>Application.windowWidth</code>
   *
   * @param windowWidth <code>int</code>
   * @return void
   */
  private void setWindowWidth(int windowWidth) {
    this.windowWidth = windowWidth;
  }

  /**
   * Setter for <code>Application.windowTitle</code>
   *
   * @param windowTitle <code>String</code>
   * @return void
   */
  private void setWindowTitle(String windowTitle) {
    this.windowTitle = windowTitle;
  }

  /**
   * Setter for <code>Application.imageArray</code>
   *
   * @param imageArray <code>ArrayList</code>
   * @return void
   */
  private void setImageArray(ArrayList<ImagePanel> imageArray) {
    this.imageArray = imageArray;
  }

  /**
   * Setter for <code>Application.frameCounter</code>
   *
   * @param frameCounter <code>int</code>
   * @return void
   */
  private void setFrameCounter(int frameCounter) {
    this.frameCounter = frameCounter;
  }

  /**
   * Setter for <code>Application.affineTransform</code>
   *
   * @param affineTransform <code>AffineTransform</code>
   * @return void
   */
  private void setAffineTransform(AffineTransform affineTransform) {
    this.affineTransform = affineTransform;
  }

  /**
   * Setter for <code>Application.animationTimer</code>
   *
   * @param animationTimer <code>Timer</code>
   * @return void
   */
  private void setAnimationTimer(Timer animationTimer) {
    this.animationTimer = animationTimer;
  }

  // Getters

  /**
   * Getter for <code>Application.windowHeight</code>
   *
   * @return windowHeight <code>int</code>
   */
  protected int getWindowHeight() {
    return this.windowHeight;
  }

  /**
   * Getter for <code>Application.windowWidth</code>
   *
   * @return windowWidth <code>int</code>
   */
  protected int getWindowWidth() {
    return this.windowWidth;
  }

  /**
   * Getter for <code>Application.windowTitle</code>
   *
   * @return windowTitle <code>String</code>
   */
  protected String getWindowTitle() {
    return this.windowTitle;
  }

  /**
   * Getter for <code>Application.imageArray</code>
   *
   * @return imageArray <code>ArrayList</code>
   */
  protected ArrayList<ImagePanel> getImageArray() {
    return this.imageArray;
  }

  /**
   * Getter for <code>Application.frameCounter</code>
   *
   * @return frameCounter <code>int</code>
   */
  protected int getFrameCounter() {
    return this.frameCounter;
  }

  /**
   * Getter for <code>Application.affineTransform</code>
   *
   * @return affineTransform <code>AffineTransform</code>
   */
  protected AffineTransform getAffineTransform() {
    return this.affineTransform;
  }

  /**
   * Getter for <code>Application.animationTimer</code>
   *
   * @return animationTimer <code>Timer</code>
   */
  protected Timer getAnimationTimer() {
    return this.animationTimer;
  }

  /**
   * Getter for private constant <code>Application.IMAGE_SIZE</code>. Use of a getter and a
   * <code>private</code> constant indicated to be a better practice overall for the use of
   * constants across classes than public static constants (see included thread).
   *
   * @see <a href="https://stackoverflow.com/questions/10047802">SO Thread</a>
   * @return IMAGE_SIZE <code>int</code>
   */
  protected int getImageSizeConstant() {
    return Application.IMAGE_SIZE;
  }

  // GUI method

  /**
   * This method is the hand-assembled user GUI handler, used to construct a Swing-based user GUI
   * that contains a number of buttons and panels used to display the program output to the user.
   * Though a GUI builder could have been used, the author has always enjoyed the process of
   * hand-building GUIs himself, and elected to continue his tradition of personally assembling such
   * components himself for this project.
   * <br />
   * <br />
   * GUI contains a button panel including "Start," "Pause," and "Clear" buttons, a set of three
   * <code>ImagePanel</code> objects extending <code>JPanel</code> for the display of the three
   * images required by the Project 1 design rubric, and a user log displaying messages related to
   * the transformations required for every frame and any errors encountered in the running of the
   * program. Assorted borders and background colors were applied as well to make the GUI appear a
   * bit more professional and sharp in appearance.
   *
   * @return void
   */
  private void constructGUI() {

    // JPanel definitions
    this.mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    this.upperPanel = new JPanel(new BorderLayout());
    this.buttonPanel = new JPanel(new GridLayout(1, 5, 5, 5));
    this.imagesPanel = new JPanel(new GridLayout(1, 3, 10, 10));
    this.logPanel = new JPanel(new GridLayout(1, 1));

    // ImagePanel/JPanel objects
    this.leftImagePanel = new ImagePanel(this, this.buildFlag());     // 4-color flag 2D array
    this.centerImagePanel = new ImagePanel(this, this.buildTriangle()); // Blue triangle array
    this.rightImagePanel = new ImagePanel(this, this.buildLetterZ());   // Gray/black "Z" array

    // Add ImagePanels to grouping ArrayList for easy repainting later
    this.getImageArray().add(this.leftImagePanel);
    this.getImageArray().add(this.centerImagePanel);
    this.getImageArray().add(this.rightImagePanel);

    // Apply borders and backgrounds
    this.leftImagePanel.setBackground(Color.WHITE);
    this.centerImagePanel.setBackground(Color.WHITE);
    this.rightImagePanel.setBackground(Color.WHITE);
    this.leftImagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    this.centerImagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    this.rightImagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    this.buttonPanel.setBorder(BorderFactory.createTitledBorder("Options"));
    this.imagesPanel.setBorder(BorderFactory.createTitledBorder("Images"));
    this.logPanel.setBorder(BorderFactory.createTitledBorder("Status log"));

    // JButton and JLabel definitions
    this.startButton = new JButton("Start");
    this.pauseButton = new JButton("Pause");
    this.clearButton = new JButton("Clear");
    this.leftLabel = new JLabel();  // Space-filling label #1
    this.rightLabel = new JLabel(); // Space filling label #2

    // Status log elements
    this.logTextArea = new JTextArea();
    this.logTextArea.setEditable(false);
    this.logTextArea.setFont(new Font("Monospaced", 0, 12));
    this.logTextArea.setLineWrap(true);
    this.logScrollPane = new JScrollPane(this.logTextArea);

    // Add JButtons and JLabels to buttonPanel
    this.buttonPanel.add(this.leftLabel);
    this.buttonPanel.add(this.startButton);
    this.buttonPanel.add(this.pauseButton);
    this.buttonPanel.add(this.clearButton);
    this.buttonPanel.add(this.rightLabel);

    // Add image JPanels to imagesPanel
    this.imagesPanel.add(this.leftImagePanel);
    this.imagesPanel.add(this.centerImagePanel);
    this.imagesPanel.add(this.rightImagePanel);

    // Add log elements to logPanel
    this.logPanel.add(this.logScrollPane);

    // Add mini-panels to mainPanel
    this.upperPanel.add(this.buttonPanel, BorderLayout.NORTH);
    this.upperPanel.add(this.imagesPanel, BorderLayout.CENTER);
    this.mainPanel.add(this.upperPanel);
    this.mainPanel.add(this.logPanel);

    // Start animation button handler
    this.startButton.addActionListener((ActionEvent e) -> {
      this.buttonHandler(false, "Starting animation", "Press \"Pause\" to pause", "start");
    });

    // Pause animation button handler
    this.pauseButton.addActionListener((ActionEvent e) -> {
      this.buttonHandler(true, "Pausing animation", "Press \"Start\" to resume", "stop");
    });

    // Clear old log entries button handler
    this.clearButton.addActionListener((ActionEvent e) -> {
      this.clearLog();
    });

    // Placement/sizing details for main JFrame element
    this.mainFrame = new JFrame(this.getWindowTitle());
    this.mainFrame.setContentPane(this.mainPanel);
    this.mainFrame.setSize(this.getWindowWidth(), this.getWindowHeight());
    this.mainFrame.setResizable(false);
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.mainFrame.setVisible(true);
  }

  // Utility methods

  /**
   * This method is used simply to log new entries in the user GUI log panel, informing the user as
   * to the progression of the animation, the operations being performed, and any associated errors
   * encountered in the course of running the program. The user log has been a feature of the
   * author's Java GUI programs since the start of his CS program, a fixture derived from the
   * multiplicity of JavaScript bot scripts the author has written for the MediaWiki framework over
   * the years that require the use of a log to denote which editing operations have been
   * successfully implemented and undertaken.
   * <br />
   * <br />
   * In the event that the global constant <code>boolean</code> <code>Application.DEBUG</code> is
   * set to true, this method also logs all status messages in the console as a backup. As discussed
   * in some detail below, the use of a constant debugging field was suggested in a relevant
   * <a href="https://stackoverflow.com/questions/869264">debugging SO thread</a> as an ideal
   * implementation taking advantage of the compiler's optimized handling of conditional statements
   * like <code>if (constant)</code> that involve constants.
   * <br />
   * <br />
   * Originally, this method prepended the new entries via
   * <code>this.logTextArea.insert(message + "\n", 0);</code>, though this was eventually reverted
   * in favor of the basic append method.
   *
   * @see <a href="https://stackoverflow.com/questions/869264">Debugging SO thread</a>
   * @param message <code>String</code>
   * @return void
   */
  private void addLogEntry(String message) {
    this.logTextArea.append(message + "\n");

    if (Application.DEBUG) {
      System.out.println(message);
    }
  }

  /**
   * This method serves as the universal click handler for presses of the "Start" and "Pause" user
   * GUI buttons. It replaces a pair of identical methods, namely
   * <code>Application.startAnimation</code> and <code>Application.pauseAnimation</code>, which were
   * judged to be too similar. In an effort to reduce redundant and copy/pasted code, the author
   * reconciled the handlers into a single method making use of the reflection paradigm to pass the
   * name of the <code>Timer</code> method needed to start or stop the animation.
   * <br />
   * <br />
   * Reflection is used to invoke the needed <code>Timer</code> method depending on the button
   * pressed. The options used are <code>Timer.start</code> and <code>Timer.stop</code>, with the
   * option needed specified via the inclusion a <code>String</code> representation of the method
   * signature, namely parameter <code>methodName</code>.
   *
   * @see java.lang.reflect
   * @param condition <code>boolean</code> assists in displaying message based on run status
   * @param successMessage <code>String</code> message if button press is legitimate
   * @param errorMessage <code>String</code> message if button is illegitimate
   * @param methodName <code>String</code> method to invoke, <tt>start</tt> or <tt>stop</tt>
   * @return void
   */
  private void buttonHandler(boolean condition, String successMessage, String errorMessage,
      String methodName) {

    try {
      // Declaration
      Method timerMethod;

      // Definition
      timerMethod = Timer.class.getDeclaredMethod(methodName);

      /**
       * In the case of the <code>if</code> block, the message displayed (and the need to invoke a
       * <code>Timer</code> method) hangs on whether or not the timer should be running at the
       * moment. If the button pressed is the "Start" button and the animation itself is running,
       * the user shouldn't be pushing the button, so an illegitimate button press message is
       * displayed to let the user know that the animation is playing. If the animation is not
       * playing, the message displayed is a success message and the method is invoked. The same
       * applies to the "Pause" button, just in reverse.
       */
      if (this.getAnimationTimer().isRunning() == condition) {

        // Indicate in log that the desired operation is in the process of implementation
        this.addLogEntry(successMessage);

        // Invoke either Timer.start or Timer.stop
        timerMethod.invoke(this.getAnimationTimer());
      } else {

        // Alert user to the fact that button has been pressed in error
        this.addLogEntry(errorMessage);
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
      this.addLogEntry("Error: " + ex);
    }
  }

  /**
   * This method is simply used to clear the user GUI error log of all messages added during the
   * course of the animation. It is invoked as the primary click handler of the "Clear" button
   * included in the buttons panel of the GUI. Originally, the method instead replaced the log
   * entries with a default bit of text. However, the default log entry approach was eventually
   * abandoned as an extraneous bit of functionality.
   *
   * @return void
   */
  private void clearLog() {
    this.logTextArea.setText("");
  }

  // Animation methods

  /**
   * This method is the primary animation handler method of the class. It is called by the
   * <code>TimerListener</code>'s required <code>actionPerformed</code> method on a preset interval
   * once the <code>Application.startAnimation</code> button handler is invoked on the press of
   * "Start." As per <a href="https://stackoverflow.com/questions/869264">a relevant SO thread</a>
   * related to debugging, the author implemented a debugging <code>boolean</code> constant used to
   * choose between transformation methods and change the number of total transformation operations
   * accordingly. The <code>Application.DEBUG</code> constant takes advantage of the compiler's
   * efficiency in handling <code>if (constant)</code> comparisons and allows the author to choose
   * between the use of the testing <code>Application.testTransformation</code> method and the
   * production <code>Application.performTransformation</code> method as needed for unit testing.
   * This testing methodology is discussed in more detail in the author's <code>.pdf</code>
   * submission document, whose screenshots and included test matrix make use of the test method
   * <code>Application.testTransformation</code> in discussion of the author's chosen test plan.
   * <br />
   * <br />
   * The method begins by performing the requisite transformation operation via the invocation of
   * <code>Application.performTransformation</code> or <code>Application.testTransformation</code>
   * before iterating through the three main <code>ImagePanel</code> objects contained in
   * <code>Application.imageArray</code> and repainting them. Once completed, the method increases
   * the <code>frameCounter</code> by one and checks to see if the associated value is greater than
   * the maximum number. If it is, the counter is reset and the images set back to default by the
   * assignment of a new, fresh <code>AffineTransform</code> instance.
   * <br />
   * <br />
   * Much of this method's functionality was extracted from the larger method included in the P1
   * template files, namely <code>CMSC405P1Template.paintComponent</code>. It made more sense to
   * isolate functionality related to custom <code>JPanel</code> image items in a separate class,
   * namely <code>ImagePanel</code>, than to keep everything in one class, in accordance with the
   * <a href="https://en.wikipedia.org/wiki/Single_responsibility_principle">single responsibility
   * principle</a>.
   *
   * @see <a href="https://stackoverflow.com/questions/869264">Debugging SO thread</a>
   * @return void
   */
  private void handleTransformation() {

    // Declaration
    int maxTransformations;

    // Concatenate current and required transforms based on frame number
    if (Application.DEBUG) {
      maxTransformations = 6;
      this.testTransformation();
    } else {
      maxTransformations = 4;
      this.performTransformation();
    }

    // Repaint all ImagePanels once undertaken
    this.getImageArray().forEach((ImagePanel panel) -> {
      panel.repaint();
    });

    // Increase the frame counter once repainted
    this.setFrameCounter(this.getFrameCounter() + 1);

    // Once the rubric-required operations are completed, reset counter and build new transform
    if (this.getFrameCounter() > maxTransformations) {
      this.setFrameCounter(0);
      this.setAffineTransform(new AffineTransform());
    }
  }

  /**
   * This method is a variation of that included in the <code>CMSC405P1Template.java</code> class,
   * namely the <code>switch</code> statement block included in <code>paintComponent</code>.
   * However, rather than make use of a multiplicity of global static variables, a practice that is
   * generally discouraged, the author simply hardcoded the appropriate integer values into the
   * transformation operations themselves. From the author's point of view, this method's own
   * functionality made more sense to be included in a separate method than bolted on into the
   * aforementioned panel painting method.
   * <br />
   * <br />
   * Originally, this method existed in the body of <code>ImagePanel</code>. However, the use of
   * three separate <code>AffineTransform</code>s in each of the three panel instances made no
   * sense, and the posting of the status message to the user GUI log three times was also an
   * undesirable piece of functionality. Thus, the method was summarily moved to the main
   * <code>Application</code> class and used to govern a universal <code>AffineTransform</code>
   * affecting all three <code>ImagePanel</code> objects via transform concatenation.
   * <br />
   * <br />
   * In addition to performing the required frame-based operations, the method also invokes the
   * class's <code>Application.addLogEntry</code> method to add entries to the user GUI log panel.
   * This assists in letting the user know what exactly is supposed to be happening on screen.
   *
   * @return void
   */
  private void performTransformation() {
    switch (this.getFrameCounter()) {
      case 0:
        this.addLogEntry("Translate images -5 along x-axis and +7 along y-axis");
        this.getAffineTransform().translate(-5.0, 7.0);
        break;
      case 1:
        this.addLogEntry("Rotate images 45 degrees counterclockwise");
        this.getAffineTransform().rotate(45 * Math.PI / 180.0);
        break;
      case 2:
        this.addLogEntry("Rotate images 90 degrees clockwise");
        this.getAffineTransform().rotate(-90 * Math.PI / 180.0);
        break;
      case 3:
        this.addLogEntry("Scale images 2 times along x-axis and 0.5 times along y-axis");
        this.getAffineTransform().scale(2.0, 0.5);
        break;
      case 4:
        this.addLogEntry("Reset images to original positions");
        break;
      default:
        this.addLogEntry("Error: Improper frameCounter value");
        break;
    }
  }

  /**
   * This test plan method differs from that above in that it inspects each individual transform
   * separately to determine if the images themselves are being moved in accordance with both the
   * rubric requirements and with the user's expectations. This method was instrumental in helping
   * the author uncover the fact that the program was using the default <code>Graphics2D</code>
   * coordinate system that lists increasing y-axis values in a downward direction to transform the
   * images rather than a standard system that lists such values as moving towards the top of the
   * screen.
   * <br />
   * <br />
   * This method was featured in the author's "Project 1 Documentation" <code>.pdf</code> file, used
   * to demonstrate the testing procedure by which the author ensured that each operation was
   * performed properly. Basically, this method assists in allowing the author to make sure each
   * operation is being performed correctly by showing each transformation individually and alone,
   * divorced from similar transformations that are required by the rubric to be undertaken in
   * tandem. Additional details may be found in <code>Eissen_Project1.pdf</code>.
   *
   * @return void
   */
  private void testTransformation() {
    switch (this.getFrameCounter()) {
      case 0:
        this.addLogEntry("Translate images -5 units along the x-axis");
        this.getAffineTransform().translate(-5.0, 0);
        break;
      case 1:
        this.addLogEntry("Translate images +7 units along the y-axis");
        this.getAffineTransform().translate(0, 7.0);
        break;
      case 2:
        this.addLogEntry("Rotate images 45 degrees counterclockwise");
        this.getAffineTransform().rotate(45 * Math.PI / 180.0);
        break;
      case 3:
        this.addLogEntry("Rotate images 90 degrees clockwise");
        this.getAffineTransform().rotate(-90 * Math.PI / 180.0);
        break;
      case 4:
        this.addLogEntry("Scale images 2 times along x-axis");
        this.getAffineTransform().scale(2.0, 1.0);
        break;
      case 5:
        this.addLogEntry("Scale images 0.5 times along y-axis");
        this.getAffineTransform().scale(1.0, 0.5);
        break;
      case 6:
        this.addLogEntry("Reset images to original positions");
        break;
      default:
        this.addLogEntry("Error: Improper frameCounter value");
        break;
    }
  }

  // Image methods

  /**
   * This method constructs a letter Z from a set of <code>for</code> loops. Originally, the author
   * made use of a number of taxing comparison operations to determine which elements to paint white
   * to match the panel background. However, making use of the below StackExchange thread, he
   * revised the method significantly to make use of loops only without the need for any
   * comparisons. As per the rubric, this method returns a 2D <code>int</code> array object for use
   * by the <code>ImagePanel</code> class.
   *
   * @see <a href="https://codereview.stackexchange.com/a/160886">CR thread response</a>
   * @return builtImage <code>int[][]</code>
   */
  private int[][] buildLetterZ() {

    // Declarations
    int[][] builtImage;
    int z;

    // Definitions
    builtImage = new int[Application.IMAGE_SIZE][Application.IMAGE_SIZE];

    for (int x = 0; x < Application.IMAGE_SIZE; x++) {
      z = Application.IMAGE_SIZE - x - 1;

      for (int y = z; y > 0; y--) {
        builtImage[x][y] = Color.WHITE.getRGB();
      }

      // Diagonal is gray
      builtImage[x][z] = Color.GRAY.getRGB();

      for (int y = z + 1; y < builtImage.length - 1; y++) {
        builtImage[x][y] = Color.WHITE.getRGB();
      }
    }

    return builtImage;
  }

  /**
   * This method is a variation on the one above, namely <code>Application.buildLetterZ</code>.
   * Using the same basic principles, this method constructs a blue triangle by painting one half of
   * a diagonal white to match the background and the rest of the image blue.As per the rubric, this
   * method returns a 2D <code>int</code> array structure for use by the <code>ImagePanel</code>
   * class.
   *
   * @see <a href="https://codereview.stackexchange.com/a/160886">CR thread response</a>
   * @return builtImage <code>int[][]</code>
   */
  private int[][] buildTriangle() {

    // Declarations
    int[][] builtImage;
    int z;

    // Definitions
    builtImage = new int[Application.IMAGE_SIZE][Application.IMAGE_SIZE];

    for (int x = 0; x < Application.IMAGE_SIZE; x++) {
      z = builtImage.length - x - 1;

      for (int y = z; y >= 0; y--) {
        builtImage[x][y] = Color.WHITE.getRGB();
      }

      for (int y = z; y < Application.IMAGE_SIZE; y++) {
        builtImage[x][y] = Color.BLUE.getRGB();
      }
    }

    return builtImage;
  }

  /**
   * This method differs slightly from those above, namely <code>Application.buildLetterZ</code>
   * and <code>Application.buildTriangle</code> in that it makes use of different coding not
   * refactored for a new shape. In this method, a set of <code>for</code> loops are used in place
   * of <code>if</code> statements to paint a white cross and color each of the resultant four
   * square quarter panels with a separate color, thus creating a simple little flag shape.
   * <br />
   * <br />
   * The author is sure there is a better way to do this, but is unsure of how specifically to
   * approach the problem differently. Though the use of three nested <code>for</code> loops is
   * certainly undesirable, any other possibilities require the use of potentially taxing
   * conditional statements, which the author was intending to avoid by implementing loops.
   *
   * @return builtImage <code>int[][]</code>
   */
  private int[][] buildFlag() {

    // Declarations
    int[][] builtImage;
    int midpoint;

    // Definitions
    builtImage = new int[Application.IMAGE_SIZE][Application.IMAGE_SIZE];
    midpoint = Application.IMAGE_SIZE / 2;

    for (int x = 0; x < Application.IMAGE_SIZE; x++) {

      // Horizontal white line
      builtImage[x][midpoint] = Color.WHITE.getRGB();

      for (int y = 0; y < Application.IMAGE_SIZE; y++) {

        // Vertical white line
        builtImage[midpoint][y] = Color.WHITE.getRGB();

        // Top-left quarter
        for (int z = midpoint - 1; z >= 0 & y < midpoint; z--) {
          builtImage[y][z] = Color.GREEN.getRGB();
        }

        // Top-right quarter
        for (int z = midpoint - 1; z >= 0 & y > midpoint; z--) {
          builtImage[y][z] = Color.RED.getRGB();
        }

        // Bottom-left quarter
        for (int z = midpoint + 1; z < Application.IMAGE_SIZE & y < midpoint; z++) {
          builtImage[y][z] = Color.BLUE.getRGB();
        }

        // Bottom-right quarter
        for (int z = midpoint + 1; z < Application.IMAGE_SIZE & y > midpoint; z++) {
          builtImage[y][z] = Color.YELLOW.getRGB();
        }
      }
    }

    return builtImage;
  }

  // TimerListener

  /**
   * This utility inner class was simply used to handle <code>Timer</code> events, and to more
   * easily allow for instantiation of a new <code>Timer</code> object to assign to the global
   * <code>animationTimer</code> field. This class contains a single required method, namely
   * <code>actionPerformed</code>, which itself invokes the outer class's own
   * <code>Application.handleTransformation</code> method.
   *
   * Implements <code>ActionListener</code>
   * @see java.awt.event.ActionListener
   * @see <a href="https://stackoverflow.com/questions/23894689">Relevant SO Thread #1</a>
   * @see <a href="https://stackoverflow.com/questions/11438048">Relevant SO Thread #2</a>
   * @author Andrew Eissen
   */
  private final class TimerListener implements ActionListener {

    /**
     * This method implements the only required method of the <code>ActionListener</code> class, and
     * simply invokes the outer class's <code>Application.handleTransformation</code> method as
     * stated above.
     *
     * @param e <code>ActionEvent</code>
     * @return void
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      handleTransformation();
    }
  }
}