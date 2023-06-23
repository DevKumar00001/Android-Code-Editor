package android.code.editor.utils;

import android.code.editor.R;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import java.io.InputStream;

public class GitCloner {

    public Context context;
    public GitCloneHandler gitCloneHandler;
    public boolean setUseCredentialsProvider;

    public GitCloner(String path, Context context) {
        this.context = context;

        MaterialAlertDialogBuilder GitCloneDialog = new MaterialAlertDialogBuilder(context);
        GitCloneDialog.setTitle("Clone a repository");
        LayoutInflater layoutGitClone =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gitDialogLayout = layoutGitClone.inflate(R.layout.git_clone_dialog, null);
        
        ((TextInputEditText) gitDialogLayout.findViewById(R.id.repository_name)).setHint("Enter repository name");
        ((TextInputEditText) gitDialogLayout.findViewById(R.id.repository_url)).setHint("Enter repository URL");
        ((TextInputEditText) gitDialogLayout.findViewById(R.id.username)).setHint("Enter username");
        ((TextInputEditText) gitDialogLayout.findViewById(R.id.password)).setHint("Enter password");

        ((TextInputEditText) gitDialogLayout.findViewById(R.id.repository_url)).setSingleLine();
        ((TextInputEditText) gitDialogLayout.findViewById(R.id.repository_name)).setSingleLine();
        ((TextInputEditText) gitDialogLayout.findViewById(R.id.username)).setSingleLine();
        ((TextInputEditText) gitDialogLayout.findViewById(R.id.password)).setSingleLine();

        ((TextInputEditText) gitDialogLayout.findViewById(R.id.username))
                .addTextChangedListener(
                        new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable arg0) {
                                // TODO: Implement this method
                            }

                            @Override
                            public void onTextChanged(
                                    CharSequence arg0, int arg1, int arg2, int arg3) {
                                // TODO: Implement this method
                                if (!(arg0.length() == 0)) {
                                    if (((TextInputEditText)gitDialogLayout.findViewById(R.id.password)).getText().length() == 0) {
                                        setUseCredentialsProvider = false;
                                    } else {
                                        setUseCredentialsProvider = true;
                                    }
                                } else {
                                    setUseCredentialsProvider = false;
                                }
                            }

                            @Override
                            public void beforeTextChanged(
                                    CharSequence arg0, int arg1, int arg2, int arg3) {
                                // TODO: Implement this method
                            }
                        });

        ((TextInputEditText) gitDialogLayout.findViewById(R.id.password))
                .addTextChangedListener(
                        new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable arg0) {
                                // TODO: Implement this method
                            }

                            @Override
                            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                                // TODO: Implement this method
                                if (!(arg0.length() == 0)) {
                                    if (((TextInputEditText)gitDialogLayout.findViewById(R.id.username)).getText().length() == 0) {
                                        setUseCredentialsProvider = false;
                                    } else {
                                        setUseCredentialsProvider = true;
                                    }
                                } else {
                                    setUseCredentialsProvider = false;
                                }
                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                                // TODO: Implement this method
                            }
                        });

        ((Button) gitDialogLayout.findViewById(R.id.cancel))
                .setOnClickListener(
                        (v) -> {
                            GitCloneDialog.create().dismiss();
                        });
        ((Button) gitDialogLayout.findViewById(R.id.clone))
                .setOnClickListener(
                        (v) -> {
                            if (((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_url)).getText().toString().length() == 0) {
                                ((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_url)).setError("Enter a repository url");
                            } else {
                                ((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_url)).setError(null);
                                if (((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_name)).getText().toString().length() == 0) {
                                    ((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_name)).setError("Enter a repository name");
                                } else {
                                    if (new File(path.concat(File.separator).concat(((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_name)).getText().toString())).exists()) {
                                        ((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_name)).setError("File path already exists");
                                    } else {
                                        gitCloneHandler = new GitCloneHandler();
                                        // Toast.makeText(context,path.concat(File.separator).concat(((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_name)).getText().toString()),Toast.LENGTH_LONG).show();
                                        gitCloneHandler.cloneRepository(((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_url)).getText().toString(),new File(path.concat(File.separator).concat(((TextInputEditText)gitDialogLayout.findViewById(R.id.repository_name)).getText().toString())));
                                        gitCloneHandler.setCredentials(((TextInputEditText) gitDialogLayout.findViewById(R.id.username)).getText().toString()/*,((TextInputEditText) gitDialogLayout.findViewById(R.id.password)).toString().toString()*/);
                                        gitCloneHandler.setGitCallback(new GitCloneHandler.GitCallback() {
                                    
                                            @Override
                                            public void onProgressComplete(boolean isSuccess, String info) {
                                                // Toast.makeText(context,String.valueOf(isSuccess).concat(" ").concat(info),Toast.LENGTH_LONG).show();
                                                Log.e("Git Hunt",String.valueOf(isSuccess).concat(" ").concat(info));
                                            }
                                                                                                             
                                            @Override
                                            public void onProgress(int progress) {
                                                ((TextView)gitDialogLayout.findViewById(R.id.progress)).setText(Integer.toString(progress));
                                            }
                                        });
                                        gitCloneHandler.execute();
                                    }
                                }
                            }
                        });

        GitCloneDialog.setView(gitDialogLayout);
        GitCloneDialog.create().show();
    }
}
