package se.kth.castor.wiwu.experiments;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lib {

    List<Version> versions;

    public Lib(List<Version> versions) {
        this.versions = versions;
    }

    public Set<String> alwaysBloatedClasses() {
        Set<String> result = versions.get(0).getReport().getBloatedClasses();
        for (int i = 1; i < versions.size(); i++) {
            Set<String> current = versions.get(i).getReport().getBloatedClasses();
            result = Sets.intersection(result, current);
        }
        return result;
    }

    public Set<String> alwaysBloatedMethods() {
        Set<String> result = versions.get(0).getReport().getBloatedMethods();
        for (int i = 1; i < versions.size(); i++) {
            Set<String> current = versions.get(i).getReport().getBloatedMethods();
            result = Sets.intersection(result, current);
        }
        return result;
    }

    public Set<String> alwaysUsedClasses() {
        Set<String> result = versions.get(0).getReport().getUsedClasses();
        for (int i = 1; i < versions.size(); i++) {
            Set<String> current = versions.get(i).getReport().getUsedClasses();
            result = Sets.intersection(result, current);
        }
        return result;
    }

    public Set<String> alwaysUsedMethods() {
        Set<String> result = versions.get(0).getReport().getUsedMethods();
        for (int i = 1; i < versions.size(); i++) {
            Set<String> current = versions.get(i).getReport().getUsedMethods();
            result = Sets.intersection(result, current);
        }
        return result;
    }

    public Set<String> usedThenAlwaysBloatedClasses() {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < versions.size() - 1; i++) {
            Set<String> usedClasses = versions.get(i).getReport().getUsedClasses();
            for (String usedClass : usedClasses) {
                if (isAlwaysBloatedClass(usedClass, versions.subList(i + 1, versions.size()))) {
                    result.add(usedClass);
                }
            }
        }
        return result;
    }

    public Set<String> bloatedThenAlwaysUsedClasses() {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < versions.size() - 1; i++) {
            Set<String> bloatedClasses = versions.get(i).getReport().getBloatedClasses();
            for (String bloatedClass : bloatedClasses) {
                if (isAlwaysUsedClass(bloatedClass, versions.subList(i + 1, versions.size()))) {
                    result.add(bloatedClass);
                }
            }
        }
        return result;
    }

    public Set<String> usedThenAlwaysBloatedMethods() {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < versions.size() - 1; i++) {
            Set<String> usedMethods = versions.get(i).getReport().getUsedMethods();
            for (String usedMethod : usedMethods) {
                if (isAlwaysBloatedMethod(usedMethod, versions.subList(i + 1, versions.size()))) {
                    result.add(usedMethod);
                }
            }
        }
        return result;
    }

    public Set<String> bloatedThenAlwaysUsedMethods() {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < versions.size() - 1; i++) {
            Set<String> bloatedMethods = versions.get(i).getReport().getBloatedMethods();
            for (String bloatedMethod : bloatedMethods) {
                if (isAlwaysUsedMethod(bloatedMethod, versions.subList(i + 1, versions.size()))) {
                    result.add(bloatedMethod);
                }
            }
        }
        return result;
    }

    public Set<String> oscillateClasses() {
        Set<String> result = new HashSet<>();
        Set<String> usedClasses = versions.get(0).getReport().getUsedClasses();
        for (String usedClass : usedClasses) {
            if (nbBloatedHitsOfClass(usedClass, versions.subList(1, versions.size())) > 1 &&
                    nbUsedHitsOfClass(usedClass, versions.subList(1, versions.size())) > 1) {
                result.add(usedClass);
            }
        }
        return result;
    }

    public Set<String> oscillateMethods() {
        Set<String> result = new HashSet<>();
        Set<String> usedMethods = versions.get(0).getReport().getUsedMethods();
        for (String usedMethod : usedMethods) {
            if (nbBloatedHitsOfMethod(usedMethod, versions.subList(1, versions.size())) > 1 &&
                    nbUsedHitsOfMethod(usedMethod, versions.subList(1, versions.size())) > 1) {
                result.add(usedMethod);
            }
        }
        return result;
    }

    private boolean isAlwaysBloatedClass(String clazz, List<Version> theVersions) {
        for (Version theVersion : theVersions) {
            if (!theVersion.getReport().getBloatedClasses().contains(clazz)) {
                return false;
            }
        }
        return true;
    }

    private int nbBloatedHitsOfClass(String clazz, List<Version> theVersions) {
        int hits = 0;
        for (Version theVersion : theVersions) {
            if (!theVersion.getReport().getBloatedClasses().contains(clazz)) {
                hits++;
            }
        }
        return hits;
    }

    private int nbUsedHitsOfClass(String clazz, List<Version> theVersions) {
        int hits = 0;
        for (Version theVersion : theVersions) {
            if (!theVersion.getReport().getUsedClasses().contains(clazz)) {
                hits++;
            }
        }
        return hits;
    }

    private int nbUsedHitsOfMethod(String method, List<Version> theVersions) {
        int hits = 0;
        for (Version theVersion : theVersions) {
            if (!theVersion.getReport().getUsedMethods().contains(method)) {
                hits++;
            }
        }
        return hits;
    }

    private int nbBloatedHitsOfMethod(String method, List<Version> theVersions) {
        int hits = 0;
        for (Version theVersion : theVersions) {
            if (!theVersion.getReport().getBloatedMethods().contains(method)) {
                hits++;
            }
        }
        return hits;
    }

    private boolean isAlwaysUsedClass(String clazz, List<Version> theVersions) {
        for (Version theVersion : theVersions) {
            if (!theVersion.getReport().getUsedClasses().contains(clazz)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAlwaysBloatedMethod(String method, List<Version> theVersions) {
        for (Version theVersion : theVersions) {
            if (!theVersion.getReport().getBloatedMethods().contains(method)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAlwaysUsedMethod(String method, List<Version> theVersions) {
        for (Version theVersion : theVersions) {
            if (!theVersion.getReport().getUsedMethods().contains(method)) {
                return false;
            }
        }
        return true;
    }
}
