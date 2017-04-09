package nstu.geo.presentation.model;


public final class SegyParameter {
    private final String name;
    private final String value;

    public SegyParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }


    public void setName(String name) {
        name = name;
    }

    public void setValue(String value) {
        value = value;
    }
}
