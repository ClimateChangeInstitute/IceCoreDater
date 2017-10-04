ROOTDIR=$(PWD)
LIBS=libs
SRCDIR=src
BINDIR=bin
DOCSDIR=docs
TARGETJVM=1.5

MAINCLASS=edu.umaine.cs.icecoredater.IceCoreDating
EXECJAR=icedater.jar
LIBRARIES=$(LIBS)/jfreechart-1.0.12.jar:$(LIBS)/jcommon-1.0.15.jar
UNIXEXEC=icedater.sh
WINEXEC=icedater.bat

MANIFEST=Manifest-Version: 1.0\\r\\nMain-Class: $(MAINCLASS)\\r\\nClass-Path: $(subst :, ,$(LIBRARIES))\\r\\n\\r\\n 

default: executables

all: jar javadoc executables

compileclasses:
	@-mkdir -p $(BINDIR)
	@javac -source $(TARGETJVM) -cp $(LIBRARIES) $(SRCDIR)/edu/umaine/cs/icecoredater/*.java -d $(BINDIR)
	@cp -r $(SRCDIR)/edu/umaine/cs/icecoredater/images $(BINDIR)/edu/umaine/cs/icecoredater/images

executables: jar
	@echo 'java -Dsun.java2d.noddraw=true -jar ' $(EXECJAR) > $(UNIXEXEC)
	@echo 'START javaw -Dsun.java2d.noddraw=true -jar ' $(EXECJAR) '\nREM pause' > $(WINEXEC)
	@chmod u+x $(UNIXEXEC)
	@chmod u+x $(WINEXEC)
	
jar: compileclasses
	@echo $(MANIFEST) > manifest.mf
	@jar -cfm $(EXECJAR) manifest.mf -C $(BINDIR) .

jaronly: jar cleanclassfiles	

javadoc:
	@javadoc -classpath :$(LIBRARIES):$(SRCDIR): -d $(DOCSDIR) -subpackages edu.umaine.cs

clean: cleanclassfiles cleandocs cleanjar cleanexecutables

cleanjar:
	@-rm $(EXECJAR)
	@-rm manifest.mf

cleanexecutables:
	@find ./ \( -name $(UNIXEXEC) -o -name $(WINEXEC) \) -type f -exec rm '{}' \;


cleanclassfiles:
	@find ./ \( -name "*~" -o -name "*.class" \) -type f -exec rm '{}' \;
	@-rm -rf $(BINDIR)

cleandocs:
	@-rm -rf $(DOCSDIR)
