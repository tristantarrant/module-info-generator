package net.dataforte.maven.moduleinfogenerator;

import java.util.ArrayList;
import java.util.List;

public class ModuleExport {
    private String package_;
    private List<String> to = new ArrayList<>();
    private boolean synthetic;
    private boolean mandated;
    private boolean pattern = false;

    public ModuleExport() {
    }

    public ModuleExport(final String package_, final List<String> to, final boolean synthetic, final boolean mandated) {
        this.package_ = package_;
        this.to = to;
        this.synthetic = synthetic;
        this.mandated = mandated;
        this.pattern = false;
    }

    public String getPackage() {
        return package_;
    }

    public void setPackage(final String package_) {
        this.package_ = package_;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(final List<String> to) {
        this.to = to;
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

    public boolean isPattern() {
        return pattern;
    }

    public void setPattern(boolean pattern) {
        this.pattern = pattern;
    }

    public ModuleExport withPackageName(String package_) {
        return new ModuleExport(package_, to, synthetic, mandated);
    }

    public int flags() {
        return (synthetic ? 0x1000 : 0) | (mandated ? 0x8000 : 0);
    }
}
