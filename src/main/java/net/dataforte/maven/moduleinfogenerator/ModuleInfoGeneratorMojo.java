package net.dataforte.maven.moduleinfogenerator;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.yaml.snakeyaml.Yaml;

@Mojo(
      name = "module-info-generator",
      defaultPhase = LifecyclePhase.VERIFY,
      requiresDependencyResolution = ResolutionScope.COMPILE,
      threadSafe = true)
public class ModuleInfoGeneratorMojo extends AbstractMojo {
   @Parameter(defaultValue = "false")
   public boolean skip;

   @Parameter(defaultValue = "${project.build.sourceDirectory}/module-info.yml")
   public File yamlFile;

   @Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
   public File outputDirectory;


   public void execute() throws MojoExecutionException {
      try {
         if (skip) {
            getLog().info("Skipping module-info.class generation");
            return;
         }
         ModuleInfoYml moduleInfo;
         try (InputStream is = Files.newInputStream(yamlFile.toPath(), StandardOpenOption.READ)) {
            Yaml yaml = new Yaml();
            moduleInfo = yaml.loadAs(is, ModuleInfoYml.class);
         }
         CPool pool = new CPool();
         Files.createDirectories(outputDirectory.toPath());
         Path output = outputDirectory.toPath().resolve("module-info.class");
         try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(output))) {
            dos.writeInt(0xCAFEBABE); //magic
            dos.writeShort(0); //minor
            dos.writeShort(53); //major, Java 9
            CPool.CpEntry.ClassEntry classEntry = pool.addClass("module-info");
            CPool.CpEntry.Utf8Entry sourceFile = pool.addUtf8("SourceFile");
            CPool.CpEntry.Utf8Entry mInfo = pool.addUtf8("module-info.java");
            CPool.CpEntry.Utf8Entry module = pool.addUtf8("Module");
            CPool.CpEntry.ModuleEntry moduleEntry = pool.addModule(moduleInfo.getName());
            for (ModuleRequire require : moduleInfo.getRequires()) {
               pool.addModule(require.getModule());
               if (require.getVersion() != null) {
                  pool.addUtf8(require.getVersion());
               }
            }
            for (ModuleExport export : moduleInfo.getExports()) {
               pool.addPackage(export.getPackage());
               for (String to : export.getTo()) {
                  pool.addModule(to);
               }
            }
            for (ModuleExport opens : moduleInfo.getOpens()) {
               pool.addPackage(opens.getPackage());
               for (String to : opens.getTo()) {
                  pool.addModule(to);
               }
            }
            for (String uses : moduleInfo.getUses()) {
               pool.addClass(uses);
            }
            for (ModuleProvide provide : moduleInfo.getProvides()) {
               pool.addClass(provide.getServiceType());
               for (String with : provide.getWith()) {
                  pool.addClass(with);
               }
            }
            pool.write(dos);
            dos.writeShort(0x8000); // access flags
            dos.writeShort(classEntry.index()); // this class
            dos.writeShort(0); // super class: a module doesn't have one
            dos.writeShort(0); // interface count: a module has no interface
            dos.writeShort(0); // fields count: a module has no fields
            dos.writeShort(0); // methods count: a module has no methods
            dos.writeShort(0x2); // attributes count: SourceFile and Module
            // SourceFile Attribute
            dos.writeShort(sourceFile.index());
            dos.writeInt(2); // sourcefile attribute length is 2
            dos.writeShort(mInfo.index());
            // Module Attribute
            dos.writeShort(module.index());
            int length = 16;
            length += moduleInfo.getRequires().size() * 6;
            for (ModuleExport e : moduleInfo.getExports()) {
               length += 6 + e.getTo().size() * 2;
            }

            for (ModuleExport e : moduleInfo.getOpens()) {
               length += 6 + e.getTo().size() * 2;
            }

            length += moduleInfo.getUses().size() * 2;

            for (ModuleProvide p : moduleInfo.getProvides()) {
               length += 4 + p.getWith().size() * 2;
            }

            dos.writeInt(length); // attribute length

            dos.writeShort(moduleEntry.index());
            dos.writeShort(0); // module flags
            dos.writeShort(moduleInfo.getVersion() == null ? 0 : pool.getUtf8(moduleInfo.getVersion()).index()); // module version index. Only if we've written the version from moduleInfo
            dos.writeShort(moduleInfo.getRequires().size());
            for (ModuleRequire require : moduleInfo.getRequires()) {
               dos.writeShort(pool.addModule(require.getModule()).index());
               dos.writeShort(require.flags());
               dos.writeShort(require.getVersion() == null ? 0 : pool.getUtf8(require.getVersion()).index());
            }

            dos.writeShort(moduleInfo.getExports().size());
            for (ModuleExport export : moduleInfo.getExports()) {
               dos.writeShort(pool.addPackage(export.getPackage()).index());
               dos.writeShort(export.flags());
               dos.writeShort(export.getTo().size());
               for (String to : export.getTo()) {
                  dos.writeShort(pool.addModule(to).index());
               }
            }

            dos.writeShort(moduleInfo.getOpens().size());
            for (ModuleExport open : moduleInfo.getOpens()) {
               dos.writeShort(pool.addPackage(open.getPackage()).index());
               dos.writeShort(open.flags());
               dos.writeShort(open.getTo().size());
               for (String to : open.getTo()) {
                  dos.writeShort(pool.addModule(to).index());
               }
            }

            dos.writeShort(moduleInfo.getUses().size());
            for (String uses : moduleInfo.getUses()) {
               dos.writeShort(pool.addClass(uses).index());
            }

            dos.writeShort(moduleInfo.getProvides().size());
            for (ModuleProvide p : moduleInfo.getProvides()) {
               dos.writeShort(pool.addClass(p.getServiceType()).index());
               dos.writeShort(p.getWith().size());
               for (String w : p.getWith()) {
                  dos.writeShort(pool.addClass(w).index());
               }
            }

         }

      } catch (Exception e) {
         throw new MojoExecutionException("An error occurred while generating module-info.class", e);
      }
   }
}
