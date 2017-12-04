package udacityteam.healthapp.models;

/**
 * Created by vvost on 11/21/2017.
 */

public class Model {

    private String name;
    private String offset;
    private String id;

    public Model()
    {
        this.name = name;
        this.offset = offset;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        //      return super.toString();
        return name;
    }

    public String toString3()
    {
        return id;
    }
}
