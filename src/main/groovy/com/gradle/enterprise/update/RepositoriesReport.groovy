package com.gradle.enterprise.update

import groovy.transform.TupleConstructor

@TupleConstructor
class RepositoriesReport {
    List<Repository> repositories

    String toDisplayable() {
        def builder = new StringBuilder()
        def rowUtil = new DisplayRow(30, 10, builder)
        builder << 'Recipe run on repos status'.center(80, '-')
        builder << '\n'
        repositories.stream().forEach {
            rowUtil.row(it.path, it.status)
        }
        builder.toString()
    }
}
