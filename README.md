# Xenon [![](https://jitpack.io/v/Sulaxan/Xenon.svg)](https://jitpack.io/#Sulaxan/Xenon)
A simple Java command library to create commands using classes.

## Getting Started
All commands start by creating a class and annotating it with the @Command annotation.

```java
@Command(names = {"test"}, desc = "Tests basic functionality of Xenon")
public class TestCommand {
}
```

### Root Command
You can add a root command simply by creating a new method with its first parameter as either
CommandSender or some implementation of it.

```java
@Command(names = {"test"}, desc = "Tests basic functionality of Xenon")
public class TestCommand {

    @Root
    public void root(CommandSender sender, String[] args) {
        // code
    }
}
```

Note: ``String[]`` is an optional second parameter.

### Sub Commands
You can also add sub commands in a similar fashion!

```java
@Command(names = {"test"}, desc = "Tests basic functionality of Xenon")
public class TestCommand {
    
    // ...
    
    @SubCommand("subcommand")
    public void aSubCommand(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("hi!");
    }
}
```

Again, similar to the root method, ``String[]`` is an optional second parameter.

### Options
Options are a good way to change the behaviour of your command. All options 
use a field to set whether they are present in the command or not.

There are two ways to declare an option:
```java
@Command(names = {"test"}, desc = "Tests basic functionality of Xenon")
public class TestCommand {

    // First way
    @Option(value = "p", longOption = "print", required = false, desc = "This value prints something")
    private boolean print;
    
    // Second way
    @Option(value = "data", desc = "A data option")
    private String data;
}
```

#### Boolean Type Options
These options only have to states: ``true`` and ``false``. If an option appears in a
command, the field will be set to true.

#### String Type Options
These options depend on some argument appearing right after the option is declared,
otherwise a runtime command parse exception will be thrown.

Note: At this time, only a max of one argument can be specified after the option.

#### Option Syntax
Commands are parsed using Apache's Common CLI library. The default command line parser 
requires options to have the following format.

##### Short Option
``command -option``

##### Long Option
``command --option``

### Permissions
Adding permission checks to your command allow further control in the behaviour of them.
Both the root and sub commands have the ability to check permissions for, and are formatted
as follows.

```java
@Command(names = {"test"}, desc = "Tests basic functionality of Xenon")
public class TestCommand {

    // code

    // For root
    @PermissionCheck(scope = PermissionScope.ROOT)
    public boolean hasPermission(CommandSender sender) {
        return true;
    }

    // For a sub command
    @PermissionCheck(subCommands = {"subcommand"})
    public boolean hasPermissionSubCommand(ConsoleCommandSender sender) {
        return true;
    }
}
```

Note: Permission methods must have a return type of boolean, and must only have one parameter:
either CommandSender, or some implementation of it.

## Examples
The code from ``Getting Started`` can be found in ``src/test/java/com/github/sulaxan/TestCommand.java``.

## Dependency
Xenon can be used as a dependency in your project using Maven.

Add the following repository:
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

Add the following dependency:
```xml
<dependency>
	<groupId>com.github.Sulaxan</groupId>
	<artifactId>Xenon</artifactId>
	<version>v1.0</version>
</dependency>
```
