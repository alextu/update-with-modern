package com.gradle.enterprise.update.graphql

import groovy.json.JsonOutput
import groovy.transform.TupleConstructor

@TupleConstructor
trait Query {
    String query
    Map<String, Object> variables
    String operationName

    String toJson() {
        """
        {
            "query": "${query.replaceAll("\n", "\\\\n")}",
            "variables": ${JsonOutput.toJson(variables)},
            "operationName":"${operationName}"
        }"""
    }
}
