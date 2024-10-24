package net.dataforte.maven.moduleinfogenerator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ModuleInfoYml {
    private String name;
    private String version;
    private boolean open;
    private boolean synthetic;
    private boolean mandated;
    private String sourceFile;
    private String mainClass;
    private List<String> packages;
    private List<ModuleRequire> requires = new ArrayList<>();
    private List<ModuleExport> exports = new ArrayList<>();
    private List<ModuleExport> opens = new ArrayList<>();
    private List<String> uses = new ArrayList<>();
    private List<ModuleProvide> provides = new ArrayList<>();
    private List<ModuleAnnotation> annotations = new ArrayList<>();

    public ModuleInfoYml() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(final boolean open) {
        this.open = open;
    }

    public boolean isSynthetic() {
        return synthetic;
    }

    public void setSynthetic(final boolean synthetic) {
        this.synthetic = synthetic;
    }

    public boolean isMandated() {
        return mandated;
    }

    public void setMandated(final boolean mandated) {
        this.mandated = mandated;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(final String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(final String mainClass) {
        this.mainClass = mainClass;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(final List<String> packages) {
        this.packages = packages;
    }

    public List<ModuleRequire> getRequires() {
        return requires;
    }

    public void setRequires(final List<ModuleRequire> requires) {
        this.requires = requires;
    }

    public List<ModuleExport> getExports() {
        return exports;
    }

    public void setExports(final List<ModuleExport> exports) {
        this.exports = exports;
    }

    public List<ModuleExport> getOpens() {
        return opens;
    }

    public void setOpens(final List<ModuleExport> opens) {
        this.opens = opens;
    }

    public List<String> getUses() {
        return uses;
    }

    public void setUses(final List<String> uses) {
        this.uses = uses;
    }

    public List<ModuleProvide> getProvides() {
        return provides;
    }

    public void setProvides(final List<ModuleProvide> provides) {
        this.provides = provides;
    }

    public List<ModuleAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(final List<ModuleAnnotation> annotations) {
        this.annotations = annotations;
    }
}
