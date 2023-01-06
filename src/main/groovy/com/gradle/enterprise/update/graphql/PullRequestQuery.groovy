package com.gradle.enterprise.update.graphql

import com.gradle.enterprise.update.Repository

import java.util.stream.Collectors

class PullRequestQuery {

    static String queryStr = '''
mutation pullRequest(
  $commitInput: CommitInput!
  $pullRequestTitle: String
  $pullRequestBody: Base64
  $isDraft: Boolean! = false
) {
  pullRequest(
    commit: $commitInput
    pullRequestTitle: $pullRequestTitle
    pullRequestBody: $pullRequestBody
    draft: $isDraft
  ) {
    id
    started
    email
    completed
    summaryResults {
      count
      successfulCount
      failedCount
      noChangeCount
    }
  }
}
'''

    static Query create(String recipeRunId, String branchName, List<Repository> repos, String commitMessage) {
        def variables = [
            'isDraft'    : false,
            'commitInput': [
                'recipeRunId' : recipeRunId,
                'branchName'  : branchName,
                'message'     : commitMessage,
                'repositories': repos.stream().map { ['branch': it.branch, 'origin': it.origin, 'path': it.path] }.collect(Collectors.toUnmodifiableList())
            ],
        ]
        return new RecipeRunStatusQuery(query: queryStr, variables: variables, operationName: 'pullRequest')
    }
}
