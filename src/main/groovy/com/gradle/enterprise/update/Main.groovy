package com.gradle.enterprise.update

import groovy.cli.picocli.CliBuilder

def token = System.getenv('MODERNE_TOKEN')

def cli = new CliBuilder(name:'moderne')
cli.usageMessage.customSynopsis('Executes Moderne/Openrewrite recipes on given repos. Env variable MODERNE_TOKEN should be set with a valid token')
cli.repos(type:File, argName:'repos', 'Repositories to execute the recipe on as a path to a file containing one line per repo.')
cli.recipe(type:File, argName:'recipe', 'Openrewrite/Moderne recipe to execute, as a path to a YAML file.')
def options = cli.parse(args)
if (!options?.recipe || !options?.repos || !token) {
    cli.usage()
} else {
    def recipe = options.recipe.text
    def repos = options.repos.text.split('\n') as List

    new RunUpdate(token, repos, 'upgrade-ge-plugin-again', recipe, 'Update GE again').run()
}
