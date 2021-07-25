package model;

public class BaseEntity {
    private static long ID_COUNT = 0;
    private long id;

    public BaseEntity() {
        id = ID_COUNT++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
