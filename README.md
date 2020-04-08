# jatziadx
## How to run
1. Ensure you have kotlin compiler on your path
2. Ensure you have the environment variables JDK_HOME and KOTLIN_HOME correctly set.
3. Run this to compile:

```cmd
kotlinc src -include-runtime -kotlin-home $KOTLIN_HOME -jdk-home $JDK_HOME -cp $JDK_HOME\lib\kotlin-test.jar -d jatziadx.jar
```

4. Then tun this to run the code (the help should show up):

```cmd
java -jar jatziadx.jar
```

