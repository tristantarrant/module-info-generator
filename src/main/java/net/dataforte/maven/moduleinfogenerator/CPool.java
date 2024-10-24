package net.dataforte.maven.moduleinfogenerator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CPool {
   private final List<CpEntry> entries = new ArrayList<>();
   private final Map<String, CpEntry.Utf8Entry> names = new HashMap<>();
   private final Map<String, CpEntry.ClassEntry> classes = new HashMap<>();
   private final Map<String, CpEntry.ModuleEntry> modules = new HashMap<>();
   private final Map<String, CpEntry.PackageEntry> packages = new HashMap<>();

   CpEntry.Utf8Entry getUtf8(String name) {
      return names.computeIfAbsent(name, CpEntry.Utf8Entry::new);
   }

   private <T extends CpEntry> T add(T entry) {
      if (entry.index == 0) {
         entries.add(entry);
         entry.index = entries.size();
      }
      return entry;
   }

   public CpEntry.ClassEntry addClass(String name) {
      return classes.computeIfAbsent(nameToPath(name), n -> add(new CpEntry.ClassEntry(getUtf8(n))));
   }

   public CpEntry.PackageEntry addPackage(String name) {
      return packages.computeIfAbsent(nameToPath(name), n -> add(new CpEntry.PackageEntry(getUtf8(n))));
   }


   public CpEntry.ModuleEntry addModule(String name) {
      return modules.computeIfAbsent(name, n -> add(new CpEntry.ModuleEntry(getUtf8(n))));
   }

   public CpEntry.Utf8Entry addUtf8(String name) {
      return add(getUtf8(name));
   }

   public void write(DataOutputStream dos) throws IOException {
      int currentSize = entries.size();
      // Add all the missing utf8 names
      for (int i = 0; i < currentSize; i++) {
         CpEntry entry = entries.get(i);
         entry.addToPool(this);
      }
      dos.writeShort(entries.size() + 1); // constant_pool_count
      for (CpEntry entry : entries) {
         entry.write(dos);
      }
   }

   private static String nameToPath(String name) {
      return name.replaceAll("\\.", "/");
   }

   public static abstract class CpEntry {
      protected final byte tag;
      protected int index = 0;

      protected CpEntry(byte tag) {
         this.tag = tag;
      }

      public int index() {
         return index;
      }

      abstract void addToPool(CPool pool);

      abstract void write(DataOutputStream dos) throws IOException;

      public static class Utf8Entry extends CpEntry {
         private final String name;

         private Utf8Entry(String name) {
            super((byte) 1);
            this.name = name;
         }

         public String value() {
            return name;
         }

         @Override
         void addToPool(CPool pool) {
            // Do nothing
         }

         @Override
         void write(DataOutputStream dos) throws IOException {
            dos.writeByte(this.tag);
            byte[] bytes = name.getBytes(StandardCharsets.UTF_8);
            dos.writeShort(bytes.length);
            dos.write(bytes);
         }
      }

      public static abstract class NamedEntry extends CpEntry {
         private final Utf8Entry name;

         protected NamedEntry(byte tag, Utf8Entry name) {
            super(tag);
            this.name = name;
         }

         public Utf8Entry name() {
            return name;
         }

         @Override
         void addToPool(CPool pool) {
            pool.add(name);
         }

         @Override
         void write(DataOutputStream dos) throws IOException {
            dos.writeByte(this.tag);
            dos.writeShort(name.index);
         }
      }

      public static class ClassEntry extends NamedEntry {
         protected ClassEntry(Utf8Entry name) {
            super((byte) 7, name);
         }
      }

      public static class PackageEntry extends NamedEntry {
         protected PackageEntry(Utf8Entry name) {
            super((byte) 20, name);
         }
      }

      public static class ModuleEntry extends NamedEntry {
         protected ModuleEntry(Utf8Entry name) {
            super((byte) 19, name);
         }
      }
   }
}