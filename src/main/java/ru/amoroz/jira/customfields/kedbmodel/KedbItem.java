package ru.amoroz.jira.customfields.kedbmodel;

public class KedbItem {

    private String key;
    private String param1;
    private String param2;
    private String problem;
    private String link;

    public KedbItem() {
    }

    public KedbItem(String key, String param1, String param2, String problem, String link) {
        this.key = key;
        this.param1 = param1;
        this.param2 = param2;
        this.problem = problem;
        this.link = link;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}