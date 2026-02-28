//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
public static void main(String[] args) {

    TerminalManager terminal = new TerminalManager();

    terminal.mkdir("Parixit");
    terminal.cd("Parixit");

    System.out.println(terminal.pwd());

    terminal.mkdir("Sanghani");

    terminal.mkdir("./Parixit/Dineshbhai");
    terminal.mkdir("/Parixit/Dineshbhai");

    System.out.println(terminal.pwd());
    terminal.cd("..");

    System.out.println(terminal.pwd());
    terminal.cd("./Parixit/Sanghani");
    terminal.cd("../../*/Dineshbhai");

    System.out.println(terminal.pwd());
}
}
