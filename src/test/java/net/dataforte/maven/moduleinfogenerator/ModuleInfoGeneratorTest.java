package net.dataforte.maven.moduleinfogenerator;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Test;

public class ModuleInfoGeneratorTest {

   @Test
   public void testSadCafe() throws URISyntaxException, MojoExecutionException {
      ModuleInfoGeneratorMojo mojo = new ModuleInfoGeneratorMojo();
      mojo.yamlFile = new File(this.getClass().getClassLoader().getResource("module-info.yml").toURI());
      Properties properties = System.getProperties();
      mojo.outputDirectory = Paths.get("target").toFile();
      mojo.execute();
   }
}
