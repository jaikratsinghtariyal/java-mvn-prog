package com.java.yaml.utility;

import com.java.yaml.parser.YAMLParserService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class GITOpsUtility {

    public void pushBranch(Git git) throws GitAPIException, URISyntaxException, IOException {

        /**
         *   Code for checkin in existing repo.
         */

        /*RemoteAddCommand remoteAddCommand = git.remoteAdd();
        remoteAddCommand.setName("origin");
        remoteAddCommand.setUri(new URIish("https://github.com/jaikratsinghtariyal/spring-mule-hello.git"));
        remoteAddCommand.call();

        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("jaikratsinghtariyal", "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"));
        pushCommand.call();*/

        /**
         *   Code for checkin in new repo.
         */
        /*StoredConfig config = git.getRepository().getConfig();
        config.setString("remote", "origin", "url", "https://github.com/jaikratsinghtariyal/spring-mule-hello.git");
        //config.setString("remote", "origin", "spring-mule-hello", "git@github.com:jaikratsinghtariyal/spring-mule-hello.git");
        config.save();*/

        /*RemoteAddCommand remoteAddCommand = git.remoteAdd();
        remoteAddCommand.setName("origin");
        remoteAddCommand.setUri(new URIish("https://github.com/jaikratsinghtariyal/spring-mule-hello.git"));
        remoteAddCommand.call();*/

        git.add()
                .addFilepattern(".")
                .call();

        git.commit()
                .setMessage("(Initial Commit)")
                .setAuthor("Jaikrat", "Jaikrat@maik.com")
                .call();

        // git push -u origin master
        PushCommand pushCommand = git.push().setForce(true);
        pushCommand.add("master");
        pushCommand.setRemote("origin");
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("jaikratsinghtariyal", "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"));

        pushCommand.call();
    }

    public void createNewBranch(Git git) throws GitAPIException {
        git.checkout()
                .setCreateBranch(true)
                .setName("new-branch")
                .call();
    }

    public Git gitInit(String path) throws GitAPIException {
        //File dir = File.createTempFile("gitinit", ".test");
        File dir = new File(path);
        return Git.init()
                .setDirectory(dir)
                .call();
    }

    public void cloneRepo(String gitLink, String apiName, String repoClonePath) throws GitAPIException {
        //File file = new File("/Users/ja20105259/projects/mule-sample-repos/".concat(apiName));
        File file = new File(repoClonePath.concat("/").concat(apiName));
        ApplicationUtility.deleteDirectory(file);

        /**
         * For Default Branch
         */

        Git.cloneRepository()
                .setURI(gitLink)
                .setDirectory(file)
                .call();

        /**
         * For specific Branch
         */
        /*Git.cloneRepository()
                .setURI("https://github.com/eclipse/jgit.git")
                .setDirectory(new File("/path/to/targetdirectory"))
                .setBranchesToClone(Arrays.asList("refs/heads/specific-branch"))
                .setBranch("refs/heads/specific-branch")
                .call();*/
    }


}
