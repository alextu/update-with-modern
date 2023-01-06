package com.gradle.enterprise.update

import com.gradle.enterprise.update.graphql.*

import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

class RunUpdate {

    Executor executor = Executors.newSingleThreadExecutor()
    Client client
    List<String> repos
    String recipeYaml
    String branchName
    String commitMessage

    RunUpdate(String token, List<String> repos, String branchName, String recipeYaml, String commitMessage) {
        this.client = new Client(token)
        this.repos = repos
        this.recipeYaml = recipeYaml
        this.branchName = branchName
        this.commitMessage = commitMessage
    }

    void run() {
        try {
            def runResult = waitForRunCompletion(runRecipe(repos, recipeYaml)).orTimeout(5, TimeUnit.MINUTES).get()
            if (runResult.status == 'ERROR') {
                throw new RuntimeException("Recipe run '${runResult.id}' has finished with an error")
            }

            def repositoriesReport = getRepositories(runResult)
            println repositoriesReport.toDisplayable()
            def modifiedRepos = repositoriesReport.repositories.stream()
                .filter { it.status == 'FINISHED' }
                .collect(Collectors.toUnmodifiableList())

            if (!modifiedRepos.isEmpty()) {
                def prId = submitPullRequest(runResult.id, modifiedRepos)
                def commitJobReport = waitForCommitCompletion(prId).orTimeout(5, TimeUnit.MINUTES).get()
                println commitJobReport.toDisplayable()
            }
        } finally {
            executor.shutdown()
        }
    }

    String runRecipe(List<String> repos, String recipeYaml) {
        def githubRepos = repos.stream()
            .map { ["branch": "main", "origin": "github.com", "path": it] }
            .collect(Collectors.toUnmodifiableList())
        def json = client.request(RunRecipeQuery.create(recipeYaml, githubRepos))
        return json.data.runYamlRecipe.id
    }

    CompletableFuture<RecipeRun> waitForRunCompletion(String recipeRunId) {
        waitForCompletion({ queryRecipeRunStatus(recipeRunId) },
            { runStatus -> runStatus.status in ['FINISHED', 'ERROR'] })
    }

    RecipeRun queryRecipeRunStatus(String recipeRunId) {
        def json = client.request(RecipeRunStatusQuery.create(recipeRunId))
        return new RecipeRun(id: recipeRunId, status: json.data.recipeRun.state)
    }

    RepositoriesReport getRepositories(RecipeRun recipeRun) {
        def json = client.request(RepositoriesQuery.create(recipeRun.id))
        def repos = json.data.recipeRun.summaryResultsPages.edges.stream()
            .map {
                new Repository(origin: it.node.repository.origin, path: it.node.repository.path,
                    branch: it.node.repository.branch, status: it.node.state)
            }
            .collect(Collectors.toUnmodifiableList())
        return new RepositoriesReport(repositories: repos)
    }

    String submitPullRequest(String recipeRunId, List<Repository> repositories) {
        def json = client.request(PullRequestQuery.create(recipeRunId, branchName, repositories, commitMessage))
        return json.data.pullRequest.id
    }

    CompletableFuture<CommitJobReport> waitForCommitCompletion(String prId) {
        waitForCompletion({ queryCommitJob(prId) }, { jobReport -> jobReport.completed in [1, 2] })
    }

    <T> CompletableFuture waitForCompletion(Closure<T> query, Closure<Boolean> stopCondition) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>()
        executor.submit({
            try {
                T result = query()
                while (!stopCondition(result) && !completableFuture.isDone()) {
                    Thread.sleep(Duration.ofSeconds(5).toMillis())
                    result = query()
                }
                completableFuture.complete(result)
            } catch (Throwable t) {
                completableFuture.completeExceptionally(t)
            }
        })
        return completableFuture
    }


    CommitJobReport queryCommitJob(String prId) {
        def json = client.request(CommitJobQuery.create(prId))
        def commits = json.data.commitJob.commits.edges.stream().map({ new CommitJob(state: it.node.state, resultLink: it.node.resultLink) }).collect(Collectors.toUnmodifiableList())
        return new CommitJobReport(completed: json.data.commitJob.completed, summaryResult: new SummaryResult(json.data.commitJob.summaryResults), commits: commits)
    }
}
