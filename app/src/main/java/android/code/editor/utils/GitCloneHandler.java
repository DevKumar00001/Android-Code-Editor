package android.code.editor.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GitCloneHandler {
    private GitCallback gitCallback;
    private File file;
    private String url = "";
    private String username = "";
    // private String password = "";
    public boolean setUseCredentialsProvider = true;

    public GitCloneHandler() {
        
    }

    public void cloneRepository(String url, File file) {
        this.url = url;
        this.file = file;
        /* ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            clone = Git.cloneRepository();
                            clone.setURI(url);
                            clone.setDirectory(file);
                            clone.setBare(false);
                            clone.setCloneAllBranches(true);
                            clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
                            clone.setProgressMonitor(
                                    new ProgressMonitor() {

                                        @Override
                                        public void start(int arg0) {}

                                        @Override
                                        public void beginTask(String arg0, int arg1) {}

                                        @Override
                                        public void update(int arg0) {
                                            gitCallback.onProgress(arg0);
                                        }

                                        @Override
                                        public void endTask() {}

                                        @Override
                                        public boolean isCancelled() {
                                            return false;
                                        }

                                        @Override
                                        public void showDuration(boolean arg0) {}
                                    });
                            clone.call();
                            gitCallback.onProgressComplete(true, "Download complete");
                        } catch (GitAPIException | JGitInternalException e) {
                            // Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
                            gitCallback.onProgressComplete(false, e.getMessage());
                        }
                    }
                });*/
    }

    public void setGitCallback(GitCallback gitCallback) {
        this.gitCallback = gitCallback;
        
    }
    
    public void setCredentials(String username/* ,String password*/ ) {
        this.username = username;
        // this.password = password;
    }

    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("GitDebug",file.getAbsolutePath());
                            Log.e("GitDebug",username);
                            // Log.e("GitDebug",password);
                            Log.e("GitDebug",url);
                            CloneCommand clone = Git.cloneRepository();
                            clone.setURI(url);
                            clone.setDirectory(file);
                            clone.setBare(false);
                            clone.setCloneAllBranches(true);
                            if (setUseCredentialsProvider) {
                                // clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
                            }
                            clone.setProgressMonitor(
                                new ProgressMonitor() {

                                    @Override
                                     public void start(int arg0) {}

                                     @Override
                                     public void beginTask(String arg0, int arg1) {}

                                     @Override
                                     public void update(int arg0) {
                                         gitCallback.onProgress(arg0);
                                     }

                                     @Override
                                     public void endTask() {}

                                     @Override
                                     public boolean isCancelled() {
                                         return false;
                                     }

                                     @Override
                                     public void showDuration(boolean arg0) {}
                            });
                            clone.call();
                            gitCallback.onProgressComplete(true, "Download complete");
                        } catch (GitAPIException | JGitInternalException e) {
                            gitCallback.onProgressComplete(false, e.getMessage());
                        }
                    }
                });
    }

    public interface GitCallback {
        public void onProgressComplete(boolean isSuccess, String info);

        public void onProgress(int progress);
    }
}
