package com.gradle.enterprise.update.graphql

class RepositoriesQuery implements Query {

    def static queryStr = '''
query selectAllRepositoriesWithResults($id: ID!, $first: Int, $after: String) {
  recipeRun(id: $id) {
    summaryResultsPages(
      first: $first
      after: $after
      filterBy: { onlyWithResults: true }
    ) {
      count
      pageInfo {
        hasNextPage
        endCursor
      }
      edges {
        node {
          repository {
            __typename
            origin
            path
            branch
          }
          state
        }
      }
    }
  }
}
'''

    static Query create(String recipeRunId) {
        return new RepositoriesQuery(query: queryStr, variables: ['id': recipeRunId], operationName: 'selectAllRepositoriesWithResults')
    }

}
