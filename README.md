# IceCoreDater

Credits

Programming:

Mark Royer and Bashar S. Abdul

Development and Production:

Andrei V. Kurbatov

Testing:

Daniel Dixon

Technical Writing:

Susan Kaspari

The program is a free software distributed under the GNU General
Public License.

The program uses JFreeChart Library created by David Gilbert. JFree
Chart is distributed under the terms of the GNU Lesser General Public
License

Reference:

Kurbatov, A.V., P.A. Mayewski, and B.S. Abdul Jawad (2005), Ice Core
Dating Software for Interactive Dating of Ice Cores, EOS,
TRANSACTIONS, American Geophysical Union, 86(52), Abstract PP33C-1603,
Fall Meet. Suppl. View PDF


The latest version of the ice core dating software is **1.2.0**.  You
may download an executable jar from the releases page http://cci.icecoredata.org/software/download.html.

If you are a developer, you may find the following section to be helpful.

### Developers

This section describes how to build the ice core dating software from
source code.  If you are not a programmer, then this section is most
likely not for you.

### Building

To generate the executable jar file, use Maven (Version 3 or
greater). The following command will download and build the necessary
files.

``` shell
mvn clean package
```

If successful, the `target` directory will contain the following jar
files (along with other items).

``` shell
icecoredater-VERSION-SNAPSHOT.jar
icecoredater-VERSION-SNAPSHOT-jar-with-dependencies.jar
```

These files have `VERSION` replaced with an actual version, for
example, 1.2.0.

The first file `icecoredater-VERSION-SNAPSHOT.jar` is the ice core
dating software compiled without its dependencies.  The file
`icecoredater-VERSION-SNAPSHOT-jar-with-dependencies.jar` is a single
executable jar file, which is what most people will want to use.

### Updating The Version

The version of the software may be updated by modifying the version
tag inside of the [pom.xml](./pom.xml) file.  When the project is
built, this version tag is automatically inserted into
the
[icecoredating.properties](./src/edu/umaine/cs/icecoredater/icecoredating.properties) file,
which is then loaded by the system at run time.  For example, the
system uses the properties file to draw the version information on the
splash screen.  In addition, the [pom.xml](./pom.xml) configuration
uses the version to generate the final executable jar file.


<!--  LocalWords:  IceCoreDater Royer Kurbatov Kaspari JFree
 -->
<!--  LocalWords:  JFreeChart Mayewski EOS AGU mvn icecoredater
 -->
<!--  LocalWords:  xml icecoredating
 -->
