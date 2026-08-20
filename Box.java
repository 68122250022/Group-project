public class Box {
    private String id;

    public Box(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "[" + id + "]";
    }
}