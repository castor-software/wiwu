package se.kth.castor.wiwu;

import org.eclipse.jgit.lib.ProgressMonitor;

class CloneProgressMonitor implements ProgressMonitor {
    @Override
    public void start(int totalTasks) {
        System.out.println("Starting work on " + totalTasks + " tasks");
    }

    @Override
    public void beginTask(String title, int totalWork) {
        System.out.println("Start " + title + ": " + totalWork);
    }

    @Override
    public void update(int completed) {
    }

    @Override
    public void endTask() {
        System.out.println("Done!");
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
