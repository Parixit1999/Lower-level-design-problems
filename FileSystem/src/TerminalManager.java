import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TerminalManager {

    private DirectoryNode root;
    private ThreadLocal<DirectoryNode> currentWorkingNode = new ThreadLocal<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public TerminalManager() {
        root = new DirectoryNode("/", null);
        currentWorkingNode.set(root);
    }

    public boolean mkdir(String path) {
        lock.writeLock().lock();
        try{
            String [] components = path.split("/");
            DirectoryNode currentNode = currentWorkingNode.get();
            int startIdx = 0;

            if(path.startsWith("/") || path.startsWith(".")) {
                currentNode = root;
                startIdx = 1;
            }

            Deque<Entry> nodesToExplore = new ArrayDeque<>();
            nodesToExplore.add(currentNode);
            boolean isAdded = false;


            for(int i = startIdx; i < components.length; i++) {
                if(components[i].equals("*")) {
                    int totalSize = nodesToExplore.size();
                    for(int j = 0; j < totalSize;j++) {
                        DirectoryNode directoryNode = (DirectoryNode) nodesToExplore.removeFirst();
                        nodesToExplore.addAll(directoryNode.getChildren().values());
                    }
                    continue;
                }

                int totalQueueSize = nodesToExplore.size();

                if(i == components.length - 1) {
                    for(int j =0;j<totalQueueSize;j++) {

                        DirectoryNode directoryNode = (DirectoryNode) nodesToExplore.removeFirst();
                        if(!directoryNode.getChildren().containsKey(components[i])) {
                            directoryNode.addEntry(new DirectoryNode(components[i], directoryNode));
                            isAdded = true;
                        }
                    }
                } else {
                    for(int j = 0; j < totalQueueSize; j++) {
                        DirectoryNode directoryNode = (DirectoryNode) nodesToExplore.removeFirst();
                        if(directoryNode.getChildren().containsKey(components[i])) {
                            nodesToExplore.add(directoryNode.getChildren().get(components[i]));
                        }
                    }

                    if(nodesToExplore.isEmpty()) {
                        System.out.println("Wrong path:" + path);
                        return false;
                    }
                }
            }
            return isAdded;
        } finally {
            lock.writeLock().unlock();
        }

    }

    public boolean cd(String path) {
        lock.readLock().lock();
        try{

            String [] components = path.split("/");
            Deque<Entry> nodesToExplore = new ArrayDeque<>();

            for (String component : components) {
                if (component.equals(".")) {
                    continue;
                }

                if (component.equals("..")) {
                    if (currentWorkingNode.get().getParent() != null) currentWorkingNode.set(currentWorkingNode.get().getParent());
                    continue;
                }

                if (component.equals("*")) {
                    if(nodesToExplore.isEmpty()) {
                        nodesToExplore.addAll(currentWorkingNode.get().getChildren().values());
                    } else {
                        int totalSize = nodesToExplore.size();
                        for(int i = 0; i < totalSize; i++) {
                            DirectoryNode node = (DirectoryNode) nodesToExplore.removeFirst();
                            nodesToExplore.addAll(node.getChildren().values());
                        }
                    }

                    continue;
                }

                if(nodesToExplore.isEmpty()){
                    if (currentWorkingNode.get().getChildren().containsKey(component)) {
                        nodesToExplore.add(currentWorkingNode.get().getChildren().get(component));
                    } else {
                        System.out.println("Wrong path: " + path);
                        return false;
                    }
                } else{
                    int totalSize = nodesToExplore.size();

                    for(int i = 0; i < totalSize; i++) {
                        DirectoryNode node = (DirectoryNode) nodesToExplore.removeFirst();
                        if(node.getChildren().containsKey(component)) {
                            nodesToExplore.add(node.getChildren().get(component));
                        }
                    }


                    if(nodesToExplore.isEmpty()){
                        System.out.println("Path not found.");
                        return false;

                    }
                }

            }

            if(nodesToExplore.size() == 1) {
                currentWorkingNode.set((DirectoryNode) nodesToExplore.removeFirst());
                return true;
            } else if (nodesToExplore.size() > 1) {
                System.out.println("Ambiguous path: multiple matches found.");
            } else{
                System.out.println("Path not found.");
            }

            return true;
        } finally {
            lock.readLock().unlock();
        }
    }

    public String pwd() {
        lock.readLock().lock();
        try{

            DirectoryNode temp = currentWorkingNode.get();
            List<String> path = new ArrayList<>();

            while(temp.getParent() != null) {
                path.add(temp.getName());
                temp = temp.getParent();
            }
            path.add(".");

            return String.join("/", path.reversed());
        } finally {
            lock.readLock().unlock();
        }
    }
}
