package net.dataforte.maven.moduleinfogenerator;

/**
 *
 */
public class ModuleRequire {
    private String module;
    private String version;
    private boolean static_;
    private boolean synthetic;
    private boolean mandated;
    private boolean transitive;

    public ModuleRequire() {
    }

    public ModuleRequire(final String module, final String version, final boolean static_, final boolean synthetic,
                         final boolean mandated, final boolean transitive) {
        this.module = module;
        this.version = version;
        this.static_ = static_;
        this.synthetic = synthetic;
        this.mandated = mandated;
        this.transitive = transitive;
    }

    public String getModule() {
        return module;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public boolean isStatic() {
        return static_;
    }

    public void setStatic(final boolean static_) {
        this.static_ = static_;
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

    public boolean isTransitive() {
        return transitive;
    }

    public void setTransitive(final boolean transitive) {
        this.transitive = transitive;
    }

    public int flags() {
        return ((transitive ? 0x0020 : 0) |(static_ ? 0x0040 : 0) | (synthetic ? 0x1000 : 0) | (mandated ? 0x8000 : 0));
    }
}
