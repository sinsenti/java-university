
JAVAC = javac
JAVA = java
RM = rm -rf
MKDIR = mkdir -p
CP = cp -r

BUILD_DIR = build
CLASSES_DIR = $(BUILD_DIR)/classes
SRC_DIR = src
RES_DIR = resources

all: compile-stage1 compile-stage2

compile-stage1:
	$(MKDIR) $(CLASSES_DIR)
	$(JAVAC) -d $(CLASSES_DIR) -encoding UTF-8 $(SRC_DIR)/stage1/*.java

compile-stage2:
	$(MKDIR) $(CLASSES_DIR)
	$(JAVAC) -d $(CLASSES_DIR) -encoding UTF-8 -cp $(SRC_DIR):$(RES_DIR) $(SRC_DIR)/stage2/*.java
	$(CP) $(RES_DIR)/*.properties $(CLASSES_DIR)/

run-stage1: compile-stage1
	$(JAVA) -cp $(CLASSES_DIR) stage1.Test

run-stage2-en: compile-stage2
	$(JAVA) -Dfile.encoding=UTF-8 -cp $(CLASSES_DIR) stage2.Test en GB

run-stage2-ru: compile-stage2
	$(JAVA) -Dfile.encoding=UTF-8 -cp $(CLASSES_DIR) stage2.Test ru RU

run-stage2-by: compile-stage2
	$(JAVA) -Dfile.encoding=UTF-8 -cp $(CLASSES_DIR) stage2.Test be BY

clean:
	$(RM) $(BUILD_DIR)

.PHONY: all compile-stage1 compile-stage2 run-stage1 run-stage2-en run-stage2-ru run-stage2-by clean
