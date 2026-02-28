import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class Main {
public static void main(String[] args) {
    OperatonFactory operatonFactory = new OperatonFactory();
    TextEditor textEditor = new TextEditor(operatonFactory);

    textEditor.addText(0, 0, "hello");
    System.out.println(textEditor.readLine(0));
    textEditor.addText(1, 0, "world");
    System.out.println(textEditor.readLine(1));

    textEditor.addText(0, 5, "-there");
    System.out.println(textEditor.readLine(1));
    textEditor.deleteText(0, 5, 6);
    System.out.println(textEditor.readLine(0));
    textEditor.undo();
    System.out.println(textEditor.readLine(0));
    textEditor.redo();
    System.out.println(textEditor.readLine(0));


    System.out.println(textEditor.readLine(1));
    textEditor.addText(1, 5, "-wide web");
    //world-wide web
    System.out.println(textEditor.readLine(1));
    textEditor.deleteText(1, 5, 5);
    textEditor.undo();

    // world-wird web
    System.out.print(textEditor.readLine(1));


}
}
