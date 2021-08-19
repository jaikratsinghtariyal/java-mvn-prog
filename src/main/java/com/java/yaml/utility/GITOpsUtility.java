package com.java.yaml.utility;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.net.URISyntaxException;

public class GITOpsUtility {
    public void pushNewBranch(Git git) throws GitAPIException, URISyntaxException {

        RemoteAddCommand remoteAddCommand = git.remoteAdd();
        remoteAddCommand.setName("origin");
        remoteAddCommand.setUri(new URIish("https://github.com/jaikratsinghtariyal/mule-hello-world.git"));
        remoteAddCommand.call();

        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("tektutorialfeedback@gmail.com", "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"));
        pushCommand.call();
    }

    public void createNewBranch(Git git) throws GitAPIException {
        git.checkout()
                .setCreateBranch(true)
                .setName("new-branch")
                .call();
    }

    public Git cloneRepo() throws GitAPIException {

        String gitLink = "https://github.com/jaikratsinghtariyal/mule-hello-world.git";
        String apiName = gitLink.substring(gitLink.lastIndexOf("/") + 1, gitLink.lastIndexOf("."));

        File file = new File("/Users/ja20105259/projects/mule-sample-repos/".concat(apiName));
        ApplicationUtility.deleteDirectory(file);

        //For Default Branch
        Git git = Git.cloneRepository()
                .setURI(gitLink)
                .setDirectory(file)
                .call();

        //For specific Branch
        /*Git.cloneRepository()
                .setURI("https://github.com/eclipse/jgit.git")
                .setDirectory(new File("/path/to/targetdirectory"))
                .setBranchesToClone(Arrays.asList("refs/heads/specific-branch"))
                .setBranch("refs/heads/specific-branch")
                .call();*/
        return git;
    }


}
