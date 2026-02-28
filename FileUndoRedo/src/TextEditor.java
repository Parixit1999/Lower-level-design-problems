import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

class TextEditor implements TextEditorOperation{

    HashMap<Integer, String> editor;
    Stack<Operation> operation = new Stack<>();
    Stack<Operation> undoStack = new Stack<>();
    OperatonFactory operationFactory;

    public TextEditor(OperatonFactory operationFactory) {
        this.operationFactory = operationFactory;
        this.operation = new Stack<>();
        this.editor = new HashMap<>();
    }

    private void editText(int row, int col, String text) {
        this.editor.merge(row, text, (old_val, new_val) -> {
            return old_val.substring(0, col) + new_val + old_val.substring(col, old_val.length());
        });
    }

    private void removeText(int row, int startCol, int lenght) {
        this.editor.computeIfPresent(row, (key, value) -> {

            return value.substring(0, startCol) + value.substring(startCol + lenght, value.length());
        });
    }

    @Override
    public void addText(int row, int col, String text) {
        this.editText(row, col, text);
        this.operation.add(this.operationFactory.createOperation(row, col, text, OperationType.ADD));
        if(!this.undoStack.empty()) this.undoStack.removeAllElements();
    }

    @Override
    public void deleteText(int row, int startCol, int lenght) {
        this.operation.add(this.operationFactory.createOperation(row, startCol, this.editor.getOrDefault(row, "").substring(startCol, startCol + lenght), OperationType.DELETE));
        this.removeText(row, startCol, lenght);
        if(!this.undoStack.isEmpty()) this.undoStack.removeAllElements();
    }

    @Override
    public void undo() {
        if (this.operation.isEmpty()) return;

        Operation lastAction = this.operation.pop();

        if (lastAction.operationType == OperationType.ADD) {
            removeText(lastAction.row, lastAction.col, lastAction.text.length());
        } else {
            editText(lastAction.row, lastAction.col, lastAction.text);
        }

        this.undoStack.push(lastAction);
    }

    @Override
    public void redo() {
        if (this.undoStack.isEmpty()) return;

        Operation undoneAction = this.undoStack.pop();

        if (undoneAction.operationType == OperationType.ADD) {
            editText(undoneAction.row, undoneAction.col, undoneAction.text);
        } else {
            removeText(undoneAction.row, undoneAction.col, undoneAction.text.length());
        }

        this.operation.push(undoneAction);
    }
    @Override
    public String readLine(int row) {
        return this.editor.getOrDefault(row, "");
    }
}
