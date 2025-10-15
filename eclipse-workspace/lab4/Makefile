JAVAC = javac
JAVA = java

SRC_DIR = src
BUILD_DIR = build

MAIN_CLASS = TriangleTest

JAR_FILE = TriangleApp.jar

.PHONY: all clean build jar run

all: run

clean:
	rm -rf $(BUILD_DIR)
	rm -f $(JAR_FILE)
	rm -f manifest.txt

build:
	mkdir -p $(BUILD_DIR)
	$(JAVAC) -d $(BUILD_DIR) $(SRC_DIR)/*.java
	@echo "Компиляция завершена"

jar: build
	echo "Main-Class: $(MAIN_CLASS)" > manifest.txt
	jar cvfm $(JAR_FILE) manifest.txt -C $(BUILD_DIR) .
	rm -f manifest.txt
	@echo "JAR-архив создан: $(JAR_FILE)"

run: jar
	@echo "Запуск приложения..."
	$(JAVA) -jar $(JAR_FILE)

run-direct: build
	@echo "Запуск приложения..."
	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS)
