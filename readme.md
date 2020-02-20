This project is just example of using Cucumber with Java+WebDriver Selenium:

1. you have one feature and one configuration ini file
2. currently it's working with Firefox - all possible artifacts
   are taken with Maven
3. feature file shows using series of data (one of tests is executed 2x)
4. all page content logic is moved into PrivateClients.java
5. there are three tests available (one executed twice like mentioned
   in point 3) - we search for page titles, links, navigate and search for
   elements using XPath, wait for opening some page parts, etc.
6. one test (intentionally!) is left in a little bit flaky state - clicking
   on link using Java Script
7. we use page of one of the most important world banks

To run tests use "gradle cucumber" from command line
or
import project into IntelliJ + use runner from RunCucumberTests.

Tested with Ubuntu 64-bit and IntelliJ.
