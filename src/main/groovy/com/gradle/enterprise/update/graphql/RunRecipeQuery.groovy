package com.gradle.enterprise.update.graphql

class RunRecipeQuery implements Query {

    def static queryStr = '''
    mutation executeRecipe($repositoryFilter: [RepositoryInput!], $yaml: Base64!) {
        runYamlRecipe(repositoryFilter: $repositoryFilter, yaml: $yaml) {
            id
        }
    }'''

    static Query create(String recipeYaml, Object repos) {
        return new RunRecipeQuery(query: queryStr,
            variables: ['yaml': recipeYaml.bytes.encodeBase64().toString(), 'repositoryFilter': repos],
            operationName: 'executeRecipe')
    }

}
