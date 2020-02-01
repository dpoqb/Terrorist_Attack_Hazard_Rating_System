package bean;

public class EventAttribute {
    private int a_id;
    private String attribute_name;
    private Integer missing;
    private String type;

    public int getA_id() {
        return a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public Integer getMissing() {
        return missing;
    }

    public void setMissing(Integer missing) {
        this.missing = missing;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
