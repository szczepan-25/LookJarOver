package pl.edu.wat.wcy.lookjarover.listview;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class CtConstructorListCell {

    private final String name;
    private final CtConstructor ctConstructor;

    public CtConstructorListCell(String name, CtConstructor ctConstructor) {
        this.ctConstructor = ctConstructor;
        this.name = name;
    }

    public CtConstructor getCtConstructor() {
        return ctConstructor;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(name);
        sb.append("(");

        try {
            CtClass[] params = ctConstructor.getParameterTypes();

            boolean first = true;
            for (CtClass p : params) {
                if (first) first = false;
                else sb.append("; ");
                sb.append(p.getName());
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        sb.append(")");

        return sb.toString();
    }

}
