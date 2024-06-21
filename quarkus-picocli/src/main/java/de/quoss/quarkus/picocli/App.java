package de.quoss.quarkus.picocli;

import picocli.CommandLine;

@CommandLine.Command
public class App implements Runnable {

    @CommandLine.Option(names = { "-n", "--name"}, description = "Who will we greet?", defaultValue = "World")
    String name;

    @Override
    public void run() {
        System.out.format("Hello %s!%n", name);
    }

}
