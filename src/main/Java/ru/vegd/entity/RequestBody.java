package ru.vegd.entity;

import java.util.ArrayList;
import java.util.List;

public class RequestBody {
    private Report report = new Report();
    private List<Inputs> inputs = new ArrayList<>();
    private List<Outputs> outputs = new ArrayList<>();
    private String sql;

    public String getReportName() {
        return report.getName();
    }

    public void setReportName(String name) {
        report.setName(name);
    }

    public String getReportDescription() {
        return report.getDescription();
    }

    public void setReportDescription(String description) {
        report.setDescription(description);
    }

    public void addInputs() {
        inputs.add(new Inputs());
    }

    public String getInputsVar(Integer id) {
        return inputs.get(id).getVar();
    }

    public void setInputsVar(Integer id, String var) {
        inputs.get(id).setVar(var);
    }

    public String getInputsLabel(Integer id) {
        return inputs.get(id).getLabel();
    }

    public void setInputsLabel(Integer id, String label) {
        inputs.get(id).setLabel(label);
    }

    public String getInputsType(Integer id) {
        return inputs.get(id).getType();
    }

    public void setInputsType(Integer id, String type) {
        inputs.get(id).setType(type);
    }

    public void addOutputs() {
        outputs.add(new Outputs());
    }

    public Outputs getOutputsById(Integer id) {
        return outputs.get(id);
    }

    public Integer getOutputsIndex(Integer id) {
        return outputs.get(id).getIndex();
    }

    public void setOutputsIndex(Integer id, Integer index) {
        outputs.get(id).setIndex(index);
    }

    public String getOutputsLabel(Integer id) {
        return outputs.get(id).getLabel();
    }

    public void setOutputsLabel(Integer id, String label) {
        outputs.get(id).setLabel(label);
    }

    public String getOutputsType(Integer id) {
        return outputs.get(id).getType();
    }

    public void setOutputsType(Integer id, String type) {
        outputs.get(id).getType();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "report=" + report +
                ", inputs=" + inputs.toString() +
                ", outputs=" + outputs.toString() +
                ", sql='" + sql + '\'' +
                '}';
    }
}

class Report {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class Inputs {
    private String var;
    private String label;
    private String type;

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class Outputs {
    private Integer index;
    private String label;
    private String type;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


