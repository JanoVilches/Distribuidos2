# Distribuidos2
Nuevo repositorio para la bella tarea de distribuidos.

Integrantes:
    Abdel Sandoval 201573504-2
    Alejandro Vilches 201573554-9

Instrucciones de Compilacion:
    1. Ir al directorio Distribuidos2.
    2. Hacer make.

Instrucciones de Ejecucion:

    1. En el mimso directorio hacer "make Servidor"
    2. Siga las instrucciones, dadas por el programa, para definir las IP y los puertos de cada maquina en el programa:
        - maquina dist41: IP = 10.6.40.181 // Puerto = 50051 (recomendacion)
        - maquina dist41: IP = 10.6.40.182 // Puerto = 50052 (recomendacion)
        - maquina dist41: IP = 10.6.40.183 // Puerto = 50053 (recomendacion)
        - maquina dist41: IP = 10.6.40.184 // Puerto = 50054 (recomendacion)

    3. IMPORTANTE: PARA UNA CORRECTA EJECUCION, SOLO ELEGIR UNA DE LAS MAQUINAS PARA INICIAR EL PROCESO DE COORDINACION. PARA ESTO INGRESAR "1" o "0" SEGUN CORRESPONDA. SI = 1 // NO = 0.
    4. Una vez incializadas las maquinas presione ENTER para inicial el programa en cada una de las maquinas.


Comparación de algoritmos:

    1. se envian los cambios al coordinador actual y este propaga a las replicas para mantener consistencia completa (consistencia secuencial).
    2. cada maquina localmente hace el cambio, y envia (propaga) el append de informacion hacia los logs de las otras maquinas. Para lograr esto tendran que hacer algun protocolo             decentralizado de exclusion mutua para que los logs mantengan el orden de informacion que se genera de manera concurrente.

    Se decide realizar el primer algoritmo, donde "se envian los cambios al coordinador actual y este propaga a las replicas para mantener consistencia completa", ya que la forma
    en que se programa tanto el Thread del Servidor como el Thread de los clientes, queda muy afin a este tipo de ejecuion, puesto que de esta forma cada maquina le indica al coordinador
    por medio del Thread Cliente, que los cambios han sido realizados y este procede a indicarle a todos los demas los cambios que se realizaron para que todos puedan tener los cambios
    apenas son realizados. 
    
    Realizando una comparacion entre ambos metodos, a modo de argumentacion respecto a ventajas y desventajas de cada uno, el primero (1) generaria un cantidad mayor de mensajes entre las maquinas con el coordinador, ya que es necesario informar todo lo que se realizo para que el coordinador pueda traspasar esta informacion a las demas maquinas, mientras que el segundo, requiere de menos mensajes por el uso de tecnicas como semaforos, sin embargo esto puede generar efectos colaterales mayores, como son el caso de deadlocks. Ademas de que si bien el segundo busca, disminuir la latencia, mediante la espera activa de colas, en la realidad, termianan perdiendo mas tiempo del que ahorra, debido a la complejidad de su estructura. Por lo que el uso de una consistencia secuencial se considera mas sencillo, ya que es posible llevar el control del orden en que llegan las solicitudes y segun ese orden ir derivando las escrituras a cada uno de los servidores, para luego simplemente desde el coordinador realizar el paso de la info a los demas servidores y mantener la concurrencia controlada.