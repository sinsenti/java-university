# Простой Makefile для Car Registry System

compile:
	javac -encoding UTF-8 *.java

run:
	java CarDatabaseApp


clean:
	rm -f *.class cars.dat

all: clean compile run

help:
	@echo "Доступные команды:"
	@echo "  make compile - компиляция проекта"
	@echo "  make run     - компиляция и запуск"
	@echo "  make clean   - удаление class файлов и базы данных"
	@echo "  make all     - выполняются все команды "

.PHONY: compile run clean help all
