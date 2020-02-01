package main.model.bean;

public class Index1 {
    private String one;
    private String code1;
    private String tow;
    private String attr;
    private String note;
    private String code2;
    private Double weight;
    private Double compreweight;

    public Index1(String one, String code1, String tow, String attr, String note, String code2, Double weight, Double compreweight) {
        this.one = one;
        this.code1 = code1;
        this.tow = tow;
        this.attr = attr;
        this.note = note;
        this.code2 = code2;
        this.weight = weight;
        this.compreweight = compreweight;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getTow() {
        return tow;
    }

    public void setTow(String tow) {
        this.tow = tow;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getCompreweight() {
        return compreweight;
    }

    public void setCompreweight(Double compreweight) {
        this.compreweight = compreweight;
    }

    @Override
    public String toString() {
        return "Index1{" +
                "one='" + one + '\'' +
                ", code1='" + code1 + '\'' +
                ", tow='" + tow + '\'' +
                ", note='" + note + '\'' +
                ", code2='" + code2 + '\'' +
                ", weight=" + weight +
                ", compreweight=" + compreweight +
                '}';
    }
}
