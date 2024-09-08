This project resolves this task:  

1. Create a Multimodule project with two modules:
- common (auxiliary/general classes, jar)
- service (main functionality, jar, depends on the common module)

2. Override the main plugins so that current versions are used everywhere

3. Unit and Integration tests must be configured to run

4. The jacoco plugin must be configured, which generates a report after Integration tests

5. Maven wrapper must be used to build projects