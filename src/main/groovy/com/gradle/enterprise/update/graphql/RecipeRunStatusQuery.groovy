package com.gradle.enterprise.update.graphql

class RecipeRunStatusQuery implements Query {
    def static queryStr = '''
query runRecipeStatus($id: ID!) {
  recipeRun(id: $id) {
    recipe {
      id
      name
    }
    state
  }
}'''

    static Query create(String recipeRunId) {
        return new RecipeRunStatusQuery(query: queryStr, variables: ['id': recipeRunId], operationName: 'runRecipeStatus')
    }
}
