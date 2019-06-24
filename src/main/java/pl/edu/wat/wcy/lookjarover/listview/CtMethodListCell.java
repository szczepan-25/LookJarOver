package pl.edu.wat.wcy.lookjarover.listview;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class CtMethodListCell {

    private final String name;
    private final CtMethod ctMethod;

    public CtMethodListCell(String name, CtMethod ctMethod) {
        this.name = name;
        this.ctMethod = ctMethod;
    }

    public CtMethod getCtMethod() {
        return ctMethod;
    }

    @Override
    public String toString() {

        String returnType = "";
        try {
            returnType = ctMethod.getReturnType().getName();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder params = new StringBuilder();
        try {
            CtClass[] parameters = ctMethod.getParameterTypes();

            boolean first = true;
            for (CtClass p : parameters) {
                if (first) first = false;
                else params.append("; ");
                params.append(p.getName());
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return returnType + " " + name
                + "(" + params + ")";
    }
}
