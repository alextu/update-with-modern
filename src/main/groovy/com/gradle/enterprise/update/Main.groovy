package com.gradle.enterprise.update

def repos = [
//    'gradle/android-cache-fix-gradle-plugin',
//    'gradle/common-custom-user-data-gradle-plugin',
//    'gradle/common-custom-user-data-maven-extension',
//    'gradle/gradle-build-scan-quickstart',
//    'gradle/gradle-enterprise-build-validation',
//    'gradle/maven-build-scan-quickstart',
//    'gradle/wrapper-upgrade-gradle-plugin',
    'alextu/gradle-sample-1',
    'alextu/gradle-sample-2'
]

def recipe = '''
type: specs.openrewrite.org/v1beta/recipe
name: org.alextu.MyCustomRecipe
displayName: Upgrade all GE plugins
description: Fix all the things.
recipeList:
  - org.openrewrite.gradle.plugins.UpgradePluginVersion:
      pluginIdPattern: com.gradle.enterprise
      newVersion: 3.x
  - org.openrewrite.gradle.plugins.UpgradePluginVersion:
      pluginIdPattern: com.gradle.build-scan
      newVersion: 3.x
'''

def token = System.getenv('MODERNE_TOKEN')

new RunUpdate(token, repos, 'upgrade-ge-plugin', recipe, 'Update GE').run()
