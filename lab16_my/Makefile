JAVAC = javac
JAVA  = java
SRC_DIR = .
GUI_DIR = gui_src

SRC_FILES = $(wildcard $(SRC_DIR)/*.java)
GUI_FILES = $(wildcard $(GUI_DIR)/*.java)
CLASS_FILES = $(SRC_FILES:.java=.class) $(GUI_FILES:.java=.class)
CONSOLE_MAIN = CarDatabaseApp
GUI_MAIN     = CarDatabaseGuiApp
CP = .:$(GUI_DIR)

.PHONY: all console gui run_console run_gui clean help
all: console gui

console: $(SRC_FILES)
	$(JAVAC) -encoding UTF-8 $(SRC_FILES)

gui: $(SRC_FILES) $(GUI_FILES)
	$(JAVAC) -encoding UTF-8 $(SRC_FILES) $(GUI_FILES)

run_console: console
	$(JAVA) -cp . $(CONSOLE_MAIN)

run_gui: gui
	$(JAVA) -cp $(CP) $(GUI_MAIN)

clean:
	rm -f $(CLASS_FILES) cars.dat

help:
	@echo "Доступные цели:"
	@echo "  make console     - компиляция только консольной версии"
	@echo "  make gui         - компиляция консоли + GUI"
	@echo "  make run_console - запустить консольную версию"
	@echo "  make run_gui     - запустить GUI-версию"
	@echo "  make clean       - удалить .class и cars.dat"
	@echo "  make all         - собрать всё (console + gui)"
