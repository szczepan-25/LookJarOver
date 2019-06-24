package pl.edu.wat.wcy.lookjarover.tree;

import javassist.CtClass;

import java.io.File;

public class CustomFile {

    private final boolean add;
    private final File file;
    private final CtClass ctClass;

    public CustomFile(File file, CtClass ctClass, boolean add) {
        this.file = file;
        this.ctClass = ctClass;
        this.add = add;
    }

    public boolean isAdd() {
        return add;
    }

    public File getFile() {
        return file;
    }

    public CtClass getCtClass() {
        return ctClass;
    }
}
