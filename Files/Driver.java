/**
 * Driver.java - Simply contains the initializing <code>main</code> method
 * Begun 08/24/18
 * @author Andrew Eissen
 */
//package graphicsprojectone;

/**
 * This class simply contains the <code>main</code> method, used to initialize a new
 * <code>Application</code> object. Originally, this method was simply appended to the end of the
 * <code>Application</code> class, but was moved into a separate class in accordance with the
 * <a href="https://en.wikipedia.org/wiki/Single_responsibility_principle">single responsibility
 * principle</a>.
 *
 * @author Andrew Eissen
 */
final class Driver {

  /**
   * Method simply creates a new <code>Application</code> instance.
   *
   * @param args <code>String[]</code>
   * @return void
   */
  public static void main(String[] args) {
    final Application newApp = new Application();
  }
}