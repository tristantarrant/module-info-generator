package net.dataforte.maven.moduleinfogenerator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ModuleProvide {
   private String serviceType;
   private List<String> with = new ArrayList<>();

   public String getServiceType() {
      return serviceType;
   }

   public void setServiceType(final String serviceType) {
      this.serviceType = serviceType;
   }

   public List<String> getWith() {
      return with;
   }

   public void setWith(final List<String> with) {
      this.with = with;
   }
}
