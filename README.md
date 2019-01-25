# IceCoreDater

Credits

Programming:

Bashar S. Abdul and Mark Royer

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

View PDF of AGU Fall 2005 Poster ( 896 K)

Please report any bugs, feedback, questions or technical problems to
Andrei Kurbatov

## Building

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



<!--  LocalWords:  IceCoreDater Bashar Royer Kurbatov Kaspari JFree
 -->
<!--  LocalWords:  JFreeChart Mayewski Jawad EOS AGU mvn icecoredater
 -->
