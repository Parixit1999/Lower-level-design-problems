import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

interface TextEditorOperation{
    void addText(int row, int col, String text);
    void deleteText(int row, int startCol, int lenght);
    void undo();
    void redo();
    String readLine(int row);
}
