package com.gradle.enterprise.update.graphql

class CommitJobQuery implements Query {

    static String queryStr = '''
query commitJob(
  $id: ID!
  $first: Int = 50
  $after: String
  $filterBy: CommitJobFilterInput
  $orderBy: CommitJobOrderInput
) {
  commitJob(id: $id) {
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
    recipeRunId
    message
    extendedMessage
    options {
      ... on PullRequestOptions {
        branchName
        draft
        pullRequestBody
        pullRequestTitle
      }
    }
    started
    commits(
      first: $first
      after: $after
      filterBy: $filterBy
      orderBy: $orderBy
    ) {
      pageInfo {
        hasNextPage
        endCursor
      }
      count
      edges {
        node {
          state
          stateMessage
          modified
          repository {
            origin
            path
            branch
            ... on GitHubRepository {
              organization
              name
              ingested
            }
          }
          resultLink
        }
      }
    }
  }
}
'''

    static Query create(String prId) {
        def variables = [
            'first': 50,
            'id'   : prId
        ]
        return new CommitJobQuery(query: queryStr, variables: variables, operationName: 'commitJob')
    }

}
