# xrdfileoperations

A Java module for reading/writing from/to various crystallographic data formats.

# Requirements

In order to begin development with this repository, the JDK and the latest release for Java 8 should be installed on your machine.

If the JDK and/or Java 8 is/are not installed on your machine:

1.  Go to the [Java SE Development Kit 8 page](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) on Oracle's website
2.  Read/accept the [Oracle binary Code License Agreement for Java SE](http://www.oracle.com/technetwork/java/javase/terms/license/index.html)
3.  Select the version that best suits your OS/development environment and click on the file link to download it
4.  Open the installer you downloaded and follow all instructions/prompts until Java 8 and the JDK are installed on your machine

Your development environment is now ready for Java.

# Instructions

Next, clone this repository to your machine.

If you're developing the old-school way using your favorite text editor and the Terminal (or Command Prompt for you Windows Junkies), that's all you need!

If you're planning to wrap this module in a project with your favorite IDE, please see instructions below for some tips on how to get started.

## When using IntelliJ

If IntelliJ IDEA is not installed, download and install the latest version from the [JetBrains website](https://www.jetbrains.com/idea/).

Next, create the project files:

1.  Open up IntelliJ IDEA and select "New Project" from the startup menu
2.  Navigate to the directory your repository was cloned to
3.  Open the directory and select it from within the file dialog - the name of the repository should now be listed as the project name
4.  Once you've confirmed that the names match, click "Finish" to create the .iml file and .idea project directory

Once you've created the project files, you must now set up the sources and test roots.

To set up the sources root:

1.  Open the Project tool window using either Command + 1 or the "View" menu
2.  With secondary click enabled, right-click on the "src" folder to pull up the project item context menu
3.  Navigate to the bottom of the menu and hover over the "Mark Directory As" sub-menu
4.  Click on "Sources Root"

The directory's folder icon should turn blue in the Project tool window, indicating that it is now the root directory for storing project source files.

To set up the tests root:

1.  With the Project tool window still open, right-click on the "test" folder to pull up the project item context menu
2.  Navigate to the bottom of the menu and hover over the "Mark Directory As" sub-menu
3.  Click on "Test Sources Root"

The directory's folder icon should turn green in the Project tool window, indicating that it is now the root directory for storing project unit tests.

Next, you need to confirm that the Project Structure is properly configured.

1.  Go to "File" -> "Project Structure..." (or hit Command + ;)
2.  Go to "Project" under "Project Settings"
3.  Confirm that the Project SDK it at least version 1.8 - if this is not the case, locate the directory where SDK 1.8 is located and select it
4.  Confirm that the Project Language Level is set to 8.0 - if this is not the case, click on the ChoiceBox and select PLL 8.0
5.  Save these settings by clicking "Apply"

Your project structure is ready to go!

One last thing:

1.  Click on one of the files in the test sources root
2.  If the org.junit packages for jUnit4 have not been loaded, click "Fix" in the popup that displays the error for missing jUnit packages

All jUnit4 packages should be attached to the module project and you should be ready to run unit tests.

Happy development!
