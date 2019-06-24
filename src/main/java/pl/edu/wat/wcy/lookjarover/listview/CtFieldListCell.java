package pl.edu.wat.wcy.lookjarover.listview;

import javassist.CtField;
import javassist.NotFoundException;

public class CtFieldListCell {

    private final String name;
    private final CtField ctField;

    public CtFieldListCell(String name, CtField ctField) {
        this.name = name;
        this.ctField = ctField;
    }

    public CtField getCtField() {
        return ctField;
    }

    @Override
    public String toString() {

        String returnType = "";
        try {
            returnType = ctField.getType().getName();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return  returnType + " " + name;
    }
}