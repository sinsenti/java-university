JAVAC = javac
JAVA  = java
SRC   = NewsItem.java NewsRepository.java NewsServer.java ClientHandler.java NewsClient.java
CLASSES = $(SRC:.java=.class)

all: compile

compile: $(CLASSES)

%.class: %.java
	$(JAVAC) $<

run-server: compile
	$(JAVA) NewsServer 5555

run-client: compile
	$(JAVA) NewsClient localhost 5555

clean:
	rm -f *.class
