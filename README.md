# greeting
Small spring project to play around with LLM message generation for birthdays

## Tools
### Ollama
I have used ollama with a llama3 model as an LLM for greeting generation. The tools was selected as it 
 is relatively easy to handle. It can be installed locally, but it also has a dockerized version, 
which makes this very portable.
Please see [compose.yml](compose.yml) to launch the service ( just launching the pod from docker
won't work immediately, as you need to start the service, pull and launch the model )

I have selected one of the smaller models since the point here is to create a POC, not the highest
greeting quality. The selected model was: llama3.2:3b as this is only 2GB in size and has small 
requirements with only 3B nodes.

### OpenAPI
As a sandbox environment for playing around with the REST API, a swagger has been added with OpenAPI docs.
The [swagger page](http://localhost:8080/swagger-ui/index.html) should be available after launching the application.

### Persistence
To persist data a file H2 db is used. For testing this is replaced with an in-memory version of H2.

### Source format
I used the google source format plugin in IntelliJ to format the project as that is a widely used formatter. 
However this is not supplied with the project. 

## Tests
I used JUnit with mockito for the tests. To "showcase" both integration and unit tests, 
[PersonRepositoryTest.java](src%2Ftest%2Fjava%2Fhu%2Fgerab%2Fgreeting%2Fpersistence%2FPersonRepositoryTest.java)
 was created with a full Spring context ( with a different test datasource) and the 
[PersonServiceImplTest.java](src%2Ftest%2Fjava%2Fhu%2Fgerab%2Fgreeting%2Fservice%2FPersonServiceImplTest.java) 
was implemented with stubs and 
[OllamaLlama3ServiceTest.java](src%2Ftest%2Fjava%2Fhu%2Fgerab%2Fgreeting%2Fservice%2FOllamaLlama3ServiceTest.java) 
with mockito and hamcrest.

## How to run
### ollama dependency
To run the application first you will need to run the Ollama container. Please see [compose.yml](compose.yml).
In case you are experiencing any issues with the ollama container compose, 
one can achieve the same by intalling ollama directly from https://ollama.com/, or running the container directly with commands by:
```
docker run -d -v ./.ollama_data:/root/.ollama -p 11434:11434 --name ollama ollama/ollama:0.9.1-rc0
```

and then executing the commands (either on the installed service or in the container):
```
ollama serve &
ollama run llama3.2:3b
```
### Application
Once ollama is running, please start the main method of [Application.java](src%2Fmain%2Fjava%2Fhu%2Fgerab%2Fgreeting%2FApplication.java).
The REST api can be conveniently interacted with via swagger: http://localhost:8080/swagger-ui/index.html