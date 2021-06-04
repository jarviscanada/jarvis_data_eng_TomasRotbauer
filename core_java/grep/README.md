# Introduction
In this project, I used Java 8 to develop an application similar to the Linux builtin
`grep` feature. Basically, the program searches recursively through a root directory
to map out any and all files contained within it. Next, it reads each of these files line-by-line
using a BufferedReader, and processes each extracted line with Java 8 stream features. Finally,
it returns all the lines of files that matched the specified Java regular expression. 
The whole application is packaged in a Docker image, and Maven was used to manage the build 
process.

# Quick Start
* Compile with Maven:
`mvn clean package` <br />
* Run locally: `java -jar path/to/grep-1.0-SNAPSHOT.jar <regex> <rootPath> <outFile>`<br />
* Run in Docker container: `docker run --rm tomasrotbauer/grep <regex> <rootPath> <outFile>`

# Implemenation
The following pseudocode highlights the application's core algorithm. It corresponds
to the `process()` function inside JavaGrepImp.java, which is the implementation of the 
JavaGrep.java interface. The interface acts as the code skeleton of the project.
## Pseudocode
```
function process() {
    strings_list = []
    files = getAllFilesRecursively(rootDirectory)
    
    for each file in files {
        for each line in file {
            if line contains the specified pattern:
                strings_list.append(line)
        }
    }
    
    write strings_list to the specified output file
    Done.
}   
```

## Performance Issue
In certain extreme cases, this program fails with an outOfMemory error.
The reason for this is that, like with any other Java program, the heap memory allocated by
the JVM is insufficient. To fix this is simple - just replace all data containers with buffers
to limit memory usage in any given iteration (e.g. BufferedReader).

# Test
The application was tested manually, and was especially easy to do given that it is an
identical twin to the Linux grep function. For printing information and other messages, the
log4j logger was used. Every individual component was tested along
the way to ensure easy debugging. This was especially important given that each component
relies on all the other components below it. This is the order of component testing:
1. File discovery - make sure all files are found under the root directory.
2. Ensure that a file is read correctly and all lines conforming to the specified regular
expression are found. For this step, the app was run from a directory containing one file with
   just some simple text.
3. Writing results to output file - check for content/formatting.

Finally, the completed app was tested on a larger directory with many files and sub-directories,
and the results were compared against the Linux grep function.

# Deployment
In order to facilitate distribution, the final product was packaged into a Docker image named
tomasrotbauer/grep. A simple Dockerfile was written which builds the image from the 
openjdk:8-alpine image and copies over the compiled jar file. To run the container, execute:
`docker run --rm tomasrotbauer/grep <regex> <rootPath> <outFile>`. The entrypoint is already
set to launch the application, hence the three arguments at the end.

# Improvement
1. Make the application faster by integrating multiple threads. Currently, this is a single-threaded
program.
2. Reimplement the file discovery function `listFiles(String rootDir)` with loops instead
of recursion to save stack memory.
3. Add line number printing for each matched line for the case of large files to make it 
easier to locate the match.