all: Enfermero.class ClienteThread.class Doctor.class Paramedico.class Servidor.class

Enfermero.class: Enfermero.java
	javac Enfermero.java

ClienteThread.class: ClienteThread.java
	javac ClienteThread.java

Doctor.class: Doctor.java
	javac Doctor.java

Paramedico.class: Paramedico.java
	javac Paramedico.java

Servidor.class: Server.java
	javac -cp .:json-simple-1.1.1.jar Server.java

Servidor:
	java -cp .:json-simple-1.1.1.jar Server

Cliente:
	java Cliente

clean :
	rm -f *.class
